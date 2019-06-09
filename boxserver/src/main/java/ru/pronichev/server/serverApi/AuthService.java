package ru.pronichev.server.serverApi;

public interface AuthService {
    String getNicknameByLoginAndPassword(String login, String password);

    boolean changeNick(String oldNickname, String newNickname);

    public boolean connect();
}