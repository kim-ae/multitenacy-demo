package br.com.kimae.multitenacydemo.persistence.multitanant;

import org.springframework.data.repository.Repository;

public interface InfoUserRepository extends Repository<InfoUser, Long> {

    InfoUser findByEmail(final String email);
}
