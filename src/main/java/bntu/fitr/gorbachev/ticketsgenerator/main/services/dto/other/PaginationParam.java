package bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.other;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PaginationParam {
    private int currentPage;
    private int itemsOnPage;
    private int totalPage;
}
