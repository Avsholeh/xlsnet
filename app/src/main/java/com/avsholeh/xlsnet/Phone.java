package com.avsholeh.xlsnet;

/**
 * Phone.java mengimplementasikan tentang pengambilan informasi handset
 * @author Avsholeh
 * @since Agustus 2016
 */

import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;

public class Phone {

    public Phone() {}

    /**
     * Mengambil informasi mobile country code
     *
     * @param tm Object dari TelephonyManager
     * @return mcc Nilai dari Mobile Country Code
     */
    public int getMcc(TelephonyManager tm) {
        int mcc = 0; //default value of mnc

        //mengambil nilai numeric dari MCC + MNC
        String networkOperator = tm.getNetworkOperator();

        //melakukan pengecekan apakah variable networkOperator kosong atau tidak
        //dan apakah tipe dari handset adalah CDMA atau bukan
        if (!TextUtils.isEmpty(networkOperator) &&
                tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {

            //mengambil nilai numeric dari index 0 dengan batasan index 3
            //dan menentukan nilai mnc dari nilai networkOperator dan melakukan parsing dari String ke Integer
            mcc = Integer.parseInt(networkOperator.substring(0, 3));
        }
        return mcc;
    }

    /**
     * Mengambil informasi mobile network code
     *
     * @param tm Object dari TelephonyManager
     * @return mnc Nilai dari Mobile Network Code (int)
     */
    public int getMnc(TelephonyManager tm){
        int mnc = 0; //default value of mnc

        //mengambil nilai numeric dari MCC + MNC
        String networkOperator = tm.getNetworkOperator();

        //melakukan pengecekan apakah variable networkOperator kosong atau tidak
        //dan apakah tipe dari handset adalah CDMA atau bukan
        if (!TextUtils.isEmpty(networkOperator) &&
                tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {

            //mengambil nilai numeric dari index 3 hingga index terakhir
            //dan menentukan nilai mnc dari nilai networkOperator dan melakukan parsing dari String ke Integer
            mnc = Integer.parseInt(networkOperator.substring(3));
        }
        return mnc;
    }

    /**
     * Mengambil informasi cell id (only on gsm)
     *
     * @param tm Object dari TelephonyManager
     * @return int Merupakan nilai atau angka dari Cell Id, akan mengembalikan
     * nilai 0 jika jaringan yang digunakan bukan GSM
     */
    public int getCellId(TelephonyManager tm) {
        if (!getPhoneType(tm).equals("GSM")) return 0;
        try {
            GsmCellLocation gcl = (GsmCellLocation) tm.getCellLocation();
            return gcl.getCid() & 0xffff;
        } catch (NullPointerException e) {
            return 0;
        }
    }

    /**
     * Mengetahui tipe dari handset/cellphone
     *
     * @param tm Object dari TelephonyManager
     * @return phoneType Nilai dari Mobile Network Code (String)
     */
    public String getPhoneType(TelephonyManager tm) {
        String phoneType = "-";
        switch (tm.getPhoneType()) {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                phoneType = "CDMA";
                break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                phoneType = "GSM";
                break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                phoneType = "NONE";
                break;
            case (TelephonyManager.PHONE_TYPE_SIP):
                phoneType = "SIP";
                break;
            default:
                return phoneType;
        }
        return phoneType;
    }

    /**
     * Mengetahui informasi tentang jenis jaringan yang digunakan oleh handset
     *
     * @param tm Object dari TelephonyManager
     * @return networkType Nilai dari Network Type (String)
     */
    public String getNetworkType(TelephonyManager tm) {
        String networkType = "-";
        switch (tm.getNetworkType()) {
            //for 2g network type
            case (TelephonyManager.NETWORK_TYPE_GPRS): //2.5g
                networkType = "GPRS";
                break;
            case (TelephonyManager.NETWORK_TYPE_CDMA): //2.5g
                networkType = "CDMA";
                break;
            case (TelephonyManager.NETWORK_TYPE_EDGE): //2.75g
                networkType = "EDGE";
                break;

            //for 3g network type
            case (TelephonyManager.NETWORK_TYPE_UMTS): //3g
                networkType = "UMTS";
                break;
            case (TelephonyManager.NETWORK_TYPE_HSPA): //3.5g
                networkType = "HSPA";
                break;

            //for 4g network type
            case (TelephonyManager.NETWORK_TYPE_LTE):
                networkType = "LTE";
                break;

            //for unknown network type
            case (TelephonyManager.NETWORK_TYPE_UNKNOWN):
                return networkType;

            case (TelephonyManager.NETWORK_TYPE_EVDO_0):
                networkType = "EVDO 0";
                break;
            case (TelephonyManager.NETWORK_TYPE_EVDO_A):
                networkType = "EVDO A";
                break;
            case (TelephonyManager.NETWORK_TYPE_1xRTT):
                networkType = "1xRTT";
                break;
            case (TelephonyManager.NETWORK_TYPE_HSDPA):
                networkType = "HSDPA";
                break;
            case (TelephonyManager.NETWORK_TYPE_HSUPA):
                networkType = "HSUPA";
                break;
            case (TelephonyManager.NETWORK_TYPE_IDEN):
                networkType = "IDEN";
                break;
            case (TelephonyManager.NETWORK_TYPE_EVDO_B):
                networkType = "EVDO B";
                break;
            case (TelephonyManager.NETWORK_TYPE_EHRPD):
                networkType = "EHRPD";
                break;
            case (TelephonyManager.NETWORK_TYPE_HSPAP):
                networkType = "HSPAP";
                break;
        }
        return networkType;
    }
}
