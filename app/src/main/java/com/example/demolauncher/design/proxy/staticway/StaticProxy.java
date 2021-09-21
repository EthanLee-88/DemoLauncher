package com.example.demolauncher.design.proxy.staticway;

public class StaticProxy implements IAnimal{
    private IAnimal mIAnimal;
    public StaticProxy(IAnimal animal){
        mIAnimal = animal;
    }

    @Override
    public void bit() {
        mIAnimal.bit();
    }
}
