package bg.uniplovdiv.talkify.friendship.model;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

  boolean existsByUserIdAndFriendIdAndActiveIsTrue(Long userId, Long friendId);

  Optional<Long> findChannelIdByUserIdAndFriendIdAndActiveIsTrue(Long userId, Long friendId);

  @Query(
      "SELECT f FROM Friendship f "
          + "WHERE (f.user.id = :userId AND f.friend.id = :friendId) "
          + "   OR (f.user.id = :friendId AND f.friend.id = :userId)")
  List<Friendship> findMutualFriendships(Long userId, Long friendId);
}
