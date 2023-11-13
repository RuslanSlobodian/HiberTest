package ua.te.tk.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private Name name;

    @Column(name = "notes")
    private String notes;

    @Column(name = "phone")
    private String phone;

    @Column(name = "starred")
    private boolean starred;

    public Contact() {
    }

    public Contact(Name name, String notes, String phone, boolean starred) {
        this.name = name;
        this.notes = notes;
        this.phone = phone;
        this.starred = starred;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name=" + name +
                ", notes='" + notes + '\'' +
                ", phone=" + phone +
                ", starred=" + starred +
                '}';
    }
}
