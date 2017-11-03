package ru.justagod.justacore.gui.overlay;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import ru.justagod.justacore.gui.overlay.event.KeyboardListener;
import ru.justagod.justacore.gui.overlay.event.MouseClickListener;
import ru.justagod.justacore.gui.overlay.event.MouseHoverListener;
import ru.justagod.justacore.gui.overlay.parent.OverlayParent;
import ru.justagod.justacore.gui.overlay.transform.Transformation;
import ru.justagod.justacore.helper.Vector;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static ru.justagod.justacore.gui.overlay.ScaledOverlay.ScaleMode.NORMAL;

/**
 * Created by JustAGod on 16.10.17.
 */
public abstract class ScaledOverlay extends Overlay {

    public static final int BOUND = 100;
    public final List<Transformation> transformations = new ArrayList<Transformation>();
    public final List<MouseClickListener> mouseClickListeners = new ArrayList<MouseClickListener>();
    public final List<MouseHoverListener> mouseHoverListeners = new ArrayList<MouseHoverListener>();
    public final List<KeyboardListener> keyboardListeners = new ArrayList<KeyboardListener>();
    protected double width;
    protected double height;
    protected boolean scalePosition = true;
    protected boolean scaleSize = true;
    protected boolean doScissor = false;
    protected boolean isFocused;
    protected ScaleMode scaleMode = NORMAL;

    protected OverlayParent parent;

    public ScaledOverlay(double x, double y) {
        super(x, y);
    }

    public ScaledOverlay(double x, double y, double width, double height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    public ScaledOverlay(double x, double y, double width, double height, boolean scalePosition, boolean scaleSize) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.scalePosition = scalePosition;
        this.scaleSize = scaleSize;
    }

    public boolean onKey(char key, int code) {
        if (isFocused) {
            if (keyboardListeners.size() > 0) {
                for (KeyboardListener listener : keyboardListeners) {
                    listener.onKey(this, key, code);
                }
                return true;
            }
        }
        return false;
    }

    public boolean isDoScissor() {
        return doScissor;
    }

    public void setDoScissor(boolean doScissor) {
        this.doScissor = doScissor;
    }

    public double getScaledX() {
        return (isScalePosition() ? ((getX()) * getXFactor()) : x) + parent.getScaledX();
    }

    public void setScaledX(double scaledX) {
        x = (isScalePosition() ? (scaledX / getXFactor()) : scaledX) + parent.getScaledX();
    }

    protected double getScaledY() {
        return (isScalePosition() ? ((getY()) * getYFactor()) : y) + parent.getScaledY();
    }

    public void setScaledY(double scaledY) {
        y = (isScalePosition() ? (scaledY / getYFactor()) : scaledY) + parent.getScaledY();
    }

    protected double getXFactor() {
        return parent.getScaledWidth() / BOUND;
    }

    protected double getYFactor() {
        return parent.getScaledHeight() / BOUND;
    }

    public double getScaledWidth() {
        if (isScaleSize()) {
            switch (scaleMode) {
                case NORMAL:
                    return (width) * getXFactor();
                case DONT_SCALE_WIDTH:
                    return width;
                case WIDTH_EQUAL_HEIGHT:
                    return getScaledHeight();
                default:
                    return (width) * getXFactor();
            }
        } else {
            return width;
        }
    }

    protected double getScaledHeight() {
        if (isScaleSize()) {
            switch (scaleMode) {
                case NORMAL:
                    return (height) * getYFactor();
                case DONT_SCALE_HEIGHT:
                    return height;
                case HEIGHT_EQUAL_WIDTH:
                    return getScaledWidth();
                default:
                    return (height) * getYFactor();
            }
        } else {
            return height;
        }
    }

    public boolean isFocused() {
        return isFocused;
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
    }

    public ScaleMode getScaleMode() {
        return scaleMode;
    }

    public void setScaleMode(ScaleMode scaleMode) {
        this.scaleMode = scaleMode;
    }

    public boolean isScalePosition() {
        return scalePosition;
    }

    public void setScalePosition(boolean scalePosition) {
        this.scalePosition = scalePosition;
    }

    public boolean isScaleSize() {
        return scaleSize;
    }

    public void setScaleSize(boolean scaleSize) {
        this.scaleSize = scaleSize;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    protected void runTransformations() {
        for (Transformation transformation : transformations) {
            transformation.transform(this);
        }
    }

    @Override
    public synchronized void draw(float partialTick, int mouseX, int mouseY) {
        GL11.glColor3d(1, 1, 1);
        glDisable(GL11.GL_CULL_FACE);

        if (isPointInBounds(mouseX, mouseY)) {
            for (MouseHoverListener listener : mouseHoverListeners) {
                listener.onClick(mouseX - getScaledX(), mouseY - getScaledY(), this);
            }
        }
        runTransformations();
        if (isDoScissor())
            realScissor(getScaledX() / getScreenScaledWidth(), getScaledY() / getScreenScaledHeight(), getScaledWidth() / getScreenScaledWidth(), getHeight() / getScreenScaledHeight());

        doDraw(getScaledX(), getScaledY(), getScaledWidth(), getScaledHeight(), partialTick, mouseX, mouseY, isPointInBounds(mouseX, mouseY));

        GL11.glEnable(GL11.GL_CULL_FACE);
        if (isDoScissor())
            glDisable(GL_SCISSOR_TEST);
    }

    public int getScreenScaledWidth() {
        return getResolution().getScaledWidth();
    }

    public int getScreenScaledHeight() {
        return getResolution().getScaledHeight();
    }

    public ScaledResolution getResolution() {
        return new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
    }

    public boolean onClick(int x, int y) {
        if (isPointInBounds(x, y)) {
            for (MouseClickListener listener : mouseClickListeners) {
                listener.onClick(x - getScaledX(), y - getScaledY(), this);
                return true;
            }
            isFocused = true;
        }
        isFocused = false;
        return false;
    }

    protected void realScissor(double x, double y, double width, double height) {
        int displayWidth = Minecraft.getMinecraft().displayWidth;
        int displayHeight = Minecraft.getMinecraft().displayHeight;

        x /= BOUND;
        y /= BOUND;
        width /= BOUND;
        height /= BOUND;

        glScissor((int) (x * displayWidth), (int) (y * displayHeight), (int) (width * displayWidth), (int) (height * displayHeight));

        GL11.glEnable(GL_SCISSOR_TEST);

    }

    @Override
    public synchronized void drawText(float partialTick, int mouseX, int mouseY) {
        GL11.glColor3d(1, 1, 1);
        glDisable(GL11.GL_CULL_FACE);
        if (isPointInBounds(mouseX, mouseY)) {
            for (MouseHoverListener listener : mouseHoverListeners) {
                listener.onClick(mouseX - getScaledX(), mouseY - getScaledY(), this);
            }
        }
        runTransformations();
        if (isDoScissor())
            realScissor(getScaledX() / getScreenScaledWidth(), getScaledY() / getScreenScaledHeight(), getScaledWidth() / getScreenScaledWidth(), getHeight() / getScreenScaledHeight());

        doDrawText(getScaledX(), getScaledY(), getScaledWidth(), getScaledHeight(), partialTick, mouseX, mouseY, isPointInBounds(mouseX, mouseY));

        GL11.glEnable(GL11.GL_CULL_FACE);
        if (isDoScissor())
            glDisable(GL_SCISSOR_TEST);
    }

    protected boolean isPointInBounds(int x, int y) {
        return x >= getScaledX() && y >= getScaledY() && x <= getScaledX() + getScaledWidth() && y <= getScaledY() + getScaledHeight();
    }

    protected abstract void doDraw(double xPos, double yPos, double width, double height, float partialTick, int mouseX, int mouseY, boolean mouseInBounds);

    protected abstract void doDrawText(double xPos, double yPos, double width, double height, float partialTick, int mouseX, int mouseY, boolean mouseInBounds);

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public OverlayParent getParent() {
        return parent;
    }

    public synchronized void setParent(OverlayParent parent) {
        this.parent = parent;
    }

    public void remove() {
        getParent().removeOverlay(this);
    }

    @Override
    public String toString() {
        return "ScaledOverlay{" +
                "width=" + width +
                ", height=" + height +
                ", scalePosition=" + scalePosition +
                ", scaleSize=" + scaleSize +
                ", parent=" + parent +
                ", scaled width=" + getScaledWidth() +
                ", scaled height=" + getScaledHeight() +
                '}';
    }

    public Vector getScaledPos() {
        return new Vector(getScaledX(), getScaledY());
    }

    public enum ScaleMode {
        WIDTH_EQUAL_HEIGHT, HEIGHT_EQUAL_WIDTH, NORMAL, DONT_SCALE_WIDTH, DONT_SCALE_HEIGHT;
    }
}
