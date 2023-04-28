package bntu.fitr.gorbachev.ticketsgenerator.main.models.impl.sender;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class SenderMessageImpl implements SenderMessage {
    List<MessageRetriever> subscribes = new ArrayList<>();

    protected SenderMessageImpl() {
    }

    @Override
    public void add(MessageRetriever sender) {
        subscribes.add(sender);
    }

    @SneakyThrows
    @Override
    public void sendMsg(String msg) {
        for (MessageRetriever sender :
                subscribes) {
            sender.getSentMsg(msg);
        }
    }
}
