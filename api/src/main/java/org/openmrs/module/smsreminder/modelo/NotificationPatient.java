package org.openmrs.module.smsreminder.modelo;

import org.openmrs.BaseOpenmrsData;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by nelson.mahumane on 12-06-2015.
 */
public class NotificationPatient extends BaseOpenmrsData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nid;
    private String telemovel;
    private String nome;
    private String sexo;
    private Integer diasRemanescente;
    private Integer identificador;
    private Integer tipoVisita;
    private Date ultimaVisita;
    private Date proximaVisita;
    private Date inicioTarv;

    public NotificationPatient(){}

    public Integer getId() {
        return getIdentificador();
    }
    public void setId(Integer id) {
        setIdentificador(id);
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getTelemovel() {
        return telemovel;
    }

    public void setTelemovel(String telemovel) {
        this.telemovel = telemovel;
    }

    public Integer getDiasRemanescente() {
        return diasRemanescente;
    }

    public void setDiasRemanescente(Integer diasRemanescente) {
        this.diasRemanescente = diasRemanescente;
    }

    public Integer getIdentificador() {
        return identificador;
    }
    public void setIdentificador(Integer identificador) {
        this.identificador = identificador;
    }

    public Integer getTipoVisita() {
        return tipoVisita;
    }

    public void setTipoVisita(Integer tipoVisita) {
        this.tipoVisita = tipoVisita;
    }

    public Date getUltimaVisita() {
        return ultimaVisita;
    }

    public void setUltimaVisita(Date ultimaVisita) {
        this.ultimaVisita = ultimaVisita;
    }

    public Date getProximaVisita() {
        return proximaVisita;
    }

    public void setProximaVisita(Date proximaVisita) {
        this.proximaVisita = proximaVisita;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getInicioTarv() {
        return inicioTarv;
    }

    public void setInicioTarv(Date inicioTarv) {
        this.inicioTarv = inicioTarv;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
