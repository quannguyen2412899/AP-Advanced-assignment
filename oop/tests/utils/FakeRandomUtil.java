package utils;

public class FakeRandomUtil extends RandomUtil {
    private int[] intSequence;
    private int intSeqInd;
    private boolean[] bernoulliSequence;
    private int bernoulliSeqInd;

    public FakeRandomUtil(int[] fixIntSequence, boolean[] fixBernoulliSequence) {
        super(0); // dummy seed, not used
        intSeqInd = 0;
        bernoulliSeqInd = 0;
        intSequence = fixIntSequence;
        bernoulliSequence = fixBernoulliSequence;
    }

    @Override
    public boolean nextBernoulli(double p) {
        boolean value = bernoulliSequence[bernoulliSeqInd % bernoulliSequence.length];
        bernoulliSeqInd++;
        return value;
    }

    @Override
    public int nextInt(int range) {
        int value = intSequence[intSeqInd % intSequence.length];
        intSeqInd++;
        return value;
    }

    @Override
    public int nextInt(int origin, int bound) {
        int value = intSequence[intSeqInd % intSequence.length];
        intSeqInd++;
        return value;
    }
}