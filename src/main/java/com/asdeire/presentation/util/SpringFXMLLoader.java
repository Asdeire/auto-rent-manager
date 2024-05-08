package com.asdeire.presentation.util;

import javafx.fxml.FXMLLoader;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.URL;

public class SpringFXMLLoader {

    private final ApplicationContext context;

    public SpringFXMLLoader(ApplicationContext context) {
        this.context = context;
    }

    public Object load(URL url) throws IOException {
        FXMLLoader loader = new FXMLLoader(url);
        loader.setControllerFactory(context::getBean);
        return loader.load();
    }
}