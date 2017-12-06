package com.yyblove.netty;

import com.yyblove.netty.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: yyb
 * @date: 17-12-6
 */
public class EchoServer {

    private static Logger logger = LoggerFactory.getLogger(EchoServer.class);

    /**
     * 端口
     */
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    /**
     * 启动方法
     */
    public void start() throws Exception{
        logger.info("启动EchoServer");
        final EchoServerHandler echoServerHandler = new EchoServerHandler();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(eventLoopGroup)
                    // 指定所使用的NIO传输Channel
                    .channel(NioServerSocketChannel.class)
                    // 使用指定的端口设置套接字地址
                    .localAddress(port)
                    // 添加一个EchoServerHandler到子Channel的ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // EchoServerHandler被标注为@Shareable，所以我们可以总是使用同样的实例
                            pipeline.addLast(echoServerHandler);
                        }
                    });

            // 异步地绑定服务器；调用sync()方法阻塞等待直到绑定完成
            ChannelFuture f = bootstrap.bind().sync();
            // 获取Channel的CloseFuture，并且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();

        } finally {
            // 关闭EventLoopGroup，释放所有的资源
            eventLoopGroup.shutdownGracefully().sync();
        }


    }

    public static void main(String[] args) throws Exception{
        new EchoServer(9999).start();
    }

}
