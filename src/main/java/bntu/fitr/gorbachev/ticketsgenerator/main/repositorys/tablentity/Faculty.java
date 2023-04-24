package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity;

import lombok.Data;

/**
 * Created entities is mapping table view
 */
@Data
public class Faculty extends Entity{
    private String name;
    private Integer universityId;
}
