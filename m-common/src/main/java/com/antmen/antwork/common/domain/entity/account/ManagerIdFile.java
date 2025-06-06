package com.antmen.antwork.common.domain.entity.account;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "manager_id_file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerIdFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long managerFileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(nullable = false)
    private String managerFileUrl;

}
