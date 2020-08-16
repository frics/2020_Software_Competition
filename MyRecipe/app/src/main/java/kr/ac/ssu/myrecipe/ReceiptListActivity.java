package kr.ac.ssu.myrecipe;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ac.ssu.myrecipe.adapter.ReceiptListAdapter;

public class ReceiptListActivity extends AppCompatActivity {
    private ReceiptListAdapter FoodAdapter;
    private ArrayList<ReceiptListAdapter.Data> foodList;
    private Integer[] iconList = {
            R.drawable.category_1, R.drawable.category_2,
            R.drawable.category_3, R.drawable.category_4,
            R.drawable.category_5, R.drawable.category_6,
            R.drawable.category_7, R.drawable.category_8,
            R.drawable.category_9, R.drawable.category_10,
            R.drawable.category_11, R.drawable.category_12,
            R.drawable.category_13, R.drawable.category_14,
            R.drawable.category_15, R.drawable.ic_question_24dp
    };
    private String[] textList = {
            "과일", "채소","쌀/잡곡","견과/건과",
            "축산/계란", "수산물/건어물","생수/음료","커피/차",
            "초콜릿/시리얼", "면/통조림","반찬/샐러드","냉동/간편요리",
            "유제품", "가루/오일","소스", "카테고리 없음"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_list);
        RecyclerView food = (RecyclerView)findViewById(R.id.receipt_foodlist);
        Context context = this;

        //데이터 생성
        MakeData();
        //음식리스트 초기화
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        food.setLayoutManager(layoutManager1);
        FoodAdapter = new ReceiptListAdapter(foodList,context);
        food.setAdapter(FoodAdapter);
        //닫기버튼 활성화
        ImageView close = (ImageView)findViewById(R.id.receipt_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void MakeData()
    {
        foodList = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            ReceiptListAdapter.Data data = new ReceiptListAdapter.Data();
            data.setCategory(iconList[i]);
            data.setName(textList[i]);
            data.setCount(i);
            data.setPrice((i + 1) * 1000);
            foodList.add(data);
        }
        ReceiptListAdapter.Data line = new ReceiptListAdapter.Data();
        line.setName("-----");
        foodList.add(line);
        for(int i = 0; i < 5; i++){
            ReceiptListAdapter.Data data = new ReceiptListAdapter.Data();
            data.setCategory(iconList[15]);
            data.setName(textList[15]);
            data.setCount(i);
            data.setPrice((i + 1) * 1000);
            foodList.add(data);
        }
    }
}