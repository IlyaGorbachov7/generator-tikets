package bntu.fitr.gorbachev.ticketsgenerator.main.entity;

import java.util.Objects;
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
        return getListParagraphs().get(0).getText();
    }

    /**
     * Return {@code true} if {@code id} == {@code o.id}
     * This method don't compare fields super class.
     * <p>
     * I'm Interested in the property {@link #id}, which is unique for each instance object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question2 question2 = (Question2) o;
        return Objects.equals(id, question2.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
