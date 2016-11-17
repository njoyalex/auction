package auction;

import java.util.concurrent.atomic.AtomicInteger;

public interface Strategy {

    Integer apply(int price);
}
