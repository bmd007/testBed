package ir.tiroon.android.stomp.client.websocket;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Base64;
import java.util.function.Consumer;
import java.util.logging.Logger;

import okhttp3.OkHttpClient;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

public class StompEchoService {

    private Consumer<String> onMessageListener;
    private StompClient mStompClient;
    public  OkHttpClient okClient;
    private String ip;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public StompEchoService(String ip, String userName, String password, String queueName, Consumer<String> onMessageListener) {
        this.onMessageListener = onMessageListener;

        String userPass = Base64.getEncoder().encodeToString(new StringBuilder().append(userName).append(":").append(password).toString().getBytes());
        okClient = new OkHttpClient.Builder().authenticator((route, response) -> response.request().newBuilder()
                .header("Authorization", "Basic "+ userPass).build()).build();
        this.ip = ip;
        String url = "ws://" + ip + ":8080/echoStopmEndpoint/websocket";
//        Map<String, String> headerMap = singletonMap("Authorization", "Basic "+ userPass); this maybe needed when using auth in brokers like activeMQ
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url, null, okClient);
        mStompClient.connect();


        mStompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED: {
                    Logger.getGlobal().info("Stomp connection opened");
                    mStompClient.topic(queueName).map(StompMessage::getPayload).subscribe(onMessageListener::accept);
                    sendText("Hello", "/app/message");
                    break;
                }
                case ERROR: {
                    Logger.getGlobal().severe(lifecycleEvent.getException().getMessage());
                    break;
                }
                case CLOSED: {
                    Logger.getGlobal().info("Stomp connection closed");
                    break;
                }
            }
        });
    }

    public void sendText(String message, String destination) {
        mStompClient.send(destination, message).subscribe();
    }

    public void disconnect() {
        mStompClient.disconnect();
    }

}
