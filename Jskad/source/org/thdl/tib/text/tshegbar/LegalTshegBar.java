/*
The contents of this file are subject to the THDL Open Community License
Version 1.0 (the "License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License on the THDL web site 
(http://www.thdl.org/).

Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
License for the specific terms governing rights and limitations under the 
License. 

The Initial Developer of this software is the Tibetan and Himalayan Digital
Library (THDL). Portions created by the THDL are Copyright 2001 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.tshegbar;

import org.thdl.tib.text.TibetanMachineWeb;
import org.thdl.util.ThdlDebug;

/** <p>A LegalTshegBar is a simple Tibetan syllable or a syllable with
 *  syntactically legal {@link #getPossibleSuffixParticles() suffix
 *  particles}.  A legal tsheg-bar is not a transliteration of Chinese
 *  or some other language.  It obeys the following properties:</p>
 *
 *  <ul>
 *
 *  <li>It contains at most one prefix, which must be one of {EWC_ga,
 *  EWC_da, EWC_ba, EWC_ma, EWC_achen} and must be prefixable to the
 *  root letter.</li>
 *
 *  <li>It contains no vocalic modifications</li>
 *
 *  <li>It may or may not contain an a-chung
 *  (<code>\u0F71</code>)</li>
 *
 *  <li>It contains at most one vowel from the set {EWV_a, EWV_i,
 *  EWV_e, EWV_u}, and that vowel is on the root stack.  The one
 *  exception is that a 'i suffix is permitted (this is a connective
 *  case marker).</li>
 *
 *  <li>It has at most one suffix, which is a single consonant or the
 *  special connective case marker 'i (i.e.,
 *  <code>"\u0F60\u0F72"</code>).</li>
 *
 *
DLC FIXME: we must allow many suffixes.  See Andres' e-mail below:
<pre>
David,

It is a particle that means "or" as opposed to "dang" that means and.

"sgom pa'am" would mean "... or meditation"

You can also have "'ang" which would be equivalent to "yang" (also)

"sgom pa'ang" : even/also meditation.

And also there are cases where they combine. For ex you can have

"le'u'i'o". "le'u" means chapter. "le'u'i" means "of this chapter".
'o would mark the end of the sentence.

	Andres 
</pre>
 *
 *
 *  <li>It may contain a EWC_sa or EWC_da postsuffix iff there exists
 *  a suffix (and a suffix that is not the special connective case
 *  marker 'i (i.e., <code>"\u0F60\u0F72"</code>) (DLC FIXME: 'o and
 *  'am maybe?  I asked in the "Embarrasing error in wylie conversion"
 *  bug report.).</li>
 *
 *  <li>The root stack follows the rules of Tibetan syntax, meaning
 *  that the following holds:
 *
 *    <ul>
 *
 *       <li>the ra-mgo, sa-mgo, la-mgo head letters appear only over
 *       root consonants (root letters) that take them, if they
 *       appear</li>
 *
 *       <li>the wa-zur, ra-btags, ya-btags, and la-btags subjoined
 *       letters appear only under root consonants (root letters) that
 *       take them</li>
 *
 *       <li>at most one subscribed letter, except for the special
 *       case that ra-btags and wa-zur or ya-btags and wa-zur
 *       sometimes appear together.</li>
 *
 *       <li>the root stack may contain at most one head letter</li>
 *
 *    </ul>
 *
 *  </li>
 *
 *  </ul>
 *
 *  <p>Note that this class uses only a subset of Unicode to represent
 *  consonants and vowels.  In some situations, you should use {@link
 *  #EWSUB_wa_zur} to represent the consonant wa, while in others you
 *  should use {@link #EWC_wa}, even though you mean to subscribe a
 *  fixed-form wa.  Basically, stick to the characters for which
 *  enumerations exist in {@link
 *  org.thdl.tib.text.tshegbar.UnicodeConstants} and use your common
 *  sense.</p>
 *
 *  <p>For a pretty good, concise summary of the rules this class
 *  knows about, see Joe B. Wilson's <i>Translating Buddhism from
 *  Tibetan</i> from Snow Lion Publications, Appendix 1,
 *  e.g. p. 548.</p>
 *
 *  @author David Chandler */
public class LegalTshegBar
    extends TshegBar
    implements UnicodeConstants
{
    /** the prefixed consonant or EW_ABSENT */
    private char prefix;
    /** the consonant superscribed over the {@link #rootLetter} or
     *  EW_ABSENT */
    private char headLetter;
    /** the root consonant, never EW_ABSENT */
    private char rootLetter;
    /** subscribed letter, or EW_ABSENT */
    private char subjoinedLetter;
    /** true iff EWSUB_wa_zur is under the root syllable. */
    private boolean hasWaZur;
    /** true iff EW_wa_zur is under the root syllable. */
    private boolean hasAChung;
    /** If this is a string, it is of a single character or is equal
     *  to {@link #getConnectiveCaseSuffix()} */
    private String suffix;
    /** EW_da, EW_sa, or EW_ABSENT */
    private char postsuffix;
    /** EWV_i, EWV_u, EWV_e, EWV_o, or EW_ABSENT */
    private char vowel;

    /** Do not use this constructor. */
    private LegalTshegBar() { super(); }

    // DLC FIXME: do we want to accept EWC_ra or EWSUB_ra_btags for
    // the root letter, even if there is no head letter?  Etc.
    /** Constructs a valid Tibetan syllable or throws an exception.
     *  Use EW_ABSENT (or null in the case of <code>suffix</code>) for
     *  those parts of the syllable that are absent.  The root letter
     *  must not be absent.  To learn about the arguments, and to be
     *  sure that your input won't cause an exception to be thrown,
     *  see {@link
     *  #formsLegalTshegBar(char,char,char,char,boolean,boolean,String,char,char)}.
     *
     *  @exception IllegalArgumentException if the rootLetter is not
     *  one of the thirty consonants (and represented nominally, at
     *  that), or if one of the other arguments is not valid, or if
     *  postsuffix is present but suffix is absent, etc. */
    public LegalTshegBar(char prefix, char headLetter, char rootLetter,
                         char subjoinedLetter,
                         boolean hasWaZur,
                         boolean hasAChung,
                         String suffix, char postsuffix, char vowel)
        throws IllegalArgumentException
    {
        super();

        throwIfNotLegalTshegBar(prefix, headLetter, rootLetter,
                                subjoinedLetter, hasWaZur, hasAChung,
                                suffix, postsuffix, vowel);

        this.prefix = prefix;
        this.headLetter = headLetter;
        this.rootLetter = rootLetter;
        this.subjoinedLetter = subjoinedLetter;

        this.hasWaZur = hasWaZur;
        this.hasAChung = hasAChung;

        // copying is slightly inefficient because it is unnecessary
        // since Java strings are read-only, but translating this code
        // to C++ is easier this way.
        this.suffix = new String(suffix);

        this.postsuffix = postsuffix;
        this.vowel = vowel;
    }

    /** Like {@link
     *  #LegalTshegBar(char,char,char,char,boolean,boolean,String,char,char)}
     *  but geared for the common case where the suffix is simply a
     *  consonant. */
    public LegalTshegBar(char prefix, char headLetter, char rootLetter,
                         char subjoinedLetter,
                         boolean hasWaZur,
                         boolean hasAChung,
                         char suffix, char postsuffix, char vowel)
        throws IllegalArgumentException
    {
        this(prefix, headLetter, rootLetter, subjoinedLetter,
             hasWaZur, hasAChung, new String(new char[] { suffix }),
             postsuffix, vowel);
    }


    /** Returns the prefixed consonant, or EW_ABSENT if there is no
     *  prefix. */
    public char getPrefix() {
        return prefix;
    }

    /** Returns true iff this syllable contains a prefixed
     *  consonant. */
    public boolean hasPrefix() {
        return (EW_ABSENT != prefix);
    }

    /** Returns the non-EWSUB_wa_zur consonant subscribed to the root
     *  consonant, or EW_ABSENT if none is.  If you want to know if there is a wa-zur, use {@link #hasWaZurSubjoinedToRootLetter()}*/
    public char getSubjoinedLetter() {
        return subjoinedLetter;
    }

    /** Returns true iff the root letter possesses a subscribed
     *  consonant ya-btags, ra-btags, la-btags, or wa-zur. */
    public boolean hasSubjoinedLetter() {
        return (EW_ABSENT != subjoinedLetter);
    }

    public boolean hasWaZurSubjoinedToRootLetter() {
        return hasWaZur;
    }

    public boolean hasAChungOnRootLetter() {
        return hasAChung;
    }

    /** Returns null if there is no suffix, or a string containing the
     *  one consonant or a string <code>"\u0F60\u0F72"</code>
     *  containing two characters in the special case that the suffix
     *  is that connective case marker {@link
     *  #getConnectiveCaseSuffix()}. */
    public String getSuffix() {
        return suffix;
    }

    /** Returns true iff there is a suffixed consonant or a suffixed
     *  <code>'i</code> (DLC FIXME). */
    public boolean hasSuffix() {
        return (null != suffix);
    }

    /** Returns true iff there is a single, suffixed consonant.  This
        means that suffixes like <code>'am</code>, <code>'i</code>,
        <code>'u</code>, and <code>'o</code> are not present, but this
        does not rule out the presence of a postsuffix. */
    public boolean hasSimpleSuffix() {
        return ((null != suffix) && (1 == suffix.length()));
    }

    /** If this syllable {@link #hasSimpleSuffix() has a simple
        suffix}, this returns it.
        @exception Exception if {@link #hasSimpleSuffix()} is not true */
    public char getSimpleSuffix() throws Exception {
        if (!hasSimpleSuffix())
            throw new Exception("there isn't a simple suffix");
        return getSuffix().charAt(0);
    }

    /** Returns the secondary suffix, which is either
     *  EWC_da or EWC_sa, or EW_ABSENT if
     *  there is no postsuffix. */
    public char getPostsuffix() {
        return postsuffix;
    }

    /** Returns true iff there is a secondary suffix EWC_da or
     *  EWC_sa. */
    public boolean hasPostsuffix() {
        return (EW_ABSENT != postsuffix);
    }

    /** Returns true iff this syllable has a <code>'i</code>
     *  suffix. */
    public boolean hasConnectiveCaseMarkerSuffix() {
        return getSuffix().equals(getConnectiveCaseSuffix());
    }

    /** Returns the root consonant. */
    public char getRootLetter() {
        return rootLetter;
    }

    /** Returns the head letter of the root stack if it has one, or
     *  EW_ABSENT otherwise. */
    public char getHeadLetter() {
        return headLetter;
    }
    
    /** Returns true iff this syllable has a head letter. */
    public boolean hasHeadLetter() {
        return (EW_ABSENT != headLetter);
    }

    /** Returns the vowel, or EW_ABSENT if there is no {@link
     *  #hasExplicitVowel() explicit vowel} (the syllable has the
     *  built-in "ah" sound in this case). */
    public char getVowel() {
        // DLC assert this is one of { EWV_i, EWV_u, EWV_e, EWV_o }
        return vowel;
    }

    /** Returns false iff the implicit, built-in "ah" sound is the
        only vowel for the root stack. */
    public boolean hasExplicitVowel() {
        return (EW_ABSENT != vowel);
    }


    /** Returns a string of two characters, da and sa. */
    public static String getPossiblePostsuffixes() {
        return new String(new char[] { EWC_da, EWC_sa });
    }

    private final static String possibleSuffixes
        = new String(new char[] {
            EWC_ga, EWC_nga, EWC_da, EWC_na, EWC_ba, EWC_ma, EWC_achen,
            EWC_ra, EWC_la, EWC_sa
        });

    /** Returns a string of ten characters, each of which can be a
     *  suffix in Tibetan. */
    public static String getPossibleSuffixes() {
        return possibleSuffixes;

        // DLC unit test that each EWC is a nominal form of a consonant

        // you could use either \u0F62 or \u0F6A, but we won't confuse
        // this ra for a ra-mgo, so we use \u0F62, EWC_ra, not
        // EWSUB_ra_btags.
    }

    private final static String connectiveCaseSuffix
        = new String(new char[] {
            EWC_achen, EWV_i
        });

    /** Returns a two-character string consisting of the Unicode
     *  representation of what Extended Wylie calls
     *  <code>'i</code>. */
    public static String getConnectiveCaseSuffix() {
        return connectiveCaseSuffix;
    }

    private final static String thirtyConsonants
        = new String(new char[] {
            EWC_ga,  EWC_kha,  EWC_ga,     EWC_nga,
            EWC_ca,  EWC_cha,  EWC_ja,     EWC_nya,
            EWC_ta,  EWC_tha,  EWC_da,     EWC_na,
            EWC_pa,  EWC_pha,  EWC_ba,     EWC_ma,
            EWC_tsa, EWC_tsha, EWC_dza,    EWC_wa,
            EWC_zha, EWC_za,   EWC_achen,  EWC_ya,
            EWC_ra,  EWC_la,   EWC_sha,    EWC_sa,
            EWC_ha,  EWC_a
        });

    /** Returns a String containing the nominal Unicode
     *  representations of the thirty consonants.  The consonants are
     *  in the usual order you find them in the 8 row by 4 column
     *  table that students of the language memorize.
     *  @see org.thdl.tib.text.tshegbar.UnicodeConstants */
    public static String getTheThirtyConsonants() {
        ThdlDebug.verify(thirtyConsonants.length() == 30); // DLC put this into a JUnit test to avoid the slow-down.
        return thirtyConsonants;
    }

    /** Returns true iff x is the preferred, nominal Unicode
     *  representation of one the thirty consonants. */
    public static boolean isNominalRepresentationOfConsonant(char x) {
        return (-1 != getTheThirtyConsonants().indexOf(x));
    }


    /** Returns an array of Unicode strings, all the legal suffix
        particles.  In Extended Wylie, these are: <ul> <li>'i</li>
        <li>'o</li> <li>'u</li> <li>'am</li> </ul>
    
        <p>This is not very efficient.</p> */
    public static String[] getPossibleSuffixParticles() {
        return new String[] {
            new String(new char[] { EWC_achen, EWV_i }),
            new String(new char[] { EWC_achen, EWV_o }),
            new String(new char[] { EWC_achen, EWV_u }),
            new String(new char[] { EWC_achen, EWC_ma }),
        };
    }


    /** Returns a String containing the nominal Unicode
     *  representations of the five prefixes.  The prefixes are in
     *  dictionary order.
     *  @see org.thdl.tib.text.tshegbar.UnicodeConstants */
    public static String getTheFivePrefixes() {
        final String s = new String(new char[] {
            EWC_ga, EWC_da, EWC_ba, EWC_ma, EWC_achen
        });
        ThdlDebug.verify(s.length() == 5); // DLC put this into a JUnit test to avoid the slow-down.
        return s;
    }

    /** Returns true iff x is the preferred, nominal Unicode
     *  representation of one of the five prefixes. */
    public static boolean isNominalRepresentationOfPrefix(char x) {
        return (-1 != getTheFivePrefixes().indexOf(x));
    }

    /** Returns a String containing the nominal Unicode
     *  representations of the ten suffixes.  The suffixes are in
     *  dictionary order.
     *  @see #getConnectiveCaseSuffix()
     *  @see org.thdl.tib.text.tshegbar.UnicodeConstants */
    public static String getTheTenSuffixes() {
        final String s = new String(new char[] {
            EWC_ga, EWC_nga, EWC_da, EWC_na, EWC_ba,
            EWC_ma, EWC_achen, EWC_ra, EWC_la, EWC_sa
        });
        ThdlDebug.verify(s.length() == 10); // DLC put this into a JUnit test to avoid the slow-down.
        return s;
    }

    /** Returns true iff x is the preferred, nominal Unicode
     *  representation of one of the ten suffixes.
     *  @see #getConnectiveCaseSuffix()
     */
    public static boolean isNominalRepresentationOfSimpleSuffix(char x) {
        return (-1 != getTheTenSuffixes().indexOf(x));
    }


    /** Returns true iff the given (rootLetter, subjoinedLetter)
        combination can accept an additional wa-zur.  Only g-r-w,
        d-r-w, and ph-y-w fall into this category according to
        tibwn.ini. (DLC FIXME: are these all legal?  are any others?)

        @param rootLetter the root consonant (in {@link
        UnicodeUtils#isPreferredFormOfConsonant(char) preferred form} in
        you expect true to be returned)
        @param subjoinedLetter the letter subscribed to rootLetter,
        which should not {@link UnicodeUtils#isWa(char) be wa} if you
        expect true to be returned
        @return true iff (rootLetter, subjoinedLetter, wa-zur) is a
        legal stack. */
    public static boolean takesWaZur(char rootLetter,
                                     char subjoinedLetter) {

        // DLC NOW use this test

        if (EW_ABSENT == subjoinedLetter) {
            return isConsonantThatTakesWaZur(rootLetter);
        }
        if (EWSUB_ra_btags == subjoinedLetter) {
            if (EWC_ga == rootLetter
                    || EWC_da == rootLetter)
                return true;
        } else if (EWSUB_ya_btags == subjoinedLetter) {
            if (EWC_pha == rootLetter)
                return true;
        }
        return false;
    }

    /** Returns true iff rootLetter is a consonant to which wa-zur can
     *  be subjoined (perhaps in addition to another subjoined
     *  ra-btags or ya-btags. */
    public static boolean isConsonantThatTakesWaZur(char rootLetter) {
        return !(EWC_ka != rootLetter
                 && EWC_kha != rootLetter
                 && EWC_ga != rootLetter
                 && EWC_nya != rootLetter
                 && EWC_da != rootLetter
                 && EWC_tsa != rootLetter
                 && EWC_tsha != rootLetter
                 && EWC_zha != rootLetter
                 && EWC_za != rootLetter
                 && EWC_ra != rootLetter
                 && EWC_la != rootLetter
                 && EWC_sha != rootLetter
                 && EWC_pha != rootLetter /* ph-y-w is legal. */
                 && EWC_ha != rootLetter);
    }

    /** Returns true iff rootLetter is a consonant to which ya-btags
     *  can be subjoined. */
    public static boolean isConsonantThatTakesYaBtags(char rootLetter) {
        return !(EWC_ka != rootLetter
                 && EWC_kha != rootLetter
                 && EWC_ga != rootLetter
                 && EWC_pa != rootLetter
                 && EWC_pha != rootLetter
                 && EWC_ba != rootLetter
                 && EWC_ma != rootLetter
                 && EWC_ha != rootLetter);
    }

    /** Returns true iff rootLetter is a consonant to which la-btags
     *  can be subjoined. */
    public static boolean isConsonantThatTakesLaBtags(char rootLetter) {
        return !(EWC_ka != rootLetter
                 && EWC_ga != rootLetter
                 && EWC_ba != rootLetter
                 && EWC_ra != rootLetter
                 && EWC_sa != rootLetter

                 // this combination is pronounced as a
                 // prenasaling, low-tone <i>da</i> in my opinion:
                 && EWC_za != rootLetter);
    }


    /** Returns true iff rootLetter is a consonant to which ra-btags
     *  can be subjoined. */
    public static boolean isConsonantThatTakesRaBtags(char rootLetter) {
        return !(EWC_ka != rootLetter
                 && EWC_kha != rootLetter
                 && EWC_ga != rootLetter
                 && EWC_ta != rootLetter
                 && EWC_tha != rootLetter
                 && EWC_da != rootLetter
                 && EWC_na != rootLetter
                 && EWC_pa != rootLetter
                 && EWC_pha != rootLetter
                 && EWC_ba != rootLetter
                 && EWC_ma != rootLetter
                 && EWC_sa != rootLetter
                 && EWC_ha != rootLetter);
    }

    /** Returns true iff rootLetter is a consonant that takes a ra-mgo
     *  (pronounced <i>rango</i> because ma is a prenasaling prefix)
     *  head letter */
    public static boolean isConsonantThatTakesRaMgo(char rootLetter) {
        return !(EWC_ka != rootLetter
                 && EWC_ga != rootLetter
                 && EWC_nga != rootLetter
                 && EWC_ja != rootLetter
                 && EWC_nya != rootLetter
                 && EWC_ta != rootLetter
                 && EWC_da != rootLetter
                 && EWC_na != rootLetter
                 && EWC_ba != rootLetter
                 && EWC_ma != rootLetter
                 && EWC_tsa != rootLetter
                 && EWC_dza != rootLetter);
    }

    /** Returns true iff rootLetter is a consonant that takes a la-mgo
     *  (pronounced <i>lango</i> because ma is a prenasaling prefix)
     *  head letter */
    public static boolean isConsonantThatTakesLaMgo(char rootLetter) {
        return !(EWC_ka != rootLetter
                 && EWC_ga != rootLetter
                 && EWC_nga != rootLetter
                 && EWC_ca != rootLetter
                 && EWC_ja != rootLetter
                 && EWC_ta != rootLetter
                 && EWC_da != rootLetter
                 && EWC_pa != rootLetter
                 && EWC_ba != rootLetter
                 && EWC_ha != rootLetter); // pronunciation exception, btw
    }

    /** Returns true iff rootLetter is a consonant that takes a sa-mgo
     *  (pronounced <i>sango</i> because ma is a prenasaling prefix)
     *  head letter */
    public static boolean isConsonantThatTakesSaMgo(char rootLetter) {
        return !(EWC_ka != rootLetter
                 && EWC_ga != rootLetter
                 && EWC_nga != rootLetter
                 && EWC_nya != rootLetter
                 && EWC_ta != rootLetter
                 && EWC_da != rootLetter
                 && EWC_na != rootLetter
                 && EWC_pa != rootLetter
                 && EWC_ba != rootLetter
                 && EWC_ma != rootLetter
                 && EWC_tsa != rootLetter);
    }

    /** Returns true iff the given arguments form a legal Tibetan
     *  syllable.
     *
     *  @param prefix the optional, prefixed consonant
     *  @param headLetter the optional superscribed consonant
     *  @param rootLetter the mandatory root consonant
     *  @param subjoinedLetter the optional, subscribed consonant
     *  @param suffix the optional suffix, which is null, a String
     *  consisting of a single consonant (i.e. a single character)
     *  except in the special case that this is {@link
     *  #getConnectiveCaseSuffix()}
     *  @param postsuffix the optional postsuffix, which should be
     *  EWC_sa or EWC_da
     *  @param vowel the optional vowel */
    public static boolean formsLegalTshegBar(char prefix,
                                             char headLetter,
                                             char rootLetter,
                                             char subjoinedLetter,
                                             boolean hasWaZur,
                                             boolean hasAChung,
                                             String suffix,
                                             char postsuffix,
                                             char vowel)
    {
        try {
            return internalLegalityTest(prefix, headLetter, rootLetter,
                                        subjoinedLetter, hasWaZur, hasAChung,
                                        suffix, postsuffix, vowel, false);
        } catch (IllegalArgumentException e) {
            throw new Error("This simply cannot happen, but it did.");
        }
    }

    /** Like {@link
     *  #formsLegalTshegBar(char,char,char,char,boolean,boolean,String,char,char)}
     *  but geared for the common case where the suffix is simply a
     *  consonant. */
    public static boolean formsLegalTshegBar(char prefix,
                                             char headLetter,
                                             char rootLetter,
                                             char subjoinedLetter,
                                             boolean hasWaZur,
                                             boolean hasAChung,
                                             char suffix,
                                             char postsuffix,
                                             char vowel)
    {
        return formsLegalTshegBar(prefix, headLetter, rootLetter,
                                  subjoinedLetter, hasWaZur, hasAChung,
                                  new String(new char[] { suffix }),
                                  postsuffix, vowel);
    }


    /** If you get through this gauntlet without having an exception
     *  thrown, then this combination makes a legal Tibetan syllable.
     *  @exception IllegalArgumentException if the syllable does not
     *  follow the rules of a Tibetan syllable.  To learn about the
     *  arguments, see {@link
     *  #formsLegalTshegBar(char,char,char,char,boolean,boolean,String,char,char)}. */
    private static void throwIfNotLegalTshegBar(char prefix,
                                                char headLetter,
                                                char rootLetter,
                                                char subjoinedLetter,
                                                boolean hasWaZur,
                                                boolean hasAChung,
                                                String suffix,
                                                char postsuffix,
                                                char vowel)
        throws IllegalArgumentException
    {
        internalLegalityTest(prefix, headLetter, rootLetter,
                             subjoinedLetter, hasWaZur, hasAChung,
                             suffix, postsuffix, vowel, true);
    }

    /** Voodoo.  Stand back. */
    private static boolean internalThrowThing(boolean doThrow, String msg)
    {
        if (doThrow)
            throw new IllegalArgumentException(msg);
        return false;
    }

    /** If you get through this gauntlet without having an exception
     *  thrown, then this combination makes a legal Tibetan syllable.
     *  To learn about the arguments, see {@link
     *  #formsLegalTshegBar(char,char,char,char,boolean,boolean,String,char,char)}.
     *  @return true if this syllable is legal, false if this syllable
     *  is illegal and throwIfIllegal is false, does not return if
     *  this syllable is illegal and throwIfIllegal is true
     *  @exception IllegalArgumentException if the syllable does not
     *  follow the rules of a Tibetan syllable and throwIfIllegal is
     *  true */
    private static boolean internalLegalityTest(char prefix,
                                                char headLetter,
                                                char rootLetter,
                                                char subjoinedLetter,
                                                boolean hasWaZur,
                                                boolean hasAChung,
                                                String suffix,
                                                char postsuffix,
                                                char vowel,
                                                boolean throwIfIllegal)
        throws IllegalArgumentException
    {
        if (!isNominalRepresentationOfConsonant(rootLetter))
            return internalThrowThing(throwIfIllegal,
                                      "The root letter must be one of the standard thirty Tibetan consonants, and must be represented nominally, not, for example, by FIXED-FORM RA (\\u0F6A)");

        if (EW_ABSENT != prefix) {
            // Ensure that this prefix is one of the five prefixes,
            // and that it can go with this root letter:
            if (!isNominalRepresentationOfPrefix(prefix))
                return internalThrowThing(throwIfIllegal,
                                          "The prefix is not absent, so it must be one of the five possible prefixes.");
            // DLC test that it can go with the root letter.
        }

        if (EW_ABSENT != subjoinedLetter) {
            if (EWSUB_ya_btags == subjoinedLetter) {
                if (!isConsonantThatTakesYaBtags(rootLetter)) {
                    return internalThrowThing(throwIfIllegal,
                                              "Cannot subscribe ya-btags to that root letter.");
                }
            } else if (EWSUB_ra_btags == subjoinedLetter) {
                if (!isConsonantThatTakesRaBtags(rootLetter)) {
                    return internalThrowThing(throwIfIllegal,
                                              "Cannot subscribe ra-btags to that root letter.");
                }
            } else if (EWSUB_la_btags == subjoinedLetter) {
                if (!isConsonantThatTakesLaBtags(rootLetter)) {
                    return internalThrowThing(throwIfIllegal,
                                              "Cannot subscribe la-btags to that root letter.");
                }
            } else if (EWSUB_wa_zur == subjoinedLetter) {
                throw new Error("DLC FIXME: can this happen?  wa-zur comes in via the boolean argument hasWaZur, not via subjoinedLetter.");
            } else {
                // check for a common mistake:
                if ('\u0FBA' == subjoinedLetter
                    || '\u0FBB' == subjoinedLetter
                    || '\u0FBC' == subjoinedLetter)
                    {
                        return internalThrowThing(throwIfIllegal,
                                                  "The subjoined letter given is subjoinable, but you gave the fixed-form variant, which is not used in Tibetan syllables but is sometimes used in Tibetan transliteration of Sanskrit, Chinese, or some non-Tibetan language.");
                    }
                return internalThrowThing(throwIfIllegal,
                                          "The subjoined letter given is not one of the four consonants that may be subscribed.");
            }
        } // subjoinedLetter tests

        // Suffix tests:
        // DLC NOW -- allow 'o, 'u, 'am, etc.
        if (null != suffix) {
            if (!getConnectiveCaseSuffix().equals(suffix)) {
                if (suffix.length() != 1) {
                    return internalThrowThing(throwIfIllegal,
                                              "Illegal suffix -- not one of the legal complex suffixes like 'u, 'o, 'i, 'am.");
                }
                if (!isNominalRepresentationOfSimpleSuffix(suffix.charAt(0))) {
                    return internalThrowThing(throwIfIllegal,
                                              "Illegal suffix -- not one of the ten legal suffixes: "
                                              + UnicodeUtils.unicodeCharToString(suffix.charAt(0)));
                }
            }
        }
        if (EW_ABSENT != postsuffix) {
            if (null == suffix)
                return internalThrowThing(throwIfIllegal,
                                          "You cannot have a postsuffix unless you also have a suffix.");
        }

        if (EW_ABSENT != headLetter) {
            if (EWC_ra == headLetter) {
                if (!isConsonantThatTakesRaMgo(rootLetter)) {
                    return internalThrowThing(throwIfIllegal,
                                              "The head letter ra cannot be used with that root letter.");
                }
            } else if (EWC_la == headLetter) {
                if (!isConsonantThatTakesLaMgo(rootLetter)) {
                    return internalThrowThing(throwIfIllegal,
                                              "The head letter la cannot be used with that root letter.");
                }
            } else if (EWC_sa == headLetter) {
                if (!isConsonantThatTakesSaMgo(rootLetter)) {
                    // handle a common error specially:
                    if (EWC_la == rootLetter)
                        return internalThrowThing(throwIfIllegal,
                                                  "sa cannot be a head letter atop the root letter la.  You probably meant to have sa the root letter and la the subjoined letter.");

                    return internalThrowThing(throwIfIllegal,
                                              "The head letter sa cannot be used with that root letter.");
                }
            } else {
                // '\u0F6A' is not a valid head letter, even for
                // "rnya".  Use EWC_ra instead.
                return internalThrowThing(throwIfIllegal,
                                          "The head letter given is not valid.");
            }
        } // headLetter tests

        // Now see if the vowel is valid:
        if (EW_ABSENT /* built-in "ah" sound */ != vowel) {
            if (EWV_i != vowel
                && EWV_u != vowel
                && EWV_e != vowel
                && EWV_o != vowel)
                {
                    if (EWC_achen == vowel)
                        return internalThrowThing(throwIfIllegal,
                                                  "The vowel given is not valid.  Use EW_ABSENT for the EWC_achen sound.");
                    if ('\u0F71' == vowel)
                        return internalThrowThing(throwIfIllegal,
                                                  "a-chung cannot be used in a simple Tibetan syllable.");
                    return internalThrowThing(throwIfIllegal,
                                              "The vowel given is not valid.");
                }
        }

        // Phew.  We got here, so this combination of inputs is valid.
        return true;
    }


    /*
      DLC add a method giving the correct connective case thingy or
      throwing error if the 'i suffix already appears.

      DLC put in a method that gets pronunciation using Unicode
      diacritical marks.  And another using just US Roman.  Note that
      pronunciation is contextual, so have these methods return all
      valid pronunciations, such as both "pa" and "wa" for EWC_ba.

      DLC would be nice in the appropriate class: boolean
      isTransliteratedSanskrit(), boolean isTransliteratedChinese()
      (design: contains fa or va, maybe?). */

    /** Returns a StringBuffer that holds the extended wylie
     *  representation of this syllable. */
    public StringBuffer getExtendedWylie() {
        StringBuffer sb = new StringBuffer();
        char rootLetter = getRootLetter();
        if (hasPrefix()) {
            // if there is a prefix but no head letter and (prefix,
            // rootLetter) is ambiguous, i.e. if it could be mistaken
            // for a legal (rootLetter, subjoinedLetter) combination,
            // then put out prefix,disambiguator.  else just put out
            // prefix.

            boolean disambiguatorNeeded = false;
            char prefix = getPrefix();
            sb.append(UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar(prefix));
            if (!hasHeadLetter()) {
                if (EWC_ya == rootLetter) {
                    if (isConsonantThatTakesYaBtags(prefix))
                        disambiguatorNeeded = true;
                } else if (EWC_ra == rootLetter) {
                    if (isConsonantThatTakesRaBtags(prefix))
                        disambiguatorNeeded = true;
                } else if (EWC_la == rootLetter) {
                    if (isConsonantThatTakesLaBtags(prefix))
                        disambiguatorNeeded = true;
                } else if (EWC_wa == rootLetter) {
                    if (isConsonantThatTakesWaZur(prefix))
                        disambiguatorNeeded = true;
                }
            }
            if (disambiguatorNeeded)
                sb.append(TibetanMachineWeb.WYLIE_DISAMBIGUATING_KEY);
        }
        if (hasHeadLetter())
            sb.append(UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar(getHeadLetter()));
        sb.append(UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar(rootLetter));
        if (hasSubjoinedLetter())
            sb.append(UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar(getSubjoinedLetter()));
        if (hasWaZurSubjoinedToRootLetter())
            sb.append(UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar(EWSUB_wa_zur));

        // a-chung is treated, in Extended Wylie, like a vowel.  I.e.,
        // you don't have 'pAa', you have 'pA'.
        if (hasAChungOnRootLetter()) {
            if (hasExplicitVowel()) {
                if (EWV_i == getVowel()) {
                    sb.append(UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar('\u0F73'));
                } else if (EWV_u == getVowel()) {
                    sb.append(UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar('\u0F75'));
                } else if (EWV_e == getVowel() || EWV_o == getVowel()) {
                    // The exception to the rule for a-chung and vowels...

                    // DLC FIXME: are these allowed in legal Tibetan?
                    // EWTS would have special cases for them if so,
                    // I'd wager...
                    sb.append(UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar(EW_achung));
                    sb.append(UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar(getVowel()));
                } else {
                    ThdlDebug.abort("only simple vowels occur in this class, how did this get past internalLegalityTest(..)?");
                }
            } else {
                sb.append(UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar(EW_achung));
            }
        } else {
            if (hasExplicitVowel())
                sb.append(UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar(getVowel()));
            else
                sb.append("a");
        }

        if (hasSuffix()) {
            String suf = getSuffix();
            sb.append(UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar(suf.charAt(0)));
            if (suf.length() > 1) {
                // DLC assert, don't verify, that the length is two.
                // This could change if I learn of more suffix
                // particles.
                ThdlDebug.verify(2 == suf.length());
                sb.append(UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar(suf.charAt(1)));
            }
        }
        if (hasPostsuffix())
            sb.append(UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar(getPostsuffix()));
        return sb;
    }


    // DLC: toXML for the dense XML
    /** Returns a <legalTibetanSyllable> element that contains only
     *  the Extended Wylie transliteration for the whole syllable and a note that the . */
    public String toConciseXML() {
        // DLC version-control the EWTS document. 0.5 is used below:
        return ("<legalTibetanSyllable "
                + "transliterationType=\"THDL Extended Wylie 0.5\" "
                + "transliteration=\"" + getExtendedWylie() + "\"" + "/>");
    }

    /** Returns a <legalTibetanSyllable> element that contains the
     *  syllable broken-down into its constituent vowel and
     *  consonants. */
    public String toVerboseXML() {
        // DLC version-control the EWTS document. 0.5 is used below:
        return ("<legalTibetanSyllable "
                + "transliterationType=\"THDL Extended Wylie 0.5\" "
                + (hasPrefix()
                   ? ("prefix=\""
                      + UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar(getPrefix()) + "\" ")
                   : "")
                + (hasHeadLetter()
                   ? ("headLetter=\""
                      + UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar(getHeadLetter())
                      + "\" ")
                   : "")
                + ("rootLetter=\""
                   + UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar(getRootLetter()) + "\" ")
                + (hasSubjoinedLetter()
                   ? ("subjoinedLetter=\""
                      + UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar(getSubjoinedLetter())
                      + "\" ")
                   : "")
                + (hasWaZurSubjoinedToRootLetter()
                   ? "hasWaZurSubjoinedToRootLetter=\"true\""
                   : "")
                + (hasAChungOnRootLetter()
                   ? "hasAChungOnRootLetter=\"true\""
                   : "")

                // DLC NOW: what about the root letter a, i.e. \u0F68 ?  do we want the EWTS to be 'aa' ?
                + ("vowel=\""
                   + (hasExplicitVowel()
                      ? UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar(getVowel())
                      : "a")
                   + "\" ")
                + (hasSuffix()
                   ? ("suffix=\""
                      + UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeString(getSuffix())
                      + "\" ")
                   : "")
                + (hasPostsuffix()
                   ? ("postsuffix=\""
                      + UnicodeCharToExtendedWylie.getExtendedWylieForUnicodeChar(getPostsuffix())
                      + "\" ")
                   : "")
                + "/>");
    }


    /** Overrides {@link org.thdl.tib.text.tshegbar.UnicodeReadyThunk}
        method to return {@link UnicodeUtils#toCanonicalForm(String)
        canonically-formed Unicode}.
        @exception UnsupportedOperationException is never thrown */
    public String getEquivalentUnicode() {
        StringBuffer sb = new StringBuffer();
        if (hasPrefix()) {
            ThdlDebug.verify(UnicodeUtils.isNonSubjoinedConsonant(getPrefix()));
            sb.append(getPrefix());
        }
        if (hasHeadLetter()) {
            // DLC FIXME this crap won't be true...
            ThdlDebug.verify(UnicodeUtils.isNonSubjoinedConsonant(getPrefix()));
            ThdlDebug.verify(UnicodeUtils.isSubjoinedConsonant(getRootLetter()));
            sb.append(getHeadLetter());
        } else {
            ThdlDebug.verify(UnicodeUtils.isNonSubjoinedConsonant(getRootLetter()));
        }
        sb.append(getRootLetter());
        if (hasSubjoinedLetter()) {
            ThdlDebug.verify(UnicodeUtils.isSubjoinedConsonant(getSubjoinedLetter()));
            sb.append(getSubjoinedLetter());
        }
        if (hasWaZurSubjoinedToRootLetter()) {
            ThdlDebug.verify(UnicodeUtils.isSubjoinedConsonant(EWSUB_wa_zur));
            sb.append(EWSUB_wa_zur);
        }
        if (hasAChungOnRootLetter()) {
            ThdlDebug.verify('\u0F71' == EW_achung);
            sb.append(EW_achung);
        }
        if (hasExplicitVowel()) {
            sb.append(getVowel());
        }
        if (hasSuffix()) {
            ThdlDebug.verify(UnicodeUtils.isNonSubjoinedConsonant(getSuffix().charAt(0)));
            sb.append(getSuffix());
        }
        if (hasPostsuffix()) {
            ThdlDebug.verify(UnicodeUtils.isNonSubjoinedConsonant(getPostsuffix()));
            sb.append(getPostsuffix());
        }
        return sb.toString();
    }

    /** Overrides {@link org.thdl.tib.text.tshegbar.UnicodeReadyThunk}
        method to return true. */
    public boolean hasEquivalentUnicode() {
        return true;
    }


    /** Returns a descriptive XML element. */
    public String toString() {
        return toConciseXML();
    }
}