package playground

class Societa {

	String nome
	String citta
	String prov
	String indirizzo
	String codiceFederale
	Integer giorniRitardoAmmesso
	String site
	String mail
	Persona persona

	static hasMany = [campos: Campo,
	                  caricas: Carica,
	                  socios: Socio]
//	static belongsTo = [Persona]

	static mapping = {
		version false
		codiceFederale column: 'codiceFederale'
		giorniRitardoAmmesso column: 'giorniRitardoAmmesso'
	}
	
	static constraints = {
		nome maxSize: 45
		citta nullable: true, maxSize: 45
		prov nullable: true, maxSize: 5
		indirizzo nullable: true, maxSize: 45
		codiceFederale nullable: true, maxSize: 45
		site nullable: true, maxSize: 255
		mail nullable: true, maxSize: 45
		persona nullable: true
	}
}
