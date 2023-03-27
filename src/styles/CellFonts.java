package styles;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CellFonts {

    /**
     * Header Font
     */
    private Font headerFont;

    /**
     * Hour Font
     */
    private Font hourFont;

    /**
     * Bolded Hour Font
     */
    private Font boldHourFont;

    public CellFonts(XSSFWorkbook workbook) {

        // Create Header Font
        this.headerFont = workbook.createFont();
        this.headerFont.setFontHeightInPoints((short) 10);
        this.headerFont.setFontName("Open Sans");
        this.headerFont.setColor(IndexedColors.WHITE.getIndex());
        this.headerFont.setBold(true);

        // Create Hours Font
        this.hourFont = workbook.createFont();
        this.hourFont.setFontHeightInPoints((short) 10);
        this.hourFont.setFontName("Open Sans");

        // Create Bolded Hours Font
        this.boldHourFont = workbook.createFont();
        this.boldHourFont.setFontHeightInPoints((short) 10);
        this.boldHourFont.setFontName("Open Sans");
        this.boldHourFont.setBold(true);
    }

    /**
     * @return the header font
     */
    public Font getHeaderFont() {
        return this.headerFont;
    }

    /**
     * the hour font
     */
    public Font getHourFont() {
        return this.hourFont;
    }

    /**
     * the bolded hour font
     */
    public Font getBoldHourFont() {
        return this.boldHourFont;
    }
}