package br.ufscar.dc.dsw;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.ufscar.dc.dsw.dao.IEmpresaDAO;
import br.ufscar.dc.dsw.dao.IProfissionalDAO;
import br.ufscar.dc.dsw.dao.IUsuarioDAO;
import br.ufscar.dc.dsw.dao.IVagaDAO;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.domain.Usuario;
import br.ufscar.dc.dsw.domain.Vaga;

@SpringBootApplication
public class LivrariaMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(LivrariaMvcApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(IUsuarioDAO usuarioDAO, BCryptPasswordEncoder encoder,
			IProfissionalDAO profissionalDAO, IEmpresaDAO empresaDAO, IVagaDAO vagaDAO) {
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

			Profissional prof1 = profissionalDAO.findByUsername("prof1@work.com");
			if (prof1 == null) {
				Profissional p1 = new Profissional();
				p1.setUsername("prof1@work.com");
				p1.setPassword(encoder.encode("prof1@work.com"));
				p1.setName("Profissional 1");
				p1.setEnabled(true);
				p1.setCPF("111.111.111-11");
				p1.setTelefone("(11) 1111-1111");
				p1.setDataNascimento(LocalDate.of(1991, 1, 1));
				p1.setSexo("M");
				profissionalDAO.save(p1);
			}

			Profissional prof2 = profissionalDAO.findByUsername("prof2@work.com");
			if (prof2 == null) {
				Profissional p2 = new Profissional();
				p2.setUsername("prof2@work.com");
				p2.setPassword(encoder.encode("prof2@work.com"));
				p2.setName("Profissional 2");
				p2.setEnabled(true);
				p2.setCPF("222.222.222-22");
				p2.setTelefone("(22) 2222-2222");
				p2.setDataNascimento(LocalDate.of(1992, 2, 2));
				p2.setSexo("M");
				profissionalDAO.save(p2);
			}

			Profissional prof3 = profissionalDAO.findByUsername("prof3@work.com");
			if (prof3 == null) {
				Profissional p3 = new Profissional();
				p3.setUsername("prof3@work.com");
				p3.setPassword(encoder.encode("prof3@work.com"));
				p3.setName("Profissional 3");
				p3.setEnabled(true);
				p3.setCPF("333.333.333-33");
				p3.setTelefone("(33) 3333-3333");
				p3.setDataNascimento(LocalDate.of(1993, 3, 3));
				p3.setSexo("F");
				profissionalDAO.save(p3);
			}

			Empresa emp1 = empresaDAO.findByUsername("emp1@work.com");
			if (emp1 == null) {
				emp1 = new Empresa();
				emp1.setUsername("emp1@work.com");
				emp1.setPassword(encoder.encode("emp1@work.com"));
				emp1.setName("Empresa 1");
				emp1.setCnpj("11.111.111/1111-11");
				emp1.setDescricao("Empresa 1");
				emp1.setCidade("São Carlos");
				empresaDAO.save(emp1);
			}

			Empresa emp2 = empresaDAO.findByUsername("emp2@work.com");
			if (emp2 == null) {
				emp2 = new Empresa();
				emp2.setUsername("emp2@work.com");
				emp2.setPassword(encoder.encode("emp2@work.com"));
				emp2.setName("Empresa 2");
				emp2.setCnpj("22.222.222/2222-22");
				emp2.setDescricao("Empresa 2");
				emp2.setCidade("São Paulo");
				empresaDAO.save(emp2);
			}

			Empresa emp3 = empresaDAO.findByUsername("emp3@work.com");
			if (emp3 == null) {
				emp3 = new Empresa();
				emp3.setUsername("emp3@work.com");
				emp3.setPassword(encoder.encode("emp3@work.com"));
				emp3.setName("Empresa 3");
				emp3.setCnpj("33.333.333/3333-33");
				emp3.setDescricao("Empresa 3");
				emp3.setCidade("Campinas");
				empresaDAO.save(emp3);
			}

			Vaga vaga1 = vagaDAO.findByDescricao("Vaga 1");
			if (vaga1 == null) {
				Vaga v1 = new Vaga();
				v1.setEmpresa(emp1);
				v1.setDescricao("Vaga 1");
				v1.setRemuneracao(BigDecimal.valueOf(1000.0));
				v1.setDataLimiteInscricao(LocalDate.now().plusDays(7));
				vagaDAO.save(v1);
			}

			Vaga vaga2 = vagaDAO.findByDescricao("Vaga 2");
			if (vaga2 == null) {
				Vaga v2 = new Vaga();
				v2.setEmpresa(emp2);
				v2.setDescricao("Vaga 2");
				v2.setRemuneracao(BigDecimal.valueOf(2000.0));
				v2.setDataLimiteInscricao(LocalDate.now().plusDays(7));
				vagaDAO.save(v2);
			}

			Vaga vaga3 = vagaDAO.findByDescricao("Vaga 3");
			if (vaga3 == null) {
				Vaga v3 = new Vaga();
				v3.setEmpresa(emp3);
				v3.setDescricao("Vaga 3");
				v3.setRemuneracao(BigDecimal.valueOf(3000.0));
				v3.setDataLimiteInscricao(LocalDate.now().plusDays(7));
				vagaDAO.save(v3);
			}
		};
	}
}
