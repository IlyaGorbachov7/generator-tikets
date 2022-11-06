package bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.attributes.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.attributes.AttributeService;

/**
 * This class represents as service between string and really data, which this string contains.
 * <p>
 * Simply put it using for extract attributes from questionTag;
 *
 * @version 06.11.2022
 */
public class QuestTagAttributeService implements AttributeService {
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
        return "QuestTagAttributeService{" +
               "n=" + n +
               ", r=" + r +
               '}';
    }
}
