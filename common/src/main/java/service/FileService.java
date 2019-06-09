package service;

/* Интерфейс для работы c файлами
 *
 * @author Aleksei Pronichev
 * @version 22.02.2019
 */

import packets.files.FileFullPacket;

import java.io.IOException;

public interface FileService {

    public String[] filesList();

    public void add(FileFullPacket packet) throws IOException;

    public void remove(String filename) throws IOException;

    public FileFullPacket copy(String filename) throws IOException;

}
