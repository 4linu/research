package biz.bokhorst.xprivacy;

import biz.bokhorst.xprivacy.PRestriction;
import biz.bokhorst.xprivacy.PSetting;
import biz.bokhorst.xprivacy.PPolicy;

interface IPrivacyService {
	int getVersion();
	List /* String */ check();
	boolean databaseCorrupt();
	void reportError(String message);
	Map getStatistics();

	void setRestriction(in PRestriction restriction);
	void setRestrictionList(in List<PRestriction> listRestriction);
	PRestriction getRestriction(in PRestriction restriction, boolean usage, String secret);
	PRestriction getFakeData(in PRestriction restriction, boolean usage, String secret);
	PPolicy      getPolicy(in PPolicy key);
	List<PRestriction> getRestrictionList(in PRestriction selector);
	boolean isRestrictionSet(in PRestriction restriction);
	void deleteRestrictions(int uid, String restrictionName);

	long getUsage(in List<PRestriction> restriction);
	List<PRestriction> getUsageList(int uid, String restrictionName);
	void deleteUsage(int uid);

	void setSetting(in PSetting setting);
	void setPolicy(in PPolicy p);
	void setSettingList(in List<PSetting> listSetting);
	PSetting getSetting(in PSetting setting);
	List<PSetting> getSettingList(in PSetting selector);
	void deleteSettings(int uid);

	void clear();
	void flush();
	void dump(int uid);
}
