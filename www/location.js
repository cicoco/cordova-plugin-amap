var AMapLocationPlugin = function () {
};

AMapLocationPlugin.prototype.call_native = function (name, args, success ,error) {
    cordova.exec(success, error, 'AMapLocation', name, args);
};

AMapLocationPlugin.prototype.getCurrentPosition = function (success ,error) {
    this.call_native("getCurrentPosition", [], success ,error);
};

if (!window.plugins) {
    window.plugins = {};
}

if (!window.plugins.aMapLocationPlugin) {
    window.plugins.aMapLocationPlugin = new AMapLocationPlugin();
}

module.exports = new AMapLocationPlugin();