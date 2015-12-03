package net.matthiasauer.stwp4j.libgdx.graphic;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.utils.Pools;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelPortsCreated;
import net.matthiasauer.stwp4j.ChannelPortsRequest;
import net.matthiasauer.stwp4j.ExecutionState;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.PortType;

public class InteractionProcess extends LightweightProcess {
    public static final String RENDEREDDATA_CHANNEL = "rendereddata-channel";
    private ChannelInPort<RenderedData> renderedDataChannel;
    private Set<RenderedData> renderedData;

    public InteractionProcess() {
        super(
                new ChannelPortsRequest<RenderedData>(
                        RENDEREDDATA_CHANNEL,
                        PortType.InputExclusive,
                        RenderedData.class));
        
        this.renderedData = new HashSet<RenderedData>();
    }
    
    @Override
    protected void preIteration() {
        for (RenderedData data : this.renderedData) {
            Pools.get(RenderedData.class).free(data);
        }
        
        this.renderedData.clear();
    }
    
    private void handleRenderedDataChannel() {
        RenderedData data = null;
        
        while ((data = this.renderedDataChannel.poll()) != null) {
            this.renderedData.add(data);
        }
        System.err.println(Pools.get(RenderedData.class).peak);
    }
    
    @Override
    protected ExecutionState execute() {
        this.handleRenderedDataChannel();
        
        return ExecutionState.Waiting;
    }

    @Override
    protected void initialize(ChannelPortsCreated createdChannelPorts) {
        this.renderedDataChannel =
                createdChannelPorts.getChannelInPort(InteractionProcess.RENDEREDDATA_CHANNEL, RenderedData.class);
    }

}
