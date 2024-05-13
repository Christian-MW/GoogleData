package GoogleData.sheet.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import GoogleData.sheet.model.CodesHtmlCode;

@Component
public class CodesHtml {
	public List<CodesHtmlCode> codesUnicode;
	public List<CodesHtmlCode> codesHtml;
	
	public CodesHtml() {
		codesHtml = new ArrayList<>();
		codesHtml.add(new CodesHtmlCode("á","&aacute;"));
		codesHtml.add(new CodesHtmlCode("é","&eacute;"));
		codesHtml.add(new CodesHtmlCode("í","&iacute;"));
		codesHtml.add(new CodesHtmlCode("ó","&oacute;"));
		codesHtml.add(new CodesHtmlCode("ú","&uacute;"));
		codesHtml.add(new CodesHtmlCode("Á","&Aacute;"));
		codesHtml.add(new CodesHtmlCode("É","&Eacute;"));
		codesHtml.add(new CodesHtmlCode("Í","&Iacute;"));
		codesHtml.add(new CodesHtmlCode("Ó","&Oacute;"));
		codesHtml.add(new CodesHtmlCode("Ú","&Uacute;"));
		codesHtml.add(new CodesHtmlCode("©","&copy;"));
		codesHtml.add(new CodesHtmlCode("ñ","&ntilde;"));
		codesHtml.add(new CodesHtmlCode("Ñ","&Ntilde;"));
		
		codesUnicode = new ArrayList<>();
		codesUnicode.add(new CodesHtmlCode("á","\\u00e1"));
		codesUnicode.add(new CodesHtmlCode("é","\\u00e9"));
		codesUnicode.add(new CodesHtmlCode("í","\\u00ed"));
		codesUnicode.add(new CodesHtmlCode("ó","\\u00f3"));
		codesUnicode.add(new CodesHtmlCode("ú","\\u00fa"));
		codesUnicode.add(new CodesHtmlCode("Á","\\u00c1"));
		codesUnicode.add(new CodesHtmlCode("É","\\u00c9"));
		codesUnicode.add(new CodesHtmlCode("Í","\\u00cd"));
		codesUnicode.add(new CodesHtmlCode("Ó","\\u00d3"));
		codesUnicode.add(new CodesHtmlCode("Ú","\\u00da"));
		codesUnicode.add(new CodesHtmlCode("ñ","\\u00f1"));
		codesUnicode.add(new CodesHtmlCode("Ñ","\\u00d1"));
	}

}
