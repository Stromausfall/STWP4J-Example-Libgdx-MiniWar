package net.matthiasauer.stwp4j.libgdx.miniwar.view;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderData;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderPositionUnit;
import net.matthiasauer.stwp4j.libgdx.graphic.SpriteRenderData;
import net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable.ClickEvent;

public class GameGui extends LightweightProcess {
    private final ChannelOutPort<RenderData> renderDataChannel;
    private final ChannelInPort<ClickEvent> clickEventChannel;

    public GameGui(ChannelOutPort<RenderData> renderDataChannel, ChannelInPort<ClickEvent> clickEventChannel) {
        this.renderDataChannel = renderDataChannel;
        this.clickEventChannel = clickEventChannel;
    }
    
    private RenderData createDummyElement() {
        SpriteRenderData data = new SpriteRenderData();
        data.set("dummy-id", 0, 0, 0, RenderPositionUnit.Pixels, null, 1, true, "normalCity");

        return data;
    }
    
    @Override
    protected void preIteration() {
        this.renderDataChannel.offer(this.createDummyElement());
    }

    @Override
    protected void execute() {
        while (this.clickEventChannel.poll() != null);
    }
}
