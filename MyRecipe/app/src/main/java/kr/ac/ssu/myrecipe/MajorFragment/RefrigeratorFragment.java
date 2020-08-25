package kr.ac.ssu.myrecipe.MajorFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.ReceiptListActivity;
import kr.ac.ssu.myrecipe.RefrigerRatorDB.RefrigeratorData;
import kr.ac.ssu.myrecipe.RefrigerRatorDB.RefrigeratorDataBase;
import kr.ac.ssu.myrecipe.RefrigerRatorDB.RefrigeratorDelete;
import kr.ac.ssu.myrecipe.adapter.RefrigeratorListAdapter;
import kr.ac.ssu.myrecipe.adapter.TagListAdapter;

public class RefrigeratorFragment extends Fragment {
    private RefrigeratorListAdapter adapter;
    private RecyclerView recyclerview;
    private EditText searching_table;
    private List<RefrigeratorListAdapter.Item> data;
    private ImageButton search_button;
    private ImageButton nested_close;
    private LinearLayout nested_layout;
    private EditText tag;
    private TextView category;
    private RefrigeratorDataBase db;
    private ItemTouchHelper.SimpleCallback simpleCallback;
    private InputMethodManager imm;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_refrigerator, container, false);
        final Context context = getContext();
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        recyclerview = (RecyclerView)v.findViewById(R.id.refri_recycler);
        search_button = (ImageButton)v.findViewById(R.id.refri_search);
        nested_layout = (LinearLayout)v.findViewById(R.id.refri_nested_layout);
        searching_table = (EditText)v.findViewById(R.id.refri_search_table);
        nested_close = (ImageButton)v.findViewById(R.id.refri_nested_close);
        recyclerview.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        db = Room.databaseBuilder(getContext(),RefrigeratorDataBase.class, "refrigerator.db").allowMainThreadQueries().build();
        ImageButton add_button = (ImageButton)v.findViewById(R.id.refri_add);
        ImageButton temp = (ImageButton)v.findViewById(R.id.refri_temp);

        //swipe동작 리스너 초기화
        setSimpleCallback();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        //리사이클러뷰에 swipehelper장착
        itemTouchHelper.attachToRecyclerView(recyclerview);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nested_layout.setVisibility(View.VISIBLE);
                search_button.setVisibility(View.GONE);
                searching_table.requestFocus();
                imm.showSoftInput(searching_table, 0);
            }
        });
        nested_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nested_layout.getVisibility() == View.VISIBLE){
                    nested_layout.setVisibility(View.INVISIBLE);
                    search_button.setVisibility(View.VISIBLE);
                    imm.hideSoftInputFromWindow(searching_table.getWindowToken(), 0);
                    searching_table.setText("");
                }
            }
        });
        //add버튼 리스너 장착
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog(context);
            }
        });
        //영수증 임시버튼
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReceiptListActivity.class);
                startActivity(intent);
            }
        });
        searching_table.addTextChangedListener(new TextWatcher() {
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
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setRecyclerviewData();
    }

    public void setRecyclerviewData(){
        data = new ArrayList<>();
        List<RefrigeratorData> dbData = db.Dao().sortData();
        String category = "null";
        for(int i = 0; i < dbData.size(); i++){
            //Log.e("RefrigeratorFragment",dbData.get(i).getName() + " " + dbData.get(i).getTag());
            if(!category.equals(dbData.get(i).getCategory())){
                category = dbData.get(i).getCategory();
                data.add(new RefrigeratorListAdapter.Item(RefrigeratorListAdapter.HEADER, category));
                data.add(new RefrigeratorListAdapter.Item(RefrigeratorListAdapter.CHILD, dbData.get(i).getName()));
            }
            else
                data.add(new RefrigeratorListAdapter.Item(RefrigeratorListAdapter.CHILD, dbData.get(i).getName()));
        }
        //Log.e("RefrigeratorFragment","=========================");
        adapter = new RefrigeratorListAdapter(data);
        recyclerview.setAdapter(adapter);
    }

    void showAddDialog(final Context context)
    {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_dialog, null);
        tag = (EditText)view.findViewById(R.id.add_dialog_tag);
        category = (TextView)view.findViewById(R.id.add_dialog_category);
        final EditText material = (EditText)view.findViewById(R.id.add_dialog_material);
        final Button okay = (Button)view.findViewById(R.id.add_dialog_okay);
        final ImageButton close = (ImageButton)view.findViewById(R.id.add_dialog_close);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tag.getText().toString().length() == 0)
                    Toast.makeText(context,"태그를 선택해주세요.",Toast.LENGTH_SHORT).show();
                else if(material.getText().toString().length() == 0)
                    Toast.makeText(context,"재료명을 입력해주세요.",Toast.LENGTH_SHORT).show();
                else{
                    RefrigeratorDataBase db = Room.databaseBuilder(context,RefrigeratorDataBase.class, "refrigerator.db").allowMainThreadQueries().build();
                    RefrigeratorData dbdata = new RefrigeratorData();
                    dbdata.setTag(tag.getText().toString());
                    dbdata.setName(material.getText().toString());
                    //카테고리 미설정
                    dbdata.setCategory(category.getText().toString());
                    if(db.Dao().findData(dbdata.getName()) == null)
                        db.Dao().insert(dbdata);
                    setRecyclerviewData();
                    dialog.dismiss();
                }
            }
        });
        tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTagDialog(context);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void showTagDialog(Context context)
    {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.tag_dialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        adapter.setOnItemClickListener(new TagListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                tag.setText(adapter.filteredList.get(pos).tag);
                category.setText(adapter.filteredList.get(pos).category);
                dialog.dismiss();
            }
        });
    }

    private void setSimpleCallback()
    {
        simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                if(adapter.getItemViewType(viewHolder.getLayoutPosition()) == 0) {
                    adapter.notifyItemChanged(viewHolder.getLayoutPosition());
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("음식을 삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //db에서 delete
                                RefrigeratorData findData = new RefrigeratorData();
                                findData.setName(adapter.filteredList.get(viewHolder.getLayoutPosition()).text);
                                new RefrigeratorDelete(db.Dao()).execute(findData);
                                int pos = adapter.filteredList.get(viewHolder.getLayoutPosition()).position;
                                Log.e("TEST",pos + " " +viewHolder.getLayoutPosition());
                                //바로위가 HEADER이면
                                if(adapter.getItemViewType(viewHolder.getLayoutPosition()-1) == 0){
                                    //맨마지막이면
                                    if(viewHolder.getLayoutPosition() == adapter.filteredList.size()-1){
                                        adapter.filteredList.remove(viewHolder.getLayoutPosition());
                                        adapter.notifyItemRemoved(viewHolder.getLayoutPosition());
                                        adapter.filteredList.remove(viewHolder.getLayoutPosition()-1);
                                        adapter.notifyItemRemoved(viewHolder.getLayoutPosition()-1);
                                    }
                                    //중간에 끼어있으면
                                    else if(adapter.getItemViewType(viewHolder.getLayoutPosition()+1) == 0){
                                        adapter.filteredList.remove(viewHolder.getLayoutPosition());
                                        adapter.notifyItemRemoved(viewHolder.getLayoutPosition());
                                        adapter.filteredList.remove(viewHolder.getLayoutPosition()-1);
                                        adapter.notifyItemRemoved(viewHolder.getLayoutPosition()-1);
                                    }
                                    //그외
                                    else
                                    {
                                        adapter.filteredList.remove(viewHolder.getLayoutPosition());
                                        adapter.notifyItemRemoved(viewHolder.getLayoutPosition());
                                    }
                                }
                                //중간에 끼어있으면
                                else
                                {
                                    adapter.filteredList.remove(viewHolder.getLayoutPosition());
                                    adapter.notifyItemRemoved(viewHolder.getLayoutPosition());
                                }
                                Log.e("TEST","table : " + searching_table.getText().toString());
                                if(searching_table.getText().toString().equals(""))
                                    return;
                                //unfiltered삭제
                                if(adapter.getUnfilterViewType(pos-1) == 0){
                                    //맨마지막이면
                                    if(pos == adapter.unFilteredlist.size()-1){
                                        adapter.unFilteredlist.remove(pos);
                                        adapter.unFilteredlist.remove(pos-1);
                                    }
                                    //중간에 끼어있으면
                                    else if(adapter.getUnfilterViewType(pos+1) == 0){
                                        adapter.unFilteredlist.remove(pos);
                                        adapter.unFilteredlist.remove(pos-1);
                                    }
                                    //그외
                                    else
                                        adapter.unFilteredlist.remove(pos);
                                }
                                //중간에 끼어있으면
                                else
                                    adapter.unFilteredlist.remove(pos);
                                CharSequence text = searching_table.getText().toString();
                                adapter.getFilter().filter(text);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                adapter.notifyItemChanged(viewHolder.getLayoutPosition());
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        };
    }
}