package styles;

import java.awt.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.ss.usermodel.ExtendedColor;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.BorderStyle;  


public class CellStyles {
    
    /**
     * Header Style
     */
    private XSSFCellStyle headerStyle;

    /**
     * Cell Style for the first row
     */
    private XSSFCellStyle firstHourStyle;

    /**
     * Cell style for the rows in the middle
     */
    private XSSFCellStyle hourStyle;

    /**
     * Cell style for the alternating rows in the middle
     */
    private XSSFCellStyle hourStyle2;

    /**
     * Cell style for the last row
     */
    private XSSFCellStyle lastHourStyle;

    /**
     * Cell style for the total row
     */
    private XSSFCellStyle totalStyle;

    public CellStyles() {


        // Header Style
        this.headerStyle = workbook.createCellStyle();
        this.headerStyle.setFillForegroundColor(headerBlue);
        this.headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.headerStyle.setFont(headerFont);
        this.headerStyle.setBorderBottom(BorderStyle.THIN);
        this.headerStyle.setBottomBorderColor(IndexedColors.WHITE.getIndex());
        this.headerStyle.setBorderRight(BorderStyle.THIN);
        this.headerStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());
        this.headerStyle.setBorderTop(BorderStyle.THIN);
        this.headerStyle.setTopBorderColor(IndexedColors.WHITE.getIndex());

        // Cell Style for the first row
        this.firstHourStyle = workbook.createCellStyle();
        this.firstHourStyle.setFillForegroundColor(highlightGreen);
        this.firstHourStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.firstHourStyle.setFont(boldHourFont);
        this.firstHourStyle.setAlignment(HorizontalAlignment.CENTER);
        this.irstHourStyle.setBorderBottom(BorderStyle.THIN);
        this.firstHourStyle.setBottomBorderColor(IndexedColors.WHITE.getIndex());
        this.firstHourStyle.setBorderRight(BorderStyle.THIN);
        this.firstHourStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());
        this.firstHourStyle.setBorderTop(BorderStyle.THIN);
        this.firstHourStyle.setTopBorderColor(IndexedColors.WHITE.getIndex());
        
        // Cell style for the rows in the middle
        this.hourStyle = workbook.createCellStyle();
        this.hourStyle.setFillForegroundColor(lightestBlue);
        this.hourStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.hourStyle.setFont(hourFont);
        this.hourStyle.setAlignment(HorizontalAlignment.CENTER);
        this.hourStyle.setBorderBottom(BorderStyle.THIN);
        this.hourStyle.setBottomBorderColor(IndexedColors.WHITE.getIndex());
        this.hourStyle.setBorderRight(BorderStyle.THIN);
        this.hourStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());
        this.hourStyle.setBorderTop(BorderStyle.THIN);
        this.hourStyle.setTopBorderColor(IndexedColors.WHITE.getIndex());
        
        // Cell style for every other row in the middle
        this.hourStyle2 = workbook.createCellStyle();
        this.hourStyle2.setFillForegroundColor(lighterBlue);
        this.hourStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.hourStyle2.setFont(hourFont);
        this.hourStyle2.setAlignment(HorizontalAlignment.CENTER);
        this.hourStyle2.setBorderBottom(BorderStyle.THIN);
        this.hourStyle2.setBottomBorderColor(IndexedColors.WHITE.getIndex());
        this.hourStyle2.setBorderRight(BorderStyle.THIN);
        this.hourStyle2.setRightBorderColor(IndexedColors.WHITE.getIndex());
        this.hourStyle2.setBorderTop(BorderStyle.THIN);
        this.hourStyle2.setTopBorderColor(IndexedColors.WHITE.getIndex());

        // Cell style for the last hours row
        this.lastHourStyle = workbook.createCellStyle();
        this.lastHourStyle.setFillForegroundColor(highlightYellow);
        this.lastHourStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.lastHourStyle.setFont(hourFont);
        this.lastHourStyle.setAlignment(HorizontalAlignment.CENTER);
        this.lastHourStyle.setBorderBottom(BorderStyle.THIN);
        this.lastHourStyle.setBottomBorderColor(IndexedColors.WHITE.getIndex());
        this.lastHourStyle.setBorderRight(BorderStyle.THIN);
        this.lastHourStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());
        this.lastHourStyle.setBorderTop(BorderStyle.THIN);
        this.lastHourStyle.setTopBorderColor(IndexedColors.WHITE.getIndex());

        // Cell Style for the final row
        this.totalStyle = workbook.createCellStyle();
        this.totalStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
        this.totalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.totalStyle.setFont(headerFont);
        this.totalStyle.setAlignment(HorizontalAlignment.CENTER);
        this.totalStyle.setAlignment(HorizontalAlignment.CENTER);
        this.totalStyle.setBorderBottom(BorderStyle.THIN);
        this.totalStyle.setBottomBorderColor(IndexedColors.WHITE.getIndex());
        this.totalStyle.setBorderRight(BorderStyle.THIN);
        this.totalStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());
        this.totalStyle.setBorderTop(BorderStyle.THIN);
        this.totalStyle.setTopBorderColor(IndexedColors.WHITE.getIndex());
    }

    /**
     * @return the headerStyle
     */
    public XSSFCellStyle getHeaderStyle() {
        return this.headerStyle;
    }

    /**
     * @return the firstHourStyle
     */
    public XSSFCellStyle getFirstHourStyle() {
        return this.firstHourStyle;
    }

    /**
     * @return the hourStyle
     */
    public XSSFCellStyle getHourStyle() {
        return this.hourStyle;
    }

    /**
     * @return the hourStyle2
     */
    public XSSFCellStyle getHourStyle2() {
        return this.hourStyle2;
    }

    /**
     * @return the lastHourStyle
     */
    public XSSFCellStyle getLastHourStyle() {
        return this.lastHourStyle;
    }

    /**
     * @return the totalStyle
     */
    public XSSFCellStyle getTotalStyle() {
        return this.totalStyle;
    }

}
