package com.ssonsal.football.team.entity;

import com.ssonsal.football.global.entity.BaseEntity;
import com.ssonsal.football.user.entity.User;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "team")
@ToString(exclude={"users", "teamRecord"})
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String name;

    @Column(length = 1000, unique = true)
    @NotNull
    private String logo;

    @ColumnDefault("'모든장소'")
    private String preferredArea;

    @ColumnDefault("'모든시간'")
    private String preferredTime;

    @ColumnDefault("1")
    private Integer recruit;

    @Column(length = 1000)
    private String intro;

    @ColumnDefault("-1")
    private Float mannerScore;

    @ColumnDefault("-1")
    private Float skillScore;

    @OneToOne(mappedBy = "team")
    private TeamRecord teamRecord;

    @Column(unique = true)
    @NotNull
    private Long leaderId;

    @OneToMany(mappedBy = "team")
    List<User> users = new ArrayList<>();

}
