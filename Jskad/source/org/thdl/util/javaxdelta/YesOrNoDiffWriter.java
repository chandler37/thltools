/* Added by David Chandler, based on stuff in this package. */

/* 
 *
 * Copyright (c) 2001 Torgeir Veimo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 *
 */

package org.thdl.util.javaxdelta;


import java.util.*;
import java.io.*;

/** We look to see that one special DATA statement and only one COPY statment
    are generated.  If so, there's no difference between the input and
    the output.  Otherwise there is.
    @author David Chandler */
public class YesOrNoDiffWriter implements DiffWriter {
    
    public YesOrNoDiffWriter() {}

    private int numCopies = 0;
    private int numDatas = 0;
    public void addCopy(int offset, int length) throws IOException {
        if (numCopies == 1) numCopies = 2;
        if (numCopies == 0) numCopies = 1;
    }
    
    public void addData(byte b) throws IOException {
        if (numDatas == 1) numDatas = 2;
        if (numDatas == 0) numDatas = 1;
    }

    public boolean areFilesDifferent() {
        return numCopies != 1 || numDatas != 1;
    }

    private void writeBuf() { }
    public void flush() throws IOException { }
    public void close() throws IOException { }
}
