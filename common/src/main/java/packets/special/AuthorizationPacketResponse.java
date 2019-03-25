package packets.special;
/* Пакет авторизации
 *
 * @author Aleksei Pronichev
 * 20.02.2019
 */

import packets.PacketResult;
import packets.PacketType;

public class AuthorizationPacketResponse extends PacketResult {
    {
        setPacketType(PacketType.AUTHORIZATION_RESPONSE);
    }

    private String nick;

    public AuthorizationPacketResponse(boolean result, String message) {
        setResult(result);
        setMessage(message);
    }

    public AuthorizationPacketResponse(boolean result, String message, String nick) {
        setResult(result);
        setMessage(message);
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }
}