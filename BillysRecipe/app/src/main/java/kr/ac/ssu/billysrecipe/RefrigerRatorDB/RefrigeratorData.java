package kr.ac.ssu.billysrecipe.RefrigerRatorDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RefrigeratorData {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String category;
    private String tag;
    private String name;
    private int tagNumber;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }

    public int getTagNumber() {
        return tagNumber;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTagNumber(int tagNumber) {
        this.tagNumber = tagNumber;
    }

    @Override
    public String toString() {
        return "RefrigeratorData{" +
                "id=" + id +
                ", category=" + category +
                ", tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
