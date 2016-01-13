package net.matthiasauer.stwp4j.libgdx.miniwar.controller.ui;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEvent;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventType;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderData;

public class ButtonProcess extends LightweightProcess {
    private final ChannelOutPort<RenderData> renderOutput;
    private final ChannelOutPort<ButtonClickEvent> buttonClickOutput;
    private final ChannelInPort<InputTouchEvent> touchEventInput;
    private final RenderData baseState;
    private final RenderData overState;
    private final RenderData downState;
    private final String id;
    private RenderData currentState;

    private boolean mouseIsDown;
    private String mouseDownOnEntity;

    public ButtonProcess(ChannelOutPort<RenderData> renderOutput, ChannelInPort<InputTouchEvent> touchEventInput,
            ChannelOutPort<ButtonClickEvent> buttonClickOutput, RenderData baseState, RenderData overState,
            RenderData downState) {
        this.mouseIsDown = false;
        this.mouseDownOnEntity = null;
        this.renderOutput = renderOutput;
        this.touchEventInput = touchEventInput;
        this.buttonClickOutput = buttonClickOutput;
        this.baseState = baseState;
        this.overState = overState;
        this.downState = downState;

        if (!this.baseState.getId().equals(this.overState.getId())
                || (!this.overState.getId().equals(this.downState.getId()))) {
            throw new IllegalArgumentException("all RenderData must have the same ID !");
        }

        this.id = this.baseState.getId();
        this.currentState = this.baseState;
    }

    @Override
    protected void execute() {
        InputTouchEvent inputTouchEventData = null;

        while ((inputTouchEventData = this.touchEventInput.poll()) != null) {
            final String targetId = inputTouchEventData.getTouchedRenderDataId();
            final InputTouchEventType eventType = inputTouchEventData.getInputTouchEventType();

            if ((this.mouseIsDown == false) && (inputTouchEventData.isTouched())) {
                this.mouseIsDown = true;
                this.mouseDownOnEntity = targetId;
            }

            if (!inputTouchEventData.isTouched()) {
                this.mouseDownOnEntity = null;
                this.mouseIsDown = false;
            }

            boolean reactToMouse = true;
            boolean stillClickedDownOnThis = false;
            if (this.mouseIsDown) {
                if (this.mouseDownOnEntity != null) {
                    if (!this.mouseDownOnEntity.equals(this.id)) {
                        reactToMouse = false;
                    } else {
                        stillClickedDownOnThis = true;
                    }
                } else {
                    reactToMouse = false;
                }
            }

            if (reactToMouse) {
                // if the event targets THIS button
                if ((targetId != null) && (targetId.equals(this.id))) {
                    // DOWN event
                    if (eventType == InputTouchEventType.TouchDown) {
                        this.currentState = this.downState;
                    } else {
                        if ((eventType == InputTouchEventType.TouchUp) && (this.currentState == this.downState)) {
                            ButtonClickEvent buttonClickEvent = new ButtonClickEvent();
                            buttonClickEvent.set(targetId);

                            this.buttonClickOutput.offer(buttonClickEvent);
                        }

                        if (inputTouchEventData.isTouched()) {
                            this.currentState = this.downState;
                        } else {
                            this.currentState = this.overState;
                        }
                    }
                } else {
                    // event doesn't target THIS button
                    if (!stillClickedDownOnThis) {
                        this.currentState = this.baseState;
                    }
                }
            } else {
                this.currentState = this.baseState;
            }
        }

    }

    @Override
    protected void postIteration() {
        this.renderOutput.offer(this.currentState);
    }
}
