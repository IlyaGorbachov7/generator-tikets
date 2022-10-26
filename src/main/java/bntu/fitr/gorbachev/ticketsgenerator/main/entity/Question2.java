package bntu.fitr.gorbachev.ticketsgenerator.main.entity;

public class Question2 extends QuestionExt {
    private int countRepeat;

    public Question2() {
    }

    public Question2(String section, int level, boolean repeat, int countRepeat) {
        super(section, level, repeat);
        this.countRepeat = countRepeat;
    }

    public int getCountRepeat() {
        return countRepeat;
    }

    public void setCountRepeat(int countRepeat) {
        this.countRepeat = countRepeat;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + super.toString() + "}";
    }
}
