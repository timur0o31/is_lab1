package app.websocket;

import javax.json.Json;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/webSocket")
public class WebSocket {
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }
    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }
    public static void broadcast(String type) {
        String message = Json.createObjectBuilder()
                .add("type", type)
                .build()
                .toString();
        synchronized (sessions) {
            sessions.forEach(session -> {
            try{
                session.getAsyncRemote().sendText(message);
            }catch( Exception e){
                e.printStackTrace();
        }});
        }
    }
}
