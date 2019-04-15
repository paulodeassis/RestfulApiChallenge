package br.com.pitang.challenge.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="PHONE")
public class Phone{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@PrimaryKeyJoinColumn
	@Column(name="NUMBER", columnDefinition="NUMBER(9)")
	private Integer number;
	
	@Column(name="AREA_CODE",columnDefinition="NUMBER(3)" )
	private Integer area_code;
	
	@Column(name="COUNTRY_CODE", columnDefinition="VARCHAR(3)")
	private String country_code;
	
	public Integer getArea_code() {
		return area_code;
	}

	public void setArea_code(Integer area_code) {
		this.area_code = area_code;
	}

	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public Integer getNumber() {
		return number;
	}
	
	public void setNumber(Integer number) {
		this.number = number;
	}
}
