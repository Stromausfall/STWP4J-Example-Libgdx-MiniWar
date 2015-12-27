package net.matthiasauer.stwp4j.libgdx.miniwar.gui;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderData;
import net.matthiasauer.stwp4j.libgdx.miniwar.gui.interaction.ClickEvent;

public class GameGui extends LightweightProcess {
    private final ChannelOutPort<RenderData> renderDataChannel;
    private final ChannelInPort<ClickEvent> clickEventChannel;

    public GameGui(ChannelOutPort<RenderData> renderDataChannel, ChannelInPort<ClickEvent> clickEventChannel) {
        this.renderDataChannel = renderDataChannel;
        this.clickEventChannel = clickEventChannel;
    }

    @Override
    protected void execute() {
        // TODO Auto-generated method stub

    }
}
