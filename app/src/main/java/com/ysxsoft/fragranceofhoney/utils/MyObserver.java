package com.ysxsoft.fragranceofhoney.utils;

import android.widget.Toast;

import rx.Observer;

public abstract class MyObserver<T> implements Observer<T> {


    @Override
    public abstract void onCompleted();

    @Override
    public void onError(Throwable e) {
        Toast.makeText(BaseApplication.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public abstract void onNext(T bean);
}
