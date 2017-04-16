package com.mishkun.yandextestexercise.presentation.views.fragments;

import android.support.v4.app.Fragment;

import com.mishkun.yandextestexercise.di.HasComponent;

/**
 * Created by Mishkun on 12.04.2017.
 */

public abstract class BaseFragment extends Fragment {
    /**
     * Gets a component for dependency injection by its type.
     */
    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }
}