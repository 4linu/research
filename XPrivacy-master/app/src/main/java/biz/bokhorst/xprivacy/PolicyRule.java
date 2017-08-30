package biz.bokhorst.xprivacy;

/**
 * Created by root on 8/27/2017.
 */
public class PolicyRule {
    
    private String attributeName;
    private String attributeValue;
    private int fact;
    
    public PolicyRule(String attrName, String compareRule, String val, int attrFact)
    {
        setAttributeName(attrName);
        setAttributeValue(compareRule + "|" + val);
        setFact(attrFact);
    }

    public PolicyRule(String attrName, String val, int attrFact) {
        setAttributeName(attrName);
        setAttributeValue(val);
        setFact(attrFact);
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public void setFact(int fact) {
        this.fact = fact;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public int getFact() {
        return fact;
    }


    public String toString()
    {
        return "{" + attributeName + "=" + attributeValue + "  effect=" + CompareRule.factToString(fact) + "}";
    }
}
