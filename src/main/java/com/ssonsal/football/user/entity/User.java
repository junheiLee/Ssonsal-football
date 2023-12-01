package com.ssonsal.football.user.entity;

import com.ssonsal.football.global.entity.BaseEntity;
import com.ssonsal.football.team.entity.Team;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@ToString(exclude = {"team"})
public class User extends BaseEntity implements UserDetails {
    //spring security의 userdetails 상속받아서 사용
    //사용자의 인증정보를 담아두는 인터페이스 해당 객체를 통해서 인증정보를 가져오려면 필수 Override메서드들을 사용해야 함
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "role", columnDefinition = "0")
    private int role;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "password", length = 50)
    private String password;

    @Column(name = "name", length = 8)
    private String name;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "gender")
    private int gender;

    @Column(name = "nickname", length = 14)
    private String nickname;

    @Column(name = "position", length = 4)
    private String position;

    @Column(name = "phone", unique = true, length = 20)
    private String phone;

    @Column(name = "preferred_time")
    private String preferredTime;

    @Column(name = "preferred_area")
    private String preferredArea;

    @Column(name = "intro")
    private String intro;

    @Column(name = "skill_score")
    private Float skillScore;

    @Column(name = "manner_score")
    private Float mannerScore;

    @Builder
    public User(String email, String password, String auth) {
        this.email = email;
        this.password = password;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // 권한 반환
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getUsername() {
        return email;
    } //사용자의 id 반환

    @Override
    public String getPassword() {
        return password;
    }//사용자 pw반환

    @Override
    public boolean isAccountNonExpired() {
        //만료되었는지를 확인하는 로직이 추가되어야함
        return true;// 만료안됨

    }// 계정 만료여부 반환

    @Override
    public boolean isAccountNonLocked() {//계정이 잠겨있는지 확인
        //계정이 잠겨있는지 확인하는 로직이 추가되어야함
        return true;//잠금안됨
    }

    @Override
    public boolean isCredentialsNonExpired() {//비번 만료여부반환
        //비번 만료되었는지를 확인하는 로직
        return true;//만료 안됨
    }

    @Override
    public boolean isEnabled() {//계정 사용가능 여부 반환
        //계정 사용가능 여부 확인하는 로직
        return true;//사용가능
    }
}


