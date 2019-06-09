package packets.special;
/* Пакет не валидного сообщения
 *
 * @author Aleksei Pronichev
 * 20.02.2019
 */

import packets.PacketResult;
import packets.PacketType;

public class NoValidPacket extends PacketResult {
    {
        setPacketType(PacketType.NO_VALID);
        setResult(false);
    }

    public NoValidPacket(String message) {
        setMessage(message);
    }
}