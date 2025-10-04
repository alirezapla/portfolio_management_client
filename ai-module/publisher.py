import orjson
from aio_pika import DeliveryMode, ExchangeType, Message, connect_robust

from log_handler import MyLogger

SEVERITY = "info"

logger = MyLogger()


async def pub(queue_name: str, host: str, message: dict, trace_id: str) -> None:
    conn = await connect_robust(host)
    async with conn:
        channel = await conn.channel(publisher_confirms=True)
        queue = await channel.declare_queue(queue_name, passive=True)
        logger.info(queue_name, trace_id, "NEXT_QUEUE", "pub")
        logger.info(message, trace_id, "MESSAGE_READY_TO_PUBLISH", "pub")
        await channel.default_exchange.publish(
            Message(body=orjson.dumps(message), delivery_mode=DeliveryMode.PERSISTENT),
            routing_key=queue_name,
        )
