package packets.special;

/* Базовый класс пакета
 *
 * @author Aleksei Pronichev
 * @version 18.02.2019
 */

import packets.Packet;
import packets.PacketType;

import java.io.Serializable;


public class UserListPacketResponse extends Packet {

    {
        setPacketType(PacketType.USER_LIST);
    }

    private String[] usertlist;

    public UserListPacketResponse(String[] usertlist) {
        this.usertlist = usertlist;
    }

    public String[] getUsertlist() {
        return usertlist;
    }
}