package com.QuesTyme.repository;

import com.QuesTyme.config.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.QuesTyme.entity.User;

import java.util.List;


/**

 This interface represents the repository for the User entity.

 It extends the JpaRepository interface which provides CRUD functionality for the entity.

 It also contains custom queries using JPA's @Query annotation.

 @author com.QuesTyme.repository
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	 Returns the user with the given email.
	 @param //email the email of the user to be retrieved.
	 @return the user with the given email.

	User findByEmail(String email);


	 Returns a list of users with the given ids.
	 @param //ids the ids of the users to be retrieved.
	 @return a list of users with the given ids.

	@Query(value = "select * from users u where u.id in(:ids)",nativeQuery = true)
	List<User> findByIds(List<Integer> ids);


	 Returns a list of users with the given type.
	 @param //type the type of the users to be retrieved.
	 @return a list of users with the given type.

	@Query(value = "SELECT u FROM Users u WHERE u.type = (:type)", nativeQuery = true)
	List<User> findByType(Type type);
}*/


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);


	@Query(value = "select * from users u where u.id in(:ids)",nativeQuery = true)
	List<User> findByIds(List<Integer> ids);


//	@Query(value = "SELECT u FROM Users u WHERE u.type = (:type)", nativeQuery = true)
//	List<User> findByType(Type type);
}
