import hashlib

def random_gen(*keys) -> int:
    """
    Returns a deterministic, stateless 'random' float [0, 1) 
    based on any number of provided keys.
    """
    input_str = ":".join(map(str, keys)).encode()
    return int.from_bytes(hashlib.sha256(input_str).digest(), 'big')


def random_int(bound: int, *keys, random_engine = random_gen) -> int:
    # [start, end)
    if bound < 0:
        raise ValueError()
    return random_engine(*keys) % bound


def random_double(start: float, end: float, *keys, random_engine = random_gen) -> float:
    # [start, end)
    if start >= end:
        raise ValueError()
    return random_engine(*keys) * (end - start) / 2**256 + start


def random_bernoulli(p: float, *keys, random_engine = random_gen) -> bool:
    if p < 0 or p > 1:
        raise ValueError(f"Probability p must be between 0 and 1, got {p}")
    return random_double(0, 1, *keys, random_engine=random_engine) < p
