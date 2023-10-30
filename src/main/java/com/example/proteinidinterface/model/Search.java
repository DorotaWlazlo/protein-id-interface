package com.example.proteinidinterface.model;


import jakarta.persistence.*;

@Entity
public class Search {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email; // Connect this to the User repository
    private String title;
    private String databaseName;
    private String enzyme;
    private int missedCleavages;
    private String ptmFix;
    private String ptmVar;
    private double pepTol;
    private String pepTolUnit;
    private double fragTol;
    private String fragTolUnit;

    @Lob
    private byte[] uploadedFile;

    @Lob
    private byte[] resultFile;

    @Lob
    private String resultJSON;

    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "email", insertable = false, updatable = false)
    private User user;
}
