package net.matthiasauer.stwp4j.example.libgdx.miniwar.model.test;

import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.ChannelPortsCreated;
import net.matthiasauer.stwp4j.ChannelPortsRequest;
import net.matthiasauer.stwp4j.ExecutionState;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.PortType;
import net.matthiasauer.stwp4j.libgdx.gui.RenderData;
import net.matthiasauer.stwp4j.libgdx.gui.RenderPositionUnit;
import net.matthiasauer.stwp4j.libgdx.gui.SpriteRenderData;

public class TestDataCreatorProcess extends LightweightProcess {
    private ChannelOutPort<RenderData> renderDataChannel;
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
        SpriteRenderData data = new SpriteRenderData();
        data.set(startX, startY, 0, RenderPositionUnit.Pixels, null, 1, true, "normalCity");

        this.renderDataChannel.offer(data);
        
        return ExecutionState.Finished;
    }

    @Override
    public void initialize(ChannelPortsCreated createdChannelPorts) {
        this.renderDataChannel =
                createdChannelPorts.getChannelOutPort("renderdata-channel", RenderData.class);
    }
}
