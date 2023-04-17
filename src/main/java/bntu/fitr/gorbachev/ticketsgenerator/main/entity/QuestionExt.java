package bntu.fitr.gorbachev.ticketsgenerator.main.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
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


    @Override
    public QuestionExt clone()  {
        QuestionExt clone = (QuestionExt) super.clone();
        clone.level = level;
        clone.repeat = repeat;
        return clone;
    }
}
