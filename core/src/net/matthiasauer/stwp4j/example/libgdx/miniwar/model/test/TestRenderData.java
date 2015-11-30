package net.matthiasauer.stwp4j.example.libgdx.miniwar.model.test;

import net.matthiasauer.stwp4j.example.libgdx.miniwar.gui.render.RenderData;

public class TestRenderData implements RenderData {
    private String bitmap;
    private int x;
    private int y;
    
    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    @Override
    public String getBitmap() {
        return this.bitmap;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }
}
