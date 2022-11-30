package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.sender;

import java.util.ArrayList;
import java.util.List;

public class RegistrarSenderMessageImpl implements RegistrarSenderMessage {
    List<MessageSender> subscribes = new ArrayList<>();
    protected RegistrarSenderMessageImpl(){
    }
    @Override
    public void add(MessageSender sender) {
        subscribes.add(sender);
    }

    @Override
    public void sendMsg(String msg) {
        for (MessageSender sender :
                subscribes) {
            sender.send(msg);
        }
    }
}
