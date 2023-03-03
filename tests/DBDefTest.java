package tests;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import bdda.Constantes;
import bdda.DBDef;
import exception.SGBDException;

class DBDefTest {

	@Test
	void test() {
		DBDef test = DBDef.getInstance();
		try {
			File file = new File(Constantes.pathName+"Catalog.def");
			test.init();
			Assertions.assertTrue(file.exists());
			
		} catch (SGBDException e) {
			e.printStackTrace();
		}
	}

}
