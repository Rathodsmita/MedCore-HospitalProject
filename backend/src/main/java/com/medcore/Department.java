package com.medcore;

import jakarta.persistence.*;

@Entity
@Table(name = "departments")
public class Department {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 100) private String name;

    protected Department() {}
    public Department(String name) { this.name = name; }
    public String getName() { return name; }
}
