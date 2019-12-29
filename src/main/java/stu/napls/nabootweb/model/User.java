package stu.napls.nabootweb.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "web_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

}
