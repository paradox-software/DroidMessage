package net.thenightwolf.dm.desktop.controller.components.colour;

public enum Colour {

    YELLOW_GOLD("#f6bf26"),
    LIGHT_BLUE("#4dd0e1"),
    SEAWATER("#219586"),
    ORANGE_CLAY("#ff8a65"),
    PURPLE_CANDY("#ff8a65");

    private final int red;
    private final int green;
    private final int blue;

    Colour(String hexColor) {
        red = Integer.valueOf(hexColor.substring(1, 3), 16);
        green = Integer.valueOf(hexColor.substring(3, 5), 16);
        blue = Integer.valueOf(hexColor.substring(5, 7), 16);
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
}

