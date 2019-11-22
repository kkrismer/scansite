package edu.mit.scansite.server.dataaccess;

import java.util.List;
import java.util.Set;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.Identifier;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.MotifSelection;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Konstantin Krismer
 */
public interface MotifDao extends Dao {

	public abstract int addMotif(Motif motif) throws DataAccessException;

	/**
	 * Equivalent to calling getAll(null, motifClass, user, false).
	 * 
	 * @param motifClass
	 *            A motif class. If no class is given (NULL), MAMMALIAN is used
	 *            as default value.
	 * @param user
	 * 			  If user == null: return only public motifs,
	 * 			  if user.userGroup == ADVANCEDUSER || user.userGroup == COLLABORATOR: return public motifs and motifs of this user
	 * 			  if user.userGroup == ADMIN: return all motifs
	 *            from the given class that are in the given set of matching
	 *            short names if one is given.
	 * @return All non-/public motifs from the given class.
	 * @throws DataAccessException
	 */
	public abstract List<Motif> getAll(MotifClass motifClass, User user)
			throws DataAccessException;

	/**
	 * Equivalent to calling getAll(null, motifClass, user, onlyUserMotifs).
	 * 
	 * @param motifClass
	 *            A motif class. If no class is given (NULL), MAMMALIAN is used
	 *            as default value.
	 * @param user
	 * 			  If user == null: return only public motifs,
	 * 			  if user.userGroup == ADVANCEDUSER || user.userGroup == COLLABORATOR: return public motifs and motifs of this user
	 * 			  if user.userGroup == ADMIN: return all motifs
	 *            from the given class that are in the given set of matching
	 *            short names if one is given.
	 * @param onlyUserMotifs only returns motifs added by user
	 * @return All non-/public motifs from the given class.
	 * @throws DataAccessException
	 */
	public abstract List<Motif> getAll(MotifClass motifClass, User user, boolean onlyUserMotifs)
			throws DataAccessException;

	/**
	 * Gets all motifs with the given short names or ALL motifs, if no short
	 * names are given.
	 * 
	 * @param motifNicks
	 *            a set of motif nick names, or NULL if all short names are
	 *            wanted for the given class.
	 * @param motifClass
	 *            A motif class. If no class is given (NULL), MAMMALIAN is used
	 *            as default value.
	 * @param user
	 * 			  If user == null: return only public motifs,
	 * 			  if user.userGroup == ADVANCEDUSER || user.userGroup == COLLABORATOR: return public motifs and motifs of this user
	 * 			  if user.userGroup == ADMIN: return all motifs
	 *            from the given class that are in the given set of matching
	 *            short names if one is given.
	 * @return A list of motifs matching the given parameters.
	 * @throws DataAccessException
	 */
	public abstract List<Motif> getAll(Set<String> motifNicks,
			MotifClass motifClass, User user)
			throws DataAccessException;

	/**
	 * Gets all motifs with the given short names or ALL motifs, if no short
	 * names are given.
	 * 
	 * @param motifNicks
	 *            a set of motif nick names, or NULL if all short names are
	 *            wanted for the given class.
	 * @param motifClass
	 *            A motif class. If no class is given (NULL), MAMMALIAN is used
	 *            as default value.
	 * @param user
	 * 			  If user == null: return only public motifs,
	 * 			  if user.userGroup == ADVANCEDUSER || user.userGroup == COLLABORATOR: return public motifs and motifs of this user
	 * 			  if user.userGroup == ADMIN: return all motifs
	 *            from the given class that are in the given set of matching
	 *            short names if one is given.
	 * @param onlyUserMotifs only returns motifs added by user
	 * @return A list of motifs matching the given parameters.
	 * @throws DataAccessException
	 */
	public abstract List<Motif> getAll(Set<String> motifNicks,
			MotifClass motifClass, User user, boolean onlyUserMotifs)
			throws DataAccessException;

	public abstract List<Motif> getSelectedMotifs(
			MotifSelection motifSelection, User user)
			throws DataAccessException;

	public abstract Motif getByShortName(String shortName)
			throws DataAccessException;

	public abstract List<Motif> getByGroup(LightWeightMotifGroup group,
			MotifClass motifClass, User user)
			throws DataAccessException;

	/**
	 * This method only updates a motif main data like display name, short name,
	 * motif class, motif group, visibility, and submitter, however, not the
	 * motif's data
	 * 
	 * @param motif
	 *            A motif with a valid id.
	 * @throws DataAccessException
	 */
	public abstract void updateMotif(Motif motif) throws DataAccessException;

	public abstract void deleteMotif(int id) throws DataAccessException;

	public abstract Motif getById(int motifId) throws DataAccessException;

	public abstract void updateMotifData(Motif motif)
			throws DataAccessException;

	public abstract Integer getNumberOfMotifs(MotifClass motifClass,
			boolean getOnlyPublic) throws DataAccessException;

	public abstract int addMotifIdentifier(Motif motif, Identifier identifier)
			throws DataAccessException;

}