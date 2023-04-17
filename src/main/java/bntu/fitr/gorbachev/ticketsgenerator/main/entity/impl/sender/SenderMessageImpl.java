package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.sender;

import java.util.ArrayList;
import java.util.List;

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
        for (MessageRetriever sender :
                subscribes) {
            sender.send(msg);
        }
    }
}
