package ru.pronichev.boxclient.visual;
/* Управление панелями
 *
 * @author Aleksei Pronichev
 * 23.03.2019
 */

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;

public class PaneController {
    private HashMap<String, Parent> panesMap = new HashMap<>();
    private Stage stage;

    public PaneController() {
        stage = new Stage();
    }

    public void addPane(String name, Pane pane){
        panesMap.put(name, pane);
    }

    public void removePane(String name){
        panesMap.remove(name);
    }

    public void activate(String name){
        stage.setScene(new Scene(panesMap.get(name)));
        stage.show();
    }
}
