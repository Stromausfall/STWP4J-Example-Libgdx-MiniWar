package net.matthiasauer.stwp4j.libgdx.miniwar.controller;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.ui.ButtonClickEvent;

/**
 * This process transforms interactions with the GUI (here buttons)
 * to interactions with the game world
 */
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
            final String id = event.getId();
            
            if (id.equals("attack")) {
                this.worldInteractionOutPort.offer(WorldInteraction.Attack);
            } else if (id.equals("increase_army")) {
                this.worldInteractionOutPort.offer(WorldInteraction.IncreaseArmy);
            } else if (id.equals("increase_industry")) {
                this.worldInteractionOutPort.offer(WorldInteraction.IncreaseIndustry);
            }
        }
    }
}
