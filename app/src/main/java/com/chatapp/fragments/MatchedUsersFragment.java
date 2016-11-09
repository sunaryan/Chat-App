package com.chatapp.fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chatapp.MainActivity;
import com.chatapp.R;
import com.chatapp.adapters.MatchedUsersAdapter;
import com.chatapp.base.BaseFragment;
import com.chatapp.databinding.FragmentMatchedUsersBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchedUsersFragment extends BaseFragment {
    private FragmentMatchedUsersBinding binding;
    private Context context;

    public MatchedUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_matched_users, container, false);
        context = getActivity();

        binding.usersList.setLayoutManager(new LinearLayoutManager(context));
        MatchedUsersAdapter adapter = new MatchedUsersAdapter(context);
        binding.usersList.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();

        if (context instanceof MainActivity)
            ((MainActivity) context).setToolbarTitle(getString(R.string.matchedUsersTitle), false);
    }
}
