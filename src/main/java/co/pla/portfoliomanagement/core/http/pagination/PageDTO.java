package co.pla.portfoliomanagement.common.http.pagination;


import java.io.Serializable;
import java.util.List;

public class PageDTO<T> implements Serializable {

    private final List<T> items;
    private final int page;
    private final int perPage;
    private final long total;

    public PageDTO(
            List<T> items,
            int page,
            int perPage,
            long total
    ) {
        this.items = items;
        this.page = page;
        this.perPage = perPage;
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public int getPage() {
        return page;
    }

    public int getPerPage() {
        return perPage;
    }

    public long getTotal() {
        return total;
    }
}
