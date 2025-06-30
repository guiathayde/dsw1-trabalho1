package br.ufscar.dc.dsw.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import br.ufscar.dc.dsw.validation.UniqueCNPJEmpresa;

@SuppressWarnings("serial")
@Entity
@Table(name = "Empresa")
public class Empresa extends Usuario {

    public Empresa() {
        setRole("ROLE_EMPRESA");
    }

    @NotBlank(message = "{NotBlank.empresa.cnpj}")
    @Column(nullable = false, length = 18, unique = true)
    @UniqueCNPJEmpresa(message = "{Unique.empresa.cnpj}")
    private String cnpj;

    @NotBlank(message = "{NotBlank.empresa.descricao}")
    @Size(max = 256, message = "{Size.empresa.descricao}")
    @Column(nullable = false, length = 256)
    private String descricao;

    @NotBlank(message = "{NotBlank.empresa.cidade}")
    @Size(max = 60, message = "{Size.empresa.cidade}")
    @Column(nullable = false, length = 60)
    private String cidade;

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
}
