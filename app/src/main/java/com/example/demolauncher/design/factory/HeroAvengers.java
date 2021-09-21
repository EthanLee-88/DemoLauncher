package com.example.demolauncher.design.factory;

import com.example.demolauncher.design.Builder.AvengersBuilder;

public class HeroAvengers extends Avengers {
    @Override
    public <T extends Hero> T createHero(Class<T> tClass) {
        Hero hero = null;
        String className = tClass.getName();
        try {
            hero = (Hero) Class.forName(className).newInstance();
        }catch (Exception e){}
        return (T) hero;
    }
}
