package bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.sender;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * see {@link TicketGeneratorUtil#getDelayStepGeneration()} this value allow to slow speed of each step generation tickets
 * on the specified interval
 *
 * @version 18.10.2024
 */
@Log4j2
public class SenderMessageImpl implements SenderMessage {
    List<MessageRetriever> subscribes = new ArrayList<>();
    ExecutorService singleExecutor = Executors.newSingleThreadExecutor();

    protected SenderMessageImpl() {
    }

    @Override
    public void add(MessageRetriever sender) {
        subscribes.add(sender);
    }

    @Override
    public void sendMsg(String msg) throws SenderStopSleepException {
        try {
            TicketGeneratorUtil.sleepFor(TicketGeneratorUtil.getDelayStepGeneration());
            CompletableFuture.runAsync(() -> {
                for (MessageRetriever sender : subscribes) {
                    sender.send(msg);
                }
            }, singleExecutor);
        }catch (RuntimeException exception){
            throw new SenderStopSleepException(exception.getMessage());
        }
    }
}
