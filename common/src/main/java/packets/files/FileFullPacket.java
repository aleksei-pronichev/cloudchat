package packets.files;

/* Базовый класс пакета
 *
 * @author Aleksei Pronichev
 * @version 23.02.2019
 */

import java.io.*;

public class FileFullPacket extends FilePacket {

    {
        setCommand(FilePacketType.FIlE_FULL);
    }

    private byte[] bytes;

    public FileFullPacket(File filename) throws IOException {
        super(filename);
        myFile();
    }

    public FileFullPacket(String filename) throws IOException {
        super(filename);
        myFile();
    }

    public FileFullPacket(FilePacket filePacket) throws IOException {
        super(filePacket.getFilename());
        myFile();
    }

    public byte[] getBytes() {
        return bytes;
    }

    private void myFile() throws IOException {
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(getFilename()));
        bytes = new byte[reader.available()];
        reader.read(bytes);
        reader.close();
    }
}