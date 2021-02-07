package me.xueyao.response;

import com.github.pagehelper.Page;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 查询结果分页
 *
 * @param <T>
 */
@Data
public class ResultPage<T> {

    private int pageIndex = 1;

    private int pageSize = 20;

    private long total;

    private List<T> results;

    public <R> ResultPage<R> convert(Function<? super T, ? extends R> mapper) {
        ResultPage<R> ret = new ResultPage<>();
        ret.setPageIndex(this.getPageIndex());
        ret.setPageSize(this.getPageSize());
        ret.setTotal(this.getTotal());
        if (!CollectionUtils.isEmpty(this.getResults())) {
            ret.setResults(this.getResults().stream().map(mapper).filter(o -> o != null).collect(Collectors.toList()));
        }
        return ret;
    }

    public List<T> getResults() {
        if (results == null) {
            return Collections.emptyList();
        } else {
            return results;
        }
    }


    public ResultPage() {
    }

    public ResultPage(int pageIndex, int pageSize, long total, List<T> results) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.total = total;
        this.results = results;
    }

    public ResultPage(Page page, List<T> results) {
        this.pageIndex = page.getPageNum();
        this.pageSize = page.getPageSize();
        this.total = page.getTotal();
        this.results = results;
    }

    /**
     * 对list进行手动分页
     * @param pageNum
     * @param pageSize
     * @param list
     * @return
     */
    public ResultPage customizePage(int pageNum, int pageSize, List<T> list) {
        int total = list.isEmpty() ? 0 : list.size();
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);
        List<T> ts;


        if (startIndex > total || startIndex < 0) {
            ts = Collections.EMPTY_LIST;
        } else {
            ts = list.subList(startIndex, endIndex);
        }
        return new ResultPage<>(pageNum, pageSize, total, ts);
    }
}
