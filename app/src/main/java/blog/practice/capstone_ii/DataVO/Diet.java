package blog.practice.capstone_ii.DataVO;

public class Diet {
    private String  dId, dName, dPrice, dThumb;

    public Diet(String dId, String dName, String dPrice, String dThumb) {
        this.dId = dId;
        this.dName = dName;
        this.dPrice = dPrice;
        this.dThumb = dThumb;
    }

    public String getdId() {
        return dId;
    }

    public void setdId(String dId) {
        this.dId = dId;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdPrice() {
        return dPrice;
    }

    public void setdPrice(String dPrice) {
        this.dPrice = dPrice;
    }

    public String getdThumb() {
        return dThumb;
    }

    public void setdThumb(String dThumb) {
        this.dThumb = dThumb;
    }
}
