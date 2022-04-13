package com.sforge.habitsprototype4.ui.counter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sforge.habitsprototype4.databinding.FragmentCounterBinding;

public class CounterFragment extends Fragment {

    private FragmentCounterBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CounterViewModel counterViewModel =
                new ViewModelProvider(this).get(CounterViewModel.class);

        binding = FragmentCounterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
