package vn.iotstar.DatingApp.Mapper;

import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.Response.Like;

public class LikeMapper {
	
	public static Like mapFromMatchList(MatchList matchList) {
	    Users user = matchList.getUser2(); // assuming user1 likes user2

	    String picture = ""; // default picture ID or placeholder
	    if (user.getImages() != null && !user.getImages().isEmpty()) {
	        picture = user.getImages().get(0).getImage(); // hoặc getImageId(), getUrl(), tùy kiểu dữ liệu
	    }

	    return new Like(
	        user.getId(),
	        user.getName(),
	        picture
	    );
	}
	
}

