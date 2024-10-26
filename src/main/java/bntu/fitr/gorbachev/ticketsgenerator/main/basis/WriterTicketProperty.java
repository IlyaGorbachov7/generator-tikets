package bntu.fitr.gorbachev.ticketsgenerator.main.basis;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.function.BiConsumer;

@Getter
@Setter
public class WriterTicketProperty implements Serializable {
    @Serial
    private final static long serialVersionUID = 34646677666L;
    // transient - ignore this filed when object serialized and deserialized.
    private transient HandlersOnProperties handlersOnProperties;

    private boolean isTicketOnSinglePage;
    private int quantityOnSinglePage;

    private int sizeFont;

    private boolean includeUniversity;
    private boolean includeFaculty;
    private boolean includeDepartment;
    private boolean includeSpecialization;
    private boolean includeDiscipline;
    private boolean includeSessionType;
    private boolean isExam;
    private boolean includeHeadDepartment;
    private boolean includeTeacher;
    private boolean includeProtocol;

    {
        quantityOnSinglePage = 1;
        sizeFont = 14;
        includeTeacher = includeHeadDepartment = includeProtocol = isExam = includeSessionType = includeUniversity
                = includeDepartment = includeFaculty = includeSpecialization
                = includeDiscipline = true;
        handlersOnProperties = new HandlersOnProperties();
    }

    public HandlersOnProperties getHandlersOnProperties() {
        if(handlersOnProperties == null) { // это написано потому что если серелизация не совводпает то нужно ицизицализирвать
            handlersOnProperties = new HandlersOnProperties();
        }
        return handlersOnProperties;
    }

    public WriterTicketProperty() {
    }

    public WriterTicketProperty(boolean isTicketOnSinglePage, int quantityOnSinglePage) {
        this.isTicketOnSinglePage = isTicketOnSinglePage;
        if (quantityOnSinglePage < 1) {
            throw new IllegalArgumentException("quantityOnSinglePage < 1");
        }
        this.quantityOnSinglePage = quantityOnSinglePage;
    }

    public void setIncludeUniversity(boolean includeUniversity) {
        handlersOnProperties.onIncludeUniversity.accept(this.includeUniversity, this.includeUniversity = includeUniversity);
    }

    public void setIncludeFaculty(boolean includeFaculty) {
        handlersOnProperties.onIncludeFaculty.accept(this.includeFaculty, this.includeFaculty = includeFaculty);
    }

    public void setIncludeDepartment(boolean includeDepartment) {
        handlersOnProperties.onIncludeDepartment.accept(this.includeDepartment, this.includeDepartment = includeDepartment);
    }

    public void setIncludeSpecialization(boolean includeSpecialization) {
        handlersOnProperties.onIncludeSpecialization.accept(this.includeSpecialization, this.includeSpecialization = includeSpecialization);
    }

    public void setIncludeDiscipline(boolean includeDiscipline) {
        handlersOnProperties.onIncludeDiscipline.accept(this.includeDiscipline, this.includeDiscipline = includeDiscipline);
    }

    public void setIncludeSessionType(boolean includeSessionType) {
        handlersOnProperties.onIncludeSessionType.accept(this.includeSessionType, this.includeSessionType = includeSessionType);
    }

    public void setExam(boolean exam) {
        handlersOnProperties.onExam.accept(this.isExam, isExam = exam);
    }

    public void setIncludeHeadDepartment(boolean includeHeadDepartment) {
        handlersOnProperties.onIncludeHeadDepartment.accept(this.includeHeadDepartment, this.includeHeadDepartment = includeHeadDepartment);
    }

    public void setIncludeTeacher(boolean includeTeacher) {
        handlersOnProperties.onIncludeTeacher.accept(this.includeTeacher, this.includeTeacher = includeTeacher);
    }

    public void setIncludeProtocol(boolean includeProtocol) {
        handlersOnProperties.onIncludeProtocol.accept(this.includeProtocol, this.includeProtocol = includeProtocol);
    }

    @Setter
    @Getter
    public class HandlersOnProperties {
        BiConsumer<Boolean, Boolean> onIncludeUniversity = (oldV, newV) -> {
        };
        BiConsumer<Boolean, Boolean> onIncludeFaculty = (oldV, newV) -> {
        };
        BiConsumer<Boolean, Boolean> onIncludeDepartment = (oldV, newV) -> {
        };
        BiConsumer<Boolean, Boolean> onIncludeSpecialization = (oldV, newV) -> {
        };
        BiConsumer<Boolean, Boolean> onIncludeDiscipline = (oldV, newV) -> {
        };
        BiConsumer<Boolean, Boolean> onIncludeSessionType = (oldV, newV) -> {
        };
        BiConsumer<Boolean, Boolean> onExam = (oldV, newV) -> {
        };
        BiConsumer<Boolean, Boolean> onIncludeHeadDepartment = (oldV, newV) -> {
        };
        BiConsumer<Boolean, Boolean> onIncludeTeacher = (oldV, newV) -> {
        };
        BiConsumer<Boolean, Boolean> onIncludeProtocol = (oldV, newV) -> {
        };
    }
}
