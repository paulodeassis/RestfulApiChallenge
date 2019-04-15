package br.com.pitang.challenge.models;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.pitang.challenge.common.Hashing;

@Entity
@Table(name="USER")
public class User implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Column(columnDefinition="VARCHAR(50)")
	private String firstName;
	@Column(columnDefinition="VARCHAR(50)")
	private String lastName;
	@Column(unique=true)
	private String email;
	private String password;
	private LocalDate created_at;

	private LocalDate last_login;
	
	public String getLast_login() {
		return convertDateToString(this.last_login);		
	}

	public void setLast_login(LocalDate last_login) {
		this.last_login = last_login;
	}

	@OneToMany(cascade=CascadeType.ALL)	
	private List<Phone> phones = new ArrayList<Phone>();
	
	public String getCreated_at() {
		return convertDateToString(created_at);
	}
	
	public void setCreated_at(LocalDate created_at) {
		this.created_at = created_at;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;		
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) throws NoSuchAlgorithmException {
		this.password = Hashing.getInstance().getHash(password);		
	}
	
	public List<Phone> getPhones() {
		return phones;
	}
	
	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}
	
	private String convertDateToString(LocalDate localDate) {
		String date="";
		if(localDate != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			date = formatter.format(localDate);
		}
        return date;
	}
}
