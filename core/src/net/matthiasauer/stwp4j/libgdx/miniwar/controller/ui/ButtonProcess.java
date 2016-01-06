package net.matthiasauer.stwp4j.libgdx.miniwar.controller.ui;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventData;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderData;

public class ButtonProcess extends LightweightProcess {
    private final ChannelOutPort<RenderData> renderOutput;
    private final ChannelInPort<InputTouchEventData> touchEventInput;
    private final RenderData baseState;
    private final RenderData overState;
    private final RenderData downState;
    
    public ButtonProcess(ChannelOutPort<RenderData> renderOutput, ChannelInPort<InputTouchEventData> touchEventInput, RenderData baseState, RenderData overState, RenderData downState) {
        this.renderOutput = renderOutput;
        this.touchEventInput = touchEventInput;
        this.baseState = baseState;
        this.overState = overState;
        this.downState = downState;
    }
    
    @Override
    protected void preIteration() {
        this.renderOutput.offer(this.baseState);
    }
    
    @Override
    protected void execute() {
    }

}
