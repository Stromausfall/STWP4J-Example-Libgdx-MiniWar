package net.matthiasauer.stwp4j.libgdx.miniwar.gui.interaction;

import net.matthiasauer.stwp4j.ChannelPortsCreated;
import net.matthiasauer.stwp4j.ExecutionState;
import net.matthiasauer.stwp4j.LightweightProcess;

public class GuiInteractionProcess extends LightweightProcess {
    public static final String CLICKEVENT_CHANNEL = "clickevent-channel";

    @Override
    protected ExecutionState execute() {
        // TODO Auto-generated method stub
        return ExecutionState.Finished;
    }

    @Override
    protected void initialize(ChannelPortsCreated createdChannelPorts) {
        // TODO Auto-generated method stub
        
    }

}
