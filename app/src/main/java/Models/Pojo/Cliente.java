package Models.Pojo;

public class Cliente {
    private String Apellido;
    private String Nombre;
    private String Email;
    private String Nid;
    private String Telefono;
    private String Direccion;
    private byte[]Bytes;

    public Cliente(String apellido, String nombre, String email, String nid, String telefono, String direccion, byte[] bytes) {
        Apellido = apellido;
        Nombre = nombre;
        Email = email;
        Nid = nid;
        Telefono = telefono;
        Direccion = direccion;
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

    public String getNid() {
        return Nid;
    }

    public String getTelefono() {
        return Telefono;
    }

    public String getDireccion() {
        return Direccion;
    }

    public byte[] getBytes() {
        return Bytes;
    }
}
