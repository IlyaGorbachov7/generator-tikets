package bntu.fitr.gorbachev.ticketsgenerator.main.entity;

public class Question2 extends QuestionExt {
    private int countRepeat;

    public Question2() {
    }

    public Question2(String section, int level, int repeat, int countRepeat) {
        super(section, level, repeat);
        this.countRepeat = countRepeat;
    }
}
