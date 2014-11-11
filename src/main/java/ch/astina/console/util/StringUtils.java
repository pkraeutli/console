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
        // todo implement

        return s;
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

    public static String ltrim(String s, char c)
    {
        int i = 0;
        while (i < s.length() && s.charAt(i) == c) {
            i++;
        }
        return s.substring(i);
    }

    public static String rtrim(String s, char c)
    {
        int i = s.length()-1;
        while (i >= 0 && s.charAt(i) == c) {
            i--;
        }
        return s.substring(0,i+1);
    }
}
