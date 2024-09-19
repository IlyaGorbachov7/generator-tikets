package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.logger.LoggerException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.logger.LoggerUtil;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

@Log4j2
public class M {
    public static void main(String[] args) throws LoggerException {
        LoggerUtil.init();

       log.debug("HI");
    }
}
