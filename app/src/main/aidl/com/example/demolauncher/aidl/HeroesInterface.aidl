// HeroesInterface.aidl
package com.example.demolauncher.aidl;

// Declare any non-default types here with import statements
 import com.example.demolauncher.aidl.Hero;
 import com.example.demolauncher.aidl.OnNewHeroJoinListener;
interface HeroesInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void addHero(in Hero hero);
    List<Hero> getHeroes();
    void registerListener(OnNewHeroJoinListener onNewHeroJoinListener);
    void unRegisterListener(OnNewHeroJoinListener onNewHeroJoinListener);
}
