package kr.ac.ssu.myrecipe;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import kr.ac.ssu.myrecipe.adapter.ReceiptListAdapter;

public class ReceiptListActivity extends AppCompatActivity {
    private ReceiptListAdapter FoodAdapter;
    private ArrayList<ReceiptListAdapter.Data> foodList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_list);
        RecyclerView food = (RecyclerView)findViewById(R.id.receipt_foodlist);
        Context context = this;

        //데이터 생성
        jsonParsing(getJsonString());
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
    private String getJsonString()
    {
        String json = "";
        try {
            InputStream is = getAssets().open("jsonResult.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return json;
    }
    private void jsonParsing(String json)
    {
        int sum = 0;
        foodList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(json);
            for(int i = 1; i <= jsonObject.length();i++){
                JSONObject object = jsonObject.getJSONObject("refrigerator"+i);
                ReceiptListAdapter.Data data = new ReceiptListAdapter.Data();
                if(i == 1){
                    //라인 구분선
                    ReceiptListAdapter.Data line = new ReceiptListAdapter.Data();
                    line.setName("-----");
                    //라인 구분선 부분의 카테고리도 카테고리없음으로 설정(db저장시 거르기위함)
                    line.setCategory(R.drawable.ic_question_24dp);
                    line.setPrice(sum);
                    foodList.add(line);
                }
                //임시
                data.setCategory(R.drawable.ic_question_24dp);
                data.setName(object.getString("ingredient"));
                data.setTag("태그없음");
                //data.setCount(Integer.parseInt(object.getString("amount")));
                data.setPrice(Integer.parseInt(object.getString("price")));
                sum += Integer.parseInt(object.getString("price"));
                foodList.add(data);
            }
            ReceiptListAdapter.Data data = new ReceiptListAdapter.Data();
            data.setName("last");
            //마지막 확인버튼 라인역시 카테고리 없음으로 설정(db저장시 거르기위함)
            data.setCategory(R.drawable.ic_question_24dp);
            data.setPrice(sum);
            foodList.add(data);

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}