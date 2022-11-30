package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.sender;

public interface RegistrarSenderMessage {
    void add(MessageSender sender);

    void sendMsg(String msg);
}
