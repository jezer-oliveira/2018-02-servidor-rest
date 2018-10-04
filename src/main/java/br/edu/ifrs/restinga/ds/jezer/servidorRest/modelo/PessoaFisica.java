package br.edu.ifrs.restinga.ds.jezer.servidorRest.modelo;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class PessoaFisica extends Pessoa {
    
    // Para não gravar no banco 
    @Transient
    // Define o campo
    @JsonProperty("tipo") 
    private final String tipo = "fisica";
    
    private String sexo;
    private String cpf;
    

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    
}