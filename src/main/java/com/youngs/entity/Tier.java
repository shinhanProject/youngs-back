package com.youngs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="tier")
@EntityListeners(AuditingEntityListener.class)
public class Tier {
    @Id
    @Column(name = "tier_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tierSeq; // 티어 인덱스

    @Column(name="tier_name", nullable = false)
    private String tierName; //단어 명

    @Column(name="low_point", nullable = false)
    private int lowPoint; //하한 포인트

    @Column(name="high_point", nullable = false)
    private int highPoint; //상한 포인트
}
