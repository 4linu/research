package biz.bokhorst.xprivacy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
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

    public ArrayList<PolicyRule> rules;

    public PPolicy(int uid, String category, ArrayList<PolicyRule> rules) {
        setUid(uid);
        setCategory(category);
        setRules(rules);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

    }

    public void readFromParcel(Parcel in) {

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
}
