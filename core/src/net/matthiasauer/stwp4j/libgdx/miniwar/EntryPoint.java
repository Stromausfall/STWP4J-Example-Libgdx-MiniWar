package net.matthiasauer.stwp4j.libgdx.miniwar;

import java.util.Arrays;

import net.matthiasauer.stwp4j.Channel;
import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.LightweightProcess;
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
import net.matthiasauer.stwp4j.libgdx.miniwar.model.test.WorldInteraction;
import net.matthiasauer.stwp4j.libgdx.miniwar.model.test.WorldProcess;
import net.matthiasauer.stwp4j.libgdx.miniwar.model.test.WorldSnapShot;
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
        Channel<RenderData> renderDataChannel = this.scheduler.createMultiplexChannel("render data-channel",
                RenderData.class, true, false);
        Channel<InputTouchEvent> inputTouchEventDataChannel = this.scheduler
                .createMultiplexChannel("input touch event-channel", InputTouchEvent.class, true, false);
        final Channel<ButtonClickEvent> buttonClickEventChannel = this.scheduler
                .createMultiplexChannel("button click event data-channel", ButtonClickEvent.class, true, false);

        Channel<WorldInteraction> worldInteractionChannel = this.scheduler
                .createMultiplexChannel("world-interaction channel", WorldInteraction.class, true, false);
        Channel<WorldSnapShot> worldSnapShotChannel = this.scheduler.createMultiplexChannel("world-snapshot channel",
                WorldSnapShot.class, true, false);

        Channel<CameraStatusEvent> cameraStatusEventChannel = this.scheduler
                .createMultiplexChannel("camera status event channel", CameraStatusEvent.class, false, true);
        Channel<CameraChangeEvent> cameraChangeEventChannel = this.scheduler
                .createMultiplexChannel("camera status change channel", CameraChangeEvent.class, false, true);

        // Plumbing - processes
        scheduler.addProcess(new RenderProcess(Arrays.asList("data1.atlas"), true, 640, 480,
                ResizeBehavior.KeepResolutionKeepAspect, renderDataChannel.createInPort(),
                applicationEventChannel.createInPort(), inputTouchEventDataChannel.createOutPort(),
                cameraChangeEventChannel.createInPort(), cameraStatusEventChannel.createOutPort()));
        scheduler.addProcess(
                new WorldProcess(worldInteractionChannel.createInPort(), worldSnapShotChannel.createOutPort()));

        scheduler.addProcess(
                new ButtonProcess(renderDataChannel.createOutPort(), inputTouchEventDataChannel.createInPort(),
                        buttonClickEventChannel.createOutPort(), new SpriteRenderData("2", 100, 0, 0,
                                RenderPositionUnit.Percent, null, 0, false, "tile_grass"),
                new SpriteRenderData("2", 100, 0, 0, RenderPositionUnit.Percent, null, 0, false, "tile_dirt"),
                new SpriteRenderData("2", 100, 0, 0, RenderPositionUnit.Percent, null, 0, false, "tile_mountain")));

        scheduler.addProcess(new ButtonProcess(renderDataChannel.createOutPort(),
                inputTouchEventDataChannel.createInPort(), buttonClickEventChannel.createOutPort(),
                new SpriteRenderData("1", 0, 0, 0, RenderPositionUnit.Pixels, null, 0, true, "tile_dirt"),
                new SpriteRenderData("1", 0, 0, 0, RenderPositionUnit.Pixels, null, 0, true, "tile_grass"),
                new SpriteRenderData("1", 0, 0, 0, RenderPositionUnit.Pixels, null, 0, true, "tile_mountain")));
        scheduler.addProcess(
                new ButtonProcess(renderDataChannel.createOutPort(), inputTouchEventDataChannel.createInPort(),
                        buttonClickEventChannel.createOutPort(), new SpriteRenderData("3", 200, 0, 0,
                                RenderPositionUnit.Pixels, null, 0, true, "tile_dirt"),
                new SpriteRenderData("3", 200, 0, 0, RenderPositionUnit.Pixels, null, 0, true, "tile_grass"),
                new SpriteRenderData("3", 200, 0, 0, RenderPositionUnit.Pixels, null, 0, true, "tile_mountain")));
        scheduler.addProcess(
                new ButtonProcess(renderDataChannel.createOutPort(), inputTouchEventDataChannel.createInPort(),
                        buttonClickEventChannel.createOutPort(), new SpriteRenderData("4", 0, 200, 0,
                                RenderPositionUnit.Pixels, null, 0, true, "tile_dirt"),
                new SpriteRenderData("4", 0, 200, 0, RenderPositionUnit.Pixels, null, 0, true, "tile_grass"),
                new SpriteRenderData("4", 0, 200, 0, RenderPositionUnit.Pixels, null, 0, true, "tile_mountain")));
        scheduler.addProcess(
                new ButtonProcess(renderDataChannel.createOutPort(), inputTouchEventDataChannel.createInPort(),
                        buttonClickEventChannel.createOutPort(), new SpriteRenderData("5", 200, 200, 45,
                                RenderPositionUnit.Pixels, null, 0, true, "button_base"),
                new SpriteRenderData("5", 200, 200, 45, RenderPositionUnit.Pixels, null, 0, true, "button_over"),
                new SpriteRenderData("5", 200, 200, 45, RenderPositionUnit.Pixels, null, 0, true, "button_down")));
        // THE BUTTON SHOULD NOW BE PROJECTED - this doesn't work !1
        scheduler.addProcess(new LightweightProcess() {
            final ChannelInPort<ButtonClickEvent> clickEventInPort = buttonClickEventChannel.createInPort();

            @Override
            protected void execute() {
                ButtonClickEvent event = null;

                while ((event = clickEventInPort.poll()) != null) {
                    System.err.println("--> " + event.getId() + " clicked !");
                }
            }
        });
    }
}
