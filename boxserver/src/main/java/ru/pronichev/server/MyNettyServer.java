package ru.pronichev.server;

/* Сервер на нетти
 *
 * @author Aleksei Pronichev
 * @version 20.02.2019
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import ru.pronichev.server.serverApi.AuthService;
import ru.pronichev.server.serverApi.ConnectionService;
import ru.pronichev.server.serverApi.ServerInitializer;
import ru.pronichev.server.service.PersonService;
import ru.pronichev.server.service.SQLservice;
import service.FileService;
import service.IOFileService;

public class MyNettyServer {
    private final int port;
    private final int maxObjSize;
    private final FileService fileService;
    private final AuthService sqLservice = new SQLservice();
    private ConnectionService connectionService;


    public MyNettyServer(int port, int maxObjSize, String filesPath) {
        this.port = port;
        this.maxObjSize = maxObjSize;
        this.fileService = new IOFileService(filesPath);
    }

    public MyNettyServer(int port) {
        maxObjSize = 1024 * 1024 * 100;
        this.port = port;
        this.fileService = new IOFileService("serverfiles");
    }

    public void run() throws Exception {
        if (!sqLservice.connect()) return;
        connectionService = new PersonService(sqLservice);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerInitializer(maxObjSize, connectionService, fileService))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
