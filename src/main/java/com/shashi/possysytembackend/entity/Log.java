package com.shashi.possysytembackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Action action;

    @Column(nullable = false, length = 100)
    private String targetTableName;

    @Column(length = 50)
    private String targetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by", nullable = false)
    private User performedBy;

    @Column(nullable = false)
    private LocalDateTime actionDate;

    @Column(columnDefinition = "TEXT")
    private String details;

    @PrePersist
    public void prePersist() {
        if (actionDate == null) {
            actionDate = LocalDateTime.now();
        }
    }

    public enum Action {
        CREATE, READ, UPDATE, DELETE
    }
}
