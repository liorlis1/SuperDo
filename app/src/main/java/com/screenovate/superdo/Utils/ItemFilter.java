package com.screenovate.superdo.Utils;

import android.widget.Filter;

import com.screenovate.superdo.Adapters.ShopListRecyclerAdapter;
import com.screenovate.superdo.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ItemFilter extends Filter {

    private List<Item> items;
    private List<Item> filteredItems = new ArrayList<>();
    private CountDownLatch filterLock = new CountDownLatch(0);
    private ShopListRecyclerAdapter adapter;

    public String getFilterString() {
        return filterString;
    }

    public void setFilterString(String filterString) {
        this.filterString = filterString;
    }

    String filterString = "";


    public ItemFilter(ShopListRecyclerAdapter adapter, List<Item> items) {
        this.adapter = adapter;
        this.items = items;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        filterString = constraint.toString();

        if(constraint.length() == 0) {
            results.values = items;
            results.count = items.size();
            return results;
        }

        filterLock = new CountDownLatch(1);
        int count = items.size();
        List<Item> tempFiltered = new ArrayList<>(count);


        for (int i = 0; i < count; i++) {
            if (items.get(i).getWeight().startsWith(filterString)) {
                tempFiltered.add(items.get(i));
            }
        }

        results.values = tempFiltered;
        results.count = tempFiltered.size();

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if(filterString.length() > 0) {
            filteredItems.clear();
            filteredItems.addAll((List<Item>) results.values);
            adapter.submitList(filteredItems);
            adapter.notifyDataSetChanged();
        } else {
            adapter.submitList(items);
            adapter.notifyDataSetChanged();
        }
        filterLock.countDown();

    }

    public List<Item> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<Item> filteredItems) {
        this.filteredItems = filteredItems;
    }

}

