package net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;

import net.matthiasauer.stwp4j.libgdx.graphic.RenderData;

public class DisplayClickableRequest implements Poolable {
    private RenderData baseState;
    private RenderData touchedState;
    private RenderData clickState;
    
    public DisplayClickableRequest() {
    }
    
    public DisplayClickableRequest set(RenderData baseState, RenderData touchedState, RenderData clickState) {
        if ((baseState == null) && (this.baseState != null)) {
            Pools.free(this.baseState);
        }
        if ((baseState == null) && (this.baseState != null)) {
            Pools.free(this.touchedState);
        }
        if ((baseState == null) && (this.baseState != null)) {
            Pools.free(this.clickState);
        }
        
        this.baseState = baseState;
        this.touchedState = touchedState;
        this.clickState = clickState;

        return this;
    }
    
    public RenderData getBaseState() {
        return this.baseState;
    }
    
    public RenderData getTouchedState() {
        return this.touchedState;
    }
    
    public RenderData getClickState() {
        return this.clickState;
    }
    
    @Override
    public void reset() {
        this.set(null, null, null);
    }
}
