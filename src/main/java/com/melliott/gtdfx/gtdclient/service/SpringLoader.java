package com.melliott.gtdfx.gtdclient.service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SpringLoader {
    private final ApplicationContext context;

    public SpringLoader(ApplicationContext context) {
        this.context = context;
    }

    public Node loadScreen(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlPath));
        loader.setControllerFactory(context::getBean);
        loader.load();
        return loader.getRoot();
    }
}
