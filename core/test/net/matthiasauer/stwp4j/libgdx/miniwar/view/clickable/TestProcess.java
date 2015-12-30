package net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventData;
import net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable.ClickEvent;

abstract class TestProcess extends LightweightProcess {
    public static final String ENTITY_ID = "entity#1";
    protected final ChannelInPort<ClickEvent> clickEventChannel;
    protected final ChannelOutPort<InputTouchEventData> inputTouchEventDataChannel;

    public TestProcess(
            ChannelInPort<ClickEvent> clickEventChannel,
            ChannelOutPort<InputTouchEventData> inputTouchEventDataChannel) {
        this.clickEventChannel = clickEventChannel;
        this.inputTouchEventDataChannel = inputTouchEventDataChannel;
    }

    @Override
    protected abstract void execute();
}
