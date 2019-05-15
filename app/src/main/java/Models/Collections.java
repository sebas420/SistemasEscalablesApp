package Models;

import java.util.HashMap;
import java.util.Map;

public class Collections {
    public static class Usuarios{
        public static String APELLIDO = "Apellido";
        public static String EMAIL = "Email";
        public static String NOMBRE = "Nombre";
        public static String ROLE = "Role";
        public static String USUARIOS = "Usuarios";
    }
    public static class Clientes{
        public static String APELLIDO = "Apellido";
        public static String EMAIL = "Email";
        public static String NOMBRE = "Nombre";
        public static String NID = "Nid";
        public static String TELEFONO = "Telefono";
        public static String DRECCION = "Direccion";
        public static String CLIENTES = "Clientes";
    }
    public static class Report_clientes {
        private static Map<String, Object> report = new HashMap<>();
        public static String DEUDA = "Deuda";
        public static String FECHADEUDA = "Fecha_deuda";
        public static String PAGO = "Pago";
        public static String FECHAPAGO = "Fecha_pago";
        public static String TICKET = "Ticket";
        public static String ReportClientes = "Report_clientes";

        public static Map<String, Object> getReport() {
            report.put(DEUDA,"$0.00");
            report.put(FECHADEUDA,"--/--/--");
            report.put(PAGO,"$0.00");
            report.put(FECHAPAGO,"--/--/--");
            report.put(TICKET,"0000000000");
            return report;
        }
    }
}