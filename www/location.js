var exec = require("cordova/exec");
var AMapLocation = {
  getCurrentPosition: function (params, success, error) {
    exec(success, error, "AMapLocation", "getCurrentPosition", params);
  }
};

module.exports = AMapLocation;
