package net.matthiasauer.stwp4j.libgdx.miniwar.gui.interaction;

import com.badlogic.gdx.utils.Pools;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventData;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventType;

public class GuiInteractionProcess extends LightweightProcess {
    private final ChannelInPort<InputTouchEventData> inputTouchEventDataChannel;
    private final ChannelOutPort<ClickEvent> clickEventChannel;

    public GuiInteractionProcess(ChannelInPort<InputTouchEventData> inputTouchEventDataChannel,
            ChannelOutPort<ClickEvent> clickEventChannel) {
        this.inputTouchEventDataChannel = inputTouchEventDataChannel;
        this.clickEventChannel = clickEventChannel;
    }

    private InputTouchEventData lastEvent = null;

    @Override
    protected void execute() {
        InputTouchEventData data = null;

        while ((data = this.inputTouchEventDataChannel.poll()) != null) {
            // if we have another TouchDown - remove the process we made until
            // now
            if (this.lastEvent != null) {
                if (data.getInputTouchEventType() == InputTouchEventType.TouchDown) {
                    this.lastEvent = null;
                }
            }

            // only take it if it is a DOWN event !
            if (this.lastEvent == null) {
                if (data.getInputTouchEventType() == InputTouchEventType.TouchDown) {
                    this.lastEvent = data;
                }
            } else {
                if (data.getInputTouchEventType() == InputTouchEventType.TouchUp) {
                    final String lastId = this.lastEvent.getTouchedRenderDataId();
                    final String currentId = data.getTouchedRenderDataId();

                    // only if both entities have the same id !
                    if (lastId.equalsIgnoreCase(currentId)) {
                        ClickEvent clickEvent = Pools.get(ClickEvent.class).obtain();
                        clickEvent.set(data.getTouchedRenderDataId());

                        this.clickEventChannel.offer(clickEvent);
                    }

                    // reset !
                    this.lastEvent = null;
                }
            }
        }
    }
}
