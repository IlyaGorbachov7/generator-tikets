package bntu.fitr.gorbachev.ticketsgenerator.main.models;

import bntu.fitr.gorbachev.ticketsgenerator.main.models.threads.tools.attributes.impl.ListTagAttributeService;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionExt extends Question {
    public static final int MIN_VALUE_LEVEL = 0;
    public static final int MIN_VALUE_REPEAT = 1;

    private String section;
    /**
     * By default, value property {@link #level} equals {@link #MIN_VALUE_LEVEL}.
     * However, this value can be changed via setter. if giving value will be less than {@link #MIN_VALUE_LEVEL}
     * then throw exception.
     * <p>
     * Setter is final
     */
    private int level = MIN_VALUE_LEVEL;
    /**
     * By default, value property {@link #repeat} equals {@link #MIN_VALUE_REPEAT}.
     * However, this value can be changed via setter. if giving value will be less than {@link #MIN_VALUE_REPEAT}
     * then throw exception
     * <p>
     * Setter is final
     */
    private int repeat = MIN_VALUE_REPEAT;

    public QuestionExt() {
    }

    public QuestionExt(String section) {
        this.section = section;
    }

    public QuestionExt(String section, int level) {
        if (level < MIN_VALUE_LEVEL) {
            throw new IllegalArgumentException("level can not be less than " + MIN_VALUE_LEVEL);
        }
        this.section = section;
        this.level = level;
    }

    public QuestionExt(String section, int level, int repeat) {
        if (repeat < MIN_VALUE_REPEAT) {
            throw new IllegalArgumentException("repeat can not be less than " + MIN_VALUE_REPEAT);
        }
        this.section = section;
        this.level = level;
        this.repeat = repeat;
    }

    public final void setRepeat(int repeat) {
        if (repeat < MIN_VALUE_REPEAT) {
            throw new IllegalArgumentException("repeat can not be less than " + MIN_VALUE_REPEAT);
        }
        this.repeat = repeat;
    }

    public final void setLevel(int level) {
        if (level < MIN_VALUE_LEVEL) {
            throw new IllegalArgumentException("level can not be less than " + MIN_VALUE_LEVEL);
        }
        this.level = level;
    }

    @Override
    public QuestionExt clone() {
        QuestionExt clone = (QuestionExt) super.clone();
        clone.level = level;
        clone.repeat = repeat;
        return clone;
    }
}
