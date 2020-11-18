package com.kafka.test;


import com.kafka.consumer.KafkaConsumerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private KafkaConsumerService kafkaConsumerService;

	@Autowired
	private KafkaTemplate<String,Object> kafkaTemplate;
	
	@Test
	public void receive() {

	}	
	
}
