package com.hanks.frame.model;

import com.hanks.frame.base.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CC on 2016/12/11.
 */

public class PageModel<Data extends Serializable> extends BaseModel {
    private List<Data> result;//
    private boolean autoCount;
    private int first;
    private boolean hasNext;
    private boolean hasPre;
    private int lastIndex;
    private int nextPage;
    private boolean orderBySetted;
    private int pageNo;
    private int pageSize;
    private int prePage;
    private int totalCount;
    private int totalPages;


    public List<Data> getResult() {
        return result;
    }

    public void setResult(List<Data> result) {
        this.result = result;
    }

    public boolean isAutoCount() {
        return autoCount;
    }

    public void setAutoCount(boolean autoCount) {
        this.autoCount = autoCount;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasPre() {
        return hasPre;
    }

    public void setHasPre(boolean hasPre) {
        this.hasPre = hasPre;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isOrderBySetted() {
        return orderBySetted;
    }

    public void setOrderBySetted(boolean orderBySetted) {
        this.orderBySetted = orderBySetted;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public String toString() {
        return "PageModel{" +
                "result=" + result +
                ", autoCount=" + autoCount +
                ", first=" + first +
                ", hasNext=" + hasNext +
                ", hasPre=" + hasPre +
                ", lastIndex=" + lastIndex +
                ", nextPage=" + nextPage +
                ", orderBySetted=" + orderBySetted +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", prePage=" + prePage +
                ", totalCount=" + totalCount +
                ", totalPages=" + totalPages +
                '}';
    }
}
