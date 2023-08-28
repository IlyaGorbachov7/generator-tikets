package bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.tools.attributes.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.tools.attributes.AttributeService;

/**
 * This class represents as service between string and really data, which this string contains.
 * <p>
 * Simply put it using for extract attributes from questionTag;
 *
 * @version 06.11.2022
 */
public class QuestTagAttributeService implements AttributeService {
    /**
     * if user don't specify explicitly this attribute then by default, value property {@link #l} equals Integer.MAX_VALUE.
     * However, this value can be changed via setter
     */
    private int l = Integer.MAX_VALUE;
    /**
     * if user don't specify explicitly this attribute then by default, value property {@link #r} equals Integer.MAX_VALUE.
     * However, this value can be changed via setter
     */
    private int r = Integer.MAX_VALUE;

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
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
               "l=" + l +
               ", r=" + r +
               '}';
    }
}
