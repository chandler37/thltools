package org.thdl.lex;

import java.io.File;
import java.util.Iterator;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;

import org.thdl.lex.component.ILexComponent;

/**
 * Description of the Class
 * 
 * @author travis
 * @created February 16, 2004
 */
public class ConvertDataToNewMapping {
	public static File file = null;

	public static void convertLexComponents(String[] args) {
		try {

			if (file.exists()) {
				System.out.println("Config File exists!");
			} else {
				System.out.println("Config File DOES NOT exist!");
			}

			HibernateSessionDataTransfer.setConfig(file);
			HibernateSessionDataTransfer.setConfigResource(args[0]);

			Iterator it;
			ILexComponent lc;

			LexComponentRepository.beginTransaction();

			String queryString = " FROM org.thdl.lex.component.LexComponent comp where metaId";
			Query query = LexComponentRepository.getSession().createQuery(
					queryString);
			it = query.iterate();
			while (it.hasNext()) {
				lc = (ILexComponent) it.next();

				System.out.println("Saving: " + lc.toString());

				try {
					LexComponentRepositoryDataTransfer.beginTransaction();
					LexComponentRepositoryDataTransfer.getSession().save(lc);
					LexComponentRepositoryDataTransfer.endTransaction(true);
					LexComponentRepositoryDataTransfer.getSession().evict(lc);
					LexComponentRepository.getSession().evict(lc);
				} catch (HibernateException he) {
					LexComponentRepositoryDataTransfer.endTransaction(false);
					throw he;
				}

			}

			LexComponentRepository.endTransaction(false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The main program for the ConvertDataToNewMapping class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		file = new java.io.File(args[0]);
		//ConvertDataToNewMapping.convertLexComponents();
		//ConvertDataToNewMapping.convertTerms();
		//ConvertDataToNewMapping.writeCredits();
	}
}

