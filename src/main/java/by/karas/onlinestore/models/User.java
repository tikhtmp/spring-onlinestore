package by.karas.onlinestore.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class User {
    private Long id;

    @NotEmpty(message = "The field can not be empty")
    @Size(min = 2, max = 10, message = "Login must contains from 2 to 10 characters.")
    private String login;

    @NotEmpty(message = "The field can not be empty")
    @Size(min = 1, max = 10, message = "Password must contains from 1 to 10 characters.")
    private String password;

    private Role role;

    public User() {
    }

    public User(Long id, String login, String password, Role role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
