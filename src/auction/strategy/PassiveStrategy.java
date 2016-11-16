package auction.strategy;

import auction.Strategy;
import java.util.concurrent.atomic.AtomicInteger;

public class PassiveStrategy implements Strategy{

    @Override
    public Integer apply(AtomicInteger price) {
        return null;
    }

}
