package net.matthiasauer.stwp4j.libgdx.miniwar.gui;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.ChannelPortsCreated;
import net.matthiasauer.stwp4j.ChannelPortsRequest;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.PortType;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderData;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderProcess;
import net.matthiasauer.stwp4j.libgdx.miniwar.gui.interaction.ClickEvent;
import net.matthiasauer.stwp4j.libgdx.miniwar.gui.interaction.GuiInteractionProcess;

public class GameGui extends LightweightProcess {
    private static final ChannelPortsRequest<RenderData> renderDataChannelRequest = new ChannelPortsRequest<RenderData>(
            RenderProcess.RENDERDATA_CHANNEL, PortType.OutputExclusive, RenderData.class);
    private static final ChannelPortsRequest<ClickEvent> clickEventChannelRequest = new ChannelPortsRequest<ClickEvent>(
            GuiInteractionProcess.CLICKEVENT_CHANNEL, PortType.InputExclusive, ClickEvent.class);
    private ChannelOutPort<RenderData> renderDataChannel;
    private ChannelInPort<ClickEvent> clickEventChannel;

    public GameGui() {
        super(renderDataChannelRequest, clickEventChannelRequest);
    }

    @Override
    protected void execute() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void initialize(ChannelPortsCreated createdChannelPorts) {
        this.renderDataChannel = createdChannelPorts.getChannelOutPort(RenderProcess.RENDERDATA_CHANNEL,
                RenderData.class);
        this.clickEventChannel = createdChannelPorts.getChannelInPort(GuiInteractionProcess.CLICKEVENT_CHANNEL,
                ClickEvent.class);
    }
}
