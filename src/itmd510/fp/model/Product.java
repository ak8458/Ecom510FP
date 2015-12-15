package itmd510.fp.model;

public class Product {

	private Integer productID; 
	private String productName ;
	private String productManufacturer; 
	private Float productPrice;
	private Character productFreeShipp;
	private Integer productsInStore;
	
	public Product(Integer productID, String productName,
			String productManufacturer, Float productPrice,
			Character productFreeShipp, Integer productsInStore) {
		super();
		this.productID = productID;
		this.productName = productName;
		this.productManufacturer = productManufacturer;
		this.productPrice = productPrice;
		this.productFreeShipp = productFreeShipp;
		this.productsInStore = productsInStore;
	}
	
	public Product() {
		// TODO Auto-generated constructor stub
	}

	public Integer getProductID() {
		return productID;
	}
	public void setProductID(Integer productID) {
		this.productID = productID;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductManufacturer() {
		return productManufacturer;
	}
	public void setProductManufacturer(String productManufacturer) {
		this.productManufacturer = productManufacturer;
	}
	public Float getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(Float productPrice) {
		this.productPrice = productPrice;
	}
	public Character getProductFreeShipp() {
		return productFreeShipp;
	}
	public void setProductFreeShipp(Character productFreeShipp) {
		this.productFreeShipp = productFreeShipp;
	}
	public Integer getProductsInStore() {
		return productsInStore;
	}
	public void setProductsInStore(Integer productsInStore) {
		this.productsInStore = productsInStore;
	}
	
	
}