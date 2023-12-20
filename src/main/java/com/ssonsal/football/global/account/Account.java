package com.ssonsal.football.global.account;

import com.ssonsal.football.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class Account extends User {

    private Account account;

    public Account(User user) {
        super(user.getId(), user.getTeam(), user.getEmail(), user.getPassword(), "user");
    }


    private static Collection<? extends GrantedAuthority> authorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }
}
