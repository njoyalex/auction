package auction;

import auction.strategy.AgressiveStrategy;
import auction.strategy.PassiveStrategy;
import auction.strategy.StableIncreaseStrategy;
import java.util.Random;

public class StrategyFactory {

    private static final Random R = new Random();

    public static Strategy pickRandomStrategy() {
        int strategyId = R.nextInt(3);
        switch (strategyId) {
            case 0:
                return new AgressiveStrategy();
            case 1:
                return new PassiveStrategy();
            case 2: 
                return new StableIncreaseStrategy();
            default:
                return null;
        }
    }
}
