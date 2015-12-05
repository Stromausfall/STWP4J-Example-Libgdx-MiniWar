package net.matthiasauer.stwp4j.libgdx.graphic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pools;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelPortsCreated;
import net.matthiasauer.stwp4j.ChannelPortsRequest;
import net.matthiasauer.stwp4j.ExecutionState;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.PortType;
import net.matthiasauer.stwp4j.libgdx.utils.InputTools;

public class InteractionProcess extends LightweightProcess implements InputProcessor {
    private static enum InputTouchEventType {
        TouchDown,
        TouchUp,
        Moved,
        Dragged
    }
    
    private static class EventData {
        public int screenX;
        public int screenY;
        public InputTouchEventType inputType;
        public int argument;
    }
    
    public static final String RENDEREDDATA_CHANNEL = "rendereddata-channel";
    private ChannelInPort<RenderedData> renderedDataChannel;
    private final Collection<EventData> lastEvents;
    private final Set<RenderedData> renderedData;
    private final OrthographicCamera camera;
    private final RenderTextureArchiveSystem archive;

    public InteractionProcess(OrthographicCamera camera) {
        super(
                new ChannelPortsRequest<RenderedData>(
                        RENDEREDDATA_CHANNEL,
                        PortType.InputExclusive,
                        RenderedData.class));

        this.archive = new RenderTextureArchiveSystem();
        this.renderedData = new HashSet<RenderedData>();
        this.lastEvents = new ArrayList<EventData>();
        this.camera = camera;
        
        // register this process as an input processor
        InputTools.addInputProcessor(this);
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

    
    
    
    
    
    
    
    
    
    
    
    private Vector2 getPosition(InputTouchEventComponent event, boolean isProjected) {
        if (isProjected) {
            return event.projectedPosition;
        } else {
            return event.unprojectedPosition;
        }
    }
    
    private Rectangle getRectangle(boolean isProjected, RenderedComponent renderedComponent) {
        if (isProjected) {
            return renderedComponent.renderedTarget;
        } else {
            Rectangle rectangle =
                    new Rectangle(renderedComponent.renderedTarget);

            // 'unzoom' the rendered rectangle - because the 
            // position is also 'unzoomed' (unprojected)
            rectangle.x /= renderedComponent.zoomFactor;
            rectangle.y /= renderedComponent.zoomFactor;
            
            return rectangle;
        }
    }
    
    /**
     * Instead of rotating the image we only rotate the mouse position !
     * @param position
     * @param rectangle
     * @param rotation
     * @param specializationType 
     */
    private Vector2 rotatePosition(Vector2 position, Rectangle rectangle, float rotation, RenderSpecialization specializationType) {
        rotation = 360 - rotation;
        
        // get center of rectangle
        Vector2 center = new Vector2();
        center = rectangle.getCenter(center);
        
        if (specializationType == RenderSpecialization.Text) {
            rectangle.getPosition(center);
        }
        
        // the arrow points from the center to the position
        // we create a new vecotr because we don't want to modify the position !
        Vector2 arrow = new Vector2(position);
        arrow.sub(center);
        
        // now rotate the arrow
        arrow.rotate(rotation);
        
        // finally attach it to the center again !
        arrow.add(center);
        
        return arrow;
    }
    
    private boolean touchesVisiblePartOfTarget(
            InputTouchEventComponent event,
            Entity targetEntity,
            RenderComponent renderComponent,
            RenderedComponent renderedComponent) {
        boolean isProjected = renderComponent.renderProjected;
        Vector2 position = this.getPosition(event, isProjected);
        Rectangle rectangle = this.getRectangle(isProjected, renderedComponent);
        RenderSpecialization specializationType =
                renderComponent.getSpecializationType();
        
        if (renderComponent.rotation != 0) {
            // get a new 'rotated vector'
            position =
                    this.rotatePosition(position, rectangle, renderComponent.rotation, specializationType);
        }
        
        // if in the bounding box
        if (rectangle.contains(position)) {
            if (specializationType == RenderSpecialization.Text) {
                // for the text render the mouse has to be just in the rectangle !
                return true;
            }
            
            if (specializationType == RenderSpecialization.Sprite) {
                // for a sprite we do pixel perfect detection !
                SpriteRenderSpecialization specialization =
                        renderComponent.getSpriteSpecialization();
                AtlasRegion spriteTexture =
                        specialization.spriteTexture;
                
                if (spriteTexture == null) {
                    throw new NullPointerException("texture was null !");
                }

                InputTouchTargetComponent targetComponent =
                        this.targetComponentMapper.get(targetEntity);
    
                if (this.isClickedPixelVisible(rectangle, spriteTexture, targetComponent, position)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private void getTouchedEntity() {
    }
    
    private Entity iterateOverAllEntitiesToFindTouched(InputTouchEventComponent event) {
        int orderOfCurrentTarget = -1;
        Entity touchedEntity = null;

        // go over all entities
        for (Entity targetEntity : targetEntities) {
            RenderComponent renderComponent =
                    this.renderComponentMapper.get(targetEntity);
            RenderedComponent renderedComponent =
                    this.renderedComponentMapper.get(targetEntity);
            
            // search for the one that is touched and has the highest order of the layer
            if (this.touchesVisiblePartOfTarget(event, targetEntity, renderComponent, renderedComponent)) {

                if (renderComponent.renderOrder > orderOfCurrentTarget) {
                    orderOfCurrentTarget = renderComponent.renderOrder;
                    touchedEntity = targetEntity;
                }
            }
        }
        
        return touchedEntity;
    }
    
    private void saveEvent2(int screenX, int screenY, InputTouchEventType inputType, int argument) {
        Vector3 projected = new Vector3(screenX, screenY, 0);
        Vector3 unprojected = this.camera.unproject(projected);
        InputTouchEventComponent newEvent =
                this.engine.createComponent(InputTouchEventComponent.class);
        newEvent.argument = argument;
        newEvent.target = null;
        newEvent.inputType = inputType;
        newEvent.projectedPosition.x = unprojected.x;
        newEvent.projectedPosition.y = unprojected.y;
        newEvent.unprojectedPosition.x = screenX - (Gdx.graphics.getWidth() / 2);
        newEvent.unprojectedPosition.y = (Gdx.graphics.getHeight() / 2) - screenY;
        
        this.lastEvents.add(newEvent);
    }
    
    @Override
    public void update(float deltaTime) {
        
        Entity touchedEntity = null;
        
        // we only need to get the entity once - because the mouse is at the same position
        // at the moment of time
        if (!this.lastEvents.isEmpty()) {
            touchedEntity =
                    this.iterateOverAllEntitiesToFindTouched(this.lastEvents.get(0));
        }
        
        for (InputTouchEventComponent eventToProcess : this.lastEvents) {
            Gdx.app.debug(
                    "InputTouchGeneratorSystem",
                    eventToProcess.inputType + " - " + eventToProcess.target);

            eventToProcess.target = touchedEntity;
            
            // save the event in an entity
            this.containerEntities.add(eventToProcess);
        }
        
        this.lastEvents.clear();
    }
    
    private boolean isClickedPixelVisible(Rectangle renderedRectangle, AtlasRegion spriteTexture, InputTouchTargetComponent targetComponent, Vector2 position) {
        // http://gamedev.stackexchange.com/questions/43943/how-to-detect-a-touch-on-transparent-area-of-an-image-in-a-libgdx-stage
        Pixmap pixmap =
                this.archive.getPixmap(spriteTexture.getTexture());

        // we want the position of the pixel in the texture !
        // first add the offset of the region inside the texture, then add the position inside the texture !
        // -> because we need the position inside the texture
        int pixelX =
                (int)(spriteTexture.getRegionX() + position.x - renderedRectangle.x);
        
        // the same goes for the Y component, BUT the Y axis is inverted, therefore
        // we need to invert the position INSIDE the texture !
        // --> that's why we use regionHeigth - positionInsideTexture
        int pixelY =
                (int)(spriteTexture.getRegionY() + spriteTexture.getRegionHeight() - (position.y - renderedRectangle.y));

        int pixel =
                pixmap.getPixel(pixelX, pixelY);

        return (pixel & 0x000000ff) != 0;
    }

    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }
    
    private void saveEvent(int screenX, int screenY, InputTouchEventType inputType, int argument) {
        EventData event = Pools.get(EventData.class).obtain();
        
        event.screenX = screenX;
        event.screenY = screenY;
        event.inputType = inputType;
        event.argument = argument;
        
        this.lastEvents.add(event);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.saveEvent(screenX, screenY, InputTouchEventType.TouchDown, button);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        this.saveEvent(screenX, screenY, InputTouchEventType.TouchUp, button);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        this.saveEvent(screenX, screenY, InputTouchEventType.Dragged, pointer);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        this.saveEvent(screenX, screenY, InputTouchEventType.Moved, 0);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }
}
    
    
    
    
}
