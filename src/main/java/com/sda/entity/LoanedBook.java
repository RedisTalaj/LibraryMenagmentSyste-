package com.sda.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name = "loanedBook")
public class LoanedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loaned_id")
    private int loaned_id;  // Ensure this matches the correct column in the database

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)  // Foreign key for the book
    private Book book;

    @Column(name = "loanedDate")
    private Date loanedDate;

    @Column(name = "returnDate")
    private Date returnDate;

}


