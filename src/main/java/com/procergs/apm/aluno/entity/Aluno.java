package com.procergs.apm.aluno.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="APM_ALUNO")
public class Aluno {

    @Id
    @Column(name = "NRO_INT_ALU")
    @SequenceGenerator(name = "Aluno_SEQ", sequenceName = "ID_ALUNO_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Aluno_SEQ")
	private Long nroIntAlu;

    @Column(name="NOME_ALU")
    private String nomeAlu;
    
	@Column(name="CTR_DTH_ATU")
	private Timestamp ctrDthAtu;

	@Column(name="CTR_DTH_INC")
	private Timestamp ctrDthInc;

	@Column(name="CTR_NRO_IP_ATU")
	private String ctrNroIpAtu;

	@Column(name="CTR_NRO_IP_INC")
	private String ctrNroIpInc;

	@Column(name="CTR_USU_ATU")
	private BigDecimal ctrUsuAtu;

	@Column(name="CTR_USU_INC")
	private BigDecimal ctrUsuInc;
	
    @Version
    private Long versao;

	public Aluno() {
	}

	public Long getNroIntAlu() {
		return this.nroIntAlu;
	}

	public void setNroIntAlu(Long nroIntAlu) {
		this.nroIntAlu = nroIntAlu;
	}

	public Timestamp getCtrDthAtu() {
		return this.ctrDthAtu;
	}

	public void setCtrDthAtu(Timestamp ctrDthAtu) {
		this.ctrDthAtu = ctrDthAtu;
	}

	public Timestamp getCtrDthInc() {
		return this.ctrDthInc;
	}

	public void setCtrDthInc(Timestamp ctrDthInc) {
		this.ctrDthInc = ctrDthInc;
	}

	public String getCtrNroIpAtu() {
		return this.ctrNroIpAtu;
	}

	public void setCtrNroIpAtu(String ctrNroIpAtu) {
		this.ctrNroIpAtu = ctrNroIpAtu;
	}

	public String getCtrNroIpInc() {
		return this.ctrNroIpInc;
	}

	public void setCtrNroIpInc(String ctrNroIpInc) {
		this.ctrNroIpInc = ctrNroIpInc;
	}

	public BigDecimal getCtrUsuAtu() {
		return this.ctrUsuAtu;
	}

	public void setCtrUsuAtu(BigDecimal ctrUsuAtu) {
		this.ctrUsuAtu = ctrUsuAtu;
	}

	public BigDecimal getCtrUsuInc() {
		return this.ctrUsuInc;
	}

	public void setCtrUsuInc(BigDecimal ctrUsuInc) {
		this.ctrUsuInc = ctrUsuInc;
	}

	public String getNomeAlu() {
		return this.nomeAlu;
	}

	public void setNomeAlu(String nomeAlu) {
		this.nomeAlu = nomeAlu;
	}

}