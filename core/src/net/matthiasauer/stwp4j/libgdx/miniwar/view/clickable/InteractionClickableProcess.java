package net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventData;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventType;
import net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable.ClickComponentEvent.ClickComponentType;

public class InteractionClickableProcess extends LightweightProcess {
    private final ChannelInPort<InputTouchEventData> inputTouchEventDataChannel;
    private final ChannelOutPort<ClickEvent> clickEventChannel;
    private final ChannelOutPort<ClickComponentEvent> clickComponentEventChannel;
    private final Pool<InputTouchEventData> inputTouchEventDataPool;
    private final Pool<ClickEvent> clickEventPool;
    private final Pool<ClickComponentEvent> clickComponentEventPool;

    public InteractionClickableProcess(ChannelInPort<InputTouchEventData> inputTouchEventDataChannel,
            ChannelOutPort<ClickEvent> clickEventChannel,
            ChannelOutPort<ClickComponentEvent> clickComponentEventChannel) {
        this.inputTouchEventDataChannel = inputTouchEventDataChannel;
        this.clickComponentEventChannel = clickComponentEventChannel;
        this.clickEventChannel = clickEventChannel;
        this.inputTouchEventDataPool = Pools.get(InputTouchEventData.class);
        this.clickEventPool = Pools.get(ClickEvent.class);
        this.clickComponentEventPool = Pools.get(ClickComponentEvent.class);
    }

    private InputTouchEventData lastEvent = null;
    private String lastEventId = null;
    private String currentEventId = null;

    private void sendClickComponentEvent(String id, ClickComponentType type) {
        ClickComponentEvent clickComponentEvent = this.clickComponentEventPool.obtain();

        clickComponentEvent.set(id, type);

        this.clickComponentEventChannel.offer(clickComponentEvent);
    }

    private void checkForLeftEvent() {
        boolean triggerLeftEvent = false;

        // if before we were on an entity - but now not anymore !
        if ((this.lastEventId != null) && (this.currentEventId == null)) {
            triggerLeftEvent = true;
        }

        // if before we were on an entity - but now on another
        if ((this.lastEventId != null) && (this.currentEventId != null)
                && (!this.lastEventId.equalsIgnoreCase(this.currentEventId))) {
            triggerLeftEvent = true;
        }
        
        if (triggerLeftEvent) {
            this.sendClickComponentEvent(this.lastEventId, ClickComponentType.Left);
        }
    }

    private void checkForOverEvent() {
        boolean triggerOverEvent = false;

        // if before we were on no entity - but now we are !
        if ((this.lastEventId == null) && (this.currentEventId != null)) {
            triggerOverEvent = true;
        }

        // if before we were on an entity - but now on another
        if ((this.lastEventId != null) && (this.currentEventId != null)
                && (!this.lastEventId.equalsIgnoreCase(this.currentEventId))) {
            triggerOverEvent = true;
        }
        
        if (triggerOverEvent) {
            this.sendClickComponentEvent(this.currentEventId, ClickComponentType.Over);
        }
    }

    @Override
    protected void execute() {
        InputTouchEventData data = null;

        while ((data = this.inputTouchEventDataChannel.poll()) != null) {
            this.lastEventId = this.currentEventId;
            this.currentEventId = data.getTouchedRenderDataId();
            
            this.checkForLeftEvent();
            this.checkForOverEvent();
            
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
                    this.sendClickComponentEvent(currentEventId, ClickComponentType.Down);
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
                        this.sendClickComponentEvent(currentEventId, ClickComponentType.Up);
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
