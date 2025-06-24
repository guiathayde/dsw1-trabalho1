package br.ufscar.dc.dsw.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Table(name = "vacancy")
public class Vacancy extends AbstractEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @NotBlank()
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String title;

    @NotBlank()
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String description;

    @PositiveOrZero()
    @Column(nullable = true, precision = 10, scale = 2)
    private BigDecimal remuneration;

    @NotNull()
    @FutureOrPresent()
    @Column(name = "registration_deadline", nullable = false)
    private Date registrationDeadline;

    @NotBlank()
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String city;

    @NotBlank()
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String state;

    @NotBlank()
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String country;

    @Column(name = "created_at", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "vacancy")
    private List<Application> applications;

    public Vacancy() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getRemuneration() {
        return remuneration;
    }

    public void setRemuneration(BigDecimal remuneration) {
        this.remuneration = remuneration;
    }

    public Date getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(Date registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
}