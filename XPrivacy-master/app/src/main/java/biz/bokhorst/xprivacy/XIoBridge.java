package biz.bokhorst.xprivacy;

import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.SyncStatusObserver;
import android.os.Binder;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

public class XIoBridge extends XHook {
	private Methods mMethod;
	private String mFileName;

	private static String mExternalStorage = null;
	private static String mEmulatedSource = null;
	private static String mEmulatedTarget = null;
	private static String mMediaStorage = null;
	private static String mSecondaryStorage = null;

	private XIoBridge(Methods method, String restrictionName) {
		super(restrictionName, method.name(), null);
		mMethod = method;
		mFileName = null;
	}

	private XIoBridge(Methods method, String restrictionName, String fileName) {
		super(restrictionName, method.name(), fileName);
		mMethod = method;
		mFileName = fileName;
	}

	public String getClassName() {
		return "libcore.io.IoBridge";
	}

	// @formatter:off

	// public static void connect(FileDescriptor fd, InetAddress inetAddress, int port) throws SocketException
	// public static void connect(FileDescriptor fd, InetAddress inetAddress, int port, int timeoutMs) throws SocketException, SocketTimeoutException
	// public static FileDescriptor open(String path, int flags) throws FileNotFoundException
	// public static FileDescriptor socket(boolean stream) throws SocketException
	// https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/os/Environment.java
	// https://android.googlesource.com/platform/libcore/+/android-5.0.1_r1/luni/src/main/java/libcore/io/IoBridge.java

	// @formatter:on

	private enum Methods {
		open, connect, access, openFile, openAssetFileDescriptor, openFileDescriptor, openInputStream
	};

	public static List<XHook> getInstances() {
		List<XHook> listHook = new ArrayList<XHook>();
		listHook.add(new XIoBridge(Methods.open, PrivacyManager.cStorage));
		listHook.add(new XIoBridge(Methods.connect, PrivacyManager.cInternet));
		listHook.add(new XIoBridge(Methods.access, PrivacyManager.cStorage));
		listHook.add(new XIoBridge(Methods.openFile, PrivacyManager.cStorage));
		listHook.add(new XIoBridge(Methods.openAssetFileDescriptor, PrivacyManager.cStorage));
		listHook.add(new XIoBridge(Methods.openFileDescriptor, PrivacyManager.cStorage));
		listHook.add(new XIoBridge(Methods.openInputStream, PrivacyManager.cStorage));

		listHook.add(new XIoBridge(Methods.open, PrivacyManager.cIdentification, "/proc"));
		listHook.add(new XIoBridge(Methods.open, PrivacyManager.cIdentification, "/system/build.prop"));
		listHook.add(new XIoBridge(Methods.open, PrivacyManager.cIdentification, "/sys/block/.../cid"));
		listHook.add(new XIoBridge(Methods.open, PrivacyManager.cIdentification, "/sys/class/.../cid"));
		listHook.add(new XIoBridge(Methods.open, PrivacyManager.cIdentification, "/sys/class/.../cid"));
		return listHook;
	}

	@Override
	@SuppressLint("SdCardPath")
	protected void before(XParam param) throws Throwable {
		int uid = Binder.getCallingUid();
		Util.log(this, Log.DEBUG, "Inside XIOBridge.before(), uid=" + uid);
		if (param != null)
		{
			Util.log(this, Log.DEBUG, "Inside XIOBridge.before(), uid=" + uid);
		}
		if (mMethod == Methods.connect) {
			if (param.args.length > 2 && param.args[1] instanceof InetAddress && param.args[2] instanceof Integer) {
				InetAddress address = (InetAddress) param.args[1];
				int port = (Integer) param.args[2];

				String hostName;
				boolean resolve = PrivacyManager.getSettingBool(uid, PrivacyManager.cSettingResolve, false);
				boolean noresolve = PrivacyManager.getSettingBool(-uid, PrivacyManager.cSettingNoResolve, false);
				if (resolve && !noresolve)
					try {
						hostName = address.getHostName();
					} catch (Throwable ignored) {
						hostName = address.toString();
					}
				else
					hostName = address.toString();

				if (isRestrictedExtra(param, hostName + ":" + port))
					param.setThrowable(new SocketException("XPrivacy"));
			}

		} else if (mMethod == Methods.open) {
			if (param.args.length > 0) {
				String fileName = (String) param.args[0];
				if (mFileName == null && fileName != null) {
					// Get storage folders
					if (mExternalStorage == null) {
						mExternalStorage = System.getenv("EXTERNAL_STORAGE");
						mEmulatedSource = System.getenv("EMULATED_STORAGE_SOURCE");
						mEmulatedTarget = System.getenv("EMULATED_STORAGE_TARGET");
						mMediaStorage = System.getenv("MEDIA_STORAGE");
						mSecondaryStorage = System.getenv("SECONDARY_STORAGE");
						if (TextUtils.isEmpty(mMediaStorage))
							mMediaStorage = "/data/media";
					}
					Util.log(this, Log.WARN, "XIoBridge.before().open, uid="+ uid +", var filename = " + fileName);
					// Check storage folders
					if (fileName.startsWith("/sdcard")
							|| (mExternalStorage != null && fileName.startsWith(mExternalStorage))
							|| (mEmulatedSource != null && fileName.startsWith(mEmulatedSource))
							|| (mEmulatedTarget != null && fileName.startsWith(mEmulatedTarget))
							|| (mMediaStorage != null && fileName.startsWith(mMediaStorage))
							|| (mSecondaryStorage != null && fileName.startsWith(mSecondaryStorage)))
						if (isRestrictedExtra(param, fileName))
							param.setThrowable(new FileNotFoundException("XPrivacy_IO_0"));

				} else if (fileName != null && mFileName != null && (fileName.startsWith(mFileName) || mFileName.contains("..."))) {
					// Zygote, Android
					if (Util.getAppId(Process.myUid()) == Process.SYSTEM_UID)
						return;

					// Proc white list
					if (mFileName.equals("/proc"))
						if ("/proc/self/cmdline".equals(fileName))
							return;

					// Check if restricted
					if (mFileName.contains("...")) {
						String[] component = mFileName.split("\\.\\.\\.");
						if (fileName.startsWith(component[0]) && fileName.endsWith(component[1]))
							if (isRestricted(param, mFileName))
								param.setThrowable(new FileNotFoundException("XPrivacy_IO_1"));

					} else if (mFileName.equals("/proc")) {
						if (isRestrictedExtra(param, mFileName, fileName))
							param.setThrowable(new FileNotFoundException("XPrivacy_IO_2"));

					} else {
						if (isRestricted(param, mFileName))
							param.setThrowable(new FileNotFoundException("XPrivacy_IO_3"));
					}
				}
				if (param.method.getName().compareTo("open") == 0)
				{

					String path = (String)param.args[0];

					if (path != null) {
						Util.log(this, Log.WARN, "XIoBridge.before().open uid=" + uid + ", var filename = " + fileName);
						PPolicy p = PrivacyManager.getPolicy(uid, PrivacyManager.cStorage);
						if (p.hasRules()) {
							if (!CompareRule.isAllowed(path, p)) {
								Util.log(this, Log.WARN, "XIoBridge.before().open, denying " + path + " to uid=" + uid);
								param.args[0] = "/storage/emulated/0/Documents/dummy.png";
							}
						}
					}
				}
			}
		}
		else if(mMethod == Methods.access) {
			Util.log(this, Log.WARN, "XIoBridge.before().access");
		} else {
			//Util.log(this, Log.WARN, "Unknown method=" + param.method.getName());
		}

/*
		if (param != null) {
			//Util.log(this, Log.WARN, "XIoBridge before method " + param.method.getName() + " | " + param.toString());
		} else {
			//Util.log(this, Log.WARN, "Debugging param: null");
		}

		if (param.hasThrowable()) {
			Throwable thr = param.getThrowable();
			//Util.log(this, Log.ERROR, "BAHGARestrictie = " + thr.getMessage());
			PrivacyService ps = PrivacyService.getPrivacyService();
			ps.onDemandWindow();
		}

*/
		/*if (param != null) {
			if (isRestrictedExtra(param, PrivacyManager.cStorage, param.method.getName(), null)) {
				//if (Binder.getCallingUid() == 10141) {
					param.args[0] = null;
				//}
			}

			if (isFakeDataExtra(param, PrivacyManager.cStorage, param.method.getName(), null)) {

				//if (Binder.getCallingUid() == 10141 && param.method.getName().compareTo("open") == 0) {
				if (param.method.getName().compareTo("open") == 0) {
					Util.log(this, Log.WARN, "inside before method, changhing fake data param");
					param.args[0] = "/storage/emulated/0/DCIM/fake_data.txt";
				}
			}
		}*/
	}

	@Override
	protected void after(XParam param) throws Throwable {
		// Do nothing
		Util.log(this, Log.WARN, "Inside XIoBridge.after(), uid=" + Binder.getCallingUid());
		if (param != null) {
			Util.log(this, Log.WARN, "XIoBridge.after() param=" + param.toString());
		}
	}
}
