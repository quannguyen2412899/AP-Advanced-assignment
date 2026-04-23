# Genetic Algorithm (GA) Assignment Report — OOP vs FP

## Student Information
| Field | Value |
| :--- | :--- |
| **Student** | Nguyễn Anh Quân |
| **Student ID** | 2412899 |
| **Course** | Advanced Programming (expanded) |

---

## 1. Overview
This project implements a **Genetic Algorithm (GA)** twice to compare two programming paradigms:

1. **Object-Oriented Programming (OOP)** implementation (Java)
2. **Functional Programming (FP)** implementation (Python)

Both implementations solve the same two benchmark problems using the same GA configuration and random seed to ensure comparability:
- **OneMax** (maximize number of 1s in a bitstring of length 100)
- **0/1 Knapsack** (100 items, capacity = 40% of total weight, infeasible solutions get fitness = 0)

---

## 2. Genetic Algorithm Configuration (Reproducible Setup)
The GA settings are controlled through configuration files in the repository (e.g., `problems/onemax.json`) and are shared across both implementations.

### Parameters
| Parameter | Value |
| :--- | :--- |
| Representation | Bitstring |
| Population size | 100 |
| Chromosome length (OneMax) | 100 |
| Parent selection | Tournament, k = 3 |
| Crossover | One-point, rate = 0.9 |
| Mutation | Bit-flip, rate per bit = 1/L = 0.01 |
| Replacement | Generational + elitism |
| Elitism | e = 2 |
| Termination | 300 generations |
| Random seed | 42 |

Example (from `problems/onemax.json`): population size 100, chromosome length 100, max generations 300, tournament size 3, crossover rate 0.9, mutation rate per bit 0.01, elitism count 2, seed 42.

---

## 3. How to Run

### 3.1 OOP Version (Java)
Run from the repository root:

```bash
python oop/run.py
```

What it does:
- Compiles Java sources into `oop/bin`
- Runs GA for both problems using:
  - `problems/onemax.json` → outputs: `reports/results_onemax_oop.json`
  - `problems/knapsack.json` → outputs: `reports/results_knapsack_oop.json`

### 3.2 FP Version (Python)
Run from the repository root:

```bash
python fp/run.py
```

What it does:
- Executes the FP GA implementation for both problems and writes:
  - `reports/results_onemax_fp.json`
  - `reports/results_knapsack_fp.json`

---

## 4. Implementation Design

Both versions implement the same pipeline:

**Reads config JSON → builds operators → runs GA → prints results → writes JSON → plots PNG.**

## 4.1 OOP Design (Java)
The OOP version is implemented in Java under `oop/src/` and is structured to follow classic OOP principles: **abstraction via interfaces**, **encapsulation of state**, and **pluggable behavior via the Strategy pattern**. The GA “engine” is composed from smaller components (fitness evaluator + operator strategies + RNG utility), so the `GeneticAlgorithm` class itself focuses on orchestration rather than implementing every operator directly.

Key OOP goals reflected in the implementation:
- **Encapsulation:** Individuals and populations are modeled with dedicated classes (`Chromosome`, `Population`) with controlled accessors and validation.
- **Abstraction:** GA operators are expressed as interfaces (`SelectionStrategy`, `CrossoverStrategy`, `MutationStrategy`, `ElitismStrategy`), separating *what the GA needs* from *how it is done*.
- **Strategy pattern:** Concrete classes (e.g., `TournamentSelection`, `OnePointCrossover`, `BitFlipMutation`, `SimpleElitism`) implement interchangeable operator behavior.
- **Separation of concerns:** The GA run loop, “one-generation evolution”, fitness evaluation, and reporting/plotting are separated into different packages/files.

Read `oop/README.md` for more implementation details.

---

## 4.2 FP Design (Python)
The FP version is implemented in Python under `fp/src/` and aims to express the GA as a set of **pure(ish) transformations** over immutable-looking data. Instead of encoding behavior in objects, behavior is expressed through **functions**, and configuration selects which functions are used.

Key FP goals reflected in the implementation:
- **Function composition / higher-order functions:** The GA is built by composing operator functions into an evolution step (e.g., creating `one_step` through `get_one_step_GA(...)`).
- **Immutability-by-copy:** Parent selection and operators return `.copy()` results and build new lists rather than mutating the original population in-place.
- **Declarative transformations:** Operators use `map(...)` and functional-style utilities to transform populations/gene strings.
- **Minimal side effects:** The primary side effects are restricted to CLI printing; the evolutionary operators themselves are structured as input → output functions.

### Data representation (FP)
- A chromosome is represented as a `(genestring, fitness)` tuple, where:
  - `genestring` is a list of booleans / bits
  - `fitness` is computed by the fitness function
- Populations are lists of these tuples, transformed into new populations each generation.

Read `fp/README.md` for more implementation details.

---

## 4.3 Plot Generation (both versions)
Both implementations generate the required fitness-evolution plots using a shared idea and near-identical scripts:

- OOP plot script: `oop/src/plot_ga_curve.py`
- FP plot script: `fp/src/plot_ga_curve.py`

The plotting script reads the exported JSON statistics and draws:
- Red line: **population max fitness** per generation
- Green line: **population average fitness**
- Green shaded area: **± 1 standard deviation** around the average fitness
- Black vertical dashed line: **generationOfOptimal** (the generation where the best fitness was first achieved)

In both versions, the PNG filename is derived from the JSON output name by appending `_curve.png`.

---

## 5. Problems Solved

## 5.1 OneMax
- **Chromosome:** bitstring length 100
- **Fitness:** count of `1` bits
- **Expected behavior:** fitness should increase quickly and (often) approach 100 within 300 generations.

## 5.2 0/1 Knapsack
- **Chromosome:** bitstring length = number of items (100)
- **Fitness:** total value if within capacity; otherwise **0**
- **Expected behavior:** fitness improves over generations but may plateau due to feasibility constraint and local optima.

---

## 6. Results and Reporting Artifacts

The `reports/` directory in the repository contains:
- `reports/results_onemax_oop.json`
- `reports/results_knapsack_oop.json`
- `reports/results_onemax_fp.json`
- `reports/results_knapsack_fp.json`
- `reports/results_onemax_oop_curve.png`
- `reports/results_knapsack_oop_curve.png`
- `reports/results_onemax_fp_curve.png`
- `reports/results_knapsack_fp_curve.png`

The following final results were printed by each implementation when running:

### OOP (Java)
- **OneMax**
  - Final best fitness: **100.0**
  - Execution time: **135.152657 ms**
- **Knapsack**
  - Final best fitness: **1857.0**
  - Execution time: **206.027027 ms**

### FP (Python)
- **OneMax**
  - Final best fitness: **100**
  - Execution time: **16845.593214035034 ms**
- **Knapsack**
  - Final best fitness: **1854**
  - Execution time: **19985.15796661377 ms**

*Note: Java's ~100× speed advantage is due to compiled bytecode vs Python interpretation; actual execution time also depends on hardware and system load.*

---

## 7. Testing
Both implementations include minimal unit tests under `oop/tests/` and `fp/tests/`

To test the stochastic operators:
- In OOP version: implement `FakeRandomUtil` that extends `RandomUtil` to inject controlled sequences
- In FP version: implement `fake_random_engine` that returns predetermined sequences passed as `rand_eng` parameter

This approach transforms inherently stochastic operators into deterministic, testable units by injecting controlled randomness.

The test scope targets core GA components:
- Fitness evaluation correctness
- Selection operator behavior (tournament)
- Crossover correctness (one-point)
- Mutation correctness (bit flip)
- Basic “improvement over generations” sanity check (fitness trend should improve from initial population)

---

## 8. Reflection: OOP vs FP Trade-offs
Both paradigms successfully express the same GA, but the development experience differs.

**OOP strengths:** The strategy-based design makes it intuitive to extend the GA with new operators or additional problems. Each component has a clearly defined responsibility, and state management is explicit through objects (e.g., `Population`, `Chromosome`). This helps readability for large projects and supports future extensibility (e.g., adding multi-point crossover or different elitism approaches) with minimal impact on existing code.

**OOP weaknesses:** There is more structural overhead (many classes/interfaces), and simple data transformations can become verbose. Debugging sometimes requires jumping through multiple abstractions.

**FP strengths:** The FP version can be more compact and direct: populations transform into new populations through composable functions. The reduced mutable state can simplify reasoning about correctness, since each function ideally depends only on its inputs. This fits GA well because evolution is naturally a repeated transformation step.

**FP weaknesses:** Without explicit objects, it can be harder to “name” and organize domain concepts, and the boundaries between concerns can become less obvious if not carefully structured. Additionally, if immutability is not consistently enforced, accidental side-effects can slip in and reduce the benefits.

Overall, **OOP** is strong for extensible architecture and “pluggable” components, while **FP** is strong for concise, testable transformations. Implementing both versions clarified how the same algorithm maps differently onto design choices.

---

## 9. Conclusion
This project demonstrates that a GA can be implemented using both OOP and FP while keeping identical configurations and problem definitions. The dual implementation highlights practical trade-offs in structure, extensibility, conciseness, and how state is managed. The produced JSON outputs and plots enable direct comparison of convergence behavior and performance across paradigms.