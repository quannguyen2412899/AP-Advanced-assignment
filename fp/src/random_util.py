import hashlib

def random_gen(*keys) -> int:
    """
    Returns a deterministic, stateless 'random' float [0, 1) 
    based on any number of provided keys.
    """
    input_str = ":".join(map(str, keys)).encode()
    return int.from_bytes(hashlib.sha256(input_str).digest(), 'big')

# def random_int(start: int, end: int ,*keys) -> int:
#     # [start, end)
#     if start >= end:
#         raise ValueError()
#     return random_gen(*keys) % (end - start) + start

def random_int(bound: int, *keys) -> int:
    # [start, end)
    if bound < 0:
        raise ValueError()
    return random_gen(*keys) % bound

def random_double(start: float, end: float, *keys) -> float:
    # [start, end)
    if start >= end:
        raise ValueError()
    return random_gen(*keys) * (end - start) / 2**256 + start

def random_bernoulli(p: float, *keys) -> bool:
    if p < 0 or p > 1:
        raise ValueError(f"Probability p must be between 0 and 1, got {p}")
    return random_double(0, 1, *keys) < p
