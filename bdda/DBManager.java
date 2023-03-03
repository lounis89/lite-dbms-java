package bdda;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.StringTokenizer;

import exception.ReqException;
import exception.SGBDException;

public class DBManager {
	private DBDef dbDef;

	private static final DBManager db = new DBManager();

	private DBManager() {

	}

	public static DBManager getInstance() {
		return db;
	}

	public void init() throws SGBDException {
		DBDef.getInstance().init();
		dbDef = DBDef.getInstance();
		FileManager.getInstance().init();
	}

	/**
	 * fonction qui execute la commande
	 * 
	 * @param commande
	 */
	public void processCommand(String commande) throws ReqException, SGBDException {
		StringTokenizer st = new StringTokenizer(commande);
		String action = st.nextToken();
		String nomRelation = "";

		switch (action) {
		case "create":
			int nombreColonnes = 0;
			ArrayList<String> typesDesColonnes = new ArrayList<>();

			nomRelation = st.nextToken();

			try { // On regarde si le nombre de colonnes indique est bien un nombre
				nombreColonnes = Integer.parseInt(st.nextToken());
			} catch (Exception e) { // si non, on lance une exception
				throw new ReqException("Le nombre de colonnes n'est pas un entier");
			}

			if (nombreColonnes != st.countTokens()) {
				throw new ReqException("Le nombre de colonne ne correspond pas au nombre de types de colonnes");
			}

			for (int i = 0; i < nombreColonnes && st.hasMoreTokens(); i++) {
				typesDesColonnes.add(st.nextToken());
			}

			// System.out.println(nomRelation);
			// System.out.println(nombreColonnes);
			// System.out.println(typesDesColonnes);
			createRelation(nomRelation, nombreColonnes, typesDesColonnes);
			break;
		case "insert":
			nomRelation = st.nextToken();
			ArrayList<String> contenuDesColonnes = new ArrayList<>();
			for (int i = 0; st.hasMoreTokens(); i++) {
				contenuDesColonnes.add(st.nextToken());
			}

			insertRecord(nomRelation, contenuDesColonnes);
			// Aucune gestion d'erreur, comme demande dans la consigne
			break;
		case "fill":
			nomRelation = st.nextToken();
			String nom_fichier = st.nextToken();
			fill(nomRelation, nom_fichier);
			break;
		case "selectall":
			nomRelation = st.nextToken();
			selectAll(nomRelation);
			break;
		case "select":
			nomRelation = st.nextToken();
			int indexColonne;
			try {
				indexColonne = Integer.parseInt(st.nextToken());
			} catch (Exception e) {
				throw new ReqException("Le numero de colonne n'est pas écrit correctement");
			}
			select(nomRelation, indexColonne, st.nextToken());
			break;
		case "clean":
			clean();
			break;
        case "join":
            String nomRel1 = st.nextToken();
            String nomRel2 = st.nextToken();
            int indiceCol1 = Integer.parseInt(st.nextToken());
            int indiceCol2 = Integer.parseInt(st.nextToken());
            join(nomRel1, nomRel2, indiceCol1, indiceCol2);
            break;
		case "debug": /// /// commande de debug poour tester des fonctions du SGBD /// ///
			switch (st.nextToken()) {
			case "createfile": /// /// commande qui cree une fichier vide dans le classpath /// ///
				String nomFichier = st.nextToken();
				File fichier = new File(Constantes.pathName + nomFichier);
				try {
					fichier.createNewFile();
				} catch (IOException e) {
					throw new SGBDException("impossible de creer un fichier dans " + Constantes.pathName);
				}
				break;
			case "framelist": /// /// commande pour afficher la liste des frames /// ///
				ArrayList<Frame> liste = BufferManager.getInstance().getFrames();
				for (int i = 0; i < liste.size(); i++) {
					try {
						System.out.println("--- --- ---");
						System.out.println("Pointeur Frame  : " + liste.get(i).toString());
						System.out.println("Pointeur PageId : " + liste.get(i).getPageId().toString());
						System.out.println("Page Idx        : " + liste.get(i).getPageId().getPageIdx());
						System.out.println("pin_count       : " + liste.get(i).getPin_count());
						System.out.println("date unpinned   : " + liste.get(i).getUnpinned().getTime());
						System.out.println("isDirty         : " + liste.get(i).isDirty());
						System.out.println("content         : " + liste.get(i).getContent().getInt());
						System.out.println("--- --- ---");

					} catch (Exception e) {
						System.out.println("probleme lors de l'affichage d'une frame");
					}
				}
				break;
			default:
				break;
			}
			break;
		default:
			throw new ReqException("Commande inconnue");
		}
	}



	private void clean() throws SGBDException {
		// Suppression de toutes les relations existantes
		// on fait par rapport au nombre de relation dans la reldef
		// ou par rapport a la taille de la liste dans fileManager
		for (int i = 0; i < FileManager.getInstance().getListe().size(); i++) {
			//System.out.println("Data " + i );
			File fileDelete = new File(Constantes.pathName + "Data_" + i + ".rf");

			//System.out.println(fileDelete.exists());

			if (fileDelete.delete()) {
				//System.out.println("Relation supprimé");
			} else {
				//System.out.println("Aucune relation supprimé");
			}

		}

        // Pour etre sûr que tous les fichiers Data soient bien supprimés
        for(int i = 0; i<10; i++){
            if(new File(Constantes.pathName + "Data_" + i + ".rf").exists()){
                try{
                    new File(Constantes.pathName + "Data_" + i + ".rf").delete();
                } catch(Exception e){ /* rien à faire */ }
            }
        }

		// Supppresion du catalogye
		File fileCatalogue = new File(Constantes.pathName + "Catalog.def");
		if (fileCatalogue.delete()) {
			//System.out.println("Le fichier Catalog.def a été supprimé");
		} else {
			//System.out.println("Aucune supression de Catalog.def");
		}

		// Tout a 0 dans le bufferManager
		BufferManager.getInstance().flushAll();
		// Tout a 0 dans le DBDef
		DBDef.getInstance().reset();
		// Liste a 0 dans FileManager
		FileManager.getInstance().reset();

		System.out.println("> La base de données à bien été remise à 0");
	}

	/**
	 * Fonction de debug
	 */
	public void afficher() {
		/*
		 * System.out.println(dbDef.getCompteurRel());
		 * System.out.println(dbDef.getListeDeRelDef());
		 * System.out.println(dbDef.getInstance());
		 */

	}

	public void finish() throws SGBDException {
		DBDef.getInstance().finish();
		BufferManager.getInstance().saveAll();
	}

	/**
	 * methode privee pour afficher proprement une liste de records
	 *
	 * @param listeRecords (la liste des records)
	 */
	private void affichageRecords(ArrayList<Record> listeRecords) {
		int nbRecords = 0;
		for (int i = 0; i < listeRecords.size(); i++) {
			ArrayList<String> record = listeRecords.get(i).getValues();
			for (int j = 0; j < record.size(); j++) {
				System.out.print(record.get(j) + "\t");
			}
			nbRecords++;
			System.out.println();
		}
		System.out.println("> Total records : " + nbRecords);
	}

	/**
	 * methode qui affiche tous les tuples dont la valeur de la colonne
	 * n°indexColonne est egale a valeurColonne
	 *
	 * @param nomRelation  (nom de la relation)
	 * @param indexColonne (numero de la colonne qu on veut filtrer)
	 * @param string       (valeur du filtre)
	 * @throws SGBDException
	 * @throws ReqException
	 */
	public void select(String nomRelation, int indexColonne, String string) throws SGBDException, ReqException {
		ArrayList<Record> listeRecords = FileManager.getInstance().getAllRecordsWithFilter(nomRelation, ((indexColonne == 0)?0:indexColonne-1), string);
		affichageRecords(listeRecords);
	}

	/**
	 * methode qui affiche tous les tuples d'une relation puis le nb le tuples
	 * affiches
	 *
	 * @param nomRelation (nom de la relation)
	 */
	public void selectAll(String nomRelation) throws SGBDException {
		ArrayList<Record> listeRecords = FileManager.getInstance().getAllRecords(nomRelation);
		affichageRecords(listeRecords);
	}

    /** methode de jointure
     *
     * @param nomRel1
     * @param nomRel2
     * @param indiceCol1
     * @param indiceCol2
     */
    public void join(String nomRel1, String nomRel2, int indiceCol1, int indiceCol2) throws SGBDException {
	    affichageRecords(FileManager.getInstance().join(nomRel1, nomRel2, indiceCol1, indiceCol2));
    }

	/**
	 * methode pour insérer des tuples dans une relation à partir d'un fichier
	 *
	 * @param nomRelation (le nom de la relation)
	 * @param nomFichier  (nom du fichier contenant les tuples)
	 * @throws SGBDException
	 */
	public void fill(String nomRelation, String nomFichier) throws SGBDException {
		File fichier = new File(nomFichier);
		if (!fichier.exists()) {
			throw new SGBDException("le fichier que vous demandez n'existe pas");
		}
		try (FileInputStream is = new FileInputStream(fichier);
				InputStreamReader isr = new InputStreamReader(is, "UTF-8")) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < fichier.length(); i++) {
				sb.append((char) isr.read());
			}
			StringTokenizer stFile = new StringTokenizer(sb.toString(), "\n");
			while (stFile.hasMoreTokens()) {
				StringTokenizer stRow = new StringTokenizer(stFile.nextToken(), ",");
				ArrayList<String> contenuDesColonnes = new ArrayList<>();
				while (stRow.hasMoreTokens()) {
					contenuDesColonnes.add(stRow.nextToken());
				}
				Record record = new Record();
				record.setValues(contenuDesColonnes);
				FileManager.getInstance().insertRecordInRelation(nomRelation, record);
			}
            System.out.println("> Tous les tuples du fichier on bien été insérés");
		} catch (IOException e) {
			throw new SGBDException("Impossible de lire le fichier fourni: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SGBDException("Erreur lors de l'execution de la methode fill() dans DBManager");
		}
	}

	/**
	 * la fonction pour inserer un tuple dans une relation
	 *
	 * @param nomRelation        (nom de la relation)
	 * @param contenuDesColonnes (contenu du tuple)
	 */
	public void insertRecord(String nomRelation, ArrayList<String> contenuDesColonnes) throws SGBDException {
		Record record = new Record();
		record.setValues(contenuDesColonnes);
		FileManager.getInstance().insertRecordInRelation(nomRelation, record);
        System.out.println("> Le tuple a bien été inséré");
	}

	/**
	 * la fonction pour creer la relation
	 * 
	 * @param nomRelation      (nom de la relation)
	 * @param nombreColonnes   (le nombre de colonnes)
	 * @param typesDesColonnes (un tableau avec le types de chaque colonnes)
	 */
	public void createRelation(String nomRelation, int nombreColonnes, ArrayList<String> typesDesColonnes)
			throws ReqException {

		int recordSize = 0;
		for (int i = 0; i < typesDesColonnes.size(); i++) {
			if (typesDesColonnes.get(i).equals("int")) {
				recordSize += Constantes.recordSize_int;
			} else if (typesDesColonnes.get(i).equals("float")) {
				recordSize += Constantes.recordSize_float;
			} else if (typesDesColonnes.get(i).substring(0, 6).equals("string")) {
				int x = Integer.parseInt(typesDesColonnes.get(i).substring(6));
				recordSize += x * Constantes.recordSize_stringx;
			}
		}
		if (Constantes.pageSize / recordSize == 0) {
			throw new ReqException(
					"La relation que vous tentez de creer prend trop de place par rapport a la taille max d'une page");
		}

		RelDef relation = new RelDef();
		relation.setNom(nomRelation);
		relation.setNbColonne(nombreColonnes);
		relation.setType(typesDesColonnes);
		relation.setRecordSize(recordSize);
		relation.setSlotCount(Constantes.pageSize /(recordSize + 1));
		relation.setFileIdx(dbDef.getCompteurRel());

		dbDef.addRelation(relation);

		FileManager.getInstance().createNewHeapFile(relation);

        System.out.println("> La relation a bien été créée");
	}
}
