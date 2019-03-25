package ru.pronichev.server.service;

/* Сервис отвечающий за подключение к БД
 *
 *@author Aleksei Pronichev
 *@version 20.02.2019
 */

import ru.pronichev.server.serverApi.AuthService;

import java.sql.*;

public class SQLservice implements AuthService {
    private static Connection connection;
    private static PreparedStatement psGetNickByLoginAndPassword;
    private static PreparedStatement psChangeNick;

    public boolean connect() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/chat", "sa", "123");
            psChangeNick = connection.prepareStatement("UPDATE PERSONS SET nickname = ? WHERE nickname = ?;");
            psGetNickByLoginAndPassword = connection.prepareStatement("SELECT nickname FROM PERSONS WHERE login = ? AND password = ?;");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getNicknameByLoginAndPassword(String login, String password) {
        String nick = null;
        try {
            psGetNickByLoginAndPassword.setString(1, login);
            psGetNickByLoginAndPassword.setString(2, password);
            ResultSet rs = psGetNickByLoginAndPassword.executeQuery();
            if (rs.next()) {
                nick = rs.getString(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nick;
    }

    public boolean changeNick(String oldNickname, String newNickname) {
        try {
            psChangeNick.setString(1, newNickname);
            psChangeNick.setString(2, oldNickname);
            psChangeNick.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static void disconnect() {
        try {
            psGetNickByLoginAndPassword.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            psChangeNick.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
