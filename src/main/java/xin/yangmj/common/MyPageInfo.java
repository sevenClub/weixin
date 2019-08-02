package xin.yangmj.common;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageSerializable;

import java.util.Collection;
import java.util.List;

public class MyPageInfo<T> extends PageSerializable<T> {
    private int pageNum;


    public MyPageInfo(List<T> list) {
        this(list, 8);
    }

    public MyPageInfo(List<T> list, int navigatePages) {
        super(list);

        if (list instanceof Page) {
            Page page = (Page)list;
            this.pageNum = page.getPageNum();

        } else if (list instanceof Collection) {
            this.pageNum = 1;

        }
    }

    public static <T> MyPageInfo<T> of(List<T> list) {
        return new MyPageInfo(list);
    }

    public static <T> MyPageInfo<T> of(List<T> list, int navigatePages) {
        return new MyPageInfo(list, navigatePages);
    }
    public int getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("PageInfo{");
        sb.append("pageNum=").append(this.pageNum);
        sb.append(", total=").append(this.total);
        sb.append('}');
        return sb.toString();
    }
}
