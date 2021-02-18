package websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

public class MyWebSocketClient extends WebSocketClient{

    public MyWebSocketClient(String url) throws URISyntaxException {
        super(new URI(url));
    }

    @Override
    public void onOpen(ServerHandshake shake) {
        System.out.println("握手...");
        for(Iterator<String> it = shake.iterateHttpFields(); it.hasNext();) {
            String key = it.next();
            System.out.println(key+":"+shake.getFieldValue(key));
        }
    }

    @Override
    public void onMessage(String paramString) {
        System.out.println("接收到消息："+paramString);
    }

    @Override
    public void onClose(int paramInt, String paramString, boolean paramBoolean) {
        System.out.println("关闭...");
    }

    @Override
    public void onError(Exception e) {
        System.out.println("异常"+e);

    }

    public static void main(String[] args) throws InterruptedException {
        try {
            //e.com/ws/" + String(listenKey))
            String url="wss://fstream.binance.com"+"/ws/SkYbN6QyQbUPuhmjj6fHzXsrRtR8311LRTslWRB33IppcLd70d5Vi8eGjAEFVTbt";
            MyWebSocketClient client = new MyWebSocketClient(url);
            client.connect();
            while (!client.getReadyState().equals(ReadyState.OPEN)) {
             //   System.out.println("还没有打开"+client.getReadyState());
            }
            client.send("{\n" +
                    "\"method\": \"SUBSCRIBE\",\n" +
                    "\"params\":\n" +
                    "[\n" +
                    "\"ethusdt@kline_1m\",\n" +
                    "\"ethusdt@miniTicker\"\n" +
                    "],\n" +
                    "\"id\": 1\n" +
                    "}");

            System.out.println("建立websocket连接");
Thread.sleep(10000);
client.close();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Thread.sleep(10000);
    }


}