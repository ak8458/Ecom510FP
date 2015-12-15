package itmd510.fp.model;

import itmd510.fp.helper.AdditionalPrice;

public class CartItem implements AdditionalPrice{
private Integer cartID;
private Product product;
private Integer quantity;
private Float price;
public Integer getCartID() {
	return cartID;
}
public void setCartID(Integer cartID) {
	this.cartID = cartID;
}

@Override
public Float calculatePrice() {
	// TODO Auto-generated method stub
	return TAX_PER*this.price;
}
public Product getProduct() {
	return product;
}
public void setProduct(Product product) {
	this.product = product;
}
public Integer getQuantity() {
	return quantity;
}
public void setQuantity(Integer quantity) {
	this.quantity = quantity;
}
public Float getPrice() {
	price=this.product.getProductPrice()*this.quantity;
	price=calculatePrice();
	return price;
}

}
