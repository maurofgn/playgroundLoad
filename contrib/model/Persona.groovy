package playground

class Persona {

	String nome
	String cognome
	String citta
	String prov
	String indirizzo
	String telefono
	String mail
	String codfisc
	String psw
	String utente
	Character ruolo

	static hasMany = [caricas: Carica,
	                  societas: Societa,
	                  socios: Socio]

	static mapping = {
		version false
	}

	static constraints = {
		nome maxSize: 45
		cognome nullable: true, maxSize: 45
		citta nullable: true, maxSize: 45
		prov nullable: true, maxSize: 45
		indirizzo nullable: true, maxSize: 45
		telefono nullable: true, maxSize: 45
		mail nullable: true, maxSize: 45, unique: true
		codfisc nullable: true, maxSize: 16, unique: true
		psw nullable: true, maxSize: 45
		utente nullable: true, maxSize: 45, unique: true
		ruolo maxSize: 1
	}
}
