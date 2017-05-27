package Client;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;


public class Report {

    public Report() {
//        String sourceFileName = "C:\\Users\\mdovey\\Documents\\Incognito\\src\\Client\\report.xml";
//        try {
//
//            JasperDesignViewer.viewReportDesign(JasperCompileManager.compileReportToFile(sourceFileName));
//            JasperDesign jasperDesign = JasperCompileManager.
//            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
//            // Second, create a map of parameters to pass to the report.
//            Map parameters = new HashMap();
//            parameters.put("ReportTitle", "Basic JasperReport");
//            parameters.put("MaxSalary", new Double(25000.00));
//            // Third, get a database connection
//            //Connection conn = Database.getConnection();
//            // Fourth, create JasperPrint using fillReport() method
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters/*, conn*/);
//            // You can use JasperPrint to create PDF
//            JasperExportManager.exportReportToPdf(jasperPrint);
//            // Or to view report in the JasperViewer
//            JasperViewer.viewReport(jasperPrint);
//        } catch (Exception e) {
//
//        }
    }

//    public void test() {
//
//        Map parameters = new HashMap();
//        parameters.put("ReportTitle", "Basic JasperReport");
//        parameters.put("MaxSalary", new Double(25000.00));
//
//        try {
//            InputStream input = new FileInputStream(new File("C:\\Users\\mdovey\\Documents\\Incognito\\src\\Client\\template.xml"));
//            JasperDesign design = JRXmlLoader.load(input);
//
//            Connection connection = DriverManager.getConnection("jdbc:sqlite:results.db");
//
//            JasperReport report = JasperCompileManager.compileReport(design);
//            JasperPrint print = JasperFillManager.fillReport(report, parameters, connection);
//
//            OutputStream output = new FileOutputStream(new File("C:\\Users\\mdovey\\Documents\\Incognito\\src\\Client\\reportTest.pdf"));
//            JasperExportManager.exportReportToPdfStream(print, output);
//
//            JasperViewer.viewReport(print);
//        } catch (Exception e) {
//
//        }
//    }
//
//    public static void main(String[] args) {
//        Report report = new Report();
//        report.test();
//    }
}