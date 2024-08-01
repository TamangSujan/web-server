package http;

import http.request.WebHttpRequest;
import http.request.WebHttpRequestHandler;
import http.response.WebHttpResponse;
import http.response.WebHttpResponseHandler;
import utils.converter.PrimitiveConverter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class HttpWebServer {
    private HttpWebServer(){}

    public static void run(String port) throws IOException {
        int intPort = PrimitiveConverter.getIntFrom(port);
        ServerSocketChannel serverSocketChannel = getServerChannel(intPort);
        System.out.println("Server running on port: "+intPort);
        while(true){
            SocketChannel requester = getRequester(serverSocketChannel);
            new Thread(()->{
                try(Selector selector = Selector.open()){
                    setReadModeForRequester(requester, selector);
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                    while(keys.hasNext()){
                        SelectionKey key = keys.next();
                        if(key.isValid() && key.isReadable()){
                            try {
                                respondToRequester(key);
                            } catch (InvocationTargetException | IllegalAccessException e) {
                                closeRequester(requester);
                                throw new RuntimeException(e);
                            }
                        }
                        keys.remove();
                        }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                closeRequester(requester);
            }).start();
        }
    }

    private static ServerSocketChannel getServerChannel(int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        return serverSocketChannel;
    }

    private static SocketChannel getRequester(ServerSocketChannel serverSocketChannel) throws IOException{
        SocketChannel requester = serverSocketChannel.accept();
        requester.configureBlocking(false);
        return requester;
    }

    private static void setReadModeForRequester(SocketChannel channel, Selector selector) throws IOException {
        channel.register(selector, SelectionKey.OP_READ);
        selector.select();
    }

    private static void respondToRequester(SelectionKey key) throws IOException, InvocationTargetException, IllegalAccessException {
        SocketChannel channel = (SocketChannel) key.channel();
        WebHttpRequest request = WebHttpRequestHandler.getHttpRequest(getDataFromRequester(channel));
        WebHttpResponse response = WebHttpResponseHandler.getHttpResponse(request);
        WebHttpResponseHandler.writeResponseTo(channel, response);
    }

    private static byte[] getDataFromRequester(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        if (channel.read(buffer) == -1) {
            channel.close();
            throw new IOException("Empty data from requester!");
        }
        buffer.flip();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        return data;
    }

    private static void closeRequester(SocketChannel requester){
        try {
            requester.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}