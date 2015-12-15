package itmd510.fp.model;

public class User {
private Integer userID;

private String uPassword;
private String uFirstName;
private String uLastName;
private String uAddress;
private Character uRole;

public User(Integer userID, String uPassword, String uFirstName,
		String uLastName, String uAddress, Character uRole) {
	super();
	this.userID = userID;
	this.uPassword = uPassword;
	this.uFirstName = uFirstName;
	this.uLastName = uLastName;
	this.uAddress = uAddress;
	this.uRole = uRole;
}
public User() {
	// TODO Auto-generated constructor stub
}
public Integer getUserID() {
	return userID;
}
public void setUserID(Integer userID) {
	this.userID = userID;
}
public String getuPassword() {
	return uPassword;
}
public void setuPassword(String uPassword) {
	this.uPassword = uPassword;
}
public String getuFirstName() {
	return uFirstName;
}
public void setuFirstName(String uFirstName) {
	this.uFirstName = uFirstName;
}
public String getuLastName() {
	return uLastName;
}
public void setuLastName(String uLastName) {
	this.uLastName = uLastName;
}
public String getuAddress() {
	return uAddress;
}
public void setuAddress(String uAddress) {
	this.uAddress = uAddress;
}
public Character getuRole() {
	return uRole;
}
public void setuRole(Character uRole) {
	this.uRole = uRole;
}
}
