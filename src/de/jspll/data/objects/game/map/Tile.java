package de.jspll.data.objects.game.map;

import de.jspll.util.PaintingUtil;

import java.awt.image.BufferedImage;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Laura Schmidt, Philipp Polland
 * @version 1.0
 */
public class Tile {
    private transient TileMap parent;
    private boolean collidable;
    private int[] textureReference;

    //Can Be Excluded
    private BufferedImage cache;
    private int tW, tH;

    public Tile() {
    }

    public Tile(boolean collidable, int[] textureReference, TileMap parent) {
        this.collidable = collidable;
        this.textureReference = textureReference;
        this.parent = parent;
    }

    public void setParent(TileMap parent) {
        this.parent = parent;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public BufferedImage getTexture(TileMap gO, int width, int height) {
        if (parent == null)
            parent = gO;
        if (cache == null || tW != width || tH != height) {
            cache = PaintingUtil.resize(parent.tileSets[textureReference[4]].getSubimage(textureReference[0],
                    textureReference[1], textureReference[2], textureReference[3]), width, height);
            tW = width;
            tH = height;
        }
        return cache;
    }

    public int[] getTextureReference() {
        return textureReference;
    }

    public void setTextureReference(int[] textureReference) {
        this.textureReference = textureReference;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile) {
            if (((Tile) obj).getTextureReference() != null) {
                if (((Tile) obj).isCollidable() != collidable)
                    return false;
                int[] arr = ((Tile) obj).getTextureReference();
                if (arr.length != textureReference.length)
                    return false;
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i] != textureReference[i]) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
