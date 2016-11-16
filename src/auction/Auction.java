package auction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Auction {

    private final AtomicInteger lastBid;
    private final List<Bot> bots;
    private final List<Bid> history;
    private final int miniAuctions;
    private final ExecutorService executorService;
    private long startTs;

    public Auction(AtomicInteger startPrice, List<Bot> bots, int threads, int miniAuctions) {
        this.lastBid = startPrice;
        this.bots = bots;
        this.history = new CopyOnWriteArrayList<>();
        this.miniAuctions = miniAuctions;
        executorService = Executors.newFixedThreadPool(threads);
    }

    public void runAuction() {
        List<Callable<Bid>> subAuctions = new ArrayList<>();
        final Auction auction = this;
        int botsPerSubAuction = bots.size() / miniAuctions;
        for (int i = 0; i < miniAuctions; i++) {
            int start = i * botsPerSubAuction;
            int end;
            if (i + 1 == miniAuctions) {
                end = (i + 1) * botsPerSubAuction + bots.size() % miniAuctions;
            } else {
                end = (i + 1) * botsPerSubAuction;
            }
            subAuctions.add((Callable<Bid>) () -> auction.findWinner(bots.subList(start, end)));
        }

        try {
            startTs = System.currentTimeMillis();
            List<Future<Bid>> subAuctionTasks = executorService.invokeAll(subAuctions);
            List<Bid> subAuctionResults = new LinkedList<>();
            for (Future<Bid> subAuctionTask : subAuctionTasks) {
                try {
                    final Bid result = subAuctionTask.get();
                    if (result != null) {
                        subAuctionResults.add(result);
                    }
                } catch (ExecutionException ex) {
                    Logger.getLogger(Auction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            Optional<Bid> winner = subAuctionResults.stream()
                    .max((a, b) -> a.getPrice() - b.getPrice());
            if (winner.isPresent()) {
                System.out.println(winner.get());
            } else {
                System.out.println("no winner!");
            }
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Auction.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            executorService.shutdown();
        }
    }

    private Bid findWinner(List<Bot> bots) {
        Optional<Bid> winner = bots.stream()
                .map(bot -> bot.placeBet(this))
                .filter(Objects::nonNull)
                .max((a, b) -> a.getPrice() - b.getPrice());
        return winner.orElse(null);
    }

    public AtomicInteger getLastBid() {
        return lastBid;
    }

    public void addToHistory(Bid bid) {
        history.add(bid);
    }

    public long getStartTs() {
        return startTs;
    }

}
