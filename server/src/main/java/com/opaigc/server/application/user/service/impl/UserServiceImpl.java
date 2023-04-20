package com.opaigc.server.application.user.service.impl;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opaigc.server.application.sso.service.impl.UserDetailsServiceImpl;
import com.opaigc.server.application.user.domain.Member;
import com.opaigc.server.application.user.domain.User;
import com.opaigc.server.application.user.mapper.UserMapper;
import com.opaigc.server.application.user.service.MemberService;
import com.opaigc.server.application.user.service.UserService;
import com.opaigc.server.infrastructure.exception.AppException;
import com.opaigc.server.infrastructure.http.CommonResponseCode;
import com.opaigc.server.infrastructure.utils.CodeUtil;

import java.time.LocalTime;
import java.util.Optional;

/**
 * @author: Runner.dada
 * @date: 2020/12/6
 * @description: the system user domain service
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	@Autowired
	private MemberService memberService;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@Override
	public User create(UserRegistrationParam req) {
		User user = User.builder().mobile(req.getMobile())
			.username(req.getUsername())
			.password(passwordEncoder.encode(req.getPassword()))
			.code(CodeUtil.generateRandomUserCode())
			.createdBy(req.getUsername())
			.updatedBy(req.getUsername())
			.registerIp(req.getRegisterIp())
			.build();
		user.setUsername(req.getUsername());
		if (save(user)) {
			memberService.findOrCreateByUserId(user.getId());
		}
		return user;
	}

	@Override
	public User findById(Long id) {
		return lambdaQuery().eq(User::getId, id).one();
	}

	@Override
	public Optional<User> getByUsername(String username) {
		return lambdaQuery().eq(User::getUsername, username).eq(User::getStatus, User.UserStatusEnum.ENABLE).oneOpt();
	}

	@Override
	public Boolean existsByUsernameOrMobile(String username, String mobile) {
		return lambdaQuery().eq(User::getUsername, username).or().eq(User::getMobile, mobile).count() > 0;
	}

	@Override
	public User getByCodeOrElseThrow(String code) {
		return getByCode(code).orElseThrow(() -> new AppException(CommonResponseCode.USER_NOT_FOUND));
	}

	@Override
	public Optional<User> getByCode(String code) {
		return lambdaQuery().eq(User::getCode, code).eq(User::getStatus, User.UserStatusEnum.ENABLE).last("LIMIT 1").oneOpt();
	}

	@Override
	public Boolean delete(Long id) {
		return lambdaUpdate().eq(User::getId, id).set(User::getDeletedAt, LocalTime.now())
			.update();
	}

	@Override
	public UserMemberDTO getUserInfo(String code) {
		User user = getByCode(code).get();
		Member member = memberService.findOrCreateByUserId(user.getId());

		UserMemberDTO userMemberDTO = new UserMemberDTO();
		BeanUtils.copyProperties(user, userMemberDTO);

		userMemberDTO.setExpireDate(member.getExpireDate());
		userMemberDTO.setDailyLimit(member.getDailyLimit());
		userMemberDTO.setUsedQuota(member.getUsedQuota());
		userMemberDTO.setTotalQuota(member.getTotalQuota());
		return userMemberDTO;
	}
}
