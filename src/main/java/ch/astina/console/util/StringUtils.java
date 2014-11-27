package ch.astina.console.util;

public class StringUtils
{
    public static String repeat(String s, int n)
    {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    public static String stripTags(String s)
    {
        return s.replaceAll("\\<.*?\\>", "");
    }

    public static int count(String word, Character ch)
    {
        int pos = word.indexOf(ch);

        return pos == -1 ? 0 : 1 + count(word.substring(pos+1),ch);
    }

    public static String join(String[] parts, String glue)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            sb.append(parts[i]);
            if (i != parts.length - 1) {
                sb.append(glue);
            }
        }

        return sb.toString();
    }

    public static String ltrim(String s)
    {
        return ltrim(s, ' ');
    }

    public static String ltrim(String s, char c)
    {
        int i = 0;
        while (i < s.length() && s.charAt(i) == c) {
            i++;
        }
        return s.substring(i);
    }

    public static String rtrim(String s)
    {
        return rtrim(s, ' ');
    }

    public static String rtrim(String s, char c)
    {
        int i = s.length()-1;
        while (i >= 0 && s.charAt(i) == c) {
            i--;
        }
        return s.substring(0,i+1);
    }

    public static String padRight(String string, Integer length, char padChar)
    {
        return padRight(string, length, String.valueOf(padChar));
    }

    public static String padRight(String string, Integer length, String padString)
    {
        if (length < 1) {
            return string;
        }
        return String.format("%-" + length + "s", string).replace(" ", padString);
    }

    public static String padLeft(String string, Integer length, char padChar)
    {
        return padLeft(string, length, String.valueOf(padChar));
    }

    public static String padLeft(String string, Integer length, String padString)
    {
        if (length < 1) {
            return string;
        }
        return String.format("%" + length + "s", string).replace(" ", padString);
    }

    /**
     * Differs from String.split() in that it behaves like PHP's explode():
     * If s is the same string as c, the method returns an array with two elements both containing ""
     */
    public static String[] split(String s, char c)
    {
        if (String.valueOf(c).equals(s)) {
            return new String[]{"", ""};
        }

        return s.split(String.valueOf(c));
    }
}
