package itmd510.fp.model;

import itmd510.fp.helper.AdditionalPrice;

import java.util.Date;

public class Order implements AdditionalPrice{
private Integer orderID;
private Integer productID;
private Integer customerID;
private Integer quantity;
private Date orderDate;
private Float price;
private String productName;
private char status;

public Order(Integer orderID, Integer productID, Integer customerID,
		Integer quantity, Date orderDate, Float price, String productName,
		char status) {
	super();
	this.orderID = orderID;
	this.productID = productID;
	this.customerID = customerID;
	this.quantity = quantity;
	this.orderDate = orderDate;
	this.price = price;
	this.productName = productName;
	this.status = status;
}

public String getProductName() {
	return productName;
}
public void setProductName(String productName) {
	this.productName = productName;
}
public char getStatus() {
	return status;
}
public void setStatus(char status) {
	this.status = status;
}
@Override
public Float calculatePrice() {
	// TODO Auto-generated method stub
	return (SHIPPING_COST+this.price);
}
public Order(){
	super();
}


public Integer getOrderID() {
	return orderID;
}
public void setOrderID(Integer orderID) {
	
	this.orderID = orderID;
}
public Integer getProductID() {
	return productID;
}
public void setProductID(Integer productID) {
	this.productID = productID;
}
public Integer getCustomerID() {
	return customerID;
}
public void setCustomerID(Integer customerID) {
	this.customerID = customerID;
}
public Integer getQuantity() {
	return quantity;
}
public void setQuantity(Integer quantity) {
	this.quantity = quantity;
}
public Date getOrderDate() {
	return orderDate;
}
public void setOrderDate(Date orderDate) {
	this.orderDate = orderDate;
}
public Float getPrice() {
	price=calculatePrice();
	return price;
}
public void setPrice(Float price) {
	this.price = price;
}
}
