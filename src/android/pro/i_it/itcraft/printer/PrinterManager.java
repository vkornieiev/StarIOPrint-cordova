package pro.i_it.itcraft.printer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.starmicronics.stario.PortInfo;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.starioextension.ICommandBuilder;
import com.starmicronics.starioextension.StarIoExt;
import com.starmicronics.starioextension.StarIoExtManager;

import pro.i_it.itcraft.printer.interfaces.IPrinterManager;
import pro.i_it.itcraft.printer.untils.Communication;
import pro.i_it.itcraft.printer.untils.ModelCapability;
import pro.i_it.itcraft.printer.untils.PrinterSetting;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by syn on 02.09.16.
 */
public class PrinterManager implements IPrinterManager {


    private StarIoExtManager mStarIoExtManager;
    private PrinterSetting setting;

    private Context context;

    @Override
    public void init(Context context) {
        this.context = context;
        setting = new PrinterSetting(context);
        mStarIoExtManager = new StarIoExtManager(StarIoExtManager.Type.WithBarcodeReader, setting.getPortName(), setting.getPortSettings(), 10000, context);     // 10000mS!!!
        mStarIoExtManager.setCashDrawerOpenActiveHigh(setting.getCashDrawerOpenActiveHigh());

    }

    public String getPrinterName(){
        return setting.getModelName();
    }

    @Override
    public Observable<PortInfo> searchPrinter() {
        return Observable.just(
                PrinterSetting.IF_TYPE_ETHERNET,
                PrinterSetting.IF_TYPE_BLUETOOTH,
                PrinterSetting.IF_TYPE_USB)
                .flatMap(type -> {
                    try {
                        return Observable.from(StarIOPort.searchPrinter(type, context));
                    } catch (StarIOPortException e) {
                        return null;
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io());

    }

    @Override
    public void saveDefaultPrinter(PortInfo portInfo, Boolean drawerOpenActiveHigh) {
        if(portInfo == null)
            return;
        int model = ModelCapability.getModel(portInfo.getModelName());
        String mPortSettings = ModelCapability.getPortSettings(model);
        StarIoExt.Emulation emulation = ModelCapability.getEmulation(model);
        setting.write(portInfo.getModelName(), portInfo.getPortName(), portInfo.getMacAddress(), mPortSettings, emulation, drawerOpenActiveHigh);
    }

    @Override
    public Observable<Communication.Result> print(String text) {


        return Observable.just(text)
                .map(message -> Communication.sendCommands(createData(text), setting.getPortName(), setting.getPortSettings(), 10000, context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public byte[] createData(String message) {
        int textSize = 22;
        Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);

        Bitmap img = createBitmapFromText(message, textSize, PrinterSetting.PAPER_SIZE_TWO_INCH, typeface);
        ICommandBuilder builder = StarIoExt.createCommandBuilder(setting.getEmulation());
        builder.beginDocument();

        builder.appendBitmap(img, false);
        builder.appendCutPaper(ICommandBuilder.CutPaperAction.PartialCutWithFeed);
        builder.endDocument();

        return builder.getCommands();
    }


    static public Bitmap createBitmapFromText(String printText, int textSize, int printWidth, Typeface typeface) {
        Paint paint = new Paint();
        Bitmap bitmap;
        Canvas canvas;

        paint.setTextSize(textSize);
        paint.setTypeface(typeface);

        paint.getTextBounds(printText, 0, printText.length(), new Rect());

        TextPaint textPaint = new TextPaint(paint);
        android.text.StaticLayout staticLayout = new StaticLayout(printText, textPaint, printWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);

        // Create bitmap
        bitmap = Bitmap.createBitmap(staticLayout.getWidth(), staticLayout.getHeight(), Bitmap.Config.ARGB_8888);

        // Create canvas
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.translate(0, 0);
        staticLayout.draw(canvas);

        return bitmap;
    }
}
