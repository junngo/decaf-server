package com.app.decaf.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "transaction")
    private List<Entry> entries;

    @Column
    private String description;

    @Column(nullable = false)
    private Date date;
}
