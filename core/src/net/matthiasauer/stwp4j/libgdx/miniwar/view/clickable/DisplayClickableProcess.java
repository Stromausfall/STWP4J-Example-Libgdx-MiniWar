package net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderData;

public class DisplayClickableProcess extends LightweightProcess {
    private final ChannelInPort<DisplayClickableRequest> displayClickableRequestChannel;
    private final ChannelInPort<ClickComponentEvent> clickComponentEventChannel;
    private final ChannelOutPort<RenderData> renderDataChannel;

    public DisplayClickableProcess(ChannelOutPort<RenderData> renderDataChannel,
            ChannelInPort<ClickComponentEvent> clickComponentEventChannel,
            ChannelInPort<DisplayClickableRequest> displayClickableRequestChannel) {
        this.displayClickableRequestChannel = displayClickableRequestChannel;
        this.clickComponentEventChannel = clickComponentEventChannel;
        this.renderDataChannel = renderDataChannel;
    }

    @Override
    protected void execute() {
        // TODO Auto-generated method stub

    }

}
