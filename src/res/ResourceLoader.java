package res;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class ResourceLoader {
    public static Image loadImage(String resourceName) throws IOException {
        URL url = ResourceLoader.class.getClassLoader().getResource(resourceName);
        if (url == null) {
            throw new IOException("Resource not found: " + resourceName);
        }
        return ImageIO.read(url);
    }
}
