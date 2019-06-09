package packets.special;
/* Пакет не валидного сообщения
 *
 * @author Aleksei Pronichev
 * 20.02.2019
 */

import packets.Packet;
import packets.PacketType;

public class NewUserPacket extends Packet {
    {
        setPacketType(PacketType.NEW_USER);
    }
    private String nick;

    public NewUserPacket(String nick) {
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }
}