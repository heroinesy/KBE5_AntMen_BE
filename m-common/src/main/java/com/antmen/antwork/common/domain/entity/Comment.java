package com.antmen.antwork.common.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Comment {

    @Id
    private Long comment_id;

}
