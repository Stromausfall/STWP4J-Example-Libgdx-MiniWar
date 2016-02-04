package net.matthiasauer.stwp4j.libgdx.miniwar.model.test;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.ui.ButtonClickEvent;

public class WorldProcess extends LightweightProcess {
    private final ChannelInPort<WorldInteraction> worldInteractionChannel;
    private final ChannelOutPort<WorldSnapShot> worldSnapShotChannel;
    private final ChannelInPort<ButtonClickEvent> clickEventInPort;

    public WorldProcess(ChannelInPort<WorldInteraction> worldInteractionChannel,
            ChannelOutPort<WorldSnapShot> worldSnapShotChannel, ChannelInPort<ButtonClickEvent> clickEventInPort) {
        this.worldInteractionChannel = worldInteractionChannel;
        this.worldSnapShotChannel = worldSnapShotChannel;
        this.clickEventInPort = clickEventInPort;
    }

    @Override
    protected void execute() {
        ButtonClickEvent event = null;

        while ((event = clickEventInPort.poll()) != null) {
            System.err.println("--> " + event.getId() + " clicked !");
        }
    }
}
