package net.matthiasauer.stwp4j.libgdx.miniwar;

import java.util.Arrays;

import net.matthiasauer.stwp4j.Channel;
import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.application.ApplicationEntryPointProcess;
import net.matthiasauer.stwp4j.libgdx.application.ApplicationEvent;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEvent;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderData;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderPositionUnit;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderProcess;
import net.matthiasauer.stwp4j.libgdx.graphic.SpriteRenderData;
import net.matthiasauer.stwp4j.libgdx.miniwar.controller.ui.ButtonClickEvent;
import net.matthiasauer.stwp4j.libgdx.miniwar.controller.ui.ButtonProcess;
import net.matthiasauer.stwp4j.libgdx.miniwar.model.test.WorldInteraction;
import net.matthiasauer.stwp4j.libgdx.miniwar.model.test.WorldProcess;
import net.matthiasauer.stwp4j.libgdx.miniwar.model.test.WorldSnapShot;

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

        // Plumbing - processes
        scheduler.addProcess(new RenderProcess(Arrays.asList("data1.atlas"), true, renderDataChannel.createInPort(),
                applicationEventChannel.createInPort(), inputTouchEventDataChannel.createOutPort()));
        scheduler.addProcess(
                new WorldProcess(worldInteractionChannel.createInPort(), worldSnapShotChannel.createOutPort()));

        scheduler.addProcess(new ButtonProcess(renderDataChannel.createOutPort(),
                inputTouchEventDataChannel.createInPort(), buttonClickEventChannel.createOutPort(),
                new SpriteRenderData().set("1", 0, 0, 0, RenderPositionUnit.Percent, null, 0, false, "tile_dirt"),
                new SpriteRenderData().set("1", 0, 0, 0, RenderPositionUnit.Percent, null, 0, false, "tile_grass"),
                new SpriteRenderData().set("1", 0, 0, 0, RenderPositionUnit.Percent, null, 0, false, "tile_mountain")));

        scheduler.addProcess(new ButtonProcess(renderDataChannel.createOutPort(),
                inputTouchEventDataChannel.createInPort(), buttonClickEventChannel.createOutPort(),
                new SpriteRenderData().set("2", 20, 0, 0, RenderPositionUnit.Percent, null, 0, false, "tile_dirt"),
                new SpriteRenderData().set("2", 20, 0, 0, RenderPositionUnit.Percent, null, 0, false, "tile_grass"),
                new SpriteRenderData().set("2", 20, 0, 0, RenderPositionUnit.Percent, null, 0, false, "tile_mountain")));
        
        scheduler.addProcess(new LightweightProcess() {
            final ChannelInPort<ButtonClickEvent> clickEventInPort =
                    buttonClickEventChannel.createInPort();
            
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
