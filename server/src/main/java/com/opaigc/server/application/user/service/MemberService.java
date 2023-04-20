package com.opaigc.server.application.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.opaigc.server.application.user.domain.Member;

import java.util.Optional;

/**
 * @author: Runner.dada
 * @date: 2020/12/21
 * @description:
 **/
public interface MemberService extends IService<Member> {
	Optional<Member> findByUserId(Long userId);

	Member findOrCreateByUserId(Long userId);

	Boolean usedQuotaIncrement(Member member, Integer amount);

}
