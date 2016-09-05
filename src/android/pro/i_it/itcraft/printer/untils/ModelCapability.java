package pro.i_it.itcraft.printer.untils;

import android.util.SparseArray;
import android.util.SparseBooleanArray;

import static com.starmicronics.starioextension.StarIoExt.Emulation;

/**
 * Printer name, emulation for each model
 */
public class ModelCapability {

    public static final int NONE = -1;
    public static final int MPOP = 0;
    public static final int FVP10 = 1;
    public static final int TSP100 = 2;
    public static final int TSP650II = 3;
    public static final int TSP700II = 4;
    public static final int TSP800II = 5;
    public static final int SP700 = 6;
    public static final int SM_S210I = 7;
    public static final int SM_S220I = 8;
    public static final int SM_S230I = 9;
    public static final int SM_T300I_T300 = 10;
    public static final int SM_T400I = 11;
    public static final int SM_L200 = 12;
    public static final int BSC10 = 13;
    public static final int SM_S210I_StarPRNT = 14;
    public static final int SM_S220I_StarPRNT = 15;
    public static final int SM_S230I_StarPRNT = 16;
    public static final int SM_T300I_T300_StarPRNT = 17;
    public static final int SM_T400I_StarPRNT = 18;

    private static final SparseArray<String> mModelTitleMap = new SparseArray<String>() {
        {
            put(MPOP, "mPOP");
            put(FVP10, "FVP10");
            put(TSP100, "TSP100");
            put(TSP650II, "TSP650II");
            put(TSP700II, "TSP700II");
            put(TSP800II, "TSP800II");
            put(SP700, "SP700");
            put(SM_S210I, "SM-S210i");
            put(SM_S220I, "SM-S220i");
            put(SM_S230I, "SM-S230i");
            put(SM_T300I_T300, "SM-T300i/T300");
            put(SM_T400I, "SM-T400i");
            put(SM_L200, "SM-L200");
            put(BSC10, "BSC10");
            put(SM_S210I_StarPRNT, "SM-S210i StarPRNT");
            put(SM_S220I_StarPRNT, "SM-S220i StarPRNT");
            put(SM_S230I_StarPRNT, "SM-S230i StarPRNT");
            put(SM_T300I_T300_StarPRNT, "SM-T300i StarPRNT");
            put(SM_T400I_StarPRNT, "SM-T400i StarPRNT");
        }
    };

    private static final SparseArray<Emulation> mEmulationMap = new SparseArray<Emulation>() {
        {
            put(MPOP, Emulation.StarPRNT);
            put(FVP10, Emulation.StarLine);
            put(TSP100, Emulation.StarGraphic);
            put(TSP650II, Emulation.StarLine);
            put(TSP700II, Emulation.StarLine);
            put(TSP800II, Emulation.StarLine);
            put(SP700, Emulation.StarDotImpact);
            put(SM_S210I, Emulation.EscPosMobile);
            put(SM_S220I, Emulation.EscPosMobile);
            put(SM_S230I, Emulation.EscPosMobile);
            put(SM_T300I_T300, Emulation.EscPosMobile);
            put(SM_T400I, Emulation.EscPosMobile);
            put(SM_L200, Emulation.StarPRNT);
            put(BSC10, Emulation.EscPos);
            put(SM_S210I_StarPRNT, Emulation.StarPRNT);
            put(SM_S220I_StarPRNT, Emulation.StarPRNT);
            put(SM_S230I_StarPRNT, Emulation.StarPRNT);
            put(SM_T300I_T300_StarPRNT, Emulation.StarPRNT);
            put(SM_T400I_StarPRNT, Emulation.StarPRNT);
        }
    };

    private static final SparseArray<String> mPortSettingsMap = new SparseArray<String>() {
        {
            put(MPOP, "");
            put(FVP10, "");
            put(TSP100, "");
            put(TSP650II, "");
            put(TSP700II, "");
            put(TSP800II, "");
            put(SP700, "");
            put(SM_S210I, "mini");
            put(SM_S220I, "mini");
            put(SM_S230I, "mini");
            put(SM_T300I_T300, "mini");
            put(SM_T400I, "mini");
            put(SM_L200, "Portable");
            put(BSC10, "escpos");
            put(SM_S210I_StarPRNT, "Portable");
            put(SM_S220I_StarPRNT, "Portable");
            put(SM_S230I_StarPRNT, "Portable");
            put(SM_T300I_T300_StarPRNT, "Portable");
            put(SM_T400I_StarPRNT, "Portable");
        }
    };

    private static final SparseArray<String[]> mModelNameArrayMap = new SparseArray<String[]>() {
        {
            put(MPOP, new String[]{"STAR mPOP-",                       // <-Bluetooth interface
                    "mPOP"});                          // <-USB interface
            put(FVP10, new String[]{"FVP10 (STR_T-001)",               // <-LAN interface
                    "Star FVP10"});                   // <-USB interface
            put(TSP100, new String[]{"TSP113", "TSP143",               // <-LAN model
                    "TSP100-",                        // <-Bluetooth model
                    "Star TSP113", "Star TSP143"});   // <-USB model
            put(TSP650II, new String[]{"TSP654II (STR_T-001)",         // Only LAN model->
                    "TSP654 (STR_T-001)",
                    "TSP651 (STR_T-001)"});
            put(TSP700II, new String[]{"TSP743II (STR_T-001)",
                    "TSP743 (STR_T-001)"});
            put(TSP800II, new String[]{"TSP847II (STR_T-001)",
                    "TSP847 (STR_T-001)"});        // <-Only LAN model
            put(SP700, new String[]{"SP712 (STR-001)",                 // Only LAN model
                    "SP717 (STR-001)",
                    "SP742 (STR-001)",
                    "SP747 (STR-001)"});
//            put(SM_S210I, new String[]{ "SM-S210i" });
//            put(SM_S220I, new String[]{ "SM-S220i" });
//            put(SM_S230I, new String[]{ "SM-S230i" });
//            put(SM_T300I_T300, new String[]{ "SM-T300i" });
//            put(SM_T400I, new String[]{ "SM-T400i" });
            put(SM_L200, new String[]{"STAR L200-", "STAR L204-",          // <-Bluetooth interface
                    "Star SM-L200", "Star SM-L204"});   // <-USB interface
            put(BSC10, new String[]{"BSC10",                           // <-LAN model
                    "Star BSC10"});                    // <-USB model
//            put(SM_S210I_StarPRNT, new String[]{ "SM-S210i" });
//            put(SM_S220I_StarPRNT, new String[]{ "SM-S220i" });
//            put(SM_S230I_StarPRNT, new String[]{ "SM-S230i" });
//            put(SM_T300I_T300_StarPRNT, new String[]{ "SM-T300i" });
//            put(SM_T400I_StarPRNT, new String[]{ "SM-T400i" });
        }
    };

    private static final SparseBooleanArray mDrawerOpenStatusArrayMap = new SparseBooleanArray() {
        {
            put(MPOP, false);
            put(FVP10, true);
            put(TSP100, true);
            put(TSP650II, true);
            put(TSP700II, true);
            put(TSP800II, true);
            put(SP700, true);
            put(SM_S210I, false);
            put(SM_S220I, false);
            put(SM_S230I, false);
            put(SM_T300I_T300, false);
            put(SM_T400I, false);
            put(SM_L200, false);
            put(BSC10, true);
            put(SM_S210I_StarPRNT, false);
            put(SM_S220I_StarPRNT, false);
            put(SM_S230I_StarPRNT, false);
            put(SM_T300I_T300_StarPRNT, false);
            put(SM_T400I_StarPRNT, false);
        }
    };

    public static String getModelTitle(int model) {
        return mModelTitleMap.get(model);
    }

    public static Emulation getEmulation(int model) {
        return mEmulationMap.get(model);
    }

    public static String getPortSettings(int model) {
        return mPortSettingsMap.get(model);
    }

    public static Boolean getDrawerOpenStatus(int model) {
        return mDrawerOpenStatusArrayMap.get(model);
    }

    /**
     * get a model constant from model name string that can be got by PortInfo.getModelName() or PortInfo.getPortName();
     */
    public static int getModel(String modelNameSrc) {
        for (int i = 0; i < mModelNameArrayMap.size(); i++) {
            for (String modelName : mModelNameArrayMap.valueAt(i)) {
                if (modelNameSrc.startsWith(modelName)) {
                    return mModelNameArrayMap.keyAt(i);
                }
            }
        }

        return NONE;
    }
}
