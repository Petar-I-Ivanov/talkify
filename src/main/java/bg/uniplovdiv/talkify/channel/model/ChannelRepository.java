package bg.uniplovdiv.talkify.channel.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository
    extends JpaRepository<Channel, Long>, QuerydslPredicateExecutor<Channel> {}
