package strategies;

import java.util.PriorityQueue;
import models.*;

public class SimpleElitism implements ElitismStrategy {
    private int count;

    public SimpleElitism(int count) {
        if (count < 0) throw new IllegalArgumentException("SimpleElitism: count must be >= 0, got " + count);
        this.count = count;
    }

    @Override
    public Chromosome[] select(Population population) {
        if (count == 0) return new Chromosome[0];
        if (count > population.size()) throw new IllegalArgumentException("SimpleElitism.select: elitism count " + count + " > population size " + population.size());
        
        // Min-heap: keep top k by fitness
        PriorityQueue<Chromosome> heap = new PriorityQueue<>(count, 
            (a, b) -> Double.compare(a.getFitness(), b.getFitness()));
        
        for (int i = 0; i < population.size(); i++) {
            Chromosome chrom = population.getIndividual(i);
            heap.offer(chrom);
            if (heap.size() > count) {
                heap.poll();  // Remove worst
            }
        }
        
        Chromosome[] elite = new Chromosome[count];
        int idx = count - 1;
        while (!heap.isEmpty()) {
            elite[idx--] = heap.poll();
        }
        return elite;
    }
}