package com.melliott.gtdfx.gtdclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class GtdApplication extends Application {
    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        this.context = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GtdApplication.class.getResource("gtd.fxml"));
        fxmlLoader.setController(context.getBean(GtdController.class));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("gtdFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}