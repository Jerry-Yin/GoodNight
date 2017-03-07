package com.hdu.team.hiwanan.tools;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * Created by JerryYin on 3/7/17.
 */

public class RxJavaTest {


    Observer<String> observer = new Observer<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onNext(String s) {

        }
    };



    Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            subscriber.onNext("Hello");
            subscriber.onCompleted();
//            subscriber.onError();
        }


    });
}
