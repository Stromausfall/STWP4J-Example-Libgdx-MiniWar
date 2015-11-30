package net.matthiasauer.stwp4j.example.libgdx.miniwar.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.matthiasauer.stwp4j.example.libgdx.miniwar.MiniWar;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new MiniWar(), config);
	}
}
