package ir.tiroon.echo.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Proxy

import javax.persistence.*

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "User")
@Proxy(lazy = false)
class User implements Serializable {

    @Id
    @Column(nullable = false, unique = true)
    String phoneNumber

    @Column(nullable = false)
    String name

    @JsonIgnore
    @Column(nullable = false)
    String password

    @Column(unique = true, nullable = false)
    String email

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "authorities",
            joinColumns = @JoinColumn(name = "phoneNumber"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    Set<Role> roles = new HashSet<>()

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    State state = State.Active

    User() {
    }

    @JsonCreator
    User(@JsonProperty("name") String name, @JsonProperty("password") String password,
         @JsonProperty("email") String email, @JsonProperty("phoneNumber") String phoneNumber) {
        this.name = name
        this.password = password
        this.email = email
        this.phoneNumber = phoneNumber
    }

    User(String phoneNumber, String name, String password, String email, Set<Role> roles, State state) {
        this.phoneNumber = phoneNumber
        this.name = name
        this.password = password
        this.email = email
        this.roles = roles
        this.state = state
    }

    String getPhoneNumber() {
        phoneNumber
    }

    void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber
    }

    String getName() {
        name
    }

    void setName(String name) {
        this.name = name
    }

    String getPassword() {
        password
    }

    void setPassword(String password) {
        this.password = password
    }

    String getEmail() {
        email
    }

    void setEmail(String email) {
        this.email = email
    }

    Set<Role> getRoles() {
        roles
    }

    void setRoles(Set<Role> roles) {
        this.roles = roles
    }

    State getState() {
        state
    }

    void setState(State state) {
        this.state = state
    }
}
