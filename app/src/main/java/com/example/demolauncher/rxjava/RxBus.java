package com.example.demolauncher.rxjava;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {
    private RxBus mRxBus;
    private final Subject<Object , Object> subject =
            new SerializedSubject<>(PublishSubject.create());

    public RxBus getInstance(){
        if (mRxBus == null){
            synchronized (RxBus.class){
                if (mRxBus == null)
                    mRxBus = new RxBus();
            }
        }
        return mRxBus;
    }

    public void sendMag(Object o){
        subject.onNext(o);
    }

    public <T> Observable<T> setListener(Class<T> classType){
        return subject.ofType(classType);
    }
}
