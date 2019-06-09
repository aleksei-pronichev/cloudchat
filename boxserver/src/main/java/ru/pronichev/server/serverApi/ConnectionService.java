package ru.pronichev.server.serverApi;

/* Интерфейс для работы с подключениями пользователей
 *
 * @author Aleksei Pronichev
 * @version 20.02.2019
 */

import io.netty.channel.Channel;
import ru.pronichev.server.Person;

import java.util.List;
import java.util.Map;

public interface ConnectionService {

    Map<Channel, Person> getGeneralMap();

    List<Channel> getConnections();

    String[] getPeople();

    void addConnection(Person person);

    void removeConnection(Person person);

    void removeConnection(Channel channel);

    String getPersonName(String login, String password);

    String getNick(Channel channel);
}
