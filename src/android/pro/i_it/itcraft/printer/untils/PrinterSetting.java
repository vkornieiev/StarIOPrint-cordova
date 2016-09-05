package pro.i_it.itcraft.printer.untils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static com.starmicronics.starioextension.StarIoExt.Emulation;

/**
 * Printer information class.
 */
@SuppressWarnings("unused")
public class PrinterSetting {

    public static final String IF_TYPE_ETHERNET  = "TCP:";
    public static final String IF_TYPE_BLUETOOTH = "BT:";
    public static final String IF_TYPE_USB       = "USB:";
    public static final String IF_TYPE_MANUAL    = "Manual:";

    public static final String PREF_KEY_PORT_NAME = "pref_key_port_name";
    public static final String PREF_KEY_MODEL_NAME = "pref_key_model_name";
    public static final String PREF_KEY_MAC_ADDRESS  = "pref_key_mac_address";
    public static final String PREF_KEY_PORT_SETTINGS  = "pref_key_port_settings";
    public static final String PREF_KEY_EMULATION  = "pref_key_emulation";
    public static final String PREF_KEY_ALLRECEIPTS_SETTINGS  = "pref_key_allreceipts_settings";
    public static final String PREF_KEY_DRAWER_OPEN_STATUS    = "pref_key_drawer_open_status";

    public static final int LANGUAGE_ENGLISH = 0;
    public static final int LANGUAGE_JAPANESE = 1;
    public static final int LANGUAGE_FRENCH = 2;
    public static final int LANGUAGE_PORTUGUESE = 3;
    public static final int LANGUAGE_SPANISH = 4;
    public static final int LANGUAGE_GERMAN = 5;
    public static final int LANGUAGE_RUSSIAN = 6;
    public static final int LANGUAGE_SIMPLIFIED_CHINESE = 7;
    public static final int LANGUAGE_TRADITIONAL_CHINESE = 8;

    public static final int PAPER_SIZE_TWO_INCH = 384;
    public static final int PAPER_SIZE_THREE_INCH = 576;
    public static final int PAPER_SIZE_FOUR_INCH = 832;
    public static final int PAPER_SIZE_ESCPOS_THREE_INCH = 512;
    public static final int PAPER_SIZE_DOT_THREE_INCH = 210;

    private Context mContext;

    private static final List<Pair<Emulation, Integer>> mEmulationList = new ArrayList<Pair<Emulation, Integer>>() {
        {
            add(new Pair<>(Emulation.StarPRNT,      0));
            add(new Pair<>(Emulation.StarLine,      1));
            add(new Pair<>(Emulation.StarGraphic,   2));
            add(new Pair<>(Emulation.StarDotImpact, 3));
            add(new Pair<>(Emulation.EscPos,        4));
            add(new Pair<>(Emulation.EscPosMobile,  5));
            add(new Pair<>(Emulation.None,          6));
        }
    };

    public PrinterSetting(Context context) {
        mContext = context;
    }

    public void write(String modelName, String portName, String macAddress, String portSettings, Emulation emulation, Boolean drawerOpenActiveHigh) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        int emulationInt = -1;

        for (Pair<Emulation, Integer> emulationPair : mEmulationList) {
            if (emulation == emulationPair.first) {
                emulationInt = emulationPair.second;
                break;
            }
        }

        prefs.edit()
                .putString(PREF_KEY_MODEL_NAME, modelName)
                .putString(PREF_KEY_PORT_NAME, portName)
                .putString(PREF_KEY_MAC_ADDRESS, macAddress)
                .putString(PREF_KEY_PORT_SETTINGS, portSettings)
                .putInt(PREF_KEY_EMULATION, emulationInt)
                .putBoolean(PREF_KEY_DRAWER_OPEN_STATUS, drawerOpenActiveHigh)
                .apply();
    }

    public void write(int allReceiptSettings) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        prefs.edit()
             .putInt(PREF_KEY_ALLRECEIPTS_SETTINGS, allReceiptSettings)
             .apply();
    }

    public String getPortName() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getString(PREF_KEY_PORT_NAME, "");
    }

    public String getModelName() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getString(PREF_KEY_MODEL_NAME, "");
    }

    public String getDeviceName() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getString(PREF_KEY_PORT_NAME, "");
    }

    public String getMacAddress() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getString(PREF_KEY_MAC_ADDRESS, "");
    }

    public String getPortSettings() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getString(PREF_KEY_PORT_SETTINGS, "");
    }

    public Emulation getEmulation() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        int emulationInt = prefs.getInt(PREF_KEY_EMULATION, -1);

        for (Pair<Emulation, Integer> emulationPair : mEmulationList) {
            if (emulationInt == emulationPair.second) {
                return emulationPair.first;
            }
        }
        
        return Emulation.None;
    }

    public int getAllReceiptSetting() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getInt(PREF_KEY_ALLRECEIPTS_SETTINGS, 0);
    }

    public Boolean getCashDrawerOpenActiveHigh() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getBoolean(PREF_KEY_DRAWER_OPEN_STATUS, false);
    }

    public Context getContext() {
        return mContext;
    }
}
