package com.ifood.quarkus.crud.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class Restaurante extends PanacheEntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	public String proprietario;

	@CreationTimestamp
	public Date dataCriacao;

	@UpdateTimestamp
	public Date dataAtualizacao;

	public String cnpj;

	public String nome;
	
	@ManyToOne
	public Localizacao localizacao;
}
