package net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventData;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventType;

public class InteractionClickableProcess extends LightweightProcess {
    private final ChannelInPort<InputTouchEventData> inputTouchEventDataChannel;
    private final ChannelOutPort<ClickEvent> clickEventChannel;
    private final Pool<InputTouchEventData> inputTouchEventDataPool;
    private final Pool<ClickEvent> clickEventPool;

    public InteractionClickableProcess(ChannelInPort<InputTouchEventData> inputTouchEventDataChannel,
            ChannelOutPort<ClickEvent> clickEventChannel) {
        this.inputTouchEventDataChannel = inputTouchEventDataChannel;
        this.clickEventChannel = clickEventChannel;
        this.inputTouchEventDataPool = Pools.get(InputTouchEventData.class);
        this.clickEventPool = Pools.get(ClickEvent.class);
    }

    private InputTouchEventData lastEvent = null;

    @Override
    protected void execute() {
        InputTouchEventData data = null;

        while ((data = this.inputTouchEventDataChannel.poll()) != null) {
            // we clicked somewhere other than an entity - remove all progress
            if (data.getTouchedRenderDataId() == null) {
                this.lastEvent = null;
                continue;
            }
            
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
                        ClickEvent clickEvent = this.clickEventPool.obtain();
                        clickEvent.set(data.getTouchedRenderDataId());

                        this.clickEventChannel.offer(clickEvent);
                    }

                    // reset !
                    this.inputTouchEventDataPool.free(this.lastEvent);
                    this.lastEvent = null;
                }

                this.inputTouchEventDataPool.free(data);
            }
        }
    }
}
