package com.krampus.legendaryshrines.client;

import com.krampus.legendaryshrines.data.ShrineBind;
import com.krampus.legendaryshrines.registry.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;

import javax.annotation.Nullable;

public final class ShrineSweep {

    private static final int PARTICLE_COUNT = 80;
    private static final int SWEEP_TICKS = 20;
    private static final double HEIGHT = 0.2D;
    private static final double STEP_UP = 0.5D / 16.0D;
    private static final double ESCAPE_SPEED = 0.08D;
    private static final double LEVITATE_SPEED = 0.03D;

    private static final int SPORE_COUNT = 50;
    private static final double SPORE_SPREAD = 0.35D;
    private static final double SPORE_HEIGHT = 2.0D;
    private static final double SPORE_SPEED = 0.02D;

    @Nullable
    private static BlockPos origin;
    private static int elapsed;

    public static void start(ShrineBind bind) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || !level.dimension().equals(bind.dimension())) {
            return;
        }
        origin = bind.pos();
        elapsed = 0;
    }

    public static void stop() {
        origin = null;
    }

    public static void tick() {
        if (origin == null) {
            return;
        }

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            origin = null;
            return;
        }

        int from = PARTICLE_COUNT * elapsed / SWEEP_TICKS;
        int sporeFrom = SPORE_COUNT * elapsed / SWEEP_TICKS;
        elapsed++;
        int to = PARTICLE_COUNT * elapsed / SWEEP_TICKS;
        int sporeTo = SPORE_COUNT * elapsed / SWEEP_TICKS;

        for (int i = from; i < to; i++) {
            double angle = level.random.nextDouble() * Math.PI * 2.0D;

            level.addParticle(ModParticles.RUNE.get(),
                    origin.getX() + 0.5D,
                    origin.getY() + HEIGHT + i * STEP_UP,
                    origin.getZ() + 0.5D,
                    Math.cos(angle) * ESCAPE_SPEED,
                    LEVITATE_SPEED,
                    Math.sin(angle) * ESCAPE_SPEED);
        }

        for (int i = sporeFrom; i < sporeTo; i++) {
            double angle = level.random.nextDouble() * Math.PI * 2.0D;
            double height = level.random.nextDouble() * SPORE_HEIGHT;

            level.addParticle(ParticleTypes.WARPED_SPORE,
                    origin.getX() + 0.5D + Math.cos(angle) * SPORE_SPREAD,
                    origin.getY() + HEIGHT + height,
                    origin.getZ() + 0.5D + Math.sin(angle) * SPORE_SPREAD,
                    Math.cos(angle) * SPORE_SPEED,
                    0.0D,
                    Math.sin(angle) * SPORE_SPEED);
        }

        if (elapsed >= SWEEP_TICKS) {
            origin = null;
        }
    }
}
