package com.zorvyn.finance.backend;

import com.zorvyn.finance.backend.dto.LoginRequest;
import com.zorvyn.finance.backend.dto.RegisterRequest;
import com.zorvyn.finance.backend.enums.Role;
import com.zorvyn.finance.backend.repository.FinancialRecordRepository;
import com.zorvyn.finance.backend.service.AuthService;
import com.zorvyn.finance.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FinanceBackendApplicationTests {

	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FinancialRecordRepository recordRepository;

	@BeforeEach
	void setUp() {
		// Delete records first (child table)
		// Then delete users (parent table)
		recordRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	void contextLoads() {
		// Verifies Spring context loads successfully
		assertNotNull(authService);
	}

	@Test
	void registerWithValidData_shouldReturnToken() {
		RegisterRequest request = new RegisterRequest();
		request.setName("Test User");
		request.setEmail("test@gmail.com");
		request.setPassword("123456");
		request.setRole(Role.ANALYST);

		var response = authService.register(request);

		assertNotNull(response.getToken());
		assertEquals("ANALYST", response.getRole());
		assertEquals("Test User", response.getName());
	}

	@Test
	void registerWithDuplicateEmail_shouldThrowException() {
		RegisterRequest request = new RegisterRequest();
		request.setName("Test User");
		request.setEmail("test@gmail.com");
		request.setPassword("123456");
		request.setRole(Role.ANALYST);

		// Register first time
		authService.register(request);

		// Register second time → should throw exception
		assertThrows(IllegalArgumentException.class, () -> {
			authService.register(request);
		});
	}

	@Test
	void loginWithWrongPassword_shouldThrowException() {
		// Register first
		RegisterRequest register = new RegisterRequest();
		register.setName("Test User");
		register.setEmail("test@gmail.com");
		register.setPassword("123456");
		register.setRole(Role.ANALYST);
		authService.register(register);

		// Login with wrong password
		LoginRequest login = new LoginRequest();
		login.setEmail("test@gmail.com");
		login.setPassword("wrongpassword");

		assertThrows(RuntimeException.class, () -> {
			authService.login(login);
		});
	}

	@Test
	void loginWithValidCredentials_shouldReturnToken() {
		// Register first
		RegisterRequest register = new RegisterRequest();
		register.setName("Test User");
		register.setEmail("test@gmail.com");
		register.setPassword("123456");
		register.setRole(Role.ADMIN);
		authService.register(register);

		// Login
		LoginRequest login = new LoginRequest();
		login.setEmail("test@gmail.com");
		login.setPassword("123456");

		var response = authService.login(login);

		assertNotNull(response.getToken());
		assertEquals("ADMIN", response.getRole());
	}
}