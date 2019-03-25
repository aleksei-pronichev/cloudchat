package ru.pronichev.server.handlers;

/* Последняя проверка, отправка пакетов на обработку
 *
 * @author Aleksei Pronichev
 * @version 25.03.2019
 */

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import packets.Packet;
import packets.PacketMessage;
import packets.files.FileFullPacket;
import packets.files.FilePacket;
import packets.lists.FileListPacket;
import packets.lists.UserListPacket;
import packets.special.NewUserPacket;
import packets.special.NoValidPacket;
import ru.pronichev.server.serverApi.ConnectionService;
import service.FileService;

import java.io.IOException;


public class GeneralHandler extends ChannelInboundHandlerAdapter {
    private ConnectionService connectionService;
    private FileService fileService;

    public GeneralHandler(ConnectionService connectionService, FileService fileService) {
        this.connectionService = connectionService;
        this.fileService = fileService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Packet packet = (Packet) msg;
        switch (packet.getPacketType()) {
            case MESSAGE_BROADCAST:
                toBroadcast(ctx.channel(), (PacketMessage) msg);
                break;
            case FILE: {
                toFile(ctx.channel(), (FilePacket) msg);
                break;
            }
            case NEW_USER: {
                connectNewUser((NewUserPacket) packet);
                break;
            }
            default:
                packet.toString();
                ctx.writeAndFlush(new PacketMessage("Server: неизвестная команда или сообщение"));
        }
    }

    // обработка файлов
    private void toFile(Channel channel, FilePacket packet) throws IOException {
        switch (packet.getCommand()) {
            case FIlE_FULL:
                fileService.add((FileFullPacket) packet);
                toBroadcast(channel, new PacketMessage("добавил новый файл " + packet.getFilename().getName()));
                sendFileListToALL();
                break;
            case FIlE_COPY:
                channel.writeAndFlush(fileService.copy(packet.getFilename().getName()));
                break;
            case FIlE_DELETE:
                fileService.remove(packet.getFilename().getName());
                sendFileListToALL();
                toBroadcast(channel, new PacketMessage("удалил файл " + packet.getFilename().getName()));
                break;
            default:
                channel.writeAndFlush(new PacketMessage("Server: неизвестная команда при обработке файла"));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.writeAndFlush(new NoValidPacket("Critical general problem"));
    }

    // сообщение всем
    private void toBroadcast(Channel channel, PacketMessage packet) {
        String message = packet.getMessage();
        String nick = connectionService.getNick(channel);
        sendALL(new PacketMessage(String.format("%s: %s", nick, message)));
    }

    // рассылка списка файлов
    private void sendFileListToALL() {;
        sendALL(new FileListPacket(fileService.filesList()));
    }

    // новый пользователь
    private void connectNewUser(NewUserPacket packet) {
        sendALL(new UserListPacket(connectionService.getPeople()));
        sendALL(new PacketMessage(String.format("%s: %s", packet.getNick(), "подключился к серверу")));
    }

    // отправка пакета всем
    private void sendALL(Packet packet) {
        for (Channel c : connectionService.getConnections()) {
            c.writeAndFlush(packet);
        }
    }
}
