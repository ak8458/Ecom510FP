package itmd510.fp.model;

public class Customer extends User{
	
	public Customer(Integer userID, String uPassword, String uFirstName,
			String uLastName, String uAddress, Character uRole) {
		super(userID, uPassword, uFirstName, uLastName, uAddress, uRole);
		// TODO Auto-generated constructor stub
	}
	public Customer(){
		super();
	}
}
