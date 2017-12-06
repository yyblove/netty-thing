package com.yyblove.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: yyb
 * @date: 17-12-6
 */
// Sharable标记该类的实例可以被多个Channel共享
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static Logger logger = LoggerFactory.getLogger(EchoClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        // 记录已接收消息的转储
        logger.info("Client received: {}", msg.toString(CharsetUtil.UTF_8));
        ctx.write(Unpooled.copiedBuffer("Netty rocks! Hello World!!!", CharsetUtil.UTF_8));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当被通知Channel是活跃的时候，发送一条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks! Hello World!!!", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 在发生异常时，记录错误并关闭Channel
        logger.info("EchoClientHandler 异常", cause);
        // 关闭该Channel
        ctx.close();
    }
}
