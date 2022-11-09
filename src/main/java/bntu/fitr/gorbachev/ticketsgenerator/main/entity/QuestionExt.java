package bntu.fitr.gorbachev.ticketsgenerator.main.entity;

import java.util.Objects;

public class QuestionExt extends Question {
    private String section;
    private int level;
    private int repeat;

    public QuestionExt() {
    }

    public QuestionExt(String section) {
        this.section = section;
    }

    public QuestionExt(String section, int level) {
        this.section = section;
        this.level = level;
    }

    public QuestionExt(String section, int level, int repeat) {
        this.section = section;
        this.level = level;
        this.repeat = repeat;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuestionExt that = (QuestionExt) o;
        return level == that.level && repeat == that.repeat && Objects.equals(section, that.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), section, level, repeat);
    }

    @Override
    public String toString() {
        return "QuestionExt{" +
               "section='" + section + '\'' +
               ", level=" + level +
               ", repeat=" + repeat +
               ", text = " + getListParagraphs().get(0).getText() +
               '}';
    }

    @Override
    public QuestionExt clone()  {
        QuestionExt clone = (QuestionExt) super.clone();
        clone.level = level;
        clone.repeat = repeat;
        return clone;
    }
}
