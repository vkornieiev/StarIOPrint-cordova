package pro.i_it.itcraft.cardova;

import android.util.Log;

import pro.i_it.itcraft.cardova.interfaces.IStarIOPrint;

/**
 * Created by syn on 02.09.16.
 */
public class StarIOPrint implements IStarIOPrint {

    private static final String TAG = StarIOPrint.class.getSimpleName();

    @Override
    public void searchPrinter() {
        Log.d(TAG, "searchPrinter: ");

    }

    @Override
    public void print(String text) {
        Log.d(TAG, "print: "+text);
    }
}
