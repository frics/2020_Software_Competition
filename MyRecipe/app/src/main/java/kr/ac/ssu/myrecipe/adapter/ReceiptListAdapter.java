package kr.ac.ssu.myrecipe.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
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
import androidx.appcompat.app.AppCompatDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.ac.ssu.myrecipe.IconData;
import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.RefrigerRatorDB.RefrigeratorData;
import kr.ac.ssu.myrecipe.RefrigerRatorDB.RefrigeratorDataBase;
import kr.ac.ssu.myrecipe.RefrigerRatorDB.ThreadTask;

public class ReceiptListAdapter extends RecyclerView.Adapter<ReceiptListAdapter.ViewHolder> {
    private ArrayList<Data> mData = null;
    private Context context;
    private int LinePos;
    private IconData iconData;

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
                            if(mData.get(i).getCategory() != R.drawable.ic_question_24dp) {
                                //냉장고 내부 db
                                RefrigeratorData data = new RefrigeratorData();
                                //icon int값에서 text로 변환
                                data.setCategory(iconData.iconTotext.get(mData.get(i).getCategory()));
                                data.setTag(mData.get(i).getTag());
                                data.setName(mData.get(i).getName());
                                list.add(data);
                                //new RefrigeratorInsert(db.Dao()).execute();
                                //서버 전송 json
                                jsonObject.put("category", iconData.iconTotext.get(mData.get(i).category));
                                jsonObject.put("name", mData.get(i).getName());
                                jsonObject.put("tag", mData.get(i).getTag());
                                jsonObject.put("price", mData.get(i).getPrice());
                                wrapObject.put("refrigerator" + (i + 1), jsonObject);
                            }
                        }
                        // 로딩 다이얼로그 설정
                        final AppCompatDialog progressDialog = new AppCompatDialog(context);
                        progressDialog.setCancelable(false);
                        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        progressDialog.setContentView(R.layout.loading_dialog);
                        progressDialog.show();
                        final ImageView img_loading_frame = (ImageView) progressDialog.findViewById(R.id.iv_frame_loading);
                        final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
                        img_loading_frame.post(new Runnable() {
                            @Override
                            public void run() {
                                frameAnimation.start();
                            }
                        });
                        final ImageView text_loading_frame = (ImageView) progressDialog.findViewById(R.id.tv_progress_message);
                        final AnimationDrawable textAnimation = (AnimationDrawable) text_loading_frame.getBackground();
                        img_loading_frame.post(new Runnable() {
                            @Override
                            public void run() {
                                textAnimation.start();
                            }
                        });
                        new ThreadTask(context, db.Dao(), progressDialog, 0).execute(list);
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
            holder.category.setImageResource(item.getCategory());
            holder.category.setBackgroundResource(R.drawable.circle);
            holder.name.setText(item.getName());
            holder.tag.setText(item.getTag());
            holder.price.setText(Integer.toString(item.getPrice()));
            //카테고리 다이얼로그
            holder.category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTagDialog(context, position, holder);
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
        final TagListAdapter adapter = new TagListAdapter(context, dialog);

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
                holder.tag.setText(adapter.filteredList.get(pos).tag);
                holder.categoryString.setText(adapter.filteredList.get(pos).category);
                String pre = item.getTag();
                String post = holder.tag.getText().toString();

                if(pre.equals("태그없음") && !post.equals("태그없음")){
                    Data newdata = new Data();
                    Integer category = IconData.textToicon.get(holder.categoryString.getText().toString());
                    newdata.setCategory(category);
                    newdata.setTag(holder.tag.getText().toString());
                    newdata.setName(item.getName());
                    newdata.setPrice(item.getPrice());
                    //먼저 추가하고
                    mData.add(LinePos,newdata);
                    //한칸 밀렸으니 positon+1삭제
                    mData.remove(position+1);
                    notifyDataSetChanged();
                }
                else if(!pre.equals("태그없음") && post.equals("태그없음"))
                {
                    Data newdata = new Data();
                    newdata.setCategory(R.drawable.ic_question_24dp);
                    newdata.setName(item.getName());
                    newdata.setTag(holder.tag.getText().toString());
                    newdata.setPrice(item.getPrice());
                    //먼저 삭제하고
                    mData.remove(position);
                    //한칸 당겨졌으니 그 자리에 그대로 삽입
                    mData.add(LinePos,newdata);
                    notifyDataSetChanged();
                }
                else{
                    Integer category = IconData.textToicon.get(holder.categoryString.getText().toString());
                    holder.category.setImageResource(category);
                    mData.get(position).setCategory(category);
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
    void addItem(Data data) {
        // 외부에서 item을 추가시킬 함수입니다.
        mData.add(data);
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
            price = itemView.findViewById(R.id.receipt_item_price);
            //구분자 or 데이터
            layout = itemView.findViewById(R.id.receipt_item_layout);
            view = itemView.findViewById(R.id.receipt_item_view);
            constrain = itemView.findViewById(R.id.receipt_item_constrain);
            button = itemView.findViewById(R.id.receipt_item_button);
        }
    }
    public static class Data{
        int category;
        String name;
        String tag;
        int price;

        public int getCategory() {
            return category;
        }
        public String getName() {
            return name;
        }
        public String getTag() {
            return tag;
        }
        public int getPrice() {
            return price;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }
}
