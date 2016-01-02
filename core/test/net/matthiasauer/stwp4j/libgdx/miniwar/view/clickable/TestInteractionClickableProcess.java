package net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;

import net.matthiasauer.stwp4j.Channel;
import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.Scheduler;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventData;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventType;
import net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable.ClickComponentEvent.ClickComponentType;

public class TestInteractionClickableProcess {

    private void expectCorrectEvent(ClickEvent clickEvent, AtomicReference<String> messageToFail,
            AtomicBoolean receivedCorrectEvent, String entityID) {
        if (clickEvent == null) {
            messageToFail.set("no event generated !");
        } else {
            if (clickEvent.getId().equals(entityID)) {
                receivedCorrectEvent.set(true);
            }
        }
    }

    private void expectCorrectClickComponentEvent(ClickComponentEvent clickEvent, String entityID,
            ClickComponentType expectedType) {
        if (clickEvent == null) {
            fail("no event generated !");
        } else {
            assertEquals("incorrect id", entityID, clickEvent.getId());
            assertEquals("incorrect event type", expectedType, clickEvent.getClickComponentType());
        }
    }

    private void sendEvent(ChannelOutPort<InputTouchEventData> inputTouchEventDataChannel, String entityId,
            InputTouchEventType type) {
        Vector2 v = new Vector2();

        InputTouchEventData event = Pools.get(InputTouchEventData.class).obtain().set(type, 0, v, v);

        event.setTouchedRenderDataId(entityId);

        inputTouchEventDataChannel.offer(event);
    }

    @Test
    public void testClickIsGeneratedFirstDownThenUp() {
        final AtomicReference<String> messageToFail = new AtomicReference<String>(null);
        final AtomicBoolean receivedCorrectEvent = new AtomicBoolean(false);

        Scheduler testScheduler = new Scheduler();
        Channel<InputTouchEventData> inputTouchEventDataChan = testScheduler
                .createMultiplexChannel("input touch test chan", InputTouchEventData.class, true, false);
        Channel<ClickEvent> clickEventChan = testScheduler.createMultiplexChannel("click event chan", ClickEvent.class,
                true, false);
        Channel<ClickComponentEvent> clickComponentEventChan = testScheduler
                .createMultiplexChannel("click component event chan", ClickComponentEvent.class, true, true);

        // the process we want to test
        testScheduler.addProcess(new InteractionClickableProcess(inputTouchEventDataChan.createInPort(),
                clickEventChan.createOutPort(), clickComponentEventChan.createOutPort()));

        // this implements the test logic !
        testScheduler
                .addProcess(new TestProcess(clickEventChan.createInPort(), inputTouchEventDataChan.createOutPort()) {
                    int iteration = 0;

                    @Override
                    protected void execute() {
                        if (iteration == 0) {
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchDown);
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchUp);
                        }

                        if (iteration == 2) {
                            // expected the clicked event !
                            expectCorrectEvent(this.clickEventChannel.poll(), messageToFail, receivedCorrectEvent,
                                    ENTITY_ID);
                        }

                        this.iteration += 1;
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

    @Test
    public void testClickIsNotGeneratedFirstDownThenDown() {
        final AtomicReference<String> messageToFail = new AtomicReference<String>(null);

        Scheduler testScheduler = new Scheduler();
        Channel<InputTouchEventData> inputTouchEventDataChan = testScheduler
                .createMultiplexChannel("input touch test chan", InputTouchEventData.class, true, false);
        Channel<ClickEvent> clickEventChan = testScheduler.createMultiplexChannel("click event chan", ClickEvent.class,
                true, false);
        Channel<ClickComponentEvent> clickComponentEventChan = testScheduler
                .createMultiplexChannel("click component event chan", ClickComponentEvent.class, true, true);

        // the process we want to test
        testScheduler.addProcess(new InteractionClickableProcess(inputTouchEventDataChan.createInPort(),
                clickEventChan.createOutPort(), clickComponentEventChan.createOutPort()));

        // this implements the test logic !
        testScheduler
                .addProcess(new TestProcess(clickEventChan.createInPort(), inputTouchEventDataChan.createOutPort()) {
                    int iteration = 0;

                    @Override
                    protected void execute() {
                        if (iteration == 0) {
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchDown);
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchDown);
                        }

                        if (iteration == 2) {
                            ClickEvent clickEvent = this.clickEventChannel.poll();

                            // DON'T expect the clicked event !
                            if (clickEvent != null) {
                                messageToFail.set("event generated !");
                            }
                        }

                        this.iteration += 1;
                    }
                });

        testScheduler.performIteration();

        if (messageToFail.get() != null) {
            fail(messageToFail.get());
        }
    }

    @Test
    public void testClickIsNotGeneratedFirstUpThenUp() {
        final AtomicReference<String> messageToFail = new AtomicReference<String>(null);

        Scheduler testScheduler = new Scheduler();
        Channel<InputTouchEventData> inputTouchEventDataChan = testScheduler
                .createMultiplexChannel("input touch test chan", InputTouchEventData.class, true, false);
        Channel<ClickEvent> clickEventChan = testScheduler.createMultiplexChannel("click event chan", ClickEvent.class,
                true, false);
        Channel<ClickComponentEvent> clickComponentEventChan = testScheduler
                .createMultiplexChannel("click component event chan", ClickComponentEvent.class, true, true);

        // the process we want to test
        testScheduler.addProcess(new InteractionClickableProcess(inputTouchEventDataChan.createInPort(),
                clickEventChan.createOutPort(), clickComponentEventChan.createOutPort()));

        // this implements the test logic !
        testScheduler
                .addProcess(new TestProcess(clickEventChan.createInPort(), inputTouchEventDataChan.createOutPort()) {
                    int iteration = 0;

                    @Override
                    protected void execute() {
                        if (iteration == 0) {
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchUp);
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchUp);
                        }

                        if (iteration == 2) {
                            ClickEvent clickEvent = this.clickEventChannel.poll();

                            // DON'T expect the clicked event !
                            if (clickEvent != null) {
                                messageToFail.set("event generated !");
                            }
                        }

                        this.iteration += 1;
                    }
                });

        testScheduler.performIteration();

        if (messageToFail.get() != null) {
            fail(messageToFail.get());
        }
    }

    @Test
    public void testClickIsNotGeneratedTwiceFirstDownThenUpThenUp() {
        final AtomicReference<String> messageToFail = new AtomicReference<String>(null);
        final AtomicBoolean receivedCorrectEvent = new AtomicBoolean(false);

        Scheduler testScheduler = new Scheduler();
        Channel<InputTouchEventData> inputTouchEventDataChan = testScheduler
                .createMultiplexChannel("input touch test chan", InputTouchEventData.class, true, false);
        Channel<ClickEvent> clickEventChan = testScheduler.createMultiplexChannel("click event chan", ClickEvent.class,
                true, false);
        Channel<ClickComponentEvent> clickComponentEventChan = testScheduler
                .createMultiplexChannel("click component event chan", ClickComponentEvent.class, true, true);

        // the process we want to test
        testScheduler.addProcess(new InteractionClickableProcess(inputTouchEventDataChan.createInPort(),
                clickEventChan.createOutPort(), clickComponentEventChan.createOutPort()));

        // this implements the test logic !
        testScheduler
                .addProcess(new TestProcess(clickEventChan.createInPort(), inputTouchEventDataChan.createOutPort()) {
                    int iteration = 0;

                    @Override
                    protected void execute() {
                        if (iteration == 0) {
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchDown);
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchUp);
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchUp);
                        }

                        if (iteration == 2) {
                            // first expect an event
                            expectCorrectEvent(this.clickEventChannel.poll(), messageToFail, receivedCorrectEvent,
                                    ENTITY_ID);

                            // then expect that there is no event
                            if (this.clickEventChannel.poll() != null) {
                                messageToFail.set("event generated !");
                            }
                        }

                        this.iteration += 1;
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

    @Test
    public void testClickIsNotGeneratedDifferentIds() {
        final AtomicReference<String> messageToFail = new AtomicReference<String>(null);

        Scheduler testScheduler = new Scheduler();
        Channel<InputTouchEventData> inputTouchEventDataChan = testScheduler
                .createMultiplexChannel("input touch test chan", InputTouchEventData.class, true, false);
        Channel<ClickEvent> clickEventChan = testScheduler.createMultiplexChannel("click event chan", ClickEvent.class,
                true, false);
        Channel<ClickComponentEvent> clickComponentEventChan = testScheduler
                .createMultiplexChannel("click component event chan", ClickComponentEvent.class, true, true);

        // the process we want to test
        testScheduler.addProcess(new InteractionClickableProcess(inputTouchEventDataChan.createInPort(),
                clickEventChan.createOutPort(), clickComponentEventChan.createOutPort()));

        // this implements the test logic !
        testScheduler
                .addProcess(new TestProcess(clickEventChan.createInPort(), inputTouchEventDataChan.createOutPort()) {
                    int iteration = 0;

                    @Override
                    protected void execute() {
                        if (iteration == 0) {
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchDown);
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID + "#2", InputTouchEventType.TouchUp);
                        }

                        if (iteration == 2) {
                            ClickEvent clickEvent = this.clickEventChannel.poll();

                            // DON'T expect the clicked event !
                            if (clickEvent != null) {
                                messageToFail.set("event generated !");
                            }
                        }

                        this.iteration += 1;
                    }
                });

        testScheduler.performIteration();

        if (messageToFail.get() != null) {
            fail(messageToFail.get());
        }
    }

    @Test
    public void testClickIsNotGeneratedIfFirstDifferentIdsButThenSame() {
        final AtomicReference<String> messageToFail = new AtomicReference<String>(null);

        Scheduler testScheduler = new Scheduler();
        Channel<InputTouchEventData> inputTouchEventDataChan = testScheduler
                .createMultiplexChannel("input touch test chan", InputTouchEventData.class, true, false);
        Channel<ClickEvent> clickEventChan = testScheduler.createMultiplexChannel("click event chan", ClickEvent.class,
                true, false);
        Channel<ClickComponentEvent> clickComponentEventChan = testScheduler
                .createMultiplexChannel("click component event chan", ClickComponentEvent.class, true, true);

        // the process we want to test
        testScheduler.addProcess(new InteractionClickableProcess(inputTouchEventDataChan.createInPort(),
                clickEventChan.createOutPort(), clickComponentEventChan.createOutPort()));

        // this implements the test logic !
        testScheduler
                .addProcess(new TestProcess(clickEventChan.createInPort(), inputTouchEventDataChan.createOutPort()) {
                    int iteration = 0;

                    @Override
                    protected void execute() {
                        if (iteration == 0) {
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchDown);
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID + "#2", InputTouchEventType.TouchUp);
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchUp);
                        }

                        if (iteration == 2) {
                            ClickEvent clickEvent = this.clickEventChannel.poll();

                            // DON'T expect the clicked event !
                            if (clickEvent != null) {
                                messageToFail.set("event generated !");
                            }
                        }

                        this.iteration += 1;
                    }
                });

        testScheduler.performIteration();

        if (messageToFail.get() != null) {
            fail(messageToFail.get());
        }
    }

    @Test
    public void testClickIsGeneratedIfFirstDifferentIdsButThenSame() {
        final AtomicReference<String> messageToFail = new AtomicReference<String>(null);
        final AtomicBoolean receivedCorrectEvent = new AtomicBoolean(false);

        Scheduler testScheduler = new Scheduler();
        Channel<InputTouchEventData> inputTouchEventDataChan = testScheduler
                .createMultiplexChannel("input touch test chan", InputTouchEventData.class, true, false);
        Channel<ClickEvent> clickEventChan = testScheduler.createMultiplexChannel("click event chan", ClickEvent.class,
                true, false);
        Channel<ClickComponentEvent> clickComponentEventChan = testScheduler
                .createMultiplexChannel("click component event chan", ClickComponentEvent.class, true, true);

        // the process we want to test
        testScheduler.addProcess(new InteractionClickableProcess(inputTouchEventDataChan.createInPort(),
                clickEventChan.createOutPort(), clickComponentEventChan.createOutPort()));

        // this implements the test logic !
        testScheduler
                .addProcess(new TestProcess(clickEventChan.createInPort(), inputTouchEventDataChan.createOutPort()) {
                    int iteration = 0;

                    @Override
                    protected void execute() {
                        if (iteration == 0) {
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID + "#2", InputTouchEventType.TouchDown);
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchDown);
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchUp);
                        }

                        if (iteration == 2) {
                            // Expect the clicked event !
                            expectCorrectEvent(this.clickEventChannel.poll(), messageToFail, receivedCorrectEvent,
                                    ENTITY_ID);
                        }

                        this.iteration += 1;
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

    @Test
    public void testClickIsNotGeneratedIfOneFirstIdIsNull() {
        final AtomicReference<String> messageToFail = new AtomicReference<String>(null);

        Scheduler testScheduler = new Scheduler();
        Channel<InputTouchEventData> inputTouchEventDataChan = testScheduler
                .createMultiplexChannel("input touch test chan", InputTouchEventData.class, true, false);
        Channel<ClickEvent> clickEventChan = testScheduler.createMultiplexChannel("click event chan", ClickEvent.class,
                true, false);
        Channel<ClickComponentEvent> clickComponentEventChan = testScheduler
                .createMultiplexChannel("click component event chan", ClickComponentEvent.class, true, true);

        // the process we want to test
        testScheduler.addProcess(new InteractionClickableProcess(inputTouchEventDataChan.createInPort(),
                clickEventChan.createOutPort(), clickComponentEventChan.createOutPort()));

        // this implements the test logic !
        testScheduler
                .addProcess(new TestProcess(clickEventChan.createInPort(), inputTouchEventDataChan.createOutPort()) {
                    int iteration = 0;

                    @Override
                    protected void execute() {
                        if (iteration == 0) {
                            sendEvent(inputTouchEventDataChannel, null, InputTouchEventType.TouchDown);
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchUp);
                        }

                        if (iteration == 2) {
                            ClickEvent clickEvent = this.clickEventChannel.poll();

                            // DON'T expect the clicked event !
                            if (clickEvent != null) {
                                messageToFail.set("event generated !");
                            }
                        }

                        this.iteration += 1;
                    }
                });

        testScheduler.performIteration();

        if (messageToFail.get() != null) {
            fail(messageToFail.get());
        }
    }

    @Test
    public void testClickIsNotGeneratedIfValidSequenceInterruptedByANullId() {
        final AtomicReference<String> messageToFail = new AtomicReference<String>(null);

        Scheduler testScheduler = new Scheduler();
        Channel<InputTouchEventData> inputTouchEventDataChan = testScheduler
                .createMultiplexChannel("input touch test chan", InputTouchEventData.class, true, false);
        Channel<ClickEvent> clickEventChan = testScheduler.createMultiplexChannel("click event chan", ClickEvent.class,
                true, false);
        Channel<ClickComponentEvent> clickComponentEventChan = testScheduler
                .createMultiplexChannel("click component event chan", ClickComponentEvent.class, true, true);

        // the process we want to test
        testScheduler.addProcess(new InteractionClickableProcess(inputTouchEventDataChan.createInPort(),
                clickEventChan.createOutPort(), clickComponentEventChan.createOutPort()));

        // this implements the test logic !
        testScheduler
                .addProcess(new TestProcess(clickEventChan.createInPort(), inputTouchEventDataChan.createOutPort()) {
                    int iteration = 0;

                    @Override
                    protected void execute() {
                        if (iteration == 0) {
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchDown);
                            sendEvent(inputTouchEventDataChannel, null, InputTouchEventType.TouchDown);
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchUp);
                        }

                        if (iteration == 2) {
                            ClickEvent clickEvent = this.clickEventChannel.poll();

                            // DON'T expect the clicked event !
                            if (clickEvent != null) {
                                messageToFail.set("event generated !");
                            }
                        }

                        this.iteration += 1;
                    }
                });

        testScheduler.performIteration();

        if (messageToFail.get() != null) {
            fail(messageToFail.get());
        }
    }

    @Test
    public void testClickComponentEventsAreGenerated() {
        final AtomicReference<String> messageToFail = new AtomicReference<String>(null);

        Scheduler testScheduler = new Scheduler();
        Channel<InputTouchEventData> inputTouchEventDataChan = testScheduler
                .createMultiplexChannel("input touch test chan", InputTouchEventData.class, true, false);
        Channel<ClickEvent> clickEventChan = testScheduler.createMultiplexChannel("click event chan", ClickEvent.class,
                true, false);
        final Channel<ClickComponentEvent> clickComponentEventChan = testScheduler
                .createMultiplexChannel("click component event chan", ClickComponentEvent.class, true, false);

        // the process we want to test
        testScheduler.addProcess(new InteractionClickableProcess(inputTouchEventDataChan.createInPort(),
                clickEventChan.createOutPort(), clickComponentEventChan.createOutPort()));

        // this implements the test logic !
        testScheduler
                .addProcess(new TestProcess(clickEventChan.createInPort(), inputTouchEventDataChan.createOutPort()) {
                    ChannelInPort<ClickComponentEvent> clickComponentChannel = clickComponentEventChan.createInPort();
                    int iteration = 0;

                    @Override
                    protected void execute() {
                        if (iteration == 0) {
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.Moved);
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchDown);
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchUp);
                            sendEvent(inputTouchEventDataChannel, null, InputTouchEventType.Moved);
                        }

                        if (iteration == 2) {
                            // expected the clicked event !
                            expectCorrectClickComponentEvent(this.clickComponentChannel.poll(), ENTITY_ID,
                                    ClickComponentType.Over);
                            expectCorrectClickComponentEvent(this.clickComponentChannel.poll(), ENTITY_ID,
                                    ClickComponentType.Down);
                            expectCorrectClickComponentEvent(this.clickComponentChannel.poll(), ENTITY_ID,
                                    ClickComponentType.Up);
                            expectCorrectClickComponentEvent(this.clickComponentChannel.poll(), ENTITY_ID,
                                    ClickComponentType.Left);
                        }

                        // empty the channel
                        while (this.clickEventChannel.poll() != null)
                            ;

                        this.iteration += 1;
                    }
                });

        testScheduler.performIteration();
    }

    @Test
    public void testClickComponentEventsAreGenerated2() {
        final AtomicReference<String> messageToFail = new AtomicReference<String>(null);

        Scheduler testScheduler = new Scheduler();
        Channel<InputTouchEventData> inputTouchEventDataChan = testScheduler
                .createMultiplexChannel("input touch test chan", InputTouchEventData.class, true, false);
        Channel<ClickEvent> clickEventChan = testScheduler.createMultiplexChannel("click event chan", ClickEvent.class,
                true, false);
        final Channel<ClickComponentEvent> clickComponentEventChan = testScheduler
                .createMultiplexChannel("click component event chan", ClickComponentEvent.class, true, false);

        // the process we want to test
        testScheduler.addProcess(new InteractionClickableProcess(inputTouchEventDataChan.createInPort(),
                clickEventChan.createOutPort(), clickComponentEventChan.createOutPort()));

        // this implements the test logic !
        testScheduler
                .addProcess(new TestProcess(clickEventChan.createInPort(), inputTouchEventDataChan.createOutPort()) {
                    ChannelInPort<ClickComponentEvent> clickComponentChannel = clickComponentEventChan.createInPort();
                    int iteration = 0;

                    @Override
                    protected void execute() {
                        if (iteration == 0) {
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.Moved);
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID, InputTouchEventType.TouchDown);
                            sendEvent(inputTouchEventDataChannel, ENTITY_ID + "#2", InputTouchEventType.TouchUp);
                            sendEvent(inputTouchEventDataChannel, null, InputTouchEventType.Moved);
                        }

                        if (iteration == 2) {
                            // expected the clicked event !
                            expectCorrectClickComponentEvent(this.clickComponentChannel.poll(), ENTITY_ID,
                                    ClickComponentType.Over);
                            expectCorrectClickComponentEvent(this.clickComponentChannel.poll(), ENTITY_ID,
                                    ClickComponentType.Down);
                            expectCorrectClickComponentEvent(this.clickComponentChannel.poll(), ENTITY_ID,
                                    ClickComponentType.Left);
                            expectCorrectClickComponentEvent(this.clickComponentChannel.poll(), ENTITY_ID+"#2",
                                    ClickComponentType.Over);
                            expectCorrectClickComponentEvent(this.clickComponentChannel.poll(), ENTITY_ID+"#2",
                                    ClickComponentType.Left);
                        }

                        // empty the channel
                        while (this.clickEventChannel.poll() != null)
                            ;

                        this.iteration += 1;
                    }
                });

        testScheduler.performIteration();
    }
}
