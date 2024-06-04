package GoogleData.sheet.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.common.base.CharMatcher;

import GoogleData.sheet.dto.request.AddDataTikTokRequest;
import GoogleData.sheet.dto.request.AddImgSlideRequest;
import GoogleData.sheet.dto.request.MeditionFSV2Request;
import GoogleData.sheet.dto.request.SearchFileV2Request;
import GoogleData.sheet.dto.request.SheetRequest;
import GoogleData.sheet.dto.request.SlideRequest;
import GoogleData.sheet.dto.response.GetListSheetsResponse;
import GoogleData.sheet.dto.response.MeditionFSResponse;
import GoogleData.sheet.dto.response.ResponseBase;
import GoogleData.sheet.dto.response.SheetResponse;
import GoogleData.sheet.dto.response.SlideResponse;
import GoogleData.sheet.model.DataAlcanceModel;
import GoogleData.sheet.model.ObjDownV2;
import GoogleData.sheet.model.ObjMelt;
import GoogleData.sheet.model.ObjMeltSearch;
import GoogleData.sheet.model.SearchV2DW;
import GoogleData.sheet.model.SearchV2Model;
import GoogleData.sheet.service.GoogleRestV2Service;
import GoogleData.sheet.utils.Utilities;

@Service("GoogleImplV2")
public class GoogleImplV2 implements GoogleRestV2Service {
	private final Log log = LogFactory.getLog(getClass());
	@Value("${file.columns.mfv2}")
	private String sheetsNames;
	@Value("${file.columns.ALmfv2}")
	private String sheetsNamesAlcance;
	@Value("${file.namesheet.meditionV2}")
	private String nameSheetMeditionV2;
	@Value("${file.headers.meditionV2}")
	private String headersMentionV2;
	@Value("${file.namesheet.tiktok}")
	private String nameSheetTiktok;
	@Value("${file.headers.tiktok}")
	private String headersTiktok;
	@Value("${file.headers.tiktok.comments}")
	private String headersCommentsTiktok;
	@Value("${file.headers.tiktok.search}")
	private String headersSearchTiktok;
	@Value("${file.namesheet.tiktokcomments}")
	private String nameSheetTiktokC;
	@Value("${file.namesheet.tiktok.commentsnew}")
	private String nameSheetTiktoknew;
	@Autowired
	GoogleImpl googleImpl;
    @Autowired
    GoogleSlideImpl googleSlideImpl;
    @Autowired
    Utilities utilities;

	@Override
	public MeditionFSResponse MeditionFileAndSlides(MeditionFSV2Request request) {
		log.info("#########___MeditionFileAndSlides___##########");
		MeditionFSResponse result = new MeditionFSResponse();
		try {
			Sheets service = googleImpl.getServiceSheet();
			String[] sheetsProperties = sheetsNames.toString().split(",");
			// Obtenemos las hojas existentes del Spreadsheet enviado
			SheetRequest reqListSheets = new SheetRequest();
			reqListSheets.setSpreadsheet_id(request.getSpreadsheet_id());
			GetListSheetsResponse resListSheets = googleImpl.getElementsListSpreadsheet(reqListSheets);
			ObjMelt itemOBJ;
			if (request.getObjectResult().size() > 0)
				itemOBJ = request.getObjectResult().get(0);
			else {
				result.setCode(401);
				result.setMessage("El objeto objectResult se encuentra vacío");
				return result;
			}
			String DateStart="";
			String DateEnd="";
			Integer RangeCount = 0;
			String Search = itemOBJ.getSearch();
			List<Object> authors = new ArrayList<Object>();
			//Obteniendo el ID de la presentación para insertar las imágenes
			String slideID = "2";
			Integer sizeUsers = 8;
			SheetResponse restGetRawD = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), "Automatización");
			for (List<Object> itemAut : restGetRawD.getObjectResult()) {
				try {
					if (itemAut.get(0).toString().toLowerCase().equals("{{numero.diapositiva}}")) 
						slideID = itemAut.get(1).toString();
				} catch (Exception e) {
					slideID = "2";
					break;
				}
				try {
					if (itemAut.get(0).toString().toLowerCase().equals("{{numero.stakeholders}}")) 
						sizeUsers = Integer.valueOf(itemAut.get(1).toString());
				} catch (Exception ex) {
					sizeUsers = 10;
					break;
				}
			}
			//Obteniendo el listado de Stakeholders(Autores)
			if(itemOBJ.getAuthors().size() > 0) {
    			authors = itemOBJ.getAuthors().stream().limit(sizeUsers).collect(Collectors.toList());
			}
			for (String sheetP : sheetsProperties) {
				Boolean isNew = false;
				Boolean existsheetProd = false;
				if (sheetP.trim().toLowerCase().equals("automatizacion"))
					sheetP = "Automatización";
				for (String sheetFile : resListSheets.getListSheets()) {
					if (sheetP.trim().toLowerCase().equals(sheetFile.trim().toLowerCase())) {
						existsheetProd = true;
						break;
					} else 
						existsheetProd = false;
				}
				// VALIDAMOS SI EXISTE LA HOJA SI NO SE CREA
				if(!existsheetProd) {
					System.out.println("Creando la hoja: " + sheetP);
					googleImpl.createSheet(request.getSpreadsheet_id(), sheetP);
				}
				System.out.println("#########---Trabajando con la hoja: " + sheetP);
				//Generar encabezados de la hoja Búsqueda
				String headersBusqueda = "";
				if(RangeCount == 0) {
					for (ObjMeltSearch item : request.getObjectSearch()) {
						headersBusqueda += item.getComlumnName() + ",";
					}
					headersBusqueda = headersBusqueda.substring(0, headersBusqueda.length() - 1);
				}
				//Código para hacer todo el desmadre
				RangeCount++;
				SheetResponse restGet = new SheetResponse();
				SheetResponse restGetRaw = new SheetResponse();
		        //##########GetData Sheet
		        restGet = googleImpl.getDataSheetByFilter("COLUMNS", request.getSpreadsheet_id(), sheetP.trim());
		        restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), sheetP.trim());
		        String valueInputOption = "RAW";
		        Boolean addHeaders = false;
		        Integer numPost = 1;
		        //Validamos si la hoja del sheet contiene datos
		        //Si esta vacía habrá que agregar los encabezados					
		        if (restGet.objectResult == null) {
		        	String[] headers = null;
		        	//Agregar encabezados
		        	if (!addHeaders) {
		        		if (RangeCount==1)
		        			headers = headersBusqueda.split(",");
		        		if (RangeCount==2)
		        			headers = sheetsNamesAlcance.split(",");
		        		/*if (RangeCount==3)
		        			headers = COLUMNS_FILE_MENTIONS_SH.split(",");
		        		if (RangeCount==4)
		        			headers = COLUMNS_FILE_MENTIONS_AU.split(",");*/
		        		String Headers_R = sheetP.trim() + "!" + "A1";
						List<List<Object>> valuesHeader = new ArrayList<List<Object>>();
						List<Object> valHead = new ArrayList<Object>();
						for (String item : headers) {
							valHead.add(item);
						}
						valuesHeader.add(valHead);
						addHeaders = googleImpl.addHeadersSheet(valuesHeader, Headers_R, request.getSpreadsheet_id());
					}
			        restGet = googleImpl.getDataSheetByFilter("COLUMNS", request.getSpreadsheet_id(), sheetP.trim());
			        restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), sheetP.trim());
			        addHeaders= true;
		        }
		        Integer AllItems = 0;
		        if (isNew) 
		        	AllItems = restGetRaw.objectResult.size();
		        else
		        	AllItems = restGetRaw.objectResult.size() + 2;
		        
		        if(RangeCount == 1) {
					List<List<Object>> values = new ArrayList<List<Object>>();
					List<Object> val = new ArrayList<Object>();
					for (ObjMeltSearch item : request.getObjectSearch()) {
						Class type = item.getValue().getClass();
						if (type.getName().equals("java.lang.String")) {
							if(item.getComlumnName().equals("Fecha inicio")) {
								DateStart = item.getValue().toString();
							}
							if(item.getComlumnName().equals("Fecha fin")) {
								DateEnd = item.getValue().toString();
							}
							val.add(item.getValue());
						} else if (type.getName().equals("java.util.ArrayList")) {
							List<String> map = (List<String>) item.getValue();
							String elem = "";
							for (String itemA : map) {
								elem += itemA + ",";
							}
							elem = elem.substring(0, elem.length() - 1);
							val.add(elem);
						}
					}
					values.add(val);
					String dataPost = sheetP.trim() + "!" + utilities.numToLetter(numPost) + 2;
			        ValueRange bodyPost = new ValueRange()
			                .setValues(values);
			    Sheets.Spreadsheets.Values.Update res =
			    		service.spreadsheets().values().update(request.getSpreadsheet_id(), dataPost, bodyPost);
			        res.setValueInputOption(valueInputOption).execute();
			        AllItems++;
			        Thread.sleep(800);
		        }
		        if(RangeCount == 2) {
			        List<ObjMelt> elementFile = new ArrayList<ObjMelt>();
					List<List<Object>> values = new ArrayList<List<Object>>();
					List<Object> val = new ArrayList<Object>();
					val.add("");
					val.add(utilities.convertDate(DateStart) + " - " + utilities.convertDate(DateEnd));
					val.add("");
					val.add(itemOBJ.getImpresiones());
					val.add(itemOBJ.getUsuarios());
					val.add(itemOBJ.getMenciones());
					for (Object itemAlc : itemOBJ.getDataAlcance()) {
			        	HashMap<String,String> hashitemAlc = new HashMap<>();
			        	hashitemAlc = (HashMap<String,String>) itemAlc;
			        	Boolean v = false;
			        	for (Entry<String, String> itAlcEn : hashitemAlc.entrySet()) {
			        		if(v) {
			        			if(itAlcEn.getKey().toString().equals("Twitter")) 
			        				val.add(itAlcEn.getValue().toString());
			        		}
			        		if(itAlcEn.getKey().toString().equals("Medicion") && itAlcEn.getValue().toString().equals("Alcance")) 
			        			v= true;
			        		else 
			        			break;
						}	
					}
					for (Object itemAlc : itemOBJ.getDataAlcance()) {
			        	HashMap<String,String> hashitemAlc = new HashMap<>();
			        	hashitemAlc = (HashMap<String,String>) itemAlc;
			        	Integer v = 0;
			        	for (Entry<String, String> itAlcEn : hashitemAlc.entrySet()) {
			        		if(v==2) {
			        			if(itAlcEn.getKey().toString().equals("Facebook")) 
			        				val.add(itAlcEn.getValue().toString());
			        		}
			        		if(itAlcEn.getKey().toString().equals("Medicion") && itAlcEn.getValue().toString().equals("Publicaciones")) 
			        			v++;
			        		if(itAlcEn.getKey().toString().equals("Twitter"))  
			        			v++;
						}	
					}	
					for (Object itemAlc : itemOBJ.getDataAlcance()) {
			        	HashMap<String,String> hashitemAlc = new HashMap<>();
			        	hashitemAlc = (HashMap<String,String>) itemAlc;
			        	Integer v = 0;
			        	for (Entry<String, String> itAlcEn : hashitemAlc.entrySet()) {
			        		if(itAlcEn.getKey().toString().equals("Medicion") && itAlcEn.getValue().toString().equals("Publicaciones"))
			        			break;
			        		if(itAlcEn.getKey().toString().equals("Medicion") && itAlcEn.getValue().toString().equals("Usuarios"))
			        			break;
			        		if(v==2) {
			        			if(itAlcEn.getKey().toString().equals("Facebook")) {
			        				val.add(itAlcEn.getValue().toString());
			        				v++;
			        			}
			        		}
			        		if(v==4) {
			        			if(itAlcEn.getKey().toString().equals("Totales")) 
			        				val.add(itAlcEn.getValue().toString());
			        		}
			        		if(itAlcEn.getKey().toString().equals("Medicion") && itAlcEn.getValue().toString().equals("Alcance")) 
			        			v++;
			        		if(itAlcEn.getKey().toString().equals("Twitter"))
			        			v++;
			        		if(itAlcEn.getKey().toString().equals("Whatsapp"))
			        			v++;
						}	
					}
					values.add(val);
					String dataPost = sheetP.trim() + "!" + utilities.numToLetter(numPost) + 2;
			        ValueRange bodyPost = new ValueRange()
			                .setValues(values);
			    Sheets.Spreadsheets.Values.Update res =
			    		service.spreadsheets().values().update(request.getSpreadsheet_id(), dataPost, bodyPost);
			        res.setValueInputOption(valueInputOption).execute();
			        AllItems++;
			        Thread.sleep(800);
		        }
	        	//##########__INSERCIÓN DE LA HOJA STAKEHOLDERS__#########
	        	if (RangeCount == 3) {
	        		
	        	}
			}
			//###############################################################
			//######___Llamar al servicio de actualización de Slides___######
			//###############################################################
			Thread.sleep(2000);
			SlideRequest objUDS = new SlideRequest();
			objUDS.setSlide_id(request.getSlide_id());
			objUDS.setSpreadsheet_id(request.getSpreadsheet_id());
			SlideResponse resUDS = googleSlideImpl.updateDataSlide(objUDS);
			if (resUDS.getCode() == 500) {
				result.setCode(500);
				result.setMessage("ERROR: " + resUDS.getMessage());
				return result;
			}
			//########################################################
			//####Llamar al servicio de agregar imagenes en Slides####
			//########################################################
			//String slideID = String.valueOf(request.getNumberSlide());
			Thread.sleep(1000);
			AddImgSlideRequest objAddIm = new AddImgSlideRequest();
			objAddIm.setPresentation_id(request.getSlide_id());
			objAddIm.setSlide_id(slideID);
			objAddIm.setAuthors(authors);
			SlideResponse resAddIm = googleSlideImpl.addImagesSlide(objAddIm);
			if (resAddIm.getCode() == 500) {
				result.setCode(500);
				result.setMessage("ERROR: " + resAddIm.getMessage());
				return result;
			}
			result.setCode(200);
			result.setMessage("OK");
			return result;
		} catch (Exception ex) {
			result.setCode(500);
			result.setMessage(ex.getMessage());
			return result;
		}
	}
	
	/**INSERTAR OBJETO DE LISTADO DE BUSQUEDAS EN MELT PARA LA VERSIÓN 2*/
	public ResponseEntity<?> addDataToSearchFile (SearchFileV2Request request){
		Map<String, Object> map = new HashMap<String, Object>();
		String charsToRetain = "0123456789";
		try {
			boolean existRegister = false;
			if(request.getObjectResult().size() == 0) {
				map.put("code", 204);
				map.put("message", "NO CONTENT");
				ResponseEntity<?> res = utilities.getResponseEntity(map);
				return res;
			}
			boolean existSheet = googleImpl.validateExistSheet(request.getSpreadsheet_id(), nameSheetMeditionV2.trim().toLowerCase());
			if(existSheet) {
				String[] Headers = headersMentionV2.split(",");
				SheetResponse restGet = new SheetResponse();
				SheetResponse restGetRaw = new SheetResponse();
		        //##########GetData Sheet
		        restGet = googleImpl.getDataSheetByFilter("COLUMNS", request.getSpreadsheet_id(), nameSheetMeditionV2.trim().toLowerCase());
		        restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), nameSheetMeditionV2.trim().toLowerCase());
		        if (restGet.objectResult != null) {
		        	//Generando encabezados (fecha del día)
		        	String letterInit = utilities.numToLetter(restGet.objectResult.size() + 1);
		        	String Headers_R = nameSheetMeditionV2.trim() + "!" + letterInit + 1;
		        	List<List<Object>> valuesHeader = new ArrayList<List<Object>>();
					List<Object> valHead = new ArrayList<Object>();
					
					Date Date = new Date();
					TimeZone zonaHorariaMexico = TimeZone.getTimeZone("America/Mexico_City");
			        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			        sdf.setTimeZone(zonaHorariaMexico);
			        String horaMexico = sdf.format(Date);
					
					String currentH = Headers[8]+ "-" + horaMexico;
		        	//Validando si existen registros del día de hoy
		        	String letterExist = utilities.numToLetter(restGet.objectResult.size());
		        	String HeadersExist = nameSheetMeditionV2.trim() + "!" + letterExist + 1;
		        	String[] ArrayH = restGetRaw.objectResult.get(0).toString().split(",");
		        	String lastH = ArrayH[ArrayH.length -1 ].trim().replaceAll("]", "");
		        	if(lastH.equals(currentH)) {
		        		//Existen registros con la fecha de hoy y hay que reemplazalos
		        		letterInit = utilities.numToLetter(restGet.objectResult.size()-8);
		        		existRegister = true;
		        	}
		        	else {
		        		//NO EXISTEN registros de hoy y se insertan en columnas nuevas
		        		//Insertando encabezados
						for (int i = 0; i < 9; i++) {
							valHead.add(Headers[i] + "-" + horaMexico);
						}
						valuesHeader.add(valHead);
						boolean addHeaders = googleImpl.addHeadersSheet(valuesHeader, Headers_R, request.getSpreadsheet_id());
		        	}
		        	
					//Buscando objeto por búsqueda para agregar sus registros
					List<String> listElements = googleImpl.getDataByColumn(request.getSpreadsheet_id(), "busqueda", nameSheetMeditionV2.trim());
					List<String> listANames = googleImpl.getDataByColumn(request.getSpreadsheet_id(), "nombre", nameSheetMeditionV2.trim());
					for (int j = 0; j < request.getObjectResult().size(); j++) {
						for (int i = 0; i < listElements.size(); i++) {
							if (request.getObjectResult().get(j).getSearch().equals(listElements.get(i)) && 
									request.getObjectResult().get(j).getName().equals(listANames.get(i))) {
								
								//INSERTAR ELEMENTOS DEL OBJETO DESCARGA
								insertDownloadV2(request.getObjectResult().get(j), request, horaMexico);
								int fila = i + 2;
								String fileItem = nameSheetMeditionV2.trim() + "!" + letterInit + fila;
								List<List<Object>> valuesItems = new ArrayList<List<Object>>();
								List<Object> valItem = new ArrayList<Object>();
								
								valItem.add(request.getObjectResult().get(j).getAuthors());
								valItem.add(request.getObjectResult().get(j).getImpressions());
								valItem.add(request.getObjectResult().get(j).getMentions());
								valItem.add(request.getObjectResult().get(j).getViews());
								
								//###_Insertando elementos de alcance Unexplored
								for (Object itemAlc : request.getObjectResult().get(j).getDataAlcance()) {
									HashMap<String,String> hashitemAlc = (HashMap<String,String>) itemAlc;
									for ( Entry<String, String> itAlcEn : hashitemAlc.entrySet()) {
										if (itAlcEn.getValue().toString().equals("Alcance")) {
											valItem.add(Long.parseLong(CharMatcher.anyOf(charsToRetain).retainFrom(hashitemAlc.get("Twitter"))));
											valItem.add(Long.parseLong(CharMatcher.anyOf(charsToRetain).retainFrom(hashitemAlc.get("Facebook"))));
											valItem.add(Long.parseLong(CharMatcher.anyOf(charsToRetain).retainFrom(hashitemAlc.get("Whatsapp"))));
											valItem.add(Long.parseLong(CharMatcher.anyOf(charsToRetain).retainFrom(hashitemAlc.get("Totales"))));
										}
									}
								}
								for (Object itemAlc : request.getObjectResult().get(j).getDataAlcance()) {
									HashMap<String,String> hashitemAlc = (HashMap<String,String>) itemAlc;
									for ( Entry<String, String> itAlcEn : hashitemAlc.entrySet()) {
										if (itAlcEn.getValue().toString().equals("Publicaciones")) {
											valItem.add(Long.parseLong(CharMatcher.anyOf(charsToRetain).retainFrom(hashitemAlc.get("Facebook"))));
										}
									}
								}
								// Insertando la URL del archivo de Tweets en caso de tener
								if (request.getObjectResult().get(j).getUrlListTweets() != null
										&& !request.getObjectResult().get(j).getUrlListTweets().isEmpty()) {
									//Buscar la columna de "URL_archivo"
									//Mediante la búsqueda encontrar la fila correcta e insertar
									List<List<Object>> valuesDownload = new ArrayList<List<Object>>();
									List<Object> itemDownload = new ArrayList<Object>();
									itemDownload.add(request.getObjectResult().get(j).getUrlListTweets());
									valuesDownload.add(itemDownload);
									String RangeDown = nameSheetMeditionV2.trim() + "!" + "M" + fila;
									googleImpl.updateAndReplaceData(valuesDownload, RangeDown, request.getSpreadsheet_id());
								}
								
								
								valuesItems.add(valItem);
								if(existRegister) 
									googleImpl.updateAndReplaceData(valuesItems, fileItem, request.getSpreadsheet_id());
								else 
									googleImpl.addHeadersSheet(valuesItems, fileItem, request.getSpreadsheet_id());
								Thread.sleep(800);
								break;
							}
						}
					}
					map.put("code", 200);
					map.put("message", "OK");
					ResponseEntity<?> res = utilities.getResponseEntity(map);
					return res;
		        }
		        else {
					map.put("code", 204);
					map.put("message", "NO CONTENT");
					ResponseEntity<?> res = utilities.getResponseEntity(map);
					return res;
		        }
			}
	        else {
				map.put("code", 204);
				map.put("message", "NO CONTENT");
				ResponseEntity<?> res = utilities.getResponseEntity(map);
				return res;
	        }
		} catch (Exception ex) {
			map.put("operation", 500);
			map.put("message", "ERROR");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		}
	}

	/** INSERTAR DATOS DEL OBJETO DE DESCARGAS (IMPRESIONES, MENCIONES, AUTORES Y VISTAS)*/
	public void insertDownloadV2(SearchV2Model itemSearch, SearchFileV2Request request, String date) {
		try {
			boolean existImp = false;
			boolean existMent = false;
			boolean existAut = false;
			boolean existView = false;
			List<ObjDownV2> elemA = new ArrayList<ObjDownV2>();
			if(itemSearch.getDownloads().getImpressions() != null) {
				existImp = true; elemA = itemSearch.getDownloads().getImpressions(); }
			if(itemSearch.getDownloads().getMentions() != null) {
				existMent = true; elemA = itemSearch.getDownloads().getMentions();}
			if(itemSearch.getDownloads().getAuthors() != null) {
				existAut = true; elemA = itemSearch.getDownloads().getAuthors();}
			if(itemSearch.getDownloads().getViews() != null) {
				existView = true; elemA = itemSearch.getDownloads().getViews();}
			if (existImp == false && existMent == false &&  existAut == false && existView == false) 
				return;
			
			//Validar si existe la hoja para la info de la descarga con fecha de hoy
			SheetResponse restGet = new SheetResponse();
			SheetResponse restGetRaw = new SheetResponse();
			boolean isUpdate = false;
			Integer posUpdate = 0;
			String fileItem = "";
			String nameSheet = "data_" + date;
			String spreadsheetID = request.getSpreadsheet_id();
			String Headers_R = nameSheet + "!A1";
			boolean existSheet = googleImpl.validateExistSheet(spreadsheetID, nameSheet);
			if(existSheet) {
				//El archivo existe y se reemplaza la data
		        //Buscando los elementos para reemplazar los datos
		        List<String> dataColFile = googleImpl.getDataByColumn(spreadsheetID, "Búsqueda", nameSheet);
		        for (int i = 0; i < dataColFile.size(); i++) {
		        	if(dataColFile.get(i).equals(itemSearch.getSearch())) {
						//System.out.println("La búsqueda: " + itemSearch.getSearch() + " está en la fila : " + (i + 2) );
		        		isUpdate = true;
		        		posUpdate = i + 2;
						break;
		        	}
		        }
			}
			else {
				//El archivo no existe, se crea y se inserta la data
				googleImpl.createSheet(spreadsheetID, nameSheet);
				//Insertar encabezados
				List<List<Object>> valuesHeader = new ArrayList<List<Object>>();
				List<Object> valHead = new ArrayList<Object>();
				valHead.add("Búsqueda");
				valHead.add("Fecha");
				valHead.add("Impresiones");
				valHead.add("Menciones");
				valHead.add("Autores");
				valHead.add("Vistas");
				valuesHeader.add(valHead);
				boolean addHeaders = googleImpl.addHeadersSheet(valuesHeader, Headers_R, spreadsheetID);
			}
	        //##########GetData Sheet
	        restGet = googleImpl.getDataSheetByFilter("COLUMNS", spreadsheetID, nameSheet);
	        restGetRaw = googleImpl.getDataSheetByFilter("RAW", spreadsheetID, nameSheet);
	        //Obtener el rango de inserción de los registros (nuevos o actualizados)
	        if(isUpdate)
	        	fileItem = nameSheet + "!A" + posUpdate;
	        else
	        	fileItem = nameSheet + "!A" + (restGetRaw.objectResult.size() + 1);
	        //Armado del objeto
			List<List<Object>> valuesItems = new ArrayList<List<Object>>();
			for (int i = 0; i < elemA.size(); i++) {
				List<Object> valItem = new ArrayList<Object>();
				valItem.add(itemSearch.getSearch());
				boolean dateObj = false;
				if (itemSearch.getDownloads().getImpressions() != null) {
					if(!dateObj) {
						valItem.add(itemSearch.getDownloads().getImpressions().get(i).getDate()); dateObj = true;}
					valItem.add(itemSearch.getDownloads().getImpressions().get(i).getValue());
				}
				else {
					if(!dateObj) { valItem.add(elemA.get(i).getDate()); dateObj = true;}
					valItem.add("0");
				}
				if(itemSearch.getDownloads().getMentions() != null) {
					if(!dateObj) {
						valItem.add(itemSearch.getDownloads().getMentions().get(i).getDate()); dateObj = true;} 
					valItem.add(itemSearch.getDownloads().getMentions().get(i).getValue());
				}
				else {
					if(!dateObj) { valItem.add(elemA.get(i).getDate()); dateObj = true;}
					valItem.add("0");
				}	
				if(itemSearch.getDownloads().getAuthors() != null) {
					if(!dateObj) {
						valItem.add(itemSearch.getDownloads().getAuthors().get(i).getDate()); dateObj = true;} 
					valItem.add(itemSearch.getDownloads().getAuthors().get(i).getValue());
				}
				else {
					if(!dateObj) { valItem.add(elemA.get(i).getDate()); dateObj = true; }
					valItem.add("0");
				}
				if(itemSearch.getDownloads().getViews() != null) {
					if(!dateObj) {
						valItem.add(itemSearch.getDownloads().getViews().get(i).getDate()); dateObj = true;} 
					valItem.add(itemSearch.getDownloads().getViews().get(i).getValue());
				}
				else{
					if(!dateObj) { valItem.add(elemA.get(i).getDate()); dateObj = true; }
					valItem.add("0");
				}	
				valuesItems.add(valItem);
			}
			//Si el registro existe se actualiza en caso de que no se inserta
			if(isUpdate)
				googleImpl.updateAndReplaceData(valuesItems, fileItem, spreadsheetID);
			else
				googleImpl.addHeadersSheet(valuesItems, fileItem, spreadsheetID);

		} catch (Exception ex) {
			log.error("###########___PROBLEMAS AL INSERTAR LA INFORMACIÓN EN EL ARCHIVO DE DESCARGAS V2_______###########");
			log.error(ex);
		}
	}
	
	/** INSERTAR OBJETO DE TIKTOK DE ARCHIVO ""LINKS""
	 * SE PUEDE ELEGIR LA OPCIÓN DE 
	 * => CON COMENTARIOS
	 * => SIN COMENTARIOS*/
	public ResponseEntity<?> addDataSearchFile (AddDataTikTokRequest request) {
		log.info("##################__PROCESANDO LOS REGISTROS DE TIKTOK___#################");
		log.info("==>Total de registros en Tiktok: " + request.getObjectResult().size());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(request.getType() == 1)
				nameSheetTiktok = nameSheetTiktokC.toLowerCase();
			boolean existRegister = false;
			boolean existText = false;
			if(request.getObjectResult().size() == 0) {
				log.error("###############____No viene ningún registro a procesar en TikTok");
				map.put("code", 204);
				map.put("message", "NO CONTENT");
				ResponseEntity<?> res = utilities.getResponseEntity(map);
				return res;
			}
			boolean existSheet = googleImpl.validateExistSheet(request.getSpreadsheet_id(), nameSheetTiktok.trim().toLowerCase());
			if(existSheet) {
				String[] Headers = headersTiktok.split(",");
				String[] HeadersSearch = headersSearchTiktok.split(",");
				String[] HeadersComm = headersCommentsTiktok.split(",");
				SheetResponse restGet = new SheetResponse();
				SheetResponse restGetRaw = new SheetResponse();
		        //##########GetData Sheet
		        restGet = googleImpl.getDataSheetByFilter("COLUMNS", request.getSpreadsheet_id(), nameSheetTiktok.trim().toLowerCase());
		        restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), nameSheetTiktok.trim().toLowerCase());
		        if (restGet.objectResult != null) {
		        	//Generando encabezados (fecha del día)
		        	String letterInit = utilities.numToLetter(restGet.objectResult.size() + 1);
		        	String Headers_R = nameSheetTiktok.trim() + "!" + letterInit + 1;
		        	List<List<Object>> valuesHeader = new ArrayList<List<Object>>();
					List<Object> valHead = new ArrayList<Object>();
					
					Date Date = new Date();
					TimeZone zonaHorariaMexico = TimeZone.getTimeZone("America/Mexico_City");
			        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			        sdf.setTimeZone(zonaHorariaMexico);
			        String horaMexico = sdf.format(Date);
					
					String currentH = Headers[4]+ "-" + horaMexico;
		        	//Validando si existen registros del día de hoy
		        	String letterExist = utilities.numToLetter(restGet.objectResult.size());
		        	String HeadersExist = nameSheetTiktok.trim() + "!" + letterExist + 1;
		        	String[] ArrayH = restGetRaw.objectResult.get(0).toString().split(",");
		        	String lastH = ArrayH[ArrayH.length -1 ].trim().replaceAll("]", "");
		        	if(lastH.equals(currentH)) {
		        		//Existen registros con la fecha de hoy y hay que reemplazalos
		        		log.info("Existen registros con la fecha de hoy y hay que reemplazalos");
		        		letterInit = utilities.numToLetter(restGet.objectResult.size()-4);
		        		existRegister = true;
		        	}
		        	else {
		        		log.info("NO EXISTEN registros de hoy y se insertan en columnas nuevas");
		        		//Validando si existe la columna Texto
		        		for (String item : ArrayH) {
							if(item.trim().toLowerCase().startsWith("texto"))
								existText = true;
						}
		        		//Insertando encabezados
						for (int i = 0; i < 5; i++) {
							if(existText) {
								if(Headers[i].toLowerCase().equals("texto")){System.out.println("");}
								else
									valHead.add(Headers[i] + "-" + horaMexico);
							}else {valHead.add(Headers[i] + "-" + horaMexico);}
							
						}
						valuesHeader.add(valHead);
						boolean addHeaders = googleImpl.addHeadersSheet(valuesHeader, Headers_R, request.getSpreadsheet_id());
		        	}
					//Buscando objeto por búsqueda para agregar sus registros
		        	String col = (request.getType() == 0) ? "post" : "busqueda";
					List<String> listElements = googleImpl.getDataByColumn(request.getSpreadsheet_id(), col, nameSheetTiktok.trim());
					for (int j = 0; j < request.getObjectResult().size(); j++) {
						for (int i = 0; i < listElements.size(); i++) {
							if (request.getObjectResult().get(j).getPost().equals(listElements.get(i))) {
								int fila = i + 2;
								String fileItem = nameSheetTiktok.trim() + "!" + letterInit + fila;
								List<List<Object>> valuesItems = new ArrayList<List<Object>>();
								List<Object> valItem = new ArrayList<Object>();
								if(!existText)
									valItem.add(request.getObjectResult().get(j).getText());
								valItem.add(request.getObjectResult().get(j).getLikes());
								valItem.add(request.getObjectResult().get(j).getComments());
								valItem.add(request.getObjectResult().get(j).getFavorites());
								valItem.add(request.getObjectResult().get(j).getShareds());
								//valItem.add(request.getObjectResult().get(j).getViews());
								valuesItems.add(valItem);
								if(existRegister) 
									googleImpl.updateAndReplaceData(valuesItems, fileItem, request.getSpreadsheet_id());
								else 
									googleImpl.addHeadersSheet(valuesItems, fileItem, request.getSpreadsheet_id());
								Thread.sleep(800);
								
								//Validamos si exist ela hoja de los comentarios, en caso que no crearla
								boolean existSheetCom = googleImpl.validateExistSheet(request.getSpreadsheet_id(),nameSheetTiktoknew.toLowerCase());
								if (!existSheetCom) {
									googleImpl.createSheet(request.getSpreadsheet_id(), nameSheetTiktoknew);
									String headComm =  nameSheetTiktoknew + "!A1";
						        	List<List<Object>> valuesCommH = new ArrayList<List<Object>>();
									List<Object> valCommH = new ArrayList<Object>();
									for (int k = 0; k < HeadersSearch.length; k++) {
										valCommH.add(HeadersSearch[k]);
									}
									//if(request.getObjectResult().get(0).getCommentsList().size() > 0) {
										for (int l = 0; l < HeadersComm.length; l++) {
											valCommH.add(HeadersComm[l]);
										}
									//}
									valuesCommH.add(valCommH);
									boolean addHeaders = googleImpl.addHeadersSheet(valuesCommH, headComm, request.getSpreadsheet_id());
								}
								
								//Buscando si el elemento tiene comentarios, en caso de si, insertarlos
								restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(),nameSheetTiktoknew);
								int countReg = restGetRaw.objectResult.size();
								if(request.getObjectResult().get(j).getCommentsList().size() > 0) {
									restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(),nameSheetTiktoknew);
									
									for (int k = 0; k < request.getObjectResult().get(j).getCommentsList().size(); k++) {
										List<List<Object>> itemsComm = new ArrayList<List<Object>>();
										List<Object> itemComm = new ArrayList<Object>();
										countReg++;
										String letterInitComm = "A";//utilities.numToLetter(restGetRaw.objectResult.size());
										String RangeComm = nameSheetTiktoknew + "!" + letterInitComm + countReg;
										String[] idPost = request.getObjectResult().get(j).getPost().split("/");
										itemComm.add(idPost[idPost.length -1]);
										itemComm.add(request.getObjectResult().get(j).getPost());
										itemComm.add(request.getObjectResult().get(j).getDate());
										itemComm.add(request.getObjectResult().get(j).getLikes());
										itemComm.add(request.getObjectResult().get(j).getFavorites());
										itemComm.add(request.getObjectResult().get(j).getShareds());
										itemComm.add(request.getObjectResult().get(j).getCommentsList().get(k).getUser());
										itemComm.add(request.getObjectResult().get(j).getCommentsList().get(k).getText());
										itemComm.add(request.getObjectResult().get(j).getCommentsList().get(k).getLikes());
										//valItem.add(DatS);
										itemComm.add(request.getObjectResult().get(j).getCommentsList().get(k).getDate());
										itemsComm.add(itemComm);
										
										googleImpl.updateAndReplaceData(itemsComm, RangeComm, request.getSpreadsheet_id());
										Thread.sleep(800);
									}

								}
								break;
							}
						}
					}
					map.put("code", 200);
					map.put("message", "OK");
					ResponseEntity<?> res = utilities.getResponseEntity(map);
					return res;
		        }
		        else {
					map.put("code", 204);
					map.put("message", "NO CONTENT");
					ResponseEntity<?> res = utilities.getResponseEntity(map);
					return res;
		        }
			}
	        else {
				map.put("code", 204);
				map.put("message", "NO CONTENT");
				ResponseEntity<?> res = utilities.getResponseEntity(map);
				return res;
	        }
		} catch (Exception ex) {
			log.error("#############___PROBLEMAS AL INSERTAR LOS ELEMENTOS EN EL ARCHIVO_____###########");
			log.error(ex.getMessage());
			map.put("operation", 500);
			map.put("message", "ERROR");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		}
	}

	/** INSERTAR OBJETO DE TIKTOK DE ARCHIVO ""BÚSQUEDAS""
	 * SE PUEDE ELEGIR LA OPCIÓN DE 
	 * => CON COMENTARIOS
	 * => SIN COMENTARIOS*/
	
	@Override
	public ResponseEntity<?> addDataFileComents(AddDataTikTokRequest request) {
		log.info("#######__PROCESANDO LOS REGISTROS DE TIKTOK CON COMENTARIOS__########");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (request.getObjectResult().get(0).getSearch().isEmpty()
					&& request.getObjectResult().get(0).getName().isEmpty())
				request.setType(0);
			else
				request.setType(1);
			// ResponseEntity<?> reqData = addDataSearchFile(request);

			if(request.getType() == 1) {
				boolean existRegister = false;
				boolean existText = false;
				String[] HeadersSearch = headersSearchTiktok.split(",");
				String[] HeadersComm = headersCommentsTiktok.split(",");
				String sheetNew = nameSheetTiktoknew + "_" +request.getObjectResult().get(0).getName();
				boolean existSheet = googleImpl.validateExistSheet(request.getSpreadsheet_id(),sheetNew.toLowerCase());
				// VALIDAMOS SI EXISTE LA HOJA SI NO SE CREA
				if (!existSheet) {
					googleImpl.createSheet(request.getSpreadsheet_id(), sheetNew);
					//###__AGREGANDO ENCABEZADOS
					String Headers_R = sheetNew + "!A1";
			        	List<List<Object>> valuesHeader = new ArrayList<List<Object>>();
						List<Object> valHead = new ArrayList<Object>();
						for (int i = 0; i < HeadersSearch.length; i++) {
							valHead.add(HeadersSearch[i]);
						}
					//if(request.getObjectResult().get(0).getCommentsList().size() > 0) {
						for (int i = 0; i < HeadersComm.length; i++) {
							valHead.add(HeadersComm[i]);
						}
					//}
					valuesHeader.add(valHead);
					boolean addHeaders = googleImpl.addHeadersSheet(valuesHeader, Headers_R, request.getSpreadsheet_id());
				}
				
				SheetResponse restGet = new SheetResponse();
				SheetResponse restGetRaw = new SheetResponse();
				// ##########GetData Sheet
				restGet = googleImpl.getDataSheetByFilter("COLUMNS", request.getSpreadsheet_id(),sheetNew);
				restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(),sheetNew);
				
				//int countReg = 1;
				int countReg = restGetRaw.objectResult.size();
				if (restGet.objectResult != null) {
					// INSERTANDO ELEMENTOS
					for (int i = 0; i < request.getObjectResult().size(); i++) {
						log.info("#####__Procesando la publicación : " + request.getObjectResult().get(0).getPost());
						if(request.getObjectResult().get(0).getCommentsList().size() > 0) {
							for (int j = 0; j < request.getObjectResult().get(i).getCommentsList().size(); j++) {
								List<List<Object>> valuesItems = new ArrayList<List<Object>>();
								List<Object> valItem = new ArrayList<Object>();
								countReg++;
								String letterInit = "A"; //utilities.numToLetter(restGetRaw.objectResult.size());
								String Headers_R = sheetNew + "!" + letterInit + countReg;
								String[] idPost = request.getObjectResult().get(i).getPost().split("/");
								valItem.add(idPost[idPost.length -1]);
								valItem.add(request.getObjectResult().get(i).getPost());
								//String[] DateAllArr = request.getObjectResult().get(i).getDate().split("T");
								//String[] DateArr = DateAllArr[0].split("-");
								//valItem.add(DateArr[2] + "-" + DateArr[1] + "-" + DateArr[0]);
								valItem.add(request.getObjectResult().get(i).getDate());
								valItem.add(request.getObjectResult().get(i).getLikes());
								valItem.add(request.getObjectResult().get(i).getFavorites());
								valItem.add(request.getObjectResult().get(i).getShareds());
								
								//String[] DateAllArr2 = request.getObjectResult().get(i).getCommentsList().get(j).getDate().split("T");
								//String[] DateArr2 = DateAllArr2[0].split("-");
								//String DatS = DateArr2[2] + "-" + DateArr2[1] + "-" + DateArr2[0] + " " + DateAllArr2[1].substring(0,8);
								valItem.add(request.getObjectResult().get(i).getCommentsList().get(j).getUser());
								valItem.add(request.getObjectResult().get(i).getCommentsList().get(j).getText());
								valItem.add(request.getObjectResult().get(i).getCommentsList().get(j).getLikes());
								//valItem.add(DatS);
								valItem.add(request.getObjectResult().get(i).getCommentsList().get(j).getDate());
								valuesItems.add(valItem);
								
								googleImpl.updateAndReplaceData(valuesItems, Headers_R, request.getSpreadsheet_id());
								Thread.sleep(800);
							}
						}
						else {
							log.info("LA PUBLICACIÓN NO TIENE COMENTARIOS");
							List<List<Object>> valuesItems = new ArrayList<List<Object>>();
							List<Object> valItem = new ArrayList<Object>();
							countReg++;
							String letterInit = "A"; //utilities.numToLetter(restGetRaw.objectResult.size());
							String Headers_R = sheetNew + "!" + letterInit + countReg;
							String[] idPost = request.getObjectResult().get(i).getPost().split("/");
							valItem.add(idPost[idPost.length -1]);
							valItem.add(request.getObjectResult().get(i).getPost());
							//String[] DateAllArr = request.getObjectResult().get(i).getDate().split("T");
							//String[] DateArr = DateAllArr[0].split("-");
							//valItem.add(DateArr[2] + "-" + DateArr[1] + "-" + DateArr[0]);
							valItem.add(request.getObjectResult().get(i).getDate());
							valItem.add(request.getObjectResult().get(i).getLikes());
							valItem.add(request.getObjectResult().get(i).getFavorites());
							valItem.add(request.getObjectResult().get(i).getShareds());
							valuesItems.add(valItem);
							
							googleImpl.updateAndReplaceData(valuesItems, Headers_R, request.getSpreadsheet_id());
							Thread.sleep(800);
						}
					}
				}
			}
			else {
				addDataSearchFile(request);
			}
		} catch (Exception ex) {
			log.error("ERROR AL PROCESAR: " + ex.getMessage());
			map.put("operation", 500);
			map.put("message", "ERROR");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		}
		map.put("code", 200);
		map.put("message", "OK");
		ResponseEntity<?> res = utilities.getResponseEntity(map);
		return res;
	}
	
}
