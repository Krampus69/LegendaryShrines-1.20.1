package com.krampus.legendaryshrines.event;

import com.krampus.legendaryshrines.LegendaryShrines;
import com.krampus.legendaryshrines.config.ShrineConfig;
import com.krampus.legendaryshrines.data.ShrineBind;
import com.krampus.legendaryshrines.data.ShrineBinding;
import com.krampus.legendaryshrines.network.ModNetwork;
import com.krampus.legendaryshrines.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LegendaryShrines.MOD_ID)
public final class PlayerDataHandler {

    private static final int SETTLE_TICKS = 30;


    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            return;
        }
        ShrineBinding.copy(event.getOriginal(), event.getEntity());
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            settle(player);
        }
        sync(event.getEntity());
    }

    public static void settle(ServerPlayer player) {
        if (ShrineBinding.getFloatUntil(player) == 0L) {
            return;
        }
        ShrineBinding.clearFloatUntil(player);
        player.setNoGravity(false);
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        boolean arrived = false;

        if (event.getEntity() instanceof ServerPlayer player) {
            boolean pending = ShrineBinding.isRespawnPending(player);
            ShrineBinding.clearRespawnPending(player);

            if (!event.isEndConquered() && (pending || shouldOverrideRespawn(player))) {
                arrived = teleportToShrine(player);
            }
        }
        sync(event.getEntity(), arrived);
    }

    @SubscribeEvent
    public static void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        sync(event.getEntity());
    }

    private static boolean shouldOverrideRespawn(ServerPlayer player) {
        if (!ShrineConfig.OVERRIDE_VANILLA_RESPAWN.get()) {
            return false;
        }
        if (ShrineBinding.get(player) == null) {
            return false;
        }
        return player.level().getGameTime() >= ShrineBinding.getCooldownUntil(player);
    }

    private static boolean teleportToShrine(ServerPlayer player) {
        ShrineBind bind = ShrineBinding.get(player);
        if (bind == null) {
            return false;
        }

        ServerLevel target = player.server.getLevel(bind.dimension());
        if (target == null) {
            return false;
        }

        Vec3 spot = findRespawnSpot(target, bind.pos());

        player.setDeltaMovement(Vec3.ZERO);
        player.fallDistance = 0.0F;
        player.setNoGravity(true);
        ShrineBinding.setFloatUntil(player, target.getGameTime() + SETTLE_TICKS);

        player.teleportTo(target, spot.x, spot.y, spot.z, yawTowards(spot, bind.pos()), 0.0F);

        int cooldown = ShrineConfig.RESPAWN_COOLDOWN.get();
        if (cooldown > 0) {
            ShrineBinding.setCooldownUntil(player, target.getGameTime() + cooldown * 20L);
        }

        if (ShrineConfig.SOUNDS_ENABLED.get()) {
            player.playNotifySound(ModSounds.RESURECT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        return true;
    }

    private static Vec3 findRespawnSpot(ServerLevel level, BlockPos shrine) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos candidate = shrine.relative(direction);
            if (level.getBlockState(candidate).isAir()
                    && level.getBlockState(candidate.above()).isAir()
                    && !level.getBlockState(candidate.below()).isAir()) {
                return Vec3.atBottomCenterOf(candidate);
            }
        }
        return Vec3.atBottomCenterOf(shrine.above(2));
    }

    private static float yawTowards(Vec3 from, BlockPos target) {
        double dx = target.getX() + 0.5D - from.x;
        double dz = target.getZ() + 0.5D - from.z;
        return (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0D);
    }

    private static void sync(Player player) {
        sync(player, false);
    }

    private static void sync(Player player, boolean activate) {
        if (player instanceof ServerPlayer serverPlayer) {
            ModNetwork.syncBind(serverPlayer, ShrineBinding.get(serverPlayer), activate);
        }
    }
}
