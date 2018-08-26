package br.com.kimae.multitenacydemo.config.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class AppPrincipal extends User {

    private final String clientDatabase;

    public AppPrincipal(final String username, final String password,
        final Collection<? extends GrantedAuthority> authorities, final String clientDatabase) {
        super(username, password, authorities);
        this.clientDatabase = clientDatabase;
    }
}
