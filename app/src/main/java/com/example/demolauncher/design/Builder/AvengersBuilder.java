package com.example.demolauncher.design.Builder;

//AvengersBuilder.createBuilder().togetherHeroes("奇异博士" , "钢铁侠" , "鹰眼").createTeam();

public class AvengersBuilder extends Builder{
    private Team avengers = new HeroTeam();

    public static AvengersBuilder createBuilder(){ //静态内部类单例模式
        return BuilderCreator.avengerBuilder;
    }

    private static class BuilderCreator{
        private static final AvengersBuilder avengerBuilder = new AvengersBuilder();
    }

    public AvengersBuilder togetherHeroes(String wizard , String soldier , String archer){
        avengers.wizard = wizard;
        avengers.soldier = soldier;
        avengers.archer = archer;
        return this;
    }

    @Override
    public AvengersBuilder engageArcher(String archer) {
        avengers.archer = archer;
        return this;
    }

    @Override
    public AvengersBuilder engageSoldier(String solder) {
        avengers.soldier = solder;
        return this;
    }

    @Override
    public AvengersBuilder engageWizard(String wizard) {
        avengers.wizard = wizard;
        return this;
    }

    @Override
    public Team createTeam() {
        return avengers;
    }
}
