package pro.i_it.itcraft.cardova;

import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import pro.i_it.itcraft.cardova.interfaces.IStarIOPrint;

/**
 * Created by syn on 02.09.16.
 */
public class StarIOPrint extends CordovaPlugin implements IStarIOPrint {

    private static final String TAG = StarIOPrint.class.getSimpleName();

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, "execute: "+action);
        if (action.equals("echo")) {
            String message = args.getString(0);
            this.echo(message, callbackContext);
            return true;
        } else if (action.equals("searchPrinter")) {
            searchPrinter();
            this.echo("All ok", callbackContext);
            return true;
        } else if (action.equals("print")) {
            String message = args.getString(0);
            print(message);
            this.echo("All ok: printed \n" + message, callbackContext);
            return true;
        }
        return false;
    }

    private void echo(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    @Override
    public void searchPrinter() {
        Log.d(TAG, "searchPrinter: ");
    }

    @Override
    public void print(String text) {
        Log.d(TAG, "searchPrinter: ");
    }
}
