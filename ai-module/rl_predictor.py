from random import randrange


class RlModelMock:
    def __init__(self):
        pass

    def predict(self, symbols: list):
        predictions = ""
        for symbol in symbols:
            predict = randrange(-100, 100)
            predictions += f"{symbol}-{predict},"
        return predictions[:-1]


model = RlModelMock()
