package bdda;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import exception.ReqException;
import exception.SGBDException;

public class DiskManager {

	private static final DiskManager diskManager = new DiskManager();
	private ByteBuffer data;

	private DiskManager() {
	}

	public static DiskManager getInstance() {
		return diskManager;
	}

	/**
	 * Cette fonction permet de creer un niveau fichier dont le nom est :
	 * "Data_(identifiantDuFichier).rf"
	 * 
	 * @param iFileIdx identifiant du fichier qu'on creer
	 * @throws IOException
	 * @throws SGBDException
	 */
	public void createFile(int iFileIdx) throws IOException, SGBDException {
		if (iFileIdx < 0)
			throw new SGBDException("L'id du fichier doit etre superieur a 0");

		File file = new File(Constantes.pathName + "Data_" + iFileIdx + ".rf");
		System.out.println(file.getAbsolutePath());
		if (!file.exists()) {
			file.createNewFile();
		}
	}

	/**
	 * Cette fonction ajoute une nouvelle page de taille 4ko a la fin du fichier (
	 * pour l'instant)
	 * 
	 * @param iFileIdx identifiant du fichier
	 * @param oPageId  de type PageId qui comprend le numero de la page et
	 *                 l'identifant du fichier
	 * @throws IOException
	 */
	public void addPage(int iFileIdx, PageId oPageId) throws IOException {
		// ajouter la page
		try (RandomAccessFile rf = new RandomAccessFile(Constantes.pathName + "Data_" + iFileIdx + ".rf", "rw")) {
			byte[] tab = new byte[Constantes.pageSize];
			for (Byte b : tab) {
				b = 0;
			}

			ByteBuffer bf = ByteBuffer.wrap(tab);

			// seek permet de connaitre la derniere position du fichier
			oPageId.setPageIdx((int) (rf.length() / Constantes.pageSize));
			rf.seek(rf.length());

			// On ecrit le contenu du buffer
			rf.write(bf.array());
			oPageId.setFileIdx(iFileIdx);
			rf.close();
		}

	}

	/**
	 * Permet de lire une page (input)
	 * 
	 * @param iPageId
	 * @throws IOException
	 */
	public void readPage(PageId iPageId, ByteBuffer iBuffer) throws IOException {
		try (RandomAccessFile readFile = new RandomAccessFile(
				Constantes.pathName + "Data_" + iPageId.getFileIdx() + ".rf", "r")) {

			FileChannel fc = readFile.getChannel();
			long position = iPageId.getPageIdx() * Constantes.pageSize;
			fc.position(position);
			iBuffer.clear();
			fc.read(iBuffer);
			fc.close();
			readFile.close();
		}

	}

	/**
	 * Permet d'ecrire dans une page (output)
	 * 
	 * @param iPageId
	 * @param oBuffer
	 * @throws IOException
	 */
	public void writePage(PageId iPageId, ByteBuffer oBuffer) throws IOException {
		// On recupere le fichier qui contient la page qu'on veut modifier

		try (RandomAccessFile writeFile = new RandomAccessFile(
				Constantes.pathName + "Data_" + iPageId.getFileIdx() + ".rf", "rw")) {
			FileChannel fc = writeFile.getChannel();
			long position = iPageId.getPageIdx() * Constantes.pageSize;
			fc.position(position);
			oBuffer.flip();
			fc.write(oBuffer);

			/*
			while(oBuffer.hasRemaining()){
				fc.write(oBuffer);
			}
			*/

			fc.close();
			writeFile.close();
		}
	}

}
