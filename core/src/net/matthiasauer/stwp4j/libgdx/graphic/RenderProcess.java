package net.matthiasauer.stwp4j.libgdx.graphic;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelPortsCreated;
import net.matthiasauer.stwp4j.ChannelPortsRequest;
import net.matthiasauer.stwp4j.ExecutionState;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.PortType;
import net.matthiasauer.stwp4j.libgdx.application.ApplicationEntryPointProcess;
import net.matthiasauer.stwp4j.libgdx.application.ApplicationEvent;
import net.matthiasauer.stwp4j.libgdx.application.ApplicationEventType;
import net.matthiasauer.stwp4j.libgdx.application.ResizeApplicationEvent;

public class RenderProcess extends LightweightProcess {
    /**
     * 11 is the default size internally so start with that initial capacity
     */
    private static final int sortedRenderComponentsInitialSize = 11;
    public static final String RENDERDATA_CHANNEL = "renderdata-channel";
    public final OrthographicCamera camera;
    private final SpriteBatch spriteBatch;
    private final Queue<RenderData> sortedRenderComponents;
    private final RenderSpriteSubSystem renderSpriteSubSystem;
    private final RenderTextSubSystem renderTextSubSystem;
    private final Viewport viewport;
    private ChannelInPort<RenderData> renderDataChannel;
    private ChannelInPort<ApplicationEvent> applicationEventChannel;
    
    public RenderProcess(List<String> atlasFilePaths) {
        super(
                new ChannelPortsRequest<RenderData>(
                        RENDERDATA_CHANNEL,
                        PortType.InputExclusive,
                        RenderData.class),
                new ChannelPortsRequest<ApplicationEvent>(
                        ApplicationEntryPointProcess.APPLICATION_EVENT_CHANNEL,
                        PortType.InputMultiplex,
                        ApplicationEvent.class));

        this.camera = new OrthographicCamera(800, 600);
        this.viewport = new ScreenViewport(this.camera);
        this.spriteBatch = new SpriteBatch();
        this.renderSpriteSubSystem =
                new RenderSpriteSubSystem(
                        new TextureLoader(atlasFilePaths),
                        this.camera,
                        this.spriteBatch);
        this.renderTextSubSystem =
                new RenderTextSubSystem(this.camera, this.spriteBatch);

        // create the PriorityQueue with the custom comparator and an initial size
        this.sortedRenderComponents =
                new PriorityQueue<RenderData>(
                        sortedRenderComponentsInitialSize,
                        new RenderDataComparator());
    }
    
    private void handleRenderDataChannel() {
        RenderData data = null;
        
        while ((data = this.renderDataChannel.poll()) != null) {
            this.sortedRenderComponents.add(data);
        }
    }
    
    private void handleApplicationEventChannel() {
        ApplicationEvent event = null;
        
        while ((event = this.applicationEventChannel.poll()) != null) {
            if (event.getApplicationEventType() == ApplicationEventType.RESIZE) {
                ResizeApplicationEvent resizeEvent = (ResizeApplicationEvent) event;

                this.viewport.update(resizeEvent.getWidth(), resizeEvent.getHeight());
            }
        }
    }

    @Override
    protected ExecutionState execute() {
        this.handleRenderDataChannel();
        this.handleApplicationEventChannel();

        // always wait for more messages
        return ExecutionState.Waiting;
    }

    @Override
    protected void initialize(ChannelPortsCreated createdChannelPorts) {
        this.renderDataChannel =
                createdChannelPorts.getChannelInPort(RENDERDATA_CHANNEL, RenderData.class);
        this.applicationEventChannel =
                createdChannelPorts.getChannelInPort(ApplicationEntryPointProcess.APPLICATION_EVENT_CHANNEL, ApplicationEvent.class);
    }

    @Override
    protected void preIteration() {
        this.sortedRenderComponents.clear();
        this.renderTextSubSystem.preIteration();

    }

    @Override
    protected void postIteration() {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.spriteBatch.begin();
        this.changeProjection(true);
        boolean lastProjectedValue = true;
        final float originalZoom = this.camera.zoom;

        // iterate over the keys in order (that's what the treemap is for)
        while (!this.sortedRenderComponents.isEmpty()) {
            RenderData baseRenderComponent = this.sortedRenderComponents.poll();
            final boolean projected = baseRenderComponent.isRenderProjected();

            if (lastProjectedValue != projected) {
                lastProjectedValue = projected;

                this.changeProjection(projected);
            }

            if (baseRenderComponent instanceof SpriteRenderData) {
                this.renderSpriteSubSystem.drawSprite((SpriteRenderData) baseRenderComponent);
                continue;
            }

            if (baseRenderComponent instanceof TextRenderData) {
                this.renderTextSubSystem.drawText((TextRenderData) baseRenderComponent);
                continue;
            }

            throw new NullPointerException("Unknown specialization of the BaseRenderComponent !");
        }

        this.spriteBatch.end();

        this.camera.zoom = originalZoom;
        this.camera.update();
    }

    private void changeProjection(boolean renderProjected) {
        // end
        this.spriteBatch.end();

        if (renderProjected) {
            this.camera.zoom = this.camera.zoom;
            this.camera.update();

            this.spriteBatch.setProjectionMatrix(this.camera.combined);
        } else {
            this.camera.zoom = 1;
            this.camera.update();

            this.spriteBatch.setProjectionMatrix(this.camera.projection);
        }

        // start new batch
        this.spriteBatch.begin();
    }
}
