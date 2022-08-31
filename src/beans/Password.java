package beans;

public class Password {
	
	private String password = "";
	
	public Password(String password) {
		this.password = password;
	}
	
	public boolean isPassword(String st) {
		return st.equals(this.password);
	}
}
