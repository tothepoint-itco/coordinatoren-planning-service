package company.tothepoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class PlanningApplication {
	private static final String PLANNING_QUEUE = "planning-queue";
	private static final String CONTRACT_EXCHANGE = "contract-exchange";
	private static final String CONTRACT_ROUTING = "contract-routing";

	public static void main(String[] args) {
		SpringApplication.run(PlanningApplication.class, args);
	}

	@Bean
	String queueName() {
		return PLANNING_QUEUE;
	}

	@Bean
	Queue queue() {
		return new Queue(PLANNING_QUEUE, true, false, false);
	}

	@Bean
	Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
		Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
		jackson2JsonMessageConverter.setJsonObjectMapper(objectMapper);
		return jackson2JsonMessageConverter;
	}

	@Bean
	TopicExchange bediendeTopicExchange() {
		return new TopicExchange(CONTRACT_EXCHANGE, true, false);
	}

	@Bean
	Binding bediendeBinding(Queue queue, TopicExchange bediendeTopicExchange) {
		return BindingBuilder.bind(queue).to(bediendeTopicExchange).with(CONTRACT_ROUTING);
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jackson2JsonMessageConverter, Receiver receiver) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(PLANNING_QUEUE);
		container.setMessageConverter(jackson2JsonMessageConverter);
		container.setMessageListener(receiver);
		return container;
	}

	@Bean
	Receiver receiver() {
		return new Receiver();
	}
}
