package ru.pronichev.server;

/* Класс запуса сервера
 *
 * @author Aleksei Pronichev
 * @version 20.02.2019
 */
public class MainServer {
    public static void main(String[] args) {
        try {
            final int port = 8189;  // порт
            final int maxObjSize = 1024 * 1024 * 100; // 100 mb максимальный размер файла
            final String filesPath = "serverfiles";
            new MyNettyServer(port, maxObjSize, filesPath).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
