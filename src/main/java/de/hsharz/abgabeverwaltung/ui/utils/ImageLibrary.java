package de.hsharz.abgabeverwaltung.ui.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class ImageLibrary {

    public static final String IMAGE_PATH = "icons/";

    private static Map<String, Image> loadedImages = new HashMap<>();

    public static Image getImage(String imageName) {
        Image image = loadedImages.get(imageName);
        if (image == null) {
            loadImageFromResources(imageName);
            image = loadedImages.get(imageName);
        }
        return image;
    }

    public static ImageView getImageViewScaled(String imageName, int size) {
        ImageView imageView = getImageView(imageName);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        return imageView;
    }

    public static ImageView getImageView(String imageName) {
        return new ImageView(getImage(imageName));
    }

    private static void loadImageFromResources(String imageName) {
        loadedImages.put(imageName, new Image(ImageLibrary.class.getClassLoader().getResourceAsStream(IMAGE_PATH + imageName)));
    }

}
