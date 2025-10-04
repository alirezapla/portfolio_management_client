from decouple import config

TRANSACTION_FEE_PERCENT = 0.001
DATASET = "data/top100.csv"
TICS_FILE = "data/top100.txt"

rabbit_user = config("rabbitmq_username", "guest")
rabbit_pass = config("rabbitmq_password", "guest")
rabbit_host = config("rabbitmq_addresses", "rabbitmq")
rabbit_vhost = config("rabbitmq_virtual_host", "ivs")
BROKER = f"amqp://{rabbit_user}:{rabbit_pass}@{rabbit_host}/{rabbit_vhost}"
