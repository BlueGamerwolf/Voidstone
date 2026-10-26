package dev.blue_gamerwolf.voidstone.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Renders a deterministic, deep-space starfield over Voidstone's End-style sky.
 * This is client-only and intentionally leaves terrain rendering to Minecraft.
 */
@Mod.EventBusSubscriber(modid = "voidstone", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class VoidSkyRenderer {
    private static final ResourceLocation VOID_DIMENSION = new ResourceLocation("voidstone", "void_dimension");
    private static final List<Star> STARS = createStars();

    private VoidSkyRenderer() {
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) {
            return;
        }

        // RenderLevelStageEvent in Forge 1.20.1 does not expose getLevel().
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null
                || !minecraft.level.dimension().location().equals(VOID_DIMENSION)) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();
        Vec3 camera = event.getCamera().getPosition();

        poseStack.pushPose();
        // Cancel the camera translation so the stars behave like a distant celestial sphere.
        poseStack.translate(camera.x, camera.y, camera.z);
        org.joml.Matrix4f pose = poseStack.last().pose();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        for (Star star : STARS) {
            float x = star.x;
            float y = star.y;
            float z = star.z;
            float size = star.size;
            float r = star.red;
            float g = star.green;
            float b = star.blue;
            float a = star.alpha;

            // A soft square core, with a few larger four-point stars for depth and variety.
            quad(buffer, pose, x, y, z, size, size, r, g, b, a);
            if (star.sparkle) {
                quad(buffer, pose, x, y, z, size * 2.8F, size * 0.16F, r, g, b, a * 0.55F);
                quad(buffer, pose, x, y, z, size * 0.16F, size * 2.8F, r, g, b, a * 0.55F);
            }
        }

        BufferUploader.drawWithShader(buffer.end());

        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    private static void quad(BufferBuilder buffer, org.joml.Matrix4f pose, float x, float y, float z,
                             float width, float height, float red, float green, float blue, float alpha) {
        float halfWidth = width * 0.5F;
        float halfHeight = height * 0.5F;
        buffer.vertex(pose, x - halfWidth, y - halfHeight, z).color(red, green, blue, alpha).endVertex();
        buffer.vertex(pose, x - halfWidth, y + halfHeight, z).color(red, green, blue, alpha).endVertex();
        buffer.vertex(pose, x + halfWidth, y + halfHeight, z).color(red, green, blue, alpha).endVertex();
        buffer.vertex(pose, x + halfWidth, y - halfHeight, z).color(red, green, blue, alpha).endVertex();
    }

    private static List<Star> createStars() {
        Random random = new Random(0x701D570EL);
        List<Star> stars = new ArrayList<>(1100);

        for (int i = 0; i < 1100; i++) {
            double yUnit = random.nextDouble() * 2.0 - 1.0;
            double angle = random.nextDouble() * Math.PI * 2.0;
            double horizontal = Math.sqrt(1.0 - yUnit * yUnit);
            float radius = 118.0F + random.nextFloat() * 8.0F;

            float x = (float) (Math.cos(angle) * horizontal * radius);
            float y = (float) (yUnit * radius);
            float z = (float) (Math.sin(angle) * horizontal * radius);

            float brightness = 0.42F + random.nextFloat() * 0.58F;
            float red = brightness;
            float green = brightness;
            float blue = brightness;

            // Rare cool-toned stars give the void a restrained violet/blue color palette.
            int tint = random.nextInt(10);
            if (tint == 0) {
                red = brightness * 0.68F;
                green = brightness * 0.62F;
                blue = brightness;
            } else if (tint == 1) {
                red = brightness * 0.86F;
                green = brightness * 0.55F;
                blue = brightness;
            }

            float size = 0.10F + random.nextFloat() * 0.20F;
            boolean sparkle = random.nextFloat() < 0.045F;
            if (sparkle) {
                size *= 1.6F;
            }

            stars.add(new Star(x, y, z, size, red, green, blue,
                    sparkle ? 0.95F : 0.72F + random.nextFloat() * 0.28F, sparkle));
        }

        return List.copyOf(stars);
    }

    private record Star(float x, float y, float z, float size,
                        float red, float green, float blue, float alpha, boolean sparkle) {
    }
}
