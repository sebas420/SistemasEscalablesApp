package Library;

public class Codes {
    private String tickets = null;
    public String codesTickets(String ticket){
        if (ticket.equals("9999999999")){
            tickets = "0000000001";
        }else{
            int num = Integer.valueOf(ticket);
            num++;
            tickets = String.format("%010d", num);
        }
        return tickets;
    }
}
