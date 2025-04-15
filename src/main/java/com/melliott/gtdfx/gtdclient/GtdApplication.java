package com.melliott.gtdfx.gtdclient;

import com.melliott.gtdfx.gtdclient.controller.GtdController;
import com.melliott.gtdfx.gtdclient.service.AppServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GtdApplication extends Application {
    private AppServices services;

    @Override
    public void init() {
        this.services = new AppServices();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GtdApplication.class.getResource("gtd.fxml"));
        fxmlLoader.setControllerFactory(param -> new GtdController(services));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("gtdFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}