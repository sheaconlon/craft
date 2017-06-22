package com.sheaconlon.realcraft.renderer;

import java.util.Arrays;

/**
 * A glpyh. Immutable.
 */
public class Glyph {
    /**
     * The number "0".
     */
    public static final Glyph NUMBER_0 = Glyph.fromString(
            ".######.\n" +
            "##....##\n" +
            "#......#\n" +
            "#......#\n" +
            "#......#\n" +
            "#......#\n" +
            "##....##\n" +
            ".######.\n"
    );

    /**
     * The number "1".
     */
    public static final Glyph NUMBER_1 = Glyph.fromString(
            "...##...\n" +
            ".##.#...\n" +
            "....#...\n" +
            "....#...\n" +
            "....#...\n" +
            "....#...\n" +
            "....#...\n" +
            "########\n"
    );

    /**
     * The number "2".
     */
    public static final Glyph NUMBER_2 = Glyph.fromString(
            ".######.\n" +
            "##....##\n" +
            ".......#\n" +
            ".....##.\n" +
            "....##..\n" +
            "..##....\n" +
            "##......\n" +
            "########\n"
    );

    /**
     * The number "3".
     */
    public static final Glyph NUMBER_3 = Glyph.fromString(
            "######..\n" +
            "......##\n" +
            ".......#\n" +
            "......##\n" +
            "######..\n" +
            "......##\n" +
            "......##\n" +
            "######..\n"
    );

    /**
     * The number "4".
     */
    public static final Glyph NUMBER_4 = Glyph.fromString(
            "#......#\n" +
            "#......#\n" +
            "#......#\n" +
            "#......#\n" +
            "########\n" +
            ".......#\n" +
            ".......#\n" +
            ".......#\n"
    );

    /**
     * The number "5".
     */
    public static final Glyph NUMBER_5 = Glyph.fromString(
            "########\n" +
            "#.......\n" +
            "#.......\n" +
            "#......\n" +
            "#######.\n" +
            "......##\n" +
            "......##\n" +
            "#######.\n"
    );

    /**
     * The number "6".
     */
    public static final Glyph NUMBER_6 = Glyph.fromString(
            ".######.\n" +
            "##....##\n" +
            "#.......\n" +
            "#......\n" +
            "#######.\n" +
            "#.....##\n" +
            "##....##\n" +
            ".######.\n"
    );

    /**
     * The number "7".
     */
    public static final Glyph NUMBER_7 = Glyph.fromString(
            "########\n" +
            "......##\n" +
            ".....##.\n" +
            "....##..\n" +
            "...##...\n" +
            "..##....\n" +
            ".##.....\n" +
            "##......\n"
    );

    /**
     * The number "8".
     */
    public static final Glyph NUMBER_8 = Glyph.fromString(
            "..####..\n" +
            ".##..##.\n" +
            "##....##\n" +
            ".##..##.\n" +
            "..####..\n" +
            "##....##\n" +
            "##....##\n" +
            "..####..\n"
    );

    /**
     * The number "9".
     */
    public static final Glyph NUMBER_9 = Glyph.fromString(
            ".######.\n" +
            "##....##\n" +
            "##.....#\n" +
            ".#######\n" +
            ".......#\n" +
            ".......#\n" +
            ".......#\n" +
            ".......#\n"
    );

    /**
     * The side length of the bitmap of a glyph.
     */
    private static final int SIZE = 8;

    /**
     * The character which separates rows in the string representation of a glyph.
     */
    private static final char SEPARATOR_CHAR = '\n';

    /**
     * The character which indicates an opaque square in the string representation of a glyph.
     */
    private static final char OPAQUE_CHAR = '#';

    /**
     * Create a glyph from a string representation.
     * @param str The string representation. Must consist of {@link #SIZE} lines, each terminated by
     *            {@link #SEPARATOR_CHAR}. The i-th line gives the bitmap for the i-th row of the glyph.
     *            {@link #OPAQUE_CHAR} indicates an opaque square and other characters indicate a transparent
     *            square.
     * @return The glyph.
     */
    static Glyph fromString(final String str) {
        final boolean[][] bitmap = new boolean[Glyph.SIZE][Glyph.SIZE];
        int x = 0;
        int y = 0;
        for (char c : str.toCharArray()) {
            if (c == Glyph.SEPARATOR_CHAR) {
                x = 0;
                y++;
            } else {
                bitmap[x][y] = (c == Glyph.OPAQUE_CHAR);
                x++;
            }
        }
        return new Glyph(bitmap);
    }

    /**
     * The bitmap of this glyph.
     *
     * Glyphs are represented on-screen by a graphic. This graphic is a square grid of squares. With the top-left
     * square as (0, 0), the square at (x, y) is opaque if {@code bitmap[x][y]} is {@code true} and transparent
     * otherwise.
     */
    private final boolean[][] bitmap;

    /**
     * Create a glyph from a bitmap.
     * @param bitmap The glyph's bitmap. Must be a 2D array of shape {@link #SIZE} by {@link #SIZE}. See
     * {@link #bitmap}.
     */
    Glyph(final boolean[][] bitmap) {
        this.bitmap = new boolean[Glyph.SIZE][];
        for (int x = 0; x < Glyph.SIZE; x++) {
            this.bitmap[x] = Arrays.copyOf(bitmap[x], bitmap[x].length);
        }
    }
}
