package br.com.pitang.challenge.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.pitang.challenge.models.Phone;

@Repository
public interface PhoneRepository  extends CrudRepository<Phone, Long> {

}
