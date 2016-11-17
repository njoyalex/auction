package auction.strategy;

import auction.Strategy;
import java.util.Random;

public class AgressiveStrategy implements Strategy {

    private static final Random R = new Random();
    
    @Override
    public Integer apply(int price) {
        return  price + R.nextInt(1000);
    }

}
