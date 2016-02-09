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

/**
 * This process transforms WorldSnapshots into RenderData that will be displayed
 * using the RenderProcess
 */
public class WorldDisplayProcess extends LightweightProcess {
    final String fontName = "arial#24";
    final SpriteRenderData background = new SpriteRenderData("background", 0, 0, 0, RenderPositionUnit.Pixels, null,
            -100, false, "background");
    SpriteRenderData progress = new SpriteRenderData("progress", 0, 0, 0, RenderPositionUnit.Pixels, null, -200, false,
            "progress");
    final TextRenderData textRenderData = new TextRenderData("", -240, 210, 0, RenderPositionUnit.Pixels, Color.BLACK,
            1, true, "increase industry", fontName);
    final TextRenderData textRenderData2 = new TextRenderData("", -240, 140, 0, RenderPositionUnit.Pixels, Color.BLACK,
            1, true, "increase army", fontName);
    final TextRenderData textRenderData3 = new TextRenderData("", -240, 70, 0, RenderPositionUnit.Pixels, Color.BLACK,
            1, true, "attack", fontName);
    private final ChannelOutPort<RenderData> renderDataOutPort;
    private final ChannelInPort<WorldSnapShot> worldSnapShotInPort;
    private WorldSnapShot latest = null;

    public WorldDisplayProcess(ChannelOutPort<RenderData> renderDataOutPort,
            ChannelInPort<WorldSnapShot> worldSnapShotInPort) {
        this.renderDataOutPort = renderDataOutPort;
        this.worldSnapShotInPort = worldSnapShotInPort;
    }

    @Override
    protected void preIteration() {
        renderDataOutPort.offer(textRenderData);
        renderDataOutPort.offer(textRenderData2);
        renderDataOutPort.offer(textRenderData3);
        renderDataOutPort.offer(progress);
        renderDataOutPort.offer(background);

        if (this.latest != null) {
            // show turn label
            renderDataOutPort.offer(new TextRenderData("turn label", -50, 50, 0, RenderPositionUnit.Pixels, null, 5,
                    false, "Turn #" + this.latest.round, this.fontName));

            // show progress label
            renderDataOutPort.offer(new TextRenderData("progress label", -10, 10, 0, RenderPositionUnit.Pixels, null, 5,
                    false, this.latest.progress + "%", this.fontName));

            // show army and industry stats
            renderDataOutPort.offer(new TextRenderData("army 1 size", -300, -100, 0, RenderPositionUnit.Pixels,
                    Color.BLUE, 5, false, "Player Army Size : " + this.latest.player1Army, this.fontName));
            renderDataOutPort.offer(new TextRenderData("industry 1 size", -300, -150, 0, RenderPositionUnit.Pixels,
                    Color.BLUE, 5, false, "Player Factories : " + this.latest.player1Factories, this.fontName));

            renderDataOutPort.offer(new TextRenderData("army 2 size", 50, -100, 0, RenderPositionUnit.Pixels,
                    Color.CORAL, 5, false, "Enemy Army Size : " + this.latest.player2Army, this.fontName));
            renderDataOutPort.offer(new TextRenderData("industry 2 size", 50, -150, 0, RenderPositionUnit.Pixels,
                    Color.CORAL, 5, false, "Enemy Factories : " + this.latest.player2Factories, this.fontName));

            this.progress = new SpriteRenderData(this.progress.getId(),
                    (int) (4 * (50 - this.latest.progress)), this.progress.getPosition().y,
                    this.progress.getRotation(), this.progress.getRenderPositionUnit(), this.progress.getTint(),
                    this.progress.getRenderOrder(), this.progress.isRenderProjected(), this.progress.getTextureName());
        }
    }

    @Override
    protected void execute() {
        WorldSnapShot snapShot = null;

        while ((snapShot = this.worldSnapShotInPort.poll()) != null) {
            this.latest = snapShot;
        }
    }
}
