package auction;

import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        StrategyFactory strategyFactory = new StrategyFactory();
        List<Bot> bots = IntStream.range(0, 100)
                .boxed()
                .map(i -> new Bot("Alex", i, strategyFactory.pickRandomStrategy()))
                .collect(toList());
        Auction auction = new Auction(bots, 4, 4);
        auction.start(10, 100);
        System.out.println("finished in : " + (System.currentTimeMillis() - auction.getStartTs()));
        for (Bid bid : auction.getHistory()) {
            System.out.println(bid);
        }
        auction.finish();

    }
}
