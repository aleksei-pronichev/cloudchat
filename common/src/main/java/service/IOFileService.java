package service;
/* Контроллер за работу с файлами
 *
 * @author Aleksei Pronichev
 * 23.02.2019
 */

import packets.files.FileFullPacket;

import java.io.*;
import java.util.Vector;

public class IOFileService implements FileService {
    private File filesPath;
    private Vector<File> files;

    public IOFileService(String filesPath) {
        files = new Vector<>();
        this.filesPath = new File(filesPath);
        initFiles();
    }

    public String[] filesList() {
        return filesPath.list();
    }

    @Override
    public synchronized void add(FileFullPacket packet) throws IOException {
        File file = new File(filesPath + "\\" + packet.getFilename().getName());
        BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file));
        writer.write(packet.getBytes());
        writer.close();
    }

    @Override
    public synchronized void remove(String filename) throws FileNotFoundException {
        File file = searchFile(filename);
        if (file.delete())
            files.remove(file);
        else
            throw new FileNotFoundException("При удалении что-то пошло не так");
    }

    @Override
    public FileFullPacket copy(String filename) throws IOException {
        filename = filesPath + "\\" + filename;
        return new FileFullPacket(filename);
    }

    // инициализация файлов при запуске
    private void initFiles() {
        for (File f : filesPath.listFiles()) {
            files.add(f);
        }
    }

    private File searchFile(String file) throws FileNotFoundException {
        for (File f : files) {
            if (file.equals(f.getName())) return f;
        }
        throw new FileNotFoundException("Файл не найден");
    }
}
