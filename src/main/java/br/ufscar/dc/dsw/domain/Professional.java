package br.ufscar.dc.dsw.domain;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Table(name = "professional")
@PrimaryKeyJoinColumn(name = "user_id")
public class Professional extends User {

    @NotBlank()
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String name;

    @NotBlank()
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}")
    @Column(nullable = false, length = 14, unique = true)
    private String cpf;

    @NotBlank()
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String phone;

    @NotNull()
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Gender gender;

    @NotNull()
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate birthDate;

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

    @OneToMany(mappedBy = "professional")
    private List<Application> applications;

    public Professional() {
        setRole(Role.PROFESSIONAL);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
}