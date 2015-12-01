package net.matthiasauer.stwp4j.libgdx.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public abstract class RenderData {
    private final Vector2 position = new Vector2();
    private float rotation;
    private RenderPositionUnit renderPositionUnit;
    private Color tint;
    private int renderOrder;
    private boolean renderProjected;
    private final RenderedData renderedData = new RenderedData();
    
    protected void set(
            float positionX,
            float positionY,
            float rotation,
            RenderPositionUnit renderPositionUnit,
            Color tint,
            int renderOrder,
            boolean renderProjected) {
        this.position.set(positionX, positionY);
        this.rotation = rotation;
        this.renderPositionUnit = renderPositionUnit;
        this.tint = tint;
        this.renderOrder = renderOrder;
        this.renderProjected = renderProjected;
    }
    
    public RenderedData getRenderedData() {
        return this.renderedData;
    }

    public Vector2 getPosition() {
        return this.position;
    }
    
    public float getRotation() {
        return this.rotation;
    }
    
    public RenderPositionUnit getRenderPositionUnit() {
        return this.renderPositionUnit;
    }
    
    public Color getTint() {
        return this.tint;
    }
    
    public int getRenderOrder() {
        return this.renderOrder;
    }
    
    public boolean isRenderProjected() {
        return this.renderProjected;
    }
}
