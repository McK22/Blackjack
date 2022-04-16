package blackjack.controller;

public enum Action {
    HIT, STAND, DOUBLE_DOWN, SPLIT, SURRENDER, INSURANCE;

    public String toString(){
        switch(this){
            case HIT:
                return "Hit";
            case STAND:
                return "Stand";
            case DOUBLE_DOWN:
                return "Double\n down";
            case SPLIT:
                return "Split";
            case SURRENDER:
                return "Surrender";
            case INSURANCE:
                return "Insurance";
        }
        return "Error";
    }
}
