package ru.pronichev.server.handlers;
/* Авторизация
 *
 * @author Aleksei Pronichev
 * @version 19.02.2019
 */

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import packets.Packet;
import packets.special.AuthorizationPacketResponse;
import packets.special.NoValidPacket;
import ru.pronichev.server.serverApi.ConnectionService;

public class AuthorizationCheckHandler extends ChannelInboundHandlerAdapter {
    private ConnectionService connectionService;

    public AuthorizationCheckHandler(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    public void channelRead(ChannelHandlerContext ctx, Object pak) throws Exception {
        Packet packet = (Packet) pak;

        if (connectionService.getConnections().contains(ctx.channel())) {
            ctx.fireChannelRead(packet);
        } else {
            sendResponse(ctx, new AuthorizationPacketResponse(false, "Вы не авторизованы"));
        }
    }

    //ответ на авторизацию
    private void sendResponse(ChannelHandlerContext ctx, Packet packet) {
        ctx.writeAndFlush(packet);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        sendResponse(ctx, new NoValidPacket("CriticalError with Authorization"));
    }
}
