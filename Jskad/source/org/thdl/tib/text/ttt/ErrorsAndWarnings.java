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
Library (THDL). Portions created by the THDL are Copyright 2004 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.ttt;

import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlOptions;

import java.util.HashMap;

/** A noninstantiable class that knows about every user-visible error
 *  or warning message.  Each has a unique integer key starting at 101
 *  for those messages that are errors and starting at 501 for those
 *  messages that are warnings.  This class knows which messages are
 *  enabled for a given warning level (which is customizable via user
 *  preferences), whether a message is a warning or an error (which
 *  could be made configurable at runtime -- easily if you just want
 *  to upgrade a warning to an error -- FIXME), and how to produce
 *  both a short and a long error message.
 *
 *  @author David Chandler */
public class ErrorsAndWarnings {
    /** Don't instantiate this class. */
    private ErrorsAndWarnings() { }

    /** Maps int -> severityString, where severityString is
        "ERROR".intern() for errors or "All".intern(),
        "Some".intern(), or "Most".intern() for warnings that are
        enabled or "DISABLED".intern() for disabled
        warnings/errors. */
    private static HashMap severityMap = new HashMap();

    static {
        setupSeverityMapFromBuiltinDefaults();
    }

    /** Returns higher numbers for higher severity. */
    private static int severityStringToInteger(String sev) {
        if (sev == "ERROR") return Integer.MAX_VALUE;
        if (sev == "Some") return Integer.MAX_VALUE - 1;
        if (sev == "Most") return Integer.MAX_VALUE - 2;
        if (sev == "All") return Integer.MAX_VALUE - 3;
        return 0;
    }
    /** Returns true if and only if sev1 is at least as severe as
        sev2.  "ERROR" means an error, the highest severity; "Some" is
        the most severe warning; "Most" and "All" follow.  Other
        values are less severe than these.
        @param sev1 an interned String or null
        @param sev2 an interned String or null */
    private static boolean severityGreaterThanOrEquals(String sev1,
                                                       String sev2) {
        return severityStringToInteger(sev1) >= severityStringToInteger(sev2);
    }

    /** Returns true if and only if the warning or error with number
        code is enabled for the given warningLevel.  Errors are
        enabled regardless of warningLevel. */
    static boolean isEnabled(int code, String warningLevel) {
        // unknown codes appear to be disabled, but let's make sure
        // that no unknown code is used during development:
        ThdlDebug.verify("Unknown error/warning code " + code,
                         null != severityMap.get(new Integer(code)));

        return severityGreaterThanOrEquals((String)severityMap.get(new Integer(code)),
                                           warningLevel);
    }

    /** Returns true if and only if code is an error and not a warning
        at the moment. */
    static boolean isError(int code) {
        return ("ERROR" == severityMap.get(new Integer(code)));
    }

    /** Returns an error or warning message concerning the snippet of
        ACIP or EWTS translit.  The warning or error
        number is code, and the message will be very short, like "101:
        {NNYA}" if short is true, or longer and self-contained if
        short is false.  Note that you cannot call this for certain
        messages that take more than one "parameter", if you will,
        like message 501. */
    static String getMessage(int code, boolean shortMessages,
                             String translit) {
        // Let's make sure that no unknown code is used during
        // development:
        ThdlDebug.verify("unknown code " + code,
                         null != severityMap.get(new Integer(code)));

        if (shortMessages) {
            if ("(".equals(translit)
                || ")".equals(translit)
                || "{".equals(translit)
                || "}".equals(translit)
                || "[".equals(translit)
                || "]".equals(translit)
                || "<".equals(translit)
                || ">".equals(translit))
                return "" + code + ": '" + translit + "'";
            else
                return "" + code + ": {" + translit + "}";
        }
        // else:
        switch (code) {

        // ERRORS:
        case 101:
            return "" + code + ": There's not even a unique, non-illegal parse for {" + translit + "}";

        case 102:
            return "" + code + ": Found an open bracket, '" + translit + "', within a [#COMMENT]-style comment.  Brackets may not appear in comments.";

        case 103:
            return "" + code + ": Found a truly unmatched close bracket, '" + translit + "'.";

        case 104:
            return "" + code + ": Found a closing bracket, '" + translit + "', without a matching open bracket.  Perhaps a [#COMMENT] incorrectly written as [COMMENT], or a [*CORRECTION] written incorrectly as [CORRECTION], caused this.";

        case 105:
            return "" + code + ": Found a truly unmatched open bracket, '[' or '{', prior to this current illegal open bracket, '" + translit + "'.";

        case 106:
            return "" + code + ": Found an illegal open bracket (in context, this is '" + translit + "').  Perhaps there is a [#COMMENT] written incorrectly as [COMMENT], or a [*CORRECTION] written incorrectly as [CORRECTION], or an unmatched open bracket?";

        case 107:
            return "" + code + ": Found an illegal at sign, @ (in context, this is " + translit + ").  This folio marker has a period, '.', at the end of it, which is illegal.";

        case 108:
            return "" + code + ": Found an illegal at sign, @ (in context, this is " + translit + ").  This folio marker is not followed by whitespace, as is expected.";

        case 109:
            return "" + code + ": Found an illegal at sign, @ (in context, this is " + translit + ").  @012B is an example of a legal folio marker.";

        case 110:
            /*
              //NYA\\ appears in ACIP input, and I think it means
              /////NYA/.  We warn about // for this reason.  \\ causes
              a tsheg-bar //error.
            */
            return "" + code + ": Found //, which could be legal (the Unicode would be \\u0F3C\\u0F3D), but is likely in an illegal construct like //NYA\\\\.";

        case 111:
            return "" + code + ": Found an illegal open parenthesis, '('.  Nesting of parentheses is not allowed.";

        case 112:
            return "" + code + ": Unexpected closing parenthesis, ')', found.";

        case 113:
            return "" + code + ": The ACIP {?}, found alone, may intend U+0F08, but it may intend a question mark, i.e. '?', in the output.  It may even mean that the original text could not be deciphered with certainty, like the ACIP {[?]} does.";

        case 114:
            return "" + code + ": Found an illegal, unprintable character.";

        case 115:
            return "" + code + ": Found a backslash, \\, which the ACIP Tibetan Input Code standard says represents a Sanskrit virama.  In practice, though, this is so often misused (to represent U+0F3D) that {\\} always generates this error.  If you want a Sanskrit virama, change the input document to use {\\u0F84} instead of {\\}.  If you want U+0F3D, use {/NYA/} or {/NYA\\u0F3D}.";

        case 116:
            ThdlDebug.verify(translit.length() == 1);
            return "" + code + ": Found an illegal character, '" + translit + "', with ordinal (in decimal) " + (int)translit.charAt(0) + ".";

        case 117:
            return "" + code + ": Unexpected end of input; truly unmatched open bracket found.";

        case 118:
            return "" + code + ": Unmatched open bracket found.  A comment does not terminate.";

        case 119:
            return "" + code + ": Unmatched open bracket found.  A correction does not terminate.";

        case 120:
            return "" + code + ": Slashes are supposed to occur in pairs, but the input had an unmatched '/' character.";

        case 121:
            return "" + code + ": Parentheses are supposed to occur in pairs, but the input had an unmatched parenthesis, '('.";

        case 122:
            return "" + code + ": Warning, empty tsheg bar found while converting from ACIP!";

        case 123:
            return "" + code + ": Cannot convert ACIP {" + translit + "} because it contains a number but also a non-number.";

        case 124:
            return "" + code + ": Cannot convert ACIP {" + translit + "} because {V}, wa-zur, appears without being subscribed to a consonant.";

        case 125:
            return "" + code + ": Cannot convert ACIP {" + translit + "} because we would be required to assume that {A} is a consonant, when it is not clear if it is a consonant or a vowel.";

        case 126:
            return "" + code + ": Cannot convert ACIP {" + translit + "} because it ends with a '+'.";

        case 127:
            return "" + code + ": Cannot convert ACIP {" + translit + "} because it ends with a '-'.";

        case 128: // fall through
        case 129:
            throw new Error("No; error messages 128 and 129 are handled elsewhere.");

        case 130:
            return "" + code + ": The tsheg bar (\"syllable\") {" + translit + "} is essentially nothing.";

        case 131:
            return "" + code + ": The ACIP caret, {^}, must precede a tsheg bar.";

        case 132:
            return "" + code + ": The ACIP {" + translit + "} must be glued to the end of a tsheg bar, but this one was not.";

        case 133:
            return "" + code + ": Cannot convert the ACIP {" + translit + "} to Tibetan because it is unclear what the result should be.";

        case 134:
            return "" + code + ": The tsheg bar (\"syllable\") {" + translit + "} has no legal parses.";

        case 135:
            ThdlDebug.verify(translit.length() == 1);
            return "" + code + ": The Unicode escape '" + translit + "' with ordinal (in decimal) " + (int)translit.charAt(0) + " is specified by the Extended Wylie Transliteration Scheme (EWTS), but is in the private-use area (PUA) of Unicode and will thus not be written out into the output lest you think other tools will be able to understand this non-standard construction.";

        case 136:
            ThdlDebug.verify(translit.length() == 1);
            return "" + code + ": The Unicode escape with ordinal (in decimal) " + (int)translit.charAt(0) + " does not match up with any TibetanMachineWeb glyph.";

        // See 137 below.

        case 138:
            ThdlDebug.verify(translit.length() == 1);
            return "" + code + ": The Unicode escape '" + translit + "' with ordinal (in decimal) " + (int)translit.charAt(0) + " is in the Tibetan range of Unicode (i.e., [U+0F00, U+0FFF]), but is a reserved code in that area.";






        // WARNINGS (by default):
        case 501:
            throw new Error("Nah -- we handle this one in the code because the message is complicated for 501");

        case 502:
            return "" + code + ": The last stack does not have a vowel in {" + translit + "}; this may indicate a typo, because Sanskrit, which this probably is (because it's not legal Tibetan), should have a vowel after each stack.";

        case 503:
            return "" + code + ": Though {" + translit + "} is unambiguous, it would be more computer-friendly if '+' signs were used to stack things because there are two (or more) ways to interpret this ACIP if you're not careful.";

        case 504:
            return "" + code + ": The ACIP {" + translit + "} is treated by this converter as U+0F35, but sometimes might represent U+0F14 in practice.  To avoid seeing this warning again, change the input to use {\\u0F35} instead of {" + translit + "}.";

        case 505:
            return "" + code + ": There is a useless disambiguator in {" + translit + "}.";

        case 506:
            return "" + code + ": There is a stack of three or more consonants in {" + translit + "} that uses at least one '+' but does not use a '+' between each consonant.";

        case 507:
            return "" + code + ": There is a chance that the ACIP {" + translit + "} was intended to represent more consonants than we parsed it as representing -- NNYA, e.g., means N+NYA, but you can imagine seeing N+N+YA and typing NNYA for it too.";

        case 508: // see 509 also
            return "" + code + ": The ACIP {" + translit + "} has been interpreted as two stacks, not one, but you may wish to confirm that the original text had two stacks as it would be an easy mistake to make to see one stack (because there is such a stack used in Sanskrit transliteration for this particular sequence) and forget to input it with '+' characters.";

        case 509: // see 508 also
            return "" + code + ": The ACIP {" + translit + "} has an initial sequence that has been interpreted as two stacks, a prefix and a root stack, not one nonnative stack, but you may wish to confirm that the original text had two stacks as it would be an easy mistake to make to see one stack (because there is such a stack used in Sanskrit transliteration for this particular sequence) and forget to input it with '+' characters.";

        case 510:
            return "" + code + ": A non-breaking tsheg, '" + translit + "', appeared, but not like \"...,\" or \".,\" or \".dA\" or \".DA\".";

        // ERROR 137 and WARNING 511 are the same:
        case 137: /* fall through */
        case 511:
            return "" + code + ": The ACIP {" + translit + "} cannot be represented with the TibetanMachine or TibetanMachineWeb fonts because no such glyph exists in these fonts.";




        // NEVER HAPPENS:
        default:
            ThdlDebug.verify("switch statement is missing a case",
                             false);
            return "unknown error or warning with number " + code;
        }
    }

    

    /** Returns true iff warningLevel is one of "All".intern(),
        "Most".intern(), or "Some".intern(). */
    static boolean warningLevelIsKnown(String warningLevel) {
        return (warningLevel == "Some"
                || warningLevel == "Most"
                || warningLevel == "All");
    }

    private static final int MIN_ERROR = 101; // inclusive
    private static final int MAX_ERROR = 138; // inclusive

    private static final int MIN_WARNING = 501; // inclusive
    private static final int MAX_WARNING = 511; // inclusive

    private static void setupSeverityMapFromBuiltinDefaults() {
        // errors:
        for (int i = MIN_ERROR; i <= MAX_ERROR; i++) {
            severityMap.put(new Integer(i), "ERROR");
        }

        // warnings:
        String[] defaultSeverities = new String[] {
            // 501:
            "Most",
            // 502:
            "All",
            // 503:
            "All",
            // 504:
            "Some",
            // 505:
            "Some",
            // 506:
            "Some",
            // 507:
            "Most",
            // 508:
            "Some",
            // 509:
            "Most",
            // 510:
            "Some",
            // 511:
            "Some",
        };
        for (int num = MIN_WARNING; num <= MAX_WARNING; num++) {
            String opt = ThdlOptions.getStringOption("thdl.acip.to.tibetan.warning.severity." + num);
            if (null != opt) {
                opt = opt.intern();
                if ("None" == opt || "DISABLED" == opt)
                    opt = "DISABLED";
                else if (!(opt == "Most"
                           || opt == "All"
                           || opt == "Some"))
                    opt = null;
            } else {
                if (!ThdlOptions.getBooleanOption("thdl.acip.to.tibetan.warning.and.error.severities.are.built.in.defaults"))
                    ThdlDebug.verify("options.txt is gone?", false);
            }
            ThdlDebug.verify((null == opt) || opt.intern() == opt);
            severityMap.put(new Integer(num), (null != opt) ? opt : defaultSeverities[num - 501]);
        }

 // DLC FIXME: make 506 an error?  or a new, super-high priority class of warning?
 // DLC FIXME: you can't turn 504 or 510 down (e.g., to an "All"-level warning)
    }

    /** Prints out the long forms of the error messages, which will
        help a user to decipher the short forms. */
    public static void printErrorAndWarningDescriptions(java.io.PrintStream out) {
        final String translit = "X";
        out.println("ACIP->Tibetan ERRORS are as follows, and appear in their short forms, embedded");
        out.println("in the output, like [#ERROR 130: {X}]:");
        out.println("");
        for (int num = MIN_ERROR; num <= MAX_ERROR; num++) {
            if (128 == num) {
                out.println("128: Cannot convert ACIP {" + translit + "} because " + "A:" + " is a \"vowel\" without an associated consonant.");
            } else if (129 == num) {
                out.println("129: Cannot convert ACIP {" + translit + "} because " + "+" + " is not an ACIP consonant.");
            } else {
                out.println(getMessage(num, false, translit));
            }
            out.println("");
        }

        out.println("ACIP->Tibetan WARNINGS are as follows, and appear in their short forms, embedded");
        out.println("in the output, like [#WARNING 510: {X}]:");
        out.println("");
        for (int num = MIN_WARNING; num <= MAX_WARNING; num++) {
            if (501 == num) {
                out.println("501: Using " + translit + ", but only because the tool's knowledge of prefix rules (see the documentation) says that " + "XX" + " is not a legal Tibetan tsheg bar (\"syllable\")");
            } else {
                out.println(getMessage(num, false, translit));
            }
            out.println("");
        }
    }
}
