package vn.iotstar.DatingApp.Model;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import vn.iotstar.DatingApp.Entity.Account;

public class AccountUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// create account for security
	private Account acc;
	
	public AccountUserDetails(Account acc) {
		this.acc = acc;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		 return Arrays.stream(acc.getRole().split(","))
	                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Thêm tiền tố
	                .collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return acc.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return acc.getEmail();
	}
	
	@Override
	public boolean isAccountNonExpired() {
	    return true;  // Change to false if you have an expiration mechanism
	}

	@Override
	public boolean isAccountNonLocked() {
	    return true;  // Change to false if you implement account locking
	}

	@Override
	public boolean isCredentialsNonExpired() {
	    return true;  // Change if password expiration is implemented
	}

	@Override
	public boolean isEnabled() {
	    return true;  // Change if you implement activation via email, etc.
	}

}
