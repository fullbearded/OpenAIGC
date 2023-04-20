package com.opaigc.server.application.user.processor.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.opaigc.server.application.user.domain.Member;
import com.opaigc.server.application.user.processor.ChargeProcessor;
import com.opaigc.server.application.user.service.MemberService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Runner.dada
 * @date: 2023/4/4
 * @description:
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class FrequencyUseProcessor implements ChargeProcessor {

	@Autowired
	private MemberService memberService;

	@Override
	@Transactional
	public boolean off(Context context) {
		Optional<Member> memberOptional = memberService.findByUserId(context.getUser().getId());

		if (memberOptional.isPresent()) {
			Member member = memberOptional.get();
			Member updateMember = Member.builder().id(member.getId())
				.dailyLimit(context.getPromotion().getType().getDailyUsageLimit())
				.expireDate(member.getExpireDate().plus(context.getPromotion().getType().getEffectiveDays(), ChronoUnit.DAYS))
				.totalQuota(member.getTotalQuota() + context.getPromotion().getType().getTotalCapacity())
				.updatedBy(context.getUser().getUsername())
				.build();
			memberService.updateById(updateMember);
		} else {
			Member member = Member.builder().userId(context.getUser().getId())
				.totalQuota(context.getPromotion().getType().getTotalCapacity())
				.dailyLimit(context.getPromotion().getType().getDailyUsageLimit())
				.expireDate(LocalDateTime.now().plus(context.getPromotion().getType().getEffectiveDays(), ChronoUnit.DAYS))
				.createdBy(context.getUser().getUsername())
				.build();
			memberService.save(member);
		}

		return true;
	}
}
