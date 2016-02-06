package net.matthiasauer.stwp4j.libgdx.miniwar.view;

import com.badlogic.gdx.graphics.Color;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderData;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderPositionUnit;
import net.matthiasauer.stwp4j.libgdx.graphic.SpriteRenderData;
import net.matthiasauer.stwp4j.libgdx.graphic.TextRenderData;
import net.matthiasauer.stwp4j.libgdx.miniwar.model.WorldSnapShot;

public class WorldDisplayProcess extends LightweightProcess {

    final String fontName = "arial#24";
    final SpriteRenderData background = new SpriteRenderData("background", 0, 0, 0, RenderPositionUnit.Pixels, null,
            -100, false, "background");
    final SpriteRenderData progress = new SpriteRenderData("progress", 0, 0, 0, RenderPositionUnit.Pixels, null, -200,
            false, "progress");
    final TextRenderData textRenderData = new TextRenderData("", -240, 210, 0, RenderPositionUnit.Pixels, Color.BLACK,
            1, true, "increase industry", fontName);
    final TextRenderData textRenderData2 = new TextRenderData("", -240, 110, 0, RenderPositionUnit.Pixels, Color.BLACK,
            1, true, "increase army", fontName);
    private final ChannelOutPort<RenderData> renderDataOutPort;
    private final ChannelInPort<WorldSnapShot> worldSnapShotInPort;
    private WorldSnapShot latest = new WorldSnapShot(0, 0, 0, 0, 0, 0);

    public WorldDisplayProcess(ChannelOutPort<RenderData> renderDataOutPort,
            ChannelInPort<WorldSnapShot> worldSnapShotInPort) {
        this.renderDataOutPort = renderDataOutPort;
        this.worldSnapShotInPort = worldSnapShotInPort;
    }

    @Override
    protected void preIteration() {
        renderDataOutPort.offer(textRenderData);
        renderDataOutPort.offer(textRenderData2);
        renderDataOutPort.offer(progress);
        renderDataOutPort.offer(background);

        // show turn label
        renderDataOutPort.offer(new TextRenderData("turn label", -50, 50, 0, RenderPositionUnit.Pixels, null, 5, false,
                "Turn #" + this.latest.round, this.fontName));

        // show progress label
        renderDataOutPort.offer(new TextRenderData("progress label", -10, 10, 0, RenderPositionUnit.Pixels, null, 5, false,
                this.latest.round + "%", this.fontName));
    }

    @Override
    protected void execute() {
        WorldSnapShot snapShot = null;

        while ((snapShot = this.worldSnapShotInPort.poll()) != null) {
            this.latest = snapShot;
        }
    }
}
