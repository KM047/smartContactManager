package com.scm.smartContactManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.scm.smartContactManager.services.EmailService;

@SpringBootTest
class SmartContactManagerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private EmailService emailService;

	@Test
	void testEmailService() {
		emailService.sendEmail("kunalzyx1@gmail.com", "This is a test email from Smart Contact Manager",
				"This is a test email from Smart Contact Manager");
	}

}
