package auction;

import static java.util.Arrays.asList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Bot> bots = BotFactory.createBots(1000);
        Auction auction = new Auction(bots, 4, 16, asList(new Watcher()));
        auction.start(10, 100);
        System.out.println("finished in : " + (System.currentTimeMillis() - auction.getStartTs()));
        auction.getHistory().forEach((bid) -> {
            System.out.println(bid);
        });
        auction.finish();
    }

}
