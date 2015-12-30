package net.matthiasauer.stwp4j.libgdx.miniwar;

import java.util.Arrays;

import net.matthiasauer.stwp4j.Channel;
import net.matthiasauer.stwp4j.libgdx.application.ApplicationEntryPointProcess;
import net.matthiasauer.stwp4j.libgdx.application.ApplicationEvent;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventData;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderData;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderProcess;
import net.matthiasauer.stwp4j.libgdx.miniwar.model.test.TestDataConsumerProcess;
import net.matthiasauer.stwp4j.libgdx.miniwar.model.test.WorldInteraction;
import net.matthiasauer.stwp4j.libgdx.miniwar.model.test.WorldProcess;
import net.matthiasauer.stwp4j.libgdx.miniwar.model.test.WorldSnapShot;
import net.matthiasauer.stwp4j.libgdx.miniwar.view.GameGui;
import net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable.ClickEvent;
import net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable.InteractionClickableProcess;

public class EntryPoint extends ApplicationEntryPointProcess {
    @Override
    public void create() {
        // Plumbing - channels
        Channel<ApplicationEvent> applicationEventChannel = this.applicationEventChannel;
        Channel<RenderData> renderDataChannel = this.scheduler.createMultiplexChannel("render data-channel",
                RenderData.class);
        Channel<InputTouchEventData> inputTouchEventDataChannel = this.scheduler
                .createMultiplexChannel("input touch event data-channel", InputTouchEventData.class);
        Channel<WorldInteraction> worldInteractionChannel = this.scheduler
                .createMultiplexChannel("world-interaction channel", WorldInteraction.class);
        Channel<WorldSnapShot> worldSnapShotChannel = this.scheduler.createMultiplexChannel("world-snapshot channel",
                WorldSnapShot.class);
        Channel<ClickEvent> clickEventChannel = this.scheduler.createMultiplexChannel("click-event channel",
                ClickEvent.class);

        // Plumbing - processes
        scheduler.addProcess(new RenderProcess(Arrays.asList("data1.atlas"), true, renderDataChannel.createInPort(),
                applicationEventChannel.createInPort(), inputTouchEventDataChannel.createOutPort()));
        scheduler.addProcess(
                new WorldProcess(worldInteractionChannel.createInPort(), worldSnapShotChannel.createOutPort()));
        scheduler.addProcess(new InteractionClickableProcess(inputTouchEventDataChannel.createInPort(), clickEventChannel.createOutPort()));
        scheduler.addProcess(new GameGui(renderDataChannel.createOutPort(), clickEventChannel.createInPort()));
        
        scheduler.addProcess(new TestDataConsumerProcess(clickEventChannel.createInPort()));
    }
}
