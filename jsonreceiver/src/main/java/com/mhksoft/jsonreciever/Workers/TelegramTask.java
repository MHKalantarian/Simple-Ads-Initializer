package com.mhksoft.jsonreciever.Workers;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

public class TelegramTask {
    private Context mContext;
    private List<String> telegramPackages;

    public TelegramTask(Context mContext) {
        this.mContext = mContext;
        this.telegramPackages = new ArrayList<String>() {
            {
                add("org.ir.talaeii");
                add("ir.persianfox.messenger");
                add("org.telegram.plus");
                add("ir.rrgc.telegram");
                add("ir.felegram");
                add("ir.teletalk.app");
                add("ir.alimodaresi.mytelegram");
                add("org.telegram.engmariaamani.messenger");
                add("org.telegram.igram");
                add("ir.ahoura.messenger");
                add("com.shaltouk.mytelegram");
                add("ir.ilmili.telegraph");
                add("ir.pishroid.telehgram");
                add("com.goldengram");
                add("com.telegram.hame.mohamad");
                add("ir.amatis.vistagram");
                add("org.mygram");
                add("org.securetelegram.messenger");
                add("com.mihan.mihangram");
                add("com.telepersian.behdadsystem");
                add("com.negaheno.mrtelegram");
                add("com.telegram.messenger");
                add("ir.samaanak.purpletg");
                add("com.ongram");
                add("com.parmik.mytelegram");
                add("life.telegram.messenger");
                add("com.baranak.turbogramf");
                add("com.baranak.tsupergram");
                add("com.negahetazehco.cafetelegram");
                add("ir.javan.messenger");
                add("org.abbasnaghdi.messenger");
                add("com.baranak.turbogram");
                add("org.vidogram.messenger");
                add("com.parsitelg.telegram");
                add("ir.android.telegram.post");
                add("telegram.plus");
                add("com.eightgroup.torbo_geram");
                add("org.khalkhaloka.messenger");
                add("com.groohan.telegrampronew");
                add("com.goftagram.telegram");
                add("com.Dorgram");
                add("com.bartarinhagp.telenashenas");
                add("org.kral.gram");
                add("com.farishsoft.phono");
                add("ir.talayenaaab.teleg");
                add("hamidhp88dev.mytelegram");
                add("ir.zinutech.android.persiangram");
                add("org.abbasnaghdi.messengerpay");
                add("com.hanista.mobogram");
                add("com.hanista.mobogram.three");
                add("com.hanista.mobogram.two");
                add("org.telegram.messenger");
                add("org.thunderdog.challegram");
            }
        };
    }

    public String searchForPackages() {
        for (String appPackageName : telegramPackages) {
            List<ApplicationInfo> packages;
            PackageManager packageManager;
            packageManager = mContext.getPackageManager();
            packages = packageManager.getInstalledApplications(0);

            for (ApplicationInfo packageInfo : packages) {
                if (packageInfo.packageName.equals(appPackageName)) {
                    return appPackageName;
                }
            }
        }
        return null;
    }
}
