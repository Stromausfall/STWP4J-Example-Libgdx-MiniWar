package net.matthiasauer.stwp4j.libgdx.miniwar.controller;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.ui.ButtonClickEvent;

public class WorldInteractionProcess extends LightweightProcess {
    private final ChannelOutPort<WorldInteraction> worldInteractionOutPort;
    private final ChannelInPort<ButtonClickEvent> buttonClickInPort;
    
    public WorldInteractionProcess(ChannelOutPort<WorldInteraction> worldInteractionOutPort, ChannelInPort<ButtonClickEvent> buttonClickInPort) {
        this.worldInteractionOutPort = worldInteractionOutPort;
        this.buttonClickInPort = buttonClickInPort;
    }

    @Override
    protected void execute() {
        ButtonClickEvent event = null;

        while ((event = buttonClickInPort.poll()) != null) {
            System.err.println("--> " + event.getId() + " clicked !");
        }
    }
}
