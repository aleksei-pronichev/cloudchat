package packets.files;

/* Типы классов пакетов
 *
 * @author Aleksei Pronichev
 * @version 23.02.2019
 */

public enum FilePacketType {
    FIlE_FULL,              // Добавление файла на сервер
    FIlE_DELETE,            // Удаление файла с сервера
    FIlE_COPY,              // Копипрование файла c с сервера
    FILE_RENAME             // Переименование файла на сервере
}