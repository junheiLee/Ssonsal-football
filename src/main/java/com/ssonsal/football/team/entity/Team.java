package com.ssonsal.football.team.entity;

import com.ssonsal.football.global.entity.BaseEntity;
import com.ssonsal.football.user.entity.User;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Table(name = "team")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String name;

    @Column(length = 1000)
    private String logo;

    private String preferredArea;

    private String preferredTime;

    private Integer recruit;

    @Column(length = 1000)
    private String intro;

    private Float mannerScore;

    private Float skillScore;

    @OneToOne(mappedBy = "team")
    private TeamRecord teamRecord;

    @Column(unique = true)
    @NotNull
    @Setter
    private Long leaderId;

    @OneToMany(mappedBy = "team")
    List<User> users = new ArrayList<>();
}
