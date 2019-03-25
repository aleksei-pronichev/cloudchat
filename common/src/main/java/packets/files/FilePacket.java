package packets.files;

/* Базовый класс пакета
 *
 * @author Aleksei Pronichev
 * @version 18.02.2019
 */

import packets.Packet;
import packets.PacketType;

import java.io.File;

public class FilePacket extends Packet {

    {
        setPacketType(PacketType.FILE);
    }

    private File filename;
    private FilePacketType command;

    public FilePacket(File filename, FilePacketType command) {
        this.filename = filename;
        this.command = command;
    }

    public FilePacket(String filename, FilePacketType command) {
        this.filename = new File(filename);
        this.command = command;
    }

    public void setFilename(File filename) {
        this.filename = filename;
    }

    public FilePacket(String filename) {
        this.filename = new File(filename);
    }

    public FilePacket(File filename) {
        this.filename = filename;
    }

    public void setCommand(FilePacketType command) {
        this.command = command;
    }

    public File getFilename() {
        return filename;
    }

    public FilePacketType getCommand() {
        return command;
    }
}