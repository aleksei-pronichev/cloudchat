package ru.pronichev.server.serverApi;

/* Инициализация
 *
 * @author Aleksei Pronichev
 * @version 20.02.2019
 */

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import service.FileService;
import ru.pronichev.server.handlers.*;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    private final int maxObjSize;                       // максимальный размер файла для сериализации
    private final ConnectionService connectionService;  // сервис соединений
    private final FileService fileService;

    public ServerInitializer(int maxObjSize, ConnectionService connectionService, FileService fileService) {
        this.maxObjSize = maxObjSize;
        this.connectionService = connectionService;
        this.fileService = fileService;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        //выходные хендлеры
        pipeline.addLast("encoder", new ObjectEncoder());
        pipeline.addLast("checkOut", new OutValidHandler());

        //входные хендлеры
        pipeline.addLast("decoder", new ObjectDecoder(maxObjSize, ClassResolvers.cacheDisabled(null)));
        pipeline.addLast("checkPacket", new CheckPackageHandler());
        pipeline.addLast("checkTypePacket", new CheckPackageTypeHandler());
        pipeline.addLast("authorization", new AuthorizationHandler(connectionService, fileService));
        pipeline.addLast("authorizationCheck", new AuthorizationCheckHandler(connectionService));
        pipeline.addLast("general", new GeneralHandler(connectionService, fileService));
    }
}
