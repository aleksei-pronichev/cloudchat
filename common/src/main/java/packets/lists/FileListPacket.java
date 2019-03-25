package packets.lists;

/* Базовый класс пакета
 *
 * @author Aleksei Pronichev
 * @version 18.02.2019
 */

import packets.Packet;
import packets.PacketType;

public class FileListPacket extends Packet {

    {
        setPacketType(PacketType.FILE_LIST);
    }

    private String[] filelist;

    public FileListPacket(String[] filelist) {
        this.filelist = filelist;
    }

    public String[] getFilelist() {
        return filelist;
    }
}