package packets.lists;

/* Базовый класс пакета
 *
 * @author Aleksei Pronichev
 * @version 18.02.2019
 */

import packets.Packet;
import packets.PacketType;

public class UserListPacket extends Packet {

    {
        setPacketType(PacketType.USER_LIST);
    }

    private String[] usertlist;

    public UserListPacket(String[] usertlist) {
        this.usertlist = usertlist;
    }

    public String[] getUsertlist() {
        return usertlist;
    }
}