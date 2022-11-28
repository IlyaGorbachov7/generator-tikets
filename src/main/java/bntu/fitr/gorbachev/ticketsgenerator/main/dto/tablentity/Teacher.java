package bntu.fitr.gorbachev.ticketsgenerator.main.dto.tablentity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created entities is mapping table view
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Teacher extends Entity{
    private String name;
    private Integer facultyId;
}
