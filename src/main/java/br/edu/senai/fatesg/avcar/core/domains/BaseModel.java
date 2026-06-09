package br.edu.senai.fatesg.avcar.core.domains;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@MappedSuperclass
public abstract class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_hora_criacao")
    private Date dataHoraCriacao;

    @Column(name = "ativo")
    private boolean ativo;

    @PrePersist
    protected void onCreate() {
        if (this.dataHoraCriacao == null) {
            this.dataHoraCriacao = new Date();
        }
        this.ativo = true;
    }
}
