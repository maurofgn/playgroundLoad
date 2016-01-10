package playgroundLoad;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mf.dao.CampoDao;
import org.mf.dao.Dao;
import org.mf.dao.PersonaDao;
import org.mf.model.Campo;
import org.mf.model.Persona;
import org.mf.model.Societa;

public class Load {
	
	public enum FondoCampo {Sintetico, Terra, Erba }
	
	public enum Cariche{Presidente, Vicepresidente, Segretario, Probiviro, Consigliere}
	
	private static final String DB_NAME = "booking";
	private static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
	private static final String HOST = "localhost";
	private static final int PORT = 3306;
	private static final String USERNAME = "root";
	private static final String PASSWORD = "toor";
	private static final String MAX_POOL = "250"; // set your own limit
	
	private static final String DATABASE_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;	// + "?user" + USERNAME + "?password" + PASSWORD + "?MaxPooledStatements" + MAX_POOL;
	
	private Connection connection;
	private Properties properties;
	private Persona amministratore;
	

	@Before
	public void setUp() throws Exception {
		
	}
	
	@Test
	public void loadAll() {
		loadPeople();
		loadSocieta();
		loadCampi();
		loadCariche();
		loadSoci();
		loadPreno();
	}
	
	@Test
	public void loadPreno() {
		
		List<IdName> societas = getSocietaID();
		List<Integer> socs = new ArrayList<Integer>(societas.size());
		for (IdName idName : societas)
			socs.add(idName.getId());
		
		Hashtable<Integer, List<Integer>> soci = getSoci();	//soci x società
		
		CampoDao campoDao = new CampoDao();
		Hashtable<Integer,  List<Campo>> campi = campoDao.getAllSoc(socs);	//campi x società

		Calendar calendar = GregorianCalendar.getInstance();
		int nrDay = 5;
		
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO preno "); 
		sb.append("(socio_id ,campo_id ,data ,ora) VALUES "); 
		sb.append("(?        ,?        ,?    ,?  ) ");
		//          1   	  2     	3	  4	 
		
		DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
		
		getAmministatore();
		PersonaDao pdao = new PersonaDao();
		List<Societa> socsAmm = pdao.getSocieta(amministratore.getId());
		amministratore = null;
		
		try {
			PreparedStatement stmt = getConnection().prepareStatement(sb.toString());
			
			for (int i = 0; i < nrDay; i++) {
				
				System.out.println("data: " + df.format(calendar.getTime()));
				
				for (Integer socId : campi.keySet()) {
					
					List<Integer> sociSoc = soci.get(socId);	//soci della società
					List<Campo> campiSoc = campi.get(socId);	//campi della società
					
					for (Campo campo : campiSoc) {
						System.out.println("campo: " + campo.toString());
						
						int nrPreno = 5;
						int oraInizio = campo.getAperturaOra();

						for (int j = 0; j < nrPreno && oraInizio >= 0; j++) {
							
							Integer socio = null;
							if (socsAmm.contains(socId)) {
								socsAmm.remove(socId);
								getAmministatore();
							}
							
							socio = amministratore == null ? getUnSocio(sociSoc) : amministratore.getId();	//socio della società
							amministratore = null;
							oraInizio += Utility.randInt(0, 6);		//ore vuote (libere)
							
							if (oraInizio < campo.getChiusuraOra()) {
								
								if (oraInizio >=campo.getIntervalloOra() && oraInizio < campo.getIntervalloOra() + campo.getIntervalloOre())
									oraInizio = campo.getIntervalloOra() + campo.getIntervalloOre();
								
								Dao.stmtPara(stmt, 1, Types.INTEGER, socio);
								Dao.stmtPara(stmt, 2, Types.INTEGER, campo.getId());
								Dao.stmtPara(stmt, 3, Types.DATE, new java.sql.Date(calendar.getTimeInMillis()));
								Dao.stmtPara(stmt, 4, Types.INTEGER, oraInizio);
	
								stmt.executeUpdate();
								
								int consegui = Utility.randInt(0, 4);	//ore conseguitive
								
								for (int k = 0; k < consegui; k++) {
									oraInizio = campo.getNextHour(oraInizio);
									if (oraInizio > 0) {
										Dao.stmtPara(stmt, 4, Types.INTEGER, oraInizio);										
										stmt.executeUpdate();
									}
								}
								
								System.out.println("preno: " + oraInizio + " successiva ora libera: " +  campo.getNextHour(oraInizio));
								oraInizio = campo.getNextHour(oraInizio);
							} else
								oraInizio = -1;
						}
					}
				}
				calendar.add(Calendar.DAY_OF_YEAR, 1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * estrae un socio casuale in soci
	 * @param soci
	 * @return id socio
	 */
	private Integer getUnSocio(List<Integer> soci) {
		if (soci.isEmpty())
			return null;
		
		int index = Utility.randInt(0, soci.size()-1);
		Integer retValue = soci.get(index);
//		soci.remove(index);
		return retValue;
	}
	
	
	@Test
	public void loadSocieta() {
		
		String[] sql = getSocSql();
		
		List<Persona> idList = getPeople();
		
		PreparedStatement stmt = null;
		try {
			for (int i = 0; i < sql.length; i++) {
				stmt = getConnection().prepareStatement(sql[i]);
				Persona rif = idList.get(Utility.randInt(0, idList.size()-1));
				stmt.setInt(1, rif.getId());
				stmt.executeUpdate();
			}
			stmt.close();
			disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Not yet implemented");
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) { /* NO ACTION */
				}
		}
	}
	
	private String[] getSocSql() {
		
		String[] retValue = new String[3];
		StringBuffer fix = new StringBuffer();
		
		fix.append("INSERT INTO societa "); 
		fix.append("(nome ,citta               ,prov ,indirizzo         ,codice_Federale,persona_id,giorni_Ritardo_Ammesso,site               ,mail                ) VALUES ");
		
		StringBuffer sb = new StringBuffer(fix);
		sb.append("('UP' ,'S.Severino Marche' ,'MC' ,'via Passione, 4' ,'T45c5'       ,?          ,45                  ,'www.upTennis.com' ,'upTessis@gmail.com') ");
		retValue[0] = sb.toString();
		
		sb = new StringBuffer(fix);
		sb.append("('Tennis Club' ,'S.Severino Marche' ,'MC' ,'via Coso, 35' ,'T465c7'       ,?          ,45                  ,'www.TennisClubSeverino.com' ,'TennisClubSeverino@hotmail.com') ");
		retValue[1] = sb.toString();
		
		sb = new StringBuffer();
		sb.append("INSERT INTO societa "); 
		sb.append("(nome ,citta               ,prov ,indirizzo         ,codice_Federale,persona_id,giorni_Ritardo_Ammesso,site               ,mail                ) VALUES ");
		sb.append("('Circolo Tennis' ,'Tolentino' ,'MC' ,'via meris, 51' ,'T485a8'       ,?          ,45                  ,'www.CircoloTennistolentino.com' ,'CircoloTennistolentino@hotmail.com') ");
		retValue[2] = sb.toString();

		return retValue;
	}
	
	@Test
	public void loadCampi() {
		String fix = "INSERT INTO campo (nome ,tipo ,descrizione,apertura_Ora ,apertura_Min ,chiusura_Ora ,intervallo_Ora ,intervallo_Ore ,societa_id, sequenza) VALUES ";
		StringBuffer sb = new StringBuffer();
		sb.append(fix);
		sb.append("(? ,?    ,?        ,?           ,0           ,?          ,13           ,2           ,?         , ?) "); 
		
		Player player = new Player();
		
		List<IdName> societa = getSocietaID();
		
		Hashtable<Integer, Integer> prgPerSoc = new Hashtable<Integer, Integer>();

		PreparedStatement stmt = null;
		try {
			stmt = getConnection().prepareStatement(sb.toString());
			for (int i = 0; i < 30; i++) {
				
				int apertura = 8;
				int chiusura = 23;

				IdName rifSoc = societa.get(Utility.randInt(0, societa.size()-1));
				
				Integer lastPrg = prgPerSoc.get(rifSoc.getId());
				if (lastPrg == null) 
					lastPrg = 0;
				
				prgPerSoc.put(rifSoc.getId(), ++lastPrg);
				
				stmt.setString(1, player.getNameNotUsed());
				
				stmt.setInt(2, Utility.randInt(FondoCampo.Sintetico.ordinal(), FondoCampo.Erba.ordinal()));
				stmt.setString(3, rifSoc.getName());	//descrizione
				
				apertura += Utility.randInt(0, 2);
				chiusura -= Utility.randInt(0, 2);
				
				stmt.setInt(4, apertura);
				stmt.setInt(5, chiusura);
				
				stmt.setInt(6, rifSoc.getId());
				stmt.setInt(7, lastPrg*10);
				
				stmt.executeUpdate();
			}
			stmt.close();
			disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Not yet implemented");
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) { /* NO ACTION */
				}
		}
	}
	
	@Test
	public void loadCariche() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO carica "); 
		sb.append("(tipo ,societa_id ,persona_id) VALUES "); 
		sb.append("(?    ,?         ,?        ) ");
		
		List<IdName> societa = getSocietaID();
		List<Persona> people = getPeople();
		getAmministatore();
		
		PreparedStatement stmt = null;
		try {
			stmt = getConnection().prepareStatement(sb.toString());
			for (IdName oneSoc : societa) {
				
				for (Cariche carica : Cariche.values()) {
					stmt.setInt(1, carica.ordinal());
					stmt.setInt(2, oneSoc.getId());
					
					Persona idName = amministratore != null ? amministratore :  getOneName(people);
					amministratore = null;
					
					if (idName == null) {
						System.out.println("Non ci sono nomi da usare per estrarre un nome casuale");
						System.exit(1);
					}
					stmt.setInt(3, idName.getId());
					stmt.executeUpdate();
					
					if (carica == Cariche.Consigliere) {
						for (int i = 0; i < 2; i++) {
							idName = getOneName(people);
							if (idName == null) {
								System.out.println("Non ci sono nome da estrarre");
								System.exit(1);
							}
							stmt.setInt(3, idName.getId());
							stmt.executeUpdate();
						}
					}
					
					
				}
			}
			
			stmt.close();
			disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Not yet implemented");
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) { /* NO ACTION */
				}
		}
	}
	
	@Test
	public void loadSoci() {
		
		List<IdName> societa = getSocietaID();
		List<Persona> people = getPeople(); 
		Hashtable<Integer, Persona> people2 = new Hashtable<Integer, Persona>(people.size()); 
		Hashtable<Integer, Integer> prgPerSoc = new Hashtable<Integer, Integer>();
		
		for (Persona persona : people) 
			people2.put(persona.getId(), persona);
				
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		Date minDate = calendar.getTime();
		int year = calendar.get(Calendar.YEAR);
		PreparedStatement stmtIns = null;
		PreparedStatement stmt = null;
		try {
			
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT INTO socio "); 
			sb.append("(tessera ,anno_Inizio ,scadenza ,societa_id ,persona_id) VALUES "); 
			sb.append("(?       ,?          ,?        ,?         ,?        ) ");
			stmtIns = getConnection().prepareStatement(sb.toString());

			sb = new StringBuffer();
			sb.append("SELECT societa_id ,persona_id, tipo FROM carica order by societa_id, tipo");
			stmt = getConnection().prepareStatement(sb.toString());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				//soci da cariche
				int idSoc = rs.getInt(1);
				int idPersona = rs.getInt(2);
				int tessera = getNextPrgPerSoc(prgPerSoc, idSoc);
				Date scade = Utility.randDate(minDate, 365, true);
				int annoIni = Utility.randInt(year-10, year);
				
				stmtIns.setInt(1, tessera);
				stmtIns.setInt(2, annoIni);
				stmtIns.setDate(3, new java.sql.Date(scade.getTime()));
				stmtIns.setInt(4, idSoc);
				stmtIns.setInt(5, idPersona);
				stmtIns.executeUpdate();
				
				people2.remove(idPersona);
			}
			
			stmt.close();
			
//			disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) { /* NO ACTION */
				}
		}
		
		
		int maxSociPerSocieta = (people2.size()) / societa.size();	//nr persone max per società
		
		people = new ArrayList<Persona>(people2.values());			//persone senza cariche
		
		try {
			for (IdName oneSoc : societa) {
				
				int nrSoci = Utility.randInt(10, maxSociPerSocieta);
				
				for (int i = 0; i < nrSoci; i++) {
					
					int idSoc = oneSoc.getId();
					Persona persona = getOneName(people);
					int tessera = getNextPrgPerSoc(prgPerSoc, idSoc);
					Date scade = Utility.randDate(minDate, 365, true);
					int annoIni = Utility.randInt(year-10, year);
					
					stmtIns.setInt(1, tessera);
					stmtIns.setInt(2, annoIni);
					stmtIns.setDate(3, new java.sql.Date(scade.getTime()));
					stmtIns.setInt(4, idSoc);
					stmtIns.setInt(5, persona.getId());
					stmtIns.executeUpdate();
				}
				
			}
			
			stmtIns.close();
			disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Not yet implemented");
		} finally {
			if (stmtIns != null)
				try {
					stmtIns.close();
				} catch (SQLException e) { /* NO ACTION */
				}
		}
		
		
		
		
		
	}
	
	private int getNextPrgPerSoc(Hashtable<Integer, Integer> prgPerSoc, int key) {
		Integer last = prgPerSoc.get(key);
		if (last == null)
			last=0;
		
		prgPerSoc.put(key, ++last);
		return last;
	}

	@Test
	public void loadDataMem() {
		
		DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
		Nomi people = new Nomi();
		HashSet<String> used = new HashSet<String>();
		
		for (int i = 0; i < 100; i++) {
			Nome nome = people.getNameNotUsed();
			nome.setUtente(uniqueUser(used, nome.getNome(), 0));
			System.out.println(nome.toString() + " " + nome.getUtente() + " " + nome.getCodiceFiscale() + " " + nome.getCitta() + " " + df.format(nome.getNascita()) + " " + nome.getGenere());
		}
	}
	
	@Test
	public void getPeopleTest() {
		List<Persona> names = getPeople();
		for (Persona name : names) {
			System.out.println(name);
		}
	}
	
	private List<Persona> getPeople() {
		PersonaDao dao = new PersonaDao();
		return dao.getAll();
	}
	
	private List<IdName> getSocietaID() {
		return getTableID("societa");
	}
	
	private List<IdName> getTableID(String tableName) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("select id, nome from " + tableName);
		
		ArrayList<IdName> retValue = new ArrayList<IdName>();

		PreparedStatement stmt = null;
		try {
			stmt = getConnection().prepareStatement(sb.toString());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				retValue.add(new IdName(rs.getInt(1), rs.getString(2)));
			}
			
			stmt.close();
			
			disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Not yet implemented");
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) { /* NO ACTION */
				}
		}
		
		return retValue;
	}

	private Hashtable<Integer, List<Integer>> getSoci() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("select "); 
		sb.append("s.id, s.societa_ID "); 
		sb.append("from socio s "); 
		sb.append("inner join persona p on p.id = s.persona_id "); 
		sb.append("order by s.societa_id, p.ruolo, s.id ");
		
		Hashtable<Integer, List<Integer>> retValue = new Hashtable<Integer, List<Integer>>();

		PreparedStatement stmt = null;
		try {
			stmt = getConnection().prepareStatement(sb.toString());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				
				List<Integer> soci = retValue.get(rs.getInt(2));
				
				if (soci == null) {
					soci = new ArrayList<Integer>();
					retValue.put(rs.getInt(2), soci);
				}
				soci.add(rs.getInt(1));
			}
			stmt.close();
			disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Not yet implemented");
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) { /* NO ACTION */
				}
		}
		
		return retValue;
	}
	
	private Persona getAmministatore() {
		
		if (amministratore != null)
			return amministratore;
		
		PersonaDao dao = new PersonaDao();
		amministratore = dao.getOneAmministratore();
		return amministratore;
	}
	
	@Test
	public void loadPeople() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO persona "); 
		sb.append("(nome ,cognome ,citta ,prov ,indirizzo ,telefono    ,mail ,cod_Fisc ,psw ,utente ,ruolo ,nascita ,sesso) VALUES "); 
		sb.append("(?    ,?       ,?     ,'MC' ,?         ,'3286054873',?    ,?       ,?   ,?      ,?   ,?       ,?    ) ");
		
		Nomi people = new Nomi();
		DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
		HashSet<String> used = new HashSet<String>();

		PreparedStatement stmt = null;
		try {
			stmt = getConnection().prepareStatement(sb.toString());
			for (int i = 0; i < 100; i++) {
				
				if (i == 0) {
					Nome amm = new Nome("Marco", "Feliziani",
							"via Trivelli, 1", "M", "A", 
							new Comune("I156", "San Severino Marche"),
							new GregorianCalendar(1993, 11, 29).getTime());	//mese 0 based, 29 dicembre del 1993

					amm.setRuolo("A");
					amm.setUtente(uniqueUser(used, amm.getNome(), 0));
					setPersona(stmt, amm);
					stmt.executeUpdate();
				}
				
				Nome nome = people.getNameNotUsed();
				nome.setUtente(uniqueUser(used, nome.getNome(), 0));
				System.out.println(nome.toString() + " " + nome.getCodiceFiscale() + " " + nome.getCitta() + " " + df.format(nome.getNascita()) + " " + nome.getGenere());
				
				setPersona(stmt, nome);

				stmt.executeUpdate();
			}

			stmt.close();
			
			disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Not yet implemented");
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) { /* NO ACTION */
				}
		}
	}
	
	private void setPersona(PreparedStatement stmt, Nome nome) throws SQLException {
		stmt.setString(1, nome.getNome());
		stmt.setString(2, nome.getCognome());
		stmt.setString(3, nome.getCitta());
		stmt.setString(4, nome.getInidirizzo());
		stmt.setString(5, nome.getMail());
		stmt.setString(6, nome.getCodiceFiscale());
		stmt.setString(7, nome.getPsw());
		stmt.setString(8, nome.getUtente());
		stmt.setString(9, nome.getRuolo());
		stmt.setDate(10, new java.sql.Date(nome.getNascita().getTime()));
		stmt.setString(11, nome.getGenere());
	}
	
	private String uniqueUser(HashSet<String> used, String nome, int prg) {
		if (used.contains(nome)) {
			prg++;
			nome = uniqueUser(used, nome + prg, prg);
		} else
			used.add(nome);
		return nome;
			
	}
	
	// connect database
	public Connection getConnection() {
		if (connection == null) {
			try {
				// DriverManager.registerDriver((Driver)
				// Class.forName(DATABASE_DRIVER).newInstance());
				Class.forName(DATABASE_DRIVER);
				connection = DriverManager.getConnection(DATABASE_URL, getProperties());

			} catch (ClassNotFoundException | SQLException e) {
				// Java 7+
				e.printStackTrace();
			}
		}
		return connection;
	}
	
	 private Properties getProperties() {
	        if (properties == null) {
	            properties = new Properties();
	            properties.setProperty("user", USERNAME);
	            properties.setProperty("password", PASSWORD);
	            properties.setProperty("MaxPooledStatements", MAX_POOL);
	        }
	        return properties;
	    }
	
	public void disconnect() {
	    if (connection != null) {
	        try {
	            connection.close();
	            connection = null;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	/**
	 * estrae un nome casuale in people e poi lo elimina dalla lista
	 * @param people
	 * @return
	 */
	private Persona getOneName(List<Persona> people) {
		if (people.isEmpty())
			return null;
		
		int index = Utility.randInt(0, people.size()-1);
		Persona retValue = people.get(index);
		people.remove(index);
		return retValue;
	}
	
	
	
//	class Persona {
//
//		public Persona(String nome, String cognome, String citta, String prov,
//				String indirizzo, String telefono, String mail, String codFisc,
//				String psw, String utente, String ruolo) {
//			
//			super();
//			this.nome = nome;
//			this.cognome = cognome;
//			this.citta = citta;
//			this.prov = prov;
//			this.indirizzo = indirizzo;
//			this.telefono = telefono;
//			this.mail = mail;
//			this.codFisc = codFisc;
//			this.psw = psw;
//			this.utente = utente;
//			this.ruolo = ruolo;
//		}
//		
//		String nome;
//		String cognome;
//		String citta;
//		String prov;
//		String indirizzo;
//		String telefono;
//		String mail;
//		String codFisc;
//		String psw;
//		String utente;
//		String ruolo;
//	}
		
}
