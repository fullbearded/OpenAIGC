package com.opaigc.server.application.user.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opaigc.server.application.user.domain.UserChat;
import com.opaigc.server.application.user.mapper.UserChatMapper;
import com.opaigc.server.application.user.service.UserChatService;

/**
 * 描述
 *
 * @author huhongda@fiture.com
 * @date 2023/4/9
 */
@Service
public class UserChatServiceImpl extends ServiceImpl<UserChatMapper, UserChat> implements UserChatService {

}
