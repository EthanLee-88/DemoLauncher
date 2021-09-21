package com.example.demolauncher.design.proxy.dynamic;

import java.lang.reflect.Proxy;

public class DynamicClient {

    public void showDynamic(){
        IHero superHero = new SuperHero();
        ISoldier superSoldier = new SuperSoldier();

        DynamicProxy heroProxy = new DynamicProxy(superHero); //创建hero代理类
        DynamicProxy soldierProxy = new DynamicProxy(superSoldier); //创建soldier代理类

        IHero finalHeroProxy =            //创建最终Hero代理人
                (IHero) Proxy.newProxyInstance(IHero.class.getClassLoader() , new Class[]{IHero.class} , heroProxy);
        ISoldier finalSoldierProxy =      //穿件最终Soldier代理人
                (ISoldier) Proxy.newProxyInstance(ISoldier.class.getClassLoader() , new Class[]{ISoldier.class} , soldierProxy);

        finalHeroProxy.fight();//调用Hero方法
        finalSoldierProxy.fightTogether();//调用Soldier方法
    }
}
