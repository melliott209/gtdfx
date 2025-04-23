package com.melliott.gtdfx.gtdclient.util;

import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;

public class UIUtil {

    private UIUtil() {
    }

    public static void clearSelectionOnEmptyRowClick(TableView<?> table) {
        table.setOnMouseClicked(event -> {
            Node selectedNode = event.getPickResult().getIntersectedNode();
            while (selectedNode != null && !(selectedNode instanceof TableCell) && !(selectedNode instanceof TableView)) {
                selectedNode = selectedNode.getParent();
            }
            if (selectedNode instanceof TableView || selectedNode != null && ((TableCell<?, ?>) selectedNode).getTableRow().isEmpty()) {
                table.getSelectionModel().clearSelection();
            }
        });
    }
}
