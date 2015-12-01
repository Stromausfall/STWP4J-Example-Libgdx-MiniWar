package net.matthiasauer.stwp4j.example.libgdx.miniwar;

import java.util.Arrays;

import net.matthiasauer.stwp4j.example.libgdx.miniwar.model.test.TestDataCreatorProcess;
import net.matthiasauer.stwp4j.libgdx.application.ApplicationEntryPointProcess;
import net.matthiasauer.stwp4j.libgdx.gui.RenderProcess;

public class MiniWar extends ApplicationEntryPointProcess {	
	@Override
	public void create () {	    
	    scheduler.addProcess(new RenderProcess(Arrays.asList("data1.atlas")));
	    scheduler.addProcess(new TestDataCreatorProcess(0, 0));
        scheduler.addProcess(new TestDataCreatorProcess(300, 200));
	}
}
