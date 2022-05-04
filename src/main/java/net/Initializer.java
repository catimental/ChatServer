package net;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import net.handlers.Handler;

public class Initializer extends ChannelInitializer<SocketChannel> {

    private final Server server;
    public Initializer(Server server) {
        this.server = server;
    }

    @Override
    protected void initChannel(SocketChannel arg0) throws Exception {
        ChannelPipeline pipeline = arg0.pipeline();

        //pipeline.addLast(sslCtx.newHandler(arg0.alloc())); 보안을 강화.
//        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
//        pipeline.addLast(new StringDecoder());
//        pipeline.addLast(new StringEncoder());

        pipeline.addLast(new Handler(server));

    }

}
