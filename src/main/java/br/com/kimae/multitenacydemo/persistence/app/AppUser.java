package br.com.kimae.multitenacydemo.persistence.app;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "APP_USER")
@Getter
@Setter
public class AppUser implements Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	private String email;

	private String password;

	private String clientDatabase;

}
