package bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.connectionpool.PoolConnection;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.AbstractController;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.impl.SplashScreenPanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.AbstractModel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl.SplashScreenModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SplashScreenController extends AbstractController {
    private final SplashScreenPanel view;
    private final SplashScreenModel model;

    public SplashScreenController(BasePanel view, AbstractModel model) {
        super(view, model);
        this.view = (SplashScreenPanel) view;
        this.model = (SplashScreenModel) model;
        initView();
        init();
    }

    @Override
    public void init() {
        model.addChangeFiledModelListener(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void eventChangeFiledModel(ChangeFieldModelEvent e) {
        view.changeStateViewElems(e);
    }

    public void actionExitBtn() {
        System.exit(0);
    }

    public void actionNextBtn() {
        view.getThread().interrupt();
        view.getThreadProcess().start();
        view.getThreadInit().start();
    }

    public void actionWindowClosing() {
        System.out.println(SplashScreenPanel.class + " window Closing");
        PoolConnection.getInstance().destroyConnectionPool();
        view.getThreadProcess().interrupt();
        /* There are cases when throw NullPointerException.
         * This event happening, if this thread trying
         * to destroy Connection Pool, that still doesn't initialize arrayQueue
         *
         * This event you can achieve way fasted closing SplashScreen frame
         * before initialization connection pool
         *
         * However, probability this case very, very small, since the thread, that
         * initialize connection pool will be
         * running first and method destroyConnectionPool and initPool is synchronized
         *
         * If this event still happened, then this thread-handler throw don't handled exception
         * Then Swing-Framework don't complete success this process.
         * In the finally, frame Window don't closed*/
    }
}