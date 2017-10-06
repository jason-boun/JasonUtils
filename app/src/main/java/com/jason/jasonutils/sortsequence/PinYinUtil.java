package com.jason.jasonutils.sortsequence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 与中文转拼音相关的工具类
 */
public class PinYinUtil {
    /**
     * @param str
     * @return 数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * @param str
     * @return 拼音
     */
    public static boolean isPinYin(String str) {
        Pattern pattern = Pattern.compile("[a-zA-Z]*");
        return pattern.matcher(str).matches();
    }

    /**
     * @param str
     * @return 中文
     */
    public static boolean containCn(String str) {
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]");
        return pattern.matcher(str).find();
    }

    public static List<String> getPinyinList(List<String> list) {
        List<String> pinyinList = new ArrayList<String>();
        for (Iterator<String> i = list.iterator(); i.hasNext();) {
            String str = (String) i.next();
            try {
                String pinyin = getPinYin(str);
                pinyinList.add(pinyin);
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        }
        return pinyinList;
    }

    public static String getPinYin(String zhongwen) throws BadHanyuPinyinOutputFormatCombination {
        String zhongWenPinYin = "";
        char[] chars = zhongwen.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            String[] pinYin = PinyinHelper.toHanyuPinyinStringArray(chars[i], getDefaultOutputFormat());
            if (pinYin != null) {
                zhongWenPinYin += pinYin[0];
            } else {
                zhongWenPinYin += chars[i];
            }
        }
        return zhongWenPinYin;
    }

    private static HanyuPinyinOutputFormat getDefaultOutputFormat() {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
        return format;
    }

	/**
	  * 输入中文字符串，返回这个中文的拼音，例如 夏=Xia，张=Zhang
	  * @param string
	  * @return
	  */
    public static String getPingYin(String string) {
        if (null != string && string.length() > 0) {
            StringBuffer sb = new StringBuffer();
            try {
                char[] chars = string.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    String strings = PinYinUtil.getPinYin(chars[i] + "").toLowerCase();
                    sb.append(strings.substring(0, 1).toUpperCase() + strings.substring(1));
                }
                return sb.toString();
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
	/**
	 * 获得汉语拼音首字母
	 * @param str
	 * @return
	 */
	public static String getAlpha(String str) {
		if (str == null) {
			return "#";
		}

		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}
}
