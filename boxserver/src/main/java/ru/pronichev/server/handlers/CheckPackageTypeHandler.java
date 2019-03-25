package ru.pronichev.server.handlers;

/* Контрольная проверка на соответвие типа входящего пакета
 *
 * @author Aleksei Pronichev
 * @version 20.02.2019
 */

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import packets.Packet;
import packets.PacketResult;
import packets.PacketType;
import packets.special.NoValidPacket;

public class CheckPackageTypeHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
        if (((Packet) packet).getPacketType() == PacketType.NONE || packet instanceof PacketResult) {
            noValid(ctx);
        } else {
            ctx.fireChannelRead(packet);
        }
    }

    // если клиент будет нам отсылат невалидные сообщения мы его выкидываем
    private void noValid(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(new NoValidPacket("Вы были отключены за нарушения правил сервиса"));
    }

    // возникновение исключения в данном контексте приравнивается к невалидному сообщению
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        noValid(ctx);
    }
}
