# amqReceive

get message from ActiveMq (maven)


Help:
```
Usage: amqReceive [-hV] [-c=<connectionUrl>] [-q=<queueName>]
Reads a message to ActiveMQ
  -c, --connection=<connectionUrl>
                            the conection to use. Default: "tcp://localhost:
                              61616"
  -h, --help                Show this help message and exit.
  -q, --queue=<queueName>   The destinantion queue.
  -V, --version             Print version information and exit.

```

Used Libraries:
* activemq-client
* picocli
