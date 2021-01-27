package me.xueyao.response;

public class PagedResponse extends BaseResponse {
    /**
     * 当前页数
     */
    private int pageIndex;
    /**
     * 分页条数
     */
    private int pageSize;
    /**
     * 总条数
     */
    private long count;

    public PagedResponse() {
    }

    public PagedResponse(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
