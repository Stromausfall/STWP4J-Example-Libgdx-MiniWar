package net.matthiasauer.stwp4j.example.libgdx.miniwar.model.test;

import java.util.Random;

import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.ChannelPortsCreated;
import net.matthiasauer.stwp4j.ChannelPortsRequest;
import net.matthiasauer.stwp4j.ExecutionState;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.PortType;
import net.matthiasauer.stwp4j.example.libgdx.miniwar.gui.render.RenderData;

public class TestDataCreatorProcess extends LightweightProcess {
    private ChannelOutPort<RenderData> renderDataChannel;
    private Random random = new Random();
    private TestRenderData renderData = new TestRenderData();
    private final int startX;
    private final int startY;

    public TestDataCreatorProcess(int startX, int startY) {
        super(
                new ChannelPortsRequest<RenderData>(
                        "renderdata-channel",
                        PortType.Output,
                        RenderData.class));
        
        this.startX = startX;
        this.startY = startY;
    }

    @Override
    public ExecutionState execute() {
        this.renderData.setBitmap("badlogic.jpg");
        
        // limit
        this.renderData.setX(Math.max(startX, Math.min(startX + 200, this.renderData.getX() + (1 - this.random.nextInt(3)))));
        this.renderData.setY(Math.max(startY, Math.min(startY + 200, this.renderData.getY() + (1 - this.random.nextInt(3)))));
        
        this.renderDataChannel.offer(this.renderData);
        
        return ExecutionState.Finished;
    }

    @Override
    public void initialize(ChannelPortsCreated createdChannelPorts) {
        this.renderDataChannel =
                createdChannelPorts.getChannelOutPort("renderdata-channel", RenderData.class);
    }
}
