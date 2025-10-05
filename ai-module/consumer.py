import asyncio
import signal
import argparse
import json
import orjson
from aio_pika import connect_robust
from aio_pika.abc import AbstractIncomingMessage, AbstractRobustConnection
from typing import Any, Dict, List, TypeAlias

from publisher import pub
from schema import PredictionMessage
from log_handler import MyLogger
from config import BROKER
from rl_predictor import model

logger = MyLogger()


async def con(conf) -> None:
    conn = await connect_robust(BROKER)
    async with conn:
        logger.info(" >>> Consuming >>>", "trace", "CONSUMING_STARTED", "con")
        queue = await broker_ready_up(conn, conf["listen"])
        async with queue.iterator() as queue_iter:
            async for message in queue_iter:
                async with message.process():
                    await message_handler(message, conf["send"])


async def broker_ready_up(conn: AbstractRobustConnection, listen_queue: str):
    channel = await conn.channel()
    await channel.set_qos(prefetch_count=1)
    # direct_logs_exchange = await channel.declare_exchange(
    #     "test", ExchangeType.DIRECT
    # )
    queue = await channel.declare_queue(listen_queue, passive=True, durable=True)
    # await queue.bind(direct_logs_exchange, routing_key="test")
    return queue


async def message_handler(message: AbstractIncomingMessage, send_queue: str):
    msg = _fetch_message(message)
    logger.info(f"", msg.trace_id, "PREDICTION_RECEIVED", "message_handler")
    await pub(send_queue, BROKER, _result_message(msg), msg.trace_id)


def _result_message(message: PredictionMessage):
    res = {
        "message": message.message,
        "trace_id": message.trace_id,
        "portfolio_id": message.portfolio_id,
    }
    return res


def _fetch_message(message: AbstractIncomingMessage) -> PredictionMessage:
    json_msg = json.loads(message.body.decode("utf8"))
    print(json_msg)
    _msg = PredictionMessage(**orjson.loads(message.body.decode("utf8")))
    logger.info(json_msg, _msg.trace_id, "INCOMING_MESSAGE", "_fetch_message")
    return _msg


def parse_args() -> Dict[str, Any]:
    parser = argparse.ArgumentParser()

    parser.add_argument("--rabbit", default=BROKER)
    parser.add_argument("--listen")
    parser.add_argument("--send")
    parser.add_argument("--exchange", default="")

    return vars(parser.parse_args())


def main():
    args = parse_args()
    loop = asyncio.new_event_loop()
    loop.set_debug(False)
    run_task = loop.create_task(con(args))

    def stop(_):
        nonlocal loop
        loop.stop()

    run_task.add_done_callback(stop)

    loop.add_signal_handler(signal.SIGTERM, run_task.cancel)
    loop.add_signal_handler(signal.SIGINT, run_task.cancel)
    try:
        loop.run_forever()
        if not run_task.cancelled():
            run_task.result()
    finally:
        logger.info("Hasta la vista", "trace", "EVENT_LOOP_KILLED", "main")
        loop.close()


if __name__ == "__main__":
    main()
