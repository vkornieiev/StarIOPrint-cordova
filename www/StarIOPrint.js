var exec = require('cordova/exec');

var StarIOPrint = function() {};

StarIOPrint.connectToPrinter = function(success, error) {
    exec(success, error, 'StarIOPrint', 'connectToPrinter', []);
};

StarIOPrint.print = function(arg0, success, error) {
    exec(success, error, 'StarIOPrint', 'cordovaPOSPrint', [arg0]);
};

module.exports = StarIOPrint;