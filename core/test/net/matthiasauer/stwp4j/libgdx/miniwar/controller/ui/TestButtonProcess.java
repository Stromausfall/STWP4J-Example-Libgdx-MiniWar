package net.matthiasauer.stwp4j.libgdx.miniwar.controller.ui;

import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import net.matthiasauer.stwp4j.Channel;
import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.Scheduler;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventData;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderData;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderPositionUnit;
import net.matthiasauer.stwp4j.libgdx.graphic.SpriteRenderData;

public class TestButtonProcess {
    Pool<SpriteRenderData> pool = Pools.get(SpriteRenderData.class);

    private SpriteRenderData createRenderData(String id, String texture) {
        SpriteRenderData data = pool.obtain();

        data.set(id, 0, 0, 0, RenderPositionUnit.Percent, null, 2, true, texture);

        return data;
    }

    @Test
    public void testUntouchedGeneratesBaseState() {
        Scheduler scheduler = new Scheduler();

        final Channel<RenderData> renderChannel = scheduler.createMultiplexChannel("#1", RenderData.class, false, false);
        final Channel<InputTouchEventData> touchEventChannel = scheduler.createMultiplexChannel("#2",
                InputTouchEventData.class, true, false);
        scheduler.addProcess(new ButtonProcess(renderChannel.createOutPort(), touchEventChannel.createInPort(),
                this.createRenderData("1", "tex#1"), this.createRenderData("2", "tex#2"),
                this.createRenderData("3", "tex#3")));
        final ChannelInPort<RenderData> renderInput = renderChannel.createInPort();
        
        scheduler.performIteration();
        
        SpriteRenderData renderData = (SpriteRenderData)renderInput.poll();
        
        assertNotNull("there should be a renderdata !", renderData);
        assertNull("there should be only one renderdata !", renderInput.poll());        
        assertEquals("the renderData was not correct", renderData.getTextureName(), "tex#1");
    }

}
