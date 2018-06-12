package apschool.utility;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.multipart.MultipartFile;

import apschool.conn.DbConn;
import apschool.model.SchoolData;

public class DataUtility {
	private Connection con;
	
	public DataUtility() {
		this.con=DbConn.getConnection();
	}
	private static String UPLOADED_FOLDER = "C:/test/";
	public List<SchoolData> uploadFile(MultipartFile file) {
		List<SchoolData> schoolDatas=new ArrayList<SchoolData>();
		SchoolData sData=null;
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);
			System.out.println("File: "+file.getOriginalFilename());
			File input=new File(UPLOADED_FOLDER+file.getOriginalFilename());
			Document doc = Jsoup.parse(input, "UTF-8");
			Elements distnam=doc.select("h4");
			String[] args=distnam.get(0).toString().split("\\- ");
			String[] disnam=args[1].toString().split("\\ District");
			System.out.println("Dist: "+disnam[0]);
			Elements trs=doc.select("TR");
			System.out.println("Table Size: "+trs.size());
			int sl=0;
			for(int i=0; i<trs.size(); i++) {
				Elements tds=trs.get(i).select("th");
				if(tds.size()==14) {
					sl=1+sl;
					DateFormat df=new SimpleDateFormat("yyy-MM-dd");
					sData=new SchoolData();
					sData.setSlno(sl);
					sData.setDistrict(disnam[0].toString());
					sData.setLocation(tds.get(2).text());
					sData.setHostelName(tds.get(1).text());
					sData.setRegisteredEmp(tds.get(3).text());
					sData.setRegisteredStu(tds.get(4).text());
					sData.setActiveEmp(tds.get(5).text());
					sData.setActiveStu(tds.get(6).text());
					sData.setEmpPresent(tds.get(11).text());
					sData.setStuPresent(tds.get(12).text());
					sData.setDate(df.format(new Date()));
					schoolDatas.add(sData);
					try {
						if(con.isClosed()) {
							con=DbConn.getConnection();
						}
						CallableStatement cb=con.prepareCall("{call UploadBCData (?,?,?,?,?,?,?,?,?,?)}");
						cb.setString(1, disnam[0].toString());
						cb.setString(2, tds.get(2).text());
						cb.setString(3, tds.get(1).text());
						cb.setString(4, tds.get(3).text());
						cb.setString(5, tds.get(4).text());
						cb.setString(6, tds.get(5).text());
						cb.setString(7, tds.get(6).text());
						cb.setString(8, tds.get(11).text());
						cb.setString(9, tds.get(12).text());
						cb.setString(10, tds.get(13).text());
						cb.execute();
						cb.close();
						con.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			return schoolDatas;
		}
		return schoolDatas;
	}


}
