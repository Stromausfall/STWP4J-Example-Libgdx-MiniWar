package net.matthiasauer.stwp4j.libgdx.miniwar;

import java.util.Arrays;

import com.badlogic.gdx.graphics.Color;

import net.matthiasauer.stwp4j.Channel;
import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
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
import net.matthiasauer.stwp4j.libgdx.graphic.TextRenderData;
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
                new ButtonProcess(
                        renderDataChannel.createOutPort(),
                        inputTouchEventDataChannel.createInPort(),
                        buttonClickEventChannel.createOutPort(),
                        new SpriteRenderData("increase_industry", -280, 200, 45, RenderPositionUnit.Pixels, null, 0, true, "button_base"),
                        new SpriteRenderData("increase_industry", -280, 200, 45, RenderPositionUnit.Pixels, null, 0, true, "button_over"),
                        new SpriteRenderData("increase_industry", -280, 200, 45, RenderPositionUnit.Pixels, null, 0, true, "button_down")));
        scheduler.addProcess(
                new ButtonProcess(
                        renderDataChannel.createOutPort(),
                        inputTouchEventDataChannel.createInPort(),
                        buttonClickEventChannel.createOutPort(),
                        new SpriteRenderData("increase_army", -280, 100, 45, RenderPositionUnit.Pixels, null, 0, true, "button_base"),
                        new SpriteRenderData("increase_army", -280, 100, 45, RenderPositionUnit.Pixels, null, 0, true, "button_over"),
                        new SpriteRenderData("increase_army", -280, 100, 45, RenderPositionUnit.Pixels, null, 0, true, "button_down")));
        
        final String fontName = "arial#24";
        final TextRenderData textRenderData =
                new TextRenderData("", -240, 210, 0, RenderPositionUnit.Pixels, Color.BLACK, 1, true, "increase industry", fontName);
        final TextRenderData textRenderData2 =
                new TextRenderData("", -240, 110, 0, RenderPositionUnit.Pixels, Color.BLACK, 1, true, "increase army", fontName);
        
        final ChannelOutPort<RenderData> renderDataOutput =
                renderDataChannel.createOutPort();
        
        scheduler.addProcess(new LightweightProcess() {
            final ChannelInPort<ButtonClickEvent> clickEventInPort = buttonClickEventChannel.createInPort();

            @Override
            protected void preIteration() {                
                renderDataOutput.offer(textRenderData);   
                renderDataOutput.offer(textRenderData2);
            }
            
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
