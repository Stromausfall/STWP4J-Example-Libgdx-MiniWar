package net.matthiasauer.stwp4j.libgdx.miniwar;

import java.util.Arrays;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.matthiasauer.stwp4j.libgdx.application.ApplicationEntryPointProcess;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderProcess;
import net.matthiasauer.stwp4j.libgdx.miniwar.model.test.TestDataConsumerProcess;
import net.matthiasauer.stwp4j.libgdx.miniwar.model.test.TestDataCreatorProcess;

public class EntryPoint extends ApplicationEntryPointProcess {
    @Override
    public void create() {
        scheduler.addProcess(new RenderProcess(Arrays.asList("data1.atlas"), true));
        scheduler.addProcess(new TestDataCreatorProcess(0, 0));
        scheduler.addProcess(new TestDataCreatorProcess(300, 200));
        scheduler.addProcess(new TestDataConsumerProcess());
    }
}
