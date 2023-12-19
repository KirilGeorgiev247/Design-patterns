package random;

import java.util.Random;
import java.util.random.RandomGenerator;

public class CustomRandom implements RandomGenerator {

    private final RandomGenerator randomGenerator;
    public CustomRandom() {
        randomGenerator = new Random();
    }
    @Override
    public int nextInt(int origin, int bound) {
        return randomGenerator.nextInt(origin, bound);
    }

    @Override
    public long nextLong() {
        return randomGenerator.nextInt();
    }
}
