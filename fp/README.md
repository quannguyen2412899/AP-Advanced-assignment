## FP Implementation Details

**Architecture Philosophy: Purity, Immutability, Composition**

The FP implementation emphasizes pure functions, stateless operations, and functional composition through closures.

**Key Components:**

1. **Type System** (`type_alias.py`):
   - `GeneString = list[bool]`
   - `Chromosome = tuple[GeneString, int]` (immutable gene + fitness)
   - `Population = list[Chromosome]` (list of immutable tuples)

2. **Random Utilities** (`random_util.py`):
   - **Stateless deterministic RNG** using SHA256 hashing
   - `random_gen(*keys)`: Accumulates keys into hash for reproducible sequences
   - Enables perfect reproducibility across runs with same input
   - **Key advantage**: No seed state needed, same inputs guarantee same outputs

3. **Pure Operators** (`ga_operators.py`):
   - `tournament_select()`: Selects best of tournament pool via functional `map()`
   - `onepoint_crossover()`: Pure function returning new chromosomes (immutable)
   - `bitflip_mutate()`: Pure function using `map()` for bit-wise operations
   - `simple_elite_select()`: Uses `heapq.nlargest()` for efficient elitism
   - All use default `rand_eng=random_gen` for deterministic behavior

4. **Dependency Injection via Closures** (`operators_factory.py`):
   - `get_selection_strategy()`: Returns `lambda population, *args: tournament_select(...)`
   - `get_crossover_strategy()`: Returns `lambda p1, p2, *args: onepoint_crossover(...)`
   - `get_mutation_strategy()`: Returns `lambda c, *args: bitflip_mutate(...)`
   - `get_elitism_strategy()`: Returns `lambda population: simple_elite_select(...)`
   - Captures configuration at factory time, creates stateless functions

5. **Pure GA Algorithm** (`genetic_algorithm.py`):
   - `one_step_GA()`: Pure function producing new population
   - `get_one_step_GA()`: Higher-order function returning configured evolution step
   - `execute_GA()`: Executes generations, records statistics
   - `get_statistics()`: Pure statistics computation

**Design Patterns Used:**
- **Higher-Order Functions**: Factories return configured functions
- **Closures for Dependency InjectionI**: Capture configuration in function scope
- **Immutability**: Tuples for chromosomes, copies for gene strings
- **Functional Composition**: Pipeline of pure transformations

**Code Metrics:**
- ~400 lines across 8 files (67% less code than OOP)
- No compile-time checking, but runtime type hints
- Deterministic randomness via stateless hashing
- Concise, expressive syntax

### Plot generation
- `fp/src/plot_ga_curve.py` reads the exported JSON stats and draws.