package auction;

import auction.strategy.AgressiveStrategy;
import auction.strategy.PassiveStrategy;
import java.util.Random;

public class StrategyFactory {

    private final Random R = new Random();

    public Strategy pickRandomStrategy() {
        int strategyId = R.nextInt(2);
        switch (strategyId) {
            case 0:
                return new AgressiveStrategy();
            case 1:
                return new PassiveStrategy();
            default:
                return null;
        }
    }
}
