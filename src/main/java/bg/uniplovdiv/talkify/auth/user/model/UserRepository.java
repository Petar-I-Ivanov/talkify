package bg.uniplovdiv.talkify.auth.user.model;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {

  @Query("SELECT u FROM User u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
  Optional<User> findByUsernameOrEmail(String usernameOrEmail);

  boolean existsByUsernameAndIdNot(String username, Long id);

  boolean existsByUsername(String username);

  boolean existsByEmailAndIdNot(String email, Long id);

  boolean existsByEmail(String email);
}
