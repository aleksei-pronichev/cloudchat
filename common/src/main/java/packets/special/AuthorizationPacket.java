package packets.special;
/* Пакет авторизации
 *
 * @author Aleksei Pronichev
 * 20.02.2019
 */

import packets.Packet;
import packets.PacketType;

public class AuthorizationPacket extends Packet {
    {
        setPacketType(PacketType.AUTHORIZATION);
    }

    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public AuthorizationPacket(String login, String password) {
        this.login = login;
        this.password = password;
    }
}