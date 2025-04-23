package com.melliott.gtdfx.gtdclient.ui;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class ContextMenuBuilder {
    private final ContextMenu menu = new ContextMenu();

    public ContextMenuBuilder item(String text, Runnable action) {
        MenuItem item = new MenuItem(text);
        item.setOnAction(event -> action.run());
        menu.getItems().add(item);
        return this;
    }

    public ContextMenuBuilder submenu(Menu submenu) {
        menu.getItems().add(submenu);
        return this;
    }

    public ContextMenu build() {
        return menu;
    }

    public static class MenuBuilder {
        private final Menu menu;

        public MenuBuilder(String name) {
            menu = new Menu(name);
        }

        public MenuBuilder item(String text, Runnable action) {
            MenuItem item = new MenuItem(text);
            item.setOnAction(event -> action.run());
            menu.getItems().add(item);
            return this;
        }

        public Menu build() {
            return menu;
        }
    }
}
