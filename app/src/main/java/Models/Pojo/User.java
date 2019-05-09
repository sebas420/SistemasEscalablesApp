package Models.Pojo;

public class User {
    private String Apellido;
    private String Nombre;
    private String Email;
    private String Role;
    private byte[]Bytes;

    public User(String apellido, String nombre, String email, String role,byte[]bytes) {
        Apellido = apellido;
        Nombre = nombre;
        Email = email;
        Role = role;
        Bytes = bytes;
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

    public byte[] getBytes() {
        return Bytes;
    }
}
