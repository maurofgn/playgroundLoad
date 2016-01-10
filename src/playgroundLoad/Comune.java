package playgroundLoad;

public class Comune {

	private String codReg;
	private String nome;

	public Comune(String codReg, String nome) {
		super();
		this.codReg = codReg;
		this.nome = nome;
	}

	public String getCodReg() {
		return codReg;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codReg == null) ? 0 : codReg.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		Comune other = (Comune) obj;
		if (codReg == null) {
			if (other.codReg != null)
				return false;
		} else if (!codReg.equals(other.codReg))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Comune [codReg=" + codReg + ", nome=" + nome + "]";
	}

}
