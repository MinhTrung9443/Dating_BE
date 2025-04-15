package vn.iotstar.DatingApp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Message;
import vn.iotstar.DatingApp.Entity.Users;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByMatchlist(MatchList matchlist);
    
    List<Message> findByFromUserAndToUser(Users fromUser, Users toUser);
    
    List<Message> findByFromUserOrToUserOrderBySentAtDesc(Users fromUser, Users toUser);
    
    List<Message> findByToUserAndIsReadFalse(Users toUser);
    
    int countByToUserAndIsReadFalse(Users toUser);
}
