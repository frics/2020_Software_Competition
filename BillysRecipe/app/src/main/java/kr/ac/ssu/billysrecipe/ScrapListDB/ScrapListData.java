package kr.ac.ssu.billysrecipe.ScrapListDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ScrapListData {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int totalNum;
    private int Scraped;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public int getScraped(){
        return Scraped;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public void setScraped(int Scraped){
        this.Scraped = Scraped;
    }

    @Override
    public String toString() {
        return "RefrigeratorData{" +
                "id=" + id +
                ", totalNum='" + totalNum + '\'' +
                ", scrap='" + Scraped + '\'' +
                '}';
    }
}
