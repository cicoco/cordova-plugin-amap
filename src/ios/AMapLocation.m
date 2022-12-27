#import "AMapLocation.h"
#import <AMapFoundationKit/AMapFoundationKit.h>
#import <AMapLocationKit/AMapLocationKit.h>
#import <MAMapKit/MAMapKit.h>

@implementation AMapLocation

//init Config
-(void) initConfig{
    if(!self.locationManager){
        [MAMapView updatePrivacyShow:AMapPrivacyShowStatusDidShow privacyInfo:AMapPrivacyInfoStatusDidContain];
        [MAMapView updatePrivacyAgree:AMapPrivacyAgreeStatusDidAgree];
        //set APIKey
        NSDictionary* infoDict = [[NSBundle mainBundle] infoDictionary];
        NSString* appKey = [infoDict objectForKey:@"AMapAppKey"];
        [AMapServices sharedServices].apiKey = appKey;
        
        //init locationManager
        self.locationManager = [[AMapLocationManager alloc]init];
        self.locationManager.delegate = self;
        //set DesiredAccuracy
        [self.locationManager setDesiredAccuracy:kCLLocationAccuracyHundredMeters];
    }
}

- (void)amapLocationManager:(AMapLocationManager *)manager doRequireLocationAuth:(CLLocationManager*)locationManager
{
    [locationManager requestAlwaysAuthorization];
}

- (void)getCurrentLocation:(CDVInvokedUrlCommand*)command
{
    [self initConfig];
    
    //   定位超时时间，最低2s，此处设置为5s
    self.locationManager.locationTimeout = 5;
    //   逆地理请求超时时间，最低2s，此处设置为5s
    self.locationManager.reGeocodeTimeout = 5;
    
    __weak AMapLocation *weakSelf = self;
    [self.locationManager requestLocationWithReGeocode:YES completionBlock:^(CLLocation *location, AMapLocationReGeocode *regeocode, NSError *error) {
        
        if (error) {
            NSLog(@"locError:{%zd - %@};", error.code, error.localizedDescription);
            CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:[[NSString alloc] initWithFormat:@"[%zd]%@", error.code , error.localizedDescription]];
            [weakSelf.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
            return;
        } else {
            NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
            [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
            
            NSDictionary *addressInfo = @{@"latitude": [NSNumber numberWithDouble:location.coordinate.latitude],
                                          @"longitude": [NSNumber numberWithDouble:location.coordinate.longitude],
                                          @"speed": [NSNumber numberWithDouble:location.speed],
                                          @"bearing": [NSNumber numberWithDouble:location.course],
                                          @"accuracy": [NSNumber numberWithDouble:location.horizontalAccuracy],
                                          @"date": [dateFormatter stringFromDate:location.timestamp],
                                          @"address": regeocode.formattedAddress ?: @"",
                                          @"country": regeocode.country ?: @"",
                                          @"province": regeocode.province ?: @"",
                                          @"city": regeocode.city ?: @"",
                                          @"cityCode": regeocode.citycode ?: @"",
                                          @"district": regeocode.district ?: @"",
                                          @"street": regeocode.street ?: @"",
                                          @"streetNum": regeocode.number ?: @"",
                                          @"adCode": regeocode.adcode ?: @"",
                                          @"poiName": regeocode.POIName ?: @"",
                                          @"aoiName": regeocode.AOIName ?: @""};
            CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:addressInfo];
            [weakSelf.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        }
    }];
}

- (void)amapLocationManager:(AMapLocationManager *)manager didUpdateLocation:(CLLocation *)location reGeocode:(AMapLocationReGeocode *)reGeocode
{
    NSLog(@"location:{lat:%f; lon:%f; accuracy:%f}", location.coordinate.latitude, location.coordinate.longitude, location.horizontalAccuracy);
    CDVPluginResult* result = nil;
    if (reGeocode) {
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
        
        NSDictionary *addressInfo = @{@"latitude": [NSNumber numberWithDouble:location.coordinate.latitude],
                                      @"longitude": [NSNumber numberWithDouble:location.coordinate.longitude],
                                      @"speed": [NSNumber numberWithDouble:location.speed],
                                      @"bearing": [NSNumber numberWithDouble:location.course],
                                      @"accuracy": [NSNumber numberWithDouble:location.horizontalAccuracy],
                                      @"date": [dateFormatter stringFromDate:location.timestamp],
                                      @"address": reGeocode.formattedAddress ?: @"",
                                      @"country": reGeocode.country ?: @"",
                                      @"province": reGeocode.province ?: @"",
                                      @"city": reGeocode.city ?: @"",
                                      @"cityCode": reGeocode.citycode ?: @"",
                                      @"district": reGeocode.district ?: @"",
                                      @"street": reGeocode.street ?: @"",
                                      @"streetNum": reGeocode.number ?: @"",
                                      @"adCode": reGeocode.adcode ?: @"",
                                      @"poiName": reGeocode.POIName ?: @"",
                                      @"aoiName": reGeocode.AOIName ?: @""};
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:addressInfo];
        
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    if (result) {
        [result setKeepCallbackAsBool:YES];
        [[self commandDelegate] sendPluginResult:result callbackId: self.callback];
    }
}

@end
