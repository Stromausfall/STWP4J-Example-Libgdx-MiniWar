package net.matthiasauer.stwp4j.example.libgdx.miniwar.gui.render;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelPortsCreated;
import net.matthiasauer.stwp4j.ChannelPortsRequest;
import net.matthiasauer.stwp4j.ExecutionState;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.PortType;

public class RenderProcess extends LightweightProcess {    
    private ChannelInPort<RenderData> renderDataChannel;
    private SpriteBatch spriteBatch = new SpriteBatch();
    private Map<String, Texture> textures = new HashMap<String, Texture>();
    
    public RenderProcess() {
        super(
                new ChannelPortsRequest<RenderData>(
                        "renderdata-channel",
                        PortType.InputExclusive,
                        RenderData.class));
    }
    
    private Texture getTexture(String name) {
        if (!textures.containsKey(name)) {
            Texture texture = new Texture(name);
            textures.put(name, texture);
        }
        
        return textures.get(name);
    }

    @Override
    protected void initialize(ChannelPortsCreated createdChannelPorts) {
        this.renderDataChannel =
                createdChannelPorts.getChannelInPort("renderdata-channel", RenderData.class);
    }

    @Override
    protected void preIteration() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        this.spriteBatch.begin();
    }
    
    @Override
    protected ExecutionState execute() {
        RenderData data = this.renderDataChannel.poll();
        
        if (data == null) {
            // we wait for input
            return ExecutionState.Waiting;
        }
        
        Texture texture = getTexture(data.getBitmap());

        this.spriteBatch.draw(texture, data.getX(), data.getY());
        
        return ExecutionState.Waiting;
    }
    
    @Override
    protected void postIteration() {
        this.spriteBatch.end();
    }
}
