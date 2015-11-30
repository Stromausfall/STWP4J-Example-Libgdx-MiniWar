package net.matthiasauer.stwp4j.example.libgdx.miniwar;

import com.badlogic.gdx.ApplicationAdapter;

import net.matthiasauer.stwp4j.Scheduler;
import net.matthiasauer.stwp4j.example.libgdx.miniwar.gui.render.RenderProcess;
import net.matthiasauer.stwp4j.example.libgdx.miniwar.model.test.TestDataCreatorProcess;

public class MiniWar extends ApplicationAdapter {
    private final Scheduler scheduler = new Scheduler();
	
	@Override
	public void create () {	    
	    scheduler.addProcess(new RenderProcess());
	    scheduler.addProcess(new TestDataCreatorProcess(0, 0));
        scheduler.addProcess(new TestDataCreatorProcess(300, 200));
	}

	@Override
	public void render () {
	    scheduler.performIteration();
	}
}
