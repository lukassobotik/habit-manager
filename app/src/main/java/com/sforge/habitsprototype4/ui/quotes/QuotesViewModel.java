package com.sforge.habitsprototype4.ui.quotes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QuotesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public QuotesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
