package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.factory;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.RegistrarSenderMessage;

public class RegistrarSenderMsgFactory {
    private final static RegistrarSenderMsgFactory instance = new RegistrarSenderMsgFactory();
    private final RegistrarSenderMessage rsm;

    private RegistrarSenderMsgFactory() {
        rsm = new RegistrarSenderMessageImpl();
    }

    public static RegistrarSenderMsgFactory getInstance() {
        return instance;
    }

    public RegistrarSenderMessage getRegistrarSenderMsg() {
        return rsm;
    }
}
