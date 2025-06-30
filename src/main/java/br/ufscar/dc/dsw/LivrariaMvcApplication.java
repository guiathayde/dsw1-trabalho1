package br.ufscar.dc.dsw;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.ufscar.dc.dsw.dao.IEditoraDAO;
import br.ufscar.dc.dsw.dao.ILivroDAO;
import br.ufscar.dc.dsw.dao.IUsuarioDAO;
import br.ufscar.dc.dsw.domain.Usuario;

@SpringBootApplication
public class LivrariaMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(LivrariaMvcApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(IUsuarioDAO usuarioDAO, BCryptPasswordEncoder encoder, IEditoraDAO editoraDAO,
			ILivroDAO livroDAO) {
		return (args) -> {
			Usuario admin = usuarioDAO.getUserByUsername("admin@work.com");
			if (admin == null) {
				Usuario u1 = new Usuario();
				u1.setUsername("admin@work.com");
				u1.setPassword(encoder.encode("admin@work.com"));
				u1.setName("Administrador");
				u1.setRole("ROLE_ADMIN");
				u1.setEnabled(true);
				usuarioDAO.save(u1);
			}
		};
	}
}
