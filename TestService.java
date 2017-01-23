package inventory;

// Java
import java.util.Arrays;

//Spring
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.Assert.assertEquals;

//static methods for mocking HTTP request and response
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// This Service

import com.bookstore.inventory.Application;
import com.bookstore.inventory.component.BookComponent;
import com.bookstore.inventory.entity.BookRecord;

/* Annotations
	@RunWith - Junit4ClassRunner class loads Spring application context
	           for use in a JUnit test and enables autowiring of beans into test class
	@SpringApplicationConfiguration comprises of
		@ContextCofiguration - Loads Spring Application Context in MvcApplication class
		                       in the same as if it was being loaded in a Prod application
	@WebAppConfiguration - Enables web context testing by declaring application context created
	                       by JUnit should a WebApplicationContext not ApplicationContext

*/
@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
@WebAppConfiguration

public class TestService {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
    /*
    	Inject WebApplication Context
    */
    @Autowired
    private WebApplicationContext webContext;
    @Autowired
	private BookComponent bookComponent;
    private MockMvc mockMvc;

	/*
	   This method should executed Before any test methods
    */
	@Before
	public void setupMockMvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
	}
	
	// Presentation Layer

	@Test
	public void homePage() throws Exception {
	
	// As static imports are defined code can can be compacted
		mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			;
	}

	@Test
	public void booksPage() throws Exception {
		mockMvc.perform(get("/books"))
			.andExpect(status().isOk())
			;
	}
	/*
	@Test
	public void getAbookPage() throws Exception {
		mockMvc.perform(get("book/1"))
			.andExpect(status().isOk())
			;
	}
	*/

	@Test
	public void saveBook() throws Exception {
		mockMvc.perform(post("savebook"));
	}
	
	@Test
	public void deleteBook() throws Exception {
		mockMvc.perform(post("deletebook"));
	}


	// Service Layer
	@Test
	public void testService() {
		logger.info("Starting Test Service"); // testService does not write to report/test/index.html
		bookComponent.initialize(); 
		logger.info("End of Test Service");
	}

}

