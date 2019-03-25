package packets;

/* Пакет для ответов с сервера
 *
 * @author Aleksei Pronichev
 * 20.02.2019
 */

public class PacketResult extends PacketMessage {

    private boolean result;

    public PacketResult(String message) {
        super(message);
    }

    public PacketResult() {
        super(null);
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

}
