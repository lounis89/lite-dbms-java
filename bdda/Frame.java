package bdda;

import exception.SGBDException;

import java.nio.ByteBuffer;
import java.util.Date;

public class Frame {
    private PageId pageId;
    private boolean dirty;
    private int pin_count;
    public int ref_bit;
    private Date unpinned; // la date à laquelle pin_count est passé à 0 (LRU)
    private ByteBuffer content; // contenu de la frame (du cadre qui contient la page)

    public Frame(){
        this.pageId = null;
        this.dirty = false;
        this.unpinned = new Date();
        this.ref_bit = 0;
        this.pin_count = 0; // Car si on vient de la creer, y a forcément 1 personne qui travaille dessus
        this.content = ByteBuffer.allocateDirect(Constantes.pageSize);
    }

    public void resetFrame(){
        pageId = null;
        dirty = false;
        pin_count = 0;
        ref_bit = 0;
        unpinned = new Date();
        // TODO on ne met pas un nouveau bytebuffer car ça pourrait prendre trrop de mémoire comme l'a dit la prof
    }

    public void incrementerPinCount(){
        pin_count++;
    }

    public void decrementerPinCount() throws SGBDException {
        if(pin_count>0){
            pin_count--;
            if(pin_count == 0){
                unpinned = new Date();
                ref_bit = 1;
            }
        } else {
            throw new SGBDException("Le pin_count de la frame (PageIdx : " + pageId.getPageIdx() + " - FileIdx : " + pageId.getFileIdx() + ") doit etre décrémenté alors qu'il est déjà nul");
        }
    }

    // Getter

    public PageId getPageId() {
        return pageId;
    }

    public boolean isDirty() {
        return dirty;
    }

    public int getPin_count() {
        return pin_count;
    }

    public ByteBuffer getContent() { return content; }

    public Date getUnpinned() { return unpinned; }

    // Setter

    public void setPageId(PageId pageId) {
        this.pageId = pageId;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void addDirty(boolean dirty){
        if(!this.dirty){
            this.dirty = dirty;
        }
    }

    public void setPin_count(int pin_count) {
        this.pin_count = pin_count;
        if(pin_count == 0){
            unpinned = new Date();
            ref_bit = 1;
        }
    }

    public void setContent(ByteBuffer content) { this.content = content; }

    public int getRef_bit() {
        return ref_bit;
    }

    public void setRef_bit(int i) {
        this.ref_bit = ref_bit;
    }
}
