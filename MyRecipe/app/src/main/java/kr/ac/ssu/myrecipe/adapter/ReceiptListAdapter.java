package kr.ac.ssu.myrecipe.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ac.ssu.myrecipe.R;

public class ReceiptListAdapter extends RecyclerView.Adapter<ReceiptListAdapter.ViewHolder> {
    private ArrayList<Data> mData = null;
    private Context context;
    private int LinePos;

    public ReceiptListAdapter(ArrayList<Data> list, Context context) {
        mData = list;
        this.context = context;
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

        if(item.getName() == "-----"){
            LinePos = position;
            holder.layout.setVisibility(View.GONE);
            holder.view.setVisibility(View.VISIBLE);
        }
        else {
            holder.layout.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.GONE);
            holder.category.setImageResource(item.getCategory());
            holder.category.setBackgroundResource(R.drawable.circle);
            holder.name.setText(item.getName());
            holder.count.setText(Integer.toString(item.getCount()));
            holder.price.setText(Integer.toString(item.getPrice()));
            holder.category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View v = inflater.inflate(R.layout.alert_dialog, null);
                    final GridView gridView = (GridView) v.findViewById(R.id.gridview_alterdialog_list);
                    final Button button = (Button) v.findViewById(R.id.button_alerdialog_button);
                    //visabe = true ->16번 카테고리없음 활성화
                    final AlertDialogImageAdapter adapter = new AlertDialogImageAdapter(context, true);
                    gridView.setAdapter(adapter);
                    gridView.setNumColumns(4);
                    gridView.setGravity(Gravity.CENTER);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(v);
                    final AlertDialog dialog = builder.create();
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                            //항목없음으로 변경했을때
                            if(item.getCategory() != R.drawable.ic_question_24dp && pos == 15){
                                Data newdata = new Data();
                                newdata.setCategory(R.drawable.ic_question_24dp);
                                newdata.setName(item.getName());
                                newdata.setPrice(item.getPrice());
                                newdata.setCount(item.count);
                                //먼저 삭제하고
                                mData.remove(position);
                                //한칸 당겨졌으니 그 자리에 그대로 삽입
                                mData.add(LinePos,newdata);
                                notifyDataSetChanged();
                            }
                            else if(item.getCategory() == R.drawable.ic_question_24dp && pos != 15){
                                Data newdata = new Data();
                                newdata.setCategory(adapter.iconList[pos]);
                                newdata.setName(item.getName());
                                newdata.setPrice(item.getPrice());
                                newdata.setCount(item.count);
                                //먼저 추가하고
                                mData.add(LinePos,newdata);
                                //한칸 밀렸으니 positon+1삭제
                                mData.remove(position+1);
                                notifyDataSetChanged();
                            }
                            holder.category.setImageResource(adapter.iconList[pos]);
                            dialog.dismiss();
                        }
                    });
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });
        }
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
        TextView count;
        TextView price;
        LinearLayout layout;
        View view;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            category = itemView.findViewById(R.id.receipt_item_category);
            name = itemView.findViewById(R.id.receipt_item_name);
            count = itemView.findViewById(R.id.receipt_item_count);
            price = itemView.findViewById(R.id.receipt_item_price);
            //구분자 or 데이터
            layout = itemView.findViewById(R.id.receipt_item_layout);
            view = itemView.findViewById(R.id.receipt_item_view);
        }
    }
    public static class Data{
        int category;
        String name;
        int count;
        int price;

        public int getCategory() {
            return category;
        }
        public String getName() {
            return name;
        }
        public int getCount() {
            return count;
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

        public void setCount(int count) {
            this.count = count;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }
}
