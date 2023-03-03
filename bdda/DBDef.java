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
		File fichier = new File(Constantes.pathName + "Catalog.def");
		try(FileInputStream fis = new FileInputStream(fichier); ObjectInputStream ois = new ObjectInputStream(fis)){
			dbdef = (DBDef) ois.readObject();
			System.out.println(dbdef.listeDeRelDef);
		} catch (FileNotFoundException e) { // si le fichier Catalog.def n'existe pas
			dbdef = new DBDef(); // on initialise la classe sans rien en plus
			try {
				fichier.createNewFile(); // On cree le fichier
			} catch (IOException ioe) {
				throw new SGBDException("Impossible de creer un fichier");
			}
		} catch (IOException e) { // S'il y a une autre erreurr d'I/O
			throw new SGBDException("Erreur lors de la lecture de l'objet DBDef dans le fichier Catalog.def, il se peut qu'il soit corrompu");
		} catch (ClassNotFoundException e) {
			throw new SGBDException("Euh c'est bizarre la, la classe DBDef ne trouve pas la classe DBDef");
		}
	}

	/** Permet d'enregistrer l'instance de la classe dans le fichier Catalog.def avant d'arreter le programme
	 *
	 * @throws SGBDException
	 */
	public void finish() throws SGBDException {
        File fichier = new File(Constantes.pathName + "Catalog.def");
        if(!fichier.exists()) { // Si le fichier n'existe pas
            //System.out.println("Le fichier n'existe pas");
            try {
                fichier.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try(FileOutputStream fos = new FileOutputStream(fichier); ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(dbdef);
        } catch (FileNotFoundException e) {
            throw new SGBDException("Impossible de creer un fichier");
        } catch (IOException e) {
            e.printStackTrace();
            throw new SGBDException("Erreur lors de l'ecriture de l'objet DBDef dans le fichier Catalog.def");
        }
    }

    public void reset(){
		listeDeRelDef = new ArrayList<>();
		this.compteurRel = 0;
	}
}
