package com.work.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "Company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{company.email.notempty}")
    @Email(message = "{company.email.invalid}")
    @Column(nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "{company.password.notempty}")
    @Size(min = 8, message = "{company.password.size}")
    @Column(nullable = false)
    private String password;

    @NotEmpty(message = "{company.cnpj.notempty}")
    // Regex for XX.XXX.XXX/XXXX-XX or XXXXXXXXXXXXXX
    @Pattern(regexp = "^(\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}|\\d{14})$", message = "{company.cnpj.invalid}")
    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    @NotEmpty(message = "{company.name.notempty}")
    @Column(nullable = false)
    private String name;

    @NotEmpty(message = "{company.description.notempty}")
    @Lob
    private String description;

    @NotEmpty(message = "{company.city.notempty}")
    private String city;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Vacancy> vacancies = new HashSet<>();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public Set<Vacancy> getVacancies() { return vacancies; }
    public void setVacancies(Set<Vacancy> vacancies) { this.vacancies = vacancies; }
}
