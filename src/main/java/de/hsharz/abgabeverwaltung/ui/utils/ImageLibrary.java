package de.hsharz.abgabeverwaltung.ui.utils;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageLibrary {

    public static final String        IMAGE_PATH   = "icons/";

    private static Map<String, Image> loadedImages = new HashMap<>();

    public static Image getImage(final String imageName) {
        Image image = loadedImages.get(imageName);
        if (image == null) {
            loadImageFromResources(imageName);
            image = loadedImages.get(imageName);
        }
        return image;
    }

    public static ImageView getImageViewScaled(final String imageName, final int size) {
        ImageView imageView = getImageView(imageName);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        return imageView;
    }

    public static ImageView getImageView(final String imageName) {
        return new ImageView(getImage(imageName));
    }

    private static void loadImageFromResources(final String imageName) {
        loadedImages.put(imageName, new Image(ImageLibrary.class.getClassLoader().getResourceAsStream(IMAGE_PATH + imageName)));
    }

}
