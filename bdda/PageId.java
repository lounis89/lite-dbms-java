package bdda;

public class PageId {
	private int fileIdx;
	private int pageIdx;

	public PageId(int fileId, int pageId) {
		this.fileIdx = fileId;
		this.pageIdx = pageId;
	}

	public PageId() {
		this.fileIdx = 0;
		this.pageIdx = 0;
	}

	public int getFileIdx() {
		return fileIdx;
	}

	public void setFileIdx(int fileIdx) {
		this.fileIdx = fileIdx;
	}

	public int getPageIdx() {
		return pageIdx;
	}

	public void setPageIdx(int pageIdx) {
		this.pageIdx = pageIdx;
	}
}
