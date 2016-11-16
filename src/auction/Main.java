package auction;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        StrategyFactory strategyFactory = new StrategyFactory();
        List<Bot> bots = IntStream.range(0, 100)
                .boxed()
                .map(i -> new Bot("Alex", i, strategyFactory.pickRandomStrategy()))
                .collect(toList());
        Auction auction = new Auction(new AtomicInteger(10), bots, 4, 4);
        auction.runAuction();
    }
}
