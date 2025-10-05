from random import randrange


class RlModelMock:
    def __init__(self):
        pass

    def predict(self, stocks: list):
        return [randrange(-100, 100) for _ in range(len(stocks))]


model = RlModelMock()
