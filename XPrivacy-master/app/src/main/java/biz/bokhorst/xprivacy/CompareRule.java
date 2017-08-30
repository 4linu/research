package biz.bokhorst.xprivacy;

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
}

