package com.bookstore.inventory.controller;

// Java
import java.util.Map;

// Spring
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

// This Service
import com.bookstore.inventory.component.BookComponent;
import com.bookstore.inventory.entity.BookRecord;


@RestController
public class BookController {
	private static final Logger logger = LoggerFactory.getLogger(BookController.class);
	BookComponent bookComponent;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	BookController(BookComponent bookComponent){
		this.bookComponent = bookComponent;
	}
	
	@RequestMapping(method = RequestMethod.GET,  value= "/")
	Iterable<BookRecord> getAll() {
		logger.info("in getAll(()");
		return this.bookComponent.listAllBooks();
	}
	
	@RequestMapping(method = RequestMethod.GET,  value = "/books")
	Iterable<BookRecord> getBooks() {
		logger.info("in getBooks(()");
		return this.bookComponent.listAllBooks();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/book/{Id}")
	BookRecord getBook(@PathVariable Integer Id) {
		logger.info("in getBook(()");
		return this.bookComponent.getBookById(Id);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "savebook")
    void saveBook(@RequestBody BookRecord book){
		logger.info("in saveBook(()");
		logger.info("Book :" + book.toString());
		bookComponent.saveBook(book);
    }
	
	@RequestMapping(method = RequestMethod.POST, value = "deletebook")
   void deleteRecord(@RequestBody Integer id){
		logger.info("in deleteRecord() Id: " + id);
		bookComponent.deleteBook(id);
    }
}
