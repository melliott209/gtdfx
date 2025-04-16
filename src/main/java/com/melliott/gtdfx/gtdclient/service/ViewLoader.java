package com.melliott.gtdfx.gtdclient.service;

import com.melliott.gtdfx.gtdclient.controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class ViewLoader {
    private final AppServices services;

    public ViewLoader(AppServices appServices) {
        this.services = appServices;
    }

    public Node load(String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        fxmlLoader.setControllerFactory(param -> {
            if (param == InboxController.class) return new InboxController(services);
            else if (param == CompleteController.class) return new CompleteController(services);
            else if (param == NextController.class) return new NextController(services);
            else if (param == GtdController.class) return new GtdController(services);
            else if (param == WaitingController.class) return new WaitingController(services);
            else if (param == SomedayController.class) return new SomedayController(services);
//            else if (param == ProjectsController.class) return new ProjectsController(services);

            throw new IllegalArgumentException("Unknown controller type: " + param);
        });
        return fxmlLoader.load();
    }
}
