package com.asdeire.presentation.util;

import javafx.fxml.FXMLLoader;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.URL;

/**
 * Utility class for loading FXML files with Spring-managed controllers.
 * This class uses FXMLLoader to load FXML files and sets the controller factory to retrieve controllers from the Spring application context.
 */
public class SpringFXMLLoader {

    private final ApplicationContext context;

    /**
     * Constructs a SpringFXMLLoader with the specified ApplicationContext.
     *
     * @param context The ApplicationContext instance.
     */
    public SpringFXMLLoader(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Loads an FXML file from the specified URL and sets the controller factory to retrieve controllers from the Spring application context.
     *
     * @param url The URL of the FXML file to load.
     * @return The root object loaded from the FXML file.
     * @throws IOException If an error occurs during FXML file loading.
     */
    public Object load(URL url) throws IOException {
        FXMLLoader loader = new FXMLLoader(url);
        loader.setControllerFactory(context::getBean);
        return loader.load();
    }
}
