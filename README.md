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
`window.plugins.aMapLocationPlugin.getCurrentPosition(success, fail);`

### success callback params properties
	#### locationType  获取当前定位结果来源，如网络定位结果，详见定位类型表，仅支持Android
	#### latitude  获取纬度
	#### longitude  获取经度
	#### accuracy  获取精度信息
	#### speed  获取速度信息
	#### bearing  获取方向信息
	#### date  定位时间
	#### address  地址详情
	#### country  国家信息
	#### province  省信息
	#### city  城市信息
	#### district  城区信息
	#### street  街道信息
	#### streetNum  街道门牌号
	#### cityCode  城市编码
	#### adCode  地区编码
	#### poiName POI名称
	#### aoiName AOI名称


### fail callback params properties
	#### code
	#### message

### example
	window.plugins.aMapLocationPlugin.getCurrentPosition(function(response){
			console.log(response.locationType);
			console.log(response.latitude);
			console.log(response.longitude);
		},function(response){
			console.log(response.code);
			console.log(response.message);
		})

### Supported Platforms
- Android
- iOS


