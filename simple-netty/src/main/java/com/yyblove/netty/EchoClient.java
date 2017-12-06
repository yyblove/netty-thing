package com.yyblove.netty;

import com.yyblove.netty.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author: yyb
 * @date: 17-12-6
 */
public class EchoClient {

    private static Logger logger = LoggerFactory.getLogger(EchoClient.class);

    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        logger.info("启动EchoClient");
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            // 指定EventLoopGroup以处理客户端事件；需要适用于NIO的实现
            Bootstrap b = new Bootstrap();
            b.group(eventLoopGroup)
                    // 指定EventLoopGroup以处理客户端事件；需要适用于NIO的实现
                    // 这里是 NioSocketChannel 跟 Sever的不一样
                    .channel(NioSocketChannel.class)
                    // 设置服务器的InetSocketAddress
                    .remoteAddress(new InetSocketAddress(host, port))
                    // 在创建Channel时，向ChannelPipeline中添加一个EchoClientHandler实例
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            // 连接到远程节点，阻塞等待直到连接完成
            ChannelFuture f = b.connect().sync();
            // 阻塞，直到Channel关闭
            f.channel().closeFuture().sync();

        }finally {
            eventLoopGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception{
        new EchoClient("127.0.0.1", 9999).start();
    }
}
