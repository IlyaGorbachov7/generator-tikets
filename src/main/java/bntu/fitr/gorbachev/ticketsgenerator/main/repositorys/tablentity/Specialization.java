package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created entities is mapping table view
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Specialization extends Entity{
    private String name;
    private Integer departmentId;
}
