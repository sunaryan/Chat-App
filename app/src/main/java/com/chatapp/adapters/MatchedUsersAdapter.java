package com.chatapp.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chatapp.MainActivity;
import com.chatapp.R;
import com.chatapp.databinding.ListItemMatchedUserBinding;
import com.chatapp.fragments.MatchedUserViewProfileFragment;

public class MatchedUsersAdapter extends RecyclerView.Adapter<MatchedUsersAdapter.Holder> {
    private Context context;

    public MatchedUsersAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_matched_user, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ListItemMatchedUserBinding binding = holder.getBinding();
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    class Holder extends RecyclerView.ViewHolder {
        private ListItemMatchedUserBinding binding;

        Holder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMenuWithPopUp(binding.options);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof MainActivity)
                        ((MainActivity) context).showFragment(new MatchedUserViewProfileFragment(), MatchedUserViewProfileFragment.class.getSimpleName());
                }
            });
        }

        ListItemMatchedUserBinding getBinding() {
            return binding;
        }

        public void setBinding(ListItemMatchedUserBinding binding) {
            this.binding = binding;
        }
    }

    /**
     * function to open the menu with Bottom Sheet
     */
    private void openMenuWithBottomSheet() {
        View menuView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_matched_users, new LinearLayout(context), false);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(menuView);
        bottomSheetDialog.show();
    }

    /**
     * function to open the menu with popup
     */
    private void openMenuWithPopUp(View view) {
        //creating a popup menu
        PopupMenu popup = new PopupMenu(context, view);
        //inflating menu from xml resource
        popup.inflate(R.menu.macthed_users_menu);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                }
                return false;
            }
        });
        //displaying the popup
        popup.show();
    }
}
