package com.example.demolauncher.design.factory;

public class Thor extends Hero{
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
