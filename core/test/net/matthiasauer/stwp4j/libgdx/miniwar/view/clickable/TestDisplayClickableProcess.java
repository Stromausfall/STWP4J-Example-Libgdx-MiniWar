package net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import net.matthiasauer.stwp4j.Channel;
import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.Scheduler;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderData;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderPositionUnit;
import net.matthiasauer.stwp4j.libgdx.graphic.SpriteRenderData;
import net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable.ClickComponentEvent.ClickComponentType;

public class TestDisplayClickableProcess {
    private static final String ENTITY_ID = "id#1";
    private final Pool<SpriteRenderData> spriteRenderDataPool = Pools.get(SpriteRenderData.class);
    private final Pool<ClickComponentEvent> clickComponentEventPool = Pools.get(ClickComponentEvent.class);

    private SpriteRenderData createBaseState() {
        SpriteRenderData baseState = spriteRenderDataPool.obtain();

        baseState.set(ENTITY_ID, 0, 0, 0, RenderPositionUnit.Percent, null, 1, true, "tex#1");

        return baseState;
    }

    private SpriteRenderData createTouchedState() {
        SpriteRenderData touchedState = spriteRenderDataPool.obtain();

        touchedState.set(ENTITY_ID, 0, 0, 0, RenderPositionUnit.Percent, null, 1, true, "tex#2");
System.err.println("check that freed");
        return touchedState;
    }

    private SpriteRenderData createClickState() {
        SpriteRenderData clickState = spriteRenderDataPool.obtain();

        clickState.set(ENTITY_ID, 0, 0, 0, RenderPositionUnit.Percent, null, 1, true, "tex#3");

        return clickState;
    }

    private void sendDefaultRequest(ChannelOutPort<DisplayClickableRequest> outPort) {
        DisplayClickableRequest request = new DisplayClickableRequest();

        request.set(this.createBaseState(), this.createTouchedState(), this.createClickState());
        
        outPort.offer(request);
    }

    private void sendClickComponentEvent(ChannelOutPort<ClickComponentEvent> outPort, String id,
            ClickComponentEvent.ClickComponentType type) {
        ClickComponentEvent clickComponentEvent = clickComponentEventPool.obtain();

        clickComponentEvent.set(id, type);

        outPort.offer(clickComponentEvent);
    }

    private void expectRenderData(ChannelInPort<RenderData> inPort, SpriteRenderData expected, AtomicInteger counter) {
        SpriteRenderData data = (SpriteRenderData) inPort.poll();

        if (data != null) {
            counter.incrementAndGet();
        
            assertNotNull("No RenderData element in the channel !", data);
    
            assertEquals("incorrect id", expected.getId(), data.getId());
            assertEquals("incorrect texture name", expected.getTextureName(), data.getTextureName());
        }
    }

    @Test
    public void testAll() {
        Scheduler testScheduler = new Scheduler();

        final Channel<RenderData> renderDataChannel = testScheduler.createMultiplexChannel("1", RenderData.class, true,
                false);
        final Channel<ClickComponentEvent> clickComponentEventChannel = testScheduler.createMultiplexChannel("2",
                ClickComponentEvent.class, true, false);
        final Channel<DisplayClickableRequest> displayClickableRequestChannel = testScheduler
                .createMultiplexChannel("3", DisplayClickableRequest.class, true, false);
        final ChannelOutPort<DisplayClickableRequest> displayClickableRequestOutPort = displayClickableRequestChannel
                .createOutPort();
        final ChannelOutPort<ClickComponentEvent> clickComponentEventOutPort = clickComponentEventChannel
                .createOutPort();
        final ChannelInPort<RenderData> renderDataInPort = renderDataChannel.createInPort();
        final AtomicInteger counter = new AtomicInteger(0);

        // the process we want to test
        testScheduler.addProcess(new DisplayClickableProcess(renderDataChannel.createOutPort(),
                clickComponentEventChannel.createInPort(), displayClickableRequestChannel.createInPort()));

        // this implements the test logic !
        testScheduler.addProcess(new LightweightProcess() {
            int subIteration = 0;
            int iteration = 0;

            @Override
            protected void preIteration() {
                subIteration = 0;
                iteration++;
            }

            @Override
            protected void execute() {
                if ((subIteration < 6) && (iteration < 6)) {
                    // send the request to display the clickable !
                    sendDefaultRequest(displayClickableRequestOutPort);
                }

                switch (iteration) {
                case 0:
                    expectRenderData(renderDataInPort, createBaseState(), counter);
                    break;
                case 1:
                    expectRenderData(renderDataInPort, createTouchedState(), counter);
                    break;
                case 2:
                    expectRenderData(renderDataInPort, createClickState(), counter);
                    break;
                case 3:
                    expectRenderData(renderDataInPort, createBaseState(), counter);
                    break;
                case 4:
                    expectRenderData(renderDataInPort, createBaseState(), counter);
                    break;
                }

                this.subIteration += 1;
            }
        });

        // send the request & perform iteration
        this.sendDefaultRequest(displayClickableRequestOutPort);
        testScheduler.performIteration();

        this.sendDefaultRequest(displayClickableRequestOutPort);
        this.sendClickComponentEvent(clickComponentEventOutPort, ENTITY_ID, ClickComponentType.Over);
        testScheduler.performIteration();

        this.sendDefaultRequest(displayClickableRequestOutPort);
        this.sendClickComponentEvent(clickComponentEventOutPort, ENTITY_ID, ClickComponentType.Down);
        testScheduler.performIteration();

        this.sendDefaultRequest(displayClickableRequestOutPort);
        this.sendClickComponentEvent(clickComponentEventOutPort, ENTITY_ID, ClickComponentType.Up);
        testScheduler.performIteration();

        this.sendDefaultRequest(displayClickableRequestOutPort);
        this.sendClickComponentEvent(clickComponentEventOutPort, ENTITY_ID, ClickComponentType.Left);
        testScheduler.performIteration();
        
        assertEquals("not enough checks were made", 5, counter.get());
    }
}