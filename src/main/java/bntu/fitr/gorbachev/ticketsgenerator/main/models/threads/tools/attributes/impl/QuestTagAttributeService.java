package bntu.fitr.gorbachev.ticketsgenerator.main.models.threads.tools.attributes.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.models.threads.tools.attributes.AttributeService;

/**
 * This class represents as service between string and really data, which this string contains.
 * <p>
 * Simply put it using for extract attributes from questionTag;
 *
 * @version 06.11.2022
 */
public class QuestTagAttributeService implements AttributeService {
    private int l;
    private int r;

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
