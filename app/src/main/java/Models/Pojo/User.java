package Models.Pojo;

public class User {
    private String Apellido;
    private String Nombre;
    private String Email;
    private String Role;

    public User(String apellido, String nombre, String email, String role) {
        Apellido = apellido;
        Nombre = nombre;
        Email = email;
        Role = role;
    }

    public String getApellido() {
        return Apellido;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getEmail() {
        return Email;
    }

    public String getRole() {
        return Role;
    }
}
