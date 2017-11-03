package ru.justagod.justacore.gui.overlay.parent;


import ru.justagod.justacore.gui.overlay.ScaledOverlay;
import ru.justagod.justacore.gui.overlay.set.OverlaySet;

import java.util.Collection;

/**
 * Created by JustAGod on 20.10.17.
 */
public interface OverlayParent {

    void addOverlay(ScaledOverlay overlay);

    void removeOverlay(ScaledOverlay overlay);

    Collection<ScaledOverlay> getOverlays();

    void addOverlays(Collection<ScaledOverlay> overlays);

    void setOverlays(OverlaySet overlaySet);

    void appendOverlays(OverlaySet overlaySet);

    void moveUp(ScaledOverlay overlay);

    void moveDown(ScaledOverlay overlay);

    double getScaledWidth();

    double getScaledHeight();

    double getScaledX();

    double getScaledY();

    void clear();
}
