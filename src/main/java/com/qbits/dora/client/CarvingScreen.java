package com.qbits.dora.client;

import com.mojang.math.Axis;
import com.qbits.dora.block.StrippedMapleLogBlock;
import com.qbits.dora.carving.Carving;
import com.qbits.dora.carving.CarvingBlockEntity;
import com.qbits.dora.init.ModTags;
import com.qbits.dora.item.CarvingChiselItem;
import com.qbits.dora.network.CarvePixelPacket;
import com.qbits.dora.network.ModNetwork;
import com.qbits.dora.network.SapRevealPacket;
import com.qbits.dora.sap.SapPattern;
import net.minecraft.client.Minecraft;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class CarvingScreen extends Screen {
    private static final double MAX_DISTANCE_SQR = 8 * 8;
    private static final float CURSOR_SCALE = 0.75F;
    private static final float TIP_X = 15.5F;
    private static final float TIP_Y = 0.5F;
    private static final long DOUBLE_CLICK_MS = 300;
    private static final float MAX_TILT = 18F;
    private static final float TILT_PER_SPEED = 0.03F;

    private final BlockPos pos;
    private final Direction face;
    private final InteractionHand hand;
    private final boolean tapping;
    private boolean revealed;

    private int pressedButton = -1;
    private boolean selecting;
    private int[] selectionStart;
    private int lastClickButton = -1;
    private long lastClickAt;
    private double lastX;
    private double lastY;

    private long lastFrameAt = Util.getMillis();
    private double previousMouseX = Double.NaN;
    private float speed;
    private float tilt;

    public CarvingScreen(BlockPos pos, Direction face, InteractionHand hand) {
        super(Component.empty());
        this.pos = pos.immutable();
        this.face = face;
        this.hand = hand;
        Minecraft client = Minecraft.getInstance();
        this.tapping = client.level != null && StrippedMapleLogBlock.canTap(client.level.getBlockState(pos), face);
    }

    @Override
    protected void init() {
        GLFW.glfwSetInputMode(minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
        if (CarvingSession.open() == null) {
            CarvingSession.start(pos, face);
            CarvingSession session = CarvingSession.open();
            if (session != null && tapping) {
                session.sap = new SapProgress(SapPattern.generate(pos, face));
            }
        }
    }

    @Override
    public void removed() {
        GLFW.glfwSetInputMode(minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        CarvingSession.stop();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        Player player = minecraft.player;
        if (player == null
                || !(player.getItemInHand(hand).getItem() instanceof CarvingChiselItem)
                || player.distanceToSqr(Vec3.atCenterOf(pos)) > MAX_DISTANCE_SQR
                || !isValid(player.level().getBlockState(pos))) {
            onClose();
        }
    }

    private boolean isValid(BlockState state) {
        return tapping ? StrippedMapleLogBlock.canTap(state, face) : state.is(ModTags.CARVABLE);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        Player player = minecraft.player;
        if (player == null) {
            return;
        }
        CarvingSession session = CarvingSession.open();
        if (session != null) {
            session.hovered = session.progress() >= 1 ? pick(mouseX, mouseY) : null;
            if (session.sap != null) {
                graphics.drawCenteredString(font, Component.translatable("gui.dora.sap", session.sap.percent()), width / 2, 12, 0xFFFFFF);
            }
        }

        graphics.pose().pushPose();
        graphics.pose().translate(mouseX, mouseY, 400);
        graphics.pose().mulPose(Axis.ZP.rotationDegrees(updateTilt(mouseX)));
        graphics.pose().scale(CURSOR_SCALE, CURSOR_SCALE, 1);
        graphics.pose().translate(-TIP_X, -TIP_Y, 0);
        graphics.renderItem(player.getItemInHand(hand), 0, 0);
        graphics.pose().popPose();
    }

    private float updateTilt(double mouseX) {
        long now = Util.getMillis();
        float delta = Mth.clamp((now - lastFrameAt) / 1000F, 0.001F, 0.1F);
        lastFrameAt = now;
        float rawSpeed = Double.isNaN(previousMouseX) ? 0 : (float) ((mouseX - previousMouseX) / delta);
        previousMouseX = mouseX;
        speed = Mth.lerp(1 - (float) Math.exp(-delta * 14), speed, rawSpeed);
        float target = Mth.clamp(speed * TILT_PER_SPEED, -MAX_TILT, MAX_TILT);
        tilt = Mth.lerp(1 - (float) Math.exp(-delta * 10), tilt, target);
        return tilt;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT && button != GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            return super.mouseClicked(mouseX, mouseY, button);
        }
        if (tapping) {
            pressedButton = button;
            lastX = mouseX;
            lastY = mouseY;
            applyAt(mouseX, mouseY);
            return true;
        }
        long now = Util.getMillis();
        boolean doubleClick = button == lastClickButton && now - lastClickAt <= DOUBLE_CLICK_MS;
        lastClickButton = doubleClick ? -1 : button;
        lastClickAt = now;
        pressedButton = button;
        lastX = mouseX;
        lastY = mouseY;

        if (doubleClick && button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            send(0, 0, Carving.SIZE - 1, Carving.SIZE - 1, false);
            pressedButton = -1;
        } else if (doubleClick) {
            int[] pixel = pick(mouseX, mouseY);
            if (pixel != null) {
                selecting = true;
                selectionStart = pixel;
                updateSelection(pixel);
            }
        } else {
            applyAt(mouseX, mouseY);
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button != pressedButton) {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
        if (selecting) {
            int[] pixel = pick(mouseX, mouseY);
            if (pixel != null) {
                updateSelection(pixel);
            }
            return true;
        }
        int steps = Math.max(1, (int) Math.ceil(Math.hypot(mouseX - lastX, mouseY - lastY)));
        for (int i = 1; i <= steps; i++) {
            double t = i / (double) steps;
            applyAt(lastX + (mouseX - lastX) * t, lastY + (mouseY - lastY) * t);
        }
        lastX = mouseX;
        lastY = mouseY;
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button != pressedButton) {
            return super.mouseReleased(mouseX, mouseY, button);
        }
        pressedButton = -1;
        if (selecting) {
            selecting = false;
            CarvingSession session = CarvingSession.open();
            if (session != null && session.selection != null) {
                int[] area = session.selection;
                send(area[0], area[1], area[2], area[3], true);
                session.selection = null;
            }
        }
        return true;
    }

    private void updateSelection(int[] pixel) {
        CarvingSession session = CarvingSession.open();
        if (session != null) {
            session.selection = new int[]{selectionStart[0], selectionStart[1], pixel[0], pixel[1]};
        }
    }

    @Nullable
    private int[] pick(double mouseX, double mouseY) {
        return CarvingRenderer.pick(pos, face, mouseX, mouseY, width, height);
    }

    private void applyAt(double mouseX, double mouseY) {
        int[] pixel = pick(mouseX, mouseY);
        if (pixel == null) {
            return;
        }
        boolean carve = pressedButton == GLFW.GLFW_MOUSE_BUTTON_LEFT;
        if (tapping) {
            if (carve) {
                scrape(pixel);
            }
            return;
        }
        Carving carving = clientCarving();
        boolean carved = carving != null && carving.isCarved(face, pixel[0], pixel[1]);
        if (carved != carve) {
            send(pixel[0], pixel[1], pixel[0], pixel[1], carve);
        }
    }

    private void scrape(int[] pixel) {
        CarvingSession session = CarvingSession.open();
        if (session == null || session.sap == null || session.progress() < 1 || revealed) {
            return;
        }
        session.sap.scrape(pixel[0], pixel[1]);
        if (session.sap.percent() >= 100) {
            revealed = true;
            ModNetwork.CHANNEL.sendToServer(new SapRevealPacket(pos, face));
            onClose();
        }
    }

    private void send(int x0, int y0, int x1, int y1, boolean carve) {
        CarvingSession session = CarvingSession.open();
        if (session == null || session.progress() < 1) {
            return;
        }
        Carving carving = clientCarving();
        if (carving != null) {
            carving.fill(face, x0, y0, x1, y1, carve);
        }
        ModNetwork.CHANNEL.sendToServer(new CarvePixelPacket(pos, face, x0, y0, x1, y1, carve));
    }

    @Nullable
    private Carving clientCarving() {
        return minecraft.level != null && minecraft.level.getBlockEntity(pos) instanceof CarvingBlockEntity entity ? entity.getCarving() : null;
    }
}
