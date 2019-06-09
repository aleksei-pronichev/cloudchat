package ru.pronichev.server;
/* пользователь
 *
 * @author Aleksei Pronichev
 * @version 19.02.2019
 */

import io.netty.channel.Channel;

public class Person {

    private final String login;
    private final String password;
    private final Channel channel;
    private String nick;

    public Person(String login, String password, String nick, Channel channel) {
        this.login = login;
        this.password = password;
        this.nick = nick;
        this.channel = channel;
    }

    public Person(String login, String password, Channel channel) {
        this.login = login;
        this.password = password;
        this.nick = nick;
        this.channel = channel;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public String toString() {
        return String.format("%s: ", (nick == null ? login : nick));
    }
}
