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

## 4.1 OOP Design (Java)
The OOP solution follows a component-based GA architecture (as required by the spec), where responsibilities are separated into distinct abstractions.

### Main ideas
- **Encapsulation:** GA state (population, individuals, configuration) is hidden behind class boundaries.
- **Modularity:** Operators (selection/crossover/mutation/elitism) can be swapped by changing strategy selection.
- **Extensibility:** Adding a new problem typically means adding a new fitness function and configuration.

### Conceptual class responsibilities (spec-aligned)
- `Chromosome`: stores genes (bitstring) and fitness
- `Population`: manages a collection of chromosomes
- `SelectionStrategy`: selects parents (tournament selection used)
- `CrossoverStrategy`: produces offspring (one-point crossover used)
- `MutationStrategy`: mutates genes (bit-flip mutation used)
- `GeneticAlgorithm`: orchestrates the evolution loop and elitism

### Execution / outputs
The OOP runner writes results into JSON files in `reports/`:
- `reports/results_onemax_oop.json`
- `reports/results_knapsack_oop.json`

These outputs are intended to support plotting of fitness curves and comparison against FP.

---

## 4.2 FP Design (Python)
The FP solution is organized around functions (no classes), aiming to keep operations **pure** and data **immutable** as much as practical.

### Main ideas
- **Pure functions:** selection, crossover, mutation, and fitness evaluation are implemented as composable functions.
- **Immutability:** rather than mutating individuals in-place, functions return new individuals/populations.
- **Higher-order functions:** evolution steps are naturally expressed using mapping and transformation.

### Execution / outputs
The FP runner calls a `main(...)` function with `--config` and `--out`, writing:
- `reports/results_onemax_fp.json`
- `reports/results_knapsack_fp.json`

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

### JSON outputs (produced by runs)
- OOP:
  - `reports/results_onemax_oop.json`
  - `reports/results_knapsack_oop.json`
- FP:
  - `reports/results_onemax_fp.json`
  - `reports/results_knapsack_fp.json`

### Plots (as required by spec)
- `reports/onemax_curve.png`
- `reports/knapsack_curve.png`

> Note: The repository run scripts ensure the `reports/` folder exists and place JSON outputs there. If the plot images are generated by a separate script/notebook, ensure they are saved with the filenames above.

---

## 7. Testing
Both implementations include minimal unit tests under:
- `oop/tests/`
- `fp/tests/`

The test scope targets core GA components:
- Fitness evaluation correctness
- Selection operator behavior (tournament)
- Crossover correctness (one-point)
- Mutation correctness (bit flip)
- Basic “improvement over generations” sanity check (fitness trend should improve from initial population)

---

## 8. Reflection (≤ 500 words): OOP vs FP Trade-offs
Both paradigms successfully express the same GA, but the development experience differs.

**OOP strengths:** The strategy-based design makes it intuitive to extend the GA with new operators or additional problems. Each component has a clearly defined responsibility, and state management is explicit through objects (e.g., `Population`, `Chromosome`). This helps readability for large projects and supports future extensibility (e.g., adding multi-point crossover or different elitism approaches) with minimal impact on existing code.

**OOP weaknesses:** There is more structural overhead (many classes/interfaces), and simple data transformations can become verbose. Debugging sometimes requires jumping through multiple abstractions.

**FP strengths:** The FP version can be more compact and direct: populations transform into new populations through composable functions. The reduced mutable state can simplify reasoning about correctness, since each function ideally depends only on its inputs. This fits GA well because evolution is naturally a repeated transformation step.

**FP weaknesses:** Without explicit objects, it can be harder to “name” and organize domain concepts, and the boundaries between concerns can become less obvious if not carefully structured. Additionally, if immutability is not consistently enforced, accidental side-effects can slip in and reduce the benefits.

Overall, **OOP** is strong for extensible architecture and “pluggable” components, while **FP** is strong for concise, testable transformations. Implementing both versions clarified how the same algorithm maps differently onto design choices.

---

## 9. Conclusion
This project demonstrates that a GA can be implemented using both OOP and FP while keeping identical configurations and problem definitions. The dual implementation highlights practical trade-offs in structure, extensibility, conciseness, and how state is managed. The produced JSON outputs and plots enable direct comparison of convergence behavior and performance across paradigms.