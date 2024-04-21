package com.app.decaf.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "entries")
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EntryType type; // DEBIT or CREDIT

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category; // Associate each entry with a category

    @Column(nullable = false)
    private Date date;

    public enum EntryType {
        DEBIT, CREDIT
    }
}
