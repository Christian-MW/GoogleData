package GoogleData.sheet.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ObjMelt {
	private String search;
	private String usuarios;
	private String menciones;
	private String impresiones;
	private String alcance;
	private HashMap<String, List<String>> valuesFile;
	private ArrayList<Object> dataAlcance;
	private ArrayList<Object> authors;
	
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}

	public String getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(String usuarios) {
		this.usuarios = usuarios;
	}
	
	public String getMenciones() {
		return menciones;
	}
	public void setMenciones(String menciones) {
		this.menciones = menciones;
	}
	public String getImpresiones() {
		return impresiones;
	}
	public void setImpresiones(String impresiones) {
		this.impresiones = impresiones;
	}
	public String getAlcance() {
		return alcance;
	}
	public void setAlcance(String alcance) {
		this.alcance = alcance;
	}
	public HashMap<String, List<String>> getValuesFile() {
		return valuesFile;
	}
	public void setValuesFile(HashMap<String, List<String>> valuesFile) {
		this.valuesFile = valuesFile;
	}
	public ArrayList<Object> getDataAlcance() {
		return dataAlcance;
	}
	public void setDataAlcance(ArrayList<Object> dataAlcance) {
		this.dataAlcance = dataAlcance;
	}
	public ArrayList<Object> getAuthors() {
		return authors;
	}
	public void setAuthors(ArrayList<Object> authors) {
		this.authors = authors;
	}
	
	
}
