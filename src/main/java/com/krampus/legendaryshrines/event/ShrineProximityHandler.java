package com.krampus.legendaryshrines.event;

import com.krampus.legendaryshrines.LegendaryShrines;
import com.krampus.legendaryshrines.block.ShrineBlock;
import com.krampus.legendaryshrines.config.ShrineConfig;
import com.krampus.legendaryshrines.data.ShrineBind;
import com.krampus.legendaryshrines.data.ShrineBinding;
import com.krampus.legendaryshrines.data.ShrineLocations;
import com.krampus.legendaryshrines.network.ModNetwork;
import com.krampus.legendaryshrines.registry.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LegendaryShrines.MOD_ID)
public final class ShrineProximityHandler {

    private static final int CHECK_INTERVAL = 20;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (!(event.player instanceof ServerPlayer player)) {
            return;
        }
        long floatUntil = ShrineBinding.getFloatUntil(player);
        if (floatUntil != 0L && player.level().getGameTime() >= floatUntil) {
            PlayerDataHandler.settle(player);
        }

        if (player.tickCount % CHECK_INTERVAL != 0) {
            return;
        }

        ServerLevel level = player.serverLevel();
        ShrineLocations locations = ShrineLocations.get(level);
        ShrineBind current = ShrineBinding.get(player);
        boolean sameDimension = current != null && current.dimension().equals(level.dimension());

        if (sameDimension && isShrineGone(level, current.pos())) {
            locations.remove(current.pos());
            ShrineBinding.clear(player);
            ModNetwork.syncBind(player, null, false);
            announceLost(player);
            current = null;
            sameDimension = false;
        }

        double breakDistance = ShrineConfig.BREAK_DISTANCE.get();

        if (sameDimension
                && breakDistance > 0.0D
                && current.distanceSqrTo(player.getX(), player.getY(), player.getZ())
                        > breakDistance * breakDistance) {
            ShrineBinding.clear(player);
            ModNetwork.syncBind(player, null, false);
            announceBreak(player);
            current = null;
        }

        if (ShrineConfig.BIND_ON_USE.get()) {
            return;
        }

        BlockPos nearest = locations.findNearest(player.getX(), player.getY(), player.getZ(), ShrineConfig.BIND_RADIUS.get());

        if (nearest != null && isShrineGone(level, nearest)) {
            locations.remove(nearest);
            nearest = null;
        }

        if (nearest == null) {
            ShrineBinding.clearSuppressed(player);
            return;
        }

        BlockPos suppressed = ShrineBinding.getSuppressed(player);
        if (suppressed != null) {
            if (suppressed.equals(nearest)) {
                return;
            }
            ShrineBinding.clearSuppressed(player);
        }

        if (current != null && current.dimension().equals(level.dimension()) && current.pos().equals(nearest)) {
            return;
        }

        bind(player, level, nearest);
    }

    public static void bind(ServerPlayer player, ServerLevel level, BlockPos pos) {
        ShrineBinding.clearSuppressed(player);
        ShrineBind bind = new ShrineBind(pos, level.dimension());
        ShrineBinding.set(player, bind);
        ModNetwork.syncBind(player, bind, true);
        announceBind(player, level, pos);
    }

    private static boolean isShrineGone(ServerLevel level, BlockPos pos) {
        if (!level.isLoaded(pos)) {
            return false;
        }

        BlockState state = level.getBlockState(pos);
        return !(state.getBlock() instanceof ShrineBlock)
                || state.getValue(ShrineBlock.HALF) != DoubleBlockHalf.LOWER;
    }

    private static void announceBind(ServerPlayer player, ServerLevel level, BlockPos pos) {
        player.sendSystemMessage(Component.translatable("message.legendaryshrines.link_formed")
                .withStyle(ChatFormatting.AQUA));
        if (!ShrineConfig.SOUNDS_ENABLED.get()) {
            return;
        }

        player.connection.send(new ClientboundSoundPacket(
                ModSounds.SHRINE_ACTIVATED.getHolder().orElseThrow(),
                SoundSource.BLOCKS,
                pos.getX() + 0.5D,
                pos.getY() + 1.5D,
                pos.getZ() + 0.5D,
                1.0F,
                1.0F,
                level.getRandom().nextLong()));
    }

    private static void announceBreak(ServerPlayer player) {
        player.sendSystemMessage(Component.translatable("message.legendaryshrines.link_broken")
                .withStyle(ChatFormatting.GRAY));

        if (ShrineConfig.SOUNDS_ENABLED.get()) {
            player.playNotifySound(SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 0.6F, 0.8F);
        }
    }

    private static void announceLost(ServerPlayer player) {
        player.sendSystemMessage(Component.translatable("message.legendaryshrines.link_lost")
                .withStyle(ChatFormatting.GRAY));

        if (ShrineConfig.SOUNDS_ENABLED.get()) {
            player.playNotifySound(SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 0.6F, 0.6F);
        }
    }
}
