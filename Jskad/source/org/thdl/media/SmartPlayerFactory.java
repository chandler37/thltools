import java.lang.reflect.*;
import java.util.*;

public static class SmartPlayerFactory {
	static final String[] possiblePlayers
		= {"org.thdl.media.SmartJMFPlayer", "org.thdl.media.SmartQT4JPlayer"};

	static SmartMoviePanel[] getAllAvailableSmartPlayers() {
		List moviePlayers = new ArrayList();
		for (int i=0; i<possiblePlayers.length; i++) {
			try {
				Class mediaClass = Class.forName(possiblePlayers[i]);
				playerClasses.add(mediaClass);
				SmartMoviePanel smp = (SmartMoviePanel)mediaClass.newInstance();
				moviePlayers.add(smp);
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			} catch (InstantiationException ie) {
				ie.printStackTrace();
				ThdlDebug.noteIffyCode();
			} catch (IllegalAccessException iae) {
				iae.printStackTrace();
				ThdlDebug.noteIffyCode();
			}
		}
		return (SmartMoviePanel[])moviePlayers.toArray();
	}
}