package kr.ac.ssu.myrecipe.TagDB;

public class Tag {
    private String cate;
    private String tag;
    private int tagNumber;

    public String getCate() {
        return cate;
    }

    public String getTag() {
        return tag;
    }

    public int getTagNumber() {
        return tagNumber;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setTagNumber(int tagNumber) {
        this.tagNumber = tagNumber;
    }
}
