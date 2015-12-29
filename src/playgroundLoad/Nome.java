package playgroundLoad;

import java.util.Date;
import java.util.GregorianCalendar;

public class Nome {

	public static final Nome amministratore = new Nome("Marco", "Feliziani", "via Trivelli, 1", "M", "A", new Comune("I156", "San Severino Marche"));
	
	private String nome;
	private String cognome;
	private String inidirizzo;
	private Date nascita;
	private String codFisc;
	private Comune comune;
	private String genere; 
	private String ruolo;
	
	Comune[] comuni = { new Comune("C715", "Cinzano"),
			new Comune("L191", "Tolentino"),
			new Comune("F051", "Matelica"),
			new Comune("E783", "Macerata"),
			new Comune("I156", "San Severino Marche"),
			new Comune("C704", "Cingoli") };
	
	private int yearMax = GregorianCalendar.getInstance().get(GregorianCalendar.YEAR)-6;
	private int yearMin = yearMax-50;
    
	public Nome(String nome, String cognome, String inidirizzo) {
		this(nome, cognome, inidirizzo, Utility.randBool() ? "M" : "F", "U", null);
	}
	
	public Nome(String nome, String cognome, String inidirizzo, String genere, String ruolo, Comune comune) {
		super();
		this.nome = Utility.toCamelCaseSpace(nome);
		this.cognome = Utility.toCamelCaseSpace(cognome);
		this.inidirizzo = inidirizzo;
		nascita = Utility.randDate(yearMin, yearMax);
		this.comune = comune == null ? comuni[Utility.randInt(0, comuni.length-1)] : comune;
		this.genere = genere.toUpperCase();
		this.ruolo = ruolo;
		codFisc = Utility.calcoloCodiceFiscale(cognome, nome, nascita, this.comune.getCodReg(), genere.equalsIgnoreCase("M"));
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public String getInidirizzo() {
		return inidirizzo;
	}
	
	public Date getNascita() {
		return nascita;
	}
	
	public String getGenere() {
		return genere;
	}
	
	
	public String getCodiceFiscale() {
		return codFisc;
	}
	
	public Comune getComune() {
		return comune;
	}
	public String getCitta() {
		return comune.getNome();
	}
	
		
	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	@Override
	public String toString() {
		return "Nome [" + nome + ", " + cognome + ", " + inidirizzo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((cognome == null) ? 0 : cognome.hashCode());
		result = prime * result
				+ ((inidirizzo == null) ? 0 : inidirizzo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Nome other = (Nome) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (cognome == null) {
			if (other.cognome != null)
				return false;
		} else if (!cognome.equals(other.cognome))
			return false;
		if (inidirizzo == null) {
			if (other.inidirizzo != null)
				return false;
		} else if (!inidirizzo.equals(other.inidirizzo))
			return false;
		return true;
	}

	public String getMail() {
		return nomeCognome(".").replaceAll(" ", "").toLowerCase() + "@gmail.com";
	}
	
	private String nomeCognome(String separator) {
		return nome + separator.trim() + cognome;
	}

	public String getPsw() {
		return nomeCognome("").replaceAll(" ", "").toLowerCase();
	}

	public String getUtente() {
		return nomeCognome(".").replaceAll(" ", "");
	}
	
	
	
}
