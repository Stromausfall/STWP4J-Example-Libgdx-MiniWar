package net.matthiasauer.stwp4j.libgdx.miniwar.gui.interaction;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.ChannelPortsCreated;
import net.matthiasauer.stwp4j.ChannelPortsRequest;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.PortType;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventData;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderProcess;

abstract class TestProcess extends LightweightProcess {
    public static final String ENTITY_ID = "entity#1";

    public TestProcess() {
        super(new ChannelPortsRequest<InputTouchEventData>(RenderProcess.INPUTTOUCHEVENTDATA_CHANNEL,
                PortType.OutputExclusive, InputTouchEventData.class),
                new ChannelPortsRequest<ClickEvent>(GuiInteractionProcess.CLICKEVENT_CHANNEL, PortType.InputExclusive,
                        ClickEvent.class));
    }

    ChannelInPort<ClickEvent> clickEventChannel;
    ChannelOutPort<InputTouchEventData> inputTouchEventDataChannel;

    @Override
    protected void initialize(ChannelPortsCreated createdChannelPorts) {
        this.clickEventChannel = createdChannelPorts.getChannelInPort(GuiInteractionProcess.CLICKEVENT_CHANNEL,
                ClickEvent.class);
        this.inputTouchEventDataChannel = createdChannelPorts
                .getChannelOutPort(RenderProcess.INPUTTOUCHEVENTDATA_CHANNEL, InputTouchEventData.class);
    }

    @Override
    protected abstract void execute();
}
