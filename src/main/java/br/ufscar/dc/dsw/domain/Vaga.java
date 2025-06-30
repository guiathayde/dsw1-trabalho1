package br.ufscar.dc.dsw.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@SuppressWarnings("serial")
@Entity
@Table(name = "Vaga")
public class Vaga extends AbstractEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @NotBlank(message = "{NotBlank.vaga.descricao}")
    @Column(nullable = false, length = 1000)
    private String descricao;

    @NotNull(message = "{NotNull.vaga.remuneracao}")
    @PositiveOrZero(message = "{PositiveOrZero.vaga.remuneracao}")
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal remuneracao;

    @NotNull(message = "{NotNull.vaga.dataLimiteInscricao}")
    @FutureOrPresent(message = "{FutureOrPresent.vaga.dataLimiteInscricao}")
    @Column(nullable = false)
    private LocalDate dataLimiteInscricao;

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getRemuneracao() {
        return remuneracao;
    }

    public void setRemuneracao(BigDecimal remuneracao) {
        this.remuneracao = remuneracao;
    }

    public LocalDate getDataLimiteInscricao() {
        return dataLimiteInscricao;
    }

    public void setDataLimiteInscricao(LocalDate dataLimiteInscricao) {
        this.dataLimiteInscricao = dataLimiteInscricao;
    }
}
