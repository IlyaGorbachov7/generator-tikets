package bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.tags.attributes.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.tags.attributes.SomeAttributes;

public class AttributesQuestTag implements SomeAttributes {
    private int n;
    private int r;

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    @Override
    public String toString() {
        return "AttributesQuestTag{" +
               "n=" + n +
               ", r=" + r +
               '}';
    }
}
