package br.com.kimae.multitenacydemo.persistence.app;

import org.springframework.data.repository.Repository;

public interface AppUserRepository extends Repository<AppUser, Long> {
    AppUser findByEmail(final String email);
    void save(final AppUser appUser);

}
