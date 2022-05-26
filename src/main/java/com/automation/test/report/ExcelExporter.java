package com.automation.test.report;

import com.automation.test.annonations.Column;
import com.automation.test.result.TestCaseResult;
import com.automation.test.result.TestStepResult;
import com.automation.test.result.TestSuiteResult;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.log4j.Logger;
import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelExporter implements ITestExporter {
    private Logger logger = Logger.getLogger(this.getClass());

    private final static String HEADER = "header";
    private final static String GROUP = "group";
    private final static String CELL = "cell";
    private final static String PASSED = "passed";
    private final static String FAILED = "failed";

    private static HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setItalic(true);

        // Font Height
        font.setFontHeightInPoints((short) 12);

        // Font Color
        font.setColor(IndexedColors.RED.index);

        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    private static HSSFCellStyle createStyleForHeading(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);

        // Font Height
        font.setFontHeightInPoints((short) 17);

        // Font Color
        font.setColor(IndexedColors.ROSE.index);

        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    private static Map<String, CellStyle> createStyles(Workbook workbook) {
        Map<String, CellStyle> styles = new HashMap<>();
        CellStyle style;
        Font font;

        // Style for title

        // Style for header
        style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.BROWN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);

        styles.put(HEADER, style);

        // Style for group
        style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setVerticalAlignment(VerticalAlignment.TOP);

        font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);

        styles.put(GROUP, style);

        // Style for normal cell
        style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setWrapText(true);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);

        styles.put(CELL, style);

        // Style for passed
        style = workbook.createCellStyle();

        font = workbook.createFont();
        font.setColor(IndexedColors.GREEN.getIndex());
        font.setBold(true);
        style.setFont(font);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);

        styles.put(PASSED, style);

        // Style for failed
        style = workbook.createCellStyle();

        font = workbook.createFont();
        font.setColor(IndexedColors.RED.getIndex());
        font.setBold(true);
        style.setFont(font);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);

        styles.put(FAILED, style);

        return styles;
    }

    public void DisplayEachTestCaseInSummary(Workbook workbook, Sheet summarySheet, TestCaseResult tcResult,
                                             Row rowTestCaseContent, int tcNo) {
        Map<String, CellStyle> styles = createStyles(workbook);
        CreationHelper createHelper = workbook.getCreationHelper();
        CellStyle style = styles.get(CELL);

        if (tcResult.isStatus() == "PASSED") {
            style = styles.get(PASSED);
        }
        else if (tcResult.isStatus() == "FAILED") {
            style = styles.get(FAILED);
        }

        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        Cell contentCellTestCaseNo = rowTestCaseContent.createCell(0);
        contentCellTestCaseNo.setCellStyle(style);
        contentCellTestCaseNo.setCellValue(tcNo);

        Cell contentCellTestCaseName = rowTestCaseContent.createCell(1);
        // Hayden: TMS #21894 - 11/8/2019
        CellStyle styleCaseName = workbook.createCellStyle();
        styleCaseName.cloneStyleFrom(style);
        styleCaseName.setAlignment(HorizontalAlignment.LEFT);
        contentCellTestCaseName.setCellStyle(styleCaseName);
        String CaseName = tcResult.getDescription();
        Pattern p = Pattern.compile("(.+)\\(.+\\)");
        Matcher m = p.matcher(CaseName);
        if (m.matches()) {
            CaseName = m.group(1);
        } // else CaseName is Description
        contentCellTestCaseName.setCellValue(CaseName);

        Cell contentCellTestCaseResult = rowTestCaseContent.createCell(2);
        contentCellTestCaseResult.setCellStyle(style);
        contentCellTestCaseResult.setCellValue(tcResult.isStatus());

        Cell contentCellTestCaseTime = rowTestCaseContent.createCell(3);
        contentCellTestCaseTime.setCellStyle(style);
        contentCellTestCaseTime.setCellValue(DurationFormatUtils.formatDuration(tcResult.getTime(), "HH:mm:ss.SSS"));

        Cell contentCellTestCaseDetail = rowTestCaseContent.createCell(4);
        contentCellTestCaseDetail.setCellStyle(style);
        contentCellTestCaseDetail.setCellValue("go to detail");
        // create a link to detail test case sheet

        Hyperlink linkDetail = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
        String detailSheet = "'" + tcResult.toString() + "'" + "!A1";
        linkDetail.setAddress(detailSheet);
        contentCellTestCaseDetail.setHyperlink((org.apache.poi.ss.usermodel.Hyperlink) linkDetail);

    }

    public void createSummarySheet(Workbook workbook, TestSuiteResult suite) {
        Map<String, CellStyle> styles = createStyles(workbook);
        String summarySheetName = "Summary";
        Sheet summarySheet = workbook.createSheet(summarySheetName);
        int i = 0;
        int testCaseContentStartRow = 8;
        // Test number
        CellStyle headerStyle = styles.get(HEADER);
        CellStyle cellStyle = styles.get(CELL);

        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        if (suite.getBuildNum() != null) {
            Row headerRowBuildNum = summarySheet.createRow(i);

            Cell headerCellBuildNum = headerRowBuildNum.createCell(0);
            headerCellBuildNum.setCellStyle(headerStyle);
            headerCellBuildNum.setCellValue("Build Number");

            Cell headerCellBuildNumValue = headerRowBuildNum.createCell(1);
            headerCellBuildNumValue.setCellStyle(cellStyle);
            headerCellBuildNumValue.setCellValue(suite.getBuildNum());

            i++;
            testCaseContentStartRow++;
        }

        if (suite.getSapInfo() != null) {
            Row headerRowSapInfo = summarySheet.createRow(i);
            Cell headerCellSapInfo = headerRowSapInfo.createCell(0);
            headerCellSapInfo.setCellStyle(headerStyle);
            headerCellSapInfo.setCellValue("SAP Info");

            Cell headerCellSapInfoValue = headerRowSapInfo.createCell(1);
            headerCellSapInfoValue.setCellStyle(cellStyle);
            headerCellSapInfoValue.setCellValue(suite.getSapInfo());

            i++;
            testCaseContentStartRow++;
        }

        Row headerRowReportTime = summarySheet.createRow(i);
        Row headerRowSuiteName = summarySheet.createRow(i + 1);
        Row headerRowPassed = summarySheet.createRow(i + 2);
        Row headerRowFailed = summarySheet.createRow(i + 3);
        Row headerRowSkipped = summarySheet.createRow(i + 4);
        Row headerRowTotal = summarySheet.createRow(i + 5);
        Row headerRowTestCases = summarySheet.createRow(i + 7);
        summarySheet.setColumnWidth(0, 12 * 256);
        summarySheet.setColumnWidth(1, 80 * 256); //Hayden: TMS #21894 - 11/8/2019
        summarySheet.setColumnWidth(2, 10 * 256);
        summarySheet.setColumnWidth(3, 20 * 256);
        summarySheet.setColumnWidth(4, 20 * 256);

        Cell headerCellReportTime = headerRowReportTime.createCell(0);
        headerCellReportTime.setCellStyle(headerStyle);
        headerCellReportTime.setCellValue("Report Time");

        Cell headerCellReportTimeValue = headerRowReportTime.createCell(1);
        headerCellReportTimeValue.setCellStyle(cellStyle);
        headerCellReportTimeValue.setCellValue(suite.getReportTime().toString());

        Cell headerCellSuiteName = headerRowSuiteName.createCell(0);
        headerCellSuiteName.setCellStyle(headerStyle);
        headerCellSuiteName.setCellValue("Suite Name");

        Cell headerCellSuiteNameValue = headerRowSuiteName.createCell(1);
        headerCellSuiteNameValue.setCellStyle(cellStyle);
        headerCellSuiteNameValue.setCellValue(suite.getSuiteName());

        Cell headerCellPass = headerRowPassed.createCell(0);
        headerCellPass.setCellStyle(headerStyle);
        headerCellPass.setCellValue("Passed");
        Cell headerCellPassNo = headerRowPassed.createCell(1);
        headerCellPassNo.setCellStyle(cellStyle);
        headerCellPassNo.setCellValue(suite.getPassNo());

        Cell headerCellFail = headerRowFailed.createCell(0);
        headerCellFail.setCellStyle(headerStyle);
        headerCellFail.setCellValue("Failed");
        Cell headerCellFailNo = headerRowFailed.createCell(1);
        headerCellFailNo.setCellStyle(cellStyle);
        headerCellFailNo.setCellValue(suite.getFailNo());

        Cell headerCellSkip = headerRowSkipped.createCell(0);
        headerCellSkip.setCellStyle(headerStyle);
        headerCellSkip.setCellValue("Skipped");
        Cell headerCellSkipNo = headerRowSkipped.createCell(1);
        headerCellSkipNo.setCellStyle(cellStyle);
        headerCellSkipNo.setCellValue(suite.getSkipNo());

        Cell headerCellTotal = headerRowTotal.createCell(0);
        headerCellTotal.setCellStyle(headerStyle);
        headerCellTotal.setCellValue("Total");
        Cell headerCellTotalNo = headerRowTotal.createCell(1);
        headerCellTotalNo.setCellStyle(cellStyle);
        headerCellTotalNo.setCellValue(suite.getTotal());

        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        Cell headerCellTestCaseNo = headerRowTestCases.createCell(0);
        headerCellTestCaseNo.setCellStyle(headerStyle);
        headerCellTestCaseNo.setCellValue("TC number");

        Cell headerCellTestCaseName = headerRowTestCases.createCell(1);
        headerCellTestCaseName.setCellStyle(headerStyle);
        headerCellTestCaseName.setCellValue("Case name");

        Cell headerCellTestCaseResult = headerRowTestCases.createCell(2);
        headerCellTestCaseResult.setCellStyle(headerStyle);
        headerCellTestCaseResult.setCellValue("Result");

        Cell headerCellTestCaseTime = headerRowTestCases.createCell(3);
        headerCellTestCaseTime.setCellStyle(headerStyle);
        headerCellTestCaseTime.setCellValue("Time(msecs)");

        Cell headerCellTestCaseDetail = headerRowTestCases.createCell(4);
        headerCellTestCaseDetail.setCellStyle(headerStyle);
        headerCellTestCaseDetail.setCellValue("Details");

        List<TestCaseResult> testcaseList = suite.getTestCases();

        int noTestCase = 1;

        for (TestCaseResult tcResult : testcaseList) {
            Row rowTestCaseContent = summarySheet.createRow(testCaseContentStartRow);
            DisplayEachTestCaseInSummary(workbook, summarySheet, tcResult, rowTestCaseContent, noTestCase);

            testCaseContentStartRow++;
            noTestCase++;
        }
    }

    public void DisplayTestStepResult(Workbook workbook, Sheet tcResultSheet, TestCaseResult tcResult) {
        Map<String, CellStyle> styles = createStyles(workbook);
        int rowTestStepStart = 2;
        int testStepNo = 1;

        for (TestStepResult tsResult : tcResult.getSteps()) {
            Row tsValueRow = tcResultSheet.createRow(rowTestStepStart);

            Cell tsNoCell = tsValueRow.createCell(0);
            tsNoCell.setCellStyle(styles.get(CELL));
            tsNoCell.setCellValue(testStepNo);

            Cell tsDescriptionCell = tsValueRow.createCell(1);
            tsDescriptionCell.setCellStyle(styles.get(CELL));
            tsDescriptionCell.setCellValue(tsResult.getDescription());

            Cell tsInputCell = tsValueRow.createCell(2);
            tsInputCell.setCellStyle(styles.get(CELL));
            tsInputCell.setCellValue(tsResult.getInput());

            Cell tsExpectedCell = tsValueRow.createCell(3);
            tsExpectedCell.setCellStyle(styles.get(CELL));

            //John- check length of expected String
            try {
                if (tsResult.getExpected().length() < 2000) {
                    tsExpectedCell.setCellValue(tsResult.getExpected());
                }
                else {
                    tsExpectedCell.setCellValue(tsResult.getVerifiableInstruction());
                    tsExpectedCell.setCellComment(createCellComment(tcResultSheet, workbook, tsExpectedCell, tsResult.getExpected().substring(0, Math.min(tsResult.getExpected().length(), 32000))));
                }
            } catch (NullPointerException e) {
                logger.info("No expected found:");
                logger.info("Set cell value as empty string");
                tsExpectedCell.setCellValue(tsResult.getExpected() == null ? "" : tsResult.getExpected());

            }

            Cell tsResultCell = tsValueRow.createCell(4);
            tsResultCell.setCellValue(tsResult.getResult());
            if (tsResult.getResult() == "PASSED") {
                tsResultCell.setCellStyle(styles.get(PASSED));
            }
            else if (tsResult.getResult() == "FAILED") {
                tsResultCell.setCellStyle(styles.get(FAILED));
            }
            else {
                tsResultCell.setCellStyle(styles.get(CELL));
            }

            Cell tsActual = tsValueRow.createCell(5);
            tsActual.setCellStyle(styles.get(CELL));
            String explanation = tsResult.getExplanation();
            if (explanation == null || explanation.isEmpty()) {
                try {
                    if (tsResult.getActual().length() < 32000) {
                        tsActual.setCellValue(tsResult.getActual());
                    }
                    else {

                        tsActual.setCellComment(createCellComment(tcResultSheet, workbook, tsActual, tsResult.getActual().substring(0, Math.min(tsResult.getActual().length(), 32000))));
                    }
                } catch (NullPointerException e) {
                    logger.info("No actual found:");
//					tsActual.setCellValue(tsResult.getActual()==null?"":tsResult.getActual());

                    if (tsResult.getActual() == null) {
                        logger.info("Set cell value as empty string");
                        tsActual.setCellValue("");
                    }
                    else {
                        tsActual.setCellValue(tsResult.getActual());
                    }
                }

            }
            else {
            	try{
					tsActual.setCellValue(explanation);
					tsActual.setCellComment(createCellComment(tcResultSheet, workbook, tsActual, tsResult.getActual().substring(0, Math.min(tsResult.getActual().length(), 32000))));
				}catch (NullPointerException e){
            		logger.info("Null Exception");
				}

            }

            rowTestStepStart++;
            testStepNo++;
        }
    }

    public void createTestCaseResultSheet(Workbook workbook, TestCaseResult tcResult) {
        Map<String, CellStyle> styles = createStyles(workbook);
        Sheet tcResultSheet = null;
        try {
            tcResultSheet = workbook.createSheet(tcResult.toString());
        } catch (Exception e) {
            logger.error("Exception while creating Sheet:" + e.getMessage());
            tcResultSheet = workbook
                    .createSheet(tcResult.toString() + "Duplicated " + java.time.LocalTime.now().toNanoOfDay());
        }
        CreationHelper createHelper = workbook.getCreationHelper();

        tcResultSheet.setColumnWidth(0, 4 * 256);
        tcResultSheet.setColumnWidth(1, 40 * 256);
        tcResultSheet.setColumnWidth(2, 50 * 256);
        tcResultSheet.setColumnWidth(3, 50 * 256);
        tcResultSheet.setColumnWidth(4, 10 * 256);
        tcResultSheet.setColumnWidth(5, 50 * 256);
        tcResultSheet.setColumnWidth(6, 20 * 256); // Hayden: TMS #21894 - 11/8/2019

        // Test case name
        Row tcNameRow = tcResultSheet.createRow(0);
        tcResultSheet.addMergedRegion(CellRangeAddress.valueOf("A1:F1"));
        Cell headerCell = tcNameRow.createCell(0);
        headerCell.setCellStyle(styles.get(GROUP));
        headerCell.setCellValue(tcResult.getDescription());

        // Hayden: TMS #21894 - 11/8/2019
        // Back to Summary sheet
        Cell headerBackToSummaryCell = tcNameRow.createCell(6);
        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(styles.get(HEADER));
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        headerBackToSummaryCell.setCellStyle(style);
        headerBackToSummaryCell.setCellValue("BACK TO SUMMARY");
        // create a link to Summary sheet
        org.apache.poi.ss.usermodel.Hyperlink linkSummary = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
        String summarySheet = "'Summary'" + "!A1";
        linkSummary.setAddress(summarySheet);
        headerBackToSummaryCell.setHyperlink(linkSummary);

        // Test case header
        Row tcHeaderRow = tcResultSheet.createRow(1);

        Cell headerNoCell = tcHeaderRow.createCell(0);
        headerNoCell.setCellStyle(styles.get(HEADER));
        headerNoCell.setCellValue("No");

        Cell headerDescriptionCell = tcHeaderRow.createCell(1);
        headerDescriptionCell.setCellStyle(styles.get(HEADER));
        headerDescriptionCell.setCellValue("Description");

        Cell headerInputCell = tcHeaderRow.createCell(2);
        headerInputCell.setCellStyle(styles.get(HEADER));
        headerInputCell.setCellValue("Input");

        Cell headerExpectedCell = tcHeaderRow.createCell(3);
        headerExpectedCell.setCellStyle(styles.get(HEADER));
        headerExpectedCell.setCellValue("Expected");

        Cell headerResultCell = tcHeaderRow.createCell(4);
        headerResultCell.setCellStyle(styles.get(HEADER));
        headerResultCell.setCellValue("Result");

        Cell headerActualCell = tcHeaderRow.createCell(5);
        headerActualCell.setCellStyle(styles.get(HEADER));
        headerActualCell.setCellValue("Actual");

        DisplayTestStepResult(workbook, tcResultSheet, tcResult);

    }

    public String createTestResult(TestSuiteResult suite, String filepath) {
        try(Workbook workbook = new XSSFWorkbook()) {
            createSummarySheet(workbook, suite);
            for (TestCaseResult tcResult : suite.getTestCases()) {
                createTestCaseResultSheet(workbook, tcResult);
            }

            File file = new File(filepath);
            file.getParentFile().mkdirs();
            try(FileOutputStream out = new FileOutputStream(file)) {
                workbook.write(out);
                return file.getAbsolutePath();
            }
        } catch (IOException ex) {
            // TODO
            logger.error("Exception while creating excel report: " + ex.getMessage());
            return null;
        }
    }

    public String createTestResult(String testSuite, List<TestStepResult> results, String resultPath)
            throws IOException, IllegalArgumentException, IllegalAccessException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(testSuite);

        int rownum = 0;
        int colnum = 0;
        Cell cell;
        Row row;
        CreationHelper factory = workbook.getCreationHelper();
        Drawing<?> drawing = sheet.createDrawingPatriarch();

        // Heading
        HSSFCellStyle style = createStyleForHeading(workbook);

        row = sheet.createRow(rownum);

        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue(testSuite);
        cell.setCellStyle(style);

        // Title
        rownum++;
        style = createStyleForTitle(workbook);

        row = sheet.createRow(rownum);

        // Fields
        Field[] fields = TestStepResult.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Column col = field.getAnnotation(Column.class);
            if (col != null) {
                sheet.setColumnWidth(colnum, col.width());
            }

            cell = row.createCell(colnum, CellType.STRING);
            cell.setCellValue(field.getName());
            cell.setCellStyle(style);
            colnum++;
        }

        // Data
        colnum = 0;
        for (TestStepResult tc : results) {
            rownum++;
            row = sheet.createRow(rownum);

            fields = tc.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                cell = row.createCell(colnum, field.getType().isPrimitive() ? CellType.NUMERIC : CellType.STRING);
                CellStyle cellStyle = cell.getCellStyle();
                cellStyle.setWrapText(true);

                if (field.getType().isPrimitive())
                    cell.setCellValue((Double) field.get(tc));
                else {
                    Object obj = field.get(tc);
                    cell.setCellValue(obj != null ? obj.toString() : null);
                }

                // When the comment box is visible, have it show in a 1x3 space
                ClientAnchor anchor = factory.createClientAnchor();
                anchor.setCol1(cell.getColumnIndex());
                anchor.setCol2(cell.getColumnIndex() + 1);
                anchor.setRow1(row.getRowNum());
                anchor.setRow2(row.getRowNum() + 3);

                // Create the comment and set the text+author
                Comment comment = drawing.createCellComment(anchor);
                RichTextString str = factory.createRichTextString("Fapfap");
                comment.setString(str);

                // Assign the comment to the cell
                cell.setCellComment(comment);
                colnum++;
            }
            colnum = 0;
        }
        File file = new File(resultPath);
        file.getParentFile().mkdirs();

        try(FileOutputStream outFile = new FileOutputStream(file)) {
            workbook.write(outFile);
        }
        return file.getAbsolutePath();
    }

    private Comment createCellComment(Sheet sheet, Workbook workbook, Cell cell, String comment) {
        if (sheet instanceof XSSFSheet) {
            CreationHelper factory = workbook.getCreationHelper();
            Drawing<?> drawing = sheet.createDrawingPatriarch();

            ClientAnchor anchor = factory.createClientAnchor();
            anchor.setCol1(cell.getColumnIndex());
            anchor.setCol2(cell.getColumnIndex() + 4);
            anchor.setRow1(cell.getRowIndex());
            anchor.setRow2(cell.getRowIndex() + 4);
            Comment cmt = drawing.createCellComment(anchor);
            RichTextString str = factory.createRichTextString(comment);
            cmt.setString(str);
            return cmt;
        }
        return null;
    }
}