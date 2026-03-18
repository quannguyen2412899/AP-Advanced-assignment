package strategies;

import java.util.BitSet;

public interface  MutationStrategy {
    void mutate(BitSet chromosome);
}