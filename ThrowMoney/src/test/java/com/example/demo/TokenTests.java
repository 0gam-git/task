package com.example.demo;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TokenTests {

	private final int COUNT = 3;

	@Test
	public void randomDuplicationTest() {
		Set<String> set = new HashSet<>();

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			String token = RandomStringUtils.random(COUNT);

			if (!set.add(token)) {
				System.out.println("RandomStringUtils.random : " + set.size());
				break;
			}
		}
	}

	@Test
	public void randomAlphabeticDuplicationTest() {
		Set<String> set = new HashSet<>();

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			String token = RandomStringUtils.randomAlphabetic(COUNT);

			if (!set.add(token)) {
				System.out.println("RandomStringUtils.randomAlphabetic : " + set.size());
				break;
			}
		}
	}

	@Test
	public void randomAlphanumericDuplicationTest() {
		Set<String> set = new HashSet<>();

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			String token = RandomStringUtils.randomAlphanumeric(COUNT);

			if (!set.add(token)) {
				System.out.println("RandomStringUtils.randomAlphanumeric : " + set.size());
				break;
			}
		}
	}

	@Test
	public void randomAsciiDuplicationTest() {
		Set<String> set = new HashSet<>();

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			String token = RandomStringUtils.randomAscii(COUNT);

			if (!set.add(token)) {
				System.out.println("RandomStringUtils.randomAscii : " + set.size());
				break;
			}
		}
	}

	@Test
	public void randomGraphDuplicationTest() {
		Set<String> set = new HashSet<>();

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			String token = RandomStringUtils.randomGraph(COUNT);

			if (!set.add(token)) {
				System.out.println("RandomStringUtils.randomGraph : " + set.size());
				break;
			}
		}
	}

	@Test
	public void randomNumericDuplicationTest() {
		Set<String> set = new HashSet<>();

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			String token = RandomStringUtils.randomNumeric(COUNT);

			if (!set.add(token)) {
				System.out.println("RandomStringUtils.randomNumeric : " + set.size());
				break;
			}
		}
	}

	@Test
	public void randomPrintDuplicationTest() {
		Set<String> set = new HashSet<>();

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			String token = RandomStringUtils.randomPrint(COUNT);

			if (!set.add(token)) {
				System.out.println("RandomStringUtils.randomPrint : " + set.size());
				break;
			}
		}
	}

	@Test
	public void randomUnboundedString() {
		Set<String> set = new HashSet<>();

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			byte[] array = new byte[3];
			new Random().nextBytes(array);
			String generatedString = new String(array, Charset.forName("UTF-8"));

			if (!set.add(generatedString)) {
				System.out.println("randomUnboundedString : " + set.size());
				break;
			}
		}
	}

	@Test
	public void randomStringTest() {
		Set<String> set = new HashSet<>();

		RandomString tickets = new RandomString(3);
		for (int i = 0; i < Integer.MAX_VALUE; i++) {

			if (!set.add(tickets.nextString())) {
				System.out.println("randomStringTest : " + set.size());
				break;
			}
		}
	}

	@Test
	public void ramdomBase64Test() {
		Set<String> set = new HashSet<>();

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			String token = RandomStringUtils.random(COUNT);
			token = Base64.getEncoder().encodeToString(token.getBytes());

			if (!set.add(token)) {
				System.out.println("RandomStringUtils.ramdomBase64Test : " + set.size());
				break;
			}
		}
	}

	public String getToken(int count) {
		String token = RandomStringUtils.random(count);
		token = Base64.getEncoder().encodeToString(token.getBytes());

		return token;
	}

	public static class RandomString {

		/**
		 * Generate a random string.
		 */
		public String nextString() {
			for (int idx = 0; idx < buf.length; ++idx)
				buf[idx] = symbols[random.nextInt(symbols.length)];
			return new String(buf);
		}

		public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		public static final String lower = upper.toLowerCase(Locale.ROOT);

		public static final String digits = "0123456789";

		public static final String alphanum = upper + lower + digits;

		private final Random random;

		private final char[] symbols;

		private final char[] buf;

		public RandomString(int length, Random random, String symbols) {
			if (length < 1)
				throw new IllegalArgumentException();
			if (symbols.length() < 2)
				throw new IllegalArgumentException();
			this.random = Objects.requireNonNull(random);
			this.symbols = symbols.toCharArray();
			this.buf = new char[length];
		}

		/**
		 * Create an alphanumeric string generator.
		 */
		public RandomString(int length, Random random) {
			this(length, random, alphanum);
		}

		/**
		 * Create an alphanumeric strings from a secure generator.
		 */
		public RandomString(int length) {
			this(length, new SecureRandom());
		}

		/**
		 * Create session identifiers.
		 */
		public RandomString() {
			this(21);
		}

	}
}
