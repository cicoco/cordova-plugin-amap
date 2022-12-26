var exec = require("cordova/exec");
var AMapLocation = {
  getCurrentLocation: function (params, success, error) {
    exec(success, error, "AMapLocation", "getCurrentLocation", params);
  }
};

module.exports = AMapLocation;
