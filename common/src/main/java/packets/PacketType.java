package packets;

/* Типы классов пакетов
 *
 * @author Aleksei Pronichev
 * @version 23.02.2019
 */

public enum PacketType {
    NONE,                   // пустой пакет
    NO_VALID,               // было послано не валидное сообщение

    AUTHORIZATION,          // Пакет авторизации
    AUTHORIZATION_RESPONSE, // Ответ на попытку залогинится
    NEW_USER,               // новый пользователь

    CLOSE_CONNECTION,       //  закрытие соединения

    MESSAGE,                //  сообщение (от сервера к клиенту)
    MESSAGE_BROADCAST,      //  сообщение для всех (от юзера)
    MESSAGE_PRIVATE,        //  сообщение личное (от юзера к  юзеру)    - не реализовано

    USER_LIST,              // Список пользователей
    FILE_LIST,              // Список файлов                            - не реализовано
    FILE,                   // Работа с файлами                         - не реализовано

    REGISTRY_REQUEST,       // Регистрация пользователя                 - не реализовано
    REGISTRY_RESPONSE,      // Ответ на регистрацию- не реализовано     - не реализовано
}
