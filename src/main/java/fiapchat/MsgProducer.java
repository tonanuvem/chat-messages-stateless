package fiapchat;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import io.smallrye.reactive.messaging.annotations.Broadcast;
//import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.messaging.Message;
//import io.smallrye.mutiny.Multi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MsgProducer {

    @Inject @Channel("inmensagens") Emitter<String> emitter;
    @Broadcast
    public void sendMsgToKafka(String msg) {
        System.out.println("Enviando para o Kafka: "+msg);
        emitter.send(Message.of(msg));
    }
/*
    @Outgoing("mensagens")
    @Broadcast
    public Message<String> sendMsgToKafka(String msg) {
        return Message.of("teste");//msg;
    }
*/
}
