Attribute VB_Name = "NewMacros"

Sub conversionHelp()
    Dim msgTitle, msg As String
    
    msgTitle = "Conversion Macro Help"
    msg = "The shortcut keys for the conversion macros are as follows:" & vbCrLf & _
        vbTab & "Convert Document to XML:" & vbTab & "    Ctrl + Alt + c" & vbCrLf & _
        vbTab & "Convert Italics to Specific Styles: Alt + i" & vbCrLf & _
        vbTab & "Display This Help Message: " & vbTab & "    Alt + Shift + ?"
    res = MsgBox(msg, vbOKOnly, msgTitle)
        
End Sub

Sub convertToXML()
    Dim teiHead, flName, temp, writepath As String
    Dim rng As Range
    
    ActiveDocument.Endnotes.Convert
    writepath = ThisDocument.Path
    
    ' Replace entities
    Application.StatusBar = "Proceessing document: replacing entities!"
    replaceEntities
    
    ' Do Metadata
    Application.StatusBar = "Proceessing document: converting metadata!"
    teiHead = doMetadata
    
    ' Deal with italicized commas
    Application.StatusBar = "Proceessing document: de-italicizing commas!"
    unItalicizeCommas
    
    'Deal with tables
    Application.StatusBar = "Processing document: processing tables!"
    doTables
    
    ' Do specific character styles
    Application.StatusBar = "Proceessing document: processing character styles!"
    doCharacterStyles
    
    ' Do the Links
    Application.StatusBar = "Proceessing document: processing links!"
    doLinks
    
    ' Do FootNotes
    Application.StatusBar = "Proceessing document: processing foot- or endnotes!"
    doFootNotes
    doNoteTags
    
    ' Do the Italics Bold and Underline
    Application.StatusBar = "Proceessing document: marking bold, italic, and underline!"
    doItalicsBoldUnderline
    
    ' Do the Paragraph tags
    doParas
    
    ' Do the Divs
    Application.StatusBar = "Proceessing document: marking outline divisions!"
    doDivs
    
    ' Clean up
    replaceAllEmptyP
    
    Application.StatusBar = "Proceessing document: finalizing markup!"
    Selection.HomeKey unit:=wdStory
    Selection.TypeParagraph
    Selection.MoveUp unit:=wdLine, Count:=1
    Selection.Style = "Normal,no"
    Selection.TypeText Text:=teiHead
    Selection.EndKey unit:=wdStory
    
    Selection.TypeText Text:="</body></text></TEI.2>"
    Selection.EndKey unit:=wdStory, Extend:=wdExtend
    Selection.Cut
    
    Selection.HomeKey unit:=wdStory
    MsgBox ("Conversion complete! Copy the results and paste into your XML editor!")
    
    Exit Sub

End Sub
Sub doDivs()
    Dim tagName, closeTag, lastTag, headNum, styleName  As String
    Dim cnt, ind1, ind2, lvl, lastLvl, numOfLevels As Integer
    
    numOfLevels = findHighestHeader()
    ActiveWindow.ActivePane.View.Type = wdMasterView
    ActiveWindow.View.ShowHeading 9
    cnt = ActiveWindow.ActivePane.Document.Paragraphs.Count - 1
    With Selection
        .HomeKey unit:=wdStory
        lastLvl = 0
        For n = 1 To cnt
            styleName = .Style
            If InStr(styleName, "Head") > 0 Then
                ind1 = InStr(styleName, " ") + 1
                ind2 = InStr(styleName, ",") - ind1
                headNum = Mid(styleName, ind1, ind2)
                lvl = Val(headNum)
                If numOfLevels < 8 Then
                    tagName = "div" & headNum
                    closeTag = tagName
                Else:
                    tagName = "div n=""" & lvl & """"
                    closeTag = "div"
                End If
                If lastLvl = lvl Then
                    .TypeText ("</" & closeTag & ">")
                ElseIf lastLvl > lvl Then
                    For lvNum = lastLvl To lvl Step -1
                       If numOfLevels < 8 Then
                            .TypeText ("</div" & lvNum & ">")
                       Else:
                            .TypeText ("</div>")
                       End If
                    Next lvNum
                End If
                lastLvl = lvl
                .TypeText ("<" & tagName & "><head>")
                If n < cnt Then
                    .MoveDown unit:=wdParagraph, Count:=1
                    .MoveLeft unit:=wdCharacter, Count:=1
                Else
                    .EndKey unit:=wdStory
                End If
                .TypeText ("</head>")
             End If
             .MoveDown unit:=wdParagraph, Count:=1
        Next n
        ActiveWindow.ActivePane.View.Type = wdNormalView
        .EndKey unit:=wdStory
        For n = lvl To 1 Step -1
            If numOfLevels < 8 Then
                .TypeText ("</div" & n & ">")
            Else
                .TypeText ("</div>")
            End If
        Next n
    End With
    
    
End Sub
Function findHighestHeader() As Integer
    Dim para As Paragraph
    Dim lvl, highest As Integer
    highest = 1
    For Each para In ActiveDocument.Paragraphs
        styleName = para.Style
        If InStr(styleName, "Head") > 0 Then
            ind1 = InStr(styleName, " ") + 1
            ind2 = InStr(styleName, ",") - ind1
            headNum = Mid(styleName, ind1, ind2)
            lvl = Val(headNum)
            If lvl > highest Then highest = lvl
        End If
    Next para
    findHighestHeader = highest
End Function
Sub doParas()

    Dim inList, inVerse As Boolean
    Dim rng As Range
    Dim openTag, endTag, tagName As String
    Dim ct As Integer
    
    inList = False: inVerse = False
    Selection.HomeKey unit:=wdStory
    
    ct = ActiveDocument.Paragraphs.Count

    For n = 1 To ct
        Set para = ActiveDocument.Paragraphs(n)
        Application.StatusBar = "Proceessing document: marking paragraphs (" & n & ")!"
        If InStr(para.Style, "Heading") > 0 Then GoTo 50
        If isTable(para) Then GoTo 50
        tagName = "": openTag = "": endTag = ""
        Select Case para.Style
        
            Case "List Number,ln"
               If inVerse Then
                  inVerse = False
                  openTag = "</lg>"
               End If
               If Not inList Then
                  openTag = openTag & "<list rend=""1"">"
                  inList = True
               End If
               tagName = "item"
               
            Case "List Bullet,lb"
               If inVerse Then
                  inVerse = False
                  openTag = "</lg>"
               End If
               If Not inList Then
                  openTag = openTag & "<list rend=""bullet"">"
                  inList = True
               End If
               tagName = "item"
               
            Case "Citation Prose,cp"
               If inList Then
                  openTag = "</list>"
                  inList = False
               End If
               If inVerse Then
                  inVerse = False
                  openTag = "</lg>"
               End If
               tagName = "q"
               
            Case "Citation Verse 1,cv1"
               If inList Then
                  openTag = "</list>"
                  inList = False
               End If
               If Not inVerse Then
                  openTag = openTag & "<lg>"
                  inVerse = True
               End If
               tagName = "l"
               
            Case "Citation Verse 2,cv2"
               If inList Then
                  openTag = "</list>"
                  inList = False
               End If
               If Not inVerse Then
                  openTag = openTag & "<lg>"
                  inVerse = True
               End If
               tagName = "l"
               
            Case Else
               If inList Then
                  openTag = "</list>"
                  inList = False
               ElseIf inVerse Then
                  openTag = "</lg>"
                  inVerse = False
               End If
               tagName = "p"
         End Select

        Set rng = ActiveDocument.Paragraphs(n).Range
        rng.StartOf
        rng.Text = openTag & "<" & tagName & ">"
  
        Set rng = ActiveDocument.Paragraphs(n).Range
        rng.End = rng.End - 1
        rng.EndOf
        rng.Text = "</" & tagName & ">" & endTag
        ActiveDocument.Paragraphs(n).Style = "Normal,no"
  
50    Next n

End Sub
Function isTable(ByVal para As Paragraph) As Boolean
    Dim tct, pst, pend, tst, tend As Integer
    pst = para.Range.Start
    pend = para.Range.End
    tct = ActiveDocument.Tables.Count
    For n = 1 To tct
        With ActiveDocument.Tables(n)
            tst = .Range.Start
            tend = .Range.End
        End With
        If pst >= tst And pst <= tend Then
            isTable = True
            Exit Function
        End If
        If pend >= tst And pend <= tend Then
            isTable = True
            Exit Function
        End If
    Next n
    isTable = False
End Function
Sub doTables()
    Dim tble As Table
    Dim wrkCell As Cell
    Dim outStr As String
    Dim rwInd As Integer
    Dim insertRng As Range
    
    While ActiveDocument.Tables.Count > 0
        outStr = "<list rend=""table""><item>"
        Set tble = ActiveDocument.Tables.Item(1)
        Set wrkCell = tble.Cell(1, 1)
        rwInd = 1
        While Not (wrkCell Is Nothing)
            If wrkCell.Row.Index > rwInd Then
                outStr = outStr & "</item><item>"
            End If
            rwInd = wrkCell.RowIndex
            outStr = outStr & addToTable(wrkCell)
            Set wrkCell = wrkCell.Next
        Wend
        outStr = outStr & "</item></list>"
        Set insertRng = tble.Range
        insertRng.Collapse wdCollapseStart
        tble.Delete
        insertRng.Select
        Selection.TypeText Text:=outStr
    Wend
End Sub
Function addToTable(ByVal cl As Cell) As String
    Dim rng As Range
    Set rng = cl.Range
    rng.End = rng.End - 1
    addToTable = "<rs>" & rng.Text & "</rs>"
End Function
Sub doFootNotes()
Attribute doFootNotes.VB_Description = "Macro recorded 8/12/2003 by Than G"
Attribute doFootNotes.VB_ProcData.VB_Invoke_Func = "Project.NewMacros.FootnoteTest"
'
' FootnoteTest Macro
' Macro recorded 8/12/2003 by Than G
'
    Dim fn As Footnote
    Dim n, fnNum As Integer
    Dim rng As Range
    
    ActiveDocument.Endnotes.Convert
    fnNum = ActiveDocument.Footnotes.Count
    If fnNum > 0 Then
        For n = 1 To fnNum
            doFootnoteItalEtc (n)
            Set fn = ActiveDocument.Footnotes.Item(n)
            fn.Range.Select
            Selection.Text = Replace(Selection.Text, Chr(13), "<!--[newLine]-->")
            Selection.Copy
            ActiveWindow.ActivePane.Close
            With Selection
                .GoTo what:=wdGoToFootnote, Which:=wdGoToFirst, Count:=n, Name:=""
                .TypeText Text:="~"
                .Paste
                .TypeText Text:="^"
                .MoveRight unit:=wdCharacter, Count:=1, Extend:=wdExtend
            End With
            
        Next n
        
        n = 1
        Do
            ActiveDocument.Footnotes.Item(1).Delete
            n = n + 1
            If n > fnNum Then Exit Do
        Loop While ActiveDocument.Footnotes.Count > 0
    End If
End Sub

Sub doLinks()
    Dim hyl As Hyperlink
    Dim n, hyNum As Integer
    Dim rng As Range
    Dim addr As String
    
    hyNum = ActiveDocument.Hyperlinks.Count
    
    For n = 1 To hyNum
        Set hyl = ActiveDocument.Hyperlinks.Item(n)
        addr = hyl.Address
        Set rng = hyl.Range
        With rng.Font
            .Italic = False
            .Bold = False
            .Underline = wdUnderlineNone
        End With
        rng.StartOf
        rng.Text = "<xref n=""" & addr & """>"
        Set rng = hyl.Range
        rng.End = rng.End - 1
        rng.EndOf
        rng.Text = "</xref>"
    Next n

End Sub

Sub doItalicsBoldUnderline()
    Dim rngToSearch, srchResult, wrng As Range
    Dim fcount, addOn, y, max As Integer
    Dim doingFeet As Boolean

    ' Do the body of the text now
    Set rngToSearch = ActiveDocument.Range
    Set srchResult = rngToSearch.Duplicate
    max = 100
    
    ' Search and Replace ITALICS
    y = 0
    Do
        y = y + 1
        With srchResult.Find
            .ClearFormatting
            .Format = True
            .Text = ""
            .Font.Italic = True
            .Wrap = wdFindStop
            .Forward = True
            .Execute
        End With
        
        If Not srchResult.Find.Found Then Exit Do
        
        Set wrng = srchResult.Duplicate
        addOn = 1
        If wrng.Style = "Normal,no" Or InStr(wrng.Style, "List") > 0 Then
            If Not isCharStyle(wrng) Then
                Set wrng = fixRange(wrng)
                wrng.Font.Italic = False
                wrng.StartOf
                wrng.Text = "<hi rend=""weak"">"
                Set wrng = srchResult.Duplicate
                wrng.End = wrng.End - 1
                wrng.EndOf
                wrng.Text = "</hi>"
                wrng.Font.Italic = False
                addOn = 5
            End If
        End If
        srchResult.Start = wrng.End + addOn
        srchResult.End = rngToSearch.End
        If y > 100 Then Exit Do
    Loop Until Not srchResult.Find.Found
    
    '  Search and Replace BOLD
    y = 0
    Set srchResult = rngToSearch.Duplicate
    Do
        y = y + 1
        With srchResult.Find
            .ClearFormatting
            .Format = True
            .Text = ""
            .Font.Bold = True
            .Wrap = wdFindStop
            .Forward = True
            .Execute
        End With
        
        If Not srchResult.Find.Found Then Exit Do
        
        Set wrng = srchResult.Duplicate
        addOn = 1
        If wrng.Style = "Normal,no" Or InStr(wrng.Style, "List") > 0 Then
            If Not isCharStyle(wrng) Then
                Set wrng = fixRange(wrng)
                wrng.Font.Bold = False
                wrng.StartOf
                wrng.Text = "<hi rend=""strong"">"
                Set wrng = srchResult.Duplicate
                wrng.End = wrng.End - 1
                wrng.EndOf
                wrng.Text = "</hi>"
                wrng.Font.Bold = False
                addOn = 5
            End If
        End If
        srchResult.Start = wrng.End + addOn
        srchResult.End = rngToSearch.End
        If y > 100 Then Exit Do
    Loop Until Not srchResult.Find.Found
    
    ' Do Underline
    y = 0
    Set srchResult = rngToSearch.Duplicate
    Do
        y = y + 1
        With srchResult.Find
            .ClearFormatting
            .Format = True
            .Text = ""
            .Font.Underline = True
            .Wrap = wdFindStop
            .Forward = True
            .Execute
        End With
        
        If Not srchResult.Find.Found Then Exit Do
        
        Set wrng = srchResult.Duplicate
        addOn = 1
        If wrng.Style = "Normal,no" Or InStr(wrng.Style, "List") > 0 Then
            If Not isCharStyle(wrng) Then
                Set wrng = fixRange(wrng)
                wrng.Font.Underline = wdUnderlineNone
                wrng.StartOf
                wrng.Text = "<hi rend=""underline"">"
                Set wrng = srchResult.Duplicate
                wrng.End = wrng.End - 1
                wrng.EndOf
                wrng.Text = "</hi>"
                wrng.Font.Underline = wdUnderlineNone
                addOn = 5
            End If
        End If
        srchResult.Start = wrng.End + addOn
        srchResult.End = rngToSearch.End
        If y > 100 Then Exit Do
    Loop Until Not srchResult.Find.Found
    
End Sub


Function doMetadata() As String
Attribute doMetadata.VB_Description = "Macro recorded 8/13/2003 by Than G"
Attribute doMetadata.VB_ProcData.VB_Invoke_Func = "Project.NewMacros.tableaccess"

'
' tableaccess Macro
' Macro recorded 8/13/2003 by Than G
'
    Dim metaTable As Table
    Dim inData, header As String
    
    Open "C:\xml\teiHeader.dat" For Input As #1
    
    Do While Not EOF(1)
        Line Input #1, inData
        header = header & inData & vbCrLf
    Loop
        
    Close #1
    
    Set metaTable = ActiveDocument.Tables(1)
    
    header = Replace(header, "{Eng Lang}", doTrim(metaTable.Cell(3, 2)))
    header = Replace(header, "{Eng Title}", doTrim(metaTable.Cell(1, 2)))
    header = Replace(header, "{Orig Lang}", doTrim(metaTable.Cell(3, 4)))
    header = Replace(header, "{Orig Title}", doTrim(metaTable.Cell(2, 2)))
    header = Replace(header, "{Author}", doTrim(metaTable.Cell(4, 2)))
    header = Replace(header, "{Author Date}", doTrim(metaTable.Cell(4, 4)))
    header = Replace(header, "{Trans}", doTrim(metaTable.Cell(5, 2)))
    header = Replace(header, "{Trans Date}", doTrim(metaTable.Cell(5, 4)))
    header = Replace(header, "{Editor}", doTrim(metaTable.Cell(6, 2)))
    header = Replace(header, "{Editor Date}", doTrim(metaTable.Cell(6, 4)))
    header = Replace(header, "{Markup}", doTrim(metaTable.Cell(7, 2)))
    header = Replace(header, "{Markup Date}", doTrim(metaTable.Cell(7, 4)))
    header = Replace(header, "{Journal Title}", doTrim(metaTable.Cell(3, 2)))
    header = Replace(header, "{Editor Coll}", doTrim(metaTable.Cell(9, 2)))
    header = Replace(header, "{Vol}", doTrim(metaTable.Cell(8, 4)))
    header = Replace(header, "{Pages}", doTrim(metaTable.Cell(8, 6)))
    header = Replace(header, "{Pub Place}", doTrim(metaTable.Cell(11, 2)))
    header = Replace(header, "{Pub Per}", doTrim(metaTable.Cell(10, 2)))
    header = Replace(header, "{Pub Date}", doTrim(metaTable.Cell(10, 4)))
    header = Replace(header, "{Description}", doTrim(metaTable.Cell(12, 2)))
    header = Replace(header, "{Domain URL}", doTrim(metaTable.Cell(13, 2)))
    header = Replace(header, "{Domain Text}", doTrim(metaTable.Cell(13, 4)))
    header = Replace(header, "{Portal URL}", doTrim(metaTable.Cell(14, 2)))
    header = Replace(header, "{Portal Text}", doTrim(metaTable.Cell(14, 4)))
    header = Replace(header, "{Project URL}", doTrim(metaTable.Cell(15, 2)))
    header = Replace(header, "{Project Text}", doTrim(metaTable.Cell(15, 4)))
    header = Replace(header, "{Home URL}", doTrim(metaTable.Cell(16, 2)))
    header = Replace(header, "{Home Text}", doTrim(metaTable.Cell(16, 4)))
    
    ActiveDocument.Tables(1).Delete
    
    doMetadata = header
    
End Function

Function doTrim(ByVal aCell As Cell) As String
    
    Dim rng As Range
    
    Set rng = aCell.Range
    rng.End = rng.End - 1
    doTrim = rng.Text
    
    
End Function
Function fixRange(ByVal rng As Range) As Range
    Dim gtPlace, ltPlace As Integer
    
    rng.Select
    rng.Style = "Plain Text"
    rng.Font.Bold = False
    rng.Font.Italic = False
    rng.Font.Underline = wdUnderlineNone
    
    gtPlace = InStr(rng.Text, ">")
    ltPlace = InStr(rng.Text, "<")
    If gtPlace < ltPlace Then
        If gtPlace > 0 Then rng.Start = rng.Start + gtPlace
        If ltPlace > 0 Then rng.End = rng.Start + ltPlace - 1
    ElseIf gtPlace > ltPlace Then
        rng.End = rng.Start + ltPlace - 1
    End If
    Set fixRange = rng
End Function

Sub doNoteTags()
    Selection.Find.ClearFormatting
    Selection.Find.Replacement.ClearFormatting
    With Selection.Find
        .Text = "~"
        .Replacement.Text = "<note>"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = False
        .MatchCase = False
        .MatchWholeWord = False
        .MatchWildcards = False
        .MatchSoundsLike = False
        .MatchAllWordForms = False
    End With
    Selection.Find.Execute Replace:=wdReplaceAll
    
    Selection.Find.ClearFormatting
    Selection.Find.Replacement.ClearFormatting
    With Selection.Find
        .Text = "^"
        .Replacement.Text = "</note>"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = False
        .MatchCase = False
        .MatchWholeWord = False
        .MatchWildcards = False
        .MatchSoundsLike = False
        .MatchAllWordForms = False
    End With
    Selection.Find.Execute Replace:=wdReplaceAll

End Sub

Sub doFootnoteItalEtc(ByVal fn As Integer)
Attribute doFootnoteItalEtc.VB_Description = "Macro recorded 8/13/2003 by Than G"
Attribute doFootnoteItalEtc.VB_ProcData.VB_Invoke_Func = "Project.NewMacros.closeFootnotes"
'
' closeFootnotes Macro
' Macro recorded 8/13/2003 by Than G
'
      'ActiveDocument.Endnotes.Convert
      Dim loopCt, addOn As Integer
      
        loopCt = 0
        Set rngToSearch = ActiveDocument.Footnotes.Item(fn).Range
        rngToSearch.End = rngToSearch.End - 1
        Set srchResult = rngToSearch.Duplicate
        Do
            loopCt = loopCt + 1
            With srchResult.Find
                .ClearFormatting
                .Format = True
                .Text = ""
                .Font.Italic = True
                .Wrap = wdFindStop
                .Forward = True
                .Execute
            End With
            
            If Not srchResult.Find.Found Then Exit Do
            addOn = 2
            Set wrng = srchResult.Duplicate
            If Not isCharStyle(wrng) Then
                wrng.StartOf
                wrng.Text = "<hi rend=""weak"">"
                Set wrng = srchResult.Duplicate
                wrng.End = wrng.End - 1
                wrng.EndOf
                wrng.Text = "</hi>"
                addOn = 5
            End If
            srchResult.Start = wrng.End + addOn
            srchResult.End = rngToSearch.End
            If loopCt > 10 Then Exit Do
        Loop Until Not srchResult.Find.Found
        
        loopCt = 0
        Set srchResult = rngToSearch.Duplicate
        Do
            loopCt = loopCt + 1
            With srchResult.Find
                .ClearFormatting
                .Format = True
                .Text = ""
                .Font.Bold = True
                .Wrap = wdFindStop
                .Forward = True
                .Execute
            End With
            
            If Not srchResult.Find.Found Then Exit Do
            
            Set wrng = srchResult.Duplicate
            addOn = 2
            If Not isCharStyle(wrng) Then
                wrng.StartOf
                wrng.Text = "<hi rend=""strong"">"
                Set wrng = srchResult.Duplicate
                wrng.End = wrng.End - 1
                wrng.EndOf
                wrng.Text = "</hi>"
                addOn = 5
            End If
            srchResult.Start = wrng.End + addOn
            srchResult.End = rngToSearch.End
            If loopCt > 10 Then Exit Do
        Loop Until Not srchResult.Find.Found
        
        loopCt = 0
        Set srchResult = rngToSearch.Duplicate
        Do
            loopCt = loopCt + 1
            With srchResult.Find
                .ClearFormatting
                .Format = True
                .Text = ""
                .Font.Underline = True
                .Wrap = wdFindStop
                .Forward = True
                .Execute
            End With
            
            If Not srchResult.Find.Found Then Exit Do
            
            Set wrng = srchResult.Duplicate
            addOn = 2
            If Not isCharStyle(wrng) Then
                wrng.StartOf
                wrng.Text = "<hi rend=""underline"">"
                Set wrng = srchResult.Duplicate
                wrng.End = wrng.End - 1
                wrng.EndOf
                wrng.Text = "</hi>"
            End If
            srchResult.Start = wrng.End + addOn
            srchResult.End = rngToSearch.End
            If loopCt > 10 Then Exit Do
        Loop Until Not srchResult.Find.Found

      End Sub

Sub replaceAllEmptyP()
Attribute replaceAllEmptyP.VB_Description = "Macro recorded 8/18/2003 by Than G"
Attribute replaceAllEmptyP.VB_ProcData.VB_Invoke_Func = "Project.NewMacros.replaceAllP"
'
' replaceAllP Macro
' Macro recorded 8/18/2003 by Than G
'
    Selection.Find.ClearFormatting
    Selection.Find.Replacement.ClearFormatting
    With Selection.Find
        .Text = "<p></p>"
        .Replacement.Text = ""
        .Forward = True
        .Wrap = wdFindContinue
        .Format = False
        .MatchCase = False
        .MatchWholeWord = False
        .MatchWildcards = False
        .MatchSoundsLike = False
        .MatchAllWordForms = False
    End With
    Selection.Find.Execute Replace:=wdReplaceAll
End Sub
Sub convertItalics()
Attribute convertItalics.VB_Description = "Macro recorded 8/18/2003 by Than G"
Attribute convertItalics.VB_ProcData.VB_Invoke_Func = "Project.NewMacros.searchItalics"
'
' searchItalics Macro
' Macro recorded 8/18/2003 by Than G
'
    Dim rngToSearch, srchResult As Range
    Dim textRun, openTag, endTage As String
    Dim resInt, loopCt As Integer
    
    loopCt = 0
    unItalicizeCommas

    Load ItalicOptions
    ItalicOptions.Hide
    
    Set rngToSearch = ActiveDocument.Range.Duplicate
    If ActiveDocument.Tables.Count > 0 Then
        rngToSearch.Start = ActiveDocument.Tables(1).Range.End + 1
    End If
    Set srchResult = rngToSearch.Duplicate
    
    Do
        loopCt = loopCt + 1
        With srchResult.Find
            .ClearFormatting
            .Format = True
            .Text = ""
            .Font.Italic = True
            .Wrap = wdFindStop
            .Forward = True
            .Execute
        End With
        
        If Not srchResult.Find.Found Or loopCt > 200 Then Exit Do
        If InStr(srchResult.Style, "Heading") = 0 And Not isCharStyle(srchResult) Then
            textRun = srchResult.Text
            ItalicOptions.ItalText.Caption = textRun
            ItalicOptions.Show
            If ItalicOptions.wasCancelled Then
                Unload ItalicOptions
                Exit Sub
            End If
            srchResult.Style = ItalicOptions.getSelectedStyle()
            If ItalicOptions.getSelectedStyle() = "Normal,no" Then
                srchResult.Font.Italic = False
            End If
        End If
        srchResult.Start = srchResult.End + 1
        srchResult.End = rngToSearch.End
        
    Loop Until Not srchResult.Find.Found
    
    convertFootnoteItalics
  End Sub
  Sub convertFootnoteItalics()
    Dim loopCt As Integer
    
    ActiveDocument.Endnotes.Convert
    For n = 1 To ActiveDocument.Footnotes.Count
        Set rngToSearch = ActiveDocument.Footnotes(n).Range.Duplicate
        Set srchResult = rngToSearch.Duplicate
        loopCt = 0
        
        Do
            loopCt = loopCt + 1
            With srchResult.Find
                .ClearFormatting
                .Format = True
                .Text = ""
                .Font.Italic = True
                .Wrap = wdFindStop
                .Forward = True
                .Execute
            End With
            
            If Not srchResult.Find.Found Or loopCt > 20 Then Exit Do
            If InStr(srchResult.Style, "Heading") = 0 And Not isCharStyle(srchResult) Then
                textRun = srchResult.Text
                ItalicOptions.ItalText.Caption = textRun
                ItalicOptions.Show
                If ItalicOptions.wasCancelled Then
                    Unload ItalicOptions
                    Exit Sub
                End If
                srchResult.Style = ItalicOptions.getSelectedStyle()
                If ItalicOptions.getSelectedStyle() = "Normal,no" Then
                    srchResult.Font.Italic = False
                End If
            End If
            srchResult.Start = srchResult.End + 2
            srchResult.End = rngToSearch.End
            
        Loop Until Not srchResult.Find.Found
    
    Next n
    
End Sub
Sub doCharacterStyles()
    Dim styleData(50, 3) As String
    Dim styleCount As Integer
    Dim rngToSearch, srchResult, wrng As Range
    
    styleData(1, 1) = "Emphasis Weak,ew": styleData(1, 2) = "<hi rend=""weak"">": styleData(1, 3) = "</hi>"
    styleData(2, 1) = "Text Title,tt": styleData(2, 2) = "<title>": styleData(2, 3) = "</title>"
    styleData(3, 1) = "Lang Tibetan,tib": styleData(3, 2) = "<foreign lang=""tib"">": styleData(3, 3) = "</foreign>"
    styleData(4, 1) = "Lang Sanskrit,san": styleData(4, 2) = "<foreign lang=""san"">": styleData(4, 3) = "</foreign>"
    styleData(5, 1) = "Lang Chinese,chi": styleData(5, 2) = "<foreign lang=""chi"">": styleData(5, 3) = "</foreign>"
    styleData(6, 1) = "Lang Japanese,jap": styleData(6, 2) = "<foreign lang=""jap"">": styleData(6, 3) = "</foreign>"
    styleData(7, 1) = "Name Personal Human,nph": styleData(7, 2) = "<persName type=""human"">": styleData(7, 3) = "</persName>"
    styleData(8, 1) = "Name Personal other,npo": styleData(8, 2) = "<persName type=""other"">": styleData(8, 3) = "</persName>"
    styleData(9, 1) = "Name Place,np": styleData(9, 2) = "<placeName>": styleData(9, 3) = "</placeName>"
    styleData(10, 1) = "Name organization,nor": styleData(10, 2) = "<orgName>": styleData(10, 3) = "</orgName>"
    styleData(11, 1) = "Reference,rf": styleData(11, 2) = "<ref type=""bibl"">": styleData(11, 3) = "</ref>"
    styleData(12, 1) = "Emphasis Strong,es": styleData(12, 2) = "<hi rend=""strong"">": styleData(12, 3) = "</hi>"
    styleData(13, 1) = "Dates , dt": styleData(13, 2) = "<date>": styleData(13, 3) = "</date>"
    styleCount = 13
    
    For x = 1 To styleCount
      Set rngToSearch = ActiveDocument.Range
      Set srchResult = rngToSearch.Duplicate
        y = 0
        Do
              Application.StatusBar = "Proceessing document: processing character styles (" & styleData(x, 1) & ")! " & y

            With srchResult.Find
                .ClearFormatting
                .Format = True
                .Style = styleData(x, 1)
                .Text = ""
                .Wrap = wdFindStop
                .Forward = True
                .Execute
            End With
            
            If Not srchResult.Find.Found Or InStr(srchResult.Text, "</hi></hi>") > 0 Then Exit Do
            
            Set wrng = fixRange(srchResult.Duplicate)
            wrng.Select
            Selection.Style = "Plain Text"
            wrng.StartOf
            wrng.Text = styleData(x, 2)
            wrng.Select
            Selection.Style = "Plain Text"
            Set wrng = srchResult.Duplicate
            wrng.EndOf
            wrng.Text = styleData(x, 3)
            wrng.Select
            Selection.Style = "Plain Text"
            srchResult.Start = wrng.End + Len(styleData(x, 3))
            srchResult.End = rngToSearch.End
50            y = y + 1
            If y > 100 Then Exit Do
        Loop Until Not srchResult.Find.Found
    Next x
    
    Application.StatusBar = "Proceessing document: processing character styles in footnotes!"
    For n = 1 To ActiveDocument.Footnotes.Count
        For x = 1 To styleCount
          Application.StatusBar = "Proceessing document: processing footnote character styles (" & styleData(x, 1) & ")!"
          Set rngToSearch = ActiveDocument.Footnotes(n).Range
          Set srchResult = rngToSearch.Duplicate
          y = 0
        Do
            With srchResult.Find
                .ClearFormatting
                .Format = True
                .Style = styleData(x, 1)
                .Text = ""
                .Wrap = wdFindStop
                .Forward = True
                .Execute
            End With
            
            If srchResult.Find.Found = False Or InStr(srchResult.Text, "</hi></hi>") > 0 Then Exit Do
            Set wrng = fixRange(srchResult.Duplicate)
           
            wrng.Select
            Selection.Style = "Plain Text"
            wrng.StartOf
            wrng.Text = styleData(x, 2)
            wrng.Select
            Selection.Style = "Plain Text"
            Set wrng = srchResult.Duplicate
            wrng.EndOf
            wrng.Text = styleData(x, 3)
            wrng.Select
            Selection.Style = "Plain Text"
            srchResult.Start = wrng.End + Len(styleData(x, 2)) + Len(styleData(x, 3)) + 2
            srchResult.End = rngToSearch.End
100         y = y + 1
            If y > 10 Then Exit Do
       Loop Until Not srchResult.Find.Found
       Next x
    Next n
    
End Sub
Function isCharStyle(ByVal rng As Range) As Boolean
    styleName = rng.Style
    isCharStyle = False
    If styleName = "Emphasis Weak,ew" Then
        isCharStyle = True
    ElseIf styleName = "Text Title,tt" Then
        isCharStyle = True
    ElseIf styleName = "Lang Tibetan,tib" Then
        isCharStyle = True
    ElseIf styleName = "Lang Sanskrit,san" Then
        isCharStyle = True
    ElseIf styleName = "Lang Chinese,chi" Then
        isCharStyle = True
    ElseIf styleName = "Lang Japanese,jap" Then
        isCharStyle = True
    ElseIf styleName = "Name Personal Human,nph" Then
        isCharStyle = True
    ElseIf styleName = "Name Personal other,npo" Then
        isCharStyle = True
    ElseIf styleName = "Name Place,np" Then
        isCharStyle = True
    ElseIf styleName = "Name organization,nor" Then
        isCharStyle = True
    End If
End Function

Sub unItalicizeCommas()
Attribute unItalicizeCommas.VB_Description = "Macro recorded 8/19/2003 by Than G"
Attribute unItalicizeCommas.VB_ProcData.VB_Invoke_Func = "Project.NewMacros.unItalicizeCommas"
'
' unItalicizeCommas Macro
' Macro recorded 8/19/2003 by Than G
'
    Dim rngToSearch, srchResult As Range
    
    Set rngToSearch = ActiveDocument.Range.Duplicate
    Set srchResult = rngToSearch.Duplicate
    Do
        With srchResult.Find
            .ClearFormatting
            .Format = True
            .Text = ","
            .Font.Italic = True
            .Wrap = wdFindStop
            .Forward = True
            .MatchWildcards = True
            .Execute
        End With
        
        If Not srchResult.Find.Found Then Exit Do
        srchResult.Font.Italic = False
        srchResult.Start = srchResult.End + 1
        srchResult.End = rngToSearch.End
    Loop Until Not srchResult.Find.Found
    
    For n = 1 To ActiveDocument.Footnotes.Count
        Set rngToSearch = ActiveDocument.Footnotes(n).Range
        Set srchResult = rngToSearch.Duplicate
    Do
        With srchResult.Find
            .ClearFormatting
            .Format = True
            .Text = ","
            .Font.Italic = True
            .Wrap = wdFindStop
            .Forward = True
            .MatchWildcards = True
            .Execute
        End With
        
        If Not srchResult.Find.Found Then Exit Do
        srchResult.Font.Italic = False
        srchResult.Start = srchResult.End + 1
        srchResult.End = rngToSearch.End
    Loop Until Not srchResult.Find.Found
        
    Next n

End Sub

Sub replaceEntities()
    Selection.Find.ClearFormatting
    Selection.Find.Replacement.ClearFormatting
    With Selection.Find
        .Text = "&"
        .Replacement.Text = "&amp;"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = False
        .MatchCase = False
        .MatchWholeWord = False
        .MatchWildcards = False
        .MatchSoundsLike = False
        .MatchAllWordForms = False
    End With
    Selection.Find.Execute Replace:=wdReplaceAll
    Selection.Find.ClearFormatting
    Selection.Find.Replacement.ClearFormatting
    With Selection.Find
        .Text = "<"
        .Replacement.Text = "&lt;"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = False
        .MatchCase = False
        .MatchWholeWord = False
        .MatchWildcards = False
        .MatchSoundsLike = False
        .MatchAllWordForms = False
    End With
    Selection.Find.Execute Replace:=wdReplaceAll
    Selection.Find.ClearFormatting
    Selection.Find.Replacement.ClearFormatting
    With Selection.Find
        .Text = ">"
        .Replacement.Text = "&gt;"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = False
        .MatchCase = False
        .MatchWholeWord = False
        .MatchWildcards = False
        .MatchSoundsLike = False
        .MatchAllWordForms = False
    End With
    Selection.Find.Execute Replace:=wdReplaceAll
    
    
    

End Sub

