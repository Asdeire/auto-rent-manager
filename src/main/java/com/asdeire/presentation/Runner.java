package com.asdeire.presentation;

import atlantafx.base.theme.PrimerLight;
import com.asdeire.domain.service.impl.AuthService;
import com.asdeire.persistence.ApplicationConfig;
import com.asdeire.persistence.util.ConnectionManager;
import com.asdeire.persistence.util.DatabaseInitializer;
import com.asdeire.presentation.util.SpringFXMLLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Entry point of the application responsible for launching the JavaFX GUI and initializing Spring context and database.
 */
public class Runner extends Application {

    private static AnnotationConfigApplicationContext springContext;

    /**
     * Starts the JavaFX application.
     *
     * @param stage The primary stage of the application.
     * @throws Exception If an error occurs during application startup.
     */
    @Override
    public void start(Stage stage) throws Exception {
        var fxmlLoader = new SpringFXMLLoader(springContext);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icons/icon.png")));
        var mainFxmlResource = Runner.class.getResource("view/signIn.fxml");
        Scene scene = new Scene((Parent) fxmlLoader.load(mainFxmlResource), 600, 400);
        stage.setTitle("Authorization");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main method to launch the application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        springContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        var connectionManager = springContext.getBean(ConnectionManager.class);
        var databaseInitializer = springContext.getBean(DatabaseInitializer.class);

        try {
            databaseInitializer.init();
            launch(args);
        } finally {
            connectionManager.closePool();
        }
    }
}
