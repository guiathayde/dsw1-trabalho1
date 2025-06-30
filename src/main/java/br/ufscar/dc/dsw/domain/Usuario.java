package br.ufscar.dc.dsw.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Table(name = "Usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario extends AbstractEntity<Long> {

	@NotBlank(message = "{NotBlank.usuario.username}")
	@Column(nullable = false, length = 224, unique = true)
	@Email(message = "{Email.usuario.username}")
	private String username;

	@NotBlank(message = "{NotBlank.usuario.password}")
	@Size(min = 6, max = 64, message = "{Size.usuario.password}")
	@Column(nullable = false, length = 64)
	private String password;

	@NotBlank(message = "{NotBlank.usuario.name}")
	@Size(min = 3, max = 60, message = "{Size.usuario.name}")
	@Column(nullable = false, length = 60)
	private String name;

	@NotBlank(message = "{NotBlank.usuario.role}")
	@Column(nullable = false, length = 20)
	private String role;

	@Column(nullable = false)
	private boolean enabled;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}