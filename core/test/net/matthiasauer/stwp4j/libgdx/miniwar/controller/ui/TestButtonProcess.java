package net.matthiasauer.stwp4j.libgdx.miniwar.controller.ui;

import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import net.matthiasauer.stwp4j.Channel;
import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.Scheduler;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventData;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventType;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderData;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderPositionUnit;
import net.matthiasauer.stwp4j.libgdx.graphic.SpriteRenderData;

public class TestButtonProcess {
    Pool<SpriteRenderData> pool = Pools.get(SpriteRenderData.class);
    Pool<InputTouchEventData> touchEventPool = Pools.get(InputTouchEventData.class);

    private SpriteRenderData createRenderData(String id, String texture) {
        SpriteRenderData data = pool.obtain();

        data.set(id, 0, 0, 0, RenderPositionUnit.Percent, null, 2, true, texture);

        return data;
    }

    @Test
    public void testUntouchedGeneratesBaseState() {
        Scheduler scheduler = new Scheduler();

        final Channel<RenderData> renderChannel = scheduler.createMultiplexChannel("#1", RenderData.class, false,
                false);
        final Channel<InputTouchEventData> touchEventChannel = scheduler.createMultiplexChannel("#2",
                InputTouchEventData.class, true, false);
        scheduler.addProcess(new ButtonProcess(renderChannel.createOutPort(), touchEventChannel.createInPort(),
                this.createRenderData("1", "tex#1"), this.createRenderData("1", "tex#2"),
                this.createRenderData("1", "tex#3")));
        final ChannelInPort<RenderData> renderInput = renderChannel.createInPort();

        scheduler.performIteration();
        scheduler.performIteration();

        SpriteRenderData renderData = (SpriteRenderData) renderInput.poll();

        assertNotNull("there should be a renderdata !", renderData);
        assertNull("there should be only one renderdata !", renderInput.poll());
        assertEquals("the renderData was not correct", renderData.getTextureName(), "tex#1");
    }

    @Test
    public void testRenderStatesMustBeTheSameEntity() {
        Scheduler scheduler = new Scheduler();

        final Channel<RenderData> renderChannel = scheduler.createMultiplexChannel("#1", RenderData.class, false,
                false);
        final Channel<InputTouchEventData> touchEventChannel = scheduler.createMultiplexChannel("#2",
                InputTouchEventData.class, true, false);

        try {
            scheduler.addProcess(new ButtonProcess(renderChannel.createOutPort(), touchEventChannel.createInPort(),
                    this.createRenderData("1", "tex#1"), this.createRenderData("2", "tex#2"),
                    this.createRenderData("3", "tex#3")));
        } catch (IllegalArgumentException e) {
            assertEquals("incorrect name", "all RenderData must have the same ID !", e.getMessage());
            return;
        } catch (Exception e) {
            fail("incorrect exception thrown : " + e);
        }

        fail("no exception thrown !");
    }

    @Test
    public void testMoveOverGeneratesOverStateContinouusly() {
        Scheduler scheduler = new Scheduler();

        final Channel<RenderData> renderChannel = scheduler.createMultiplexChannel("#1", RenderData.class, false,
                false);
        final Channel<InputTouchEventData> touchEventChannel = scheduler.createMultiplexChannel("#2",
                InputTouchEventData.class, false, false);
        scheduler.addProcess(new ButtonProcess(renderChannel.createOutPort(), touchEventChannel.createInPort(),
                this.createRenderData("1", "tex#1"), this.createRenderData("1", "tex#touched"),
                this.createRenderData("1", "tex#3")));
        final ChannelInPort<RenderData> renderInput = renderChannel.createInPort();
        final ChannelOutPort<InputTouchEventData> touchOutput = touchEventChannel.createOutPort();

        final InputTouchEventData event = touchEventPool.obtain();
        event.set(InputTouchEventType.Moved, 0, new Vector2(), new Vector2());
        event.setTouchedRenderDataId("1");

        touchOutput.offer(event);
        scheduler.performIteration();

        for (int i = 0; i < 10; i++) {
            scheduler.performIteration();
            SpriteRenderData renderData = (SpriteRenderData) renderInput.poll();
            assertEquals("the renderData was correct", renderData.getTextureName(), "tex#touched");
        }

        assertNull("there should be no more renderdata !", renderInput.poll());
    }

    @Test
    public void testMoveOverGeneratesOverStateContinouuslyButNotIfOtherTarget() {
        Scheduler scheduler = new Scheduler();

        final Channel<RenderData> renderChannel = scheduler.createMultiplexChannel("#1", RenderData.class, false,
                false);
        final Channel<InputTouchEventData> touchEventChannel = scheduler.createMultiplexChannel("#2",
                InputTouchEventData.class, false, false);
        scheduler.addProcess(new ButtonProcess(renderChannel.createOutPort(), touchEventChannel.createInPort(),
                this.createRenderData("1", "tex#1"), this.createRenderData("1", "tex#touched"),
                this.createRenderData("1", "tex#3")));
        final ChannelInPort<RenderData> renderInput = renderChannel.createInPort();
        final ChannelOutPort<InputTouchEventData> touchOutput = touchEventChannel.createOutPort();

        final InputTouchEventData event = touchEventPool.obtain();
        event.set(InputTouchEventType.Moved, 0, new Vector2(), new Vector2());
        event.setTouchedRenderDataId("12");

        touchOutput.offer(event);
        scheduler.performIteration();

        for (int i = 0; i < 10; i++) {
            scheduler.performIteration();
            SpriteRenderData renderData = (SpriteRenderData) renderInput.poll();
            assertEquals("the renderData was correct", renderData.getTextureName(), "tex#1");
        }

        assertNull("there should be no more renderdata !", renderInput.poll());
    }

    @Test
    public void testMoveAndDownGeneratesFirstOverThenDownContinouusly() {
        Scheduler scheduler = new Scheduler();

        final Channel<RenderData> renderChannel = scheduler.createMultiplexChannel("#1", RenderData.class, false,
                false);
        final Channel<InputTouchEventData> touchEventChannel = scheduler.createMultiplexChannel("#2",
                InputTouchEventData.class, false, false);
        scheduler.addProcess(new ButtonProcess(renderChannel.createOutPort(), touchEventChannel.createInPort(),
                this.createRenderData("1", "tex#base"), this.createRenderData("1", "tex#over"),
                this.createRenderData("1", "tex#down")));
        final ChannelInPort<RenderData> renderInput = renderChannel.createInPort();
        final ChannelOutPort<InputTouchEventData> touchOutput = touchEventChannel.createOutPort();

        final InputTouchEventData event = touchEventPool.obtain();
        event.set(InputTouchEventType.Moved, 0, new Vector2(), new Vector2());
        event.setTouchedRenderDataId("1");
        touchOutput.offer(event);
        scheduler.performIteration();

        for (int i = 0; i < 5; i++) {
            scheduler.performIteration();
            SpriteRenderData renderData = (SpriteRenderData) renderInput.poll();
            assertEquals("the renderData was correct", "tex#over", renderData.getTextureName());
        }

        final InputTouchEventData event2 = touchEventPool.obtain();
        event2.set(InputTouchEventType.TouchDown, 0, new Vector2(), new Vector2());
        event2.setTouchedRenderDataId("1");
        touchOutput.offer(event2);
        scheduler.performIteration();
        
        // empty the renderInput
        while (renderInput.poll() != null) {
        }

        for (int i = 0; i < 5; i++) {
            scheduler.performIteration();
            SpriteRenderData renderData = (SpriteRenderData) renderInput.poll();
            assertEquals("the renderData was correct", "tex#down", renderData.getTextureName());
        }

        assertNull("there should be no more renderdata !", renderInput.poll());
    }

}
