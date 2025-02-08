package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

import bdda.Constantes;
import bdda.DiskManager;
import bdda.PageId;
import exception.SGBDException;

class DiskManagerTest {

	@Test
	void test() {
		DiskManager dm = DiskManager.getInstance();
		try {
			dm.createFile(11);

			PageId pid = new PageId();
			PageId pid2 = new PageId();

			dm.addPage(11, pid);
			assertEquals(11, pid.getFileIdx());
			assertEquals(0, pid.getPageIdx());
			dm.addPage(11, pid2);
			assertEquals(11, pid2.getFileIdx());
			assertEquals(1, pid2.getPageIdx());

			ByteBuffer bf = ByteBuffer.allocateDirect(Constantes.pageSize);
			while (bf.hasRemaining()) {
				bf.putInt(1);
			}

			bf.rewind();
			while (bf.hasRemaining()) {
				System.out.print(bf.getInt());
			}

			System.out.println();

			ByteBuffer copie = ByteBuffer.allocateDirect(Constantes.pageSize);
			dm.writePage(pid, bf);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			dm.readPage(pid, copie);

			copie.rewind();
			while (copie.hasRemaining()) {
				System.out.print(copie.getInt());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SGBDException e) {
			e.printStackTrace();
		}

	}

}
