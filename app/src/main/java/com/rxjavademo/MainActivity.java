package com.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView)findViewById(R.id.aa);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("aa","aa");
            }
        });

        test5();
    }


    private void test1(){
        Observable observable = Observable.create(new Observable.OnSubscribe<String>(){
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("aaa");
                subscriber.onCompleted();
            }
        });
        Subscriber subscriber = new Subscriber<String>(){
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.i("====rx======",s);
            }
        };
        observable.subscribe(subscriber);
    }

    private void test2(){
        //just函数 ：快速创建Observable对象
        //action1 ：作为参数传入subscribe(subscribe接受三个Action1类型的参数，分别对应OnNext，OnComplete， OnError函数)
        Observable.just("aaa")
            .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i("====rx======",s);
                    }
                });
    }

    private void test3(){
        //Operators
        //map 用于变换Observable对象的
        //map 返回一个Observable对象
        Observable.just("aaa")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Integer aa = null;
                        aa = aa + 1;
                        return s + "bbb";
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.i("====rx======","onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("====rx======",e+"");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.i("====rx======",s);
                    }
                });
    }

    private void test4(){
        //Operators
        //from  接收一个集合作为输入，然后每次输出一个元素给subscriber
        ArrayList arrayList = new ArrayList<String>();
        arrayList.add("aaa");
        arrayList.add("bbb");
        arrayList.add("ccc");
        Observable.from(arrayList)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i("====rx======",s);
                    }
                });
    }

    private void test5(){
        //Operators
        //flatMap  接收一个Observable的输出作为输入，同时输出另外一个Observable
        //filter  输出和输入相同的元素，并且会过滤掉那些不满足检查条件的
        //take  输出最多指定数量的结果
        //doOnNext  允许我们在每次输出一个元素之前做一些额外的事情(不影响subscribe中接收的结果)
        //subscribeOn  指定观察者(Observable)代码运行的线程
        //observerOn  指定订阅者(Subscriber)运行的线程：
        ArrayList arrayList = new ArrayList<String>();
        arrayList.add("aaa");
        arrayList.add("bbb");
        arrayList.add("ccc");
        Observable.just(arrayList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<ArrayList, Observable<String>>() {
                    @Override
                    public Observable<String> call(ArrayList arrayList) {
                        return Observable.from(arrayList);
                    }
                })
                .flatMap(new Func1<String, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(String s) {
                        if(s.equals("bbb"))
                            return Observable.just(Integer.valueOf(2));
                        return Observable.just(Integer.valueOf(1));
                    }
                })
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer != 2;
                    }
                })
                .take(3)
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        integer = 4;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer value) {
                        Toast.makeText(MainActivity.this,"" + value,Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void testAndroid(){

    }

}
