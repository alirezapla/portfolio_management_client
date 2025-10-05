import json
import logging


class MyLogger:
    _logger: logging = None

    def __new__(cls, *args, **kwargs) -> logging:
        if cls._logger is None:
            cls._logger = super().__new__(cls, *args, **kwargs)
            cls._logger = logging.getLogger("Portfolio")
            cls._logger.setLevel(logging.INFO)
            formatter = logging.Formatter(
                '{"date":"%(asctime)s","trace_id": "%(trace_id)s","event":"%(event)s","level_name":"%(levelname)s","method_name":"%(method_name)s","request":" ","payload": "%(message)s"}'
            )
            streamHandler = logging.StreamHandler()
            streamHandler.setFormatter(formatter)
            cls._logger.addHandler(streamHandler)

        return cls

    @classmethod
    def info(cls, message: str, trace_id: str, event: str, method_name: str):
        cls._logger.info(
            message,
            extra={
                "trace_id": f"{trace_id}",
                "event": f"{event}",
                "method_name": f"{method_name}",
            },
        )

    @classmethod
    def warn(cls, message: str, trace_id: str, event: str, method_name: str):
        cls._logger.warn(
            message,
            extra={
                "trace_id": f"{trace_id}",
                "event": f"{event}",
                "method_name": f"{method_name}",
            },
        )

    @classmethod
    def error(cls, message: str, trace_id: str, event: str, method_name: str):
        cls._logger.error(
            message,
            extra={
                "trace_id": f"{trace_id}",
                "event": f"{event}",
                "method_name": f"{method_name}",
            },
        )

    @classmethod
    def debug(cls, message: str, trace_id: str, event: str, method_name: str):
        cls._logger.debug(
            message,
            extra={
                "trace_id": f"{trace_id}",
                "event": f"{event}",
                "method_name": f"{method_name}",
            },
        )