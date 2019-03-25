package ru.pronichev.server.service;

/* Сервис обработки клиентов
 *
 * @author Aleksei Pronichev
 * @version 20.02.2019
 */

import io.netty.channel.Channel;
import ru.pronichev.server.Person;
import ru.pronichev.server.serverApi.AuthService;
import ru.pronichev.server.serverApi.ConnectionService;

import java.util.*;

/* Сервис отвечающий за работу с подключениями пользователей
 *
 *@author Aleksei Pronichev
 *@version 25.03.2019
 */

public class PersonService implements ConnectionService {

    private AuthService sqlService;

    private volatile Map<Channel, Person> connections = new HashMap();

    public PersonService(AuthService sqlService) {
        this.sqlService = sqlService;
    }

    private void checkConnections() {
        connections.entrySet().removeIf(e -> (!e.getKey().isActive()));
    }

    @Override
    public String[] getPeople() {
        checkConnections();
        List<String> people = new ArrayList<>();
        for (Map.Entry<Channel, Person> entry : connections.entrySet()) {
            people.add(entry.getValue().getNick());
        }
        return people.toArray(new String[people.size()]);
    }

    @Override
    public Map<Channel, Person> getGeneralMap() {
        checkConnections();
        return connections;
    }

    @Override
    public List<Channel> getConnections() {
        checkConnections();
        return new ArrayList<Channel>(connections.keySet());
    }

    @Override
    public void addConnection(Person person) throws NullPointerException {
        if (person == null || person.getChannel() == null) throw new NullPointerException();
        connections.put(person.getChannel(), person);
    }

    @Override
    public void removeConnection(Person person) throws NullPointerException {
        if (person == null || person.getChannel() == null) throw new NullPointerException();
        connections.remove(person.getChannel());
    }

    @Override
    public void removeConnection(Channel channel) {
        if (channel == null) throw new NullPointerException();
        connections.remove(channel);
    }

    @Override
    public String getPersonName(String login, String password) {
        return sqlService.getNicknameByLoginAndPassword(login, password);
    }

    @Override
    public String getNick(Channel channel) {
        return connections.get(channel).getNick();
    }


}
