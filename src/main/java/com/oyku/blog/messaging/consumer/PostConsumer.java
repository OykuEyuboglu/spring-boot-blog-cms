package com.oyku.blog.messaging.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.oyku.blog.Security.config.RabbitMQConfig;
import com.oyku.blog.messaging.dto.PostMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostConsumer.class);

	@RabbitListener(queues = RabbitMQConfig.QUEUE)
	public void receivePost(PostMessage message) {
		LOGGER.info("Post received : {}", message);
	}
}
