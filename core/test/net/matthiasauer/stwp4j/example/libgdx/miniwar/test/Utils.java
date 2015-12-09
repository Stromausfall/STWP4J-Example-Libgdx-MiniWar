package net.matthiasauer.stwp4j.example.libgdx.miniwar.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;

public class Utils {
    public static void initializeDummyGraphics() {
        Gdx.graphics =
                new Graphics() {
                    @Override
                    public boolean supportsExtension(String extension) {
                        // TODO Auto-generated method stub
                        return false;
                    }
                    
                    @Override
                    public boolean supportsDisplayModeChange() {
                        // TODO Auto-generated method stub
                        return false;
                    }
                    
                    @Override
                    public void setVSync(boolean vsync) {
                        // TODO Auto-generated method stub
                        
                    }
                    
                    @Override
                    public void setTitle(String title) {
                        // TODO Auto-generated method stub
                        
                    }
                    
                    @Override
                    public boolean setDisplayMode(int width, int height, boolean fullscreen) {
                        // TODO Auto-generated method stub
                        return false;
                    }
                    
                    @Override
                    public boolean setDisplayMode(DisplayMode displayMode) {
                        // TODO Auto-generated method stub
                        return false;
                    }
                    
                    @Override
                    public void setCursor(Cursor cursor) {
                        // TODO Auto-generated method stub
                        
                    }
                    
                    @Override
                    public void setContinuousRendering(boolean isContinuous) {
                        // TODO Auto-generated method stub
                        
                    }
                    
                    @Override
                    public void requestRendering() {
                        // TODO Auto-generated method stub
                        
                    }
                    
                    @Override
                    public Cursor newCursor(Pixmap pixmap, int xHotspot, int yHotspot) {
                        // TODO Auto-generated method stub
                        return null;
                    }
                    
                    @Override
                    public boolean isGL30Available() {
                        // TODO Auto-generated method stub
                        return false;
                    }
                    
                    @Override
                    public boolean isFullscreen() {
                        // TODO Auto-generated method stub
                        return false;
                    }
                    
                    @Override
                    public boolean isContinuousRendering() {
                        // TODO Auto-generated method stub
                        return false;
                    }
                    
                    @Override
                    public int getWidth() {
                        // TODO Auto-generated method stub
                        return 0;
                    }
                    
                    @Override
                    public GraphicsType getType() {
                        // TODO Auto-generated method stub
                        return null;
                    }
                    
                    @Override
                    public float getRawDeltaTime() {
                        // TODO Auto-generated method stub
                        return 0;
                    }
                    
                    @Override
                    public float getPpiY() {
                        // TODO Auto-generated method stub
                        return 0;
                    }
                    
                    @Override
                    public float getPpiX() {
                        // TODO Auto-generated method stub
                        return 0;
                    }
                    
                    @Override
                    public float getPpcY() {
                        // TODO Auto-generated method stub
                        return 0;
                    }
                    
                    @Override
                    public float getPpcX() {
                        // TODO Auto-generated method stub
                        return 0;
                    }
                    
                    @Override
                    public int getHeight() {
                        // TODO Auto-generated method stub
                        return 0;
                    }
                    
                    @Override
                    public GL30 getGL30() {
                        // TODO Auto-generated method stub
                        return null;
                    }
                    
                    @Override
                    public GL20 getGL20() {
                        // TODO Auto-generated method stub
                        return null;
                    }
                    
                    @Override
                    public int getFramesPerSecond() {
                        // TODO Auto-generated method stub
                        return 0;
                    }
                    
                    @Override
                    public long getFrameId() {
                        // TODO Auto-generated method stub
                        return 0;
                    }
                    
                    @Override
                    public DisplayMode[] getDisplayModes() {
                        // TODO Auto-generated method stub
                        return null;
                    }
                    
                    @Override
                    public DisplayMode getDesktopDisplayMode() {
                        // TODO Auto-generated method stub
                        return null;
                    }
                    
                    @Override
                    public float getDensity() {
                        // TODO Auto-generated method stub
                        return 0;
                    }
                    
                    @Override
                    public float getDeltaTime() {
                        // TODO Auto-generated method stub
                        return 0;
                    }
                    
                    @Override
                    public BufferFormat getBufferFormat() {
                        // TODO Auto-generated method stub
                        return null;
                    }
                };
    }
    
    public static Camera createDummyCamera() {
        return new OrthographicCamera() {
                @Override
                public com.badlogic.gdx.math.Vector3 unproject(com.badlogic.gdx.math.Vector3 screenCoords) {
                    return screenCoords;
                }
            };
    }
}
