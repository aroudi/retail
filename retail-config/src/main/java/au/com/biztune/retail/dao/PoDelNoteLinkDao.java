package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.PoDelNoteLink;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by arash on 1/06/2016.
 */
@Mapper
public interface PoDelNoteLinkDao {

    /**
     * link purchase order to delivery note.
     * @param poDelNoteLink poDelNoteLink
     */
    void insert (PoDelNoteLink poDelNoteLink);

    /**
     * get all DeliveryNoteLinked to Purchase Order.
     * @param pohId pohId
     * @return List of linked delivery notes.
     */
    List<PoDelNoteLink> getAllPoDelNoteLinkPerPohId(long pohId);

    /**
     * get all purchase orders linked to delivery note.
     * @param delNoteId delNoteId
     * @return List of PoDelNoteLink
     */
    List<PoDelNoteLink> getAllPoDelNoteLinkPerDelNoteId(long delNoteId);
}
