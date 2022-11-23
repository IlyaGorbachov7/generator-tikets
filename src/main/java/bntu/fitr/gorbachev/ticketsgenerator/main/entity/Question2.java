package bntu.fitr.gorbachev.ticketsgenerator.main.entity;

import java.util.UUID;

public class Question2 extends QuestionExt {
    private final UUID id;

    {
        id = UUID.randomUUID();
    }

    public Question2() {
    }

    public Question2(String section, int level, int repeat, int countRepeat) {
        super(section, level, repeat);
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Question2{" +
               "id=" + id +
               super.toString() +
               '}';
    }
}
