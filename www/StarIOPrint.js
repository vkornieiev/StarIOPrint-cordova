var exec = require('cordova/exec');

exports.cordovaPOSPrint = function(arg0, success, error) {
    exec(success, error, "StarIOPrint", “cordovaPOSPrint”, [arg0]);
};
