package kr.ac.ssu.myrecipe.ShoppingListDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ShoppingListData {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String tag;
    private String name;
    private int tagNumber;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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
                ", tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
