package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

import bdda.BufferManager;
import bdda.Constantes;
import bdda.DiskManager;
import bdda.Frame;
import bdda.PageId;
import exception.SGBDException;

class BufferTest {

	@Test
	void test() {
		DiskManager dm = DiskManager.getInstance();
		try {
			dm.createFile(10);

			PageId pid = new PageId();
			PageId pid2 = new PageId();
			PageId pid3 = new PageId();

			dm.addPage(10, pid);
			System.out.println(pid.getPageIdx());
			assertTrue(pid.getFileIdx() == 10 && pid.getPageIdx() == 0);
			dm.addPage(10, pid2);
			assertTrue(pid2.getFileIdx() == 10 && pid2.getPageIdx() == 1);
			dm.addPage(10, pid3);
			assertTrue(pid3.getFileIdx() == 10 && pid3.getPageIdx() == 2);

			BufferManager.getInstance().getPage(pid);		
			BufferManager.getInstance().getPage(pid2);
			BufferManager.getInstance().getPage(pid);
			BufferManager.getInstance().freePage(pid2, false);
			BufferManager.getInstance().freePage(pid, true);
			BufferManager.getInstance().getPage(pid3);
			
			Frame frame1 = BufferManager.getInstance().getFrames().get(0);
			assertEquals(pid.getFileIdx(), frame1.getPageId().getFileIdx());
			assertEquals(pid.getPageIdx(), frame1.getPageId().getPageIdx());
			
			
			Frame frame2 = BufferManager.getInstance().getFrames().get(1);
			assertEquals(pid3.getFileIdx(), frame2.getPageId().getFileIdx());
			assertEquals(pid3.getPageIdx(), frame2.getPageId().getPageIdx());
			
			

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SGBDException e) {
			e.printStackTrace();
		}
	}

}
