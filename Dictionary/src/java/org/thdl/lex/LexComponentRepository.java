package org.thdl.lex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.thdl.lex.component.ILexComponent;
import org.thdl.lex.component.ITerm;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 1, 2003
 */
public class LexComponentRepository {

	/**
	 * Description of the Field
	 */
	public final static String EXACT = "exact";

	/**
	 * Description of the Field
	 */
	public final static String STARTS_WITH = "startsWith";

	/**
	 * Description of the Field
	 */
	public final static String ANYWHERE = "anywhere";

	private static long start;

	private static long lastUpdate = now();

	/**
	 * Sets the lastUpdate attribute of the LexComponentRepository class
	 * 
	 * @param last
	 *            The new lastUpdate value
	 */
	public static void setLastUpdate(long last) {
		lastUpdate = last;
	}

	/**
	 * Gets the lastUpdate attribute of the LexComponentRepository class
	 * 
	 * @return The lastUpdate value
	 */
	public static long getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * Sets the start attribute of the LexComponentRepository object
	 * 
	 * @param startTime
	 *            The new start value
	 * @since
	 */
	private static void setStart(long startTime) {
		Logger logger = Logger.getLogger("org.thdl.lex");
		logger.debug("Query start time: " + new java.util.Date(startTime));
		start = startTime;
	}

	/**
	 * Gets the start attribute of the LexComponentRepository object
	 * 
	 * @return The start value
	 * @since
	 */
	private static long getStart() {
		return start;
	}

	/**
	 * Gets the duration attribute of the LexComponentRepository class
	 * 
	 * @return The duration value
	 */
	private static long getDuration() {
		long duration = now() - getStart();

		Logger logger = Logger.getLogger("org.thdl.lex");
		logger.debug("Query finish: " + new java.util.Date(now()));
		logger.debug("Query duration in ms: " + duration);
		logger.info("Query duration: " + duration / 1000 + " seconds.");
		return duration;
	}

	/**
	 * Gets the session attribute of the LexComponentRepository class
	 * 
	 * @return The session value
	 * @exception HibernateException
	 *                Description of Exception
	 * @since
	 */
	protected static Session getSession() throws HibernateException {
		Session session = HibernateSession.currentSession();
		if (!session.isConnected()) {
			//session.reconnect();
		}
		return session;
	}

	/**
	 * Description of the Method
	 * 
	 * @exception LexRepositoryException
	 *                Description of the Exception
	 */
	protected static void beginTransaction() throws LexRepositoryException {
		try {
			HibernateTransaction.beginTransaction();
		} catch (HibernateException he) {
			throw new LexRepositoryException(he);
		}

	}

	/**
	 * Description of the Method
	 * 
	 * @param commit
	 *            Description of Parameter
	 * @exception LexRepositoryException
	 *                Description of the Exception
	 * @since
	 */
	protected static void endTransaction(boolean commit)
			throws LexRepositoryException {
		try {
			HibernateTransaction.endTransaction(commit);
		} catch (HibernateException he) {
			throw new LexRepositoryException(he);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Returned Value
	 * @since
	 */
	private static long now() {
		return System.currentTimeMillis();
	}

	/**
	 * Description of the Method
	 * 
	 * @param comp
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 * @exception LexRepositoryException
	 *                Description of Exception
	 * @since
	 */
	private static ITerm assertTerm(ILexComponent comp)
			throws LexRepositoryException {
		ITerm term = null;
		try {
			term = (ITerm) comp;
		} catch (Exception e) {
			throw new LexRepositoryException("Query Component was not a term.");
		}
		return term;
	}

	/**
	 * Queries the database for Terms that start with the string in the term
	 * property of the queryComponent. Sets entry property the first hit
	 * returned.
	 * 
	 * @param lexQuery
	 *            Description of Parameter
	 * @exception LexRepositoryException
	 *                Description of Exception
	 * @since
	 */
	public static void findTermsByTerm(LexQuery lexQuery)
			throws LexRepositoryException {
		setStart(now());
		beginTransaction();
		ITerm term = assertTerm(lexQuery.getQueryComponent());
		if (null == term.getTerm()) {
			throw new LexRepositoryException("Query Component term was null.");
		}

		Query query = null;
		Iterator it = null;

		/*
		 * String termForQuery = LexUtilities.hqlEscape( term.getTerm() );
		 * LexLogger.debug( "Escaped term string: " + termForQuery );
		 */
		String termForQuery = term.getTerm();

		if (lexQuery.getFindMode().equals(LexComponentRepository.STARTS_WITH)) {
			termForQuery = termForQuery + "%";
		} else if (lexQuery.getFindMode().equals(
				LexComponentRepository.ANYWHERE)) {
			termForQuery = "%" + termForQuery + "%";
		}

		String queryString = " FROM org.thdl.lex.component.ITerm as term WHERE term.term like :term AND term.deleted=0 ORDER BY term";
		try {
			query = getSession().createQuery(queryString);
			query.setString("term", termForQuery);

		} catch (HibernateException he) {
			throw new LexRepositoryException(he);
		}

		try {
			it = query.iterate();
		} catch (HibernateException he) {
			throw new LexRepositoryException(he);
		}

		if (it.hasNext()) {
			term = (ITerm) it.next();
			lexQuery.setEntry(term);
			lexQuery.getResults().clear();
			lexQuery.getResults().put(term.getMetaId(), term.getTerm());
		} else {
			lexQuery.setEntry(null);
			lexQuery.getResults().clear();
		}
		while (it.hasNext()) {
			term = (ITerm) it.next();
			lexQuery.getResults().put(term.getMetaId(), term.getTerm());
		}
		endTransaction(false);
		lexQuery.setDuration(getDuration());
	}

	/**
	 * Description of the Method
	 * 
	 * @param pk
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception LexRepositoryException
	 *                Description of the Exception
	 */
	public static ITerm findTermByPk(Integer pk) throws LexRepositoryException {
		ITerm term = null;

		beginTransaction();
		String queryString = " FROM org.thdl.lex.component.ITerm as term WHERE term.metaId = "
				+ pk.toString();
		try {
			Query query = getSession().createQuery(queryString);
			term = (ITerm) query.uniqueResult();
		} catch (HibernateException he) {
			throw new LexRepositoryException(he);
		}
		endTransaction(false);
		return term;
	}

	public static List getAllTerms() throws LexRepositoryException {
		List terms = null;

		beginTransaction();
		String queryString = " FROM org.thdl.lex.component.ITerm";
		try {
			Query query = getSession().createQuery(queryString);
			terms = query.list();
		} catch (HibernateException he) {
			throw new LexRepositoryException(he);
		}
		endTransaction(false);
		return terms;
	}

	/**
	 * Description of the Method
	 * 
	 * @param lexQuery
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception LexRepositoryException
	 *                Description of the Exception
	 */
	public static Map findTermsByMeta(LexQuery lexQuery)
			throws LexRepositoryException {
		Logger logger = Logger.getLogger("org.thdl.lex");
		ITerm term = assertTerm(lexQuery.getQueryComponent());
		Map terms = new HashMap();
		ILexComponent comp = null;
		ITerm aTerm;
		Query query = null;
		Iterator it = null;

		setStart(now());
		beginTransaction();
		if (null == term.getMeta()) {
			throw new LexRepositoryException(
					"Query Component term.meta was null.");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Tibetan Dictionary begin query!");
		}
		String queryString = "select term from org.thdl.lex.component.Term as term "
				//join term collections
				+ " join term.pronunciations as pron "
				+ " join term.etymologies as ety "
				+ " join term.spellings as sp "
				+ " join term.functions as func "
				+ " join term.encyclopediaArticles as ency "
				+ " join term.transitionalData as transData "
				+ " join term.definitions as def "
				+ " join term.glosses as glo "
				+ " join term.keywords as key "
				+ " join term.translationEquivalents as trans "
				+ " join term.relatedTerms as rel "
				+ " join term.passages as pass "
				+ " join term.registers as reg "
				//join def collections
				+ " join def.subdefinitions as sub "
				+ " join def.glosses as gloDef "
				+ " join def.keywords as keyDef "
				+ " join def.modelSentences as modDef "
				+ " join def.translationEquivalents as transDef "
				+ " join def.relatedTerms as relDef "
				+ " join def.passages as passDef "
				+ " join def.registers as regDef "
				//join subdef collections
				+ " join sub.glosses as gloSub "
				+ " join sub.keywords as keySub "
				+ " join sub.modelSentences as modSub "
				+ " join sub.translationEquivalents as transSub "
				+ " join sub.relatedTerms as relSub "
				+ " join sub.passages as passSub "
				+ " join sub.registers as regSub "
				// join translation collections
				/*
				 * + " join ety.translations as etyTrans " + " join
				 * term.definitions.translations as defTrans " + " join
				 * term.modelSentences.translations as modTrans " + " join
				 * term.passages.translations as passTrans " + " join
				 * def.subdefinition.translations as subTrans " + " join
				 * def.modelSentences.translations as modDefTrans " + " join
				 * def.passages.translations as passDefTrans " + " join
				 * sub.modelSentences.translations as modSubTrans " + " join
				 * sub.passages.translations as passSubTrans "
				 */
				//restrict by projectSubject in createdByProjSub
				+ " where term.meta.createdByProjSub = :projSub"
				+ " or pron.meta.createdByProjSub = :projSub"
				+ " or ety.meta.createdByProjSub = :projSub"
				+ " or sp.meta.createdByProjSub = :projSub"
				+ " or func.meta.createdByProjSub = :projSub"
				+ " or ency.meta.createdByProjSub = :projSub"
				+ " or transData.meta.createdByProjSub = :projSub"
				+ " or def.meta.createdByProjSub = :projSub"
				+ " or glo.meta.createdByProjSub = :projSub"
				+ " or key.meta.createdByProjSub = :projSub"
				+ " or trans.meta.createdByProjSub = :projSub"
				+ " or rel.meta.createdByProjSub = :projSub"
				+ " or pass.meta.createdByProjSub = :projSub"
				+ " or reg.meta.createdByProjSub = :projSub"
				+ " or sub.meta.createdByProjSub = :projSub"
				+ " or gloDef.meta.createdByProjSub = :projSub"
				+ " or keyDef.meta.createdByProjSub = :projSub"
				+ " or modDef.meta.createdByProjSub = :projSub"
				+ " or transDef.meta.createdByProjSub = :projSub"
				+ " or relDef.meta.createdByProjSub = :projSub"
				+ " or passDef.meta.createdByProjSub = :projSub"
				+ " or regDef.meta.createdByProjSub = :projSub"
				+ " or gloSub.meta.createdByProjSub = :projSub"
				+ " or keySub.meta.createdByProjSub = :projSub"
				+ " or modSub.meta.createdByProjSub = :projSub"
				+ " or transSub.meta.createdByProjSub = :projSub"
				+ " or relSub.meta.createdByProjSub = :projSub"
				+ " or passSub.meta.createdByProjSub = :projSub"
				+ " or regSub.meta.createdByProjSub = :projSub"
				/*
				 * + " or etyTrans.meta.createdByProjSub = :projSub" + " or
				 * defTrans.meta.createdByProjSub = :projSub" + " or
				 * modTrans.meta.createdByProjSub = :projSub" + " or
				 * passTrans.meta.createdByProjSub = :projSub" + " or
				 * subTrans.meta.createdByProjSub = :projSub" + " or
				 * modDefTrans.meta.createdByProjSub = :projSub" + " or
				 * passDefTrans.meta.createdByProjSub = :projSub" + " or
				 * modSubTrans.meta.createdByProjSub = :projSub" + " or
				 * passSubTrans.meta.createdByProjSub = :projSub"
				 */
				//restrict by projectSubject in modifiedByProjSub
				+ " or term.meta.modifiedByProjSub = :projSub"
				+ " or pron.meta.modifiedByProjSub = :projSub"
				+ " or ety.meta.modifiedByProjSub = :projSub"
				+ " or sp.meta.modifiedByProjSub = :projSub"
				+ " or func.meta.modifiedByProjSub = :projSub"
				+ " or ency.meta.modifiedByProjSub = :projSub"
				+ " or transData.meta.modifiedByProjSub = :projSub"
				+ " or def.meta.modifiedByProjSub = :projSub"
				+ " or glo.meta.modifiedByProjSub = :projSub"
				+ " or key.meta.modifiedByProjSub = :projSub"
				+ " or trans.meta.modifiedByProjSub = :projSub"
				+ " or rel.meta.modifiedByProjSub = :projSub"
				+ " or pass.meta.modifiedByProjSub = :projSub"
				+ " or reg.meta.modifiedByProjSub = :projSub"
				+ " or sub.meta.modifiedByProjSub = :projSub"
				+ " or gloDef.meta.modifiedByProjSub = :projSub"
				+ " or keyDef.meta.modifiedByProjSub = :projSub"
				+ " or modDef.meta.modifiedByProjSub = :projSub"
				+ " or transDef.meta.modifiedByProjSub = :projSub"
				+ " or relDef.meta.modifiedByProjSub = :projSub"
				+ " or passDef.meta.modifiedByProjSub = :projSub"
				+ " or regDef.meta.modifiedByProjSub = :projSub"
				+ " or gloSub.meta.modifiedByProjSub = :projSub"
				+ " or keySub.meta.modifiedByProjSub = :projSub"
				+ " or modSub.meta.modifiedByProjSub = :projSub"
				+ " or transSub.meta.modifiedByProjSub = :projSub"
				+ " or relSub.meta.modifiedByProjSub = :projSub"
				+ " or passSub.meta.modifiedByProjSub = :projSub"
				+ " or regSub.meta.modifiedByProjSub = :projSub"
				/*
				 * + " or etyTrans.meta.modifiedByProjSub = :projSub" + " or
				 * defTrans.meta.modifiedByProjSub = :projSub" + " or
				 * modTrans.meta.modifiedByProjSub = :projSub" + " or
				 * passTrans.meta.modifiedByProjSub = :projSub" + " or
				 * subTrans.meta.modifiedByProjSub = :projSub" + " or
				 * modDefTrans.meta.modifiedByProjSub = :projSub" + " or
				 * passDefTrans.meta.modifiedByProjSub = :projSub" + " or
				 * modSubTrans.meta.modifiedByProjSub = :projSub" + " or
				 * passSubTrans.meta.modifiedByProjSub = :projSub";
				 */

				+ "";
		try {
			query = getSession().createQuery(queryString);
			query.setMaxResults(100);
			query.setInteger("projSub", lexQuery.getQueryComponent().getMeta()
					.getCreatedByProjSub().intValue());
			logger.debug("About to list query");
			List list = query.list();
			logger.debug("results size: " + list.size());
			it = list.iterator();
			logger.debug("Starting to add terms to map");
			while (it.hasNext()) {
				aTerm = (ITerm) comp;
				logger.debug("successfully cast comp to an ITerm");

				Integer id = aTerm.getMetaId();
				String tm = aTerm.getTerm();
				terms.put(id, tm);
			}
		} catch (HibernateException he) {
			throw new LexRepositoryException(he);
		}
		endTransaction(false);
		lexQuery.setDuration(getDuration());
		return terms;
	}

	public static Map findTermsByMetaViaLc(LexQuery lexQuery)
			throws LexRepositoryException {
		Logger logger = Logger.getLogger("org.thdl.lex");
		ITerm term = assertTerm(lexQuery.getQueryComponent());
		Map terms = new HashMap();
		ILexComponent comp = null;
		ITerm aTerm;
		Query query = null;
		Iterator it = null;

		setStart(now());
		beginTransaction();
		if (null == term.getMeta()) {
			throw new LexRepositoryException(
					"Query Component term.meta was null.");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Tibetan Dictionary begin query!");
		}
		String queryString = "from org.thdl.lex.component.LexComponent as comp where comp.meta.createdByProjSub=:projSub";

		try {
			query = getSession().createQuery(queryString);
			//query.setMaxResults( 100 );
			query.setInteger("projSub", lexQuery.getQueryComponent().getMeta()
					.getCreatedByProjSub().intValue());
			logger.debug("About to list query");
			List list = query.list();
			logger.debug("results size: " + list.size());
			it = list.iterator();
			while (it.hasNext()) {
				logger.debug("Starting quest for a term parent");

				comp = (ILexComponent) it.next();
				int safetyFirst = 0;
				parentSearch: while (!(comp instanceof ITerm) && comp != null) {
					logger.debug("comp class: " + comp.getClass().getName());
					comp = comp.getParent();
					if (comp instanceof ITerm) {
						try {
							aTerm = (ITerm) comp;
							terms.put(aTerm.getMetaId(), aTerm.getTerm());
							logger.debug("successfully cast comp to an ITerm");
						} catch (ClassCastException cce) {
							logger
									.debug("LCR caught ClassCastException Failed cast of "
											+ comp.toString() + " to ITerm");
							throw cce;
						}
					}

					safetyFirst++;
					if (safetyFirst > 10) {
						logger
								.debug("could not find an ITerm parent for component: "
										+ comp);
						break parentSearch;
					}
				}
			}
		} catch (HibernateException he) {
			throw new LexRepositoryException(he);
		}
		endTransaction(false);
		lexQuery.setDuration(getDuration());
		return terms;
	}

	/**
	 * Description of the Method
	 * 
	 * @param term
	 *            Description of the Parameter
	 * @exception LexRepositoryException
	 *                Description of Exception
	 * @since
	 */
	public static void loadTerm(ITerm term) throws LexRepositoryException {
		try {
			beginTransaction();
			getSession().load(term, term.getMetaId());
			endTransaction(false);
		} catch (HibernateException he) {
			throw new LexRepositoryException(he);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param lexQuery
	 *            Description of the Parameter
	 * @exception LexRepositoryException
	 *                Description of the Exception
	 */
	public static void loadTermByPk(LexQuery lexQuery)
			throws LexRepositoryException {
		beginTransaction();
		ITerm term = assertTerm(lexQuery.getQueryComponent());
		loadTerm(term);
		lexQuery.setEntry(term);
		if (!lexQuery.getResults().containsKey(term.getMetaId())) {
			lexQuery.getResults().put(term.getMetaId(), term.getTerm());
		}
		endTransaction(false);
	}

	public static ITerm loadTermByTerm(String term)
			throws LexRepositoryException {
		ITerm returnTerm = null;
		beginTransaction();
		String queryString = " FROM org.thdl.lex.component.ITerm as theTerm where theTerm.term = :term";
		try {
			Query query = getSession().createQuery(queryString);
			query.setString("term", term);
			returnTerm = (ITerm) query.uniqueResult();
		} catch (HibernateException he) {
			throw new LexRepositoryException(he);
		}

		endTransaction(false);
		return returnTerm;
	}

	/**
	 * Description of the Method
	 * 
	 * @param component
	 *            Description of the Parameter
	 * @exception LexRepositoryException
	 *                Description of the Exception
	 */
	public static void loadByPk(ILexComponent component)
			throws LexRepositoryException {

		try {
			beginTransaction();
			getSession().load(component, component.getMetaId());
			endTransaction(false);
		} catch (HibernateException he) {
			throw new LexRepositoryException(he);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param component
	 *            Description of the Parameter
	 * @param pk
	 *            Description of the Parameter
	 * @exception LexRepositoryException
	 *                Description of the Exception
	 */
	public static void loadByPk(ILexComponent component, Integer pk)
			throws LexRepositoryException {

		try {
			beginTransaction();
			getSession().load(component, pk);
			endTransaction(false);
		} catch (HibernateException he) {
			throw new LexRepositoryException(he);
		}
	}

	/**
	 * Gets the recentTerms attribute of the LexComponentRepository class
	 * 
	 * @param limit
	 *            Description of the Parameter
	 * @return The recentTerms value
	 * @exception LexRepositoryException
	 *                Description of the Exception
	 */
	public static List getRecentTerms(int limit) throws LexRepositoryException {
		Query query = null;
		List results = null;
		String queryString = " FROM org.thdl.lex.component.ITerm ORDER BY modifiedOn DESC LIMIT "
				+ limit;
		try {
			beginTransaction();
			query = getSession().createQuery(queryString);
			results = query.list();
			endTransaction(false);
			getSession().clear();
		} catch (HibernateException he) {
			throw new LexRepositoryException(he);
		}

		return results;
	}

	/**
	 * Description of the Method
	 * 
	 * @param component
	 *            Description of the Parameter
	 * @exception LexRepositoryException
	 *                Description of the Exception
	 */
	public static void save(ILexComponent component)
			throws LexRepositoryException {

		try {
			beginTransaction();
			getSession().saveOrUpdate(component);
			endTransaction(true);
			setLastUpdate(now());
		} catch (HibernateException he) {
			throw new LexRepositoryException(he);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param component
	 *            Description of the Parameter
	 * @exception LexRepositoryException
	 *                Description of the Exception
	 */
	public static void update(ILexComponent component)
			throws LexRepositoryException {

		try {
			beginTransaction();
			getSession().update(component);
			endTransaction(true);
			setLastUpdate(now());
		} catch (HibernateException he) {
			throw new LexRepositoryException(he);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param component
	 *            Description of the Parameter
	 * @exception LexRepositoryException
	 *                Description of the Exception
	 */
	public static void remove(ILexComponent component)
			throws LexRepositoryException {

		try {
			beginTransaction();
			getSession().delete(component);
			endTransaction(true);
			setLastUpdate(now());
		} catch (HibernateException he) {
			throw new LexRepositoryException(he);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param component
	 *            Description of the Parameter
	 * @exception LexRepositoryException
	 *                Description of the Exception
	 */
	public static void refresh(ILexComponent component)
			throws LexRepositoryException {

		try {
			beginTransaction();
			getSession().refresh(component);
			endTransaction(true);
		} catch (HibernateException he) {
			throw new LexRepositoryException(he);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @exception LexRepositoryException
	 *                Description of Exception
	 * @since
	 */
	public static void cleanup() throws LexRepositoryException {
		try {
			endTransaction(false);
			HibernateSession.closeSession();
		} catch (HibernateException he) {
			throw new LexRepositoryException(he);
		}
	}
}

/*
 * /join term collections + " join term.pronunciations as pron " + " join
 * term.etymologies as ety " + " join term.spellings as sp " + " join
 * term.functions as func " + " join term.encyclopediaArticles as ency " + "
 * join term.transitionalData as trans " + " join term.definitions as def " + "
 * join term.glosses as glo " + " join term.keywords as key " + " join
 * term.translationEquivalents as trans " + " join term.relatedTerms as rel " + "
 * join term.passages as pass " + " join term.registers as reg " join def
 * collections + " join def.subdefinitions as sub " + " join def.glosses as
 * gloDef " + " join def.keywords as keyDef " + " join def.modelSentences as
 * modDef " + " join def.translationEquivalents as transDef " + " join
 * def.relatedTerms as relDef " + " join def.passages as passDef " + " join
 * def.registers as regDef " join subdef collections + " join sub.glosses as
 * gloSub " + " join sub.keywords as keySub " + " join sub.modelSentences as
 * modSub " + " join sub.translationEquivalents as transSub " + " join
 * sub.relatedTerms as relSub " + " join sub.passages as passSub " + " join
 * sub.registers as regSub " join translation collections + " join
 * ety.translations as etyTrans " + " join term.definitions.translations as
 * defTrans " + " join term.modelSentences.translations as modTrans " + " join
 * term.passages.translations as passTrans " + " join
 * def.subdefinition.translations as subTrans " + " join
 * def.modelSentences.translations as modDefTrans " + " join
 * def.passages.translations as passDefTrans " + " join
 * sub.modelSentences.translations as modSubTrans " + " join
 * sub.passages.translations as passSubTrans " /restrict by projectSubject in
 * createdByProjSub + " where term.meta.createdByProjSub = :projSub" + " or
 * pron.meta.createdByProjSub = :projSub" + " or ety.meta.createdByProjSub =
 * :projSub" + " or sp.meta.createdByProjSub = :projSub" + " or
 * func.meta.createdByProjSub = :projSub" + " or ency.meta.createdByProjSub =
 * :projSub" + " or trans.meta.createdByProjSub = :projSub" + " or
 * def.meta.createdByProjSub = :projSub" + " or glo.meta.createdByProjSub =
 * :projSub" + " or key.meta.createdByProjSub = :projSub" + " or
 * trans.meta.createdByProjSub = :projSub" + " or rel.meta.createdByProjSub =
 * :projSub" + " or pass.meta.createdByProjSub = :projSub" + " or
 * reg.meta.createdByProjSub = :projSub" + " or subDef.meta.createdByProjSub =
 * :projSub" + " or gloDef.meta.createdByProjSub = :projSub" + " or
 * keyDef.meta.createdByProjSub = :projSub" + " or modDef.meta.createdByProjSub =
 * :projSub" + " or transDef.meta.createdByProjSub = :projSub" + " or
 * relDef.meta.createdByProjSub = :projSub" + " or passDef.meta.createdByProjSub =
 * :projSub" + " or regDef.meta.createdByProjSub = :projSub" + " or
 * gloSub.meta.createdByProjSub = :projSub" + " or keySub.meta.createdByProjSub =
 * :projSub" + " or modSub.meta.createdByProjSub = :projSub" + " or
 * transSub.meta.createdByProjSub = :projSub" + " or
 * relSub.meta.createdByProjSub = :projSub" + " or passSub.meta.createdByProjSub =
 * :projSub" + " or regSub.meta.createdByProjSub = :projSub" + " or
 * etyTrans.meta.createdByProjSub = :projSub" + " or
 * defTrans.meta.createdByProjSub = :projSub" + " or
 * modTrans.meta.createdByProjSub = :projSub" + " or
 * passTrans.meta.createdByProjSub = :projSub" + " or
 * subTrans.meta.createdByProjSub = :projSub" + " or
 * modDefTrans.meta.createdByProjSub = :projSub" + " or
 * passDefTrans.meta.createdByProjSub = :projSub" + " or
 * modSubTrans.meta.createdByProjSub = :projSub" + " or
 * passSubTrans.meta.createdByProjSub = :projSub" /restrict by projectSubject in
 * modifiedByProjSub + " or term.meta.modifiedByProjSub = :projSub" + " or
 * pron.meta.modifiedByProjSub = :projSub" + " or ety.meta.modifiedByProjSub =
 * :projSub" + " or sp.meta.modifiedByProjSub = :projSub" + " or
 * func.meta.modifiedByProjSub = :projSub" + " or ency.meta.modifiedByProjSub =
 * :projSub" + " or trans.meta.modifiedByProjSub = :projSub" + " or
 * def.meta.modifiedByProjSub = :projSub" + " or glo.meta.modifiedByProjSub =
 * :projSub" + " or key.meta.modifiedByProjSub = :projSub" + " or
 * trans.meta.modifiedByProjSub = :projSub" + " or rel.meta.modifiedByProjSub =
 * :projSub" + " or pass.meta.modifiedByProjSub = :projSub" + " or
 * reg.meta.modifiedByProjSub = :projSub" + " or subDef.meta.modifiedByProjSub =
 * :projSub" + " or gloDef.meta.modifiedByProjSub = :projSub" + " or
 * keyDef.meta.modifiedByProjSub = :projSub" + " or
 * modDef.meta.modifiedByProjSub = :projSub" + " or
 * transDef.meta.modifiedByProjSub = :projSub" + " or
 * relDef.meta.modifiedByProjSub = :projSub" + " or
 * passDef.meta.modifiedByProjSub = :projSub" + " or
 * regDef.meta.modifiedByProjSub = :projSub" + " or
 * gloSub.meta.modifiedByProjSub = :projSub" + " or
 * keySub.meta.modifiedByProjSub = :projSub" + " or
 * modSub.meta.modifiedByProjSub = :projSub" + " or
 * transSub.meta.modifiedByProjSub = :projSub" + " or
 * relSub.meta.modifiedByProjSub = :projSub" + " or
 * passSub.meta.modifiedByProjSub = :projSub" + " or
 * regSub.meta.modifiedByProjSub = :projSub" + " or
 * etyTrans.meta.modifiedByProjSub = :projSub" + " or
 * defTrans.meta.modifiedByProjSub = :projSub" + " or
 * modTrans.meta.modifiedByProjSub = :projSub" + " or
 * passTrans.meta.modifiedByProjSub = :projSub" + " or
 * subTrans.meta.modifiedByProjSub = :projSub" + " or
 * modDefTrans.meta.modifiedByProjSub = :projSub" + " or
 * passDefTrans.meta.modifiedByProjSub = :projSub" + " or
 * modSubTrans.meta.modifiedByProjSub = :projSub" + " or
 * passSubTrans.meta.modifiedByProjSub = :projSub";
 */

