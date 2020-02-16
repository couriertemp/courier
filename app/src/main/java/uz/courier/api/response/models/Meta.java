package uz.courier.api.response.models;

import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("totalCount")
    private int totalCount;

    @SerializedName("PageCount")
    private int pageCount;

    @SerializedName("CurrentPage")
    private int currentPage;

    @SerializedName("PerPage")
    private int perPage;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }
}
