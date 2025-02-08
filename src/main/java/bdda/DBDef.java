package bdda;

import exception.SGBDException;

import java.io.*;
import java.util.ArrayList;

public class DBDef implements Serializable {
	private ArrayList<RelDef> listeDeRelDef;
	private int compteurRel;
	
	private static DBDef dbdef = new DBDef();
	private DBDef() {
		listeDeRelDef = new ArrayList<RelDef>();
		this.compteurRel = 0;
	}
	
	public static DBDef getInstance() {
		return dbdef;
	}
	
	/** Pour ajouter une relation
	 * 
	 * @param relation (la relation a ajouter)
	 */
	public void addRelation(RelDef relation) {
		listeDeRelDef.add(relation);
		compteurRel++;
	}

	public ArrayList<RelDef> getListeDeRelDef() {
		return listeDeRelDef;
	}

	
	public void setListeDeRelDef(ArrayList<RelDef> listeDeRelDef) {
		this.listeDeRelDef = listeDeRelDef;
	}

	public int getCompteurRel() {
		return compteurRel;
	}

	public void setCompteurRel(int compteurRel) {
		this.compteurRel = compteurRel;
	}

	/** Initialiser la classe lorsque le programme demarre a partir d'un fichier Catalog.def
	 * */
	public void init() throws SGBDException {
		try {
			// Créer le répertoire DB s'il n'existe pas
			File dbDirectory = new File("DB");
			if (!dbDirectory.exists()) {
				boolean created = dbDirectory.mkdir();
				if (!created) {
					throw new SGBDException("Impossible de créer le répertoire DB");
				}
			}
			
			// Créer le fichier Catalog.def s'il n'existe pas
			File catalogFile = new File(dbDirectory, "Catalog.def");
			if (!catalogFile.exists()) {
				boolean created = catalogFile.createNewFile();
				if (!created) {
					throw new SGBDException("Impossible de créer le fichier Catalog.def");
				}
			}
			
			// Initialiser les relations si nécessaire
			this.listeDeRelDef = new ArrayList<>();
			
			// Lire le contenu du catalogue s'il existe
			if (catalogFile.length() > 0) {
				// Code pour lire le catalogue existant
				// ... 
			}
			
		} catch (IOException e) {
			throw new SGBDException("Impossible de créer un fichier: " + e.getMessage());
		}
	}

	/** Permet d'enregistrer l'instance de la classe dans le fichier Catalog.def avant d'arreter le programme
	 *
	 * @throws SGBDException
	 */
	public void finish() throws SGBDException {
		File dbDirectory = new File("DB");
		File fichier = new File(dbDirectory, "Catalog.def");
		
		try (FileOutputStream fos = new FileOutputStream(fichier);
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(dbdef);
		} catch (IOException e) {
			throw new SGBDException("Erreur lors de l'écriture dans Catalog.def: " + e.getMessage());
		}
	}

	public void reset(){
		listeDeRelDef = new ArrayList<>();
		this.compteurRel = 0;
	}
}
