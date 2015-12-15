package net.matthiasauer.stwp4j.libgdx.miniwar.gui.interaction;

import static org.junit.Assert.fail;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;

import net.matthiasauer.stwp4j.ExecutionState;
import net.matthiasauer.stwp4j.Scheduler;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventData;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventType;

public class TestGuiInteractionProcess {

    @Test
    public void testClickIsGenerated() {
        final AtomicReference<String> messageToFail = new AtomicReference<String>(null);
        final AtomicBoolean receivedCorrectEvent = new AtomicBoolean(false);

        Scheduler testScheduler = new Scheduler();

        // the process we want to test
        testScheduler.addProcess(new GuiInteractionProcess());

        // this implements the test logic !
        testScheduler.addProcess(new TestProcess() {
            int iteration = 0;
            
            @Override
            protected ExecutionState execute() {
                if (iteration == 0) {
                    Vector2 v = new Vector2();

                    // first send the events that should trigger a ClickEvent !
                    InputTouchEventData event1 = Pools.get(InputTouchEventData.class).obtain()
                            .set(InputTouchEventType.TouchDown, 0, v, v);
                    InputTouchEventData event2 = Pools.get(InputTouchEventData.class).obtain()
                            .set(InputTouchEventType.TouchUp, 0, v, v);

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

                return ExecutionState.Waiting;
            }
        });

        testScheduler.performIteration();

        if (messageToFail.get() != null) {
            fail(messageToFail.get());
        }

        if (receivedCorrectEvent.get() == false) {
            fail("did'nt receive correct event !");
        }
    }
}
