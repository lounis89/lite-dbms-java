package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import bdda.Constantes;
import bdda.DataPage;
import bdda.HeaderPageInfo;

class HeaderPageInfoTest {
	private ByteBuffer btest;

	@Test
	void testReadBufferWriteBuffer() {
		ByteBuffer bf = ByteBuffer.allocate(Constantes.pageSize);
		Random rn = new Random();
		int dataPageCount = rn.nextInt(50) + 1;
		int tab[] = rn.ints(dataPageCount * 2, 1, 11).toArray();
		System.out.println(dataPageCount);
		for (int i : tab) {
			System.out.print(i + " ");
		}
		System.out.println();
		bf.putInt(dataPageCount);
		for (int i = 0; i < tab.length; i++) {
			bf.putInt(tab[i]);
		}

		HeaderPageInfo test = new HeaderPageInfo();

		test.readFromBuffer(bf);
		assertTrue(dataPageCount == test.getDataPageCount());
		assertTrue(test.getDataPageCount() == test.getListePages().size());
		assertTrue(dataPageCount == test.getListePages().size());

		bf.position(4);
		for (DataPage d : test.getListePages()) {
			assertEquals(bf.getInt(), d.getPageIdx());
			assertEquals(bf.getInt(), d.getFreeSlots());
		}

		for (DataPage d : test.getListePages()) {
			System.out.print("[" + d.getPageIdx() + " " + d.getFreeSlots() + "]");
		}

		ByteBuffer vide = ByteBuffer.allocate(Constantes.pageSize);
		test.writeToBuffer(vide);
		assertEquals(bf.getInt(0), vide.getInt(0));

		for (int i = 0; i < test.getDataPageCount(); i++) {
			assertEquals(bf.getInt(), vide.getInt());
		}

	}

}
