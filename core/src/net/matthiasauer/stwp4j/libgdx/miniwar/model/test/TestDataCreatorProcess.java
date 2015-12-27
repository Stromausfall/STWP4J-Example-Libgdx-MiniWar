package net.matthiasauer.stwp4j.libgdx.miniwar.model.test;

import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderData;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderPositionUnit;
import net.matthiasauer.stwp4j.libgdx.graphic.SpriteRenderData;

public class TestDataCreatorProcess extends LightweightProcess {
    private final ChannelOutPort<RenderData> renderDataChannel;
    private final int startX;
    private final int startY;
    private final String renderId;

    public TestDataCreatorProcess(int startX, int startY, ChannelOutPort<RenderData> renderDataChannel) {
        this.renderDataChannel = renderDataChannel;
        this.startX = startX;
        this.startY = startY;
        this.renderId = "id" + (id++);
    }

    private static int id = 0;

    @Override
    protected void preIteration() {
        SpriteRenderData data = new SpriteRenderData();
        data.set(this.renderId, startX, startY, 0, RenderPositionUnit.Pixels, null, 1, true, "normalCity");

        this.renderDataChannel.offer(data);
    }

    @Override
    public void execute() {
    }
}
