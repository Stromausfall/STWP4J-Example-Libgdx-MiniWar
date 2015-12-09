package net.matthiasauer.stwp4j.example.libgdx.miniwar.gui.interaction;

import static org.junit.Assert.fail;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Pools;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.ChannelPortsCreated;
import net.matthiasauer.stwp4j.ChannelPortsRequest;
import net.matthiasauer.stwp4j.ExecutionState;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.PortType;
import net.matthiasauer.stwp4j.Scheduler;
import net.matthiasauer.stwp4j.example.libgdx.miniwar.test.Utils;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventData;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventType;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderProcess;

public class TestGuiInteractionProcess {
    private static final String ENTITY_ID = "entity#1";

    @Test
    public void test() {
        final AtomicReference<String> messageToFail = new AtomicReference<String>(null) ;
        final AtomicBoolean receivedCorrectEvent = new AtomicBoolean(false);
        final Camera camera = Utils.createDummyCamera();
        
        Scheduler testScheduler = new Scheduler();
        
        // the process we want to test
        testScheduler.addProcess(new GuiInteractionProcess());
        
        // initialize the graphics
        Utils.initializeDummyGraphics();
        
        // this implements the test logic !
        testScheduler.addProcess(
                new LightweightProcess(
                        new ChannelPortsRequest<InputTouchEventData>(RenderProcess.INPUTTOUCHEVENTDATA_CHANNEL, PortType.OutputExclusive, InputTouchEventData.class),
                        new ChannelPortsRequest<ClickEvent>(GuiInteractionProcess.CLICKEVENT_CHANNEL, PortType.InputExclusive, ClickEvent.class)
                ) {
                    
            // create du
            
            ChannelInPort<ClickEvent> clickEventChannel;
            ChannelOutPort<InputTouchEventData> inputTouchEventDataChannel;
            int iteration = 0;
            
            @Override
            protected void initialize(ChannelPortsCreated createdChannelPorts) {
                this.clickEventChannel = createdChannelPorts.getChannelInPort(GuiInteractionProcess.CLICKEVENT_CHANNEL, ClickEvent.class);
                this.inputTouchEventDataChannel = createdChannelPorts.getChannelOutPort(RenderProcess.INPUTTOUCHEVENTDATA_CHANNEL, InputTouchEventData.class);
            }
            
            @Override
            protected ExecutionState execute() {
                if (iteration == 0) {
                    // first send the events that should trigger a ClickEvent !
                    InputTouchEventData event1 =
                            Pools.get(InputTouchEventData.class).obtain()
                                    .set(0, 0, InputTouchEventType.TouchDown, 0, camera);
                    InputTouchEventData event2 =
                            Pools.get(InputTouchEventData.class).obtain()
                                    .set(0, 0, InputTouchEventType.TouchUp, 0, camera);
                    
                    event1.setTouchedRenderDataId(ENTITY_ID);
                    event2.setTouchedRenderDataId(ENTITY_ID);
                    
                    this.inputTouchEventDataChannel.offer(event1);
                    this.inputTouchEventDataChannel.offer(event2);
                }
                
                if (iteration == 1) {
                    // expected the clicked event !
                    ClickEvent clickEvent = this.clickEventChannel.poll();
                    
                    if (clickEvent == null) {
                        messageToFail.set("no event generated !");
                    } else {
                        if (clickEvent.getId().equals(ENTITY_ID)) {
                            receivedCorrectEvent.set(true);
                        }
                    }
                }
                
                this.iteration += 1;
            
                return ExecutionState.Finished;
            }
        });
        
        testScheduler.performIteration();
        testScheduler.performIteration();
        
        if (messageToFail.get() != null) {
            fail(messageToFail.get());
        }
    }

}
