# StarIOPrint-cordova
Cordova/PhoneGap plugin for the StarIO printers.


# Installing
This plugin follows the Cordova 3.0+ plugin spec, so it can be installed through the Cordova CLI in your existing Cordova project

# JavaScript Usage Example

•	Send custom text on printer
	var StarIOPrint = cordova.require("com-Star-plugins-StarPrint.StarIOPrint");

	var success  = function() { alert('Success block called!'); } 
	var fail = function() { alert('Fail block called!'); }
        
	StarIOPrint.cordovaPOSPrint('Text To Print', success, fail);


# Important

If you change this plugin, next time you update any plugin your changes will be overwritten. You need to figure out how to disable auto updates.
