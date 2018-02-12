package khengat.sagar.scanqrc.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import khengat.sagar.scanqrc.R;
import khengat.sagar.scanqrc.util.RawTextFileUtils;


/**
 * This class is all about the About Dialog
 */

public class AboutDialog extends Dialog {

    private static final String TAG = AboutDialog.class.getName();

    private Context mContext = null;

    public AboutDialog(Context context) {
        super(context);

        mContext = context;

    }

    /**
     * Standard Android on create method that gets called when the activity
     * initialized.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.about);

        TextView tv = (TextView) findViewById(R.id.legal_text);

        tv.setText(RawTextFileUtils.readRawTextFile(mContext, R.raw.copyright));

        tv = (TextView) findViewById(R.id.info_version);
        String packageName = getContext().getPackageName();
        PackageInfo packageInfo;
        try {
            packageInfo = getContext().getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            String appInfo = "ScanQRC";
            String versionInfo = "Version " +
                    packageInfo.versionName + " (Build " +
                    Integer.toString(packageInfo.versionCode) + ")";
            tv.setText(appInfo + "\n" + versionInfo);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Call to getPackageInfo() failed! => ", e);
        }

    }

}