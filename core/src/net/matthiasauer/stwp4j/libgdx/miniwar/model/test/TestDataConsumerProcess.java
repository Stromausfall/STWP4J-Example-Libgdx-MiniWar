package net.matthiasauer.stwp4j.libgdx.miniwar.model.test;

import com.badlogic.gdx.utils.Pools;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.miniwar.gui.interaction.ClickEvent;

public class TestDataConsumerProcess extends LightweightProcess {
    private final ChannelInPort<ClickEvent> inputChannel;

    public TestDataConsumerProcess(ChannelInPort<ClickEvent> channelInPort) {
        this.inputChannel = channelInPort;
    }

    @Override
    protected void preIteration() {
    }

    @Override
    protected void execute() {
        ClickEvent data = null;

        while ((data = this.inputChannel.poll()) != null) {
            System.out.println("---> " + data.getId());

            // free the event !
            Pools.get(ClickEvent.class).free(data);
        }
    }
}
