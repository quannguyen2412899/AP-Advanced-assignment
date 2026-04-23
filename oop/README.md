## OOP Implementation Details

**Architecture Philosophy: Encapsulation, Modularity, Clarity**

The OOP implementation prioritizes explicit structure and type safety through well-defined classes and interfaces.

**Key Components:**

1. **Models Package** (`Chromosome.java`, `Population.java`):
   - `Chromosome`: Encapsulates a BitSet with fitness value
   - Defensive copying prevents unintended mutations
   - `Population.getPopulation()` intentionally removed to enforce immutability
   - Only `getIndividual(index)` is public for controlled access

2. **Strategies Package** (implements Strategy Pattern):
   - `SelectionStrategy` interface with `TournamentSelection` implementation
   - `CrossoverStrategy` interface with `OnePointCrossover` implementation
   - `MutationStrategy` interface with `BitFlipMutation` implementation
   - `ElitismStrategy` interface with `SimpleElitism` implementation
   - Each strategy captures its configuration (tournament size, crossover rate, mutation rate)

3. **Core Package** (GA Algorithm):
   - `EvolutionStep`: Encapsulates one generation's evolution
   - Clear step-by-step pipeline: Elitism → Selection → Crossover → Mutation → Fitness → Insertion
   - `GeneticAlgorithm`: Orchestrates multiple generations

4. **Utilities & Support**:
   - `RandomUtil`: Stateful PRNG using L32X64MixRandom (seeded)
   - `FitnessEvaluator`: Abstract base for problem-specific fitness
   - `EvolutionReporter`: Records and exports generation statistics

**Design Patterns Used:**
- **Strategy Pattern**: Pluggable operators (selection, crossover, mutation, elitism)
- **Factory Pattern**: `GAConfigLoader` creates appropriate fitness evaluators and strategies
- **Template Method**: `GeneticAlgorithm.run()` defines algorithm structure
- **Dependency Injection**: Strategies injected into `EvolutionStep`

This package-level separation makes the OOP implementation intentionally modular: the GA core depends on abstractions, not concrete implementations.

**Code Metrics:**
- ~1,200 lines across 19 files
- Strong typing with compile-time safety
- Explicit error handling with descriptive messages
- BitSet usage for memory efficiency

### Plotting integration (OOP)
In `oop/src/Main.java`, after exporting JSON results, plotting is triggered by executing:

`python oop/src/plot_ga_curve.py <output.json> <output_curve.png>`

This makes the reporting pipeline automated from a single `oop/run.py` run.