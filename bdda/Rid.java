package bdda;

public class Rid {

	private PageId pageId;
	private int slotIdx;
	
	public Rid(PageId pid, int slotId) {
		this.pageId = pid;
		this.slotIdx = slotId;
	}

	public PageId getPageId() {
		return pageId;
	}

	public void setPageId(PageId pageId) {
		this.pageId = pageId;
	}

	public int getSlotIdx() {
		return slotIdx;
	}

	public void setSlotIdx(int slotIdx) {
		this.slotIdx = slotIdx;
	}

}
