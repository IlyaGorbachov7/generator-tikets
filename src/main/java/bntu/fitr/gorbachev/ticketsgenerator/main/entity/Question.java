package bntu.fitr.gorbachev.ticketsgenerator.main.entity;


import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.util.*;

/**
 * The class represents the content of the question
 *
 * @author Gorbachev I. D.
 * @version 09.03.2022
 */
public abstract class Question implements Cloneable {
    private List<XWPFParagraph> listParagraphs;

    /**
     * Constructor without parameters
     */
    public Question() {
        listParagraphs = new ArrayList<>(4);
    }

    /**
     * @return list paragraphs
     */
    public List<XWPFParagraph> getListParagraphs() {
        return listParagraphs;
    }

    /**
     * @param listParagraphs set list paragraphs
     */
    public void setListParagraphs(List<XWPFParagraph> listParagraphs) {
        this.listParagraphs = listParagraphs;
    }

    /**
     * @param p added new Paragraph in list paragraphs
     */
    public void add(XWPFParagraph p) {
        this.listParagraphs.add(p);
    }

    public void addAll(Collection<XWPFParagraph> collection) {
        listParagraphs.addAll(collection);
    }

    /**
     * @param p remov new Paragraph in list paragraphs
     */
    public void remove(XWPFParagraph p) {
        this.listParagraphs.remove(p);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (obj instanceof Question q) {
            return Objects.equals(this.listParagraphs, q.listParagraphs);
        }
        return false;
    }

    /**
     * @return hash code of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(listParagraphs);
    }

    @Override
    public Question clone() {
        Question clone = null;
        try {
            clone = (Question) super.clone(); // НУЖНО ТАК! По соглашению Java
            clone.addAll(listParagraphs);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
