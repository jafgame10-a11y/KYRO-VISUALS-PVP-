package com.kyro.visuals;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import org.lwjgl.glfw.GLFW;

public final class KyroVisualsClient implements ClientModInitializer {
    private static final String MOD_ID = "kyrovisuals";
    private static boolean hudEnabled = true;
    private static KeyMapping.Category category;
    private static KeyMapping toggleKey;
    private static int leftCps;
    private static int rightCps;
    private static long cpsWindowStart = System.currentTimeMillis();
    private static int leftClicks;
    private static int rightClicks;

    @Override
    public void onInitializeClient() {
        category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "controls"));
        toggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.kyrovisuals.toggle_hud",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_0,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                hudEnabled = !hudEnabled;
                if (client.player != null) {
                    client.player.displayClientMessage(Component.literal("§bKYRO VISUALS §7» HUD " + (hudEnabled ? "§aON" : "§cOFF")), true);
                }
            }
            long now = System.currentTimeMillis();
            if (now - cpsWindowStart >= 1000) {
                leftCps = leftClicks;
                rightCps = rightClicks;
                leftClicks = 0;
                rightClicks = 0;
                cpsWindowStart = now;
            }
            if (client.mouseHandler.isLeftPressed()) leftClicks = Math.min(leftClicks + 1, 30);
            if (client.mouseHandler.isRightPressed()) rightClicks = Math.min(rightClicks + 1, 30);
        });

        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(MOD_ID, "main_hud"),
                (graphics, deltaTracker) -> renderHud(graphics));
    }

    private static void renderHud(GuiGraphicsExtractor graphics) {
        if (!hudEnabled) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) return;

        int x = 8;
        int y = 8;
        int panelWidth = 124;
        int panelHeight = 66;
        graphics.fill(x, y, x + panelWidth, y + panelHeight, 0xD9000712);
        graphics.fill(x, y, x + panelWidth, y + 2, 0xFF18BFFF);
        graphics.fill(x, y + 2, x + 2, y + panelHeight, 0xFF126BFF);
        graphics.drawString(mc.font, "KYRO VISUALS", x + 8, y + 7, 0xFF50C8FF);
        graphics.drawString(mc.font, "FPS  " + mc.getFps(), x + 8, y + 22, 0xFFFFFFFF);

        int ping = -1;
        if (mc.player != null && mc.getConnection() != null) {
            var info = mc.getConnection().getPlayerInfo(mc.player.getUUID());
            if (info != null) ping = info.getLatency();
        }
        graphics.drawString(mc.font, "PING " + (ping < 0 ? "--" : ping + " ms"), x + 8, y + 34, 0xFFB5E9FF);
        graphics.drawString(mc.font, "CPS  " + leftCps + " | " + rightCps, x + 8, y + 46, 0xFFFFFFFF);
        graphics.drawString(mc.font, "HUD: 0", x + 8, y + 56, 0xFF6BBDFF);
    }
}
