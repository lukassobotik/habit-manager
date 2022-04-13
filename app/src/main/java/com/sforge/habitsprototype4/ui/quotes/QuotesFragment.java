package com.sforge.habitsprototype4.ui.quotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sforge.habitsprototype4.databinding.FragmentQuotesBinding;

public class QuotesFragment extends Fragment {

    private FragmentQuotesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        QuotesViewModel quotesViewModel =
                new ViewModelProvider(this).get(QuotesViewModel.class);

        binding = FragmentQuotesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}