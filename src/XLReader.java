/**
 * Created by ikirov on 4/11/2016.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * A dirty simple program that reads an Excel file.
 * @author www.codejava.net
 *
 */
public class XLReader {
    boolean average = false;
    HashMap<String, ArrayList<Float>> allChromosomes = new HashMap<>(); // Chromosome name: [Ci, L] or [Ci, SD, L, SD....]

    ContainerForIdeoStorage containerAverage = new ContainerForIdeoStorage(true); // special constructor for XL reader

    HashMap<String, float[]> XLallChromosomes = new HashMap<>();
    HashMap<String, ArrayList<ArrayList<Object>>> XLmetadataPerChromosome = new HashMap<>();

    // Check 2003 or 2007 version of XL

    private Workbook getWorkbook(FileInputStream inputStream, String excelFilePath)
            throws IOException {
        Workbook workbook = null;

        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }

    public XLReader(File filename) throws IOException {
        ArrayList<String> metanames = new ArrayList<>();
        String excelFilePath = filename.getPath();
        FileInputStream inputStream = new FileInputStream(filename);

        Workbook workbook = getWorkbook(inputStream, excelFilePath);

        // Chromosome data

        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();
        int row_cnt = 0;
        while (iterator.hasNext()) {
            row_cnt+=1;
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            if (row_cnt == 1){// read the headers
                int i = 0;
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    i++;
                }

                if (i > 7){
                    average = true;
                }
            }
            // read content of the table
            else  {
                int cell_cnt = 0;
                String chromName = "";
                float[] L_CI = new float[2];

                float[] SDLCI = new float[8];
                ArrayList<Float> row = new ArrayList<>();
                while (cellIterator.hasNext()) {


                    Cell cell = cellIterator.next();

                    cell_cnt++;
                    switch (cell_cnt) {
                        case 1:
                            chromName = cell.toString();
                            break;
                        case (2):
                            L_CI[1] = Float.valueOf(cell.toString());
                            break;
                        case (3):
                            if (!(average)){
                                L_CI[0] = Float.valueOf(cell.toString());
                            }
                            break;
                        case (4):
                            if (average){
                                L_CI[0] = Float.valueOf(cell.toString());
                            }
                    }

                    if (cell_cnt!=1){
                        row.add(Float.valueOf(cell.toString()));
                    }

                }
                if (average){
                    allChromosomes.put(chromName, row);
                }


                XLallChromosomes.put(chromName, L_CI);


            }


        }

        Sheet MetaSheet = workbook.getSheetAt(1);
        Iterator<Row> M_iterator = MetaSheet.iterator();
        row_cnt = 0;
        while (M_iterator.hasNext()) {
            row_cnt+=1;
            Row nextRow = M_iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            if (row_cnt > 1) {
                int cell_cnt = 0;

                String chromName = "";
                String metaname = "";
                float from;
                float to;

                ArrayList<Object> tempRow = new ArrayList<>();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    cell_cnt++;
                    switch (cell_cnt) {
                        case (1):
                            chromName = cell.toString();
                            break;
                        case (2)://metaname
                            tempRow.add(0,cell.toString());
                            if (!(metanames.contains(cell.toString()))){
                                metanames.add(cell.toString());
                            }
                            break;
                        case (3):
                            tempRow.add(1, Float.valueOf(cell.toString()));
                            break;
                        case (4):
                            tempRow.add(2, Float.valueOf(cell.toString()));
                            break;
                    }

                }

                if (XLmetadataPerChromosome != null){
                    if (XLmetadataPerChromosome.containsKey(chromName)){
                        XLmetadataPerChromosome.get(chromName).add(tempRow);
                    }else {
                        ArrayList<ArrayList<Object>> temp = new ArrayList<>();
                        temp.add(tempRow);
                        XLmetadataPerChromosome.put(chromName,temp);
                    }
                }

            }
        }


        workbook.close();
        inputStream.close();

    }


}