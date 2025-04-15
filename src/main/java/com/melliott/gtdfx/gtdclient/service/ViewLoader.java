package com.melliott.gtdfx.gtdclient.service;

import com.melliott.gtdfx.gtdclient.controller.CompleteController;
import com.melliott.gtdfx.gtdclient.controller.GtdController;
import com.melliott.gtdfx.gtdclient.controller.InboxController;
import com.melliott.gtdfx.gtdclient.controller.NextController;
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

            throw new IllegalArgumentException("Unknown controller type: " + param);
        });
        return fxmlLoader.load();
    }
}
