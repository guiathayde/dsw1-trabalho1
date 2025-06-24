package br.ufscar.dc.dsw;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.ufscar.dc.dsw.dao.IAdministradorDAO;
import br.ufscar.dc.dsw.dao.IEmpresaDAO;
import br.ufscar.dc.dsw.dao.IProfissionalDAO;
import br.ufscar.dc.dsw.domain.Admin;
import br.ufscar.dc.dsw.domain.Company;
import br.ufscar.dc.dsw.domain.Gender;
import br.ufscar.dc.dsw.domain.Professional;
import br.ufscar.dc.dsw.domain.Role;

@SpringBootApplication
public class WorkApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(IAdministradorDAO administradorDAO, IEmpresaDAO empresaDAO,
			IProfissionalDAO profissionalDAO, BCryptPasswordEncoder encoder) {
		return (args) -> {
			if (administradorDAO.findByEmail("admin@work.com") == null) {
				Admin adm = new Admin();
				adm.setEmail("admin@work.com");
				adm.setPassword(encoder.encode("admin@work.com"));
				adm.setName("Admin User");
				adm.setRole(Role.ADMIN);
				administradorDAO.save(adm);
			}

			if (empresaDAO.findByCnpj("00.000.000/0001-01") == null) {
				Company emp1 = new Company();
				emp1.setEmail("company1@example.com");
				emp1.setPassword(encoder.encode("company1_pass_xyz"));
				emp1.setCnpj("00.000.000/0001-01");
				emp1.setName("Tech Solutions Ltd.");
				emp1.setDescription("Leading company in software and technology solutions.");
				emp1.setCity("São Paulo");
				emp1.setState("SP");
				emp1.setCountry("Brazil");
				empresaDAO.save(emp1);
			}

			if (empresaDAO.findByCnpj("11.111.111/0001-11") == null) {
				Company emp2 = new Company();
				emp2.setEmail("company2@example.com");
				emp2.setPassword(encoder.encode("company2_pass_abc"));
				emp2.setCnpj("11.111.111/0001-11");
				emp2.setName("Global Innovations Inc.");
				emp2.setDescription("Consulting specialized in marketing strategies and innovation.");
				emp2.setCity("Rio de Janeiro");
				emp2.setState("RJ");
				emp2.setCountry("Brazil");
				empresaDAO.save(emp2);
			}

			if (profissionalDAO.findByCpf("111.222.333-44") == null) {
				Professional prof1 = new Professional();
				prof1.setEmail("professional1@example.com");
				prof1.setPassword(encoder.encode("prof1_pass_789"));
				prof1.setName("Anna Paula Smith");
				prof1.setCpf("111.222.333-44");
				prof1.setPhone("(11) 98765-4321");
				prof1.setGender(Gender.Female);
				prof1.setBirthDate(LocalDate.of(1995, 3, 10));
				prof1.setCity("Belo Horizonte");
				prof1.setState("MG");
				prof1.setCountry("Brazil");
				profissionalDAO.save(prof1);
			}

			if (profissionalDAO.findByCpf("555.666.777-88") == null) {
				Professional prof2 = new Professional();
				prof2.setEmail("professional2@example.com");
				prof2.setPassword(encoder.encode("prof2_pass_456"));
				prof2.setName("Charles Edward Santos");
				prof2.setCpf("555.666.777-88");
				prof2.setPhone("(21) 99887-6655");
				prof2.setGender(Gender.Male);
				prof2.setBirthDate(LocalDate.of(1992, 11, 25));
				prof2.setCity("Curitiba");
				prof2.setState("PR");
				prof2.setCountry("Brazil");
				profissionalDAO.save(prof2);
			}
		};
	}
}
