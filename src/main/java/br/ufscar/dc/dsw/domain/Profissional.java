package br.ufscar.dc.dsw.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Table(name = "Profissional")
public class Profissional extends Usuario {

    public Profissional() {
        setRole("ROLE_PROFISSIONAL");
    }

    @NotBlank(message = "{NotNull.profissional.CPF}")
    @Size(min = 11, max = 14, message = "{Size.profissional.CPF}")
    @Column(nullable = false, length = 14, unique = true)
    private String CPF;

    @NotBlank(message = "{NotNull.profissional.telefone}")
    @Size(min = 8, max = 20, message = "{Size.profissional.telefone}")
    @Column(nullable = false, length = 20)
    private String telefone;

    @NotBlank(message = "{NotNull.profissional.sexo}")
    @Column(nullable = false, length = 20)
    private String sexo;

    @NotNull(message = "{NotNull.profissional.dataNascimento}")
    @Past(message = "{Past.profissional.dataNascimento}")
    @Column(nullable = false)
    private LocalDate dataNascimento;

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
