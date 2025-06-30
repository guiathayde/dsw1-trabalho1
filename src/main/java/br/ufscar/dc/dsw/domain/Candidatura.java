package br.ufscar.dc.dsw.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Candidatura")
public class Candidatura extends AbstractEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "profissional_id", nullable = false)
    private Profissional profissional;

    @ManyToOne
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String curriculoPath;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String status; // ABERTO, NAO_SELECIONADO, ENTREVISTA

    public Candidatura() {
    }

    public Candidatura(Profissional profissional, Vaga vaga, String curriculoPath, String status) {
        this.profissional = profissional;
        this.vaga = vaga;
        this.curriculoPath = curriculoPath;
        this.status = status;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
    }

    public String getCurriculoPath() {
        return curriculoPath;
    }

    public void setCurriculoPath(String curriculoPath) {
        this.curriculoPath = curriculoPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
