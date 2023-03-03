package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import bdda.DBDef;
import bdda.FileManager;
import bdda.RelDef;

class FileManagerTest {

	@Test
	public void testFileManager() {
		RelDef relation1 = new RelDef();
		relation1.setNom("emilie");
		DBDef.getInstance().addRelation(relation1);
		FileManager.getInstance().init();
		assertEquals(relation1.getNom(),FileManager.getInstance().getListe().get(0).getPointeur().getNom());
		
		RelDef relation2 = new RelDef();
		relation2.setNom("TOTO");
		DBDef.getInstance().addRelation(relation2);
		FileManager.getInstance().createNewHeapFile(relation2);
		assertEquals(relation2.getNom(),FileManager.getInstance().getListe().get(1).getPointeur().getNom());

	}

}
