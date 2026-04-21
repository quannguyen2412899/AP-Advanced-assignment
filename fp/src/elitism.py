from type_alias import Population, Chromosome
import heapq

def get_elitism_strategy(name: str, **kwargs):
    if name == "simpleElitism":
        count = kwargs.get("count", None)
        return lambda population: simple_elite_select(population, count)


def simple_elite_select(population: Population,
                        count: int) -> list[Chromosome]:
    if count >= len(population):
        return sorted(population, key=lambda x: x[1], reverse=True)
    
    # Use heapq.nlargest to efficiently get top k elements
    top_k = heapq.nlargest(count, population, key=lambda x: x[1])
    return [(genestring.copy(), fitness) for genestring, fitness in top_k]