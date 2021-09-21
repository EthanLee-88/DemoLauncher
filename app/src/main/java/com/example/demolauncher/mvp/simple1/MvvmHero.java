package com.example.demolauncher.mvp.simple1;

public class MvvmHero {

    public String realName;
    public String nickName;
    public String job;

    public MvvmHero(String realName , String nickName , String job){
        this.realName = realName;
        this.nickName = nickName;
        this.job = job;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getRealName() {
        return realName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getJob() {
        return job;
    }
}
