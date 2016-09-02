# StarIOPrint-cordova
Cordova plugin for the StarIO printers.


# Installing
This plugin follows the Cordova 3.5+ plugin spec, so it can be installed through the Cordova CLI in your existing Cordova project

# JavaScript Usage Example

•	Example of use:
        
    onDeviceReady: function() {
    app.receivedEvent('deviceready');

    // Connect to available printer.
    cordova.plugins.starIOPrint.connectToPrinter(
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

