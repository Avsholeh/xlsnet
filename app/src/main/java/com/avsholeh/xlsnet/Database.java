package com.avsholeh.xlsnet;

/**
 * Database.java ini mengimplementasikan tentang bagaimana cara
 * mengambil data dari Microsoft Excel.
 * @author Avsholeh
 * @since Agustus 2016
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Database {

    private String siteName;
    private int number;
    private int cellId;
    private int sector;
    public static final int DEFAULT_VALUE = 0;
    public static final String UNKNOWN = "-";

    /* Memberikan nilai default pada variable */
    public Database() {
        this.number = DEFAULT_VALUE;
        this.cellId = DEFAULT_VALUE;
        this.sector = DEFAULT_VALUE;
        this.siteName = UNKNOWN;
    }

    /**
     * MEMBACA DATA FILE EXCEL,
     *
     * Pada tahap awal akan dibuatkan sebuah list, yang mana list tersebut akan diisi dengan
     * object Database() ketika melakukan perulangan pertama. Kemudian object Database()
     * akan dibuat berdasarkan jumlah baris yang ada pada EXCEL.
     *
     * Pada perulangan kedua, akan ditentukaan nilai pada varible Database() berdasarkan
     * tipe datanya masing-masing.
     *
     * @param filename nama file excel yang akan dibaca, disertai dengan ekstensi ex: XL2017.xls
     * @return List<Database> listData
     */
    public List<Database> readFromExcelFile(String filename) throws IOException, BiffException {
        //membuat list untuk menyimpan data
        List<Database> listData = new ArrayList<>();

        //memulai membaca file EXCEL
        Workbook workbook = Workbook.getWorkbook(new File(filename));

        //menetapkan bahwa sheet pada EXCEL yang akan dibaca adalah sheet pertama
        Sheet sheet = workbook.getSheet(0);

        //melakukan perulangan berdasarkan jumlah row/baris
        for (int row = 1; row < sheet.getRows(); row++) {

            //membuat object baru dari class Database()
            Database aBook = new Database();

            //melakukan perulangan berdasarkan jumlah kolom
            for (int column = 0; column < sheet.getColumns(); column++) {

                //mengambil data cell dari kolom dan baris tertentu
                Cell cell = sheet.getCell(column, row);
                switch (column) {
                    case 0:
                        aBook.setNumber(Integer.valueOf(cell.getContents()));
                        break;
                    case 1:
                        aBook.setCellId(Integer.valueOf(cell.getContents()));
                        break;
                    case 2:
                        aBook.setSiteName(cell.getContents());
                        break;
                    case 3:
                        aBook.setSector(Integer.valueOf(cell.getContents()));
                }
            }
            //menambahkan data kedalam list yang telah dibuat sebelumnya
            listData.add(aBook);
        }
        //mengembalikan list yang telah dibuat
        return listData;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

    public void setSector(int sector) {
        this.sector = sector;
    }

    public String getSiteName() {
        return siteName;
    }

    public int getNumber() {
        return number;
    }

    public int getCellId() {
        return cellId;
    }

    public int getSector() {
        return sector;
    }
}
