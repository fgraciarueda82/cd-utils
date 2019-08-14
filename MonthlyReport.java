package com.bbva.cdutils.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bbva.cdutils.bean.EmployeeBean;
import com.bbva.cdutils.bean.ImputationBean;
import com.bbva.cdutils.bean.ReportRowBean;

public class MonthlyReport {

	private static final String INPUT_FILE_NAME 	= "/Users/francisco.garcia/Downloads/ReporteCD-EngSpain&Otros_Julio2019.xlsx";
	private static final String OUTPUT_FILE_NAME	= "/Users/francisco.garcia/Reporte_Julio2019EngSpain.xlsx";
		
	private static final List<String> EMPLOYEE_LIST = new ArrayList<>(Arrays.asList("E043402","E052073"));
	
	private static final HashMap<String,String> employeeNames = new HashMap<String,String>();
	
	/**
	 * Función que obtiene el método a invocar de la clase ReportRowBean por reflection
	 * 
	 * @param i posición del campo
	 * @return nombre del método a invocar
	 */
	public static String getMethodName(int i) {
		String methodName;
		switch (i) {
			case 0: methodName = "setEmployeeName";
				break;
			case 1: methodName = "setProject";
				break;
			case 2: methodName = "setBillable";
				break;
			case 3: methodName = "setClient";
				break;
			case 4: methodName = "setDate";
				break;
			case 5: methodName = "setTime";
				break;
			case 6: methodName = "setEmployeeProfile";
				break;
			case 7: methodName = "setState";
				break;
			case 8: methodName = "setFamily";
				break;
			case 9: methodName = "setDeliveryManagerName";
				break;
			case 10: methodName = "setDeliveryCoordinatorName";
				break;
			default:
				methodName = "";
				break;
		}
		return methodName;
	}
	
	/**
	 * Función que procesa el contenido del fichero de entrada con los registros de imputación
	 * 
	 * @param filename path del fichero con los registros de imputacion sin procesar
	 * @return lista de registros procesados
	 * @throws Exception
	 */
	public static ArrayList<ReportRowBean> readRegisters(String filename) throws Exception {
		ArrayList<ReportRowBean> registers = new ArrayList<ReportRowBean>();
		ReportRowBean currRegister = new ReportRowBean();
		String metodo = new String();
		Method m;
		int i = 0;
		FileInputStream excelFile = new FileInputStream(new File(filename));
		@SuppressWarnings("resource") Sheet datatypeSheet = new XSSFWorkbook(excelFile).getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();
        if(iterator.hasNext()) iterator.next();
        while (iterator.hasNext()) {
        	currRegister = new ReportRowBean();
            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();
            i=0;
            while (cellIterator.hasNext()) {
                Cell currentCell = cellIterator.next();
                metodo = getMethodName(i);
                try {
                	if (currentCell.getCellType() == CellType.STRING) {
                		m = currRegister.getClass().getMethod(metodo, String.class);
                        m.invoke(currRegister, currentCell.getStringCellValue());
                    } else {
                    	if (currentCell.getCellType() == CellType.BLANK) {
                    		m = currRegister.getClass().getMethod(metodo, String.class);
                            m.invoke(currRegister, currentCell.getStringCellValue());
                    	} else {
                        	m = currRegister.getClass().getMethod(metodo, Date.class);
                        	m.invoke(currRegister, currentCell.getDateCellValue());                  		
                        }
                    }
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
                i++;
            }
            registers.add(currRegister);
            employeeNames.put(currRegister.getEmployeeId(), currRegister.getEmployeeName());
        }      
		return registers;
	}
	
	/**
	 * Función que prepara un mapa para contener las imputaciones de los empleados a procesar 
	 * 
	 * @return mapa con el listado de empleados a procesar
	 */
	public static HashMap<String,EmployeeBean> initializeEmployeeMap() {
		HashMap<String,EmployeeBean> employeeMap = new HashMap<String,EmployeeBean>();
		for (String employeeId : EMPLOYEE_LIST) {
			employeeMap.put(employeeId, new EmployeeBean(employeeId, employeeNames.get(employeeId), null));
		}
		return employeeMap;
	}
	
	/**
	 * Función que procesa los registros de imputación de los usuarios seleccionados
	 * 
	 * @param registers listado de registros de imputación de todos los empleados
	 * @return mapa con el listado de usuarios seleccionado junto a sus registros de imputación procesados
	 */
	public static HashMap<String,EmployeeBean> getSelectedEmployees(ArrayList<ReportRowBean> registers) {
		HashMap<String,EmployeeBean> employeeMap = initializeEmployeeMap();
		EmployeeBean currEmployee;
		ImputationBean imputationRecord;
		for (ReportRowBean currReport : registers) {
			if(employeeMap.containsKey(currReport.getEmployeeId())) {
				imputationRecord = new ImputationBean(currReport.getProject(), currReport.isBillable(), currReport.getTime(), currReport.getState(), currReport.getDate());
				currEmployee = employeeMap.get(currReport.getEmployeeId());
				currEmployee.addImputationRecord(imputationRecord);
				employeeMap.put(currReport.getEmployeeId(), currEmployee);
			}
		}
		return employeeMap;
	}
	
	/**
	 * Procedimiento que genera el fichero de salida con las imputaciones procesadas y ordenadas
	 * 
	 * @param employees mapa de usuarios con sus imputaciones a exportar
	 */
	public static void generateRecap(HashMap<String,EmployeeBean> employees) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet;
		List<ImputationBean> imputations;
		Cell cell;
		Row row;
		int rowNum, colNum;
		List<String> columnNames = new ArrayList<String>(Arrays.asList("ID", "Name", "Project", "Date", "Time", "State", "Billable"));
		for (Map.Entry<String,EmployeeBean> entry : employees.entrySet()) {
			System.out.println("Generando pestaña para el empleado " + entry.getKey() + ": " + entry.getValue().getName());
			sheet = workbook.createSheet(entry.getValue().getId() + " - " + entry.getValue().getName());
			imputations = entry.getValue().getImputationRecords();
			rowNum = 0;
			colNum = 0;
			row = sheet.createRow(rowNum++);
			for (String currColumn : columnNames) {
				cell = row.createCell(colNum++);
                cell.setCellValue((String) currColumn);
			}			
			for (ImputationBean currImputation : imputations) {
				colNum = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(colNum++);
                cell.setCellValue((String) entry.getValue().getId());
                cell = row.createCell(colNum++);
                cell.setCellValue((String) entry.getValue().getName());
                cell = row.createCell(colNum++);
                cell.setCellValue((String) currImputation.getProjectName());
                cell = row.createCell(colNum++);
                cell.setCellValue((String) dateFormat.format(currImputation.getDate()));
                cell = row.createCell(colNum++);
                cell.setCellValue((String) currImputation.getTime());
                cell = row.createCell(colNum++);
                cell.setCellValue((String) currImputation.getState());
                cell = row.createCell(colNum++);
                cell.setCellValue((String) Boolean.toString(currImputation.isBillable()));
			}
			for(int i=0;i<columnNames.size();i++) {
				sheet.autoSizeColumn(i);
			}
		}
        try {
            FileOutputStream outputStream = new FileOutputStream(OUTPUT_FILE_NAME);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Fichero de reporte completado");
	}
	
	/**
	 * Método principal de la aplicación
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Inicio");
		HashMap<String,EmployeeBean> employees;
		ArrayList<ReportRowBean> registers = new ArrayList<ReportRowBean>();
		try {
			registers = readRegisters(INPUT_FILE_NAME);
			employees = getSelectedEmployees(registers);
			System.out.println(employees.size());
			generateRecap(employees);
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
    		System.out.println("Leídos " + registers.size() + " registros.");
		}
		System.out.println("Fin");
	}
}