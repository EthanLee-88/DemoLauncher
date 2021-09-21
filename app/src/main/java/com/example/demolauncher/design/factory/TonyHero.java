package com.example.demolauncher.design.factory;

import com.example.demolauncher.design.Builder.AvengersBuilder;

public class TonyHero extends Hero{
    private String nickName;

    @Override
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String getNickName() {
        return nickName;
    }
}
