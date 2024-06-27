package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic purpose this realization at the class is means to simply and esy use incoming input data from view.
 * <p>
 * I don't want directly change view items. I want it makes from this class;
 */
@Getter
@Setter
@Builder
public final class PaginationView {
    public final static String CURRENTPAGE = "CURRENTPAGE";
    @Builder.Default
    private int currentPage = 1;

    public final static String TOTALPAGE = "TOTALPAGE";
    @Builder.Default
    private int totalPage = 1;

    public final static String ITEMSPAGE = "ITEMSPAGE";
    @Builder.Default
    private int itemsOnPage = 0;

    public final static String FILTERTEXT = "FILTERTEXT";
    @Builder.Default
    private String filterText = "";

    @Builder.Default
    private List<PropertyChangeListener> handler = new ArrayList<>(2);


    public void setCurrentPage(int currentPage) {
        if (currentPage == 0) throw new IllegalArgumentException("CurrentPage must be > 0");
        int oldV = this.currentPage;
        this.currentPage = currentPage;
        firePropertyChange(new PropertyChangeEvent(this, CURRENTPAGE, oldV, this.currentPage));
    }

    public void setTotalPage(int totalPage) {
        if (currentPage == 0) throw new IllegalArgumentException("CurrentPage must be > 0");
        int oldV = this.totalPage;
        this.totalPage = totalPage;
        firePropertyChange(new PropertyChangeEvent(this, TOTALPAGE, oldV, this.totalPage));
    }

    public void setItemsOnPage(int itemsOnPage) {
        int oldV = this.itemsOnPage;
        this.itemsOnPage = itemsOnPage;
        firePropertyChange(new PropertyChangeEvent(this, ITEMSPAGE, oldV, this.itemsOnPage));
    }

    public void setFilterText(String filterText) {
        String oldV = this.filterText;
        this.filterText = filterText;
        firePropertyChange(new PropertyChangeEvent(this, FILTERTEXT, oldV, this.filterText));
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        handler.add(listener);
    }

    public void removePropertyChangeListenerAll() {
        handler.clear();
    }

    private void firePropertyChange(PropertyChangeEvent event) {
        handler.parallelStream().forEach(h -> h.propertyChange(event));
    }
}