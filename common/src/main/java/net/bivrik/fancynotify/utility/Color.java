package net.bivrik.fancynotify.utility;

public final class Color {
    private Color() {}

    public static int create(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    public static int create(int r, int g, int b) {
        return create(r, g, b, 255);
    }

    public static int WHITE = create(255, 255, 255);

    public static int LIGHT_GRAY = create(192, 192, 192);

    public static int GRAY = create(128, 128, 128);

    public static int DARK_GRAY = create(64, 64, 64);

    public static int BLACK = create(0, 0, 0);

    public static int RED = create(255, 0, 0);

    public static int GREEN = create(0, 255, 0);

    public static int BLUE = create(0, 0, 255);

    public static int YELLOW = create(255, 255, 0);

    public static int MAGENTA = create(255, 0, 255);

    public static int CYAN = create(0, 255, 255);
}
