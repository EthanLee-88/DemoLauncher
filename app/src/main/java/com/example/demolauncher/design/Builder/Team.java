package com.example.demolauncher.design.Builder;

public abstract class Team {
    protected String wizard = ""; //巫师
    protected String soldier = ""; //战士
    protected String archer = ""; //射手

    protected Team(){}

    public void setArcher(String archer) {
        this.archer = archer;
    }

    public void setSoldier(String soldier) {
        this.soldier = soldier;
    }

    public void setWizard(String wizard) {
        this.wizard = wizard;
    }
}
