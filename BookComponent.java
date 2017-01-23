package com.bookstore.inventory.component;

// Java
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.lang.Integer;

// Spring
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

// This Service
import com.bookstore.inventory.entity.BookRecord;
import com.bookstore.inventory.repository.BookRepository;


@Component
public class BookComponent {
	private static final Logger logger = LoggerFactory.getLogger(BookComponent.class);
	private static final String ConfigURL = "http://localhost:8000/getValue/";
	private static final String MessageQProperty = "InventoryMessageQName";
	
	BookRepository bookRepository;
	Sender sender;
	
	@Autowired
	public BookComponent (BookRepository bookRepository, Sender sender){
		this.bookRepository = bookRepository;
		this.sender = sender;
	
	}
	
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

	public Iterable<BookRecord> listAllBooks() {
		return bookRepository.findAll();
	}

	public BookRecord getBookById(Integer id) {
		return bookRepository.findOne(id);
    }

	public long saveBook(BookRecord book) {
    	logger.info("Saving book - id: " + book.getId() + " name: " + book.getDescription());
		return this.create(book);
    }
	
	public void deleteBook(Integer id) {
		logger.info("in delete() id: " + id);
		bookRepository.delete(id);
    }

	private long create(BookRecord record) {
	   logger.info("creating book");
	   long id = bookRepository.save(record).getId();
	   logger.info("Successfully saved book"); 
	   logger.info("Sending an event");
	   Map<String, Object> bookDetails = new HashMap<String, Object>();
	    StringBuilder sb = new StringBuilder();
	    sb.append("{id:");
	    sb.append(record.getId());
	    sb.append(", description:");
	    sb.append(record.getDescription());
	    sb.append(", price:");
	    sb.append(record.getPrice());
	    sb.append(", qunatity:");
	    sb.append(record.getQuantity());
	    bookDetails.put((record.getId().toString()),sb.toString());
	    try {
	    	sender.send(bookDetails);
	    } catch (Exception e) {
	    	logger.warn("Exception Message Send");
	    	logger.error(e.getMessage());
	    }
	   logger.info("Created an event successfully delivered for "+ sb.toString());	   
	   return id;
	}
	
	public void initialize() {
		// Check InventoryQ Name
		RestTemplate restTemplate = new RestTemplate();
    	ResponseEntity<String> response = restTemplate.getForEntity(ConfigURL + MessageQProperty, String.class);
    	if(response != null && response.getStatusCode() == HttpStatus.OK) {
			String qName = response.getBody().toString();
			logger.info("qName: " + qName);
			if (qName.contentEquals("InventoryQ")) {
				logger.info("InventoryQ name matches");
			} else {
				logger.warn("InventoryQ does not match");
			}
		} else {
			logger.warn("GET request for Inventory Message Q Name failed");
		}
		
		// Build Inventory
		BookRecord[] books = {
			new BookRecord("Growing Dahlias in Colorado", "19.95", new Integer(10)),
			new BookRecord("Colorado 14ers", "29.95", new Integer(14)),
			new BookRecord("Rocky Mountains Parks","9.95", new Integer(7)),
			new BookRecord("Biking along Cherry Creek Trail","14.95", new Integer(3))				
		};
		Arrays.asList(books).forEach(book -> this.create(book));
		logger.info("Books successfully saved...");
		
		// Check Inventory
		logger.info("Books successfully saved...");
		logger.info("Get One Book...");
		logger.info(getBookById(new Integer(1)).toString());
		logger.info("Got the book ...");
		
	}
}


