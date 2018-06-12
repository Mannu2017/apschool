package apschool.views;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import apschool.conn.DbConn;



public class ExcelReport extends AbstractXlsView{
	private String fdate,tdate;
	private Connection con;

	public ExcelReport(String fdate, String tdate) {
		this.fdate=fdate;
		this.tdate=tdate;
		this.con=DbConn.getConnection();
	}

	@Override
	 protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
	   HttpServletResponse response) throws Exception {
		DateFormat df=new SimpleDateFormat("yyyyMMddhhmmss");
		response.setHeader("Content-disposition", "attachment; filename=\"Report"+df.format(new Date())+".xls\"");
//		response.setHeader("Content-disposition", "attachment; C:\\pan\"Report"+df.format(new Date())+".xls");
		Sheet sheet = workbook.createSheet("Summary Report");
		Row header = sheet.createRow(0);
		Sheet sheet1 = workbook.createSheet("Hostel Report");
		Row header1 = sheet1.createRow(0);
		Sheet sheet2 = workbook.createSheet("BC Welfare Summary");
		Row header2 = sheet2.createRow(0);
		Sheet sheet3 = workbook.createSheet("BC Welfare Hostel");
		Row header3 = sheet3.createRow(0);
		try {
			if(con.isClosed()) {
				con=DbConn.getConnection();
			}
			ResultSet rs=null;
			CallableStatement ps=con.prepareCall("{call AttnReport (?,?)}");
			ps.setString(1, fdate);
			ps.setString(2, tdate);
			boolean sta=ps.execute();
			int rowe=0;
			while(sta || rowe!=-1) {
				if(sta) {
					rs=ps.getResultSet();
					break;
				} else {
					rowe=ps.getUpdateCount();
				}
				sta=ps.getMoreResults();
			}
			System.out.println("Hello: "+fdate+"^"+tdate);
			ResultSetMetaData meta=rs.getMetaData();
			System.out.println("Data: "+(meta.getColumnCount()-3));
			
			
			int j=0;
			for(int i=0; i<meta.getColumnCount(); i++) {
				j=j+1;
				header.createCell(i).setCellValue(meta.getColumnName(j));
				if(i>2) {
					header.createCell(i).setCellValue(meta.getColumnName(j)+"(Average %)");
				}
			}
			
			int rowNum = 1;
			int ll=0;
			DecimalFormat format = new DecimalFormat();
			format.setMaximumFractionDigits(2);
			float tot=0;
			while(rs.next()) {
				Row row = sheet.createRow(rowNum++);
				for(int i=0; i<meta.getColumnCount(); i++) {
					ll=ll+1;
					tot=rs.getFloat(3);
					row.createCell(i).setCellValue(rs.getString(ll));
					if(i>2) {
						row.createCell(i).setCellValue(rs.getString(ll)+" ("+format.format(rs.getFloat(ll)*100/tot)+")" );
					}
				}
				ll=0;
			}
			rs.close();
			ps.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(con.isClosed()) {
				con=DbConn.getConnection();
			}
			ResultSet rs=null;
			CallableStatement ps=con.prepareCall("{call instrirepo (?,?)}");
			ps.setString(1, fdate);
			ps.setString(2, tdate);
			boolean sta=ps.execute();
			int rowe=0;
			while(sta || rowe!=-1) {
				if(sta) {
					rs=ps.getResultSet();
					break;
				} else {
					rowe=ps.getUpdateCount();
				}
				sta=ps.getMoreResults();
			}
			System.out.println("Hello: "+fdate+"^"+tdate);
			ResultSetMetaData meta=rs.getMetaData();
			int j=0;
			for(int i=0; i<meta.getColumnCount(); i++) {
				j=j+1;
				header1.createCell(i).setCellValue(meta.getColumnName(j));
				if(i>4) {
					header1.createCell(i).setCellValue(meta.getColumnName(j)+"(Average %)");
				}
			}
			
			int rowNum = 1;
			int ll=0;
			float tot=0;
			DecimalFormat format = new DecimalFormat();
			format.setMaximumFractionDigits(2);
			while(rs.next()) {
				Row row = sheet1.createRow(rowNum++);
				for(int i=0; i<meta.getColumnCount(); i++) {
					ll=ll+1;
					row.createCell(i).setCellValue(rs.getString(ll));
					tot=rs.getFloat(5);
					if(i>4) {
						row.createCell(i).setCellValue(rs.getString(ll)+" ("+format.format(rs.getFloat(ll)*100/tot)+"%)");
					}
				}
				ll=0;
			}
			rs.close();
			ps.close();
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(con.isClosed()) {
				con=DbConn.getConnection();
			}
			ResultSet rs=null;
			CallableStatement ps=con.prepareCall("{call BCWelSummary (?,?)}");
			ps.setString(1, fdate);
			ps.setString(2, tdate);
			boolean sta=ps.execute();
			int rowe=0;
			while(sta || rowe!=-1) {
				if(sta) {
					rs=ps.getResultSet();
					break;
				} else {
					rowe=ps.getUpdateCount();
				}
				sta=ps.getMoreResults();
			}
			System.out.println("Hello: "+fdate+"^"+tdate);
			ResultSetMetaData meta=rs.getMetaData();
			int j=0;
			for(int i=0; i<meta.getColumnCount(); i++) {
				j=j+1;
				header2.createCell(i).setCellValue(meta.getColumnName(j));
				if(i>3) {
					header2.createCell(i).setCellValue(meta.getColumnName(j)+"(Average %)");
				}
			}
			
			int rowNum = 1;
			int ll=0;
			float tot=0;
			DecimalFormat format = new DecimalFormat();
			format.setMaximumFractionDigits(2);
			while(rs.next()) {
				Row row = sheet2.createRow(rowNum++);
				for(int i=0; i<meta.getColumnCount(); i++) {
					ll=ll+1;
					row.createCell(i).setCellValue(rs.getString(ll));
					tot=rs.getFloat(4);
					if(i>3) {
						row.createCell(i).setCellValue(rs.getString(ll)+" ("+format.format(rs.getFloat(ll)*100/tot)+"%)");
					}
				}
				ll=0;
			}
			rs.close();
			ps.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(con.isClosed()) {
				con=DbConn.getConnection();
			}
			ResultSet rs=null;
			CallableStatement ps=con.prepareCall("{call BCInsrepo (?,?)}");
			ps.setString(1, fdate);
			ps.setString(2, tdate);
			boolean sta=ps.execute();
			int rowe=0;
			while(sta || rowe!=-1) {
				if(sta) {
					rs=ps.getResultSet();
					break;
				} else {
					rowe=ps.getUpdateCount();
				}
				sta=ps.getMoreResults();
			}
			System.out.println("Hello: "+fdate+"^"+tdate);
			ResultSetMetaData meta=rs.getMetaData();
			int j=0;
			for(int i=0; i<meta.getColumnCount(); i++) {
				j=j+1;
				header3.createCell(i).setCellValue(meta.getColumnName(j));
				if(i>4) {
					header3.createCell(i).setCellValue(meta.getColumnName(j)+"(Average %)");
				}
			}
			
			int rowNum = 1;
			int ll=0;
			float tot=0;
			DecimalFormat format = new DecimalFormat();
			format.setMaximumFractionDigits(2);
			while(rs.next()) {
				Row row = sheet3.createRow(rowNum++);
				for(int i=0; i<meta.getColumnCount(); i++) {
					ll=ll+1;
					row.createCell(i).setCellValue(rs.getString(ll));		
					tot=rs.getFloat(5);
					if(i>4) {
						row.createCell(i).setCellValue(rs.getString(ll)+" ("+format.format(rs.getFloat(ll)*100/tot)+"%)");		
					}
				}
				ll=0;
			}
			rs.close();
			ps.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
