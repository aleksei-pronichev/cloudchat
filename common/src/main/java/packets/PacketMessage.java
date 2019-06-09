package packets;

/* Базовый класс пакета
 *
 * @author Aleksei Pronichev
 * @version 18.02.2019
 */

public class PacketMessage extends Packet {
    private String message;

    public PacketMessage(String message) {
        this.message = message;
        setPacketType(PacketType.MESSAGE);
    }

    public PacketMessage(String message, PacketType type) {
        this.message = message;
        setPacketType(type);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public java.lang.String toString() {
        return getMessage();
    }
}