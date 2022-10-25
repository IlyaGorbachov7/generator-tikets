package bntu.fitr.gorbachev.ticketsgenerator.main.entity;

import java.util.Objects;

public class QuestionExt extends Question {

    private String section;
    private int level;
    private boolean repeat;

    public QuestionExt() {
    }

    public int getLevel() {
        return level;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setRepeat(boolean repeat) {
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
        return Objects.hash(super.hashCode(), level, repeat, section);
    }
}
