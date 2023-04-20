package com.opaigc.server.infrastructure.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Runner.dada
 * @date: 2020/12/15
 * @description:
 **/
@Slf4j
public class HttpServletRequestUtil {

	public static String getRequestBody(HttpServletRequest request) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			bufferedReader = request.getReader();
			char[] charBuffer = new char[128];
			int bytesRead;
			while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
				sb.append(charBuffer, 0, bytesRead);
			}
		} catch (IOException ex) {
			log.warn("Read buffer caught throwable", ex);
			throw new IOException();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					log.warn("Read buffer caught throwable", ex);
					throw new IOException();
				}
			}
		}

		return sb.toString();
	}
}
