package kr.ac.ssu.billysrecipe.MajorFragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import kr.ac.ssu.billysrecipe.MainActivity;
import kr.ac.ssu.billysrecipe.R;
import kr.ac.ssu.billysrecipe.RefrigerRatorDB.RefrigeratorData;
import kr.ac.ssu.billysrecipe.RefrigerRatorDB.RefrigeratorDataBase;
import kr.ac.ssu.billysrecipe.ServerConnect.GetTag;
import kr.ac.ssu.billysrecipe.adapter.CategoryAdapter;
import kr.ac.ssu.billysrecipe.adapter.RefrigeratorAdapter;
import kr.ac.ssu.billysrecipe.adapter.TagListAdapter;
import kr.ac.ssu.billysrecipe.recipe.RecipeOrderList;

public class RefrigeratorFragment extends Fragment{
    private ArrayList<ArrayList<RefrigeratorAdapter.Data>> dataList;
    private RecyclerView categoryRecycler;
    private RecyclerView recyclerview;
    private EditText tag;
    private String cate;
    private int tagNumber;
    private String filterString;
    private RefrigeratorAdapter refrigeratorAdapter;
    private CategoryAdapter categoryAdapter;
    private RefrigeratorDataBase db;
    private InputMethodManager imm;
    private ImageView image;
    private ArrayList<TagListAdapter.Item> taglist;
    private ArrayList<String> categoryList;
    private Context context;
    private SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_refrigerator, container, false);

        MainActivity.isRecipeSetting= false; // 툴바 관련 변수
        context = getContext();
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        recyclerview = (RecyclerView)v.findViewById(R.id.refri_recycler);
        categoryRecycler = (RecyclerView)v.findViewById(R.id.refri_recycler_horizon);
        image = (ImageView)v.findViewById(R.id.refri_image);
        recyclerview.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        categoryRecycler.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false));
        db = Room.databaseBuilder(getContext(),RefrigeratorDataBase.class, "refrigerator.db").allowMainThreadQueries().build();

        setHasOptionsMenu(true);
        return v;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.refrigerator_menu,menu);
        searchView = (SearchView) menu.findItem(R.id.action_ref_search).getActionView();
        searchView.setQueryHint("재료명을 입력하세요");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                refrigeratorAdapter.getFilter().filter(s);
                filterString = s;
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                filterString = "";
                return false;
            }
        });
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId){
            case R.id.action_ref_search:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
                break;
            case R.id.ref_add:
                showAddDialog(context);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    public void setData(){
        ArrayList<RefrigeratorAdapter.Data> items = new ArrayList<>();
        List<RefrigeratorData> dbData = db.Dao().sortData();
        categoryList = new ArrayList<>();

        for(int i = 0; i < dbData.size(); i++){
            RefrigeratorAdapter.Data data = new RefrigeratorAdapter.Data();
            data.category = dbData.get(i).getCategory();
            data.tag = dbData.get(i).getTag();
            data.name = dbData.get(i).getName();
            data.tagNumber = dbData.get(i).getTagNumber();
            items.add(data);
        }
        int position = 0;
        String category = "null";
        //전체카테고리 생성
        dataList = new ArrayList<ArrayList<RefrigeratorAdapter.Data>>();
        dataList.add(new ArrayList<RefrigeratorAdapter.Data>());
        categoryList.add("전체");
        for(int i = 0; i < items.size(); i++){
            if(!category.equals(items.get(i).category)){
                category = items.get(i).category;
                dataList.add(new ArrayList<RefrigeratorAdapter.Data>());
                categoryList.add(items.get(i).category);
                position++;
            }
            dataList.get(0).add(items.get(i));
            dataList.get(position).add(items.get(i));
        }
        refrigeratorAdapter = new RefrigeratorAdapter(getContext(), dataList.get(0),dataList);
        recyclerview.setAdapter(refrigeratorAdapter);
        categoryAdapter = new CategoryAdapter(categoryList,getContext());
        categoryRecycler.setAdapter(categoryAdapter);
        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                int prePositon = categoryAdapter.getClick_position();
                categoryAdapter.setClick_position(position);
                categoryAdapter.notifyItemChanged(prePositon);
                categoryAdapter.notifyItemChanged(position);
                refrigeratorAdapter.changeData(dataList.get(position));
                refrigeratorAdapter.getFilter().filter(filterString);
            }
        });
        refrigeratorAdapter.setOnItemClickListener(new RefrigeratorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                showDeleteDialog(position);
            }
        });
        if(dbData.size() == 0){
            recyclerview.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
            return;
        }
        else{
            recyclerview.setVisibility(View.VISIBLE);
            image.setVisibility(View.GONE);
        }

        RecipeOrderList.RenewOrder(getContext()); // 레시피 갱신
    }

    void showAddDialog(final Context context)
    {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_dialog, null);
        tag = (EditText)view.findViewById(R.id.add_dialog_tag);
        final EditText material = (EditText)view.findViewById(R.id.add_dialog_material);
        final Button okay = (Button)view.findViewById(R.id.add_dialog_okay);
        final ImageButton close = (ImageButton)view.findViewById(R.id.add_dialog_close);
        TextView title = (TextView)view.findViewById(R.id.add_dialog_title);

        title.setText("재료추가");

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
                    RefrigeratorDataBase db = Room.databaseBuilder(context,RefrigeratorDataBase.class, "refrigerator.db").allowMainThreadQueries().build();
                    RefrigeratorData dbdata = new RefrigeratorData();
                    dbdata.setTag(tag.getText().toString());
                    dbdata.setName(material.getText().toString());
                    dbdata.setTagNumber(tagNumber);
                    dbdata.setCategory(cate);
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
                cate =  adapter.filteredList.get(pos).getCategory();
                tagNumber =  adapter.filteredList.get(pos).getTagNumber();
                tag.setText(adapter.filteredList.get(pos).getTag());
                dialog.dismiss();
            }
        });
    }

    public void showDeleteDialog(final int position)
    {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.delete_dialog, null);
        TextView category = (TextView)view.findViewById(R.id.delete_dialog_category);
        TextView tag = (TextView) view.findViewById(R.id.delete_dialog_tag);
        TextView material = (TextView) view.findViewById(R.id.delete_dialog_material);
        final Button okay = (Button)view.findViewById(R.id.delete_dialog_okay);
        final ImageButton close = (ImageButton)view.findViewById(R.id.delete_dialog_close);

        category.setText(refrigeratorAdapter.filteredList.get(position).category);
        tag.setText(refrigeratorAdapter.filteredList.get(position).tag);
        material.setText(refrigeratorAdapter.filteredList.get(position).name);

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
                String name = refrigeratorAdapter.filteredList.get(position).name;
                //백그라운드 삭제
                //new RefrigeratorDelete(db.Dao()).execute(data);
                RefrigeratorData result = db.Dao().findData(name);
                db.Dao().delete(result);
                refrigeratorAdapter.filteredList.remove(position);
                //필터된 리스트와 원본 리스트가 다르면
                if (refrigeratorAdapter.unFilteredlist != refrigeratorAdapter.filteredList) {
                    //원본리스트에서도 삭제
                    for (int i = 0; i < refrigeratorAdapter.unFilteredlist.size(); i++) {
                        if (name.equals(refrigeratorAdapter.unFilteredlist.get(i).name)) {
                            refrigeratorAdapter.unFilteredlist.remove(i);
                            break;
                        }
                    }
                }
                //전체 카테고리가 아닐경우
                if (refrigeratorAdapter.unFilteredlist != refrigeratorAdapter.allItems.get(0)) {
                    //전체 카테고리 아이템도 삭제
                    for (int i = 0; i < refrigeratorAdapter.allItems.size(); i++) {
                        if (name.equals(refrigeratorAdapter.allItems.get(0).get(i).name)) {
                            refrigeratorAdapter.allItems.get(0).remove(i);
                            break;
                        }
                    }
                    //선택된 카테고리가 0이되었으면
                    if (refrigeratorAdapter.allItems.get(categoryAdapter.getClick_position()).size() == 0) {
                        //allitem에 dataset에서 삭제
                        refrigeratorAdapter.allItems.remove(categoryAdapter.getClick_position());
                        //0인 카테고리를 리스트에서 삭제
                        categoryList.remove(categoryAdapter.getClick_position());
                        int prePositon = categoryAdapter.getClick_position();
                        //데이터변경
                        categoryAdapter.setClick_position(0);
                        categoryAdapter.notifyDataSetChanged();
                        //전체카테고리로 이동
                        refrigeratorAdapter.changeData(dataList.get(0));
                        refrigeratorAdapter.getFilter().filter(filterString);
                    }
                }
                //전체 카테고리 일경우
                else {
                    boolean exit = false;
                    for (int i = 1; i < refrigeratorAdapter.allItems.size(); i++) {
                        for (int j = 0; j < refrigeratorAdapter.allItems.get(i).size(); j++) {
                            if (name.equals(refrigeratorAdapter.allItems.get(i).get(j).name)) {
                                //해당하는 아이템 삭제
                                refrigeratorAdapter.allItems.get(i).remove(j);
                                //i번째 카테고리가 비었으면
                                if (refrigeratorAdapter.allItems.get(i).size() == 0) {
                                    //데이터셋 삭제
                                    refrigeratorAdapter.allItems.remove(i);
                                    //0인 카테고리를 리스트에서 삭제
                                    categoryList.remove(i);
                                    //데이터변경
                                    categoryAdapter.notifyDataSetChanged();
                                }
                                exit = true;
                                break;
                            }
                            if (exit)
                                break;
                        }
                    }
                }
                //전체 카테고리가 0이면
                if (refrigeratorAdapter.allItems.get(0).size() == 0) {
                    recyclerview.setVisibility(View.GONE);
                    image.setVisibility(View.VISIBLE);
                }
                refrigeratorAdapter.notifyDataSetChanged();
                RecipeOrderList.RenewOrder(getContext()); // 레시피 갱신
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) { // 툴바 관련 메소드
        if (!MainActivity.isRecipeSetting) {
            for (int i = 0; i < menu.size(); i++) {
                if (menu.getItem(i).getTitle().toString().compareTo("list_setting") == 0) {
                    menu.getItem(i).setEnabled(false);
                    menu.getItem(i).setVisible(false);
                }
            }
        }
    }
}