package com.ssonsal.football.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.ssonsal.football.global.entity.BaseEntity;
import com.ssonsal.football.team.entity.Team;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table
@DynamicInsert
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "email", unique = true)
    private String email;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "position")
    private String position;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "intro")
    private String intro;

    @Column(name = "preferred_area")
    private String preferredArea;

    @Column(name = "preferred_time")
    private String preferredTime;


    @Column(name = "role")
    private int role;

    @Column(name = "skill_score")
    private Float skillScore;

    @Column(name = "manner_score")
    private Float mannerScore;

    @Builder
    public User(String email, String password, String auth) {
        this.email = email;
        this.password = password;
    }

    public void updateRole(int role) {
        this.role = role;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    /**
     * security 에서 사용하는 회원 email
     *
     * @return email
     */
    @JsonProperty(access = Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * 계정이 만료되었는지 체크하는 로직
     * 이 예제에서는 사용하지 않으므로 true 값 return
     *
     * @return true
     */
    @JsonProperty(access = Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠겼는지 체크하는 로직
     * 이 예제에서는 사용하지 않으므로 true 값 return
     *
     * @return true
     */
    @JsonProperty(access = Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 계정의 패스워드가 만료되었는지 체크하는 로직
     * 이 예제에서는 사용하지 않으므로 true 값 return
     *
     * @return true
     */
    @JsonProperty(access = Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정이 사용가능한지 체크하는 로직
     * 이 예제에서는 사용하지 않으므로 true 값 return
     *
     * @return true
     */
    @JsonProperty(access = Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public void deleteTeam(User user) {

    }

    public void joinTeam(Team team) {

    }
}