package net.matthiasauer.stwp4j.libgdx.graphic;

import com.badlogic.gdx.math.Vector2;

public class UnprojectRequest {
    private final Vector2 data = new Vector2();
    
    public void set(Vector2 data) {
        this.data.set(data);
    }
    
    public void set(float x, float y) {
        this.data.x = x;
        this.data.y = y;
    }
    
    public Vector2 get() {
        return this.data;
    }
}
