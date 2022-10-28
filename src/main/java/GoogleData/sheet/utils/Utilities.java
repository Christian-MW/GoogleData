package GoogleData.sheet.utils;

import java.util.HashMap;
import java.util.Iterator;

import org.springframework.stereotype.Component;

@Component
public class Utilities {

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
}
