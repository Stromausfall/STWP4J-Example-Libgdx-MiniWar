package net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable;

import com.badlogic.gdx.utils.Pool.Poolable;

public class ClickComponentEvent implements Poolable {
    public static enum ClickComponentType {
        Over, Down, Up, Left
    }

    private String id;
    private ClickComponentType type;

    public ClickComponentEvent set(String id, ClickComponentType type) {
        this.id = id;
        this.type = type;

        return this;
    }

    public String getId() {
        return this.id;
    }

    public ClickComponentType getClickComponentType() {
        return this.type;
    }

    @Override
    public void reset() {
        this.set(null, null);
    }
}
