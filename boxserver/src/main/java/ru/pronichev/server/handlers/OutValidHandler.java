package ru.pronichev.server.handlers;

/* Проверка на валидацию
 * Плохая валидация - закрытие соединения
 *
 * @author Aleksei Pronichev
 * @version 20.02.2019
 */

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import packets.Packet;
import packets.PacketType;
import packets.special.CloseConnectionPacket;
import packets.special.NoValidPacket;

public class OutValidHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object pac, ChannelPromise promise) throws Exception {
        Packet packet = (Packet) pac;

        if (packet.getPacketType() == PacketType.NO_VALID) {
            closeConnection(ctx, (NoValidPacket) packet);
        } else {
            ctx.writeAndFlush(packet);
        }
    }

    private void closeConnection(ChannelHandlerContext ctx, NoValidPacket packet) {
        ctx.writeAndFlush(packet);
        ctx.writeAndFlush(new CloseConnectionPacket());
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        closeConnection(ctx, new NoValidPacket("Critical Error with Respose"));
    }
}
