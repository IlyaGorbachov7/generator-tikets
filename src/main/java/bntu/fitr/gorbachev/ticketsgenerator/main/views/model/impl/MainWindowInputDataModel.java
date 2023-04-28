package bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.models.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.threads.tools.constants.TextPatterns;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.tools.GenerationMode;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.AbstractModel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.exceptions.IllegalInputValueException;
import lombok.Getter;
import lombok.ToString;

import java.io.File;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.impl.actionconst.MainWindowActionConst.ACTION_ADD_LIST_QUESTIONFILE;
import static bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.impl.actionconst.MainWindowActionConst.ACTION_REMOVE_LIST_QUESTIONFILE;
import static bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl.perconst.MainWindowInputDataModelConst.*;
import static java.util.Map.entry;

@Getter
@ToString(doNotUseGetters = true)
public class MainWindowInputDataModel extends AbstractModel {
    private String institute = "Белорусский национальный технический университет";
    private String faculty = "Факультет информационных технологий и робототехники";
    private String department = "Кафедра программного обеспечения и технологий";
    private String specialization = "Инженер-программист (в проектировании и производстве)";
    private String discipline = "Программирование на Java";
    private String teacher = "Фамилия И. О.";
    private String headDepartment = "Фамилия И. О.";
    private String protocol = "32432.3423.23";
    private Ticket.SessionType typeSession;
    private LocalDate dateTime = LocalDate.now().minusDays(20);

    // -------------------------------------------
    @ToString.Exclude
    private final int minVQuantityTickets = 1;
    @ToString.Exclude
    private final int maxVQuantityTickets = 1000;
    @ToString.Exclude
    private final int minVQuantityQuestionTickets = 1;
    @ToString.Exclude
    private final int maxVQuantityQuestionTickets = 100;

    private int quantityTickets = 15;
    private int quantityQuestionTickets = 2;
    private final List<File> listFilesRsc = new ArrayList<>();
    private GenerationMode generationMode = GenerationMode.MODE_1;
    private boolean randomRead = true;
    private boolean randomWrite = true;

    {
        typeSession = ValueRange.of(Month.SEPTEMBER.getValue(), Month.DECEMBER.getValue()).
                isValidIntValue(dateTime.getMonthValue()) ?
                Ticket.SessionType.WINTER :
                Ticket.SessionType.SUMMER;
    }

    @Override
    public void triggeringInitView() {
        fireInitViewEvent(Map.ofEntries(
                entry(INSTITUTE, institute),
                entry(FACULTY, faculty),
                entry(DEPARTMENT, department),
                entry(SPECIALIZATION, specialization),
                entry(DISCIOLINE, discipline),
                entry(TEACHAR, teacher),
                entry(HEADDEPARTMENT, headDepartment),
                entry(PROTOCOL, protocol),
                entry(TYPESESSION, typeSession),
                entry(DATETIME, dateTime),
                entry(QUANTITYTICKETS, quantityTickets),
                entry(QUANTITYQUESTIONTICKETS, quantityQuestionTickets),
                entry(LIST_QUESTIONSFILE, listFilesRsc),
                entry(GENERATIONMODE, generationMode),
                entry(RANDOMREAD, randomRead),
                entry(RANDOMWRITE, randomWrite)));
    }

    protected void checkConditionsValueField(final String fieldName, Object value) {
        String msgWarning = "";
        switch (fieldName) {
            case INSTITUTE, FACULTY, DEPARTMENT, SPECIALIZATION, DISCIOLINE -> {
                msgWarning = (!TextPatterns.COMMON_PATTERN.matches((String) value))
                        ? "Допустимы символы:\n A-Я,А-Z, а-я, a-z, 0-9, -, _, «, », \", }, {, ),(" : "";
            }
            case TEACHAR, HEADDEPARTMENT -> {
                msgWarning = (!TextPatterns.PERSON_NAME_PATTERN_V1.matches((String) value))
                        ? "Пример:\n Фамилия Имя Отчество (или Фамилия И. О.)" : "";
            }
            case PROTOCOL -> {
                msgWarning = (!TextPatterns.PROTOCOL_PATTERN.matches((String) value))
                        ? "Пример:\n 4.1 (или 1.23.1)" : "";
            }
            case TYPESESSION -> {
                Ticket.SessionType sessionType = (Ticket.SessionType) value;
                msgWarning = (sessionType != Ticket.SessionType.WINTER && sessionType != Ticket.SessionType.SUMMER) ?
                        String.format("Допустимо знач. %s либо %s", Ticket.SessionType.WINTER, Ticket.SessionType.SUMMER) : "";
            }
            case DATETIME -> {
                // no need to check anything
            }
            case QUANTITYTICKETS -> {
                Integer newValue = (Integer) value;
                msgWarning = (newValue < minVQuantityTickets || newValue > maxVQuantityTickets) ?
                        String.format("newValue quantityTickets=%d outside required boundary : [%d, %d]",
                                newValue, minVQuantityTickets, maxVQuantityTickets) : "";
            }
            case QUANTITYQUESTIONTICKETS -> {
                Integer newValue = (Integer) value;
                msgWarning = (newValue < minVQuantityQuestionTickets || newValue > maxVQuantityQuestionTickets) ?
                        String.format("newValue quantityTickets=%d outside required boundary : [%d, %d]",
                                newValue, minVQuantityQuestionTickets, maxVQuantityQuestionTickets) : "";
            }
            default -> throw new IllegalArgumentException(String.format("FiledName=%s not defined!", fieldName));
        }
        if (!msgWarning.isEmpty()) {
            throw new IllegalInputValueException(msgWarning);
        }
    }

    public void setInstitute(String institute) {
        checkConditionsValueField(INSTITUTE, institute);
        String oldV = this.institute;
        this.institute = institute;
        fireChangeFiledEvent(INSTITUTE, oldV, institute);
    }

    public void setFaculty(String faculty) {
        checkConditionsValueField(FACULTY, faculty);
        String oldV = this.faculty;
        this.faculty = faculty;
        fireChangeFiledEvent(FACULTY, oldV, faculty);
    }

    public void setDepartment(String department) {
        checkConditionsValueField(DEPARTMENT, department);
        String oldV = this.department;
        this.department = department;
        fireChangeFiledEvent(DEPARTMENT, oldV, department);
    }

    public void setSpecialization(String specialization) {
        checkConditionsValueField(SPECIALIZATION, specialization);
        String oldV = this.specialization;
        this.specialization = specialization;
        fireChangeFiledEvent(SPECIALIZATION, oldV, specialization);
    }

    public void setDiscipline(String discipline) {
        checkConditionsValueField(DISCIOLINE, discipline);
        String oldV = this.discipline;
        this.discipline = discipline;
        fireChangeFiledEvent(DISCIOLINE, oldV, discipline);
    }

    public void setTeacher(String teacher) {
        checkConditionsValueField(TEACHAR, teacher);
        String oldV = this.teacher;
        this.teacher = teacher;
        fireChangeFiledEvent(TEACHAR, oldV, teacher);
    }

    public void setHeadDepartment(String headDepartment) {
        checkConditionsValueField(HEADDEPARTMENT, headDepartment);
        String oldV = this.headDepartment;
        this.headDepartment = headDepartment;
        fireChangeFiledEvent(HEADDEPARTMENT, oldV, headDepartment);
    }

    public void setProtocol(String protocol) {
        checkConditionsValueField(PROTOCOL, protocol);
        String oldV = this.protocol;
        this.protocol = protocol;
        fireChangeFiledEvent(PROTOCOL, oldV, protocol);
    }

    public void setTypeSession(Ticket.SessionType typeSession) {
        checkConditionsValueField(TYPESESSION, typeSession);
        Ticket.SessionType oldV = this.typeSession;
        this.typeSession = typeSession;
        fireChangeFiledEvent(TYPESESSION, oldV, typeSession);
    }

    public void setDateTime(LocalDate dateTime) {
        checkConditionsValueField(DATETIME, dateTime);
        LocalDate oldV = this.dateTime;
        this.dateTime = dateTime;
        fireChangeFiledEvent(DATETIME, oldV, dateTime);
    }
    // ---------------------------------------------

    public void setQuantityTickets(int quantityTickets) {
        checkConditionsValueField(QUANTITYTICKETS, quantityTickets);
        Integer oldV = this.quantityTickets;
        this.quantityTickets = quantityTickets;
        fireChangeFiledEvent(QUANTITYTICKETS, oldV, quantityTickets);
    }

    public void setQuantityQuestionTickets(int quantityQuestionTickets) {
        checkConditionsValueField(QUANTITYQUESTIONTICKETS, quantityQuestionTickets);
        Integer oldV = this.quantityQuestionTickets;
        this.quantityQuestionTickets = quantityQuestionTickets;
        fireChangeFiledEvent(QUANTITYQUESTIONTICKETS, oldV, quantityQuestionTickets);
    }

    public void setGenerationMode(GenerationMode generationMode) {
        GenerationMode oldV = this.generationMode;
        this.generationMode = generationMode;
        fireChangeFiledEvent(GENERATIONMODE, oldV, generationMode);
    }

    public void setRandomRead(boolean randomRead) {
        boolean oldV = this.randomRead;
        this.randomRead = randomRead;
        fireChangeFiledEvent(RANDOMREAD, oldV, randomRead);
    }

    public void setRandomWrite(boolean randomWrite) {
        boolean oldV = this.randomWrite;
        this.randomWrite = randomWrite;
        fireChangeFiledEvent(RANDOMWRITE, oldV, randomWrite);
    }

    public void addFile(File file) {
        listFilesRsc.add(file);
        fireActionEvent(ACTION_ADD_LIST_QUESTIONFILE, file);
    }

    public void removeFile(File file) {
        listFilesRsc.remove(file);
        fireActionEvent(ACTION_REMOVE_LIST_QUESTIONFILE, file);
    }

    public boolean containsFile(File file) {
        return listFilesRsc.contains(file);
    }
}
