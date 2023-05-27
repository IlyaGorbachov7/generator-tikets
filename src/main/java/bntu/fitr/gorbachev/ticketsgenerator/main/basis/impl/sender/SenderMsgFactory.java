package bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.sender;

public class SenderMsgFactory {
    private final static SenderMsgFactory instance = new SenderMsgFactory();
    private final SenderMessage rsm;

    private SenderMsgFactory() {
        rsm = new SenderMessageImpl();
    }

    public static SenderMsgFactory getInstance() {
        return instance;
    }

    public SenderMessage getSenderMsg() {
        return rsm;
    }
}
