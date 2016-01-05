package net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.graphic.RenderData;
import net.matthiasauer.stwp4j.libgdx.miniwar.view.clickable.ClickComponentEvent.ClickComponentType;

public class DisplayClickableProcess extends LightweightProcess {
    private final ChannelInPort<DisplayClickableRequest> displayClickableRequestChannel;
    private final ChannelInPort<ClickComponentEvent> clickComponentEventChannel;
    private final ChannelOutPort<RenderData> renderDataChannel;
    private final Map<String, DisplayClickableRequest> requests;
    private final List<ClickComponentEvent> clickComponents;

    public DisplayClickableProcess(ChannelOutPort<RenderData> renderDataChannel,
            ChannelInPort<ClickComponentEvent> clickComponentEventChannel,
            ChannelInPort<DisplayClickableRequest> displayClickableRequestChannel) {
        this.displayClickableRequestChannel = displayClickableRequestChannel;
        this.clickComponentEventChannel = clickComponentEventChannel;
        this.renderDataChannel = renderDataChannel;
        this.requests = new HashMap<String, DisplayClickableRequest>();
        this.clickComponents = new LinkedList<ClickComponentEvent>();
    }
    
    @Override
    protected void postIteration() {
        this.requests.clear();
    }
    
    private void handleRequests() {
        DisplayClickableRequest request = null;
        
        while ((request = this.displayClickableRequestChannel.poll()) != null) {
            // save all requests under the id of the images they have
            this.requests.put(request.getBaseState().getId(), request);
            this.requests.put(request.getTouchedState().getId(), request);
            this.requests.put(request.getClickState().getId(), request);
        }
    }
    
    private void handleClickComponentEvents() {
        ClickComponentEvent event = null;
        System.err.println("----------------");
        while ((event = this.clickComponentEventChannel.poll()) != null) {
            this.clickComponents.add(event);
            System.err.println("-- " + event.getId() + " - " + event.getClickComponentType());
        }
    }

    @Override
    protected void execute() {
        // collect requests
        this.handleRequests();
        
        // handle the clickComponents
        this.handleClickComponentEvents();
        
        List<ClickComponentEvent> processed = new LinkedList<ClickComponentEvent>();
        
        for (ClickComponentEvent event : this.clickComponents) {        
            System.err.println("check if it exists/null");
            
            String id = event.getId();
            DisplayClickableRequest request = this.requests.get(id);
            
            
?????????????????????????? ONE BUTTON PROCESS PER BUTTON !            
            
            
            if (request != null) {
                ClickComponentType type = event.getClickComponentType();
                
                if (type != null) {
                    switch (event.getClickComponentType()) {
                    case Down:
                        this.renderDataChannel.offer(request.getClickState());
                        break;
                    case Over:
                        this.renderDataChannel.offer(request.getTouchedState());
                        break;
                    case Up:
                        this.renderDataChannel.offer(request.getBaseState());
                        break;
                    case Left:
                        this.renderDataChannel.offer(request.getBaseState());
                        break;
                    default:
                        this.renderDataChannel.offer(request.getBaseState());
                        break;
                    }
                }
            
                processed.add(event);
            }
        }
        
        this.clickComponents.removeAll(processed);
    }
}
