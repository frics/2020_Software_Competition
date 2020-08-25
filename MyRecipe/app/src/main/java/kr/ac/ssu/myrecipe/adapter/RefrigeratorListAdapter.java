package kr.ac.ssu.myrecipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.ssu.myrecipe.IconData;
import kr.ac.ssu.myrecipe.R;

public class RefrigeratorListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    public List<Item> filteredList;
    public List<Item> unFilteredlist;
    public static final int HEADER = 0;
    public static final int CHILD = 1;

    public RefrigeratorListAdapter(List<Item> data) {
        this.filteredList = data;
        this.unFilteredlist = data;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()) {
                    filteredList = unFilteredlist;
                } else {
                    List<Item> filteringList = new ArrayList<>();
                    Item currentHeader = new Item();
                    boolean isSelected = false;
                    for(int i = 0; i < unFilteredlist.size(); i++) {
                        //헤더가 아니고 해당문자열 포함하면
                        if(unFilteredlist.get(i).type == HEADER){
                            currentHeader = unFilteredlist.get(i);
                            currentHeader.position = i;
                            isSelected = false;
                        }
                        else if(unFilteredlist.get(i).text.toLowerCase().contains(charString.toLowerCase())) {
                            //이미 이전에 헤더가 선택되지 않았으면
                            if(isSelected == false) {
                                //헤더 추가
                                filteringList.add(currentHeader);
                                isSelected = true;
                            }
                            unFilteredlist.get(i).position = i;
                            filteringList.add(unFilteredlist.get(i));
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //filteredList = (ArrayList<Item>)results.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = null;
        Context context = parent.getContext();
        float dp = context.getResources().getDisplayMetrics().density;
        int subItemPaddingLeft = (int) (18 * dp);
        int subItemPaddingTopAndBottom = (int) (5 * dp);
        switch (type) {
            case HEADER:
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_header, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;
            case CHILD:
                TextView itemTextView = new TextView(context);
                itemTextView.setPadding(subItemPaddingLeft, subItemPaddingTopAndBottom, 0, subItemPaddingTopAndBottom);
                itemTextView.setTextColor(0x88000000);
                itemTextView.setLayoutParams(
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                100));
                return new RecyclerView.ViewHolder(itemTextView) {
                };
        }
        return null;
    }


    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = filteredList.get(position);
        IconData iconDataMap = new IconData();
        switch (item.type) {
            case HEADER:
                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                itemController.refferalItem = item;
                itemController.header_title.setText(item.text);
                itemController.header_image.setImageResource(iconDataMap.textToicon.get(item.text));
                if (item.invisibleChildren == null) {
                    itemController.btn_expand_toggle.setImageResource(R.drawable.circle_minus);
                } else {
                    itemController.btn_expand_toggle.setImageResource(R.drawable.circle_plus);
                }
                itemController.btn_expand_toggle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int count = 0;
                            int pos = filteredList.indexOf(itemController.refferalItem);
                            while (filteredList.size() > pos + 1 && filteredList.get(pos + 1).type == CHILD) {
                                item.invisibleChildren.add(filteredList.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            itemController.btn_expand_toggle.setImageResource(R.drawable.circle_plus);
                        } else {
                            int pos = filteredList.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                filteredList.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            itemController.btn_expand_toggle.setImageResource(R.drawable.circle_minus);
                            item.invisibleChildren = null;
                        }
                    }
                });
                break;
            case CHILD:
                TextView itemTextView = (TextView) holder.itemView;
                itemTextView.setText(filteredList.get(position).text);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return filteredList.get(position).type;
    }
    public int getUnfilterViewType(int positon){
        return unFilteredlist.get(positon).type;
    }


    @Override
    public int getItemCount() {
        return filteredList.size();
    }


    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView header_title;
        public ImageView btn_expand_toggle;
        public Item refferalItem;
        public ImageView header_image;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            header_title = (TextView) itemView.findViewById(R.id.header_title);
            btn_expand_toggle = (ImageView) itemView.findViewById(R.id.btn_expand_toggle);
            header_image = (ImageView) itemView.findViewById(R.id.header_image);
        }
    }

    public static class Item {
        public int type;
        public int position;
        public String text;
        public List<Item> invisibleChildren;

        public Item() {
        }

        public Item(int type, String text) {
            this.type = type;
            this.text = text;
        }
    }
}