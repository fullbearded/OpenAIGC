package com.opaigc.server.infrastructure.utils;

import com.ibm.icu.text.BreakIterator;
import com.opaigc.server.application.openai.service.OpenAiService;

import java.util.List;
import java.util.Locale;

/**
 * 描述
 *
 * @author huhongda@fiture.com
 * @date 2023/4/9
 */

public class TokenCounter {
	private final BreakIterator breakIterator;

	public TokenCounter() {
		this.breakIterator = BreakIterator.getWordInstance(Locale.ROOT);
	}

	public int countTokens(String text) {
		breakIterator.setText(text);
		int count = 0;
		int start = breakIterator.first();
		for (int end = breakIterator.next(); end != BreakIterator.DONE; start = end, end = breakIterator.next()) {
			String token = text.substring(start, end).trim();
			if (!token.isEmpty()) {
				count++;
			}
		}
		return count;
	}

	public int countMessages(List<OpenAiService.Message> messages) {
		int count = 0;
		for (OpenAiService.Message message : messages) {
			count += countTokens(message.getContent());
			count += countTokens(message.getRole());
		}
		return count + 2;
	}
}
