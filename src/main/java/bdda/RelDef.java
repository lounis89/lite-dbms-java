package bdda;

import java.io.Serializable;
import java.util.ArrayList;

public class RelDef implements Serializable { // === === Classes pour une relation === ===
	private String nom;				// nom de la relation
	private int nbColonne;			// nombre de colonnes
	private ArrayList<String> type;	// type de chaque colonnes
	private int recordSize;			// taille d'un record (de cette relation)
	private int slotCount;			// le nombre de slots associés à cette relation
	private int fileIdx;			// fichier auquel cette relation est associée

	private static int compteurDeRelDef = 0;	// Compteur statique du nombre de relations présentes dans la BDD

	/** Contructeur: Crée une relation vide
	 */
	public RelDef() {
		this.nom = "";
		this.nbColonne = 0;
		this.type = new ArrayList<String>();
	}

	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getNbColonne() {
		return nbColonne;
	}
	public void setNbColonne(int nbColonne) {
		this.nbColonne = nbColonne;
	}

	public ArrayList<String> getType() {
		return type;
	}
	public void setType(ArrayList<String> type) {
		this.type = type;
	}

	public int getRecordSize() {
		return recordSize;
	}
	public void setRecordSize(int recordSize) {
		this.recordSize = recordSize;
	}

	public int getSlotCount() {
		return slotCount;
	}
	public void setSlotCount(int slotCount) {
		this.slotCount = slotCount;
	}

	public void setFileIdx(int fileIdx){
		this.fileIdx = fileIdx;
	}
	public int getFileIdx(){
		return fileIdx;
	}

	public static int getCompteurDeRelDef() {
		return compteurDeRelDef;
	}

	public static void setCompteurDeRelDef(int compteurDeRelDef) {
		RelDef.compteurDeRelDef = compteurDeRelDef;
	}

}
