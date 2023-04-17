package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.sender;

public interface SenderMessage {
    void add(MessageRetriever sender);

    void sendMsg(String msg);
}
