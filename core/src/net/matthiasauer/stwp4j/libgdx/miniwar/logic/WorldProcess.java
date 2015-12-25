package net.matthiasauer.stwp4j.libgdx.miniwar.logic;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.ChannelPortsCreated;
import net.matthiasauer.stwp4j.ChannelPortsRequest;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.PortType;

public class WorldProcess extends LightweightProcess {
    public static final String WORLDINTERACTION_CHANNEL = "world-interaction channel";
    public static final String WORLDSNAPSHOT_CHANNEL = "world-snapshot channel";
    private static final ChannelPortsRequest<WorldInteraction> worldInteractionChannelRequest = new ChannelPortsRequest<WorldInteraction>(
            WORLDINTERACTION_CHANNEL, PortType.InputExclusive, WorldInteraction.class);
    private static final ChannelPortsRequest<WorldSnapShot> worldSnapShotChannelRequest = new ChannelPortsRequest<WorldSnapShot>(
            WORLDSNAPSHOT_CHANNEL, PortType.OutputExclusive, WorldSnapShot.class);
    private ChannelInPort<WorldInteraction> worldInteractionChannel;
    private ChannelOutPort<WorldSnapShot> worldSnapShotChannel;

    public WorldProcess() {
        super(worldInteractionChannelRequest, worldSnapShotChannelRequest);
    }

    @Override
    protected void execute() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void initialize(ChannelPortsCreated createdChannelPorts) {
        this.worldSnapShotChannel = createdChannelPorts.getChannelOutPort(WORLDSNAPSHOT_CHANNEL, WorldSnapShot.class);
        this.worldInteractionChannel = createdChannelPorts.getChannelInPort(WORLDINTERACTION_CHANNEL,
                WorldInteraction.class);
    }
}
