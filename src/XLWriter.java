/**
 * Write XL report from DRAWID
 */

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class XLWriter {

    HashMap<String, Object[]> karyotoWrite;
    HashMap<String, ArrayList<ArrayList<Object>>> metatoWrite;

    public XLWriter(HashMap<String, Object[]> karyotoWrite,  HashMap<String, ArrayList<ArrayList<Object>>> metatoWrite, String picture, boolean average) throws IOException {

        this.karyotoWrite = karyotoWrite;
        this.metatoWrite = metatoWrite;

        String karyoheadersNor[] = {"Chromosome name", "Centromere index", "Chromosome length", "Short arm Length", "Long arm Length"};
        String karyoheadersAv[] = {"Chromosome name", "Centromere index","SD", "Chromosome length", "SD",
                "Short arm Length", "SD","Long arm Length", "SD"};
        String karyoheaders[];

        if (average){
            karyoheaders = karyoheadersAv;
        }else {
            karyoheaders = karyoheadersNor;
        }


        String metaheader[] = {"Chromosome name", "MetaName", "From", "To"};


        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet Karyosheet = workbook.createSheet("Chromosome");
        XSSFSheet Metasheet = workbook.createSheet("Metadata");



        XSSFFont font = workbook.createFont();
        font.setBold(true);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);

        int rowCount = 1;
        int c = 0;
        Row header = Karyosheet.createRow(rowCount);
        for (String s:karyoheaders){
            c++;
            Cell h = header.createCell(c);
            h.setCellStyle(style);
            h.setCellValue((String) s);
        }



        int cm = 0;
        Row headerM = Metasheet.createRow(rowCount);
        for (String s:metaheader){
            cm++;
            Cell hm = headerM.createCell(cm);
            hm.setCellValue((String) s);
            hm.setCellStyle(style);
        }


        // Table with karyotypic info
        for (Map.Entry<String, Object[]> chromosome: karyotoWrite.entrySet()) {
            Row row = Karyosheet.createRow(++rowCount);

            int columnCount = 0;

            for (Object field : chromosome.getValue()) {
                Cell cell = row.createCell(++columnCount);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                } else if (field instanceof Float){
                    cell.setCellValue((Float) field);
                }
            }

            // short and long arm length

            if (!(average)){
                Cell cell1 = row.createCell(++columnCount);
                float shortarmlength = ((float) chromosome.getValue()[1] * (float) chromosome.getValue()[2])/100;
                cell1.setCellValue(shortarmlength);

                Cell cell2 = row.createCell(++columnCount);
                float longarmlength = (float) chromosome.getValue()[2] - shortarmlength;
                cell2.setCellValue(longarmlength);
            }


        }

        // Table with metadata
        if (metatoWrite != null){
            rowCount = 1;

            for (Map.Entry<String, ArrayList<ArrayList<Object>>> meta: metatoWrite.entrySet()) {
                for (ArrayList<Object> ob: meta.getValue()){

                    rowCount++;
                    Row row = Metasheet.createRow(rowCount);

                    int columnCount = 0;
                    Cell chr = row.createCell(++columnCount);
                    chr.setCellValue((String) meta.getKey());


                    for (Object field : ob) {
                        System.out.println(field);
                        Cell cell = row.createCell(++columnCount);
                        if (field instanceof String) {
                            cell.setCellValue((String) field);
                        } else if (field instanceof Integer) {
                            cell.setCellValue((Integer) field);
                        } else if (field instanceof Float){
                            cell.setCellValue((Float) field);
                        }
                }

                }
                System.out.println("++++++++++++");
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream(picture + ".xlsx")) {
            workbook.write(outputStream);
        }

        JOptionPane.showMessageDialog(null,"table " + picture + ".xlsx" + " has been saved");
    }


}
