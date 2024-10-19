package com.sda;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "book")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private int id;
    @Column(name = "tittle")
    private String tittle;
    @Column(name = "author")
    private String author;
    @Column(name = "saisa")
    private int sasia;

}