/*
The contents of this file are subject to the AMP Open Community License
Version 1.0 (the "License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License on the AMP web site 
(http://www.tibet.iteso.mx/Guatemala/).

Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
License for the specific terms governing rights and limitations under the 
License. 

The Initial Developer of this software is Andres Montano Pellegrini. Portions
created by Andres Montano Pellegrini are Copyright 2001 Andres Montano
Pellegrini. All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.scanner;
import org.thdl.util.*;

/** Defines the core methods required to provide access to a dictionary; local or remote.

    @author Andr&eacute;s Montano Pellegrini
*/
public abstract class TibetanScanner
{
	public static final String copyrightUnicode="Copyright " + '\u00A9' + " 2000-2004 by Andr" + '\u00E9' + "s Montano Pellegrini, all rights reserved.";
	public static final String copyrightASCII="Copyright 2000-2004 by Andres Montano Pellegrini, all rights reserved.";
	public static final String copyrightHTML="<hr><small><strong>" + "The Tibetan to English Translation Tool: Version 2.2.0, compiled on " + ThdlVersion.getTimeOfCompilation() + ". Copyright &copy; 2000-2004 by <a href=\"http://www.people.virginia.edu/~am2zb/\" target=\"_blank\">Andr&eacute;s Montano Pellegrini.</a><br/>All rights reserved.</strong></small>";
	public static final String aboutUnicode=
	"Warning: Since version 1.3.0. the dictionary database format changed and " +
	"is incompatible with previous versions. In order to use the newest version " +
	"you have to re-build the dictionary database.\n\n" +
	"The Tibetan to English Translation Tool, version 2.2.0, compiled on " + ThdlVersion.getTimeOfCompilation() + ". " +
	"Copyright " + '\u00A9' + " 2000-2004 Andr" + '\u00E9' + "s Montano Pellegrini, all rights reserved.\n\n" + 
	"This software is protected by the terms of the AMP Open Community License, " +
	"Version 1.0 (available at www.tibet.iteso.mx/Guatemala/). The Tibetan script " +
	"input facility was built by THDL's Edward Garrett (http://www.thdl.org/).\n" +
	"It uses Tibetan Computer Company (http://www.tibet.dk/tcc/)fonts created by " +
	"Tony Duff and made available by the Trace Foundation (http://trace.org/).\n\n" +
    "About the Tibetan-Sanskrit Table of Buddhist Terminology based on the Yogacarabhumi:\n\n" +
    "The Tibetan-Sanskrit tab separated database file was extracted, converted, " +
    "filtered, and sorted by Richard B. Mahoney from YOKOYAMA, Koitsu & HIROSAWA, " + 
    "Takayuki, eds., Index to the Yogacarabhumi (Chinese-Sanskrit-Tibetan) (Tokyo : " +
    "Sankibo Busshorin, 1996). See http://homepages.comnet.co.nz/~r-mahoney/links/links.html " +
    "for details.\n\n" +
	"About Rangjung Yeshe Tibetan-English Dharma Dictionary 3.0:\n\n" +
	"Copyright "  + '\u00A9' + " Rangjung Yeshe Publications and the individual contributors.\n\n" + 
	"The Rangjung Yeshe Tibetan-English Dharma Dictionary is specifically licensed to " + 
	"Andres Montano by Rangjung Yeshe Publications. Permission must be obtained " +
	"directly from Rangjung Yeshe Publications for any other public display of the " +
	"dictionary.\n\n" + 
	"Users desiring off-line access to the dictionary through The Tibetan to " +
	"English Translation Tool must purchase the dictionary directly with Rangjung " +
	"Yeshe Publications. See http://www.rangjung.com/ry-dic.htm for details.\n\n" + 
	"About Jeffrey Hopkins' Tibetan-Sanskrit-English Dictionary Version 2.0.0:\n\n" + 
	"Formulator and Editor: Jeffrey Hopkins\n" + 
	"Contributors: Joe Wilson, Craig Preston, John Powers, Nathaniel Garson, " + 
	"Paul Hackett, Andres Montano\n\n" + 
	"A project of the Tibetan Studies Institute, Boonesville, Virginia, and the " + 
	"University of Virginia Tibetan Studies Program\n\n" + 
	"\u00A9 Jeffrey Hopkins 1992.\n\n" + 
	"Apology\n\n" + 
	"This is a work in progress in crude form that is being shared with students " + 
	"of the Tibetan language mainly in order to receive input for further " + 
	"development. The English translations of the entries can be said only to " + 
	"represent what contributors, over a span of over thirty years, thought were " + 
	"my current translations. A small number are simply wrong; others need to be " + 
	"updated; and all will receive much more attention and, hence, detail.\n\n" + 
	"The Dictionary has been entered into a database with fields for the entry, " + 
	"Sanskrit, tenses, my English, a few others’ interests, examples, " + 
	"definition, divisions, and comments. At this point, very few entries " + 
	"contain all of these items, but the plan is provide these, where " + 
	"appropriate, over the years. Translations for entries that have arisen from " + 
	"my work and from interactions with my students are in boldface, whereas " + 
	"those from other works are in regular type on separate lines and are marked " + 
	"with an initial at the end of the line. A key to these markings is given on " + 
	"the next page.\n\n" + 
	"(Please note that the radical signs for Sanskrit roots are, after the first " + 
	"letter of the alphabet, in a state of disarray.)\n\n" + 
	"I hope that you will bear with the many inadequacies of this first release.\n\n" + 
	"	Paul Jeffrey Hopkins\n" + 
	"	Professor of Tibetan Studies\n\n" + 
	"Abbreviations\n\n" + 
	"B-7: ??? {PH: see dngos med ... & dngos po (synonyms) }\n\n" + 
	"BJ: Bel-jor-hlun-drup (Dpal \'byor lhun grub).  Legs bshad snying po\'i dka' " + 
	"\'grel bstan pa\'i sgron me (Buxaduar: Sera Monastery, 1968).\n\n" + 
	"BK: ??? {PH: see bka\' (examples) }\n\n" + 
	"BR: Losang Gyatso (Blo bzang rgya mtsho).  Presentation of Knowledge and " + 
	"Awareness (Blo rig).\n\n" + 
	"BWT:  Ngak-wang-bel-den (Ngag dbang dpal ldan).  Annotations for " + 
	"[Jam-yang-shay-ba\'s] \"Tenets\" (Grub mtha\' chen mo\'i mchan).\n\n" + 
	"C: Conze, Edward.  Materials for a Dictionary of the Prajnaparamita " + 
	"Literature (Tokyo: Suzuki Research Foundation, 1967).\n\n" + 
	"col.: colloquial\n\n" + 
	"D1: Pur-bu-jok (Phur bu lcog).  Presentation of the Collected Topics " + 
	"(Part 1: Bsdus grwa chung ngu).\n\n" + 
	"D2: Pur-bu-jok (Phur bu lcog).  Presentation of the Collected Topics " + 
	"(Part 2: Bsdus grwa \'bring).\n\n" + 
	"DASI: Decisive Analysis of Special Insight.\n\n" + 
	"DG: Germano, David. Poetic Thought, the Intelligent Universe, and the " + 
	"Mystery of Self: the Tantric Synthesis of rDzogs Chen in Fourteenth Century " + 
	"Tibet. (Ph.d. dissertation, University of Wisconsin, Madison,WI 1992).\n\n" + 
	"DK: Dzong-ka-ba (Tsong kha pa blo bzang grags pa).  Drang ba dang nges pa\'i " + 
	"don rnam par phye ba'i bstan bcos legs bshad snying po (Sarnath: Pleasure of " + 
	"Elegant Sayings Press, 1979).\n\n" + 
	"Ganden Triba: Oral commentary of Ganden Triba Jam-bel-shen-pen.\n\n" + 
	"GCT: Ngak-wang-dra-shi (Ngag dbang bkra shis).  Collected Topics by a " + 
	"Spiritual Son of Jam-yang-shay-ba (Sgo mang sras bsdus grwa).\n\n" + 
	"GD: Dreyfus, George. Ontology, Philosophy of Language, and Epistemology in " + 
	"Buddhist Tradition (Ph.d. dissertation. Religious Studies, University of " + 
	"Virginia, Charlottesville,VA 1991).\n\n" + 
	"Gon-chok: Gon-chok-jik-may-wang-bo (Dkon mchog \'jigs med dbang po). " + 
	"Precious Garland of Tenets (Grub mtha\' rin chen phreng ba).\n\n" + 
	"Jang.: Jang-gya (Lcang skya rol pa\'i rdo rje). " + 
	"Presentation of Tenets (Lcang skya grub mtha').\n\n" + 
	"JKA: ??? {PH: see mngon sum (definition) } \n\n" + 
	"KS: Khetsun Sangpo, Biographical Dictionary of Tibet and Tibetan Buddhism. " + 
	"(LTWA: Dharamsala, HP)\n\n" + 
	"L: Lamotte, Etienne.  Samdhinirmocana-sutra " + 
	"(Louvain: Universite de Louvain, 1935).\n\n" + 
	"LAK: Jam-bel-sam-pel (\'Jam dpal bsam phel).  Presentation of Awareness and " + 
	"Knowledge (Blo rig gi rnam bzhag).\n\n" + 
	"Lati: Oral commentary by Lati Rinbochay.\n\n" + 
	"LCh: Chandra, Lokesh.  Tibetan-Sanskrit Dictionary (New Delhi, 1987).\n\n" + 
	"LG: Losang Gyatso\'s Blo rig.\n\n" + 
	"LM: ??? {PH: see skye bu chung ngu ... }\n\n" + 
	"LR: Hopkins, Jeffrey.  Glossary for Gsung rab kun gyi snying po lam rim gyi " + 
	"gtso bo rnam pa gsung gi khrid yid gzhan phan snying po (by Panchen Lama IV).\n\n" + 
	"LSR: Tsul-trim-nam-gyel (Tshul khrims rnam rgyal).  Presentation of Signs " + 
	"and Reasonings (Rtags rigs kyi rnam bzhag).\n\n" + 
	"LWT: Lo-sang-gon-chok (Blo bzang dkon mchog).  Word Commentary on the Root " + 
	"Text of [Jam-yang-shay-ba\'s] \"Tenets\".\n\n" + 
	"ME: Hopkins, Jeffrey.  Meditation on Emptiness (London, Wisdom, 1983).\n\n" + 
	"MGP: ??? {PH: see bkag (examples) }\n\n" + 
	"MSA: Nagao, Gadjin.  Index to the  Mahayanasutralankara (Tokyo: Nippon " + 
	"Gakujutsu Shinkvo-kai, 1958).\n\n" + 
	"MSI: Dzong-ka-ba (Tsong kha pa blo bzang grags pa).  Middling Exposition of " + 
	"Special Insight (Lhag mthong \'bring).\n\n" + 
	"MV: Nagao, Gadjin.  Index to the Madhyanta-vibhaga (Tokyo: 1961).\n\n" + 
	"N: Zuiryu NAKAMURA. Index to the Ratnagotravibhaga-mahayanottaratantra-sastra " + 
	"(Tokyo, 1961).\n\n" + 
	"P: Peking edition of the Tripitaka.\n\n" + 
	"PGP: Lo-sang-da-yang (Blo bzang rta dbyangs).  Presentation of the Grounds " + 
	"and Paths in Prasangika (Thal \'gyur pa\'i sa lam).\n\n" + 
	"PP: Candrakirti.  Prasannapada.\n\n" + 
	"S: Samdhinirmocana-sutra (Tok Palace version, 160 pp., Leh, Ladakh: Shesrig " + 
	"Dpemzod, 1975-1980, vol. ja).\n\n" + 
	"TAK: Pur-bu-jok (Phur bu lcog).  Explanation of the Presentation of Objects " + 
	"and Object-Possessors as Well as Awareness and Knowledge (Yul dang yul can " + 
	"dang blo rig).\n\n" + 
	"TCT: Pur-bu-jok (Phur bu lcog).  Presentation of the Collected Topics (Yongs " + 
	"\'dzin bsdus grwa).\n\n" + 
	"TGP: Nga-wang-bel-den (Ngag dbang dpal ldan).  Treatise Illuminating the " + 
	"Presentation of the Four Great Secret Tantra Sets (Sngags kyi sa lam).\n\n" + 
	"TN: Vasubandhu.  Trisvabhavanirdesha.\n\n" + 
	"VM: Bu-don-rin-chen-drup (bu ston rin chen grub), The Practice of " + 
	"(Jnandagarbha\'s) \"The Rite of the Vajra-Element Mandala: The Source of All " + 
	"Vajras\": A Precious Enhancer of Thought (rDo rje dbyings kyi dkyil \'khor gyi " + 
	"cho ga rdo rje thams cad \'byung ba zhes bya ba\'i lag len rin chen bsam \'phel), " + 
	"in Collected Works, Part 12 na. Lhasa: Zhol Printing House, 1990.\n\n" + 
	"Y: Susumi YAMAGUCHI.Index to the Prasannapada Madhyamakavrtti. " + 
	"(Kyoto: Heirakuji-Shoten, 1974).\n\n" + 
	"YT: Oral commentary by Yeshi Thupten.";

	public static final String aboutTomeraider=
	"Welcome to Jeffrey Hopkins' Tibetan-Sanskrit-English Dictionary version 2.0.0!<p>\n" + 
	"This file was automatically generated using software developed by Andres Montano Pellegrini. " +
	"For more information, see http://www.people.virginia.edu/~am2zb/tibetan .<p>" +
	"<b>Formulator and Editor</b>: Jeffrey Hopkins<br>\n" + 
	"<b>Contributors</b>: Joe Wilson, Craig Preston, John Powers, Nathanial Garson, " + 
	"Paul Hackett, Andres Montano<p>" + 
	"A project of the Tibetan Studies Institute, Boonesville, Virginia, and the " + 
	"University of Virginia Tibetan Studies Program<p>" + 
	"<i>\u00A9 Jeffrey Hopkins 1992.</i><p>" + 
	"<b>Apology</b><p>" + 
	"This is a work in progress in crude form that is being shared with students " + 
	"of the Tibetan language mainly in order to receive input for further " + 
	"development. The English translations of the entries can be said only to " + 
	"represent what contributors, over a span of over thirty years, thought were " + 
	"my current translations. A small number are simply wrong; others need to be " + 
	"updated; and all will receive much more attention and, hence, detail.<p>\n" + 
	"The Dictionary has been entered into a database with fields for the entry, " + 
	"Sanskrit, tenses, my English, a few others’ interests, examples, " + 
	"definition, divisions, and comments. At this point, very few entries " + 
	"contain all of these items, but the plan is provide these, where " + 
	"appropriate, over the years. Translations for entries that have arisen from " + 
	"my work and from interactions with my students are in boldface, whereas " + 
	"those from other works are in regular type on separate lines and are marked " + 
	"with an initial at the end of the line. A key to these markings is given on " + 
	"the next page.<p>\n" + 
	"(Please note that the radical signs for Sanskrit roots are, after the first" + 
	"letter of the alphabet, in a state of disarray.)<p>\n" + 
	"I hope that you will bear with the many inadequacies of this first release.<p>\n" + 
	"Paul Jeffrey Hopkins<br>\n" + 
	"Professor of Tibetan Studies<p>\n" + 
	"<b>Abbreviations</b><p>\n" + 
	"B-7: ??? {PH: see dngos med ... & dngos po (synonyms) }<p>\n" + 
	"BJ: Bel-jor-hlun-drup (Dpal \'byor lhun grub).  Legs bshad snying po\'i dka' " + 
	"\'grel bstan pa\'i sgron me (Buxaduar: Sera Monastery, 1968).<p>\n" + 
	"BK: ??? {PH: see bka\' (examples) }<p>\n" + 
	"BR: Losang Gyatso (Blo bzang rgya mtsho).  Presentation of Knowledge and " + 
	"Awareness (Blo rig).<p>\n" + 
	"BWT:  Ngak-wang-bel-den (Ngag dbang dpal ldan).  Annotations for " + 
	"[Jam-yang-shay-ba\'s] \"Tenets\" (Grub mtha\' chen mo\'i mchan).<p>\n" + 
	"C: Conze, Edward.  Materials for a Dictionary of the Prajnaparamita " + 
	"Literature (Tokyo: Suzuki Research Foundation, 1967).<p>\n" + 
	"col.: colloquial<p>\n" + 
	"D1: Pur-bu-jok (Phur bu lcog).  Presentation of the Collected Topics " + 
	"(Part 1: Bsdus grwa chung ngu).<p>\n" + 
	"D2: Pur-bu-jok (Phur bu lcog).  Presentation of the Collected Topics " + 
	"(Part 2: Bsdus grwa \'bring).<p>\n" + 
	"DASI: Decisive Analysis of Special Insight.<p>\n" + 
	"DG: Germano, David. Poetic Thought, the Intelligent Universe, and the " + 
	"Mystery of Self: the Tantric Synthesis of rDzogs Chen in Fourteenth Century " + 
	"Tibet. (Ph.d. dissertation, University of Wisconsin, Madison,WI 1992).<p>\n" + 
	"DK: Dzong-ka-ba (Tsong kha pa blo bzang grags pa).  Drang ba dang nges pa\'i " + 
	"don rnam par phye ba'i bstan bcos legs bshad snying po (Sarnath: Pleasure of " + 
	"Elegant Sayings Press, 1979).<p>\n" + 
	"Ganden Triba: Oral commentary of Ganden Triba Jam-bel-shen-pen.<p>\n" + 
	"GCT: Ngak-wang-dra-shi (Ngag dbang bkra shis).  Collected Topics by a " + 
	"Spiritual Son of Jam-yang-shay-ba (Sgo mang sras bsdus grwa).<p>\n" + 
	"GD: Dreyfus, George. Ontology, Philosophy of Language, and Epistemology in " + 
	"Buddhist Tradition (Ph.d. dissertation. Religious Studies, University of " + 
	"Virginia, Charlottesville,VA 1991).<p>\n" + 
	"Gon-chok: Gon-chok-jik-may-wang-bo (Dkon mchog \'jigs med dbang po). " + 
	"Precious Garland of Tenets (Grub mtha\' rin chen phreng ba).<p>\n" + 
	"Jang.: Jang-gya (Lcang skya rol pa\'i rdo rje). " + 
	"Presentation of Tenets (Lcang skya grub mtha').<p>\n" + 
	"JKA: ??? {PH: see mngon sum (definition) } <p>\n" + 
	"KS: Khetsun Sangpo, Biographical Dictionary of Tibet and Tibetan Buddhism. " + 
	"(LTWA: Dharamsala, HP)<p>\n" + 
	"L: Lamotte, Etienne.  Samdhinirmocana-sutra " + 
	"(Louvain: Universite de Louvain, 1935).<p>\n" + 
	"LAK: Jam-bel-sam-pel (\'Jam dpal bsam phel).  Presentation of Awareness and " + 
	"Knowledge (Blo rig gi rnam bzhag).<p>\n" + 
	"Lati: Oral commentary by Lati Rinbochay.<p>\n" + 
	"LCh: Chandra, Lokesh.  Tibetan-Sanskrit Dictionary (New Delhi, 1987).<p>\n" + 
	"LG: Losang Gyatso\'s Blo rig.<p>\n" + 
	"LM: ??? {PH: see skye bu chung ngu ... }<p>\n" + 
	"LR: Hopkins, Jeffrey.  Glossary for Gsung rab kun gyi snying po lam rim gyi " + 
	"gtso bo rnam pa gsung gi khrid yid gzhan phan snying po (by Panchen Lama IV).<p>\n" + 
	"LSR: Tsul-trim-nam-gyel (Tshul khrims rnam rgyal).  Presentation of Signs " + 
	"and Reasonings (Rtags rigs kyi rnam bzhag).<p>\n" + 
	"LWT: Lo-sang-gon-chok (Blo bzang dkon mchog).  Word Commentary on the Root " + 
	"Text of [Jam-yang-shay-ba\'s] \"Tenets\".<p>\n" + 
	"ME: Hopkins, Jeffrey.  Meditation on Emptiness (London, Wisdom, 1983).<p>\n" + 
	"MGP: ??? {PH: see bkag (examples) }<p>\n" + 
	"MSA: Nagao, Gadjin.  Index to the  Mahayanasutralankara (Tokyo: Nippon " + 
	"Gakujutsu Shinkvo-kai, 1958).<p>\n" + 
	"MSI: Dzong-ka-ba (Tsong kha pa blo bzang grags pa).  Middling Exposition of " + 
	"Special Insight (Lhag mthong \'bring).<p>\n" + 
	"MV: Nagao, Gadjin.  Index to the Madhyanta-vibhaga (Tokyo: 1961).<p>\n" + 
	"N: Zuiryu NAKAMURA. Index to the Ratnagotravibhaga-mahayanottaratantra-sastra " + 
	"(Tokyo, 1961).<p>\n" + 
	"P: Peking edition of the Tripitaka.<p>\n" + 
	"PGP: Lo-sang-da-yang (Blo bzang rta dbyangs).  Presentation of the Grounds " + 
	"and Paths in Prasangika (Thal \'gyur pa\'i sa lam).<p>\n" + 
	"PP: Candrakirti.  Prasannapada.<p>\n" + 
	"S: Samdhinirmocana-sutra (Tok Palace version, 160 pp., Leh, Ladakh: Shesrig " + 
	"Dpemzod, 1975-1980, vol. ja).<p>\n" + 
	"TAK: Pur-bu-jok (Phur bu lcog).  Explanation of the Presentation of Objects " + 
	"and Object-Possessors as Well as Awareness and Knowledge (Yul dang yul can " + 
	"dang blo rig).<p>\n" + 
	"TCT: Pur-bu-jok (Phur bu lcog).  Presentation of the Collected Topics (Yongs " + 
	"\'dzin bsdus grwa).<p>\n" + 
	"TGP: Nga-wang-bel-den (Ngag dbang dpal ldan).  Treatise Illuminating the " + 
	"Presentation of the Four Great Secret Tantra Sets (Sngags kyi sa lam).<p>\n" + 
	"TN: Vasubandhu.  Trisvabhavanirdesha.<p>\n" + 
	"VM: Bu-don-rin-chen-drup (bu ston rin chen grub), The Practice of " + 
	"(Jnandagarbha\'s) \"The Rite of the Vajra-Element Mandala: The Source of All " + 
	"Vajras\": A Precious Enhancer of Thought (rDo rje dbyings kyi dkyil \'khor gyi " + 
	"cho ga rdo rje thams cad \'byung ba zhes bya ba\'i lag len rin chen bsam \'phel), " + 
	"in Collected Works, Part 12 na. Lhasa: Zhol Printing House, 1990.<p>\n" + 
	"Y: Susumi YAMAGUCHI.Index to the Prasannapada Madhyamakavrtti. " + 
	"(Kyoto: Heirakuji-Shoten, 1974).<p>\n" + 
	"YT: Oral commentary by Yeshi Thupten.";
	
	protected SimplifiedLinkedList wordList;
	
	public TibetanScanner()
	{
	    wordList = new SimplifiedLinkedList();
	}

	public void clearTokens()
	{
		wordList = new SimplifiedLinkedList();
	}

	public Token[] getTokenArray()
	{
		int n=wordList.size();
		Token token[] = new Token[n];
		SimplifiedListIterator li = wordList.listIterator();
		while(li.hasNext())
			token[--n] = (Token)li.next();
		return token;
	}

	public SimplifiedLinkedList getTokenLinkedList()
	{
		return wordList;
	}
	
	public Word[] getWordArray()
	{
	    Token token;
	    Word array[];
	    int n=0;
		SimplifiedListIterator li = wordList.listIterator();
		while(li.hasNext())
			if (li.next() instanceof Word) n++;
	    
	    if (n==0) return null;
	    
	    array = new Word[n];
	    n--;
		li = wordList.listIterator();
		while(li.hasNext())
		{
		    token = (Token) li.next();
			if (token instanceof Word)
			{
			    array[n] = (Word) token;
			    n--;
			}
		}
		
	    return array;
	}
		
	public abstract void scanLine(String linea);
	public abstract void scanBody(String linea);
	public abstract void finishUp();
	public abstract DictionarySource getDictionarySource();
	public abstract String[] getDictionaryDescriptions();
}
