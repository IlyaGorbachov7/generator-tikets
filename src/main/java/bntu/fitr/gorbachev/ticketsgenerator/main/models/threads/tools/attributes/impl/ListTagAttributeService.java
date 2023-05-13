package bntu.fitr.gorbachev.ticketsgenerator.main.models.threads.tools.attributes.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.models.threads.tools.attributes.AttributeService;

/**
 * This class represents as service between string and really data, which this string contains.
 * <p>
 * Simply put it using for extract attributes from startTag;
 *
 * @version 01.11.2022
 */
public class ListTagAttributeService implements AttributeService {
    private String n;
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

    public ListTagAttributeService() {
        n = "";
    }

    public ListTagAttributeService(String name, int l, int r) {
        this.n = name;
        this.l = l;
        this.r = r;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

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
        return "ListTagAttributeService{" +
               "n='" + n + '\'' +
               ", l=" + l +
               ", r=" + r +
               '}';
    }


}
