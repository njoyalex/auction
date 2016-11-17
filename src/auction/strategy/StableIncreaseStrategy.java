package auction.strategy;

import auction.Strategy;

public class StableIncreaseStrategy implements Strategy {

    private static final int INCREASE_STEP = 100;

    @Override
    public Integer apply(int price) {
       return price + INCREASE_STEP;
    }

}
