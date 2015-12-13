package net.matthiasauer.stwp4j.libgdx.miniwar.model.test;

import com.badlogic.gdx.utils.Pools;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelPortsCreated;
import net.matthiasauer.stwp4j.ChannelPortsRequest;
import net.matthiasauer.stwp4j.ExecutionState;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.PortType;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventData;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderProcess;

public class TestDataConsumerProcess extends LightweightProcess {
    private ChannelInPort<InputTouchEventData> inputChannel;

    public TestDataConsumerProcess() {
        super(new ChannelPortsRequest<InputTouchEventData>(RenderProcess.INPUTTOUCHEVENTDATA_CHANNEL,
                PortType.InputExclusive, InputTouchEventData.class));
    }

    @Override
    protected ExecutionState execute() {
        InputTouchEventData data = null;

        while ((data = this.inputChannel.poll()) != null) {
            System.out.println(
                    data.getInputTouchEventType() + "(" + data.getArgument() + ") on " + data.getTouchedRenderDataId());

            // free the event !
            Pools.get(InputTouchEventData.class).free(data);
        }

        return ExecutionState.Finished;
    }

    @Override
    protected void initialize(ChannelPortsCreated createdChannelPorts) {
        this.inputChannel = createdChannelPorts.getChannelInPort(RenderProcess.INPUTTOUCHEVENTDATA_CHANNEL,
                InputTouchEventData.class);
    }

}
