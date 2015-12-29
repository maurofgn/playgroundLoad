package playground

class Carica {

	Integer tipo
	Persona persona
	Societa societa

	static belongsTo = [Persona, Societa]

	static mapping = {
		version false
	}
}
