var exec = require('cordova/exec');

var StarIOPrint = function() {};

StarIOPrint.getAvailablePrintersList = function(success, error) {
    exec(success, error, 'StarIOPrint', 'getAvailablePrintersList', []);
};

StarIOPrint.connectToPrinter = function(printerIP, success, error) {
    exec(success, error, 'StarIOPrint', 'connectToPrinter', [printerIP]);
};

StarIOPrint.print = function(arg0, success, error) {
    exec(success, error, 'StarIOPrint', 'cordovaPOSPrint', [arg0]);
};

module.exports = StarIOPrint;