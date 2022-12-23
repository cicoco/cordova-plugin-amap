# cordova-plugin-uamap

## 安装

    cordova plugin add cordova-plugin-uamap --variable AMAP_ANDROID_KEY=高德网站申请android证书 --variable AMAP_IOS_KEY=高德网站申请IOS证书

config.xml，添加配置    
<edit-config target="NSLocationWhenInUseUsageDescription" file="*-Info.plist" mode="merge">
    <string>用于打卡时签到定位</string>
</edit-config>
<edit-config target="NSLocationAlwaysAndWhenInUseUsageDescription" file="*-Info.plist" mode="merge">
    <string>用于打卡时签到定位</string>
</edit-config>
<edit-config target="NSLocationAlwaysUsageDescription" file="*-Info.plist" mode="merge">
    <string>用于打卡时签到定位</string>
</edit-config>

## 使用
- 获取当前定位

### Supported Platforms
- Android
- iOS


