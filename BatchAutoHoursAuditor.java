// This is an auto-hours auditor for COPE Swedish Health Scholars created by Liem Vu
// Apache POI Documentation: https://poi.apache.org/apidocs/5.0/index.html

// To function
// 1) Download Java (You can use any)
// 2) In your Java IDE add the poi-5.0.0 folder in your CLASSPATH. It depends on the program you use so google how to do this!
// Don't need this if this program is allowed on Swedish. :)

// To use
// 1) Put the CORE extracted hours file into the Swedish Auto Hours Auditor folder.
// 2) Open the CORE file resave (with same name) as .xls file.
// 3) Under the *** FILL OUT PART *** put in your initials, today's date, the scholars last name, first name, rotation they joined,
//    whether or not they are JHS, and the file name of the CORE extracted file.
// 4) Compile and Run, the finished excel file will be in the Swedish Auto Hours Auditor Folder.
// 5) Check and make sure there's no big errors in the output file.

package source;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.lang.*;
import java.text.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.ss.usermodel.ExtendedColor;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.BorderStyle;  
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.ExcelStyleDateFormatter;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.CellValue;

public class BatchAutoHoursAuditor { // Version 1.3
   
   // *** FILL OUT PART ***
   
   public static String initials = "QLV"; // Your Initials
   public static String date = "09/09/2022"; // Today's Date
   
   // *** CODE PART (DO NOT MODIFY) ***
   
   public static void main(String[] args) throws IOException {
      
      String inputDir = "In Progress Audits"; // Input File Directory
      String outputDir = "Completed Audits"; // Output File Directory
      final File folder = new File(inputDir);
      int totalErrors = 0;
      
      // *** Stage 0: Compile Scholar Information ***
      for (final File fileEntry : folder.listFiles()) {
            String[] findScholarData = fileEntry.getName().split(" ");
            boolean JHS = false;
            if (findScholarData.length == 4 && findScholarData[3].toUpperCase().contains("JHS")) {
               JHS = true;
            }
            String fileName = fileEntry.getName();
            String firstName = findScholarData[0];
            String lastName = findScholarData[1];
            String rotation = "";
            if (JHS) {
               rotation = findScholarData[2];
            } else {
               rotation = findScholarData[2].substring(0, findScholarData[2].length() - 4);
            }
         // *** Stage 1: Read in Data ***
   
         // Reads and Builds Hours Audit 
         System.out.println("Filename: " + fileName);
         System.out.println();
         FileInputStream inputStream = new FileInputStream(new File(inputDir + "/" + fileName));
         POIFSFileSystem fs = new POIFSFileSystem(inputStream);
         HSSFWorkbook sourceWorkbook = new HSSFWorkbook(fs);
         HSSFSheet sourceSheet = sourceWorkbook.getSheetAt(0);
         
         Iterator iterator = sourceSheet.iterator(); 
         int colCount = 0;
         int rowCount = 0;
         String rowData = "";
         String rotationDate = "";
         String rotationType = "";
         String hoursType = "";
         double hours = 0.0;
         int problem = 0;
         Map<String, Double> data = new TreeMap<>();
         
         while (iterator.hasNext()) {
         
            HSSFRow row = (HSSFRow) iterator.next(); // Row Iterator
            Iterator cellIterator = row.cellIterator(); // Cell Iterator
            colCount = 0; // Column Counter
            boolean JHSHSDataError = false; // Error if JHS data is found with a HS. (should be seperate and not included in audit)
            Date cellDate = new Date();
            while (cellIterator.hasNext() && rowCount > 1) {
               HSSFCell cell = (HSSFCell) cellIterator.next();
               // Save Date
               if (colCount == 0) {
                  cellDate = cell.getDateCellValue();
                  //System.out.println(cellDate); // Bug Testing
               }
   
               // Rotation Date
               if (colCount == 6) {
                  rotationDate = cell.getStringCellValue();
                  String[] tokens = rotationDate.split(" ");
                  
                  if (tokens.length != 1 && tokens.length != 5) { // Forgot what this or statement is for || tokens[1].length() != 5) {
                     problem++;
                     System.out.println("ERROR: Typo OR Transfer Scholar found in data, please fix -- " + rotationDate);
                  }
                  
                  if (tokens.length == 5) {
                     tokens[1] = tokens[1].substring(0,4); // Year
                  
                     if (tokens[0].contains("January")) {
                        tokens[0] = "01 " + tokens[0];
                     } else if (tokens[0].contains("Febraury") || tokens[0].contains("February")) {
                        tokens[0] = "02 February";
                     } else if (tokens[0].contains("March")) {
                        tokens[0] = "03 " + tokens[0];
                     } else if (tokens[0].contains("April")) {
                        tokens[0] = "04 " + tokens[0];
                     } else if (tokens[0].contains("May")) {
                        tokens[0] = "05 " + tokens[0];
                     } else if (tokens[0].contains("June")) {
                        tokens[0] = "06 " + tokens[0];
                     } else if (tokens[0].contains("July")) {
                        tokens[0] = "07 " + tokens[0];
                     } else if (tokens[0].contains("August")) {
                        tokens[0] = "08 " + tokens[0];
                     } else if (tokens[0].contains("September")) {
                        tokens[0] = "09 " + tokens[0];
                     } else if (tokens[0].contains("October")) { 
                        tokens[0] = "10 " + tokens[0];
                     } else if (tokens[0].contains("November")) {
                        tokens[0] = "11 " + tokens[0];
                     } else if (tokens[0].contains("December")) {
                        tokens[0] = "12 " + tokens[0];
                     } else if (tokens[0].contains("PAST") || tokens[0].contains("past") || tokens[0].contains("Past")) { // Past Hours
                        problem++;
                        tokens[0] = "ERROR: PASTHOURS";
                        tokens[1] = "0";
                        System.out.println("ERROR: Detected Old Hours, data isn't in the database, will need to be done manually. Sorry!");
                     } else {
                        tokens[0] = "ERROR " + tokens[0];
                        problem++;
                        System.out.println("ERROR: Typo OR Transfer Scholar found in data, please fix -- " + rotationDate);
                        System.out.println();
                     }
                     
                     rotationDate = tokens[1] + " " + tokens[0] ;
                  }
                  
                  if (tokens.length == 1) {
                     rotationDate = cellDate.toString();
                     String[] tokens2 = rotationDate.split(" ");
                     //System.out.println("*************************** " + rotationDate);
                     if (tokens2[0].contains("1")) {
                        tokens2[0] = "01 January";
                     } else if (tokens2[0].contains("2")) {
                        tokens2[0] = "02 February";
                     } else if (tokens2[0].contains("3")) {
                        tokens2[0] = "03 March";
                     } else if (tokens2[0].contains("4")) {
                        tokens2[0] = "04 April";
                     } else if (tokens2[0].contains("5")) {
                        tokens[0] = "05 May";
                     } else if (tokens2[0].contains("6")) {
                        tokens2[0] = "06 June";
                     } else if (tokens2[0].contains("7")) {
                        tokens2[0] = "07 July";
                     } else if (tokens2[0].contains("8")) {
                        tokens2[0] = "08 August";
                     } else if (tokens2[0].contains("9")) {
                        tokens2[0] = "09 September";
                     } else if (tokens2[0].contains("10")) { 
                        tokens2[0] = "10 October";
                     } else if (tokens2[0].contains("11")) {
                        tokens2[0] = "11 November";
                     } else if (tokens2[0].contains("12")) {
                        tokens2[0] = "12 December";
                     } else if (tokens2[0].contains("PAST") || tokens2[0].contains("past") || tokens2[0].contains("Past")) { // Past Hours
                        problem++;
                        throw new IllegalStateException("ERROR: Detected Old Hours, data isn't in the database, will need to be done manually. Sorry!");
                     } else {
                        tokens2[0] = "ERROR " + tokens2[0];
                        problem++;
                        System.out.println("ERROR: Typo OR Transfer Scholar found in data, please fix -- " + rotationDate);
                        System.out.println();
                     }
                     rotationDate = tokens2[2] + " " + tokens2[0] ;
                  }
                  
               }
               
               // Rotation Type
               String otherHospital = "";
               if (colCount == 7) {
                  rotationType = cell.getStringCellValue();
   
                  if (!JHS && rotationType.contains("JHS")) { // Error for if the JHS data is in the HS data
                     JHSHSDataError = true;
                     
                  }
                  if (rotationType.contains("EH")) {
                     rotationType = rotationType;
                     
                  } else if (rotationType.contains("Acute Rehab") || rotationType.contains("6E")) {
                     rotationType = "Acute Rehab, Cherry Hill";
                     
                  } else if (rotationType.contains("Health Care Administration")) {
                     rotationType = "Administration, Cherry Hill";
                     
                  } else if (rotationType.contains("Neuro Critical Care")) {
                     rotationType =  "Critical Care Units, Cherry Hill";
                  
                  } else if (rotationType.contains("ED") && rotationType.contains("First Hill")) {
                     rotationType = "Emergency Department, First Hill";
                  
                  } else if (rotationType.contains("Emergency Department") || (rotationType.contains("ED") && rotationType.contains("Cherry"))) {
                     rotationType = "Emergency Department, Cherry Hill";
                     
                  } else if (rotationType.contains("Neuro Epilepsy") || rotationType.contains("Neurological") || rotationType.contains("5E")) {
                     rotationType = "Neuro Epilepsy, Cherry Hill";
                     
                  } else if (rotationType.contains("Neuro Telemetry") || rotationType.contains("3E")) {
                     rotationType = "Neuro Tele, Cherry Hill";
                                    
                  } else if (rotationType.contains("PACU") || rotationType.contains("Post Anesthesia Care Unit")) {
                     rotationType = "PACU/SPAU, Cherry Hill";
                                    
                  } else if (rotationType.contains("Surgical Telemetry") || rotationType.contains("4E")) {
                     rotationType = "Surgical Tele, Cherry Hill";
                                    
                  } else if (rotationType.contains("Medical Surgery") || rotationType.contains("Medical Surgical") || rotationType.contains("10E")) {
                     rotationType = "10 East, First Hill";
                                    
                  } else if (rotationType.contains("Nephrology") || rotationType.contains("11E")) {
                     rotationType = "11 East, First Hill";
                                    
                  } else if (rotationType.contains("Oncology") || rotationType.contains("12SW")) {
                     rotationType = "12 Southwest, First Hill";
                                    
                  } else if (rotationType.contains("Short Stay") || rotationType.contains("3SW")) {
                     rotationType = "3 Southwest, First Hill";
                                    
                  } else if (rotationType.contains("Intermediate Care") || rotationType.contains("7E")) {
                     rotationType = "7 East, First Hill";
                                    
                  } else if (rotationType.contains("Telemetry") || rotationType.contains("7SW") || rotationType.contains("10SW")) {
                     rotationType = "7 Southwest, First Hill";
                                    
                  } else if (rotationType.contains("Medical Respiratory") || rotationType.contains("8SW")) {
                     rotationType = "8 Southwest, First Hill";
                                    
                  } else if (rotationType.contains("General Medicine") || rotationType.contains("9SW")) {
                     rotationType = "9 Southwest, First Hill";
                                    
                  } else if (rotationType.contains("Gynecology") || rotationType.contains("11SW")) {
                     rotationType = "Gynecology 11SW, First Hill";
                                    
                  } else if (rotationType.contains("Intensive Care")) {
                     rotationType = "Intensive Care Units, First Hill";
                                    
                  } else if (rotationType.contains("Antepartum") || rotationType.contains("L&D")) {
                     rotationType = "Maternity, First Hill";
                                    
                  } else if (rotationType.contains("Neonatal")) {
                     rotationType = "Neonatal ICU, First Hill";
                                    
                  } else if (rotationType.contains("Patient Experience")) {
                     rotationType = "Patient Experience Ambassador, First Hill";
                                    
                  } else if (rotationType.contains("Pediatrics") || rotationType.contains("PICU")) {
                     rotationType = "Pediatrics, First Hill";
                                    
                  } else if (rotationType.contains("Postpartum") || rotationType.contains("4SW") || rotationType.contains("6SW")) {
                     rotationType = "Postpartum, First Hill";
                                    
                  } else if (rotationType.contains("Swedish Orthopedic") || rotationType.contains("SOI")) {
                     rotationType = "SOI";
                  
                  } else if (rotationType.contains("Endoscopy") || rotationType.contains("2E First Hill")) {
                     rotationType = "Endoscopy, First Hill";
                     
                  } else if (rotationType.contains("Lobby")) {
                     rotationType = "Z" + "Lobby"; // Added Z for key ordering
                     
                  } else if (rotationType.contains("Mentorship")) {
                     rotationType = "Mentorship Project";
                     
                  } else if (rotationType.contains("Project")) {
                     rotationType = "Z" + "Projects"; // Added Z for key ordering
                  
                  } else if (rotationType.contains("Leadership")) {
                     rotationType = "Z" + "Leadership"; // Added Z for key ordering
                  
                  } else if (rotationType.contains("Meeting")) {
                     rotationType = "Z" + "Meeting"; // Added Z for key ordering
                     
                  } else if (rotationType.contains("Training")) {
                     rotationType = "Z" + "Training"; // Added Z for key ordering
                  
                  } else if (rotationType.contains("CCU") || rotationType.contains("2E Cherry Hill")) {
                     rotationType = "Critical Care Units, Cherry Hill";
                  
                  } else if (rotationType.contains("Simulation Lab")) {
                     rotationType = "Simulation Lab, Cherry Hill";
                     
                  } else if (rotationType.contains("Ambulatory Infusion Center")) {
                     rotationType = "Ambulatory Infusion Center, First Hill";
                     
                  } else if (rotationType.contains("Peri-Operative Services")) {
                     rotationType = "Peri-Operative Services, First Hill";
                     
                  } else if (rotationType.contains("Exam")) {
                     rotationType = "Exam";
                     
                  } else {
                     problem++;
                     System.out.print("ERROR: In Scholar's data, a Rotation Type was found that is unknown, please manually add: ");
                     System.out.println(rotationType + " in " + rotationDate);
                     System.out.println();
                  }
               }
               
               // Total Hours (decimal)
               if (colCount == 14) {
                  hours = cell.getNumericCellValue();
               }
               
               // Hours Type + This section effectively doesn't do anything. Unsure why "Floor" does not equal "Floor"
               if (colCount == 21) {
                  hoursType = cell.getStringCellValue();
                  //System.out.println(hoursType); // Bug Testing
                  if (hoursType.contains("Leadership") || rotationType.contains("Leadership")) {
                     hoursType = "Leadership";
                  } else if (hoursType.equals("Lobby") || rotationType.contains("Lobby")) {
                     hoursType = "Lobby";
                  } else if (hoursType.equals("Projects") || rotationType.contains("Projects")) {
                     hoursType = "Projects";
                  } else if (hoursType.equals("Training") || rotationType.contains("Training")) {
                     hoursType = "Training";
                  } else if (hoursType.equals("Meeting") || rotationType.contains("Meeting")) {
                     hoursType = "Meeting";
                  } else {
                     hoursType = "Floor";
                  }
               }
   
               colCount++;
            }
            
            
            // Inserting Key
            if (!JHSHSDataError && rowCount > 1 && hours != 0.0) {
               rowData = rotationDate + " " + rotationType + " " + hoursType;
               if (data.containsKey(rowData)) {
                  data.put(rowData, round(data.get(rowData) + hours));
               } else {
                  data.put(rowData, round(hours));
               }
            }
               
            JHSHSDataError = false;
            rowCount++;
         }
         
         // Inserting Training Hours
         
         ArrayList<String> keyList = new ArrayList<String>(data.keySet());
   
         String[] firstKey = keyList.get(0).split(" ");
         rowData = firstKey[0] + " " + firstKey[1] + " " + firstKey[2] + " ZTraining Training";
         String misRowData = firstKey[0] + " " + firstKey[1] + " " + firstKey[2] + " ZTraining Floor"; // Some people put it as floor
         if (JHS) {
            hours = 20;
         } else {
            hours = 30;
         }
         
         if (!data.containsKey(rowData) || !data.containsKey(misRowData)) {
            data.put(rowData, hours);
         }
         
         // Finish Stage 1
         sourceWorkbook.close();
         
         
         
         // *** Stage 2: Export Data ***
         
         // Checking Total Hours Found + Printing
         double hourSums = 0.0; // Total Hour Total
         double hourSumsL = 0.0; // Leadership Hour Total
   
         for (Map.Entry<String, Double> entry : data.entrySet()) {
             System.out.println("key: " + entry.getKey() + "; value: " + entry.getValue());                                /*** print statement  ***/
             if (entry.getKey().contains("Leadership")) {
               hourSumsL += entry.getValue();
             } else {
               hourSums += entry.getValue();
             }
             
         }
         hourSums = round(hourSums);
         System.out.println();
         System.out.println("Hours Found: " + round(hourSums));
         System.out.println("Leadership Hours Found: " + round(hourSumsL));
         System.out.println();
         
         // Create Workbook
         XSSFWorkbook workbook = new XSSFWorkbook();
         
         // Create Sheet
         XSSFSheet sheet = workbook.createSheet("Hours Break Down");
         
         // Create Colors
         XSSFColor lightestBlue = new XSSFColor(new java.awt.Color(217, 225, 242));
         XSSFColor lighterBlue = new XSSFColor (new java.awt.Color(180, 198, 231));
         XSSFColor headerBlue = new XSSFColor (new java.awt.Color(48, 84, 150));
         XSSFColor highlightBlue = new XSSFColor (new java.awt.Color(0, 176, 240));
         XSSFColor highlightGreen = new XSSFColor (new java.awt.Color(169, 208, 142));
         XSSFColor highlightYellow = new XSSFColor (new java.awt.Color(255, 217, 102));
         
         
         // ** Create Header Row **
         
         
         //initialSheetFormat(); // Creates header, formats page
         ArrayList<String> header = new ArrayList<String>(); // Columns
         header.add("Rotation Date");
         header.add("Rotation");
         header.add("Floor");
         header.add("Lobby");
         header.add("PXA");
         header.add("Other Projects");
         header.add("Meeting");
         header.add("Training");
         header.add("Leadership");
         header.add("Month Total");
         header.add("Notes");
         
         // Set Header Style
         XSSFCellStyle headerStyle = workbook.createCellStyle();
         headerStyle.setFillForegroundColor(headerBlue);
         headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
         
   		Font headerFont = workbook.createFont();
   		headerFont.setFontHeightInPoints((short) 10);
   		headerFont.setFontName("Open Sans");
         headerFont.setColor(IndexedColors.WHITE.getIndex());
         headerFont.setBold(true);
   		headerStyle.setFont(headerFont);
         headerStyle.setBorderBottom(BorderStyle.THIN);
         headerStyle.setBottomBorderColor(IndexedColors.WHITE.getIndex());
         headerStyle.setBorderRight(BorderStyle.THIN);
         headerStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());
         headerStyle.setBorderTop(BorderStyle.THIN);
         headerStyle.setTopBorderColor(IndexedColors.WHITE.getIndex());
         
         // Writing Columns
         XSSFRow headerRow = sheet.createRow(0);
         for (int i = 0; i < header.size(); i++) {
            XSSFCell cell = headerRow.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header.get(i));
         }
         
         // Setting Filter (Enables the Excel sort and filter function for the titles)
         // sheet.setAutoFilter(CellRangeAddress.valueOf("A1:K1"));
         
         // Setting Column Width
         int[] headerWidths = new int[] {31,32,9,9,9,17,12,12,14,16,24};
         for (int i = 0; i < header.size(); i++) {
            sheet.setColumnWidth(i, headerWidths[i]*256);
         }
   
   
         // ** Create Hours Row **
   
         
         // Sets Style
   		Font hourFont = workbook.createFont();
   		hourFont.setFontHeightInPoints((short) 10);
   		hourFont.setFontName("Open Sans");
         
   		Font boldHourFont = workbook.createFont();
   		boldHourFont.setFontHeightInPoints((short) 10);
   		boldHourFont.setFontName("Open Sans");
         boldHourFont.setBold(true);
         
   		XSSFCellStyle firstHourStyle = workbook.createCellStyle();
         firstHourStyle.setFillForegroundColor(highlightGreen);
         firstHourStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
   		firstHourStyle.setFont(boldHourFont);
         firstHourStyle.setAlignment(HorizontalAlignment.CENTER);
         firstHourStyle.setBorderBottom(BorderStyle.THIN);
         firstHourStyle.setBottomBorderColor(IndexedColors.WHITE.getIndex());
         firstHourStyle.setBorderRight(BorderStyle.THIN);
         firstHourStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());
         firstHourStyle.setBorderTop(BorderStyle.THIN);
         firstHourStyle.setTopBorderColor(IndexedColors.WHITE.getIndex());
         
   		XSSFCellStyle hourStyle = workbook.createCellStyle();
         hourStyle.setFillForegroundColor(lightestBlue);
         hourStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
   		hourStyle.setFont(hourFont);
         hourStyle.setAlignment(HorizontalAlignment.CENTER);
         hourStyle.setBorderBottom(BorderStyle.THIN);
         hourStyle.setBottomBorderColor(IndexedColors.WHITE.getIndex());
         hourStyle.setBorderRight(BorderStyle.THIN);
         hourStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());
         hourStyle.setBorderTop(BorderStyle.THIN);
         hourStyle.setTopBorderColor(IndexedColors.WHITE.getIndex());
         
   		XSSFCellStyle hourStyle2 = workbook.createCellStyle();
         hourStyle2.setFillForegroundColor(lighterBlue);
         hourStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
   		hourStyle2.setFont(hourFont);
         hourStyle2.setAlignment(HorizontalAlignment.CENTER);
         hourStyle2.setBorderBottom(BorderStyle.THIN);
         hourStyle2.setBottomBorderColor(IndexedColors.WHITE.getIndex());
         hourStyle2.setBorderRight(BorderStyle.THIN);
         hourStyle2.setRightBorderColor(IndexedColors.WHITE.getIndex());
         hourStyle2.setBorderTop(BorderStyle.THIN);
         hourStyle2.setTopBorderColor(IndexedColors.WHITE.getIndex());
         
   		XSSFCellStyle lastHourStyle = workbook.createCellStyle();
         lastHourStyle.setFillForegroundColor(highlightYellow);
         lastHourStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
   		lastHourStyle.setFont(hourFont);
         lastHourStyle.setAlignment(HorizontalAlignment.CENTER);
         lastHourStyle.setBorderBottom(BorderStyle.THIN);
         lastHourStyle.setBottomBorderColor(IndexedColors.WHITE.getIndex());
         lastHourStyle.setBorderRight(BorderStyle.THIN);
         lastHourStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());
         lastHourStyle.setBorderTop(BorderStyle.THIN);
         lastHourStyle.setTopBorderColor(IndexedColors.WHITE.getIndex());
         
         int hourStylerCount = 0;
         
         // Writing Rows
         int numHourRows = data.size();
         int hourRowCount = 1;
         String rotationData = "";
         String hoursTypeData = "";
         XSSFFormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
         Map<String, Integer> monthData = new TreeMap<>();
         int monthLocate = 0;
         boolean repeatMonth = false;
         
         for (Map.Entry<String, Double> entry : data.entrySet()) {
            String hourKey = (String) entry.getKey();
            String[] tokensKey = hourKey.split(" ");
            String dateData = "Swedish " + tokensKey[0] + " " + tokensKey[2];
            
            // Check if Month key already exists                                           TODO: FIX LOBBY BEFORE FLOOR ENTRY
            if (monthData.containsKey(dateData)) {
               repeatMonth = true;
               monthLocate = monthData.get(dateData);
               if (tokensKey[tokensKey.length - 1].equals("Floor")) {
                  repeatMonth = false;
                  monthData.put(dateData, hourRowCount);
               }
            } else {
               monthData.put(dateData, hourRowCount);
            }
            
            // Build Rotation + Hours Type
            if (tokensKey.length == 5) {
               rotationData = tokensKey[3];
               hoursTypeData = tokensKey[4];
            } else if (tokensKey.length == 6) {
               rotationData = tokensKey[5] + " " + tokensKey[4];
               hoursTypeData = tokensKey[4];
            } else if (tokensKey.length == 7) {
               rotationData = tokensKey[3] + " " + tokensKey[4] + " " + tokensKey[5];
               hoursTypeData = tokensKey[6];
            } else if (tokensKey.length == 8) {
               rotationData = tokensKey[3] + " " + tokensKey[4] + " " + tokensKey[5] + " " + tokensKey[6];
               hoursTypeData = tokensKey[7];
            } else if (tokensKey.length == 9) {
               rotationData = tokensKey[3] + " " + tokensKey[4] + " " + tokensKey[5] + " " + tokensKey[6]+ " " + tokensKey[7];
               hoursTypeData = tokensKey[8];
            } 
            
            double hourData = entry.getValue();
            int hourColCount = 0;
            
            if (!repeatMonth) {
               XSSFRow hourRow = sheet.createRow(hourRowCount);
            
               // Putting information in Cells
               for (int j = 0; j < header.size(); j++) {
                  XSSFCell cell = hourRow.createCell(j);
                  
                  // Set Hour Style
                  if (hourRowCount == 1) {
                     cell.setCellStyle(firstHourStyle);
                  } else if (hourRowCount == numHourRows) {
                     cell.setCellStyle(lastHourStyle);
                  } else if (hourStylerCount % 2 == 0) {
                     cell.setCellStyle(hourStyle);
                  } else {
                     cell.setCellStyle(hourStyle2);
                  }
                  
                  // Fill In Hour Data
                  if (hourColCount == 0) { // Insert Date
                     cell.setCellValue(dateData);
                     
                  } else if (hourColCount == 1) { // Insert Rotation
                  
                     // If statements are for when added Z for keyordering so fix.
                     if (rotationData.contains("Lobby")) {
                        cell.setCellValue("Lobby");
                     } else if (rotationData.contains("Projects")) {
                        cell.setCellValue("Projects");
                     } else if (rotationData.contains("Leadership")) {
                        cell.setCellValue("Leadership");
                     } else if (rotationData.contains("Meeting")) {
                        cell.setCellValue("Meeting");
                     } else if (rotationData.contains("Training")) {
                        cell.setCellValue("Training");
                     } else {
                        cell.setCellValue(rotationData);
                     }
                     
                  } else if (hourColCount > 1 && hourColCount < 9) { // Insert Hours
                  
                     if (hoursTypeData.contains("Floor") && hourColCount == 2) {
                        cell.setCellValue(hourData);
                     } else if (hoursTypeData.contains("Lobby") && hourColCount == 3) {
                        cell.setCellValue(hourData);
                     } else if (hoursTypeData.contains("PXA") && hourColCount == 4) {
                        cell.setCellValue(hourData);
                     } else if (hoursTypeData.contains("Projects") && hourColCount == 5) {
                        cell.setCellValue(hourData);
                     } else if (hoursTypeData.contains("Meeting") && hourColCount == 6) {
                        cell.setCellValue(hourData);
                     } else if (hoursTypeData.contains("Training") && hourColCount == 7) {
                        cell.setCellValue(hourData);
                     } else if (hoursTypeData.contains("Leadership") && hourColCount == 8) {
                        cell.setCellValue(hourData);
                     }
                     
                  } else if (hourColCount == 9) { // Insert Rotation Total
                     cell.setCellFormula("SUM(C" + (hourRowCount + 1) + ":H" + (hourRowCount + 1) + ")");
                     
                  } else if (hourRowCount == numHourRows && hourColCount == 10) { // Insert Signature
                     cell.setCellValue("Final Audit: " + date + " " + initials);
                     
                  } else {
                     cell.setCellValue("");
                     
                  }
                  
                  hourColCount++;
               }
               
            } else { // if Repeat Month
               XSSFRow hourRow2 = sheet.getRow(monthLocate);
               // Putting information in Cells
                  if (hoursTypeData.contains("Floor")) {
                     sheet.getRow(monthLocate).getCell(1).setCellValue(rotationData);//
                     sheet.getRow(monthLocate).getCell(2).setCellValue(hourData);
                  } else if (hoursTypeData.contains("Lobby")) {
                        if (sheet.getRow(monthLocate).getCell(3) == null) {
                           sheet.getRow(monthLocate).getCell(3).setCellValue(hourData);
                        } else {
                           sheet.getRow(monthLocate).getCell(3).setCellValue(sheet.getRow(monthLocate).getCell(3).getNumericCellValue() + hourData);
                        }
                  } else if (hoursTypeData.contains("PXA")) {
                        if (sheet.getRow(monthLocate).getCell(4) == null) {
                           sheet.getRow(monthLocate).getCell(4).setCellValue(hourData);
                        } else {
                           sheet.getRow(monthLocate).getCell(4).setCellValue(sheet.getRow(monthLocate).getCell(4).getNumericCellValue() + hourData);
                        }
                  } else if (hoursTypeData.contains("Projects")) {
                        if (sheet.getRow(monthLocate).getCell(5) == null) {
                           sheet.getRow(monthLocate).getCell(5).setCellValue(hourData);
                        } else {
                           sheet.getRow(monthLocate).getCell(5).setCellValue(sheet.getRow(monthLocate).getCell(5).getNumericCellValue() + hourData);
                        }
                  } else if (hoursTypeData.contains("Meeting")) {
                        if (sheet.getRow(monthLocate).getCell(6) == null) {
                           sheet.getRow(monthLocate).getCell(6).setCellValue(hourData);
                        } else {
                           sheet.getRow(monthLocate).getCell(6).setCellValue(sheet.getRow(monthLocate).getCell(6).getNumericCellValue() + hourData);
                        }
                  } else if (hoursTypeData.contains("Training")) {
                        if (sheet.getRow(monthLocate).getCell(7) == null) {
                           sheet.getRow(monthLocate).getCell(7).setCellValue(hourData);
                        } else {
                           sheet.getRow(monthLocate).getCell(7).setCellValue(sheet.getRow(monthLocate).getCell(7).getNumericCellValue() + hourData);
                        }
                  } else if (hoursTypeData.contains("Leadership")) {
                        if (sheet.getRow(monthLocate).getCell(8) == null) {
                           sheet.getRow(monthLocate).getCell(8).setCellValue(hourData);
                        } else {
                           sheet.getRow(monthLocate).getCell(8).setCellValue(sheet.getRow(monthLocate).getCell(8).getNumericCellValue() + hourData);
                        }
                  }
                  
                  // Add up Row
                  sheet.getRow(monthLocate).getCell(9).setCellFormula("SUM(C" + (monthLocate + 1) + ":H" + (monthLocate + 1) + ")");
                  
                  if (monthLocate == numHourRows) { // Insert Signature
                     sheet.getRow(monthLocate).getCell(10).setCellValue("Final Audit: " + date + " " + initials);
                  }
               }
            
            // Iteration Values
            if (!repeatMonth) {
               hourRowCount++;
               hourStylerCount++;
            }
            repeatMonth = false;
         }
         
         
         // ** Create Total Row **
         
         
         // Set Total Style + Font
         XSSFCellStyle totalStyle = workbook.createCellStyle();
         totalStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
         totalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
   		totalStyle.setFont(headerFont);
         totalStyle.setAlignment(HorizontalAlignment.CENTER);
         totalStyle.setAlignment(HorizontalAlignment.CENTER);
         totalStyle.setBorderBottom(BorderStyle.THIN);
         totalStyle.setBottomBorderColor(IndexedColors.WHITE.getIndex());
         totalStyle.setBorderRight(BorderStyle.THIN);
         totalStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());
         totalStyle.setBorderTop(BorderStyle.THIN);
         totalStyle.setTopBorderColor(IndexedColors.WHITE.getIndex());
         
         // Writing Columns
         
         // Get numHourRows
         int comp = 0;
         int max = 0;
         for (Map.Entry<String, Integer> entry : monthData.entrySet()) {
             comp = entry.getValue();
             if (comp > max) {
               max = comp;
             }
         }
         numHourRows = max;
         
         // Set last hour row style
         for (int i = 0; i < header.size(); i++) {
            sheet.getRow(numHourRows).getCell(i).setCellStyle(lastHourStyle);
            if (i == 10) {
               sheet.getRow(numHourRows).getCell(10).setCellValue("Final Audit: " + date + " " + initials); // getRow was orignally monthLocate, forgot the logic behind that
            }
         }
         
         XSSFRow totalRow = sheet.createRow(numHourRows + 1);
         ArrayList<String> total = new ArrayList<String>(); // Columns
         total.add("Total");
         total.add("");
         total.add("SUBTOTAL(109,C2:C" + (numHourRows + 1) + ")" ); // total.add("=SUBTOTAL(109,[Floor])");
         total.add("SUBTOTAL(109,D2:D" + (numHourRows + 1) + ")" );
         total.add("SUBTOTAL(109,E2:E" + (numHourRows + 1) + ")" );
         total.add("SUBTOTAL(109,F2:F" + (numHourRows + 1) + ")" );
         total.add("SUBTOTAL(109,G2:G" + (numHourRows + 1) + ")" );
         total.add("SUBTOTAL(109,H2:H" + (numHourRows + 1) + ")" );
         total.add("SUBTOTAL(109,I2:I" + (numHourRows + 1) + ")" );
         total.add("SUBTOTAL(109,J2:J" + (numHourRows + 1) + ")" );
         total.add("Total Hours: " + round(hourSums + hourSumsL));
         
         // Fill in cells
         for (int i = 0; i < header.size(); i++) {
            XSSFCell cell = totalRow.createCell(i);
            cell.setCellStyle(totalStyle);
            if (i == 0 || i == 1 || i == 10) {
               cell.setCellValue(total.get(i));
            } else {
               cell.setCellFormula(total.get(i));
               if (i == 9) {
                  CellValue hourSum2 = evaluator.evaluate(cell);
                  double hourSum3 = round(hourSum2.getNumberValue());
                  if (hourSum3 != round(hourSums)) {
                     problem++;
                     System.out.println("ERROR: The program found " + round(hourSums) + " hours, but the total hours written out is " + hourSum3);
                     System.out.println();
                  }
   
               }
            }
         }
         
         // ** Add Rotation Letters **
         
         // Find the Start Month
         char rotLetter = rotation.charAt(4);
         String[] startMonthData = sheet.getRow(2).getCell(0).getStringCellValue().split(" ");
         String startMonth = startMonthData[2];
         String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
         int startMonthInt = 0;
         
         for (int i = 0; i < months.length; i++) {
            if (months[i].equals(startMonth)) {
               startMonthInt = i;
            }
         }
         
         // Find the Start Letter
         char[] letters = {'A', 'B', 'C', 'D'};
         int letPoint = 0;
         
         for (int i = 0; i < letters.length; i++) {
            if (rotLetter == letters[i]) {
               letPoint = i;
            }
         }
         
         // Build the Letter Month List
         String[] monthsLet = new String[12];
         int pointerMonth = 0;
         int resetPointer = 0;
         boolean reset = false;
         int letCount = 0;
         
         for (int i = 0; i < monthsLet.length; i++) {
            pointerMonth = i + startMonthInt;
            rotLetter = letters[letPoint];
            
            if (pointerMonth >= monthsLet.length) {
                pointerMonth = resetPointer;
                reset = true;
            }
            
            monthsLet[pointerMonth] = Character.toString(rotLetter);
            
            // iterations
            letCount++;
            if (letCount >= 3) {
               letCount = 0;
               letPoint++;
               if (letPoint >= 4) {
                  letPoint = 0;
               }
            }
            if (reset) {
               resetPointer++;
            }
         }
         
         
         // Add the Letters to the Sheet
         
         String rotData2 = "";
         String[] rotDataSplit = new String[3];
         String rotDataFinal = "";
         for (int i = 2; i <= numHourRows; i++) {
            rotData2 = sheet.getRow(i).getCell(0).getStringCellValue();
            rotDataSplit = rotData2.split(" ");
            
            if (rotDataSplit[2].equals("January")) {
               rotDataSplit[1] = rotDataSplit[1] + monthsLet[0];
            } else if (rotDataSplit[2].equals("February")) {
               rotDataSplit[1] = rotDataSplit[1] + monthsLet[1];
            } else if (rotDataSplit[2].equals("March")) {
               rotDataSplit[1] = rotDataSplit[1] + monthsLet[2];
            } else if (rotDataSplit[2].equals("April")) {
               rotDataSplit[1] = rotDataSplit[1] + monthsLet[3];
            } else if (rotDataSplit[2].equals("May")) {
               rotDataSplit[1] = rotDataSplit[1] + monthsLet[4];
            } else if (rotDataSplit[2].equals("June")) {
               rotDataSplit[1] = rotDataSplit[1] + monthsLet[5];
            } else if (rotDataSplit[2].equals("July")) {
               rotDataSplit[1] = rotDataSplit[1] + monthsLet[6];
            } else if (rotDataSplit[2].equals("August")) {
               rotDataSplit[1] = rotDataSplit[1] + monthsLet[7];
            } else if (rotDataSplit[2].equals("September")) {
               rotDataSplit[1] = rotDataSplit[1] + monthsLet[8];
            } else if (rotDataSplit[2].equals("October")) {
               rotDataSplit[1] = rotDataSplit[1] + monthsLet[9];
            } else if (rotDataSplit[2].equals("November")) {
               rotDataSplit[1] = rotDataSplit[1] + monthsLet[10];
            } else if (rotDataSplit[2].equals("December")) {
               rotDataSplit[1] = rotDataSplit[1] + monthsLet[11];
            }
            
            rotDataFinal = rotDataSplit[0] + " " + rotDataSplit[1] + " " + rotDataSplit[2];
            
            sheet.getRow(i).getCell(0).setCellValue(rotDataFinal);
            
         }
         
         // *** Stage 3: Export File *** 
   
         // Export File
         String JHSname;
         if (JHS) {
            JHSname = "JHS ";
         } else {
            JHSname = "";
         }
         
         try {
            String newFileName = lastName + "_" + firstName + " " + JHSname + rotation + " Hours Summary.xlsx";
            FileOutputStream out = new FileOutputStream(new File(outputDir + "/" + newFileName));
            workbook.write(out);
            out.close();
            
            totalErrors += problem;
            String problemString = Integer.toString(problem);
            if (problem == 0) {
               System.out.println(newFileName + " written successfully on disk.");
            } else {
               System.out.println("ERROR(S) DETECTED: " + newFileName + " has been written, but " + problemString + " error(s) has been found.");
            }
            System.out.println();
            System.out.println("********************************************************************************************");
            System.out.println();
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      }
      
      // Warn of the errors
      if (totalErrors > 0) {
         System.out.println();
         System.out.println("********************************************************************************************");
         System.out.println();
         System.out.println("WARNING: Error(s) need to be fixed!");
         System.out.println("TOTAL ERROR(S) DETECTED: " + Integer.toString(totalErrors) + " error(s) has been found in total to be fixed.");
         System.out.println();
         System.out.println("********************************************************************************************");
      } else {
         System.out.println();
         System.out.println("********************************************************************************************    ");
         System.out.println();
         System.out.println("Batch Auto Hours Auditor ran successfully! :D      ");
         System.out.println();
         System.out.println("********************************************************************************************    ");
      }
   }
   
   
   // Rounding Function
   public static double round (double num) {
      double answer = Math.round(num * 100.0) / 100.0;
      return answer;
   }
   
   // Print File Names Function
   public static void listFilesForFolder(final File folder) {
       for (final File fileEntry : folder.listFiles()) {
           if (fileEntry.isDirectory()) {
               listFilesForFolder(fileEntry);
           } else {
               System.out.println(fileEntry.getName());
           }
       }
   }
   
}

   /*  Extra Code that I didn't want to throw away :(
   
   // Methods for Concision
   
       public void initialSheetFormat() {
         // Header
         ArrayList<String> header = new ArrayList<String>(); // Columns
         header.add("File Name");
         header.add("Rotations");
         header.add("Floor");
         header.add("Lobby");
         header.add("PXA");
         header.add("Other Projects");
         header.add("Meeting");
         header.add("Training");
         header.add("Leadership");
         
         // Set Header Style
         XSSFCellStyle headerStyle = workbook.createCellStyle();
         headerStyle.setFillBackgroundColor(headerBlue);
         
         // Writing Columns
         XSSFRow header = sheet.createRow(0);
         for (int i = 0; i < header.size(); i++) {
            XSSFCell cell = header.createCell(i);
            cell.setCellValue(header.get(i));
            cell.setCellStyle(headerStyle);
         }
         
         // Setting Filter
         sheet.setAutoFilter(CellRangeAddress.valueOf("A1:K1"));
         
         // Setting Column Width
         int[] headerWidths = new int[] {31,21,8,8,8,15.5,10,10,12,13.1,21};
         headerWidths = headerWidths * 256;
         for (int i = 0; i < header.size(); i++) {
            sheet.setColumnWidth(i, headerWidths[i]*256);
         }

      }
      
      public void rowFormat() {
      
      }
      // Exporting the Notebook
      public void exportFile() {
         if (JHS) {
            String JHSname = "JHS ";
         } else {
            String JHSname = "";
         }
         
         try {
            String fileName = "TEST" + lastName + "_" + firstName + " " + JHS + rotation + " Hours Summary.xlsx";
            FileOutputStream out = new FileOutputStream(new File(fileName));
            workbook.write(out);
            out.close();
               
            System.out.println(fileName + " written successfully on disk.");
         
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      }
   
   // Allows support for multiple file types (in case CORE changes)
   private Workbook getWorkbook(FileInputStream inputStream, String excelFilePath) throws IOException {
       Workbook workbook = null;
       if (excelFilePath.endsWith("xlsx")) {
           workbook = new XSSFWorkbook(inputStream);
       } else if (excelFilePath.endsWith("xls")) {
           workbook = new HSSFWorkbook(inputStream);
       } else {
           throw new IllegalArgumentException("The specified file is not an Excel file");
       }
       return workbook;
   }
   
      // make gui?
}

      // Adding Rotation Letter
      
      
      // Get Rotation Letter
      String rotLetter = rotation.substring(4);
      String[] rotLetters = {"A" , "B", "C", "D"};
      int rotCount = 0;
      if (rotLetter == rotLetters[1]) {
         rotCount = 4;
      } else if (rotLetter == rotLetters[2]) {
         rotCount = 6;
      } else if (rotLetter == rotLetters[3]) {
         rotCount = 9;
      }
      
      String checkKey = "";
      Map<String, Double> data2 = new TreeMap<>();
            
      for (Map.Entry<String, Double> entry : data.entrySet()) {
         checkKey = (String) entry.getKey();
         String[] tokensKey = checkKey.split(" ");
         
         // Reset rotCount
         if (rotCount > 11) {
            rotCount = 0;
         }
         
         // Placing Value on Letter
         if (rotCount < 3) {
            rotLetter = rotLetters[0];
         } else if (rotCount > 2 && rotCount < 6) {
            rotLetter = rotLetters[1];
         } else if (rotCount > 5 && rotCount < 9) {
            rotLetter = rotLetters[2];
         } else if (rotCount > 8 && rotCount < 12) {
            rotLetter = rotLetters[3];
         } else {
            rotLetter = "ERROR";
            problem++;
         }
         
         // Check if Repeat Month/Year
         for (Map.Entry<String, Double> entry2 : data2.entrySet()) {
             if (entry2.getKey().contains(tokensKey[0]) && entry2.getKey().contains(tokensKey[1])) {
               rotCount--;
             }
         }
         
         checkKey = "Swedish " + tokensKey[0] + rotLetter + " " + tokensKey[2] + " " + tokensKey[4] + " " + tokensKey[5];
         data2.put(checkKey, entry.getValue());
         rotCount++;
         
      }

*/