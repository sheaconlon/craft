package com.sheaconlon.realcraft.renderer;

import java.util.Arrays;

/**
 * A text box.
 */
public class TextBox {
    /**
     * The contents of this text box.
     */
    private Glyph[] contents;

    /**
     * The position of this text box.
     */
    private final double[] position;

    /**
     * Create a text box.
     * @param contents
     * @param position
     */
    public TextBox(final Glyph[] contents, final double[] position) {
        this.contents = Arrays.copyOf(contents, contents.length);
        this.position = Arrays.copyOf(position, position.length);
    }

    public Glyph[] getContents() {
        return this.contents;
    }

    public void setContents(final Glyph[] contents) {
        this.contents = Arrays.copyOf(contents, contents.length);
    }
}
