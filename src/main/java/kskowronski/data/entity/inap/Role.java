package kskowronski.data.entity.inap;

import javax.persistence.*;

@Entity
@Table(name = "NPP_ROLES")
public class Role {
    @Id
    @Column(name = "ROLE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NAME")
    private String name;
    public Integer getId() {
        return id;
    }

    public Role() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
