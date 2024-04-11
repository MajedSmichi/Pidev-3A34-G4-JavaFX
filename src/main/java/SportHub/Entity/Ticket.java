package SportHub.Entity;

import java.util.Collection;

public class Ticket {
    private Integer id;
    private Integer prix;
    private String type;
    private Integer nbreTicket;

    private Integer evenementId;
    //private Collection<User> users;

    public Ticket() {
    }

    public Ticket(Integer id, Integer prix, String type, Integer nbreTicket, Integer evenementId) {
        this.id = id;
        this.prix = prix;
        this.type = type;
        this.nbreTicket = nbreTicket;
        this.evenementId = evenementId;
    }

    public Ticket(Integer id, Integer prix, String type, Integer nbreTicket) {
        this.id = id;
        this.prix = prix;
        this.type = type;
        this.nbreTicket = nbreTicket;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrix() {
        return prix;
    }

    public void setPrix(Integer prix) {
        this.prix = prix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNbreTicket() {
        return nbreTicket;
    }

    public void setNbreTicket(Integer nbreTicket) {
        this.nbreTicket = nbreTicket;
    }

    public Integer getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(Integer evenementId) {
        this.evenementId = evenementId;
    }

    // public Collection<User> getUsers() {
    //     return users;
    // }

    // public void setUsers(Collection<User> users) {
    //     this.users = users;
    // }


    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", prix=" + prix +
                ", type='" + type + '\'' +
                ", nbreTicket=" + nbreTicket +
                ", evenementId=" + evenementId +
                '}';
    }

}