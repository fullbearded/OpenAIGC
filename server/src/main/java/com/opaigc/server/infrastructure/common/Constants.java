package com.opaigc.server.infrastructure.common;

/**
 * @author: Runner.dada
 * @date: 2020/12/13
 * @description: 全局常量
 **/
public class Constants {

	public static final String COPYRIGHT = "liankebang";

	public static final String AUTHORIZATION_HEADER = "Authorization";

	public static final String AUTHORIZATION_BEARER_PREFIX = "Bearer ";

	public static final String USER_CODE_KEY = "code";

	public static final String CHAT_STREAM_COMPLETED_TOPIC = "chat.stream.completed.topic";


	public static final String LOGIN_LOCKED_TIMES = "%s.login.times.%s";

	public static final String LOGIN_TOKEN = "%s.login.token.%s";

	public static final String LOGIN_LOCKED_VALUE = "login.locked";

	public static final String USER_LOGIN_PATH = "/api/auth/login";
	public static final String USER_LOGOUT_PATH = "/api/auth/logout";

	public static final String CHAT_WITH_ANONYMOUS_USER_KEY = "anonymous";

	public static final Long DEFAULT_APP_ID = 1l;

	public static final String MOBILE_REGEX = "^1[3-9]\\d{9}$";

	public static final String[] STATIC_WHITELIST = {
		"/*.html", "/*.css", "/*.js", "/*.png", "/*/*.html", "/*/*.css", "/*/*.js", "/*/*.png",
		"/*/*.woff", "/*/*.ttf", "/*.json",
	};

	public static final String[] SWAGGER_WHITELIST = {
		"/v2/api-docs",
		"/swagger-resources",
		"/swagger-resources/*",
		"/swagger-ui.html",
	};

	public static final String[] URI_WHITELIST = {
		"/private/*",

		/**
		 * Must Ignore when using WebSocket, otherwise the WebSocket connection will be disconnected,
		 * because the WebSocket connection is a long connection, Spring Security will intercept the request and return 401
		 **/
		"/api/chat/stream",
		"/api/auth/registration",
		"/api/chat/stream/anonymous",
		"/api/v2/chat/stream/anonymous",
		"/api/app/create/anonymous",
		"/api/app/check/anonymous",
		"/api/app/list/anonymous",
		"/api/app/page/anonymous",
		"/api/app/like/anonymous"
	};

}
