package com.opaigc.server.application.sso.domain.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opaigc.server.application.user.domain.User;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author: Runner.dada
 * @date: 2020/12/6
 * @description: Spring Security Login User Implement
 **/
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDto implements UserDetails {

	private Long id;

	private String code;

	private String username;

	private String email;

	private String mobile;

	private String avatar;

	private User.UserStatusEnum status;

	private User user;

	private String token;

	/**
	 * The permissions
	 */
	private Set<String> permissions;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	@JsonIgnore
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return User.UserStatusEnum.ENABLE.equals(getStatus());
	}

	public boolean isBanned() {
		return User.UserStatusEnum.BANNED.equals(getStatus());
	}

}
