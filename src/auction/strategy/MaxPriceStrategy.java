package auction.strategy;

import auction.Strategy;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * Cannot be used in current implementation of auction
 * @author Alex
 */
public class MaxPriceStrategy implements Strategy {

    private static final int MAX_PRICE = 100;

    @Override
    public Integer apply(AtomicInteger price) {
        
        int updated = price.getAndUpdate(n -> {
            if (n < MAX_PRICE) {
                return n += 1;
            } else {
                return n;
            }
        });
        return updated;
    }

}
