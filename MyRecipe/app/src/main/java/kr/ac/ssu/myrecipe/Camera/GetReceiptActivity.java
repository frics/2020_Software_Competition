package kr.ac.ssu.myrecipe.Camera;

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

import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.adapter.ReceiptListAdapter;

public class GetReceiptActivity extends AppCompatActivity {
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
            ArrayList<ReceiptListAdapter.Data> noCategoryList = new ArrayList<>();
            for(int i = 0; i < jsonObject.length();i++){
                JSONObject object = jsonObject.getJSONObject("classification"+i);
                ReceiptListAdapter.Data data = new ReceiptListAdapter.Data();
                data.setName(object.getString("name"));
                data.setTag(object.getString("tag"));
                if(data.getTag().equals("태그없음")){
                    //임시로 이 아이콘 사용
                    data.setCategory("카테고리 없음");
                    //임시로 TagNumber 0사용
                    data.setTagNumber(0);
                    //태그없음에 해당하는 tagNumber
                    noCategoryList.add(data);
                }
                else {
                    data.setCategory(object.getString("cate"));
                    //임시로 TagNumber 0사용
                    data.setTagNumber(object.getInt("tagNumber"));
                    foodList.add(data);
                }
            }
            //구분선 추가
            ReceiptListAdapter.Data line = new ReceiptListAdapter.Data();
            line.setName("-----");
            //라인 구분선 부분의 카테고리도 카테고리없음으로 설정(db저장시 거르기위함)
            line.setCategory("카테고리 없음");
            foodList.add(line);
            //카테고리없음 아이템 추가
            for(int i = 0 ; i < noCategoryList.size(); i++)
                foodList.add(noCategoryList.get(i));
            //마지막 화인버튼 라인 추가
            ReceiptListAdapter.Data data = new ReceiptListAdapter.Data();
            data.setName("last");
            //마지막 확인버튼 라인역시 카테고리 없음으로 설정(db저장시 거르기위함)
            data.setCategory("카테고리 없음");
            foodList.add(data);

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}