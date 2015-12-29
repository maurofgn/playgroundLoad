package playground

class Preno {

	Date data
	Integer oraInizio
	Integer nrOre
	Socio socio
	Campo campo

	static belongsTo = [Campo, Socio]

	static mapping = {
		version false
	}

//	static constraints = {
//		oraInizio unique: ["data", "idCampo"]
//	}
}
