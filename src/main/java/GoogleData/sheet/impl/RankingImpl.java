package GoogleData.sheet.impl;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.gson.Gson;

import GoogleData.sheet.dto.request.*;
import GoogleData.sheet.dto.response.*;
import GoogleData.sheet.model.*;
import GoogleData.sheet.service.RankingService;
import GoogleData.sheet.utils.Utilities;

@Service("RankingImpl")
public class RankingImpl implements RankingService{
	private static Logger log = Logger.getLogger(CampaignImpl.class);
    @Value("${file.range.ranking}")
    private String SHEET_COLUMN_RANKING;
    @Value("${file.columns.ranking}")
    private String FILE_COLUMNS_RANKING;
    @Value("${file.columns.ranking2}")
    private String FILE_COLUMNS_RANKING2;
    @Value("${file.ranking.perception}")
    private String RANKING_PERCEPTION;
    @Value("${url.save.users.campaign}")
    private String URL_APOLLO;
    @Autowired
    Utilities utilities;
    @Autowired
    CampaignImpl campaignImpl;
    @Autowired
    GoogleImpl googleImpl;
	
    public AddCampaignResponse addRanking(AddCampaignRequest request) {
    	AddCampaignResponse result = new AddCampaignResponse();
    	log.info("#####################################################");
    	log.info("##################____addRanking____#################");
    	
    	try {
    		AccountsRankingModel ObjAccounts = new AccountsRankingModel();
    		String prompt = "";
    		Sheets service = utilities.getServiceSheet();
	        //##########GetData Sheet
			SheetResponse restGet = new SheetResponse();
			SheetResponse restGetRaw = new SheetResponse();
			SheetResponse restGetRawSheet2 = new SheetResponse();
			String[] sheetsFile = SHEET_COLUMN_RANKING.split(",");
			
	        restGet = campaignImpl.googleImpl.getDataSheetByFilter("COLUMNS", request.getSpreadsheet_id(), sheetsFile[0].toString().toLowerCase());
	        restGetRaw = campaignImpl.googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), sheetsFile[0].toString().toLowerCase());
	        
	        //Obtener las hojas del archivo
	        GetListSheetsResponse sheets = googleImpl.getSheetsBySheet(request.getSpreadsheet_id());
	        restGetRawSheet2 = campaignImpl.googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), sheetsFile[1].toString().toLowerCase());
	        if (restGetRawSheet2.getCode() == 500) {
	        	restGetRawSheet2 = campaignImpl.googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), "líneas discursivas");
			}
	        
	        
    		String[] headersRanking = FILE_COLUMNS_RANKING.split(",");
    		String[] headersFile = restGetRaw.getObjectResult().get(0).toString().split(",");
    		String[] headersRanking2 = FILE_COLUMNS_RANKING2.split(",");
    		String[] headersFile2 = restGetRawSheet2.getObjectResult().get(0).toString().split(",");
    		List<UsersRankingModel> usersRanking = new ArrayList<UsersRankingModel>();
    		String[] valuesPerception = RANKING_PERCEPTION.split(",");
    		
    		/*###########################################
    		 * ########__OBTENIENDO COLUMNAS___##########
    		 ###########################################*/
    		Integer PosUser=0,PosRank=0,PosDesc=0,PosVis=0,PosEng=0,PosFac=0,PosRan=0;
    		Integer PosUserL=0,PosTittle=0,PosLine=0,PosType=0,PosLineP=0,PosLineN=0;
    		
    		//##OBTENIENDO POSICIÓN DE LOS ENCABEZADOS DE LA PRIMER HOJA
    		for (String column : headersRanking) {
    			Integer posColumnFile = 0;
    			column = column.trim().toLowerCase();
    			for (String columnFile: headersFile) {
    				posColumnFile++;
        			columnFile = columnFile.replace("[", "").replace("]", "").replace(" ", "").trim().toLowerCase();
        			String cadenaNormalize = Normalizer.normalize(columnFile, Normalizer.Form.NFD);   
        			columnFile = cadenaNormalize.replaceAll("[^\\p{ASCII}]", "");
        			if (columnFile.equals(column)) {
						System.out.println(column + ", Fund-- Position: " + posColumnFile);
						
						if (column.equals(headersRanking[0].trim().toLowerCase())) 
							PosUser = posColumnFile -1;
						if (column.equals(headersRanking[1].trim().toLowerCase())) 
							PosRank = posColumnFile -1;
						if (column.equals(headersRanking[2].trim().toLowerCase())) 
							PosDesc = posColumnFile -1;
						if (column.equals(headersRanking[3].trim().toLowerCase())) 
							PosVis = posColumnFile -1;
						if (column.equals(headersRanking[4].trim().toLowerCase())) 
							PosEng = posColumnFile -1;
						if (column.equals(headersRanking[5].trim().toLowerCase())) 
							PosFac = posColumnFile -1;
						if (column.equals(headersRanking[6].trim().toLowerCase())) 
							PosRan = posColumnFile -1;
						if (column.equals(headersRanking[7].trim().toLowerCase())) 
							PosLineP = posColumnFile -1;
						if (column.equals(headersRanking[8].trim().toLowerCase())) 
							PosLineN = posColumnFile -1;
					}
    			}
    		}
    		
    		//##OBTENIENDO POSICIÓN DE LOS ENCABEZADOS DE LA SEGUNDA HOJA
    		for (String column : headersRanking2) {
    			Integer posColumnFile = 0;
    			column = column.trim().toLowerCase();
    			for (String columnFile: headersFile2) {
    				posColumnFile++;
        			columnFile = columnFile.replace("[", "").replace("]", "").replace(" ", "").trim().toLowerCase();
        			String cadenaNormalize = Normalizer.normalize(columnFile, Normalizer.Form.NFD);   
        			columnFile = cadenaNormalize.replaceAll("[^\\p{ASCII}]", "");
        			if (columnFile.equals(column)) {
						System.out.println(column + ", Fund-- Position: " + posColumnFile);
						
						if (column.equals(headersRanking2[0].trim().toLowerCase())) 
							PosUserL = posColumnFile -1;
						if (column.equals(headersRanking2[1].trim().toLowerCase())) 
							PosTittle = posColumnFile -1;
						if (column.equals(headersRanking2[2].trim().toLowerCase())) 
							PosLine = posColumnFile -1;
						if (column.equals(headersRanking2[3].trim().toLowerCase())) 
							PosType = posColumnFile -1;
						
					}
    			}
    		}
    		
    		
    		/*######__OBTENER DATOS DE LA PRIMER HOJA__######*/
    		List<Object> columnsHeaders =  new ArrayList<Object>();
    		columnsHeaders = restGetRaw.getObjectResult().get(0);
    		for (Integer i = 1; i < restGetRaw.getObjectResult().size(); i++) {
    			List<Object> ObjUser = restGetRaw.getObjectResult().get(i);
    			//List<DiscursiveLinesModel> lines = new ArrayList<DiscursiveLinesModel>();
    			
				UsersRankingModel item = new UsersRankingModel();
				item.setUser(ObjUser.get(PosUser).toString());
				item.setRanking(ObjUser.get(PosRank).toString());
				item.setDescription(ObjUser.get(PosDesc).toString());
				prompt = ObjUser.get(PosLineP).toString() + "|" + ObjUser.get(PosLineN).toString();
				//item.setPrompt(ObjUser.get(PosLineP).toString() + "|" + ObjUser.get(PosLineN).toString());
				
				try {
					item.setAverageViews(Integer.parseInt(ObjUser.get(PosVis).toString()));
				} catch (Exception e) {
					item.setAverageViews(null);
				}
				try {
					item.setAverageEngagement(Integer.parseInt(ObjUser.get(PosEng).toString()));
				} catch (Exception ex) {
					item.setAverageEngagement(null);
				}
				try {
					item.setImpactFactor(Integer.parseInt(ObjUser.get(PosFac).toString()));
				} catch (Exception ex) {
					item.setImpactFactor(null);
				}
				try {
					item.setRankings(ObjUser.get(PosRan).toString());
				} catch (Exception ex) {
					item.setRankings(null);
				}
				
				System.out.println("");
	    		String[] listUsersFile = restGet.getObjectResult().get(PosUser).toString().split(",");
	    		List<Object> objUsersFile = restGet.getObjectResult().get(PosUser);			
				/*######__OBTENER LÍNEAS DISCURSIVAS DE LA SEGUNDA HOJA__######*/
	    		
	    		
	    		//for (Object usersheet:objUsersFile) {
	    		
	    		//LÍNEAS DISCURSIVAS EN LA HOJA LLAMADA "LÍNEAS DISCURSIVAS"
	    		/*List<DiscursiveLinesModel> lines = new ArrayList<DiscursiveLinesModel>();
	    			for (Integer g = 1; g < restGetRawSheet2.getObjectResult().size(); g++) {
						List<Object> ObjLine = restGetRawSheet2.getObjectResult().get(g);
						if (ObjLine.get(0).toString().equals(ObjUser.get(PosUser).toString())) {
							System.out.println("La cuenta: " + ObjLine.get(0).toString() + ", tiene líneas discursivas");
							DiscursiveLinesModel line = new DiscursiveLinesModel();
							line.setTitle(ObjLine.get(PosTittle).toString());
							line.setType(ObjLine.get(PosType).toString());
							line.setValue(ObjLine.get(PosLine).toString());
							lines.add(line);
						}
	    			}
	    			item.setDiscursiveLines(lines);*/
	    		//}
				
				//LÍNEAS DISCURSIVAS EN LA MISMA HOJA
				/*for(Integer j = 3; j < columnsHeaders.size(); j++) {
					System.out.println(columnsHeaders.get(j));
        			String cadenaNormalize = Normalizer.normalize(columnsHeaders.get(j).toString().toLowerCase(), Normalizer.Form.NFD);   
        			String itemHead = cadenaNormalize.replaceAll("[^\\p{ASCII}]", "");
					if (itemHead.startsWith("titulo") && itemHead.contains(valuesPerception[0].toString().toLowerCase())) {
						System.out.println( itemHead + "___A FAVOR");
						DiscursiveLinesModel line = new DiscursiveLinesModel();
						line.setTitle(ObjUser.get(j).toString());
						line.setValue(ObjUser.get(j + 1).toString());
						line.setType("positive");
						//lines.add(line);
					}
					if (itemHead.startsWith("titulo") && itemHead.contains(valuesPerception[1].toString().toLowerCase())) {
						System.out.println( itemHead + "___EN CONTRA");
						DiscursiveLinesModel line = new DiscursiveLinesModel();
						line.setTitle(ObjUser.get(j).toString());
						line.setValue(ObjUser.get(j + 1).toString());
						line.setType("negative");
						//lines.add(line);
					}
				}
				item.setDiscursiveLines(lines);*/
				usersRanking.add(item);
			}
    		ObjAccounts.setAccounts(usersRanking);
    		ObjAccounts.setPrompt(prompt);
    		System.out.println( new Gson().toJson(ObjAccounts));
    		utilities.saveUsersRankBD(ObjAccounts);
    		log.info("");
			
    		return result;
		} catch (Exception e) {
			log.error("###################################");
			log.error("######__ERROR__ADD-RANKING__#######");
			log.error(e.getMessage());
			result.setMessage(e.getMessage());
			result.setCode(500);
			return result;
		}
    }
}
