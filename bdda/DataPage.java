package bdda;

public class DataPage {
	private int pageIdx;
	private int freeSlots;

	public int getPageIdx() {
		return pageIdx;
	}

	public void setPageIdx(int pageIdx) {
		this.pageIdx = pageIdx;
	}

	public int getFreeSlots() {
		return freeSlots;
	}

	public void setFreeSlots(int freeSlots) {
		this.freeSlots = freeSlots;
	}

	public DataPage(int pageIdx, int freeSlots) {
		this.pageIdx = pageIdx;
		this.freeSlots = freeSlots;
	}

}
