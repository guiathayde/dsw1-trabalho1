package br.ufscar.dc.dsw.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Table(name = "company")
@PrimaryKeyJoinColumn(name = "user_id")
public class Company extends User {

    @NotBlank()
    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2}")
    @Column(nullable = false, length = 18, unique = true)
    private String cnpj;

    @NotBlank()
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String name;

    @NotBlank()
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotBlank()
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String city;

     @Size(max = 255)
     @Column(length = 255)
     private String state;

     @Size(max = 255)
     @Column(length = 255)
     private String country;

    @OneToMany(mappedBy = "company")
    private List<Vacancy> vacancies;

    public Company() {
        setRole(Role.COMPANY);
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Vacancy> getVacancies() {
        return vacancies;
    }

    public void setVacancies(List<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}