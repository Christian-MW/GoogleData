package GoogleData.sheet.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.gson.Gson;

import GoogleData.sheet.config.GoogleAuthorizationConfig;
import GoogleData.sheet.impl.CampaignImpl;
import GoogleData.sheet.model.*;

@Component
public class Utilities {
    @Value("${credentials.file.path}")
    private String credentialsFilePath;
    @Autowired
    private static GoogleAuthorizationConfig googleAuthorizationConfig;
    @Value("${application.name}")
    private String APPLICATIONNAME;
    @Value("${url.add.json.stream}")
    private String URL_ADD_JSON_STREAM;
    @Value("${url.add.stream}")
    private String URL_ADD_STREAM;
    @Value("${url.get.users.stream}")
    private String URL_GET_USERS_STREAM;
    @Value("${url.save.users.campaign}")
    private String URL_USERS_SAVE;
    @Value("${url.scrapper.melt}")
    private String URL_SCRAPPER_MELT;
    private static Logger log = Logger.getLogger(CampaignImpl.class);
    
	public String numToLetter(Integer num) {
		String letter = "";
		HashMap<Integer,String>map=new HashMap<>();
		try {
			
			Integer pos = 0;
			String AlphabetS=" ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			char[] charArrS = AlphabetS.toCharArray();
			String Alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			char[] charArr = Alphabet.toCharArray();
			String Letter = "";
			String Letter2 = "";
			String Letter3 = "";
			
			for (pos = 0; pos <= charArr.length; pos++) {
				Letter = String.valueOf(charArrS[pos]);
				map.put(pos,Letter);
			}
			
			for (Integer i = 0; i < charArr.length; i++) {
				Letter = String.valueOf(charArr[i]);
				for (Integer i2 = 0; i2 < charArr.length; i2++) {
					Letter2 = String.valueOf(charArr[i2]);
					map.put(pos++,Letter+Letter2);
				}
			}
			
			for (Integer i = 0; i < charArr.length; i++) {
				Letter = String.valueOf(charArr[i]);
				for (Integer i2 = 0; i2 < charArr.length; i2++) {
					Letter2 = String.valueOf(charArr[i2]);
					for (Integer i3 = 0; i3 < charArr.length; i3++) {
						Letter3 = String.valueOf(charArr[i3]);
						map.put(pos++,Letter+Letter2+Letter3);
					}
				}
			}
			
		    System.out.print(map.get(num)+" ");
		    letter = map.get(num);
		    return letter;
		} catch (Exception ex) {
			return letter;
		}
	}
	
	
	public Sheets getServiceSheet() {
		try {
			//log.info("#########_Current Working Directory is = " +this.getClass().getClassLoader().getResource("").getPath() + credentialsFilePath);
			
	        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
	        Sheets service = new Sheets.Builder(httpTransport, jsonFactory, 
	        		googleAuthorizationConfig.getCredentialsServiceAccount(httpTransport, jsonFactory, credentialsFilePath))
	        		.setApplicationName(APPLICATIONNAME).build();
	        
	        return service;
		} catch (Exception e) {
			System.out.println("=> ERROR__getServiceSheet__: " + e.getMessage());
			return new Sheets(null, null, null);
		}
	}
	
	public Boolean sendItemStream(CampaignStreamModel req, String type) {
		
		Boolean result = false;
		Gson gson = new Gson();
		try {
			String urlSend ="";
			if (type.equals("json"))
				urlSend = URL_ADD_JSON_STREAM;
			else if (type.equals("search"))
				urlSend = URL_ADD_STREAM;
			
			var request = HttpRequest.newBuilder()
					.uri(URI.create(urlSend))
					.header("Content-type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(req)))
					.build();
			var client = HttpClient.newHttpClient();
			var response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			log.info("##=>Response_____sendItemStream: ");
			log.info(response.statusCode());
			log.info(response.body());
			log.info(response.body().getClass());
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = mapper.readValue(response.body(), Map.class);
			
			log.info("########========> RESPONSE---sendItemStream__" + gson.toJson(map));
			return result;
		} catch (Exception ex) {
			log.error("#####__ERROR AL AGREGAR O ACTUALIZAR EL STREAM##");
			log.error(ex.getMessage());
			return result;
		}
	}
	
	public void sendUsersProcessAverage(List<String> users, String sheetID) {
		log.info("#########################################");
		log.info("#####___sendUsersProcessAverage___#######");
		log.info("#########################################");
		Gson gson = new Gson();
		try {
			ReachScrapperModel req = new ReachScrapperModel();
			req.setAccount(users);
			req.setSpreadsheetId(sheetID);
			System.out.println("req: " + new Gson().toJson(req));
			
			String path = URL_SCRAPPER_MELT + "process/average";
			var request = HttpRequest.newBuilder()
					.uri(URI.create(path))
					.header("Content-type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(req)))
					.build();
			var client = HttpClient.newHttpClient();
			var response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			log.info("##=>Response_____sendUsersProcessAverage: ");
			log.info(response.statusCode());
			log.info(response.body());
			log.info(response.body().getClass());
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = mapper.readValue(response.body(), Map.class);
			log.info("########========> RESPONSE---sendUsersProcessAverage__" + gson.toJson(map));
			
			/*if (response.statusCode() == 200)
				return true;
			else 
				return false;*/
			
			
		} catch (Exception e) {
			log.error("#####__ERROR___sendUsersProcessAverage___#######");
			log.error(e.getMessage());
		}
	}
	
	public boolean saveUsersDB(List<UsersDB> users) {
		Boolean result = false;
		log.info("#####################################");
		log.info("##########__SAVE_USERS__#############");
		try {
			Gson gson = new Gson();
			log.info("USERS: " + new Gson().toJson(users));
			System.out.println("USERS: " + new Gson().toJson(users));
			String path = URL_USERS_SAVE + "account/add";
			var request = HttpRequest.newBuilder()
					.uri(URI.create(path))
					.header("Content-type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(users)))
					.build();
			var client = HttpClient.newHttpClient();
			var response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			log.info("##=>Response_____saveUsersDB: ");
			log.info(response.statusCode());
			log.info(response.body());
			log.info(response.body().getClass());
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = mapper.readValue(response.body(), Map.class);
			log.info("########========> RESPONSE---saveUsersDB__" + gson.toJson(map));
			
			if (response.statusCode() == 200)
				return true;
			else 
				return false;
			
			
		} catch (Exception e) {
			return false;
		}
	}
	
	public void getJSONusers(String campaign) {
		try {
			CampaignStreamModel request = new CampaignStreamModel();
			request.setTheme(campaign);
		    var req = HttpRequest.newBuilder()
					.uri(URI.create(URL_GET_USERS_STREAM))
					.header("Content-type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(request)))			
					.build();
			var client = HttpClient.newHttpClient();
			var response = client.send(req, HttpResponse.BodyHandlers.ofString());
			
			log.info("##=>Response_____sendItemStream: ");
			log.info(response.statusCode());
			log.info(response.body());
			log.info(response.body().getClass());
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = mapper.readValue(response.body(), Map.class);
			
		} catch (Exception ex) {
			log.error("#####__ERROR AL OBTENER LOS USUARIOS DEL STREAM##");
			log.error(ex.getMessage());
		}
	}

	public void createFile(String url, String nameFile, String content) {
		log.info("####Crear archivo de usuarios####");
		try {
			Gson gson = new Gson();
            String ruta = "../" + nameFile + ".json";
            File file = new File(ruta);
            Boolean isnew = false;
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
                isnew = true;
            }
            
            
            String fichero = "";
            
            try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    fichero += linea;
                }
             
            } catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            
            Properties properties = gson.fromJson(fichero, Properties.class);
            System.out.println(properties.get("date_start"));
            
            
            
            
            //Leer archivo 
            FileReader fr = new FileReader (file);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            String conFile = "";
            while((linea=br.readLine())!=null)
            	conFile += linea;	
            
            if(!isnew)
            	conFile += "," + content;
            else
            	conFile = content;
            /*List<Object> itemsJSON = new ArrayList<Object>();
            Object object = new Object();
            if (!conFile.isBlank() || !conFile.isEmpty()) {
            	object = conFile;
            	itemsJSON.add(object);
			}
            Object objectNew = content;
            itemsJSON.add(objectNew);*/
            
            //Almacenar contenido en el archivo
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(new Gson().toJson(conFile));
            bw.close();
			
		} catch (Exception ex) {
			log.info("#####__ERROR AL CREAR EL ARCHIVO");
		}
	}
}
