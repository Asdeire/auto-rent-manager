package com.asdeire.presentation;

import atlantafx.base.theme.PrimerLight;
import com.asdeire.domain.service.impl.AuthServiceImpl;
import com.asdeire.persistence.ApplicationConfig;
import com.asdeire.persistence.util.ConnectionManager;
import com.asdeire.persistence.util.DatabaseInitializer;
import com.asdeire.presentation.util.SpringFXMLLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Runner extends Application {

    private static AnnotationConfigApplicationContext springContext;
    private AuthServiceImpl authService;

    @Override
    public void start(Stage stage) throws Exception {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        var fxmlLoader = new SpringFXMLLoader(springContext);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        var mainFxmlResource = Runner.class.getResource("view/signIn.fxml");
        Scene scene = new Scene((Parent) fxmlLoader.load(mainFxmlResource), 600, 400);
        stage.setTitle("Authorization");
        stage.setScene(scene);
        stage.show();
    }

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
