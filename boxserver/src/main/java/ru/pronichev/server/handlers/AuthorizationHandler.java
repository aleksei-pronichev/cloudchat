package ru.pronichev.server.handlers;

/* Авторизация
 *
 * @author Aleksei Pronichev
 * @version 22.02.2019
 */

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import packets.Packet;
import packets.lists.FileListPacket;
import packets.special.AuthorizationPacket;
import packets.special.AuthorizationPacketResponse;
import packets.special.NewUserPacket;
import packets.special.NoValidPacket;
import packets.lists.UserListPacket;
import ru.pronichev.server.Person;
import ru.pronichev.server.serverApi.ConnectionService;
import service.FileService;

import static packets.PacketType.AUTHORIZATION;
import static packets.PacketType.CLOSE_CONNECTION;

public class AuthorizationHandler extends ChannelInboundHandlerAdapter {
    private ConnectionService connectionService;
    private FileService fileService;

    public AuthorizationHandler(ConnectionService connectionService, FileService fileService) {
        this.connectionService = connectionService;
        this.fileService = fileService;
    }

    public void channelRead(ChannelHandlerContext ctx, Object pak) throws Exception {
        Packet packet = (Packet) pak;

        if (packet.getPacketType() == CLOSE_CONNECTION) {
            closeConnection(ctx);
        } else if (packet.getPacketType() == AUTHORIZATION) {
            toAuthorization(ctx, (AuthorizationPacket) packet);
        } else {
            ctx.fireChannelRead(packet);
        }
    }

    //авторизация пользователя
    private void toAuthorization(ChannelHandlerContext ctx, AuthorizationPacket packet) {
        String nick = connectionService.getPersonName(packet.getLogin(), packet.getPassword());
        if (nick != null) {
            System.out.println("Выполнена авторизация пользователя под ником :" + nick);
            sendResponse(ctx, new AuthorizationPacketResponse(true, "Server: Тебе удалось вспомнить свой пароль, " + nick, nick));
            Person person = new Person(packet.getLogin(), packet.getPassword(), nick, ctx.channel());
            connectionService.addConnection(person);
            sendResponse(ctx, new UserListPacket(connectionService.getPeople())); //отправляем список пользователей
            sendResponse(ctx, new FileListPacket(fileService.filesList()));       //отправляем список файлов
            ctx.fireChannelRead(new NewUserPacket(nick));
        } else {
            sendResponse(ctx, new AuthorizationPacketResponse(false, "Пользователь не найден"));
        }
    }

    //ответ на авторизацию
    private void sendResponse(ChannelHandlerContext ctx, Packet packet) {
        ctx.writeAndFlush(packet);
    }

    //закрытие соединения
    private void closeConnection(ChannelHandlerContext ctx) {
        connectionService.removeConnection(ctx.channel());
        sendResponse(ctx, new NoValidPacket("Пользователь отключился"));
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        sendResponse(ctx, new NoValidPacket("Критическая ошибка при авторизации"));
    }
}
