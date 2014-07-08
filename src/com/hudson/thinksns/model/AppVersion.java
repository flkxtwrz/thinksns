package com.hudson.thinksns.model;
/**
 * 
 * @author Íõ´úÒø
 * 
 */
public class AppVersion {
private int AppVersionCode;
private String AppVersionName;
private String AppVersion_DownloadUrl;
private String AppVersion_UpgradeTips;
private int AppVersion_UpgradeMust;
public int getAppVersionCode() {
	return AppVersionCode;
}
public void setAppVersionCode(int appVersionCode) {
	AppVersionCode = appVersionCode;
}
public String getAppVersionName() {
	return AppVersionName;
}
public void setAppVersionName(String appVersionName) {
	AppVersionName = appVersionName;
}
public String getAppVersion_DownloadUrl() {
	return AppVersion_DownloadUrl;
}
public void setAppVersion_DownloadUrl(String appVersion_DownloadUrl) {
	AppVersion_DownloadUrl = appVersion_DownloadUrl;
}
public String getAppVersion_UpgradeTips() {
	return AppVersion_UpgradeTips;
}
public void setAppVersion_UpgradeTips(String appVersion_UpgradeTips) {
	AppVersion_UpgradeTips = appVersion_UpgradeTips;
}
public int getAppVersion_UpgradeMust() {
	return AppVersion_UpgradeMust;
}
public void setAppVersion_UpgradeMust(int appVersion_UpgradeMust) {
	AppVersion_UpgradeMust = appVersion_UpgradeMust;
}
@Override
public String toString() {
	return "AppVersion [AppVersionCode=" + AppVersionCode + ", AppVersionName="
			+ AppVersionName + ", AppVersion_DownloadUrl="
			+ AppVersion_DownloadUrl + ", AppVersion_UpgradeTips="
			+ AppVersion_UpgradeTips + ", AppVersion_UpgradeMust="
			+ AppVersion_UpgradeMust + "]";
}


}
