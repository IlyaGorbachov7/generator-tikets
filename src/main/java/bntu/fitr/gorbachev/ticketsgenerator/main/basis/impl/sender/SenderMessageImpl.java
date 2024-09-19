package bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.sender;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SenderMessageImpl implements SenderMessage {
    List<MessageRetriever> subscribes = new ArrayList<>();
    protected SenderMessageImpl(){
    }
    @Override
    public void add(MessageRetriever sender) {
        subscribes.add(sender);
    }

    @Override
    public void sendMsg(String msg) {
        CompletableFuture.runAsync(()->{
            for (MessageRetriever sender : subscribes) sender.send(msg);
        });
    }
}
