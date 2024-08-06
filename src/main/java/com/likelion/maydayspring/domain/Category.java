package com.likelion.maydayspring.domain;

import com.likelion.maydayspring.enums.OfficeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @OneToOne(mappedBy = "category")
    private Location location;

    private boolean monitor;

    private boolean conferenceRoom;

    @Enumerated(EnumType.STRING)
    private OfficeType officeType;

    private boolean parking;

    private boolean phoneBooth;
}
