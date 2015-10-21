package xyz.ayadev.junkcallblocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

public class IncomingCallReceiver extends BroadcastReceiver {

    private DbHelper dbhelper = null;

    @Override
    public void onReceive(final Context context, Intent intent) {
        final TelephonyManager Telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Telephony.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int State, String IncomingNumber) {
                super.onCallStateChanged(State, IncomingNumber);
                dbhelper = new DbHelper(context);
                try {
                    SQLiteDatabase Database = dbhelper.getReadableDatabase();
                    String Number = IncomingNumber.substring(0, 1);
                    if (Number.equals("+")) Number = "";
                    Log.e("Incoming", Number);
                    Cursor Cursor = Database.rawQuery("SELECT * FROM Phone3 WHERE Number = " + IncomingNumber, null);
                    if (Cursor.getCount() > 0) {
                        TelephonyManager TelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        Class<?> ClassTelephony = Class.forName(TelephonyManager.getClass().getName());
                        Method MethodGetITelephony = ClassTelephony.getDeclaredMethod("getITelephony");
                        MethodGetITelephony.setAccessible(true);
                        Object TelephonyInterface = MethodGetITelephony.invoke(TelephonyManager);
                        Class<?> TelephonyInterfaceClass = Class.forName(TelephonyInterface.getClass().getName());
                        Method MethodEndCall = TelephonyInterfaceClass.getDeclaredMethod("endCall");
                        MethodEndCall.invoke(TelephonyInterface);
                    }
                    Cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dbhelper.close();
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

}