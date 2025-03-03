package vn.iotstar.DatingApp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.DatingApp.Entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{

}
