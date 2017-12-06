var exec = require('cordova/exec');

var StarIOPrint = function() {};

StarIOPrint.getAvailablePrintersList = function(success, error) {
    exec(success, error, 'StarIOPrint', 'getAvailablePrintersList', []);
};

StarIOPrint.connectToPrinter = function(printerIP, success, error) {
    exec(success, error, 'StarIOPrint', 'connectToPrinter', [printerIP]);
};

StarIOPrint.printText = function(arg0, success, error) {
    exec(success, error, 'StarIOPrint', 'cordovaPOSPrintText', [arg0]);
};

StarIOPrint.printHTMLText = function(arg0, success, error) {
    exec(success, error, 'StarIOPrint', 'cordovaPOSPrintHTMLText', [arg0]);
};

module.exports = StarIOPrint;