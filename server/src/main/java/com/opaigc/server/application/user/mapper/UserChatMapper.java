package com.opaigc.server.application.user.mapper;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opaigc.server.application.user.domain.UserChat;
import com.opaigc.server.application.user.domain.UserPromotion;

/**
 * @author: Runner.dada
 * @date: 2023/4/4
 * @description:
 **/
@Repository
public interface UserChatMapper extends BaseMapper<UserChat> {

}
