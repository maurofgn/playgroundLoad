package playgroundLoad;

import java.util.HashSet;

public class Player {
	
	private String[] names = {
	"John McEnroe","Jimmy Connors","Bob Bryan","Mike Bryan","Ilie Nastase","Ivan Lendl","Roger Federer","Todd Woodbridge","Daniel Nestor","Tom Okker","Stan Smith","Brian Gottfried","Raúl Ramírez",
	"Guillermo Vilas","Mark Woodforde","Rafael Nadal","Rod Laver","Frew McMillan","Björn Borg","Wojciech Fibak","Anders Järryd","John Newcombe","Marty Riessen","Pete Sampras","Leander Paes",
	"Emilio Sánchez","Boris Becker","Bob Hewitt","Tomáš Šmíd","Peter Fleming","Andre Agassi","Mahesh Bhupathi","Jonas Björkman","Stefan Edberg","Novak Đoković","Nenad Zimonjić","Mark Knowles",
	"Paul Haarhuis","Manuel Orantes","Andrés Gómez","Maks Mirny","Sherwood Stewart","Evgenij Kafel'nikov","Robert Lutz","Martina Navrátilová","Chris Evert","Billie Jean King","Margaret Court",
	"Pam Shriver","Rosemary Casals","Steffi Graf","Evonne GoolagongCawley","Jana Novotná","Arantxa SánchezVicario","Martina Hingis","Lindsay Davenport","Serena Williams","Lisa Raymond","Nataša Zvereva",
	"Helena Suková","Gigi Fernández","Larisa Neiland","Venus Williams","Wendy Turnbull","Cara Black","Rennae Stubbs","Monica Seles","Liezel Huber","Virginia Wade","Kim Clijsters"
	};
	
	private HashSet<Integer> used = new HashSet<Integer>();
	
	/**
	 * 
	 * @return
	 */
	public String getNameNotUsed() {
		return names[getPosNotUsed()];
	}
	
	public String getName() {
		return names[Utility.randInt(0, names.length-1)];
	}
	
	private int getPosNotUsed() {
		int pos = 0;
		
		 do { 
			 pos = Utility.randInt(0, names.length-1);
		 } while(used.contains(pos));
		 
		 used.add(pos);
		return pos;
	}

	
	
	
}
