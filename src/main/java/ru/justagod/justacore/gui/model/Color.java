package ru.justagod.justacore.gui.model;

/**
 * Created by JustAGod on 07.11.17.
 */
public class Color {

    public static final Color RED = new Color(1, 0, 0);
    public static final Color GREEN = new Color(0, 1, 0);
    public static final Color BLUE = new Color(0, 0, 1);
    public static final Color WHITE = new Color(1, 1, 1);

    double red;
    double green;
    double blue;
    double alpha;

    public Color(double red, double green, double blue) {
        this(red, green, blue, 1);
    }

    public Color(double red, double green, double blue, double alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public double getRed() {
        return red;
    }

    public void setRed(double red) {
        this.red = red;
    }

    public double getGreen() {
        return green;
    }

    public void setGreen(double green) {
        this.green = green;
    }

    public double getBlue() {
        return blue;
    }

    public void setBlue(double blue) {
        this.blue = blue;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public String toString() {
        return "Color{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                ", alpha=" + alpha +
                '}';
    }
}
