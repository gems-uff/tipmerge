/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Catarina
 */
 
public class Export {
 
    public static void toExcel(Map<String,TableModel> sheets){
				
		File file = chooseFile();
		if (file != null){
				
			try {

				Workbook wb = new HSSFWorkbook();

				//Sheet sheetName = wb.createSheet(sheet);
				Set<String> sheetNames = sheets.keySet();
				for (String sheetName : sheetNames)
					fillSheet((DefaultTableModel)sheets.get(sheetName), wb.createSheet(sheetName));

				String path = file.toString().concat(".xls");
				FileOutputStream out = new FileOutputStream(path);
				wb.write(out);
				out.close();
			} catch (FileNotFoundException ex) {
				System.out.println(ex.getMessage());
				Logger.getLogger(ex.getMessage());
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
				Logger.getLogger(ex.getMessage());
			}
	   
		}
	}

	private static void fillSheet(DefaultTableModel dtm, Sheet sheet) {
		Row row = null;
		Cell cell = null;
		row = sheet.createRow(0);
		for (int j=0;j<dtm.getColumnCount();j++) {
			cell = row.createCell(j);
			cell.setCellValue( dtm.getColumnName(j) );
		}
		for (int i=0;i<dtm.getRowCount();i++) {
			row = sheet.createRow(i+1);
			for (int j=0;j<dtm.getColumnCount();j++) {
				cell = row.createCell(j);
				cell.setCellValue( String.valueOf( dtm.getValueAt(i, j) ) );
			}
		}
	}
  //choosing a directory
	private static File chooseFile() {
		//Exporting to Excel
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Workbook (*.xls)", "xls");
		chooser.setFileFilter(filter);
		chooser.setDialogTitle("Export to Excel");
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
			return chooser.getSelectedFile();
		}
		return null;
	}
}

 
     
