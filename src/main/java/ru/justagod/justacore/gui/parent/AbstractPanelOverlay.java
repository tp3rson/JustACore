package ru.justagod.justacore.gui.parent;


import ru.justagod.justacore.gui.overlay.ScaledOverlay;
import ru.justagod.justacore.gui.set.OverlaySet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

/**
 * Created by JustAGod on 20.10.17.
 */
public abstract class AbstractPanelOverlay extends ScaledOverlay implements OverlayParent {

    protected List<ScaledOverlay> overlays = new ArrayList<ScaledOverlay>();

    public AbstractPanelOverlay(double x, double y) {
        super(x, y);
        setDoScissor(true);
    }

    public AbstractPanelOverlay(double x, double y, double width, double height) {
        super(x, y, width, height);
        setDoScissor(true);
    }

    public AbstractPanelOverlay(double x, double y, double width, double height, boolean scalePosition, boolean scaleSize) {
        super(x, y, width, height, scalePosition, scaleSize);
        setDoScissor(true);
    }

    @Override
    public synchronized void addOverlay(ScaledOverlay overlay) {
        overlays.add(overlay);
        overlay.setParent(this);
    }

    @Override
    public synchronized void removeOverlay(ScaledOverlay overlay) {
        if(overlays.remove(overlay)) {
            overlay.setParent(null);
        }

    }

    @Override
    public synchronized void update() {
        super.update();
        for (ScaledOverlay overlay : overlays) {
            overlay.update();
        }
    }

    @Override
    public Collection<ScaledOverlay> getOverlays() {
        return overlays;
    }

    @Override
    public void moveUp(ScaledOverlay overlay) {
        int position = overlays.indexOf(overlay);

        if (position != -1) {
            overlays.remove(position);
            overlays.add(position + 1, overlay);
        }
    }


    @Override
    public void moveDown(ScaledOverlay overlay) {
        int position = overlays.indexOf(overlay);

        if (position != -1) {
            overlays.remove(position);
            overlays.add(position - 1, overlay);
        }
    }

    @Override
    public void moveToFront(ScaledOverlay overlay) {

        if (overlays.contains(overlay)) {
            overlays.remove(overlay);
            overlays.add(0, overlay);
        }
    }

    @Override
    public void moveToBackground(ScaledOverlay overlay) {

        if (overlays.contains(overlay)) {
            overlays.remove(overlay);
            overlays.add(overlays.size(), overlay);
        }
    }

    @Override
    public double getParentWidth() {
        return super.getScaledWidth();
    }

    @Override
    public double getParentHeight() {
        return super.getScaledHeight();
    }

    @Override
    public double getScaledX() {
        return super.getScaledX();
    }

    @Override
    public double getScaledY() {
        return super.getScaledY();
    }


    @Override
    protected boolean doMouseScroll(double mouseX, double mouseY, int scrollAmount) {
        boolean flag = false;
        for (ScaledOverlay overlay : overlays) {
            flag |= overlay.onMouseScroll(mouseX, mouseY, scrollAmount);
        }
        return flag;
    }

    @Override
    protected void doDraw(double xPos, double yPos, double width, double height, float partialTick, int mouseX, int mouseY, boolean mouseInBounds) {
        glPushMatrix();
        for (ScaledOverlay overlay : overlays) {
            overlay.draw(partialTick, mouseX, mouseY);
        }
        glPopMatrix();
    }

    @Override
    protected void doDrawText(double xPos, double yPos, double width, double height, float partialTick, int mouseX, int mouseY, boolean mouseInBounds) {
        glPushMatrix();
        for (ScaledOverlay overlay : overlays) {
            overlay.drawText(partialTick, mouseX, mouseY);
        }
        glPopMatrix();
    }

    @Override
    public void doOnKey(char key, int code) {
        for (ScaledOverlay overlay : overlays) {
            if (overlay.onKey(key, code)) {
                break;
            }
        }
    }

    @Override
    public void addOverlays(Collection<ScaledOverlay> overlays) {
        for (ScaledOverlay overlay : overlays) {
            addOverlay(overlay);
        }
    }

    @Override
    public void setOverlays(OverlaySet overlaySet) {
        clear();
        addOverlays(overlaySet.getOverlays());
    }

    @Override
    public void appendOverlays(OverlaySet overlaySet) {
        addOverlays(overlaySet.getOverlays());
    }

    @Override
    public synchronized void clear() {
        Iterator<ScaledOverlay> iterator = overlays.iterator();

        while(iterator.hasNext()) {
            ScaledOverlay overlay = iterator.next();
            overlay.setParent(null);
            iterator.remove();
        }
    }

}