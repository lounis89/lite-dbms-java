package bdda;

import java.util.Scanner;

import exception.ReqException;
import exception.SGBDException;

public class Main {
	public static void main(String[] args) throws SGBDException {
		DBManager db = DBManager.getInstance();
		db.init();
		System.out.println("**************************************************************");
		System.out.println("*                                                            *");
		System.out.println("*                         Mini SGBDR                         *");
		System.out.println("*                                                            *");
		System.out.println("**************************************************************");
		System.out.println("*                                                            *");
		System.out.println("*                    LISTE DES COMMANDES:                    *");
		System.out.println("*                                                            *");
		System.out.println("*  Création d'une table:                                     *");
		System.out.println("*    - create <nom_rel> <nb_colonnes> <type_nième_col>       *");
		System.out.println("*  Insertion de tuples:                                      *");
		System.out.println("*    - insert <nom_rel> <val_de_la_nième_col>                *");
		System.out.println("*    - fill <nom_rel> <nom_fichier>                          *");
		System.out.println("*  Affichage de tuples:                                      *");
		System.out.println("*    - selectall <nom_rel>                                   *");
		System.out.println("*    - select <nom_rel> <num_col_filtrée> <valeur_filtre>    *");
		System.out.println("*    - join <nom_rel_1> <nom_rel_2> <col_rel_1> <col_rel_2>  *");
		System.out.println("*  Remise à 0 de la BDD:                                     *");
		System.out.println("*    - clean                                                 *");
		System.out.println("*  Quitter la BDD:                                           *");
		System.out.println("*    - exit                                                  *");
		System.out.println("*                                                            *");
		System.out.println("**************************************************************");


		String commande = "";
		do {

			try {
				System.out.println("> Veuillez entrer votre commande");
				Scanner sc = new Scanner(System.in);
				commande = sc.nextLine();

				switch (commande) {
				case "exit":
					db.finish();
					break;
				default:
					db.processCommand(commande);
					break;
				}
				db.afficher();
			} catch (ReqException e) {
				e.printStackTrace();
			} catch (SGBDException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} while (!commande.equals("exit"));
		System.out.println("> Au revoir");
	}

}
