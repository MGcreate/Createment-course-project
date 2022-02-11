package nl.createment.stuga.security;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BasicConfigurationIntegrationTest {

	TestRestTemplate restTemplate;
	URL base;
	@LocalServerPort
	int port;

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@BeforeEach
	public void setup() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).apply(springSecurity()).build();
		
	}

	@BeforeAll
	public void setUp() throws MalformedURLException, Exception {
		restTemplate = new TestRestTemplate("user", "password");
		base = new URL("http://localhost:" + port);
	}

	@Test
	public void HomePageTest() throws IllegalStateException, IOException {
		ResponseEntity<String> response = restTemplate.getForEntity(base.toString(), String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void LoginPageTest() throws Exception {
		URL loginPage = new URL(base + "/login");
		ResponseEntity<String> response = restTemplate.getForEntity(loginPage.toString(), String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void Login_Test_Succes() throws Exception {
		this.mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"admin\",\"password\":\"Admin12!\"}")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
		this.mvc.perform(formLogin("/login").user("username", "admin").password("password", "Admin12!"))
				.andExpect(status().isFound()).andExpect(redirectedUrl("/home"))
				.andExpect(authenticated().withUsername("admin"));
	}

	@Test
	public void Login_Test_failed() throws Exception {
		this.mvc.perform(formLogin("/login").user("username", "admin").password("password", "invalid"))
				.andExpect(status().isFound()).andExpect(redirectedUrl("/login?error"))
				.andExpect(unauthenticated());
	}

}