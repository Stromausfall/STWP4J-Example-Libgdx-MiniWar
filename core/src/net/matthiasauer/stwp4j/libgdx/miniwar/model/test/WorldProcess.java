package net.matthiasauer.stwp4j.libgdx.miniwar.model.test;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;

public class WorldProcess extends LightweightProcess {
    private ChannelInPort<WorldInteraction> worldInteractionChannel;
    private ChannelOutPort<WorldSnapShot> worldSnapShotChannel;

    public WorldProcess(ChannelInPort<WorldInteraction> worldInteractionChannel,
            ChannelOutPort<WorldSnapShot> worldSnapShotChannel) {
        this.worldInteractionChannel = worldInteractionChannel;
        this.worldSnapShotChannel = worldSnapShotChannel;
    }

    @Override
    protected void execute() {
    }
}
