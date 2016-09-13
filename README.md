# StarIOPrint-cordova
Cordova plugin for the StarIO printers.


# Installing
This plugin follows the Cordova 3.5+ plugin spec, so it can be installed through the Cordova CLI in your existing Cordova project

# JavaScript Usage Example

•	Example of use:
        
    onDeviceReady: function() {
    app.receivedEvent('deviceready');

    //Search for available printer
    cordova.plugins.starIOPrint.getAvailablePrintersList(
        function(success) {
            alert('Port:');
            alert(success[0].portName);
            //alert(success[0].macAddress);
            //alert(success[0].modelName);
        }
        , function(error) {
            alert(error);
        }
    );

    // Connect to selected printer.
    cordova.plugins.starIOPrint.connectToPrinter('TCP:10.3.1.195',
        function(success) { 
            alert(success); 
        }
        , function(error) { 
            alert(error); 
        }
    );


    // Print text.
    cordova.plugins.starIOPrint.print('Text To Print', 
        function(success) { 
            alert(success); 
        }
        , function(error) { 
            alert(error); 
        }
    );
    }

