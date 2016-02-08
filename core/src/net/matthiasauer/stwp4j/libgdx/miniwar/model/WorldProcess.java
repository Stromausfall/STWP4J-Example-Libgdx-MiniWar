package net.matthiasauer.stwp4j.libgdx.miniwar.model;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.miniwar.controller.WorldInteraction;

/**
 * This process modifies the world according to WorldInteraction objects and
 * periodically creates WorldSnapshot instances which will be used to display
 * the world
 */
public class WorldProcess extends LightweightProcess {
    private int round;
    private final ChannelInPort<WorldInteraction> worldInteractionChannel;
    private final ChannelOutPort<WorldSnapShot> worldSnapShotChannel;
    private int player1Army = 10;
    private int player2Army = 10;
    private int player1Industry = 10;
    private int player2Industry = 10;

    public WorldProcess(ChannelInPort<WorldInteraction> worldInteractionChannel,
            ChannelOutPort<WorldSnapShot> worldSnapShotChannel) {
        this.worldInteractionChannel = worldInteractionChannel;
        this.worldSnapShotChannel = worldSnapShotChannel;
        this.round = 0;
    }

    @Override
    protected void preIteration() {
        this.worldSnapShotChannel.offer(new WorldSnapShot(round, 50, this.player1Industry, this.player2Industry,
                this.player1Army, this.player2Army));
    }

    @Override
    protected void execute() {
        WorldInteraction worldInteraction = null;

        while ((worldInteraction = this.worldInteractionChannel.poll()) != null) {
            switch (worldInteraction) {
            case Attack:
                this.player1Army -= 1;
                break;
            case IncreaseArmy:
                this.player1Army += 1;
                break;
            case IncreaseIndustry:
                this.player1Industry += 1;
                break;
            default:
                throw new NullPointerException("Unknown world interaction : " + worldInteraction);
            }
        }
    }
}
