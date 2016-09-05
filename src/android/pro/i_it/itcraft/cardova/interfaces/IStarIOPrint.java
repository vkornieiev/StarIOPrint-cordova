package pro.i_it.itcraft.cardova.interfaces;

import org.apache.cordova.CallbackContext;

/**
 * Created by syn on 02.09.16.
 */
public interface IStarIOPrint {
    void searchPrinter(CallbackContext callbackContext);
    
    void print(String text,CallbackContext callbackContext);
}
