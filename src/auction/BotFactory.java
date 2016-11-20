package auction;

import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

public class BotFactory {

    public static List<Bot> createBots(int n) {
        return IntStream.range(0, n)
                .mapToObj(i -> new Bot("Alex", i, StrategyFactory.pickRandomStrategy()))
                .collect(toList());
    }

}
