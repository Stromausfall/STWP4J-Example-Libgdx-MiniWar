package net.matthiasauer.stwp4j.libgdx.miniwar;

import java.util.Arrays;

import net.matthiasauer.stwp4j.Channel;
import net.matthiasauer.stwp4j.libgdx.application.ApplicationEntryPointProcess;
import net.matthiasauer.stwp4j.libgdx.application.ApplicationEvent;
import net.matthiasauer.stwp4j.libgdx.graphic.CameraChangeEvent;
import net.matthiasauer.stwp4j.libgdx.graphic.CameraStatusEvent;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEvent;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderData;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderPositionUnit;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderProcess;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderProcess.ResizeBehavior;
import net.matthiasauer.stwp4j.libgdx.graphic.SpriteRenderData;
import net.matthiasauer.stwp4j.libgdx.miniwar.controller.WorldInteraction;
import net.matthiasauer.stwp4j.libgdx.miniwar.controller.WorldInteractionProcess;
import net.matthiasauer.stwp4j.libgdx.miniwar.model.WorldProcess;
import net.matthiasauer.stwp4j.libgdx.miniwar.model.WorldSnapShot;
import net.matthiasauer.stwp4j.libgdx.miniwar.view.WorldDisplayProcess;
import net.matthiasauer.stwp4j.libgdx.ui.ButtonClickEvent;
import net.matthiasauer.stwp4j.libgdx.ui.ButtonProcess;

public class EntryPoint extends ApplicationEntryPointProcess {
    public EntryPoint() {
        super(true, false);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void create() {
        // Plumbing - channels
        Channel<ApplicationEvent> applicationEventChannel = this.applicationEventChannel;
        Channel<RenderData> renderDataChan = this.scheduler.createMultiplexChannel("render data-channel",
                RenderData.class, true, false);
        Channel<InputTouchEvent> inputTouchEventDataChannel = this.scheduler
                .createMultiplexChannel("input touch event-channel", InputTouchEvent.class, true, false);
        final Channel<ButtonClickEvent> buttonClickChan = this.scheduler
                .createMultiplexChannel("button click event data-channel", ButtonClickEvent.class, true, false);

        Channel<WorldInteraction> worldInteractionChan = this.scheduler
                .createMultiplexChannel("world-interaction channel", WorldInteraction.class, true, false);
        Channel<WorldSnapShot> worldSnapShotChan = this.scheduler.createMultiplexChannel("world-snapshot channel",
                WorldSnapShot.class, true, false);

        Channel<CameraStatusEvent> cameraStatusEventChannel = this.scheduler
                .createMultiplexChannel("camera status event channel", CameraStatusEvent.class, false, true);
        Channel<CameraChangeEvent> cameraChangeEventChannel = this.scheduler
                .createMultiplexChannel("camera status change channel", CameraChangeEvent.class, false, true);

        // Render Process
        scheduler.addProcess(new RenderProcess(Arrays.asList("data1.atlas"), true, 640, 480,
                ResizeBehavior.KeepResolutionKeepAspect, renderDataChan.createInPort(),
                applicationEventChannel.createInPort(), inputTouchEventDataChannel.createOutPort(),
                cameraChangeEventChannel.createInPort(), cameraStatusEventChannel.createOutPort()));
        
        // World Processes
        scheduler.addProcess(
                new WorldProcess(worldInteractionChan.createInPort(), worldSnapShotChan.createOutPort()));
        scheduler.addProcess(
                new WorldDisplayProcess(renderDataChan.createOutPort(), worldSnapShotChan.createInPort()));
        scheduler.addProcess(
                new WorldInteractionProcess(worldInteractionChan.createOutPort(), buttonClickChan.createInPort()));

        // Button Processes
        scheduler.addProcess(new ButtonProcess(renderDataChan.createOutPort(),
                inputTouchEventDataChannel.createInPort(), buttonClickChan.createOutPort(),
                new SpriteRenderData("increase_industry", -280, 200, 45, RenderPositionUnit.Pixels, null, 0, true,
                        "button_base"),
                new SpriteRenderData("increase_industry", -280, 200, 45, RenderPositionUnit.Pixels, null, 0, true,
                        "button_over"),
                new SpriteRenderData("increase_industry", -280, 200, 45, RenderPositionUnit.Pixels, null, 0, true,
                        "button_down")));
        scheduler.addProcess(new ButtonProcess(renderDataChan.createOutPort(),
                inputTouchEventDataChannel.createInPort(), buttonClickChan.createOutPort(),
                new SpriteRenderData("increase_army", -280, 130, 45, RenderPositionUnit.Pixels, null, 0, true,
                        "button_base"),
                new SpriteRenderData("increase_army", -280, 130, 45, RenderPositionUnit.Pixels, null, 0, true,
                        "button_over"),
                new SpriteRenderData("increase_army", -280, 130, 45, RenderPositionUnit.Pixels, null, 0, true,
                        "button_down")));
        scheduler.addProcess(new ButtonProcess(renderDataChan.createOutPort(),
                inputTouchEventDataChannel.createInPort(), buttonClickChan.createOutPort(),
                new SpriteRenderData("attack", -280, 60, 45, RenderPositionUnit.Pixels, null, 0, true,
                        "button_base"),
                new SpriteRenderData("attack", -280, 60, 45, RenderPositionUnit.Pixels, null, 0, true,
                        "button_over"),
                new SpriteRenderData("attack", -280, 60, 45, RenderPositionUnit.Pixels, null, 0, true,
                        "button_down")));
    }
}
