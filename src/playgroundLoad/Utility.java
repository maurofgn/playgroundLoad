package playgroundLoad;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class Utility {
	
	public static int randInt(int min, int max) {
		Random rand = new Random();
	    return rand.nextInt((max - min) + 1) + min;
	}
	
	public static Date randDate(int yearMin, int yearMax) {
		GregorianCalendar d = new GregorianCalendar(randInt(yearMin, yearMax), randInt(1, 12),  randInt(1, 31));
	    return d.getTime();
	}
	
	public static Date randDate(Date min, int ggMax) {
		return randDate(min, ggMax, false);
	}
	
	public static Date randDate(Date min, int ggMax, boolean fineMese) {
		GregorianCalendar d = new GregorianCalendar();
		d.setTime(min);
		d.set(Calendar.HOUR_OF_DAY, 0);
		d.add(Calendar.DAY_OF_YEAR, randInt(0, ggMax));
		
		if (fineMese) {
			d.add(Calendar.MONTH, 1);
			d.set(Calendar.DAY_OF_MONTH, 0);
		}
		
	    return d.getTime();
	}

	
	public static boolean randBool() {
		Random rand = new Random();
	    return rand.nextInt((1 - 0) + 1) + 0 == 1;
	}
	
	public static String toCamelCase(String s) {
		String[] parts = s.split("_");
		String camelCaseString = "";
		for (String part : parts) {
			camelCaseString = camelCaseString + toProperCase(part);
		}
		return camelCaseString;
	}
	
	public static String toCamelCaseSpace(String s) {
		String[] parts = s.split(" ");
		String camelCaseString = "";
		for (String part : parts) {
			camelCaseString = camelCaseString + " "  + toProperCase(part);
		}
		return camelCaseString.trim();
	}


	public static String toProperCase(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}



	/**
	 * 
	 * @param cognomecf cognome
	 * @param nomecf nome
	 * @param dataNascitacf data di nascita
	 * @param codReg codice regionale del comune
	 * @param maschio sesso, true per maschio
	 * @return codice fiscale
	 */
	public static String calcoloCodiceFiscale(String cognome, String nome, Date dataNascitacf, String codReg, Boolean maschio) {
		
		String cognomecf = cognome.replaceAll(" ", "").trim().toUpperCase();
		String nomecf = nome.replaceAll(" ", "").trim().toUpperCase();
		String codFis = "";
		
		/* calcolo prime 3 lettere */
		int cont = 0;
		/* caso cognome minore di 3 lettere */
		if (cognomecf.length() < 3) {
			codFis += cognomecf;
			while (codFis.length() < 3)
				codFis += "X";
			cont = 3;
		}
		/* caso normale */
		for (int i = 0; i < cognomecf.length(); i++) {
			if (cont == 3)
				break;
			if (cognomecf.charAt(i) != 'A' && cognomecf.charAt(i) != 'E'
					&& cognomecf.charAt(i) != 'I' && cognomecf.charAt(i) != 'O'
					&& cognomecf.charAt(i) != 'U') {
				codFis += Character.toString(cognomecf.charAt(i));
				cont++;
			}
		}
		/* nel casoci siano meno di 3 consonanti */
		while (cont < 3) {
			for (int i = 0; i < cognomecf.length(); i++) {
				if (cont == 3)
					break;
				if (cognomecf.charAt(i) == 'A' || cognomecf.charAt(i) == 'E'
						|| cognomecf.charAt(i) == 'I'
						|| cognomecf.charAt(i) == 'O'
						|| cognomecf.charAt(i) == 'U') {
					codFis += Character.toString(cognomecf.charAt(i));
					cont++;
				}
			}
		}
		/* lettere nome */
		cont = 0;
		/* caso nome minore di 3 lettere */
		if (nomecf.length() < 3) {
			codFis += nomecf;
			while (codFis.length() < 6)
				codFis += "X";
			cont = 3;
		}
		/* caso normale */
		for (int i = 0; i < nomecf.length(); i++) {
			if (cont == 3)
				break;
			if (nomecf.charAt(i) != 'A' && nomecf.charAt(i) != 'E'
					&& nomecf.charAt(i) != 'I' && nomecf.charAt(i) != 'O'
					&& nomecf.charAt(i) != 'U') {
				codFis += Character.toString(nomecf.charAt(i));
				cont++;
			}
		}
		/* nel caso ci siano meno di 3 consonanti */
		while (cont < 3) {
			for (int i = 0; i < nomecf.length(); i++) {
				if (cont == 3)
					break;
				if (nomecf.charAt(i) == 'A' || nomecf.charAt(i) == 'E'
						|| nomecf.charAt(i) == 'I' || nomecf.charAt(i) == 'O'
						|| nomecf.charAt(i) == 'U') {
					codFis += Character.toString(nomecf.charAt(i));
					cont++;
				}
			}
		}
		
		GregorianCalendar dn = new GregorianCalendar();
		dn.setTime(dataNascitacf);
		
		/* anno */
		int year = dn.get(GregorianCalendar.YEAR);
		int mese = dn.get(GregorianCalendar.MONTH)+1;
		int giorno = dn.get(GregorianCalendar.DAY_OF_MONTH);
		
		codFis += String.format("%02d", year % 100);
		codFis += getMeseAlfa(mese);
		codFis += String.format("%02d", giorno + (maschio ? 0 : 40));
		codFis += codReg;	/* comune nascita */
		codFis += calculateCheck(codFis);
		return codFis;
	}

	private static String calculateCheck(String codFis) {
		/* Carattere di controllo */
		int sommaPari = 0;
		for (int i = 1; i <= 13; i += 2) {
			switch (codFis.charAt(i)) {
			case '0': {
				sommaPari += 0;
				break;
			}
			case '1': {
				sommaPari += 1;
				break;
			}
			case '2': {
				sommaPari += 2;
				break;
			}
			case '3': {
				sommaPari += 3;
				break;
			}
			case '4': {
				sommaPari += 4;
				break;
			}
			case '5': {
				sommaPari += 5;
				break;
			}
			case '6': {
				sommaPari += 6;
				break;
			}
			case '7': {
				sommaPari += 7;
				break;
			}
			case '8': {
				sommaPari += 8;
				break;
			}
			case '9': {
				sommaPari += 9;
				break;
			}
			case 'A': {
				sommaPari += 0;
				break;
			}
			case 'B': {
				sommaPari += 1;
				break;
			}
			case 'C': {
				sommaPari += 2;
				break;
			}
			case 'D': {
				sommaPari += 3;
				break;
			}
			case 'E': {
				sommaPari += 4;
				break;
			}
			case 'F': {
				sommaPari += 5;
				break;
			}
			case 'G': {
				sommaPari += 6;
				break;
			}
			case 'H': {
				sommaPari += 7;
				break;
			}
			case 'I': {
				sommaPari += 8;
				break;
			}
			case 'J': {
				sommaPari += 9;
				break;
			}
			case 'K': {
				sommaPari += 10;
				break;
			}
			case 'L': {
				sommaPari += 11;
				break;
			}
			case 'M': {
				sommaPari += 12;
				break;
			}
			case 'N': {
				sommaPari += 13;
				break;
			}
			case 'O': {
				sommaPari += 14;
				break;
			}
			case 'P': {
				sommaPari += 15;
				break;
			}
			case 'Q': {
				sommaPari += 16;
				break;
			}
			case 'R': {
				sommaPari += 17;
				break;
			}
			case 'S': {
				sommaPari += 18;
				break;
			}
			case 'T': {
				sommaPari += 19;
				break;
			}
			case 'U': {
				sommaPari += 20;
				break;
			}
			case 'V': {
				sommaPari += 21;
				break;
			}
			case 'W': {
				sommaPari += 22;
				break;
			}
			case 'X': {
				sommaPari += 23;
				break;
			}
			case 'Y': {
				sommaPari += 24;
				break;
			}
			case 'Z': {
				sommaPari += 25;
				break;
			}
			}
		}
		
		if (codFis.length()<15) {
			System.out.println("Errore " + codFis);
		}
		
		int sommaDispari = 0;
		for (int i = 0; i <= 14; i += 2) {
			switch (codFis.charAt(i)) {
			case '0': {
				sommaDispari += 1;
				break;
			}
			case '1': {
				sommaDispari += 0;
				break;
			}
			case '2': {
				sommaDispari += 5;
				break;
			}
			case '3': {
				sommaDispari += 7;
				break;
			}
			case '4': {
				sommaDispari += 9;
				break;
			}
			case '5': {
				sommaDispari += 13;
				break;
			}
			case '6': {
				sommaDispari += 15;
				break;
			}
			case '7': {
				sommaDispari += 17;
				break;
			}
			case '8': {
				sommaDispari += 19;
				break;
			}
			case '9': {
				sommaDispari += 21;
				break;
			}
			case 'A': {
				sommaDispari += 1;
				break;
			}
			case 'B': {
				sommaDispari += 0;
				break;
			}
			case 'C': {
				sommaDispari += 5;
				break;
			}
			case 'D': {
				sommaDispari += 7;
				break;
			}
			case 'E': {
				sommaDispari += 9;
				break;
			}
			case 'F': {
				sommaDispari += 13;
				break;
			}
			case 'G': {
				sommaDispari += 15;
				break;
			}
			case 'H': {
				sommaDispari += 17;
				break;
			}
			case 'I': {
				sommaDispari += 19;
				break;
			}
			case 'J': {
				sommaDispari += 21;
				break;
			}
			case 'K': {
				sommaDispari += 2;
				break;
			}
			case 'L': {
				sommaDispari += 4;
				break;
			}
			case 'M': {
				sommaDispari += 18;
				break;
			}
			case 'N': {
				sommaDispari += 20;
				break;
			}
			case 'O': {
				sommaDispari += 11;
				break;
			}
			case 'P': {
				sommaDispari += 3;
				break;
			}
			case 'Q': {
				sommaDispari += 6;
				break;
			}
			case 'R': {
				sommaDispari += 8;
				break;
			}
			case 'S': {
				sommaDispari += 12;
				break;
			}
			case 'T': {
				sommaDispari += 14;
				break;
			}
			case 'U': {
				sommaDispari += 16;
				break;
			}
			case 'V': {
				sommaDispari += 10;
				break;
			}
			case 'W': {
				sommaDispari += 22;
				break;
			}
			case 'X': {
				sommaDispari += 25;
				break;
			}
			case 'Y': {
				sommaDispari += 24;
				break;
			}
			case 'Z': {
				sommaDispari += 23;
				break;
			}
			}
		}
		int interoControllo = (sommaPari + sommaDispari) % 26;
		String carattereControllo = "";
		switch (interoControllo) {
		case 0: {
			carattereControllo = "A";
			break;
		}
		case 1: {
			carattereControllo = "B";
			break;
		}
		case 2: {
			carattereControllo = "C";
			break;
		}
		case 3: {
			carattereControllo = "D";
			break;
		}
		case 4: {
			carattereControllo = "E";
			break;
		}
		case 5: {
			carattereControllo = "F";
			break;
		}
		case 6: {
			carattereControllo = "G";
			break;
		}
		case 7: {
			carattereControllo = "H";
			break;
		}
		case 8: {
			carattereControllo = "I";
			break;
		}
		case 9: {
			carattereControllo = "J";
			break;
		}
		case 10: {
			carattereControllo = "K";
			break;
		}
		case 11: {
			carattereControllo = "L";
			break;
		}
		case 12: {
			carattereControllo = "M";
			break;
		}
		case 13: {
			carattereControllo = "N";
			break;
		}
		case 14: {
			carattereControllo = "O";
			break;
		}
		case 15: {
			carattereControllo = "P";
			break;
		}
		case 16: {
			carattereControllo = "Q";
			break;
		}
		case 17: {
			carattereControllo = "R";
			break;
		}
		case 18: {
			carattereControllo = "S";
			break;
		}
		case 19: {
			carattereControllo = "T";
			break;
		}
		case 20: {
			carattereControllo = "U";
			break;
		}
		case 21: {
			carattereControllo = "V";
			break;
		}
		case 22: {
			carattereControllo = "W";
			break;
		}
		case 23: {
			carattereControllo = "X";
			break;
		}
		case 24: {
			carattereControllo = "Y";
			break;
		}
		case 25: {
			carattereControllo = "Z";
			break;
		}
		}
		
		return carattereControllo;
	}

	private static String getMeseAlfa(int mese) {
		
		String meseAlfa = "";
		
		switch (mese) {
		case 1: {
			meseAlfa += "A";
			break;
		}
		case 2: {
			meseAlfa += "B";
			break;
		}
		case 3: {
			meseAlfa += "C";
			break;
		}
		case 4: {
			meseAlfa += "D";
			break;
		}
		case 5: {
			meseAlfa += "E";
			break;
		}
		case 6: {
			meseAlfa += "H";
			break;
		}
		case 7: {
			meseAlfa += "L";
			break;
		}
		case 8: {
			meseAlfa += "M";
			break;
		}
		case 9: {
			meseAlfa += "P";
			break;
		}
		case 10: {
			meseAlfa += "R";
			break;
		}
		case 11: {
			meseAlfa += "S";
			break;
		}
		case 12: {
			meseAlfa += "T";
			break;
		}
		}
		
		return meseAlfa;
		
	}
	
	

}
