package br.com.pitang.challenge.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.pitang.challenge.exceptions.UserNotFoundException;
import br.com.pitang.challenge.models.User;

@Repository
public interface UserRepository  extends CrudRepository<User, Long> {
	 @Query(value = "SELECT * FROM USER U WHERE U.EMAIL= ?1 AND U.PASSWORD = ?2", nativeQuery = true)
	 User findByCredential(String email, String password) throws UserNotFoundException;
	 //@Param("mail") String email, @Param("password") String password
}

