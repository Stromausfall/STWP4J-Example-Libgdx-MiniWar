package net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable;

public class ClickEvent {
    private String id;
    
    public ClickEvent set(String id) {
        this.id = id;
        
        return this;
    }
    
    public String getId() {
        return this.id;
    }
}
