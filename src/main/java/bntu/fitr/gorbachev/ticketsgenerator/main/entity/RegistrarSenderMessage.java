package bntu.fitr.gorbachev.ticketsgenerator.main.entity;

public interface RegistrarSenderMessage {
    void add(MessageSender sender);

    void sendMsg(String msg);
}
