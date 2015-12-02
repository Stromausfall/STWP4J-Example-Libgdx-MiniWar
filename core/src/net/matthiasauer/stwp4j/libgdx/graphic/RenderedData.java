package net.matthiasauer.stwp4j.libgdx.graphic;

import com.badlogic.gdx.math.Rectangle;

public class RenderedData {
	private final Rectangle renderedTarget = new Rectangle();
	private float zoomFactor;
	
	RenderedData() {
    }
	
	public RenderedData set(float x, float y, float width, float height, float zoomFactor) {
		this.renderedTarget.x = x;
		this.renderedTarget.y = y;
		this.renderedTarget.width = width;
		this.renderedTarget.height = height;
		this.zoomFactor = zoomFactor;
		
		return this;
	}
	
	public Rectangle getRenderedTarget() {
	    return this.renderedTarget;
	}
	
	public float getZoomFactor() {
	    return this.zoomFactor;
	}
}
