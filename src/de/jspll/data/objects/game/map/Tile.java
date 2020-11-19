package de.jspll.data.objects.game.map;

import de.jspll.util.PaintingUtil;

import java.awt.image.BufferedImage;

public class Tile {
    private transient TileMap parent;
    private boolean collidable;
    private int[] textureReference;

    //Can Be Excluded
    private BufferedImage cache;

    public Tile(){
    }

    public Tile(boolean collidable, int[] textureReference, TileMap parent) {
        this.collidable = collidable;
        this.textureReference = textureReference;
        this.parent = parent;
    }

    public void setTextureReference(int[] textureReference) {
        this.textureReference = textureReference;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public void setParent(TileMap parent) {
        this.parent = parent;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public BufferedImage getTexture(TileMap gO, int width, int height) {
        if (parent == null)
            parent = gO;
        if (cache == null || cache.getWidth() != width || cache.getHeight() != height) {
            cache = PaintingUtil.resize(parent.tileSets[textureReference[4]].getSubimage(textureReference[0], textureReference[1], textureReference[2], textureReference[3]), width, height);
        }
        return cache;
    }

    public int[] getTextureReference() {
        return textureReference;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Tile){
            if( ((Tile) obj).getTextureReference() != null ){
                if( ((Tile) obj).isCollidable() != collidable )
                    return false;
                int[] arr = ((Tile) obj).getTextureReference();
                if(arr.length != textureReference.length)
                    return false;
                for(int i = 0; i < arr.length; i++){
                    if( arr[i] != textureReference[i]){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}