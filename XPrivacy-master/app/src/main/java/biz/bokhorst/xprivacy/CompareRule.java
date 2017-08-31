package biz.bokhorst.xprivacy;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 8/27/2017.
 */
public class CompareRule
{
    public static final String STARTS_WITH = "Starts with";
    public static final String ENDS_WITH = "Ends with";
    public static final String IS_LIKE = "Is like";
    public static final String EQUALS = "Equals";

    public static final int FACT_PERMIT = 1;
    public static final int FACT_DENY = 0;

    private static Pattern pattern;
    private static Matcher matcher;

    public static int getFact(String fact) {
        if (fact.compareTo("permit") == 0)
            return FACT_PERMIT;
        else
            return FACT_DENY;
    }

    public static String factToString(int fact) {
        if (fact == FACT_PERMIT)
            return "permit";
        return "deny";
    }

    public static String getRegexValue(String attributeValue) {
        return attributeValue.substring(attributeValue.indexOf('|') + 1);
    }

    public static String getRegexType(String attributeValue) {
        return attributeValue.substring(0, attributeValue.indexOf('|'));
    }

    public static boolean isAllowed(String compareValue, PPolicy p) {

        if (p.getOverrides() == PPolicy.ALLOW_OVERRIDES) {
            Util.log(Log.WARN, "Inside CompareRule.isAllowed - ALLOW_OVERRIDES");
            for (PolicyRule r : p.getRules()) {
                if (r.getFact() == CompareRule.FACT_PERMIT) {
                    Util.log(Log.WARN, "Inside CompareRule.isAllowed - Matching Rule on ALLOW_OVERRIDES");
                    if (matchesRegex(compareValue, r)) {
                        return true;
                    }
                }
            }
            return false;
        }
        else {
            for (PolicyRule r : p.getRules()) {
                if (r.getFact() == CompareRule.FACT_DENY) {
                    if (matchesRegex(compareValue, r)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private static boolean matchesRegex(String compareValue, PolicyRule r) {

        String regexValue = getRegexValue(r.getAttributeValue());
        String regexType = getRegexType(r.getAttributeValue());
        try {
            if (regexType.compareTo(STARTS_WITH) == 0) {
                regexValue = "^" + regexValue.toLowerCase();
            } else if (regexType.compareTo(ENDS_WITH) == 0) {
                regexValue = regexValue.toLowerCase() + "$";
            } else if (regexType.compareTo(IS_LIKE) == 0) {
                regexValue = regexValue.toLowerCase();
            } else if (regexType.compareTo(EQUALS) == 0) {
                regexValue = regexValue.toLowerCase();
            }
        }
        catch (Exception e)
        {
            Util.log(Log.WARN, "Exception in CompareRule.matchesRegex.regexValue, e=" + e.getMessage());
        }

        try {
            pattern = Pattern.compile(regexValue);
            matcher = pattern.matcher(compareValue.toLowerCase());
        }
        catch (Exception e)
        {
            Util.log(Log.WARN, "Exception in CompareRule.matchesRegex.matcher, e=" + e.getMessage());
            return false;
        }
        if (regexType.compareTo(EQUALS) == 0)
        {
            return matcher.matches();
        }
        else
        {
            return matcher.find();
        }
    }
}

