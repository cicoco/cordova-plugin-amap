package unic.cicoco.cordova.amap;

import android.Manifest;
import android.content.pm.PackageManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Location extends CordovaPlugin implements AMapLocationListener {

    private static final String TAG = "GeolocationPlugin";
    private static final String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private boolean keepSendBack = false;
    private CallbackContext callback;

    public static final int GPS_REQUEST_CODE = 100001;

    public static final int PERMISSION_DENIED_ERROR = 20;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("getCurrentLocation")) {
            callback = callbackContext;
            getCurrentLocation();
            return true;
        } else {
            return false;
        }
    }

    private void getCurrentLocation() {
        boolean hasGpsLocationPermission = hasPermisssion();
        if (!hasGpsLocationPermission) {
            requestPermissions(GPS_REQUEST_CODE);
            return;
        }

        AMapLocationClient.updatePrivacyAgree(this.cordova.getActivity().getApplicationContext(), true);
        AMapLocationClient.updatePrivacyShow(this.cordova.getActivity().getApplicationContext(), true, true);

        try {
            locationClient = new AMapLocationClient(this.cordova.getActivity().getApplicationContext());
        } catch (Exception e) {
            callback.error("init locationClient failed.");
            return;
        }

        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        //设置为单次定位
        locationOption.setOnceLocation(true);
        // 设置定位监听
        locationClient.setLocationListener(this);
        locationOption.setNeedAddress(true);
        locationOption.setInterval(2000);

        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间

                JSONObject locationInfo = new JSONObject();
                try {
                    locationInfo.put("locationType", aMapLocation.getLocationType()); //获取当前定位结果来源，如网络定位结果，详见定位类型表
                    locationInfo.put("latitude", aMapLocation.getLatitude()); //获取纬度
                    locationInfo.put("longitude", aMapLocation.getLongitude()); //获取经度
                    locationInfo.put("accuracy", aMapLocation.getAccuracy()); //获取精度信息
                    locationInfo.put("speed", aMapLocation.getSpeed()); //获取速度信息
                    locationInfo.put("bearing", aMapLocation.getBearing()); //获取方向信息
                    locationInfo.put("date", date); //定位时间
                    locationInfo.put("address", aMapLocation.getAddress()); //地址，如果option中设置isNeedAddress为false，则没有此结果
                    locationInfo.put("country", aMapLocation.getCountry()); //国家信息
                    locationInfo.put("province", aMapLocation.getProvince()); //省信息
                    locationInfo.put("city", aMapLocation.getCity()); //城市信息
                    locationInfo.put("district", aMapLocation.getDistrict()); //城区信息
                    locationInfo.put("street", aMapLocation.getStreet()); //街道信息
                    locationInfo.put("streetNum", aMapLocation.getStreetNum()); //街道门牌号
                    locationInfo.put("cityCode", aMapLocation.getCityCode()); //城市编码
                    locationInfo.put("adCode", aMapLocation.getAdCode()); //地区编码
                    locationInfo.put("poiName", aMapLocation.getPoiName());
                    locationInfo.put("aoiName", aMapLocation.getAoiName());
                } catch (JSONException e) {
                    LOG.e(TAG, "Assemble Location json error:" + e);
                }
                PluginResult result = new PluginResult(PluginResult.Status.OK, locationInfo);
                if (!keepSendBack) { //不持续传回定位信息
                    locationClient.stopLocation(); //只获取一次的停止定位
                } else {
                    result.setKeepCallback(true);
                }
                callback.sendPluginResult(result);
            } else {
                LOG.e(TAG, "Get Location error:" + aMapLocation.getErrorCode());
                callback.error(String.format("[%d]%s", aMapLocation.getErrorCode(), aMapLocation.getErrorInfo()));
            }
        }
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                this.callback.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, PERMISSION_DENIED_ERROR));
                return;
            }
        }
        switch (requestCode) {
            case GPS_REQUEST_CODE: {
                getCurrentLocation();
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean hasPermisssion() {
        for (String p : permissions) {
            if (!PermissionHelper.hasPermission(this, p)) {
                return false;
            }
        }
        return true;
    }

    /*
     * We override this so that we can access the permissions variable, which no longer exists in
     * the parent class, since we can't initialize it reliably in the constructor!
     */
    @Override
    public void requestPermissions(int requestCode) {
        PermissionHelper.requestPermissions(this, requestCode, permissions);
    }
}
