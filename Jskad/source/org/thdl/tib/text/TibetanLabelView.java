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

package org.thdl.tib.text;

import javax.swing.*; 
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;

/** A TibetanLabelView is a LabelView that has its own idea, informed
 *  by its knowledge of Tibetan, about where a good place to break
 *  text is.
 *
 *  <p>
 *
 *  If Character.isWhiteSpace() could be overridden, and if that only
 *  affected breaking (which is doubtful), we wouldn't need this--we'd
 *  just treat Tibetan punctuation there.  We might also like to
 *  override java.awt.font.GlyphMetrics idea of whitespace (though I'm
 *  not sure what consequences besides breaking that might have).  But
 *  we can't override either since they're final.  So we roll our own.
 *
 *  @author David Chandler */
class TibetanLabelView extends LabelView {
    /** Creates a new TibetanLabelView. */
    public TibetanLabelView(Element e) {
        super(e);
        // FIXME: assert (e == this.getElement())
    }

    public int getBreakWeight(int axis, float pos, float len) {
        if (View.X_AXIS != axis) {
            // This doesn't impact line wrapping.
            return super.getBreakWeight(axis, pos, len);
        } else {
            int startPos = this.getElement().getStartOffset();

            int boundedPos = getPosNearTheEnd(startPos, pos, len);

            // boundedPos is short, and can be as short as startPos.
            // I don't know when to say something is good as opposed
            // to bad, but calling everything bad didn't work so well.
            // So let's call boundedPos <= startPos bad and everything
            // else without whitespace or tshegs et cetera good.
            if (boundedPos <= startPos)
                return View.BadBreakWeight;

            if (getGoodBreakingLocation(startPos, boundedPos) >= 0)
                return View.ExcellentBreakWeight;
            else
                return View.GoodBreakWeight;
        }
    }

    public View breakView(int axis, int p0, float pos, float len) {
        if (View.X_AXIS != axis) {
            // This doesn't impact line wrapping.
            return super.breakView(axis, p0, pos, len);
        } else {
            int boundedPos = getPosNearTheEnd(p0, pos, len);

            if (p0 == boundedPos) {
                // We can't call createFragment safely.  Return the
                // current view.
                return this;
            } else {
                int bloc = getGoodBreakingLocation(p0, boundedPos);
                int whereToBreak;
                if (bloc >= 0)
                    whereToBreak = bloc;
                else
                    whereToBreak = boundedPos;
                /* Return a new view, a fragment of the current one.
                 * If createFragment isn't smart, we could create
                 * infinitely many views of the same text if we don't
                 * check to see that this new view is actually
                 * different than the current view. */
                if (this.getStartOffset() != p0
                    || this.getEndOffset() != whereToBreak) {
                    return createFragment(p0, whereToBreak);
                } else
                    return this;
            }
        }
    }

    /** Returns an offset >= 0 if we find a character (FIXME: before
     *  or after?) where breaking would be good.  Returns negative
     *  otherwise. */
    private int getGoodBreakingLocation(int startOffset, int endOffset) {

        // Grab the underlying characters:
        Segment seggy = this.getText(startOffset, endOffset);

        //        System.out.println("DLC: getGoodBreakingLocation(start=" + startOffset + ", end=" + endOffset + "\"" + new String(seggy.array, seggy.offset, seggy.count) + "\"");

        // Now look for whitespace:
        //
        // FIXME: does going backwards or forwards matter?
        char currentChar = seggy.first();
        for (; currentChar != Segment.DONE; currentChar = seggy.next()) {
            // FIXME: eeek!  How do we know when we're dealing with
            // Tibetan and when we're not?  I'm assuming it's all
            // Tibetan, all the time.
            if (Character.isWhitespace(currentChar)
                || '-' /* FIXME: this is the TSHEG (i.e., the Wylie is ' '), but we have no constant for it. */ == currentChar
                || ' ' /* FIXME: this is space (i.e., the Wylie is '_'), but we have no constant for it. */ == currentChar

                // FIXME: am I missing anything?  move this into TibetanMachineWeb, anyway.
                )
                {
                    //                    System.out.println("DLC: We've got a good place to break: " + (startOffset + seggy.getIndex() - seggy.getBeginIndex()
                    //                        + 1));
                    return startOffset + seggy.getIndex() - seggy.getBeginIndex()
                        + 1 /* FIXME: why this foo work so good? */
                        ;
                }
        }
        // System.out.println("DLC: We DO NOT have any good place to break.");
        return -1;
    }

    /** Returns a position just before or at the position specified by
     *  the three arguments.  viewToModel seems like the thing to use,
     *  but we don't have the parameters to pass to it.  We can call
     *  GlyphView.GlyphPainter.getBoundedPosition(..)  instead, and
     *  its comment mentions viewToModel, so maybe this is actually
     *  better.
     */
    private int getPosNearTheEnd(int startPos, float pos, float len) {
        // this is provided, and it appears that we'd better use it:
        checkPainter();

        return this.getGlyphPainter().getBoundedPosition(this, startPos,
                                                         pos, len);
    }
}
