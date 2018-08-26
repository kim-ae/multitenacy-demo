package br.com.kimae.multitenacydemo.service;

import static java.util.Optional.ofNullable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.kimae.multitenacydemo.persistence.multitanant.InfoUserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional("multitenantTransaction")
public class InfoManagerService {

    @Autowired
    private InfoUserRepository infoUserRepository;

    public String getSensitivyUserInfo(String user) {
        log.info("Retrieving sensitivy information about {}", user);
        return ofNullable(infoUserRepository.findByEmail(user)).map((i)->i.getSensitivyInformation()).orElse("");
    }
}
