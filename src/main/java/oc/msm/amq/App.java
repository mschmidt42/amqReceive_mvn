package oc.msm.amq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.lang.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.*;

@Command(name = "amqReceive", mixinStandardHelpOptions = true, version = "amqReceive 0.8",
	description = "Reads a message to ActiveMQ")
public class App implements Callable<Integer> {
	private static Logger LOGGER = LoggerFactory.getLogger(App.class);

    @Option(names = {"-c", "--connection"}, description = "the conection to use. Default: \"tcp://localhost:61616\"")
    private String connectionUrl = "tcp://localhost:61616";

    @Option(names = {"-q", "--queue"}, description = "The destinantion queue.")
    private String queueName;

    @Option(names = {"-a", "--all"}, description = "Get all messages.")
    private boolean continueLoop = false;

	@Option(names = {"-p", "--props"}, description = "Show messahe properties")
    private boolean showProperties = false;

	@Option(names = {"-t", "--timeout"}, description = "Set the timeout")
    private Long timeout = (long) 1000;


    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String... args) {
    	int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
		var user = ActiveMQConnection.DEFAULT_USER;
		var password = ActiveMQConnection.DEFAULT_PASSWORD;

		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(connectionUrl);
		Connection connection = null;

		try {
			LOGGER.info("connecting ...");
			connection = connectionFactory.createConnection();
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(queueName);

			MessageConsumer consumer = session.createConsumer(destination);

			do {
				// Wait for a message
				Message message = consumer.receive(timeout);

				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					String text = textMessage.getText();
					System.out.println("Received: " + text + "\n");

					if(showProperties){
						var props = message.getPropertyNames();
						while(props.hasMoreElements()){
							String propertyName = String.valueOf(props.nextElement());
							System.out.println(propertyName + ": "+ message.getStringProperty(propertyName));
						}
					}

				} else {
					if(message == null){
						continueLoop = false;
						System.out.println("No more messages");
					} else {
						System.out.println("Received: " + message);
					}
				}
			} while(continueLoop);


            consumer.close();
            session.close();


		} catch (JMSException e) {
			LOGGER.error("ERROR: {}", e.getMessage());
			// e.printStackTrace();
		} finally {
			try {
				if(connection != null){
					LOGGER.info("closing connection ...");
					connection.close();
				}
			} catch (JMSException e) {
				LOGGER.error("Error closing connection: {}", e.getMessage());
			}
		}
		return 0;

    }

}
