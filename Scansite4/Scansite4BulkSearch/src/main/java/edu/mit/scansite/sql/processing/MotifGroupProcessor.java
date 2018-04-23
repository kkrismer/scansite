package edu.mit.scansite.sql.processing;

import edu.mit.scansite.model.Motif;
import edu.mit.scansite.model.MotifGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static edu.mit.scansite.sql.constants.TableMotifGroups.COLUMN_MOTIF_GROUP_DISPLAY_NAME;
import static edu.mit.scansite.sql.constants.TableMotifGroups.COLUMN_MOTIF_GROUP_SHORT_NAME;
import static edu.mit.scansite.sql.constants.TableMotifs.*;

public class MotifGroupProcessor {
    private Logger logger = LoggerFactory.getLogger(MotifGroupProcessor.class);

    public List<MotifGroup> process(PreparedStatement stmt) {
        try {
            ResultSet rs = stmt.executeQuery();
            List<MotifGroup> motifGroups = new ArrayList<>();

            while (rs.next()) {
                MotifGroup motifGroup = new MotifGroup();
                if (ProcessingUtils.hasColumn(rs, COLUMN_MOTIF_GROUP_ID)) {
                    motifGroup.setMotifGroupId(rs.getInt(COLUMN_MOTIF_GROUP_ID));
                }
                if (ProcessingUtils.hasColumn(rs, COLUMN_MOTIF_GROUP_DISPLAY_NAME)) {
                    motifGroup.setMotifGroupDisplayName(rs.getString(COLUMN_MOTIF_GROUP_DISPLAY_NAME));
                }
                if (ProcessingUtils.hasColumn(rs, COLUMN_MOTIF_GROUP_SHORT_NAME)) {
                    motifGroup.setMotifGroupShortName(rs.getString(COLUMN_MOTIF_GROUP_SHORT_NAME));
                }

                motifGroups.add(motifGroup);
            }

            return motifGroups;
        } catch (SQLException e) {
            logger.error("Could not retrieve Motif Group values from database!", e);
        }
        return null;
    }

}
