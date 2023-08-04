package br.com.kimae.multitenacydemo.service;

import static java.util.Optional.ofNullable;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.kimae.multitenacydemo.persistence.multitanant.InfoUser;
import br.com.kimae.multitenacydemo.persistence.multitanant.InfoUserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional("multitenantTransaction")
public class InfoManagerService {

	private final InfoUserRepository repository;

	public InfoManagerService(InfoUserRepository repository) {
		this.repository = repository;
	}

	public Optional<String> getSensitivyUserInfo(String user) {
		log.info("Retrieving sensitivy information about {}", user);
		return ofNullable(repository.findByEmail(user)).map(InfoUser::getSensitivyInformation);
	}
}
