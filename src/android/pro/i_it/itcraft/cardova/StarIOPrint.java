package pro.i_it.itcraft.cardova;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import pro.i_it.itcraft.cardova.interfaces.IStarIOPrint;
import pro.i_it.itcraft.printer.PrinterManager;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by syn on 02.09.16.
 */
public class StarIOPrint extends CordovaPlugin implements IStarIOPrint {
    
    private static final String TAG = StarIOPrint.class.getSimpleName();
    private static Context context;
    
    
    private static PrinterManager printerManager;
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, "execute: " + action);
        if (action.equals("echo")) {
            String message = args.getString(0);
            this.echo(message, callbackContext);
            Log.d(TAG, "execute: echo : " + message);
            return true;
        } else if (action.equals("connectToPrinter")) {
            searchPrinter(callbackContext);
            return true;
        } else if (action.equals("print")) {
            String message = args.getString(0);
            print(message, callbackContext);
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
    
    public static void init(Activity activity) {
        if (printerManager == null) {
            printerManager = new PrinterManager();
            printerManager.init(activity);
            context = activity;
        }
    }
    
    @Override
    public void searchPrinter(CallbackContext callbackContext) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.show();
        
        printerManager.searchPrinter()
        .observeOn(AndroidSchedulers.mainThread())
        .toList()
        .doOnNext(printer -> printerManager.saveDefaultPrinter(printer.size() > 0 ? printer.get(0) : null, true))
        .subscribe(data -> {
            String info = new Gson().toJson(data);
            Toast.makeText(context, info, Toast.LENGTH_LONG).show();
            callbackContext.success(info);
            dialog.dismiss();
        }, throwable -> {
            throwable.printStackTrace();
            callbackContext.error(throwable.getMessage());
            dialog.dismiss();
        }, () -> {
            callbackContext.error("Printer not found");
            dialog.dismiss();
        });
    }
    
    @Override
    public void print(String text,CallbackContext callbackContext) {
        printerManager.print(text)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(data -> {
            String info = new Gson().toJson(data);
            Toast.makeText(context, info, Toast.LENGTH_LONG).show();
            callbackContext.success(info);
        }, throwable -> {
            throwable.printStackTrace();
            callbackContext.error(throwable.getMessage());
        }, () -> callbackContext.error("Status result unknown =( "));
    }
}
