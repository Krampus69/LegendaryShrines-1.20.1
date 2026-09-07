package com.krampus.legendaryshrines.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RuneParticle extends TextureSheetParticle {

    private static final int MIN_LIFETIME = 30;
    private static final int EXTRA_LIFETIME = 15;

    private static final float FADE_FRACTION = 0.35F;

    private static final double ORBIT_REVOLUTIONS = 2.0D;
    private static final int ORBIT_PERIOD_TICKS = 80;
    private static final double ORBIT_SPEED =
            -(Math.PI * 2.0D) * ORBIT_REVOLUTIONS / ORBIT_PERIOD_TICKS;

    private final double centerX;
    private final double centerZ;
    private final double radialSpeed;
    private final double riseSpeed;

    private double orbitAngle;
    private double orbitRadius;

    RuneParticle(ClientLevel level, double x, double y, double z,
                 double dx, double dy, double dz, SpriteSet sprites) {
        super(level, x, y, z);

        setSprite(sprites.get(this.random));

        this.lifetime = MIN_LIFETIME + this.random.nextInt(EXTRA_LIFETIME);
        this.quadSize = 0.1F + this.random.nextFloat() * 0.06F;

        this.centerX = x;
        this.centerZ = z;
        this.orbitAngle = Math.atan2(dz, dx);
        this.orbitRadius = 0.0D;
        this.radialSpeed = Math.sqrt(dx * dx + dz * dz);
        this.riseSpeed = dy;

        this.roll = facingFor(this.orbitAngle);
        this.oRoll = this.roll;
    }

    private static float facingFor(double angle) {
        return (float) (Math.PI / 2.0D - angle);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.oRoll = this.roll;

        if (this.age++ >= this.lifetime) {
            remove();
            return;
        }

        this.orbitAngle += ORBIT_SPEED;
        this.orbitRadius += this.radialSpeed;
        this.roll = facingFor(this.orbitAngle);

        setPos(this.centerX + Math.cos(this.orbitAngle) * this.orbitRadius,
                this.y + this.riseSpeed,
                this.centerZ + Math.sin(this.orbitAngle) * this.orbitRadius);
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTick) {
        Vec3 cam = camera.getPosition();
        float x = (float) (Mth.lerp(partialTick, this.xo, this.x) - cam.x());
        float y = (float) (Mth.lerp(partialTick, this.yo, this.y) - cam.y());
        float z = (float) (Mth.lerp(partialTick, this.zo, this.z) - cam.z());

        Quaternionf facing = new Quaternionf().rotationY(Mth.lerp(partialTick, this.oRoll, this.roll));
        float size = getQuadSize(partialTick);

        Vector3f[] corners = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)
        };

        for (Vector3f corner : corners) {
            corner.rotate(facing);
            corner.mul(size);
            corner.add(x, y, z);
        }

        int light = getLightColor(partialTick);
        float fade = alphaAt(partialTick);
        emit(buffer, corners, light, fade, false);
        emit(buffer, corners, light, fade, true);
    }

    private float alphaAt(float partialTick) {
        float elapsed = this.age + partialTick;
        float remaining = 1.0F - elapsed / this.lifetime;
        return Mth.clamp(remaining / FADE_FRACTION, 0.0F, 1.0F) * this.alpha;
    }

    private void emit(VertexConsumer buffer, Vector3f[] corners, int light, float alpha, boolean reversed) {
        float[][] uv = {
                {getU1(), getV1()},
                {getU1(), getV0()},
                {getU0(), getV0()},
                {getU0(), getV1()}
        };

        for (int i = 0; i < 4; i++) {
            int index = reversed ? 3 - i : i;
            Vector3f corner = corners[index];
            buffer.vertex(corner.x(), corner.y(), corner.z())
                    .uv(uv[index][0], uv[index][1])
                    .color(this.rCol, this.gCol, this.bCol, alpha)
                    .uv2(light)
                    .endVertex();
        }
    }

    @Override
    protected int getLightColor(float partialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new RuneParticle(level, x, y, z, dx, dy, dz, this.sprites);
        }
    }
}
