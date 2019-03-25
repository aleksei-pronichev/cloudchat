package ru.pronichev.boxclient;
/* Контроллер клиента
 *
 * @author Aleksei Pronichev
 * @version 25.03.2019
 */

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import static ru.pronichev.boxclient.MainClientBox.net;


public class LoginWindowController {
    @FXML
    VBox mainVBox;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passField;

    public void sendAuth() {
        net.sendAuth(loginField.getText(), passField.getText());
        loginField.clear();
        passField.clear();
    }
}
