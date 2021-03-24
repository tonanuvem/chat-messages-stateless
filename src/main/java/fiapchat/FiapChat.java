package fiapchat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;

//import fiapchat.MsgConsumer;
//import fiapchat.MsgProducer;

import javax.inject.Inject;
import javax.annotation.Resource;

@ServerEndpoint("/chat/{username}")         
@ApplicationScoped
public class FiapChat {

    Map<String, Session> sessions = new ConcurrentHashMap<>(); 
    //MsgConsumer consumer = new MsgConsumer(this);
    //@Inject MsgConsumer consumer;
    //MsgProducer producer = new MsgProducer();
    @Inject MsgProducer producer;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        System.out.println("Nova conexao: "+session.toString());
        sessions.put(username, session);
        System.out.println("Sessions existentes: "+sessions.values().toString());
        broadcast("Usuário \'" + username + "\' entrou");
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessions.remove(username);
        broadcast("Usuário \'" + username + "\' saiu");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        sessions.remove(username);
        broadcast("Usuário \'" + username + "\' saiu com erro: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {
        broadcast(">> " + username + ": " + message);        
    }

    private void broadcast(String message) {
        producer.sendMsgToKafka(message);
        /* // Codigo movido para o Consumer
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Falha ao enviar a mensagem: " + result.getException());
                }
            });
        });*/
    }

    protected Map<String, Session> getSessions() {
        return sessions;
    }

}
