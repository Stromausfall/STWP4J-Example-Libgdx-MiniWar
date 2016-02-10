package net.matthiasauer.stwp4j.libgdx.miniwar.desktop;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class Tools {
	public static void main(String[] args) throws URISyntaxException {
        String packFileName = "data1.atlas";

	    // get the location of the executed file
        URI location = Tools.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        
        // move up two levels
        String root = Paths.get(location).getParent().getParent().toString();
        String androidAssetsFolder = Paths.get(root, "android", "assets").toString();
        String gfxSourceFolder = Paths.get(root, "gfxSource", packFileName).toString();

        System.out.println("source : " + gfxSourceFolder);
        System.out.println("target : " + androidAssetsFolder);
        
        TexturePacker.process(gfxSourceFolder, androidAssetsFolder, packFileName);
	}
}
