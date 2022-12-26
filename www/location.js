var exec = require("cordova/exec");
var AMapLocation = {
  getCurrentLocation: function (params, success, error) {
    exec(success, error, "AMapLocation", "getCurrentPosition", params);
  }
};

module.exports = AMapLocation;
