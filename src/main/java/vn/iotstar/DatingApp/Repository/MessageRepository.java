package vn.iotstar.DatingApp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.DatingApp.Entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{

}
