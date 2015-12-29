package playground

class Socio {

	Integer tessera
	Integer annoInizio
	Date scadenza
	Persona persona
	Societa societa

	static hasMany = [prenos: Preno]
	static belongsTo = [Persona, Societa]

	static mapping = {
		version false
	}
}
