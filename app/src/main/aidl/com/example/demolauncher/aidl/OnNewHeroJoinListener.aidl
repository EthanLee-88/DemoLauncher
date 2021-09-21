// OnNewHeroJoinListener.aidl
//包名要与其他类及相关接口的包名要保持一致
package com.example.demolauncher.aidl;

// Declare any non-default types here with import statements
//AIDl特性，即使所用到的类与本接口同属一个包，仍然要引入该类声明，否则报错
import com.example.demolauncher.aidl.Hero;
interface OnNewHeroJoinListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onNewHeroJoin(in List<Hero> heroes);// 形参需声明in，否则会报错
}
