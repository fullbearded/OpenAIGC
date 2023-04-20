package com.opaigc.server.infrastructure.auth;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Runner.dada
 * @date: 2020/12/27
 * @description:
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AccountSession {

	private Long id;

	private String token;

	private String code;

	private String username;

	private String email;

	private String mobile;

	private String avatar;

	private String sex;

	private String userType;

	private String status;

	private String remark;

	private LocalDateTime createdAt;

}
