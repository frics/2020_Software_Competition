package kr.ac.ssu.myrecipe.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.ac.ssu.myrecipe.UI.IconData;
import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.Camera.ReceiptListActivity;
import kr.ac.ssu.myrecipe.RefrigerRatorDB.RefrigeratorData;
import kr.ac.ssu.myrecipe.RefrigerRatorDB.RefrigeratorDataBase;
import kr.ac.ssu.myrecipe.RefrigerRatorDB.ThreadTask;
import kr.ac.ssu.myrecipe.ServerConnect.GetTag;

public class ReceiptListAdapter extends RecyclerView.Adapter<ReceiptListAdapter.ViewHolder> {
    private ArrayList<Data> mData = null;
    private Context context;
    private int LinePos;
    private IconData iconData;
    private ArrayList<TagListAdapter.Item> taglist;

    public ReceiptListAdapter(ArrayList<Data> list, Context context) {
        mData = list;
        this.context = context;
        iconData = new IconData();
    }

    @Override
    public ReceiptListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.receipt_items, parent, false) ;
        ReceiptListAdapter.ViewHolder vh = new ReceiptListAdapter.ViewHolder(view) ;
        return vh ;
    }
    @Override
    public void onBindViewHolder(final ReceiptListAdapter.ViewHolder holder, final int position) {

        final Data item = mData.get(position) ;

        //카테고리 구분된것과 안된것 사이 구분선
        if(item.getName() == "-----"){
            LinePos = position;
            holder.layout.setVisibility(View.GONE);
            holder.constrain.setVisibility(View.GONE);
            holder.view.setVisibility(View.VISIBLE);
        }
        //마지막 확인버튼 구분선
        else if(position == mData.size()-1){
            holder.layout.setVisibility(View.GONE);
            holder.constrain.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    JSONObject wrapObject = new JSONObject();
                    try{
                        RefrigeratorDataBase db = Room.databaseBuilder(context,RefrigeratorDataBase.class, "refrigerator.db").build();
                        ArrayList<RefrigeratorData> list = new ArrayList<>();
                        for(int i = 0; i < mData.size(); i++){
                            JSONObject jsonObject = new JSONObject();
                            //카테고리가 설정되었으면 서버로 보내거나 db에 저장
                            if(!mData.get(i).getCategory().equals("카테고리 없음")) {
                                //냉장고 내부 db
                                RefrigeratorData data = new RefrigeratorData();
                                //icon int값에서 text로 변환
                                data.setCategory(mData.get(i).getCategory());
                                data.setTag(mData.get(i).getTag());
                                data.setName(mData.get(i).getName());
                                data.setTagNumber(mData.get(i).getTagNumber());
                                list.add(data);
                                //new RefrigeratorInsert(db.Dao()).execute();
                                //서버 전송 json
                                jsonObject.put("category", mData.get(i).category);
                                jsonObject.put("name", mData.get(i).getName());
                                jsonObject.put("tag", mData.get(i).getTag());
                                wrapObject.put("refrigerator" + (i + 1), jsonObject);
                            }
                        }
                        ThreadTask.OnTaskCompleted listener = new ThreadTask.OnTaskCompleted() {
                            @Override
                            public void onTaskCompleted(String str) {
                                ((ReceiptListActivity)context).finish();
                            }
                            @Override
                            public void onTaskFailure(String str) {
                                Log.e("RefrigeratorFragment","Task Failure!");
                            }
                        };
                        new ThreadTask(db.Dao(), listener, ThreadTask.RECIEPT).execute(list);
                        //new ThreadTask(context, db.Dao(), progressDialog, 0).execute(list);
                        Log.e("test",wrapObject.toString());
                        //실제 데이터 전송 메소드;
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            holder.layout.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.GONE);
            holder.constrain.setVisibility(View.GONE);
            holder.category.setImageResource(IconData.textToicon.get(item.getCategory()));
            holder.category.setBackgroundResource(R.drawable.circle);
            holder.name.setText(item.getName());
            holder.tag.setText(item.getTag());
            //카테고리 다이얼로그
            holder.category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //tag담을 리스트 초기화
                    taglist = new ArrayList<>();
                    GetTag.OnTaskCompleted listener = new GetTag.OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted(String str) {
                            showTagDialog(context,position,holder);
                        }
                        @Override
                        public void onTaskFailure(String str) {
                            Log.e("RefrigeratorFragment","Task Failure!");
                        }
                    };
                    //백그라운드 수행(완료시 progressDialog 종료)
                    new GetTag(taglist,listener).execute();
                    //showTagDialog(context, position, holder);
                }
            });
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final EditText et = new EditText(context);
                    et.setText(holder.name.getText());
                    final AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
                    alt_bld.setMessage("재료명을 입력해주세요")
                            .setView(et)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String value = et.getText().toString();
                                    holder.name.setText(value);
                                    mData.get(position).setName(value);
                                }
                            });
                    AlertDialog alert = alt_bld.create();
                    alert.show();
                }
            });
        }
    }

    private void showTagDialog(final Context context, final int position, final ViewHolder holder)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tag_dialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        final Data item = mData.get(position) ;
        final AlertDialog dialog = builder.create();
        final EditText editText = (EditText)view.findViewById(R.id.tag_edittext);
        final RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.tag_recylcerview);
        final TagListAdapter adapter = new TagListAdapter(context,taglist,TagListAdapter.RECEIPT);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        adapter.setOnItemClickListener(new TagListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                IconData iconData = new IconData();
                holder.tag.setText(adapter.filteredList.get(pos).getTag());
                holder.categoryString.setText(adapter.filteredList.get(pos).getCategory());
                String pre = item.getTag();
                String post = holder.tag.getText().toString();

                if(pre.equals("태그없음") && !post.equals("태그없음")){
                    Data newdata = new Data();
                    newdata.setCategory(holder.categoryString.getText().toString());
                    newdata.setTag(holder.tag.getText().toString());
                    newdata.setName(item.getName());
                    //먼저 추가하고
                    mData.add(LinePos,newdata);
                    //한칸 밀렸으니 positon+1삭제
                    mData.remove(position+1);
                    notifyDataSetChanged();
                }
                else if(!pre.equals("태그없음") && post.equals("태그없음"))
                {
                    Data newdata = new Data();
                    newdata.setCategory("카테고리 없음");
                    newdata.setName(item.getName());
                    newdata.setTag(holder.tag.getText().toString());
                    //먼저 삭제하고
                    mData.remove(position);
                    //한칸 당겨졌으니 그 자리에 그대로 삽입
                    mData.add(LinePos,newdata);
                    notifyDataSetChanged();
                }
                else{
                    Integer category = iconData.textToicon.get(holder.categoryString.getText().toString());
                    holder.category.setImageResource(category);
                    mData.get(position).setCategory(holder.categoryString.getText().toString());
                    mData.get(position).setTagNumber(adapter.filteredList.get(pos).getTagNumber());
                }
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView category;
        TextView name;
        TextView categoryString;
        TextView tag;
        TextView price;
        LinearLayout layout;
        View view;
        ConstraintLayout constrain;
        TextView sum;
        Button button;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            category = itemView.findViewById(R.id.receipt_item_category);
            categoryString = itemView.findViewById(R.id.receipt_item_categoryString);
            name = itemView.findViewById(R.id.receipt_item_name);
            tag = itemView.findViewById(R.id.receipt_item_tag);
            //구분자 or 데이터
            layout = itemView.findViewById(R.id.receipt_item_layout);
            view = itemView.findViewById(R.id.receipt_item_view);
            constrain = itemView.findViewById(R.id.receipt_item_constrain);
            button = itemView.findViewById(R.id.receipt_item_button);
        }
    }
    public static class Data{
        String category;
        String name;
        String tag;
        int tagNumber;

        public String getCategory() {
            return category;
        }

        public int getTagNumber() {
            return tagNumber;
        }

        public String getName() {
            return name;
        }
        public String getTag() {
            return tag;
        }


        public void setCategory(String category) {
            this.category = category;
        }

        public void setTagNumber(int tagNumber) {
            this.tagNumber = tagNumber;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

    }
}
