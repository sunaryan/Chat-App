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
import com.chatapp.adapters.EducationAdapter;
import com.chatapp.adapters.ExpertiseAdapter;
import com.chatapp.adapters.InterestedPeopleAdapter;
import com.chatapp.adapters.WorkExperienceAdapter;
import com.chatapp.databinding.FragmentMatchedUserViewProfileBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchedUserViewProfileFragment extends Fragment {
    private FragmentMatchedUserViewProfileBinding binding;
    private Context context;
    private WorkExperienceAdapter workExperienceAdapter;
    private EducationAdapter educationAdapter;
    private ExpertiseAdapter expertiseAdapter;
    private InterestedPeopleAdapter interestedPeopleAdapter;

    public MatchedUserViewProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_matched_user_view_profile, container, false);
        context = getActivity();

        // work experience
        workExperienceAdapter = new WorkExperienceAdapter();
        binding.workExperienceList.setLayoutManager(new LinearLayoutManager(context));
        binding.workExperienceList.setAdapter(workExperienceAdapter);
        binding.workExperienceList.setHasFixedSize(true);
        binding.workExperienceList.setNestedScrollingEnabled(false);

        // Education Adapter
        educationAdapter = new EducationAdapter();
        binding.educationList.setLayoutManager(new LinearLayoutManager(context));
        binding.educationList.setAdapter(educationAdapter);
        binding.educationList.setHasFixedSize(true);
        binding.educationList.setNestedScrollingEnabled(false);

        // Expertise Adapter
        expertiseAdapter = new ExpertiseAdapter();
        binding.expertiseList.setLayoutManager(new LinearLayoutManager(context));
        binding.expertiseList.setAdapter(expertiseAdapter);
        binding.expertiseList.setHasFixedSize(true);
        binding.expertiseList.setNestedScrollingEnabled(false);

        // Interested people Adapter
        interestedPeopleAdapter = new InterestedPeopleAdapter();
        binding.interestPeopleList.setLayoutManager(new LinearLayoutManager(context));
        binding.interestPeopleList.setAdapter(interestedPeopleAdapter);
        binding.interestPeopleList.setHasFixedSize(true);
        binding.interestPeopleList.setNestedScrollingEnabled(false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        if (context instanceof MainActivity) {
            ((MainActivity) context).setToolbarTitle(getString(R.string.matchedUserProfileTitle), true);
        }
    }
}
