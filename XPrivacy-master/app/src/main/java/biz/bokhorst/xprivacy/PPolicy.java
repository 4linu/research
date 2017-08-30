package biz.bokhorst.xprivacy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.jar.Attributes;

/**
 * Created by root on 8/27/2017.
 */
public class PPolicy implements Parcelable {


    public static final int POLICY_ACTIVE = 1;
    public static final int POLICY_DISABLED = 0;
    public static final int ALLOW_OVERRIDES = 1;
    public static final int DENY_OVERRIDES = 0;
    private int uid;
    private String category;
    private int restrictionType;
    private int overrides;

    private long mExpiry;

    public ArrayList<PolicyRule> rules;

    public PPolicy(int uid, String category, ArrayList<PolicyRule> rules) {
        setUid(uid);
        setCategory(category);
        setOverrides(ALLOW_OVERRIDES);
        setRestrictionType(POLICY_ACTIVE);
        setRules(rules);
        setExpiry(0);
    }

    public PPolicy(int uid, String category, int overrides, ArrayList<PolicyRule> rules) {
        setUid(uid);
        setCategory(category);
        setOverrides(overrides);
        setRestrictionType(POLICY_ACTIVE);
        setRules(rules);
        setExpiry(0);
    }

    public PPolicy(int uid, String category) {
        setUid(uid);
        setCategory(category);
        setRestrictionType(POLICY_ACTIVE);
        setOverrides(ALLOW_OVERRIDES);
        rules = new ArrayList<PolicyRule>();
        setExpiry(0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(uid);

        out.writeString(category);
        out.writeInt(overrides);
        out.writeInt(rules.size());
        for (PolicyRule r : rules)
        {
            out.writeString(r.getAttributeName());
            out.writeString(r.getAttributeValue());
            out.writeInt(r.getFact());
        }
    }

    public void readFromParcel(Parcel in) {
        int rulesSize = 0;
        uid = in.readInt();
        category = in.readString();
        overrides = in.readInt();
        rulesSize = in.readInt();
        if (rulesSize > 0) {
            if (rules == null)
                rules = new ArrayList<PolicyRule>();
            while (rulesSize > 0) {
                PolicyRule r = new PolicyRule(in.readString(), in.readString(), in.readInt());
                rules.add(r);
                rulesSize--;
            }
        }
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setRules(ArrayList<PolicyRule> rules) {
        this.rules = rules;
    }

    public int getUid() {
        return uid;
    }

    public static final Parcelable.Creator<PPolicy> CREATOR = new Parcelable.Creator<PPolicy>() {
        public PPolicy createFromParcel(Parcel in) {
            return new PPolicy(in);
        }

        public PPolicy[] newArray(int size) {
            return new PPolicy[size];
        }
    };

    private PPolicy(Parcel in) {
        readFromParcel(in);
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<PolicyRule> getRules() {
        return rules;
    }

    @Override
    public boolean equals(Object obj) {
        PPolicy other = (PPolicy) obj;
        // @formatter:off
        return (uid == other.getUid()
                && category.compareTo(other.getCategory()) == 0);
    }

    @Override
    public int hashCode() {
        int hash = uid;
        if (category != null)
            hash = hash ^ category.hashCode();
        return hash;
    }

    public void setExpiry(long time) {
        mExpiry = time;
    }

    public boolean isExpired() {
        return (new Date().getTime() > mExpiry);
    }

    @Override
    public String toString()
    {
        String ret = uid + ":" + category + ":overrides:" + overrides;

        if (hasRules())
        {
            ret += "  rules: ";
            for (PolicyRule r : rules)
            {
                ret += r.toString() + "  ";
            }
        }

        return ret;
    }

    public boolean hasRules() {
        return rules != null && rules.size() > 0;
    }

    public void setOverrides(int overrides) {
        this.overrides = overrides;
    }

    public int getOverrides() {
        return overrides;
    }

    public int getRestrictionType() {
        return restrictionType;
    }

    public void setRestrictionType(int restrictionType) {
        this.restrictionType = restrictionType;
    }
}
