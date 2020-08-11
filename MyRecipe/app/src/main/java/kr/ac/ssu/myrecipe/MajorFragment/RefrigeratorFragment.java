package kr.ac.ssu.myrecipe.MajorFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.ssu.myrecipe.AddActivity;
import kr.ac.ssu.myrecipe.R;
import kr.ac.ssu.myrecipe.ui.ExpandableListAdapter;

public class RefrigeratorFragment extends Fragment {
    private ExpandableListAdapter adapter;
    private RecyclerView recyclerview;
    private ImageButton add_button;
    private List<ExpandableListAdapter.Item> data = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_refrigerator, container, false);
        recyclerview = (RecyclerView)v.findViewById(R.id.refri_recycler);
        add_button = (ImageButton)v.findViewById(R.id.refri_add);
        recyclerview.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        setRecyclerviewData();
        recyclerview.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
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
                                //deleteItem(viewHolder);
                                //바로위가 HEADER이면
                                if(adapter.getItemViewType(viewHolder.getLayoutPosition()-1) == 0){
                                    //맨마지막이면
                                    if(viewHolder.getLayoutPosition() == data.size()-1){
                                        data.remove(viewHolder.getLayoutPosition());
                                        adapter.notifyItemRemoved(viewHolder.getLayoutPosition());
                                        data.remove(viewHolder.getLayoutPosition()-1);
                                        adapter.notifyItemRemoved(viewHolder.getLayoutPosition()-1);
                                    }
                                    //중간에 끼어있으면
                                    else if(adapter.getItemViewType(viewHolder.getLayoutPosition()+1) == 0){
                                        data.remove(viewHolder.getLayoutPosition());
                                        adapter.notifyItemRemoved(viewHolder.getLayoutPosition());
                                        data.remove(viewHolder.getLayoutPosition()-1);
                                        adapter.notifyItemRemoved(viewHolder.getLayoutPosition()-1);
                                    }
                                    //그외
                                    else
                                    {
                                        data.remove(viewHolder.getLayoutPosition());
                                        adapter.notifyItemRemoved(viewHolder.getLayoutPosition());
                                    }
                                }
                                //중간에 끼어있으면
                                else
                                {
                                    data.remove(viewHolder.getLayoutPosition());
                                    adapter.notifyItemRemoved(viewHolder.getLayoutPosition());
                                }
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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerview);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
                // getActivity().finish();
            }
        });
        return v;
    }
    public void setRecyclerviewData(){
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, " 과일"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, " 나주배"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, " 아삭 세척 사과"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, " 농협 하우스 감귤"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, " 깨비농원 황금향"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, " 제스프리 골든 키위"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, " 채소"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, " " + "풀무원 1등급 콩나물"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, " "+ "국산 우리 콩두부"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, " " + "곰곰 국내산 당근"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, " " + "국내산 양파"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, " 축산/계란"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, " " + "소고기 안창살"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, " " + "곰곰 대패 삼겹살"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, " " + "무항생제 햇살계란"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, " " + "하림 닭가슴살"));
        adapter = new ExpandableListAdapter(data);
    }
}