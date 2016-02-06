package net.matthiasauer.stwp4j.libgdx.miniwar.model;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.miniwar.controller.WorldInteraction;
import net.matthiasauer.stwp4j.libgdx.ui.ButtonClickEvent;

public class WorldProcess extends LightweightProcess {
    private int round;
    private final ChannelInPort<WorldInteraction> worldInteractionChannel;
    private final ChannelOutPort<WorldSnapShot> worldSnapShotChannel;

    public WorldProcess(ChannelInPort<WorldInteraction> worldInteractionChannel,
            ChannelOutPort<WorldSnapShot> worldSnapShotChannel) {
        this.worldInteractionChannel = worldInteractionChannel;
        this.worldSnapShotChannel = worldSnapShotChannel;
        this.round = 0;
    }
    
    @Override
    protected void preIteration() {
        this.worldSnapShotChannel.offer(
                new WorldSnapShot(round, 50, 5, 6, 55, 50));
    }

    @Override
    protected void execute() {
    }
}
