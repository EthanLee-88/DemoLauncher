package com.example.demolauncher.design.factory;

public abstract class Avengers {

    public abstract <T extends Hero> T createHero(Class<T> tClass);
}
