from random import randrange


class RlModelMock:
    def __init__(self):
        pass

    def predict(self, symbols: list):
        predictions = ""
        for symbol in symbols:
            predict = randrange(-300, 100)
            if predict < 0:
                predict = f"-{abs(predict)}"
            else:
                predict = f"+{abs(predict)}"
            predictions += f"{symbol}_{predict},"
        return predictions[:-1]


model = RlModelMock()
