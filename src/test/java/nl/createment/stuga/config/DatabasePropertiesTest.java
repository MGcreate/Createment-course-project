package nl.createment.stuga.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class DatabasePropertiesTest {
	
	@Autowired
	DatabaseProperties dbProperties;
	
	@BeforeAll
	void setup() {
		
	}

	@Test
	void test() {
		System.out.println(dbProperties.getUrl());
		System.out.println(dbProperties.getUsername());
		System.out.println(dbProperties.getPassword());
		assertNotNull(dbProperties.getUrl());
	}

}
