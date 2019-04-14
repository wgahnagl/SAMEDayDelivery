package app.model;

public class User {
    private String firstName;
    private String lastName;
    private String email;

    public User(String email, String firstname, String lastname){
        this.email = email;
        this.firstName = firstname;
        this.lastName = lastname;
    }


    public String getFirstname(){return firstName;}
    public String getLastname(){return lastName;}
    public String getEmail(){return email;}
    public void setFirstname(String firstname){this.firstName = firstname;}
    public void setLastName (String lastName){this.lastName = lastName;}
    public void setEmail(String email){this.email = email;}
}
