package net.matthiasauer.stwp4j.libgdx.graphic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Pools;

import net.matthiasauer.stwp4j.ChannelOutPort;

class RenderSpriteSubSystem {
	private final OrthographicCamera camera;
	private final SpriteBatch spriteBatch;
	private final TextureLoader textureLoader;
	
	public RenderSpriteSubSystem(
	        TextureLoader textureLoader,
			OrthographicCamera camera,
			SpriteBatch spriteBatch) {
		this.camera = camera;
		this.spriteBatch = spriteBatch;
		this.textureLoader = textureLoader;
	}
	
	public void drawSprite(SpriteRenderData data, ChannelOutPort<RenderedData> renderedDataChannel) {
	    final AtlasRegion texture =
	            this.textureLoader.getTexture(data.getTextureName());
	    final Color tint = data.getTint();
		float actualPositionX =
				RenderPositionUnitTranslator.translateX(
				        data.getPosition().x,
				        data.getPosition().y,
				        data.getRenderPositionUnit()) - texture.getRegionWidth() / 2;
		float actualPositionY =
				RenderPositionUnitTranslator.translateY(
				        data.getPosition().x,
				        data.getPosition().y,
				        data.getRenderPositionUnit()) - texture.getRegionHeight() / 2;
		float width = texture.getRegionWidth();
		float height = texture.getRegionHeight();
		float originX = width/2;
		float originY = height/2;
		
		if (!data.isRenderProjected()) {
			actualPositionX *= this.camera.zoom;
			actualPositionY *= this.camera.zoom;
			originX *= this.camera.zoom;
			originY *= this.camera.zoom;
			width *= this.camera.zoom;
			height *= this.camera.zoom;
		}
		
		Color base = this.spriteBatch.getColor();
		
		if (tint != null) {
			this.spriteBatch.setColor(tint);
		}

		this.spriteBatch.draw(
				texture,
				actualPositionX,
				actualPositionY,
				originX,
				originY,
				width,
				height,
				1,
				1,
				data.getRotation());
		
		if (tint != null) {
			this.spriteBatch.setColor(base);
		}
		
        renderedDataChannel.offer(
        		Pools.get(RenderedData.class).obtain().set(
        		        actualPositionX,
        				actualPositionY,
        				texture.getRegionWidth(),
        				texture.getRegionHeight(),
        				this.camera.zoom));
	}

}
