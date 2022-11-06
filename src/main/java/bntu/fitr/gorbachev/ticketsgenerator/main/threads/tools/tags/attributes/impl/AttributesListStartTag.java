package bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.tags.attributes.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.tags.attributes.SomeAttributes;

/**
 * This class represents as data transfer object between string and really data, which this string contains.
 * <p>
 * Simply put it using for extract attributes from startTag;
 *
 * @version 01.11.2022
 */
public class AttributesListStartTag implements SomeAttributes {
    private String n;
    private int l;
    private int r;

    public AttributesListStartTag() {
        n = "";
    }

    public AttributesListStartTag(String name, int l, int r) {
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
        return "AttributesListStartTag{" +
               "n='" + n + '\'' +
               ", l=" + l +
               ", r=" + r +
               '}';
    }



}
