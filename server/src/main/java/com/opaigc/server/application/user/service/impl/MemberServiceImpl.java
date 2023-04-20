package com.opaigc.server.application.user.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opaigc.server.application.user.domain.Member;
import com.opaigc.server.application.user.mapper.MemberMapper;
import com.opaigc.server.application.user.service.MemberService;
import com.opaigc.server.config.AppConfig;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author: Runner.dada
 * @date: 2020/12/6
 * @description: the system user domain service
 **/
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

	@Autowired
	private AppConfig appConfig;

	@Override
	public Optional<Member> findByUserId(Long userId) {
		return lambdaQuery().eq(Member::getUserId, userId).last("LIMIT 1").oneOpt();
	}

	@Override
	public Member findOrCreateByUserId(Long userId) {
		Optional<Member> memberOptional = findByUserId(userId);
		if (memberOptional.isPresent()) {
			return memberOptional.get();
		} else {
			Member member = Member.builder().userId(userId)
				.expireDate(LocalDateTime.now()).dailyLimit(appConfig.getDailyLimit())
				.totalQuota(0).usedQuota(0).build();
			save(member);
			return member;
		}
	}

	@Override
	public Boolean usedQuotaIncrement(Member member, Integer amount) {
		if (member.isFreeUser()) {
			return lambdaUpdate().eq(Member::getId, member.getId()).setSql("free_used_quota = free_used_quota + " + amount).update();
		} else {
			return lambdaUpdate().eq(Member::getId, member.getId()).setSql("used_quota = used_quota + " + amount).update();
		}
	}
}
