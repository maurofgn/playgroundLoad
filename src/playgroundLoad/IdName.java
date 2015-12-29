package playgroundLoad;

public class IdName {
	
	private int id;
	private String name;
	
	public IdName(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "IdName [id=" + id + ", name=" + name + "]";
	}
	
}
