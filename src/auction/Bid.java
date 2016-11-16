package auction;

public class Bid {

    private final int botId;
    private final String botOwner;
    private final long ts;
    private final int price;

    public Bid(int botId, String botOwner, long ts, int price) {
        this.botId = botId;
        this.botOwner = botOwner;
        this.ts = ts;
        this.price = price;
    }

    public int getBotId() {
        return botId;
    }

    public String getBotOwner() {
        return botOwner;
    }

    public long getTs() {
        return ts;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Bid{" + "botId=" + botId + ", botOwner=" + botOwner + ", ts=" + ts + ", price=" + price + '}';
    }

}
