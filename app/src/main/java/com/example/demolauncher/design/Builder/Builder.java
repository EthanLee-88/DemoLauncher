package com.example.demolauncher.design.Builder;

public abstract class Builder {
    protected abstract Builder engageWizard(String wizard); //召集巫师
    protected abstract Builder engageSoldier(String solder); //返回值Builder实现链式操作
    protected abstract Builder engageArcher(String archer);
    public abstract Team createTeam();
}
