package com.eemu.berryaura;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import net.minecraft.block.BlockState;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class BerryAuraMod implements ClientModInitializer {

    private static boolean enabled = false;
    private static KeyBinding toggleKey;

    // Tweakables
    private static final int MAX_PER_TICK = 8; // up to 8 bushes per tick
    private static final int RADIUS = 5;       // search radius (blocks)
    private static final boolean REQUIRE_BLAZE_ROD = true;
    private static final boolean ONLY_AGE_THREE = true;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.berryaura.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.berryaura"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                enabled = !enabled;
                if (client.player != null) {
                    client.player.sendMessage(Text.literal("Berry Aura: " + (enabled ? "ENABLED" : "DISABLED")), true);
                }
            }

            if (!enabled) return;
            if (client.player == null || client.world == null || client.interactionManager == null) return;

            if (REQUIRE_BLAZE_ROD && client.player.getMainHandStack().getItem() != Items.BLAZE_ROD) {
                return;
            }

            harvestNearbyBushes(client, MAX_PER_TICK, RADIUS, ONLY_AGE_THREE);
        });
    }

    private void harvestNearbyBushes(MinecraftClient client, int max, int radius, boolean onlyAge3) {
        World world = client.world;
        var player = client.player;

        BlockPos playerPos = player.getBlockPos();
        List<BlockPos> targets = new ArrayList<>();

        for (int dx = -radius; dx <= radius && targets.size() < max; dx++) {
            for (int dy = -1; dy <= 2 && targets.size() < max; dy++) {
                for (int dz = -radius; dz <= radius && targets.size() < max; dz++) {
                    BlockPos pos = playerPos.add(dx, dy, dz);
                    BlockState state = world.getBlockState(pos);

                    if (!state.isOf(Blocks.SWEET_BERRY_BUSH)) continue;

                    if (onlyAge3) {
                        Integer age = state.get(SweetBerryBushBlock.AGE);
                        if (age == null || age < 3) continue;
                    }

                    targets.add(pos);
                }
            }
        }

        for (BlockPos pos : targets) {
            Vec3d hitPos = Vec3d.ofCenter(pos, 0.5);
            BlockHitResult bhr = new BlockHitResult(hitPos, Direction.UP, pos, false);

            ActionResult result = client.interactionManager.interactBlock(player, Hand.MAIN_HAND, bhr);
            // Optional: add throttling if needed for servers
        }
    }
}
