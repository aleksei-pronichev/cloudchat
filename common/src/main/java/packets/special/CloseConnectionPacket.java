package packets.special;
/* Пакет не валидного сообщения
 *
 * @author Aleksei Pronichev
 * 20.02.2019
 */

import packets.Packet;
import packets.PacketType;

public class CloseConnectionPacket extends Packet {
    {
        setPacketType(PacketType.CLOSE_CONNECTION);
    }
}