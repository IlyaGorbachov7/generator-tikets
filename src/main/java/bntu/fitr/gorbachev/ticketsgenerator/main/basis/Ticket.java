package bntu.fitr.gorbachev.ticketsgenerator.main.basis;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is a representation of Tickets
 *
 * @author Gorbachev I. D.
 * @version 09.03.2022
 */
@Setter
@Getter
@ToString(callSuper = true)
public class Ticket<T extends QuestionExt> implements Cloneable {
    private String institute;
    private String faculty;
    private String department;
    private String specialization;
    private String discipline;
    private String teacher;
    private String headDepartment;

    private SessionType type;
    private String date;
    private String protocolNumber;
    private List<T> questions;

    {
        institute = "";
        faculty = "";
        department = "";
        specialization = "";
        discipline = "";
        teacher = "";
        headDepartment = "";

        type = SessionType.AUTUMN;
        date = "";
        protocolNumber = "";
    }

    /**
     * Constructor without parameters
     */
    public Ticket() {
        questions = new ArrayList<>(4);
    }

    /**
     * Constructor  with parameters
     *
     * @param capacity capacity list questions
     */
    public Ticket(int capacity) {
        questions = new ArrayList<>(capacity);
    }

    /**
     * Constructor  with parameters
     *
     * @param institute      institute
     * @param faculty        faculty
     * @param department     department
     * @param specialization specialization
     * @param discipline     discipline
     * @param teacher        teacher
     * @param headDepartment headDepartment
     * @param type           session type
     * @param date           date of approval
     * @param protocolNumber protocolNumber
     */
    public Ticket(String institute, String faculty, String department,
                  String specialization, String discipline,
                  String teacher, String headDepartment, SessionType type,
                  String date, String protocolNumber) {
        this();
        this.institute = institute;
        this.faculty = faculty;
        this.department = department;
        this.specialization = specialization;
        this.discipline = discipline;
        this.teacher = teacher;
        this.headDepartment = headDepartment;

        this.type = type;
        this.date = date;
        this.protocolNumber = protocolNumber;

    }

    /**
     * Constructor  with parameters
     *
     * @param institute      institute
     * @param faculty        faculty
     * @param department     department
     * @param specialization specialization
     * @param discipline     discipline
     * @param teacher        teacher
     * @param headDepartment headDepartment
     * @param type           session type
     * @param date           date of approval
     * @param protocolNumber protocolNumber
     * @param capacity       capacity list questions
     */
    public Ticket(String institute, String faculty, String department,
                  String specialization, String discipline,
                  String teacher, String headDepartment, SessionType type,
                  String date, String protocolNumber, int capacity) {
        this.institute = institute;
        this.faculty = faculty;
        this.department = department;
        this.specialization = specialization;
        this.discipline = discipline;
        this.teacher = teacher;
        this.headDepartment = headDepartment;

        this.type = type;
        this.date = date;
        this.protocolNumber = protocolNumber;
        questions = new ArrayList<>(capacity);
    }

    /**
     * Method add new question in Ticket
     *
     * @param q question
     */
    public void add(T q) {
        questions.add(q);
    }

    /**
     * Method removing exist question in Ticket
     *
     * @param q question
     */
    public boolean remove(T q) {
        return questions.remove(q);
    }

    /**
     * Removes all of the elements from this list (optional operation).
     * The list will be empty after this call returns.
     *
     * @throws UnsupportedOperationException if the {@code clear} operation
     *                                       is not supported by this list
     */
    public void clearQuestions() {
        questions.clear();
    }

    /**
     * @return quantity questions in Ticket
     */
    public int getQuantity() {
        return questions.size();
    }

    public static <T extends QuestionExt> Ticket<T> of() {
        return new Ticket<>();
    }

    public static <T extends QuestionExt> Ticket<T> of(String institute, String faculty, String department,
                                                       String specialization, String discipline,
                                                       String teacher, String headDepartment, SessionType type,
                                                       String date, String protocolNumber) {
        return new Ticket<>(institute, faculty, department,
                specialization, discipline, teacher, headDepartment, type,
                date, protocolNumber);
    }

    public static <T extends QuestionExt> Ticket<T> of(String institute, String faculty, String department,
                                                       String specialization, String discipline,
                                                       String teacher, String headDepartment, SessionType type,
                                                       String date, String protocolNumber, int cap) {
        return new Ticket<>(institute, faculty, department,
                specialization, discipline, teacher, headDepartment, type,
                date, protocolNumber, cap);
    }

    /**
     * Enum choosing session type
     *
     * @author Gorbachev I. D.
     * @version 10.03.2022
     */
    public enum SessionType {
        AUTUMN("sessionType.autumn"), WINTER("sessionType.winter"),
        SPRING("sessionType.spring"), SUMMER("sessionType.summer"),
        NON("sessionType.nondefine");
        private final String name;

        /**
         * The constructor without paragraph
         *
         * @param name string session
         */
        SessionType(String name) {
            this.name = name;
        }

        public String key() {
            return name;
        }

        /**
         * This method represent object
         *
         * @return string
         */
        @Override
        public String toString() {
            return Localizer.get(name);
        }

    }

    /**
     * Not a complete copy.
     * <p>
     * <b>NOT a complete copy:</b> questions inside list questions
     */
    @Override
    public Ticket<T> clone() {
        Ticket<T> clone = null;
        try {
            clone = (Ticket<T>) super.clone();
            clone.questions = new ArrayList<>(this.questions);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
