package auction.strategy;

import auction.Strategy;
import java.util.concurrent.atomic.AtomicInteger;

public class AgressiveStrategy implements Strategy {

    @Override
    public Integer apply(AtomicInteger price) {
        return price.updateAndGet(n -> n + 100);
    }

}
