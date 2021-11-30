package com.application.foodnoculars;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public interface NearLocationInterface {
    View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                      Bundle savedInstanceState);
}
