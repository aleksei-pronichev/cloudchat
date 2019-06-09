package packets;

/* Базовый класс пакета
 *
 * @author Aleksei Pronichev
 * @version 18.02.2019
 */

import java.io.Serializable;


public class Packet implements Serializable {

    private PacketType packetType = PacketType.NONE;

    public PacketType getPacketType() {
        return packetType;
    }

    public void setPacketType(PacketType packetType) {
        this.packetType = packetType;
    }
}