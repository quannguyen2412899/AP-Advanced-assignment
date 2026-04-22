def fake_random_engine(const_seq: list):
    iterator = iter(const_seq)
    return lambda *_: next(iterator, None)
