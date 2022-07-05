# amqSend

send data to ActiveMq (maven)


Help:
```
Usage: amqSend [-r] [-c=<connectionUrl>] [-h=<headerName>] [-q=<queueName>] [-v=<headerValue>] <file>
Sends a message to ActiveMQ
      <file>                A (JSON) file containing the data to send as text
  -c, --connection=<connectionUrl>  the conection to use. Default: "tcp://localhost:61616"
  -h, --header-name=<headerName>    the header name to send. Default: "JMSType"
  -v, --header-value=<headerValue>  the header value to send. Default: "unknown"
  -q, --queue=<queueName>           The destinantion queue.
  -r, --remove-nl                   remove "\n", "\r" and "\t" characters from messahe.

```

Used Libraries:
* activemq-client
* picocli
