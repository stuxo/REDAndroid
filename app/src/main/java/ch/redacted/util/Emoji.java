package ch.redacted.util;

/**
 * Created by sxo on 24/05/17.
 */

public class Emoji {

	private static final int HAPPY_EMOJI = 0x1F60A;
	private static final int TONGUE_OUT_EMOJI = 0x1F61B;
	private static final int GRINNING_EMOJI = 0x1F600;
	private static final int HEART_EMOJI = 0x2764;

	private static final String HTML_HAPPY =
		"<img border=\"0\" src=\"static/common/smileys/smile.gif\" alt=\"\" />";
	private static final String HTML_TOUNGE_OUT =
		"<img border=\"0\" src=\"static/common/smileys/tongue.gif\" alt=\"\" />";
	private static final String HTML_GRINNING =
		"<img border=\"0\" src=\"static/common/smileys/biggrin.gif\" alt=\"\" />";
	private static final String HTML_HEART =
		"<img border=\"0\" src=\"static/common/smileys/heart.gif\" alt=\"\" />";

	public static String convertEmojis(String body) {
		return body.replaceAll(HTML_TOUNGE_OUT, getEmojiByUnicode(TONGUE_OUT_EMOJI))
			.replaceAll(HTML_HAPPY, getEmojiByUnicode(HAPPY_EMOJI))
			.replaceAll(HTML_GRINNING, getEmojiByUnicode(GRINNING_EMOJI))
			.replaceAll(HTML_HEART, getEmojiByUnicode(HEART_EMOJI));
	}

	public static String getEmojiByUnicode(int unicode) {
		return new String(Character.toChars(unicode));
	}
}
