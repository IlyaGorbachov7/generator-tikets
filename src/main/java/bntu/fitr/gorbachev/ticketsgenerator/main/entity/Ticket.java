package bntu.fitr.gorbachev.ticketsgenerator.main.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is a representation of Tickets
 *
 * @author Gorbachev I. D.
 * @version 09.03.2022
 */
public class Ticket<T extends QuestionExt> {
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

    /**
     * Constructor without parameters
     */
    public Ticket() {
        questions = new ArrayList<>();
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
        this(institute, faculty, department, specialization, discipline, teacher,
                headDepartment, type, date, protocolNumber);
        questions = new ArrayList<>(capacity);
    }

    /**
     * @return string Institute
     */
    public String getInstitute() {
        return institute;
    }

    /**
     * @param institute institute
     */
    public void setInstitute(String institute) {
        this.institute = institute;
    }

    /**
     * @return string Faculty
     */
    public String getFaculty() {
        return faculty;
    }

    /**
     * @param faculty faculty
     */
    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    /**
     * @return Department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department Department
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return Specialization
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * @param specialization Specialization
     */
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    /**
     * @return Discipline
     */
    public String getDiscipline() {
        return discipline;
    }

    /**
     * @param discipline Discipline
     */
    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    /**
     * @return Teacher
     */
    public String getTeacher() {
        return teacher;
    }

    /**
     * @param teacher Teacher
     */
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    /**
     * @return HeadDepartment
     */
    public String getHeadDepartment() {
        return headDepartment;
    }

    /**
     * @param headDepartment HeadDepartment
     */
    public void setHeadDepartment(String headDepartment) {
        this.headDepartment = headDepartment;
    }

    /**
     * @return ProtocolNumber
     */
    public String getProtocolNumber() {
        return protocolNumber;
    }

    /**
     * @param protocolNumber ProtocolNumber
     */
    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    /**
     * @return date of approval
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date date of approval
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return session type
     */
    public SessionType getType() {
        return type;
    }

    /**
     * @param type SessionType
     */
    public void setType(SessionType type) {
        this.type = type;
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
     * @return quantity questions in Ticket
     */
    public int getQuantity() {
        return questions.size();
    }

    /**
     * @param q questions list
     */
    public void setQuestions(List<T> q) {
        this.questions = q;
    }

    /**
     * @return questions list
     */
    public List<T> getQuestions() {
        return questions;
    }

    @Override
    public String toString() {
        return "Ticket{" +
               "institute='" + institute + '\'' +
               ", faculty='" + faculty + '\'' +
               ", department='" + department + '\'' +
               ", specialization='" + specialization + '\'' +
               ", discipline='" + discipline + '\'' +
               ", teacher='" + teacher + '\'' +
               ", headDepartment='" + headDepartment + '\'' +
               ", type=" + type +
               ", date='" + date + '\'' +
               ", protocolNumber='" + protocolNumber + '\'' +
               ", questions=" + questions +
               '}';
    }

    /**
     * Enum choosing session type
     *
     * @author Gorbachev I. D.
     * @version 10.03.2022
     */
    public enum SessionType {
        AUTUMN("Осенняя"), WINTER("Зимняя"),
        SPRING("Весенняя"), SUMMER("Летняя");
        private final String name;

        /**
         * The constructor without paragraph
         *
         * @param name string session
         */
        SessionType(String name) {
            this.name = name;
        }

        /**
         * This method represent object
         *
         * @return string
         */
        @Override
        public String toString() {
            return name;
        }
    }
}
