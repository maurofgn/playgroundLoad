package playground

class Campo {

	String nome
	Integer tipo = 1
	String descrizione
	Integer aperturaOra = 8
	Integer aperturaMin = 0
	Integer chiusuraOra = 20
	Integer inervalloOra = 13
	Integer intervalloOre = 2
	Societa societa

	static hasMany = [prenos: Preno]
	static belongsTo = [Societa]

	static mapping = {
		version false
	}

	static constraints = {
		nome maxSize: 45
		descrizione nullable: true, maxSize: 45
	}
}
