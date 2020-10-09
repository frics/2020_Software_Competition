package kr.ac.ssu.billysrecipe;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import kr.ac.ssu.billysrecipe.ServerConnect.GetTag;
import kr.ac.ssu.billysrecipe.ShoppingListDB.ShoppingListData;
import kr.ac.ssu.billysrecipe.ShoppingListDB.ShoppingListDataBase;
import kr.ac.ssu.billysrecipe.adapter.ShoppingListAdapter;
import kr.ac.ssu.billysrecipe.adapter.TagListAdapter;

public class ShopingListFragment extends Fragment {
    private int tagNumber;
    private EditText tag;
    private RecyclerView recyclerView;
    private ShoppingListAdapter adapter;
    private ArrayList<TagListAdapter.Item> taglist;
    private ArrayList<ShoppingListAdapter.Item> items;
    private ShoppingListDataBase db;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shoppinglist, container, false);
        ImageView addButton = v.findViewById(R.id.content2_add);
        recyclerView = v.findViewById(R.id.content2_recycler);
        db = Room.databaseBuilder(getContext(), ShoppingListDataBase.class, "shoppinglist.db").allowMainThreadQueries().build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showAddDialog(getContext());
            }
        });
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        setData();
    }

    void setData(){
        items = new ArrayList<>();
        List<ShoppingListData> dbData = db.Dao().getAll();

        for(int i = 0; i < dbData.size(); i++){
            ShoppingListAdapter.Item item = new ShoppingListAdapter.Item();
            item.setName(dbData.get(i).getName());
            item.setTag(dbData.get(i).getTag());
            item.setTagNumber(dbData.get(i).getTagNumber());
            items.add(item);
        }
        adapter = new ShoppingListAdapter(items, getContext());
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                db.Dao().delete(db.Dao().findData(items.get(viewHolder.getLayoutPosition()).getName()));
                items.remove(viewHolder.getLayoutPosition());
                adapter.notifyItemRemoved(viewHolder.getLayoutPosition());
                //adapter.notifyDataSetChanged();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    void showAddDialog(final Context context)
    {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_dialog, null);
        tag = (EditText)view.findViewById(R.id.add_dialog_tag);
        final EditText material = (EditText)view.findViewById(R.id.add_dialog_material);
        final Button okay = (Button)view.findViewById(R.id.add_dialog_okay);
        final ImageButton close = (ImageButton)view.findViewById(R.id.add_dialog_close);
        TextView title = view.findViewById(R.id.add_dialog_title);
        title.setText("장바구니 추가");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
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
                    ShoppingListData dbdata = new ShoppingListData();
                    dbdata.setTag(tag.getText().toString());
                    dbdata.setName(material.getText().toString());
                    dbdata.setTagNumber(tagNumber);
                    if(db.Dao().findData(dbdata.getName()) == null)
                        db.Dao().insert(dbdata);
                    else
                        Toast.makeText(context,"이미 같은이름의 재료가 등록되어 있습니다.",Toast.LENGTH_SHORT).show();
                    setData();
                    dialog.dismiss();
                }
            }
        });
        tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tag담을 리스트 초기화
                taglist = new ArrayList<>();
                GetTag.OnTaskCompleted listener = new GetTag.OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(String str) {
                        showTagDialog(getContext());
                    }
                    @Override
                    public void onTaskFailure(String str) {
                        Log.e("RefrigeratorFragment","Task Failure!");
                    }
                };
                //백그라운드 수행(완료시 progressDialog 종료)
                new GetTag(taglist,listener).execute();

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
        final TagListAdapter adapter = new TagListAdapter(context, taglist, TagListAdapter.ADD);

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
                tag.setText(adapter.filteredList.get(pos).getTag());
                tagNumber =  adapter.filteredList.get(pos).getTagNumber();
                dialog.dismiss();
            }
        });
    }
}
