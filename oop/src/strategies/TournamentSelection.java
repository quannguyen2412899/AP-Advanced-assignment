package strategies;

import models.*;
import utils.RandomUtil;

public class TournamentSelection implements SelectionStrategy {
    private int tournamentSize;

    public TournamentSelection(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }
    public TournamentSelection(TournamentSelection other) {
        this.tournamentSize = other.tournamentSize;
    }
    
    @Override
    public Chromosome select(Population population, RandomUtil random) {

        /* implemntations */
        
        return null;
    }
}