package com.example.demolauncher.design.proxy.staticway;

public class StaticClient {

    public void playClient(){
        IAnimal tiger = new Tiger();
        IAnimal tigerProxy = new StaticProxy(tiger);
        tigerProxy.bit();
    }
}
