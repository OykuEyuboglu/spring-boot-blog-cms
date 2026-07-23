package com.oyku.blog.messaging.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.oyku.blog.Security.config.RabbitMQConfig;
import com.oyku.blog.messaging.dto.PostMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostProducer {
	
	private final RabbitTemplate rabbitTemplate;

	public void sendPostCreated(PostMessage message) {

		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, message);
	}
}
