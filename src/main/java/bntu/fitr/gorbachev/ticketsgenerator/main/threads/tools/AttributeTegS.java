package bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools;

import java.util.regex.Pattern;

/**
 * This class represents as data transfer object between string and really data, which this string contains.
 * <p>
 * Simply put it using for extract attributes from startTag;
 *
 * @version 01.11.2022
 */
public class AttributeTegS {
    private String n;
    private int l;
    private int r;

    public AttributeTegS() {
        n = "";
    }

    public AttributeTegS(String name, int l, int r) {
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
        return "AttributeTegS{" +
               "n='" + n + '\'' +
               ", l=" + l +
               ", r=" + r +
               '}';
    }



}
