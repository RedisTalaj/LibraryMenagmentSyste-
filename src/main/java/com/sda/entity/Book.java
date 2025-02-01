package com.sda.entity;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long book_id;

    @Column(name = "tittle") // corrected the spelling
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "sasia")
    private int sasia;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<LoanedBook> loanedBooks;
}

