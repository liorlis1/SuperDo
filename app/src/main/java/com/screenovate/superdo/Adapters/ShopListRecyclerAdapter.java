package com.screenovate.superdo.Adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.screenovate.superdo.Item;
import com.screenovate.superdo.R;
import com.screenovate.superdo.Utils.ItemFilter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ShopListRecyclerAdapter extends ListAdapter<Item, ItemViewHolder> {

    private final static String TAG = ShopListRecyclerAdapter.class.getSimpleName();
    private List<Item> items = new ArrayList<>();
    private List<Item> filteredItems = new ArrayList<>();
    private CountDownLatch filterLock = new CountDownLatch(0);
    private ItemFilter filter;
    private RecyclerView mRecyclerView;

    public ShopListRecyclerAdapter(@NonNull DiffUtil.ItemCallback<Item> diffCallback) {
        super(diffCallback);
        filter = new ItemFilter(this,items);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_holder, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        try {
            Item item = getItem(position);
            holder.colorImage.setBackgroundColor( Color.parseColor(item.getBagColor()));
            holder.name.setText(item.getName());
            holder.weight.setText(item.getWeight());
        }catch (Exception e){
            Log.d(TAG, "onBindViewHolder: something went wrong ");
            Log.d(TAG, "onBindViewHolder: error : " + e.getMessage());
        }
    }

    public void addItem(Item item) {
        if(filter.getFilterString().length() > 0 &&
                item.getWeight().startsWith(filter.getFilterString())) {
            try {
                filterLock.await();
            } catch (InterruptedException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
            //adding to the top
            filteredItems.add(0,item);
            itemUpdate(filteredItems);
            items.add(item);
            filterLock.countDown();
        }else {
            //adding to the top
            items.add(0, item);
            if (filter.getFilterString().length() == 0) {
               itemUpdate(items);
            }
        }
    }


    private void itemUpdate(List<Item> itemsList) {
        submitList(itemsList);
        notifyDataSetChanged();

        //scrolling to position 0 - Does showing like the example that the top value is updated
        mRecyclerView.smoothScrollToPosition(0);
    }


    public ItemFilter getFilter() {
        return filter;
    }

}



