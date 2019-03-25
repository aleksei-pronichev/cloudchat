package ru.pronichev.boxclient;
/* Контроллер клиента
 *
 * @author Aleksei Pronichev
 * @version 25.03.2019
 */

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import packets.Packet;
import packets.PacketMessage;
import packets.files.FileFullPacket;
import packets.files.FilePacket;
import packets.files.FilePacketType;
import packets.lists.FileListPacket;
import packets.special.AuthorizationPacketResponse;
import packets.lists.UserListPacket;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static ru.pronichev.boxclient.MainClientBox.*;

public class Controller implements Initializable {

    @FXML
    TextArea textArea;

    @FXML
    TextField msgField;

    @FXML
    HBox msgPanel;

    @FXML
    ListView<String> clientsList, serverFilesList, filesList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        runLaterList(filesList, fileService.filesList());
        initFileMenu();
        initFileServerMenu();
        linkCallbacks();
    }

    public void sendMsg() {
        if (msgField.getText().length() < 1) return;
        if (net.sendMsg(msgField.getText())) {
            msgField.clear();
            msgField.requestFocus();
        }
    }

    public void showAlert(String msg, boolean close) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
            alert.showAndWait();
            if (close) System.exit(0);
        });
    }

    public void linkCallbacks() {
        net.setCallOnException(packet -> {
            showAlert(packet.toString(), true);
        });

        net.setCallOnMsgReceived((Packet packet) -> {
            switch (packet.getPacketType()) {
                case AUTHORIZATION_RESPONSE:
                    checkAuthorization((AuthorizationPacketResponse) packet);
                    break;
                case USER_LIST: {
                    toUsers((UserListPacket) packet);
                    break;
                }
                case FILE_LIST: {
                    toFiles((FileListPacket) packet);
                    break;
                }
                case MESSAGE: {
                    toMessage((PacketMessage) packet);
                    break;
                }
                case FILE: {
                    toFile((FilePacket) packet);
                    break;
                }
            }
        });
    }

    private void checkAuthorization(AuthorizationPacketResponse packet) {
        if (packet.isResult()) {
            Platform.runLater(() -> {
                paneController.activate("chat");
            });
        } else {
            showAlert(packet.getMessage(), false);
        }
    }

   private void toUsers(UserListPacket packet) {
        runLaterList(clientsList, packet.getUsertlist());
    }

    private void toFiles(FileListPacket packet) {
        runLaterList(serverFilesList, packet.getFilelist());
    }

    private void toMessage(PacketMessage packet) {
        textArea.appendText(packet + "\n");
    }

    private void toFile(FilePacket packet) {
        if (packet.getCommand() == FilePacketType.FIlE_FULL) {
            try {
                fileService.add((FileFullPacket) packet);
                runLaterList(filesList, fileService.filesList());
            } catch (IOException e) {
                showAlert("При копировании файла с сервера возникла проблема", false);
            }
        }
    }

    private void runLaterList(ListView<String> list, String[] tokens) {
        Platform.runLater(() -> {
            list.getItems().clear();
            for (int i = 0; i < tokens.length; i++) {
                list.getItems().add(tokens[i]);
            }
        });
    }

    private void initFileMenu() {
        ContextMenu myFileMenu = new ContextMenu();

        MenuItem copyItem = new MenuItem("Copy on Server");
        MenuItem deleteItem = new MenuItem("delete");

        copyItem.setOnAction(event -> net.sendFile(filesList.getSelectionModel().getSelectedItem(), fileService));
        deleteItem.setOnAction(event -> {
            try {
                fileService.remove(filesList.getSelectionModel().getSelectedItem());
                runLaterList(filesList, fileService.filesList());

            } catch (IOException e) {
               showAlert("Невозможно удалить файл", false);
            }
        });

        myFileMenu.getItems().add(copyItem);
        myFileMenu.getItems().add(deleteItem);

        filesList.setContextMenu(myFileMenu);
    }

    private void initFileServerMenu() {
        ContextMenu myFileServerMenu = new ContextMenu();

        MenuItem copyServerItem = new MenuItem("Copy");
        MenuItem deleteServerItem = new MenuItem("delete from Server");

        copyServerItem.setOnAction(event -> net.copyServerFile(serverFilesList.getSelectionModel().getSelectedItem()));
        deleteServerItem.setOnAction(event -> net.deleteServerFile(serverFilesList.getSelectionModel().getSelectedItem()));

        myFileServerMenu.getItems().add(copyServerItem);
        myFileServerMenu.getItems().add(deleteServerItem);

        serverFilesList.setContextMenu(myFileServerMenu);
    }
}