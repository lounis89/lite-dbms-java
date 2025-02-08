package bdda;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import exception.ReqException;
import exception.SGBDException;

public class FileManager {

	private List<HeapFile> listeHeapFiles = new ArrayList<>();
	private static final FileManager fileManager = new FileManager();

	private FileManager() {
	}

	public static FileManager getInstance() {
		return fileManager;
	}

	public List<HeapFile> getListe() {
		return listeHeapFiles;
	}

	public void init() {
		ArrayList<RelDef> list = DBDef.getInstance().getListeDeRelDef();
		for (int i = 0; i < list.size(); i++) {
			HeapFile heapFile = new HeapFile();
			heapFile.setPointeur(list.get(i));
			listeHeapFiles.add(heapFile);
		}
	}

	public void createNewHeapFile(RelDef iRelDel) {
		HeapFile heapFile = new HeapFile();
		heapFile.setPointeur(iRelDel);
		listeHeapFiles.add(heapFile); // TODO j'ai ajouté ça (Enzo)
		try {
			heapFile.createNewOnDisk();
		} catch (IOException | ReqException | SGBDException e) {
			//TODO ça c'est un peu bof bof x)
			e.printStackTrace();
		}

	}

	/**
	 * fonction pour utiliser la methode insertRecord() du HeapFile correspondant a
	 * la relation en question
	 *
	 * @param iRelationName
	 *            (le nom de la relation)
	 * @param iRecord
	 *            (le contenu du tuple a ajouter)
	 * @return (l'ID du tuple ajoute)
	 */
	Rid insertRecordInRelation(String iRelationName, Record iRecord) throws SGBDException {
		// parcourir la liste des HeapFiles pour trouver celui qui correspond a la
		// relation en question, et ensuite appeler sa propre methode InsertRecord
		Rid rid = null;
		boolean found = false;
		//System.out.println(listeHeapFiles.size());
		for (int i = 0; i < listeHeapFiles.size(); i++) {
			//System.out.println(listeHeapFiles.get(i).getPointeur().getNom());
			if (listeHeapFiles.get(i).getPointeur().getNom().equals(iRelationName)) {
				rid = listeHeapFiles.get(i).insertRecord(iRecord);

				//TODO le print de debug qu'on utilisait depuis le début
                //System.out.println("Insertion dans la relation : " + iRelationName + " ID: "+ rid.getPageId().getFileIdx() +" page  n°: "+ rid.getPageId().getPageIdx() + " slot n°: " + rid.getSlotIdx() );

				found = true;
			}
		}
		if (found) {
			return rid;
		} else {
			throw new SGBDException("La relation dans laquelle vous voulez insérer votre tuple n'est pas dans la liste des HeapFiles");
		}

	}

	public ArrayList<Record> getAllRecords(String iRelationName) throws SGBDException {
		ArrayList<Record> listeDeRecords = new ArrayList<Record>();
		boolean trouve = false;
		for (int i = 0; i < listeHeapFiles.size(); i++) {
			if (listeHeapFiles.get(i).getPointeur().getNom().equals(iRelationName)) {
				List<PageId> listePageId = new ArrayList<PageId>();
				listePageId = listeHeapFiles.get(i).getDataPagesIds();
				for (int j = 0; j < listePageId.size(); j++) {
					listeDeRecords.addAll(listeHeapFiles.get(i).getRecordsOnPage(listePageId.get(j)));
				}
				trouve = true;
			}
		}
		if(trouve){
			return listeDeRecords;
		} else {
			throw new SGBDException("Relation introuvable dans la liste des HeapFiles");
		}

	}

	/**
	 * Fonction qui renvoie une liste contenant tous les records si la valeur retourné par iIdxCol correspond bien
	 * au String donné en paramètre
	 * @throws ReqException 
	 * @throws SGBDException 
	 **/
	public ArrayList<Record> getAllRecordsWithFilter(String iRelationName, int iIdxCol, String iValeur) throws SGBDException {
		ArrayList<Record> listeAllRecords = getAllRecords(iRelationName);
		ArrayList<Record> listeFilter = new ArrayList<Record>();
		for(int i = 0 ; i<listeAllRecords.size();i++) {
			if(listeAllRecords.get(i).getValues().get(iIdxCol).equals(iValeur)) {
				listeFilter.add(listeAllRecords.get(i));
			}
		}
		return listeFilter;
	}

	public ArrayList<Record> join(String nomRel1, String nomRel2, int indiceCol1, int indiceCol2) throws SGBDException{
	    HeapFile hfRel1 = null;
	    HeapFile hfRel2 = null;

	    ArrayList<Record> resultat = new ArrayList<>();

	    for(HeapFile hf : listeHeapFiles){
	        if(hf.getPointeur().getNom().equals(nomRel1)){
	            hfRel1 = hf;
	        }
            if(hf.getPointeur().getNom().equals(nomRel2)){
	            hfRel2 = hf;
            }
        }


	    for(PageId pageRel1 : hfRel1.getDataPagesIds()){
	        ArrayList<Record> recordsRel1 = hfRel1.getRecordsOnPage(pageRel1);
	        for(PageId pageRel2 : hfRel2.getDataPagesIds()){
                ArrayList<Record> recordsRel2 = hfRel2.getRecordsOnPage(pageRel2);
                for (Record recRel1 : recordsRel1){
                    for(Record recRel2 : recordsRel2){
                        if(recRel1.getValues().get(indiceCol1-1).equals(recRel2.getValues().get(indiceCol2-1))){
                            Record record = new Record();
                            record.addValue(recRel1.getValues().get(indiceCol1-1));
                            for (int i = 0; i<recRel1.getValues().size(); i++){
                                if(i != indiceCol1-1) record.addValue(recRel1.getValues().get(i));
                            }
                            for (int i = 0; i<recRel2.getValues().size(); i++){
                                if(i != indiceCol2-1) record.addValue(recRel2.getValues().get(i));
                            }
                            resultat.add(record);
                        }
                    }
                }
            }
        }

	    return resultat;
    }

	public void reset(){
		listeHeapFiles = new ArrayList<>();
	}
}