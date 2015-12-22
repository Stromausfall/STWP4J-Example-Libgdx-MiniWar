package net.matthiasauer.stwp4j.libgdx.miniwar.gui.interaction;

import com.badlogic.gdx.utils.Pools;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.ChannelPortsCreated;
import net.matthiasauer.stwp4j.ChannelPortsRequest;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.PortType;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventData;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderProcess;

public class GuiInteractionProcess extends LightweightProcess {
    public static final String CLICKEVENT_CHANNEL = "clickevent-channel";
    private ChannelInPort<InputTouchEventData> inputTouchEventDataChannel;
    private ChannelOutPort<ClickEvent> clickEventChannel;

    public GuiInteractionProcess() {
        super(new ChannelPortsRequest<InputTouchEventData>(RenderProcess.INPUTTOUCHEVENTDATA_CHANNEL,
                PortType.InputMultiplex, InputTouchEventData.class),
                new ChannelPortsRequest<ClickEvent>(CLICKEVENT_CHANNEL, PortType.OutputExclusive, ClickEvent.class));
    }

    int bla = 0;

    @Override
    protected void execute() {
        InputTouchEventData data = null;

        while ((data = this.inputTouchEventDataChannel.poll()) != null) {
            System.err.println("oi oi > " + (bla++) + " - " + (data == null));

            if (data != null) {
                ClickEvent clickEvent = Pools.get(ClickEvent.class).obtain();
                clickEvent.set(data.getTouchedRenderDataId());

                this.clickEventChannel.offer(clickEvent);
            }
        }
    }

    @Override
    protected void initialize(ChannelPortsCreated createdChannelPorts) {
        this.inputTouchEventDataChannel = createdChannelPorts
                .getChannelInPort(RenderProcess.INPUTTOUCHEVENTDATA_CHANNEL, InputTouchEventData.class);
        this.clickEventChannel = createdChannelPorts.getChannelOutPort(CLICKEVENT_CHANNEL, ClickEvent.class);
    }

}
