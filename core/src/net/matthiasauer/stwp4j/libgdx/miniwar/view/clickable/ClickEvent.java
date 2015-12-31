package net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable;

import com.badlogic.gdx.utils.Pool.Poolable;

public class ClickEvent implements Poolable {
    private String id;
    
    public ClickEvent set(String id) {
        this.id = id;
        
        return this;
    }
    
    public String getId() {
        return this.id;
    }
    
    @Override
    public void reset() {
        this.set(null);
    }
}
