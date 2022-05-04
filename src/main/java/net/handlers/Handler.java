package net.handlers;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

import client.Client;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import net.ByteBufReader;
import net.Server;
import net.packet.opcode.COutOpcode;

public class Handler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private final Server server;

    public Handler(Server server) {
        this.server = server;
    }
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded of [SERVER]");
        Channel incoming = ctx.channel();
        for (Channel channel : channelGroup) {
            //사용자가 추가되었을 때 기존 사용자에게 알림
            channel.write("[SERVER] - " + incoming.remoteAddress() + "has joined!\n");
        }
        channelGroup.add(incoming);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        var storage = server.getClientStorage();
        storage.registerClient(new Client(ctx.channel()));
        System.out.println("User Access!");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved of [SERVER]");
        Channel incoming = ctx.channel();
        for (Channel channel : channelGroup) {
            //사용자가 나갔을 때 기존 사용자에게 알림
            channel.write("[SERVER] - " + incoming.remoteAddress() + "has left!\n");
        }
        channelGroup.remove(incoming);
        var storage = server.getClientStorage();
        storage.registerClient(storage.getClientFromIP(ctx.channel().remoteAddress()));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        var bbr = new ByteBufReader(Unpooled.wrappedBuffer(msg).order(ByteOrder.LITTLE_ENDIAN));
        var opcodeValue = bbr.readShort();
        var opcode = COutOpcode.getOpcode(opcodeValue);
        var client = server.getClientStorage().getClientFromIP(ctx.channel().remoteAddress());
        System.out.println(opcode +" handled");
        CommonHandler.handle(client, opcode, bbr);
//        System.out.println("channelRead of [SERVER]" +  message);
//        Channel incoming = ctx.channel();
//        for (Channel channel : channelGroup) {
//            if (channel != incoming) {
//                //메시지 전달.
//                //channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + message + "\n");
//            }
//        }

    }
}
