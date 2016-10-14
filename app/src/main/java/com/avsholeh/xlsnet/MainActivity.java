package com.avsholeh.xlsnet;

/**
 * MainActivity.java mengimplementasikan tentang bagaimana interaksi user dengan aplikasi
 * @author Avsholeh
 * @since Agustus 2016
 */

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.os.Environment;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import jxl.read.biff.BiffException;

public class MainActivity extends AppCompatActivity {

    private TextView mccView;
    private TextView mncView;
    private TextView cellidView;
    private TextView networkTypeView;
    private TextView siteNameView;
    private TextView sectorView;
    private TextView pathView;

    private Button btnProcess;
    private EditText filename;
    private boolean fileExist = false;

    private TelephonyManager tm;
    private Phone phone;
    private String path;
    private boolean success = false;
    private boolean nonactive = false;

    private String mcc;
    private String cellid;
    private String mnc;
    private String net;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //menentukan batasan penggunaan aplikasi hanya hingga 10 November 2016
        //checkExpireDate("10-11-2016");

        //membuat folder dengan nama MGGDATA
        createFolder("MGGDATA");
        pathView = (TextView) findViewById(R.id.path);

        //declare a variable for layout id usage
        mccView = (TextView) findViewById(R.id.mcc_view);
        mncView = (TextView) findViewById(R.id.mnc_view);
        cellidView = (TextView) findViewById(R.id.cell_id_view);
        networkTypeView = (TextView) findViewById(R.id.network_type_view);
        siteNameView = (TextView) findViewById(R.id.site_name_view);
        sectorView = (TextView) findViewById(R.id.sector_view);
        btnProcess = (Button) findViewById(R.id.btnrefresh);
        filename = (EditText) findViewById(R.id.filename);

        //membuat object TelephonyManager()
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        //membuat object Phone()
        phone = new Phone();

        //menampilkan informasi handset
        showPhoneData();

        //melakukan listener pada button proses
        View.OnClickListener btn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mengecek apakah aplikasi sudah expired atau belum
                if (!nonactive) {
                    //menampilkan hasil dari method showProcessResult() jika button 'proses' di touch
                    //oleh user
                    showProcessResult(filename.getText().toString());

                } else {
                    //menampilkan notifikasi bahwa aplikasi telah melewati batas penggunaan
                    Toast.makeText(getApplicationContext(), "Expired!", Toast.LENGTH_LONG).show();
                }
            }
        };

        //menentukan setOnClickListener pada varible btnProcess
        btnProcess.setOnClickListener(btn);
    }

    /**
     * MENENTUKAN BATASAN PENGGUNAAN APLIKASI,
     *
     * @param expireDate Date dengan format 'dd-M-yyyy'
     */
    private void checkExpireDate(String expireDate) {
        try {
            //menentukan format Date yang akan digunakan
            SimpleDateFormat sf = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault());

            //mengambil waktu/Date pada saat ini
            long now = System.currentTimeMillis();

            //menentukan batasan penggunaan aplikasi
            Date expired = sf.parse(expireDate);

            //membandingkan apakah waktu/Date saat ini lebih besar dari batasan waktu aplikasi
            if (now > expired.getTime()) {
                //memberikan nilai true pada nonactive
                nonactive = true;
            }
        } catch (ParseException e) {
            //do nothing
        }
    }

    /**
     * MENAMPILKAN HASIL DARI PENCARIAN PADA MICROSOFT EXCEL
     *
     * @param filename Nama file yang akan dibaca datanya
     */
    private void showProcessResult(String filename) {
        //menginisialisasi object Database() berdasarkan return dari method searchDataByCellId
        Database book = searchDataByCellId(phone.getCellId(tm), filename);

        //mengecek apakah file tersedia
        if (fileExist) {

            //mengecek apakah nilai dari siteName adalah UNKNOWN atau sector adalah 0
            if (!book.getSiteName().equals(Database.UNKNOWN) || book.getSector() != Database.DEFAULT_VALUE) {

                //menampilkan nilai dari siteName dan sector
                siteNameView.setText(book.getSiteName());
                sectorView.setText(String.valueOf(book.getSector()));

            } else {

                //menampilkan notifikasi bahwa cell id pada handset tidak terdapat pada file Microsoft Excel
                Toast.makeText(getApplicationContext(), "Cell ID tidak terdapat pada file " + filename, Toast.LENGTH_LONG).show();

                //menampilkan nilai Unknown dan 0 pada siteNameView dan sectorView
                siteNameView.setText(Database.UNKNOWN);
                sectorView.setText(String.valueOf(Database.DEFAULT_VALUE));
            }
        } else {
            //menampilkan notifikasi bahwa file tidak tersedia
            Toast.makeText(getApplicationContext(), "File " +filename+ " tidak tersedia.", Toast.LENGTH_LONG).show();
            fileExist = false;
        }

        //memanggil kembali method showPhoneData()
        showPhoneData();
    }

    /**
     * MENGAMBIL DATA DARI OBJECT PHONE
     */
    private void showPhoneData() {
        this.mcc = String.valueOf(phone.getMcc(tm));
        this.mnc = String.valueOf(phone.getMnc(tm));
        this.cellid = String.valueOf(phone.getCellId(tm));
        this.net = phone.getNetworkType(tm);

        mccView.setText(this.mcc);
        mncView.setText(this.mnc);
        cellidView.setText(this.cellid);
        networkTypeView.setText(this.net);

        pathView.setText("Path: " + this.path);
    }

    /**
     * MEMBUAT FOLDER UNTUK MENENTUKAN TEMPAT PENYIMPANAN FILE MICROSOFT EXCEL
     *
     * @param name Nama folder
     */
    private void createFolder(String name) {
        //menentukan path untuk folder yang akan dibuat
        File folder = new File(Environment.getExternalStorageDirectory()+ File.separator + name);

        //memeriksa apakah folder sudah tersedia
        if (!folder.exists()) {
            //membuat folder baru dan mengembalikan nilai boolean pada variable success
            success = folder.mkdir();
        } else {
            success = true;
        }

        //menentukan nilai dari variable path dari method getPath()
        path = folder.getPath();
    }

    /**
     * MENGAMBIL PATH DARI FILE MICROSOFT EXCEL
     *
     * @param name Nama file microsoft excel
     * @return String
     */
    private String getFileFromPath(String name) {
        return this.path + "/" + name + ".xls";
    }


    /**
     * MENCARI DATA PADA MICROSOFT EXCEL BERDASARKAN CELL ID PADA HANDSET
     *
     * @param cellid
     * @param filename Nama file microsoft excel
     */
    private Database searchDataByCellId(int cellid, String filename) {
        Database book = new Database();
        try {
            if (success) {
                List<Database> list = book.readFromExcelFile(getFileFromPath(filename));
                fileExist = true;
                for (Database wb : list) {
                    if (wb.getCellId() == cellid) {
                        book.setNumber(wb.getNumber());
                        book.setCellId(wb.getCellId());
                        book.setSiteName(wb.getSiteName());
                        book.setSector(wb.getSector());
                        return book;
                    }
                }
            }
        } catch (BiffException | IOException e) {
            //do nothing
            fileExist = false;
        }
        return book;
    }
}
