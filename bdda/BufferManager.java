package bdda;

import exception.SGBDException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;

public class BufferManager {
    private ArrayList<Frame> frames;

    //Pour avoir une unique instance de BufferManager
    private static final BufferManager instance = new BufferManager();

    /** Constructeur de cette classe qui cree un tableau de frames
     */
    private BufferManager() {
        this.frames = new ArrayList<Frame>();
        for(int i = 0; i<Constantes.frameCount; i++){
            this.frames.add(new Frame());
        }
    }

    public static BufferManager getInstance() {
        return instance;
    }

    /** Methode creee pour comprendre ce qu'il se passe et corriger des bugs
     *
     * @return (liste de toutes les frames)
     */
    public ArrayList<Frame> getFrames() {
        return frames;
    }

    // il faut toujours incrementer le pin_count

    /** Retourne un des buffers qui stockent le contenu d’une page dans une des cases
     * @param iPageId l'ID de la page en question
     *
     * @return un des buffer qui stockent le contenu de la page
     */
    public ByteBuffer getPage(PageId iPageId) throws SGBDException {
        // on regarde si la page recherchee n'est pas déjà dans le tableau de frames
        for(Frame frame: frames) {
            // Si on trouve une page avec le meme pageID et fileID
            if((frame.getPageId() != null) && (frame.getPageId().getPageIdx() == iPageId.getPageIdx()) && (frame.getPageId().getFileIdx() == iPageId.getFileIdx())){
                frame.incrementerPinCount();
                return frame.getContent();
            }
        }

        // si non, on cherche la page dans les fichiers et on la met dans le tableau
        // si il reste de la place
        for(Frame frame: frames){
            // Si la frame sur laquelle on se trouve est libre
            if(frame.getPageId() == null){
                // On remplis la frame
                frame.setPageId(iPageId);
                frame.incrementerPinCount();
                try {
                    DiskManager.getInstance().readPage(iPageId, frame.getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return frame.getContent();
            }
        }



        // s'il n'en reste pas, on execute le LRU pour savoir quelle page remplacer
        // initialisation de la position de la page à remplacer
        int indexOldestDate = -1;
        // initialisation de la date de la page la plus récente
        // ==> Oui là ça correspond à aucune page mais c'est pour que n'importe quelle page soit plus vielle que cette date
        // ==> Oui c'est bien la consigne qui demande qu'on utilise la date
        // ==> La prof a regardé au TD5 elle a dit OK, puis au TD6 et elle aussi dit OK
        Date oldestDate = new Date();

        // On parcourt le tableau des frames
        for(int i = 0; i<frames.size(); i++){
            // Si la frame n'est pas en cours d'utilisation (pin_count == 0)
            //   ET que soit elle est la première a avoir été visitée, soit parmis toutes les pages qui ont déjà été visitées, elle est celle dont la date à laquelle elle a fini d'etre utilisée est la plus ancienne
            // ==> Oui la prof a regardé et a dit OK
            if((frames.get(i).getPin_count() == 0) && ((indexOldestDate == -1) || (frames.get(i).getUnpinned().getTime() < oldestDate.getTime()))){
                // on memorise la position de cette page
                indexOldestDate = i;
                // on memorise la date à laquelle elle a fini d'etre utilisée
                oldestDate = frames.get(i).getUnpinned();
            }
        }


        // Si il reste de la place
        if(indexOldestDate >= 0){
            // Si il faut enregistrer des modifications dans la page
            if(frames.get(indexOldestDate).isDirty()){
                try {
                    DiskManager.getInstance().writePage(frames.get(indexOldestDate).getPageId(), frames.get(indexOldestDate).getContent());
                    //System.out.println("ecriture page n°" + frames.get(indexOldestDate).getPageId().getPageIdx());
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new SGBDException("erreur d'E/S à l'ecriture d'une page");
                }
            }

            // on reset la frame à 0
            frames.get(indexOldestDate).resetFrame();

            // On installe la nouvelle page dans la frame
            frames.get(indexOldestDate).setPageId(iPageId);
            frames.get(indexOldestDate).incrementerPinCount();
            // On extrait le contenu de la page
            try {
                DiskManager.getInstance().readPage(iPageId, frames.get(indexOldestDate).getContent());
            } catch (IOException e) {
                e.printStackTrace();
                throw new SGBDException("erreur d'E/S à la lecture du contenu d'une page");
            }

            return frames.get(indexOldestDate).getContent();

        } else { // Traitement d'erreur
            // On va regarder si le problème vient de l'algo LRU qui n'arriverait pas à trouver de page à supp alors qu'il y en a
            // ou si c'est une autre classe qui utilise mal le BufferManager
            int nb_pages_utilisees = 0;
            for(Frame frame: frames){
                if(frame.getPin_count() > 0) nb_pages_utilisees++;
            }
            if(nb_pages_utilisees >= Constantes.frameCount){
                throw new SGBDException("Il y a trop de getPage et pas assez de freePage");
            } else {
                throw new SGBDException("erreur sur l'algo de LRU");
            }
        }
    }

    /** Methode qui sauvegarde toutes les pages qui sont dans le tableau de frame du BufferManager
     *
     * @throws SGBDException
     */
    public void saveAll() throws SGBDException {
        for(Frame frame: frames){
            if(frame.getPageId() != null){
                try {
                    DiskManager.getInstance().writePage(frame.getPageId(), frame.getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /** Liberer toutes les pages du tableau de frames
     *
     * @throws SGBDException
     */
    public void flushAll() throws SGBDException {
        for(Frame frame: frames){
            if(frame.getPageId() != null){
                frame.setDirty(false);
                frame.setPin_count(0);
            }
        }
    }

    /** Liberer une page
     * @param pageId l'index de la frame qui contient la page a virer
     * @param iIsDirty TRUE pour change FALSE pour inchange
     */
    public void freePage(PageId pageId, boolean iIsDirty) throws SGBDException {
        for(Frame frame: frames){
            if((frame.getPageId() != null) && (frame.getPageId().getPageIdx() == pageId.getPageIdx()) && (frame.getPageId().getFileIdx() == pageId.getFileIdx())){
                // si la page n'est pas encore dirty elle le devient, sinon elle le reste
                frame.addDirty(iIsDirty);
                frame.decrementerPinCount();
            }
        }
    }
}
