package com.example.proteinidinterface.model;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "SEARCHES")
public class Search {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private byte[] uploadedFile;
    @Lob
    private byte[] resultFile;
    private String title;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public Search(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public byte[] getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(byte[] uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public byte[] getResultFile() {
        return resultFile;
    }

    public void setResultFile(byte[] resultFile) {
        this.resultFile = resultFile;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
