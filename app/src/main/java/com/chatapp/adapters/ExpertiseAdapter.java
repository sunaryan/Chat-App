package com.chatapp.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chatapp.R;
import com.chatapp.databinding.ListItemExpertiseBinding;

public class ExpertiseAdapter extends RecyclerView.Adapter<ExpertiseAdapter.Holder> {

    @Override
    public ExpertiseAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_expertise, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class Holder extends RecyclerView.ViewHolder {
        private ListItemExpertiseBinding binding;

        Holder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
