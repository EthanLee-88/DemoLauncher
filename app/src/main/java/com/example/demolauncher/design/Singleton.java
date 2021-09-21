package com.example.demolauncher.design;

public class Singleton { //静态内部类单例模式

    public static Singleton getInstance(){
        return Holder.singleton;
    }

    private static class Holder{
        private static final Singleton singleton = new Singleton();
    }

}
