package net.matthiasauer.stwp4j.libgdx.miniwar.model.test;

import com.badlogic.gdx.utils.Pools;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.graphic.InputTouchEventData;

public class TestDataConsumerProcess extends LightweightProcess {
    private final ChannelInPort<InputTouchEventData> inputChannel;

    public TestDataConsumerProcess(ChannelInPort<InputTouchEventData> inputChannel) {
        this.inputChannel = inputChannel;
    }

    @Override
    protected void preIteration() {
    }

    @Override
    protected void execute() {
        InputTouchEventData data = null;

        while ((data = this.inputChannel.poll()) != null) {
            System.out.println(
                    data.getInputTouchEventType() + "(" + data.getArgument() + ") on " + data.getTouchedRenderDataId());

            // free the event !
            Pools.get(InputTouchEventData.class).free(data);
        }
    }
}
