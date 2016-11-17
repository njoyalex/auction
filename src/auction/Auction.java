package auction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Auction {

    private final List<Bot> bots;
    private final List<Bid> history;
    private final int miniAuctions;
    private final ExecutorService executorService;
    private long startTs;
    private List<Callable<Bid>> subAuctions;

    public Auction(List<Bot> bots, int threads, int miniAuctions) {
        this.bots = bots;
        this.history = new CopyOnWriteArrayList<>();
        this.miniAuctions = miniAuctions;
        executorService = Executors.newFixedThreadPool(threads);
        prepareSubAuctions();
    }

    public void start(int bids, int startPrice) {
        history.add(new Bid(-1, "initPrice", 0, startPrice));
        startTs = System.currentTimeMillis();
        CompletableFuture<Bid> winner = askBestBid(new Bid(-1, "initPrice", -1, startPrice));
        for (int i = 1; i < bids; i++) {
            winner = winner.thenCompose(this::askBestBid);
        }
        winner.thenAccept(winBid -> System.out.println(winBid));
    }

    public CompletableFuture<Bid> askBestBid(Bid previousWinner) {
        CompletableFuture<Bid> future = new CompletableFuture<>();
        try {
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
            Optional<Bid> possibleWinner = subAuctionResults.stream()
                    .max((a, b) -> a.getPrice() - b.getPrice());
            if (possibleWinner.isPresent()) {
                history.add(possibleWinner.get());
                future.complete(possibleWinner.get());
            } else {
                future.complete(previousWinner);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Auction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return future;
    }

    private void prepareSubAuctions() {
        subAuctions = new ArrayList<>();
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
    }

    private Bid findWinner(List<Bot> bots) {
        Optional<Bid> winner = bots.stream()
                .map(bot -> bot.placeBet(this))
                .filter(Objects::nonNull)
                .max((a, b) -> a.getPrice() - b.getPrice());
        return winner.orElse(null);
    }

    public int getLastBid() {
        return history.get(history.size() - 1).getPrice();
    }

    public void addToHistory(Bid bid) {
        history.add(bid);
    }

    public long getStartTs() {
        return startTs;
    }

    public List<Bid> getHistory() {
        return history;
    }
    
    public void finish(){
        executorService.shutdown();
    }

}
