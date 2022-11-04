package bntu.fitr.gorbachev.ticketsgenerator.main.test;

import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.AttributeTag;

public class BeanObj extends AttributeTag {
    private Integer k;
    private boolean b;

    public BeanObj() {
    }

    public BeanObj(String name, int l, int r) {
        super(name, l, r);
    }

    public void setK(Integer k) {
        this.k = k;
    }

    public boolean isB() {
        return b;
    }
}
