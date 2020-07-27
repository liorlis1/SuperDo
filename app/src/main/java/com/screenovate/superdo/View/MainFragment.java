package com.screenovate.superdo.View;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.screenovate.superdo.Adapters.ShopListRecyclerAdapter;
import com.screenovate.superdo.Item;
import com.screenovate.superdo.ItemListener;
import com.screenovate.superdo.Model.SuperDoModel;
import com.screenovate.superdo.R;

import java.util.Objects;

public class MainFragment extends Fragment {

    private Button startBtn;
    private Button stopBtn;
    private SuperDoModel mSDodel;
    private RecyclerView mItemList;
    private ShopListRecyclerAdapter mAdapter;
    private EditText mFilterEdit;

    public static MainFragment newInstance() {
        return new MainFragment();
    }
    private final Object insertLock = new Object();

    private Spinner spinner;
    private static final String[] options = {"","name", "weight", "color"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View mainFragment = inflater.inflate(R.layout.main_fragment, container, false);
        startBtn = mainFragment.findViewById(R.id.start_btn);
        stopBtn = mainFragment.findViewById(R.id.stop_btn);
        mItemList = mainFragment.findViewById(R.id.groceries_list);
        mFilterEdit = mainFragment.findViewById(R.id.filter_edit);
        spinner = mainFragment.findViewById(R.id.spinner);

        return mainFragment;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // TODO - add support for more filtering options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                options);

        spinner.setAdapter(adapter);
        /////////////

        mSDodel = new SuperDoModel(getContext(), new ItemListener() {
            @Override
            public void onNewItem(final Item item) {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (insertLock) {
                            if(mAdapter != null)
                                mAdapter.addItem(item);
                        }
                    }
                });
            }
        });

        mAdapter = new ShopListRecyclerAdapter(new DiffUtil.ItemCallback<Item>() {

            @Override
            public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
                return  oldItem.getBagColor().equals(newItem.getBagColor()) &&
                        oldItem.getName().equals(newItem.getName()) &&
                        oldItem.getWeight().equals(newItem.getWeight());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
                return  oldItem.getBagColor().equals(newItem.getBagColor()) &&
                        oldItem.getName().equals(newItem.getName()) &&
                        oldItem.getWeight().equals(newItem.getWeight());
            }
        });

        mItemList.setAdapter(mAdapter);
        if (startBtn != null) {
            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSDodel.startLoading();
                }
            });
        }

        mItemList.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(stopBtn != null) {
            stopBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSDodel.stopLoading();
                }
            });
        }

        mFilterEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                synchronized (insertLock) {
                    mAdapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}