package com.quizly.quizs.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quizs")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String owner;
    private Instant createdAt;
    private Instant expiresAt;
    private Long attempts;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "quiz", fetch = FetchType.EAGER)
    private List<Question> questions;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "quiz", fetch = FetchType.EAGER)
    private List<Result> results;
}