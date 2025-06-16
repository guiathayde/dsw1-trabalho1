package com.work.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "Vacancy")
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{vacancy.company.notnull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @NotEmpty(message = "{vacancy.description.notempty}")
    @Lob
    @Column(nullable = false, columnDefinition="TEXT")
    private String description;

    @DecimalMin(value = "0.01", message = "{vacancy.remuneration.min}") // Changed from 0.0 to 0.01, inclusive false removed
    @Digits(integer=10, fraction=2, message = "{vacancy.remuneration.digits}")
    private BigDecimal remuneration;

    @NotNull(message = "{vacancy.applicationdeadline.notnull}")
    @Future(message = "{vacancy.applicationdeadline.future}")
    @Column(nullable = false)
    private LocalDate applicationDeadline;

    @NotEmpty(message = "{vacancy.city.notempty}")
    @Column(nullable = false)
    private String city;

    @OneToMany(mappedBy = "vacancy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Application> applications = new HashSet<>();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getRemuneration() { return remuneration; }
    public void setRemuneration(BigDecimal remuneration) { this.remuneration = remuneration; }
    public LocalDate getApplicationDeadline() { return applicationDeadline; }
    public void setApplicationDeadline(LocalDate applicationDeadline) { this.applicationDeadline = applicationDeadline; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public Set<Application> getApplications() { return applications; }
    public void setApplications(Set<Application> applications) { this.applications = applications; }
}
