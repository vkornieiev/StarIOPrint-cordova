package pro.i_it.itcraft.printer.interfaces;

import android.app.Activity;
import android.content.Context;

import com.starmicronics.stario.PortInfo;

import pro.i_it.itcraft.printer.untils.Communication;
import rx.Observable;

/**
 * Created by syn on 02.09.16.
 */
public interface IPrinterManager {
    void init(Context context);

    Observable<PortInfo> searchPrinter();


    void saveDefaultPrinter(PortInfo portInfo, Boolean drawerOpenActiveHigh);

    Observable<Communication.Result> print(String text);


}
