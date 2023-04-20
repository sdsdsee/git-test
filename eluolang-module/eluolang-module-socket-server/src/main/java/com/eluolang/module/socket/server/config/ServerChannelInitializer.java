package com.eluolang.module.socket.server.config;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author renzhixing
 *
 * netty服务初始化器
 **/
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //添加解码器
        socketChannel.pipeline().addLast(new NewDeviceDecoder());
        socketChannel.pipeline().addLast(new NewDeviceEncoder());
        ///socketChannel.pipeline().addLast("decoder", new StringDecoder(Charset.forName(Constants.GBK)));
        //socketChannel.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
        socketChannel.pipeline().addLast(new IdleStateHandler(180,0,0, TimeUnit.SECONDS));
        socketChannel.pipeline().addLast(new NettyServerHandler());
    }
}