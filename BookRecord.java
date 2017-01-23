package com.bookstore.inventory.entity;

// Java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BookRecord  {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    private String description;
    private String price;
    private Integer quantity;
    
    public BookRecord() { 
	}
    
    // Construct BookRecord from Json
	public BookRecord(String json) {
		BookRecord bookRecord = new BookRecord();
		List<String> tokens = new ArrayList<>(Arrays.asList(json.split("=")));
		//logger.info("Tokens " + tokens);
		for (int j = 0; j < tokens.size(); j++) {
			if(j==2) {
				bookRecord.setDescription(tokens.get(j).replace(", price",""));
			}else if (j==3) {
				bookRecord.setPrice(tokens.get(j).replace(", qunatity", ""));
			} else if (j==4) {
				bookRecord.setQuantity(new Integer(tokens.get(j).replace("}", "")));
			}
		}
	}

	public BookRecord(String description, String price, Integer quantity) {
		this.description = description;		
		this.price = price;
		this.quantity = quantity;
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String toString() {
    	return (this.getId()+ " "  + this.getDescription() + " " + this.getPrice() + " " + this.getQuantity());
    }
    
}
