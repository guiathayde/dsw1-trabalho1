package com.work.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;


@Entity
@Table(name = "Professional")
public class Professional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{professional.email.notempty}")
    @Email(message = "{professional.email.invalid}")
    @Column(nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "{professional.password.notempty}")
    @Size(min = 8, message = "{professional.password.size}")
    @Column(nullable = false)
    private String password;

    @NotEmpty(message = "{professional.cpf.notempty}")
    // Regex for XXX.XXX.XXX-XX or XXXXXXXXXXX
    @Pattern(regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})$", message = "{professional.cpf.invalid}")
    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @NotEmpty(message = "{professional.name.notempty}")
    @Column(nullable = false)
    private String name;

    @Size(max = 20, message = "{professional.phone.size}")
    private String phone;

    private String gender;

    @Past(message = "{professional.birthdate.past}")
    private LocalDate birthDate;

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Application> applications = new HashSet<>();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public Set<Application> getApplications() { return applications; }
    public void setApplications(Set<Application> applications) { this.applications = applications; }
}
