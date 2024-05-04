package bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.sender;

public interface SenderMessage {
    void add(MessageRetriever sender);

    void sendMsg(String msg);
}
