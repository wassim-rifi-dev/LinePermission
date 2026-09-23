package ma.youcode.lineperm.models;

public class User {
    private Long id;
    private String username;
    private String password;

    public User(String username , String password) {
        setUsername(username);
        setPassword(password);
    }

    public User(long id, String username , String password) {
        setUsername(username);
        setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
