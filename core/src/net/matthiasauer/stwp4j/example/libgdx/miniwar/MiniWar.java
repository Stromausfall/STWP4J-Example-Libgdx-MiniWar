package net.matthiasauer.stwp4j.example.libgdx.miniwar;

import java.util.Arrays;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import net.matthiasauer.stwp4j.example.libgdx.miniwar.model.test.TestDataCreatorProcess;
import net.matthiasauer.stwp4j.libgdx.application.ApplicationEntryPointProcess;
import net.matthiasauer.stwp4j.libgdx.graphic.InteractionProcess;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderProcess;

public class MiniWar extends ApplicationEntryPointProcess {	
	@Override
	public void create () {

        OrthographicCamera camera = new OrthographicCamera(800, 600);
        ScreenViewport viewport = new ScreenViewport(camera);
        
	    scheduler.addProcess(new RenderProcess(Arrays.asList("data1.atlas"), camera, viewport));
        scheduler.addProcess(new InteractionProcess(camera));
	    scheduler.addProcess(new TestDataCreatorProcess(0, 0));
        scheduler.addProcess(new TestDataCreatorProcess(300, 200));
	}
}
