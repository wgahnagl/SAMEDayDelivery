package app.model;

public class User {
    private String firstname;
    private String lastname;
    private String email;

    public User(String email){
        this.email = email;
    }


    public String getFirstname(){return firstname;}
    public String getLastname(){return lastname;}
    public String getEmail(){return email;}
    public void setFirstname(String firstname){this.firstname = firstname;}
    public void setLastName (String lastName){this.lastname = lastName;}
    public void setEmail(String email){this.email = email;}
}
