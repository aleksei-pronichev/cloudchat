package ru.pronichev.boxclient;
/* Класс запуска клиента
 *
 * @author Aleksei Pronichev
 * @version 24.03.2019
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ru.pronichev.boxclient.visual.PaneController;
import service.FileService;
import service.IOFileService;

public class MainClientBox extends Application {
    public static final PaneController paneController = new PaneController();
    public static final ConnectionAdapter net  =  new ConnectionAdapter("localhost", 8189);
    public static final FileService fileService = new IOFileService("clientfiles");

    @Override
    public void start(Stage primaryStage) throws Exception{
        paneController.addPane("login", FXMLLoader.load(getClass().getResource("/login.fxml")));
        paneController.addPane("chat", FXMLLoader.load(getClass().getResource("/box.fxml")));
        paneController.activate("login");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
