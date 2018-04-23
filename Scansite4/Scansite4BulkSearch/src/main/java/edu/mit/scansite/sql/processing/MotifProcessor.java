package edu.mit.scansite.sql.processing;

import edu.mit.scansite.model.Motif;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static edu.mit.scansite.sql.constants.TableMotifs.*;

public class MotifProcessor {
    private Logger logger = LoggerFactory.getLogger(MotifProcessor.class);

    public List<Motif> process(PreparedStatement stmt) {
        try {
            ResultSet rs = stmt.executeQuery();
            List<Motif> motifs = new ArrayList<>();

            while (rs.next()) {
                Motif motif = new Motif();
                if (ProcessingUtils.hasColumn(rs, COLUMN_MOTIF_ID)) {
                    motif.setMotifId(rs.getInt(COLUMN_MOTIF_ID));
                }
                if (ProcessingUtils.hasColumn(rs, COLUMN_MOTIF_DISPLAY_NAME)) {
                    motif.setMotifDisplayName(rs.getString(COLUMN_MOTIF_DISPLAY_NAME));
                }
                if (ProcessingUtils.hasColumn(rs, COLUMN_MOTIF_SHORT_NAME)) {
                    motif.setMotifShortName(rs.getString(COLUMN_MOTIF_SHORT_NAME));
                }
                if (ProcessingUtils.hasColumn(rs, COLUMN_MOTIF_GROUP_ID)) {
                    motif.setMotifGroupId(rs.getInt(COLUMN_MOTIF_GROUP_ID));
                }
                if (ProcessingUtils.hasColumn(rs, COLUMN_USER_EMAIL)) {
                    motif.setUserEmail(rs.getString(COLUMN_USER_EMAIL));
                }
                if (ProcessingUtils.hasColumn(rs, COLUMN_MOTIF_OPTIMAL_SCORE)) {
                    motif.setMotifOptimalScore(rs.getDouble(COLUMN_MOTIF_OPTIMAL_SCORE));
                }
                if (ProcessingUtils.hasColumn(rs, COLUMN_MOTIF_IS_PUBLIC)) {
                    motif.setMotifIsPublic(rs.getBoolean(COLUMN_MOTIF_IS_PUBLIC));
                }
                if (ProcessingUtils.hasColumn(rs, COLUMN_MOTIF_CLASS)) {
                    motif.setMotifClass(rs.getString(COLUMN_MOTIF_CLASS));
                }

                motifs.add(motif);
            }

            return motifs;
        } catch (SQLException e) {
            logger.error("Could not retrieve Motif values from database!", e);
        }
        return null;
    }

}
