package net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable;

import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.gdx.utils.Pools;

import net.matthiasauer.stwp4j.libgdx.graphic.RenderData;
import net.matthiasauer.stwp4j.libgdx.graphic.SpriteRenderData;
import net.matthiasauer.stwp4j.libgdx.graphic.TextRenderData;

public class TestDisplayClickableRequest {

    @Test
    public void testSetAndResetMethod() {
        DisplayClickableRequest request = new DisplayClickableRequest();
        
        assertNull("getBaseState didn't return null", request.getBaseState());
        assertNull("getTouchedState didn't return null", request.getTouchedState());
        assertNull("getClickState didn't return null", request.getClickState());
        
        RenderData renderData1 = new SpriteRenderData();
        RenderData renderData2 = new SpriteRenderData();
        RenderData renderData3 = new SpriteRenderData(); 
                
        request.set(renderData1, renderData2, renderData3);
        
        assertEquals("baseState not correct", renderData1, request.getBaseState());
        assertEquals("touchedState not correct", renderData2, request.getTouchedState());
        assertEquals("clickState not correct", renderData3, request.getClickState());
        
        request.reset();
        
        assertNull("getBaseState didn't return null", request.getBaseState());
        assertNull("getTouchedState didn't return null", request.getTouchedState());
        assertNull("getClickState didn't return null", request.getClickState());
    }

    @Test
    public void testResetMethodFreesObjects() {
        DisplayClickableRequest request = new DisplayClickableRequest();

        SpriteRenderData renderData1 = Pools.get(SpriteRenderData.class).obtain();
        SpriteRenderData renderData2 = Pools.get(SpriteRenderData.class).obtain();
        TextRenderData renderData3 = Pools.get(TextRenderData.class).obtain();
        renderData1.set("renderData1", 0, 0, 0, null, null, 0, false, null);
        renderData2.set("renderData2", 0, 0, 0, null, null, 0, false, null);
        renderData3.set("renderData3", 0, 0, 0, null, null, 0, false, null, null);
                
        request.set(renderData1, renderData2, renderData3);
        request.reset();
        
        assertNull("renderData1 not freed", renderData1.getId());
        assertNull("renderData2 not freed", renderData2.getId());
        assertNull("renderData3 not freed", renderData3.getId());
    }
}
