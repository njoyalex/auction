package auction;

public class Bot {

    private final String owner;
    private final int id;
    private final Strategy strategy;

    public Bot(String owner, int id, Strategy strategy) {
        this.owner = owner;
        this.id = id;
        this.strategy = strategy;
    }

    public String getOwner() {
        return owner;
    }

    public int getId() {
        return id;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public Bid placeBet(Auction auction) {
        Integer updated = strategy.apply(auction.getLastBid());
        if (updated != null) {
            return new Bid(id, owner, System.currentTimeMillis() - auction.getStartTs(), updated);
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "Bot{" + "owner=" + owner + ", id=" + id + ", strategy=" + strategy + '}';
    }

}
