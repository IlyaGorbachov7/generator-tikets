package bntu.fitr.gorbachev.ticketsgenerator.main.dto.tablentity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HeadDepartment extends Entity {
    private String name;
    private Integer departmentId;
}
