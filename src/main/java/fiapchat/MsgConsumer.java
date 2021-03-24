package fiapchat;

//import io.smallrye.reactive.messaging.kafka.IncomingKafkaRecord;
import org.eclipse.microprofile.reactive.messaging.Incoming;
//import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

//import fiapchat.FiapChat;
import io.smallrye.reactive.messaging.annotations.Merge;

import javax.inject.Inject;

@ApplicationScoped
public class MsgConsumer {

    @Inject private FiapChat chat;

    //private final Logger logger = Logger.getLogger(MsgConsumer.class);
    //MsgConsumer(FiapChat fiapchat) {
    //    this.chat = fiapchat;
    //}

    //@Inject @Channel("mensagens") Multi<String> streamOfPayloads;
    //public void receive(IncomingKafkaRecord<String> msg) {
    @Incoming("inmensagens")
    @Merge()
    public void receive(String msg) {
        //logger.infof("Mensagem: %d - %s", record.key(), record.value());
        System.out.println("Recebido do Kafka: "+msg);
        broadcast(msg);
    }

    private void broadcast(String message) {
        System.out.println("Sessions existentes: "+chat.getSessions().values().toString());
        chat.getSessions().values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Falha ao enviar a mensagem: " + result.getException());
                }
            });
        });
    }
}

