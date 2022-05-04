package client;

import java.net.Socket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.net.SocketAddress;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class Client {

    private Channel channel;
    private String accountId;
    private Profile profile;

    public Client(Channel channel) {
        this.channel = channel;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    @Override
    public String toString() { //uid is id
        return channel.remoteAddress().toString();
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

    //todo ChannelFuture
    public void writeAndFlush(byte[] packet) throws InterruptedException {
        ByteBuf writeBuf = Unpooled.wrappedBuffer(packet).order(ByteOrder.LITTLE_ENDIAN);
        ChannelFuture lastWriteFuture = channel.writeAndFlush(
                writeBuf);
        if(lastWriteFuture != null) {
            lastWriteFuture.sync();
        }
    }
}
