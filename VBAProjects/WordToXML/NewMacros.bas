Attribute VB_Name = "NewMacros"
Dim para As Paragraph
Dim sty As Style
Dim styName, prevSty, ch, temp, stylesUsed, statusStr, outDoc(3000) As String
Dim c, l, m, n, nbeg, nend, index, docEnd, level, listLevel, totalParas, tableEnd As Long
Dim isFront, bodyOpen, useDiv1, listOpen, lgOpen, citOpen, speechOpen, tableOpen As Boolean

Sub convert()
Attribute convert.VB_Description = "Macro recorded 3/11/2004 by Than Garson"
Attribute convert.VB_ProcData.VB_Invoke_Func = "Project.NewMacros.Macro1"
'
' Convert Macro
' Macro created 3/11/2004 by Than Garson
'
    ' Switch to normal view
    switchtonormal
    
    ' Initialize variables
    For n = 0 To 3000
        outDoc(n) = ""
    Next n
    c = 0
    level = 0
    tableEnd = -1
    isFront = False: listOpen = False: lgOpen = False: citOpen = False:
    speechOpen = False: bodyOpen = False: tableOpen = False
    prevSty = ""
    
    ' Find out how deep headers go and set useDiv1 variable appropriately
    stylesUsed = getStylesUsed()
    If InStr(stylesUsed, "Heading 8") = 0 Then
        useDiv1 = True
    Else:
        useDiv1 = False
    End If
    
    ' Do the metadata table
    temp = doMetadata
    index = InStr(temp, "<body>") - 1
    temp = Left(temp, index)
    outDoc(c) = temp & vbCrLf
    
    ' Check for unstylized italic usage
    If Not (italicsDone) Then
        convertItalics
        ItalicOptions.Hide
    End If

    ' Find Doc end and total paragraphs
    docEnd = ActiveDocument.Range.End
    totalParas = ActiveDocument.Paragraphs.Count
    
    ' Iterate through paragraphs
    For Each para In ActiveDocument.Paragraphs
        c = c + 1
        statusStr = Str(Int(c / totalParas * 100)) & "% of document processed!"
        Application.StatusBar = statusStr
        Set sty = para.Style
        styName = sty.NameLocal
        
        ' is it a header?
        If InStr(styName, "Heading") > 0 Then
            doHeaders
            
        ElseIf tableOpen Then
            
            If para.Range.End = tableEnd Then
                tableOpen = False
                tableEnd = -1
            End If
            
        ElseIf isTable(para.Range) Then
            
            tableEnd = doTable
            tableOpen = True
            
        ' otherwise do as regular paragrph (doParaStyles)
        Else:
            doParaStyles
        End If
        
        ' Set previous style variable
        prevSty = styName
    Next para
    
    Selection.HomeKey unit:=wdStory
    Selection.Paste
    ' Close any open elements
    styName = "Normal"
    c = c + 1
    outDoc(c) = closeStyle & vbCrLf
    
    ' Iterate back through levels to close the div elements
    For m = level To 0 Step -1
        If useDiv1 = True Then
            outDoc(c) = outDoc(c) & "</div" & Format(m + 1) & ">" & vbCrLf
        Else:
            outDoc(c) = outDoc(c) + "</div><!--Close of level: " & Format(m + 1) & "//-->" & vbCrLf
        End If
    Next m
    
    ' Close out the XML document and print it in a new word doc
    If back Then
        outDoc(c) = outDoc(c) + "</back>"
    Else:
        outDoc(c) = outDoc(c) + "</body>"
    End If
    outDoc(c) = outDoc(c) + "</text></TEI.2>"
    
    Documents.Add DocumentType:=wdNewBlankDocument
    For n = 0 To c
        Selection.TypeText Text:=outDoc(n)
    Next n

    
End Sub
Sub doHeaders()

    ' Front Sections
    outDoc(c) = outDoc(c) & closeStyle
    If InStr(sty, "Heading1_Front") > 0 Then
        outDoc(c) = outDoc(c) & "<front>" & vbCrLf & "<head>" & iterateRange(para.Range) & "</head>" & vbCrLf
        isFront = True
        level = 0
        
    ' Body Sections
    ElseIf InStr(sty, "Heading1_Body") > 0 Then
        ' Close out any open front sections and the front
        If isFront = True Then
            For m = level To 0 Step -1
                If useDiv1 = True Then
                    outDoc(c) = outDoc(c) & "</div" & Format(m + 1) & ">" & vbCrLf
                Else:
                    outDoc(c) = outDoc(c) & "</div>" & vbCrLf
                End If
            Next m
            outDoc(c) = outDoc(c) & "</front>" & vbCrLf
            front = False
            level = 0
        End If
        ' Add body element
        outDoc(c) = outDoc(c) & "<body><div1>" & vbCrLf & "<head>" & iterateRange(para.Range) & "</head>" & vbCrLf
        bodyOpen = True
        
    ' Back sections
    ElseIf InStr(sty, "Heading1_Back") > 0 Then
        'close out open body sections and body
        For m = level To 0 Step -1
            If useDiv1 = True Then
                outDoc(c) = outDoc(c) & "</div" & Format(m + 1) & ">" & vbCrLf
            Else:
                outDoc(c) = outDoc(c) & "</div><!--Close of level: " & Str(m + 1) & "//-->" & vbCrLf
            End If
        Next m
        'add bac sections
        outDoc(c) = outDoc(c) & "</body>" & vbCrLf & "<back>" & vbCrLf
        
    ' Do Divs within the body using the useDiv1 boolean to determine which kind of div to enter
    ElseIf InStr(sty, "Heading") > 0 Then
        If Not (isFront) And Not (bodyOpen) Then
            outDoc(c) = outDoc(c) & "<body>" & vbCrLf
            bodyOpen = True
        End If
        index = InStr(styName, ",")
        If index > 9 Then
            temp = Mid(styName, 9, (index - 9))
            l = Int(temp)
            If (l - 1) < level Then
                For m = level To (l - 1) Step -1
                    If useDiv1 = True Then
                        outDoc(c) = outDoc(c) & "</div" & Format(m + 1) & ">" & vbCrLf
                    Else:
                        outDoc(c) = outDoc(c) & "</div><!--Close of level: " & Format(m + 1) & "//-->" & vbCrLf
                    End If
                Next m
                level = l - 1
                If useDiv1 = True Then
                    outDoc(c) = outDoc(c) & "<div" & Format(l) & ">"
                Else:
                    outDoc(c) = outDoc(c) & "<div n=""" & Format(l) & """>"
                End If
                outDoc(c) = outDoc(c) & vbCrLf & "<head>" & iterateRange(para.Range) & "</head>" & vbCrLf
            ElseIf (l - 1) = level Then
                If useDiv1 = True Then
                    If level > 0 Then outDoc(c) = outDoc(c) & "</div" & Format(l) & ">"
                    outDoc(c) = outDoc(c) & vbCrLf & "<div" & Format(l) & ">"
                Else:
                    If level > 0 Then outDoc(c) = outDoc(c) & "</div><!--Close of level: " & Format(l) & "//-->"
                    outDoc(c) = outDoc(c) & vbCrLf & "<div n=""" & Format(l) & """>"
                End If
                outDoc(c) = outDoc(c) & vbCrLf & "<head>" & iterateRange(para.Range) & "</head>" & vbCrLf
            Else:
                level = level + 1
                If useDiv1 = True Then
                    outDoc(c) = outDoc(c) & "<div" & Format((level + 1)) & ">"
                Else:
                    outDoc(c) = outDoc(c) & "<div n=""" & Format((level + 1)) & """>"
                End If
                outDoc(c) = outDoc(c) & vbCrLf & "<head>" & iterateRange(para.Range) & "</head>" & vbCrLf
            End If
        End If
    End If
End Sub

Sub doParaStyles()
    outDoc(c) = outDoc(c) & closeStyle()
    ' Normal style becomes a <p>
    If InStr(styName, "Normal") > 0 Or styName = "Paragraph,pr" Then
        outDoc(c) = outDoc(c) & "<p>" & vbCrLf
        outDoc(c) = outDoc(c) & iterateRange(para.Range) & vbCrLf
        outDoc(c) = outDoc(c) & "</p>" & vbCrLf
    
    ' If the style is a citation style and a <q> element
    ElseIf InStr(styName, "Citation") > 0 Then
        If Not (citOpen) Then
            outDoc(c) = outDoc(c) & "<q>" & vbCrLf
            citOpen = True
        End If
        If styName = "Citation List Bullet,clb" Then
            If Not (prevSty = "Citation List Bullet,clb") Then
                outDoc(c) = outDoc(c) & "<list rend=""bullet"">"
                listOpen = True
            Else:
                outDoc(c) = outDoc(c) & "</item>" & vbCrLf
            End If
            outDoc(c) = outDoc(c) & "<item>" & iterateRange(para.Range)
        
        ElseIf styName = "Citation List Number,cln" Then
            If Not (prevSty = "Citation List Number,cln") Then
                outDoc(c) = outDoc(c) & "<list rend=""1"">"
                listOpen = True
            Else:
                outDoc(c) = outDoc(c) & "</item>" & vbCrLf
            End If
            outDoc(c) = outDoc(c) & "<item>" & iterateRange(para.Range)
            
        ElseIf styName = "Citation Prose Nested,cpn" Then
            outDoc(c) = outDoc(c) & "<q>" & iterateRange(para.Range) & "</q>" & vbCrLf
            
        ElseIf styName = "Citation Prose,cp" Then
            outDoc(c) = outDoc(c) & "<p>" & iterateRange(para.Range) & "</p>" & vbCrLf
            
        ElseIf styName = "Citation Verse 1,cv1" Then
            If lgOpen Then outDoc(c) = outDoc(c) & "</lg>"
            outDoc(c) = outDoc(c) & "<lg><l>" & iterateRange(para.Range) & "</l>" & vbCrLf
            lgOpen = True
            
        ElseIf styName = "Citation Verse 2,cv2" Then
            outDoc(c) = outDoc(c) & "<l>" & iterateRange(para.Range) & "</l>" & vbCrLf
        
        End If
        
    ' Do lists
    ElseIf InStr(styName, "List") > 0 Then
        If Not (listOpen) Then
            doNewList
            listOpen = True
            listLevel = 1
            outDoc(c) = outDoc(c) & iterateRange(para.Range) & vbCrLf
            
        ElseIf Not (prevSty = styName) Then
            doNestedLists

        Else:
            outDoc(c) = outDoc(c) & "</item><item>" & iterateRange(para.Range) & vbCrLf
        End If
        
    ElseIf InStr(styName, "Speech") > 0 Then
        If Not (speechOpen) Then
            If InStr(styName, "Inline") > 0 Then
                outDoc(c) = outDoc(c) & "<quote rend=""inline"">" & vbCrLf & iterateRange(para.Range) & "</quote>" & vbCrLf
            ElseIf InStr(styName, "Verse") > 0 Then
                outDoc(c) = outDoc(c) & "<quote>" & vbCrLf & "<lg>" & vbCrLf & "<l>" & iterateRange(para.Range) & "</l>" & vbCrLf
                lgOpen = True
            Else:
                outDoc(c) = outDoc(c) & "<quote>" & vbCrLf & "<p>" & vbCrLf & iterateRange(para.Range) & "</p>" & vbCrLf
            End If
            speechOpen = True
        Else:
            If InStr(styName, "Verse 1") > 0 Then
                If lgOpen Then outDoc(c) = outDoc(c) & "</lg>"
                outDoc(c) = outDoc(c) & "<lg>" & vbCrLf & "<l>" & iterateRange(para.Range) & "</l>" & vbCrLf
                lgOpen = True
            ElseIf InStr(styName, "Verse 2") > 0 Then
                outDoc(c) = outDoc(c) & "<l>" & iterateRange(para.Range) & "</l>" & vbCrLf
            Else:
                outDoc(c) = outDoc(c) & "<p>" & vbCrLf & iterateRange(para.Range) & "</p>" & vbCrLf
            End If
        End If
    ElseIf InStr(styName, "Verse") > 0 Then
    
        If InStr(styName, "Verse 1") > 0 Then
            If lgOpen Then outDoc(c) = outDoc(c) & "</lg>"
            outDoc(c) = outDoc(c) & "<lg>" & vbCrLf & "<l>" & iterateRange(para.Range) & "</l>" & vbCrLf
            lgOpen = True
        ElseIf InStr(styName, "Verse 2") > 0 Then
            outDoc(c) = outDoc(c) & "<l>" & iterateRange(para.Range) & "</l>" & vbCrLf
        End If
        
    End If
    
End Sub
Function doTable() As Long
    Dim tableNum As Integer
    Dim theTable As Table
    Dim cellText As String
    
    For tableNum = 1 To ActiveDocument.Tables.Count
        If para.Range.Start >= ActiveDocument.Tables(tableNum).Range.Start Then Exit For
    Next tableNum
    Set theTable = ActiveDocument.Tables(tableNum)
    outDoc(c) = outDoc(c) & vbCrLf
    c = c + 1
    outDoc(c) = "<list rend=""table"">" & vbCrLf
    For r = 1 To theTable.Rows.Count
        outDoc(c) = outDoc(c) & "<item>"
        For ct = 1 To theTable.Columns.Count
            cellText = Replace(theTable.Cell(r, ct).Range.Text, Chr(13), "")
            cellText = Replace(cellText, Chr(7), "")
            outDoc(c) = outDoc(c) & "<rs>" & cellText & "</rs>"
        Next ct
        outDoc(c) = outDoc(c) & "</item>" & vbCrLf
        If r Mod 5 = 0 Then c = c + 1
    Next r
    outDoc(c) = outDoc(c) & "</list>" & vbCrLf
            
    doTable = theTable.Range.End
    
End Function
Function closeStyle()
    If lgOpen And InStr(styName, "Verse") = 0 Then
        lgOpen = False
        closeStyle = "</lg>" & vbCrLf
    
    ElseIf listOpen And InStr(styName, "List") = 0 Then
        listOpen = False
        If InStr(prevSty, "5") Then
            closeStyle = "</item></list></item></list></item></list></item></list></item></list>" & vbCrLf
        ElseIf InStr(prevSty, "4") Then
            closeStyle = "</item></list></item></list></item></list></item></list>" & vbCrLf
        ElseIf InStr(prevSty, "3") Then
            closeStyle = "</item></list></item></list></item></list>" & vbCrLf
        ElseIf InStr(prevSty, "2") Then
            closeStyle = "</item></list></item></list>" & vbCrLf
        Else:
            closeStyle = "</item></list>" & vbCrLf
        End If
    End If
    If citOpen And InStr(styName, "Citation") = 0 Then
        citOpen = False
        closeStyle = closeStyle & "</q>" & vbCrLf
    End If
    If speechOpen And InStr(styName, "Speech") = 0 Then
        speechOpen = False
        closeStyle = closeStyle & "</quote>" & vbCrLf
    End If
        
End Function
Sub doNestedLists()
    Dim prevListNum, listNum As Integer
    
    prevListNum = Val(Right(prevSty, 1))
    listNum = Val(Right(styName, 1))
    If listNum = 0 Or Not IsNumeric(listNum) Then listNum = 1
    
    If prevListNum > listNum Then
        For ln = prevListNum To (listNum + 1) Step -1
            outDoc(c) = outDoc(c) & "</item></list>"
        Next ln
        outDoc(c) = outDoc(c) & "</item>" & vbCrLf & "<item>" & iterateRange(para.Range) & vbCrLf
    Else:
        If InStr(styName, "Bullet") > 0 Then
            outDoc(c) = outDoc(c) & "<list rend=""bullet""><item>" & iterateRange(para.Range) & vbCrLf
        Else:
            outDoc(c) = outDoc(c) & "<list rend=""1""><item>" & iterateRange(para.Range) & vbCrLf
        End If
    End If
End Sub
Sub doNewList()
    If styName = "List Bullet Tibetan,lbt" Then
        outDoc(c) = outDoc(c) & "<list rend=""bullet"" lang=""tib""><item>"
    ElseIf styName = "List Bullet,lb" Then
        outDoc(c) = outDoc(c) & "<list rend =""bullet""><item>"
    ElseIf styName = "List Numbered,ln" Then
        outDoc(c) = outDoc(c) & "<list rend =""1""><item>"
    Else:
        outDoc(c) = outDoc(c) & "<list rend =""no bull""><item>"
    End If
End Sub
Function iterateRange(ByVal rng)
    Dim tempRng, char1 As Range
    Dim temp, closeTag, currStyle, outStr As String
    Dim isItalics, isBold, isUnderline As Boolean
    
    isItalics = False: isBold = False: isUnderline = False
    currStyle = styName
    For n = rng.Start To rng.End - 1
        If n = docEnd Then Exit For
        Set char1 = ActiveDocument.Range(Start:=n, End:=(n + 1))
                
        If char1.Style = styName Then
            If Not (currStyle = styName) Then
                If currStyle = "Page Number,pgn" Then
                    outStr = outStr & """/>"
                Else:
                    outStr = outStr & closeTag
                End If
                currStyle = styName
                closeTag = ""
            End If
            outStr = outStr & char1.Text
            
        ElseIf char1.Style = "Footnote Reference,fr" Then
            outStr = outStr & "<note>" & iterateNote(char1.Footnotes(1).Range) & "</note>"
            
        ElseIf char1.Style = "Hyperlink,hl" Then
            textToDis = char1.Hyperlinks(1).TextToDisplay
            outStr = Left(outStr, Len(outStr) - (14 + Len(textToDis)))
            outStr = outStr & "<a href=""" & char1.Hyperlinks(1).Address & """>" _
                & textToDis & "</a>"
            n = n + Len(textToDis)
            
        Else:
            If char1.Style = currStyle Then
                outStr = outStr & char1.Text
            ElseIf char1.Style = "Page Number,pgn" Then
                outStr = outStr & "<milestone unit=""page"" n=""" & char1.Text
                currStyle = char1.Style
            Else:
                outStr = outStr & closeTag
                temp = getElement(char1.Style)
                closeTag = Mid(temp, InStr(temp, ">") + 1)
                outStr = outStr & Left(temp, InStr(temp, ">")) & char1.Text
                currStyle = char1.Style
            End If
        End If
        If n Mod 100 = 0 Then
            statusStr = statusStr & " !"
            Application.StatusBar = statusStr
        End If
    Next n
    iterateRange = outStr & closeTag
End Function
Function iterateNote(ByVal rng As Range)
    Dim tempRng, char1 As Range
    Dim temp, closeTag, currStyle, outStr As String
    Dim ct As Integer
    Dim pnOpen As Boolean
    
    currStyle = styName
    pnOpen = False
    
    For ct = 1 To rng.Characters.Count

        Set char1 = rng.Characters(ct)
        If char1.Style = styName Then
            If Not (currStyle = styName) Then
                outStr = outStr & closeTag
                currStyle = styName
                closeTag = ""
            End If
            outStr = outStr & char1.Text
            
        ElseIf char1.Style = "Hyperlink,hl" Then
            textToDis = char1.Hyperlinks(1).TextToDisplay
            outStr = Left(outStr, Len(outStr) - 1)
            outStr = outStr & "<a href=""" & char1.Hyperlinks(1).Address & """>" _
                & textToDis & "</a>"
            ct = ct + Len(textToDis) - 2
            
        Else:
            If char1.Style = currStyle Then
                outStr = outStr & char1.Text
            ElseIf char1.Style = "Page Number,pgn" Then
                outStr = outStr & "<milestone unit=""page"" n=""" & char1.Text
                currStyle = char1.Style
                closeTag = """/>"
            Else:
                outStr = outStr & closeTag
                temp = getElement(char1.Style)
                closeTag = Mid(temp, InStr(temp, ">") + 1)
                outStr = outStr & Left(temp, InStr(temp, ">")) & char1.Text
                currStyle = char1.Style
            End If
        End If
        If ct Mod 100 = 0 Then
            statusStr = statusStr & " !"
            Application.StatusBar = statusStr
        End If
    Next ct
    
    iterateNote = outStr & closeTag
End Function
Function getElement(ByVal chStyle) As String
    
    If chStyle = "Annotations,an" Then
        getElement = "<add n=""annotation""></add>"
        
    ElseIf chStyle = "Dates,dt" Then
        getElement = "<date></date>"
        
    ElseIf chStyle = "Date Range,dr" Then
        getElement = "<dateRange></dateRange>"
        
    ElseIf chStyle = "Doxographical-Bibliographical Category,dbc" Then
        getElement = "<term type=""doxbibl""></term>"
        
    ElseIf chStyle = "Emphasis Strong,es" Then
        getElement = "<hi rend=""strong""></hi>"
    
    ElseIf chStyle = "Emphasis Weak,ew" Then
        getElement = "<hi rend=""weak""></hi>"
    
    ElseIf chStyle = "Lang Chinese,chi" Then
        getElement = "<foreign lang=""chi""></foreign>"
    
    ElseIf chStyle = "Lang English,en" Then
        getElement = "<foreign lang=""eng""></foreign>"
    
    ElseIf chStyle = "Lang Japanese,jap" Then
        getElement = "<foreign lang=""jap""></foreign>"
    
    ElseIf chStyle = "Lang Korean,kor" Then
        getElement = "<foreign lang=""kor""></foreign>"
    
    ElseIf chStyle = "Lang Nepali,nep" Then
        getElement = "<foreign lang=""nep""></foreign>"
    
    ElseIf chStyle = "Lang Pali,pal" Then
        getElement = "<foreign lang=""pal""></foreign>"
    
    ElseIf chStyle = "Lang Sanskrit,san" Then
        getElement = "<foreign lang=""san""></foreign>"
    
    ElseIf chStyle = "Lang Tibetan,tib" Then
        getElement = "<foreign lang=""tib""></foreign>"
    
    ElseIf chStyle = "Monuments,mm" Then
        getElement = "<placeName n=""monument""></placeName>"
    
    ElseIf chStyle = "Name Buddhist  Deity,npb" Or chStyle = "Name Buddhist Deity,npb" Then
        getElement = "<persName type=""bud_deity""></persName>"
    
    ElseIf chStyle = "Name generic,ng" Then
        getElement = "<name></name>"
    
    ElseIf chStyle = "Name of ethnicity,noe" Then
        getElement = "<orgName type=""ethnic""></orgName>"
    
    ElseIf chStyle = "Name org clan,noc" Then
        getElement = "<orgName type=""clan""></orgName>"
    
    ElseIf chStyle = "Name org lineage,nol" Then
        getElement = "<orgName type=""lineage""></orgName>"
        
    ElseIf chStyle = "Name organization monastery,norm" Then
        getElement = "<orgName type=""monastery""></orgName>"
        
    ElseIf chStyle = "Name organization,nor" Then
        getElement = "<orgName></orgName>"
        
    ElseIf chStyle = "Name Personal Human,nph" Then
        getElement = "<persName></persName>"
        
    ElseIf chStyle = "Name Personal other,npo" Then
        getElement = "<persName type=""other""></persName>"
    
    ElseIf chStyle = "Name Place,np" Then
        getElement = "<placeName></placeName>"
    
    ElseIf chStyle = "Pages,pg" Then
        getElement = "<num type=""pagination""></num>"
        
    ElseIf chStyle = "Page Number,pgn" Then
        getElement = "<milestone unit=""page"" n=""REPLACE""/>"
    
    ElseIf chStyle = "Root text,rt" Then
        getElement = "<seg type=""roottext""></seg>"
    
    ElseIf chStyle = "Speaker generic,sg" Then
        getElement = "<persName type=""speaker""></persName>"
    
    ElseIf chStyle = "SpeakerBuddhistDeity,sb" Then
        getElement = "<persName type=""speaker_bud_deity""></persName>"
    
    ElseIf chStyle = "SpeakerHuman,sh" Then
        getElement = "<persName type=""human""></persName>"
    
    ElseIf chStyle = "SpeakerOther,so" Then
        getElement = "<persName type=""other""></persName>"
    
    ElseIf chStyle = "Text Title Sanksrit,tts" Then
        getElement = "<title lang=""san"" level=""m""></title>"
    
    ElseIf chStyle = "Text Title Tibetan,ttt" Then
        getElement = "<title lang=""tib"" level=""m""></title>"
    
    ElseIf chStyle = "Text Title,tt" Then
        getElement = "<title level=""m""></title>"
    
    ElseIf chStyle = "TextGroup,tg" Then
        getElement = "<title level=""s"" type=""group""></title>"
    
    ElseIf chStyle = "Topical Outline,to" Then
        getElement = "<seg type=""outline""></seg>"
    
    End If
    
End Function
Function doMetadata() As String

'
' tableaccess Macro
' Macro recorded 8/13/2003 by Than G
'
    Dim metaTable As Table
    Dim inData, header, today As String
    
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
    header = Replace(header, "{Input}", doTrim(metaTable.Cell(8, 2)))
    header = Replace(header, "{Input Date}", doTrim(metaTable.Cell(8, 4)))
    header = Replace(header, "{Journal Title}", doTrim(metaTable.Cell(9, 2)))
    header = Replace(header, "{Editor Coll}", doTrim(metaTable.Cell(10, 2)))
    header = Replace(header, "{Vol}", doTrim(metaTable.Cell(9, 4)))
    header = Replace(header, "{Pages}", doTrim(metaTable.Cell(9, 6)))
    header = Replace(header, "{Pub Place}", doTrim(metaTable.Cell(12, 2)))
    header = Replace(header, "{Pub Per}", doTrim(metaTable.Cell(11, 2)))
    header = Replace(header, "{Pub Date}", doTrim(metaTable.Cell(11, 4)))
    header = Replace(header, "{Description}", doTrim(metaTable.Cell(13, 2)))
    header = Replace(header, "{Domain URL}", doTrim(metaTable.Cell(14, 2)))
    header = Replace(header, "{Domain Text}", doTrim(metaTable.Cell(14, 4)))
    header = Replace(header, "{Portal URL}", doTrim(metaTable.Cell(15, 2)))
    header = Replace(header, "{Portal Text}", doTrim(metaTable.Cell(15, 4)))
    header = Replace(header, "{Project URL}", doTrim(metaTable.Cell(16, 2)))
    header = Replace(header, "{Project Text}", doTrim(metaTable.Cell(16, 4)))
    header = Replace(header, "{Home URL}", doTrim(metaTable.Cell(17, 2)))
    header = Replace(header, "{Home Text}", doTrim(metaTable.Cell(17, 4)))
    header = Replace(header, "{Self Crumb}", doTrim(metaTable.Cell(18, 2)))
    header = Replace(header, "{Elec Pub Date}", Format(Date, "yyyy-mm-dd"))
    ActiveDocument.Tables(1).Select
    Selection.Cut
    header = Replace(header, "&", "&amp;")
    header = Replace(header, "&amp;amp;", "&amp;")
    doMetadata = header
    
End Function

Function doTrim(ByVal aCell As Cell) As String
    
    Dim rng As Range
    
    Set rng = aCell.Range
    rng.End = rng.End - 1
    doTrim = rng.Text
    
    
End Function


Function getStylesUsed()
    Dim para As Paragraph
    Dim ch As Range
    Dim outStr As String
    
    For Each para In ActiveDocument.Paragraphs
        If InStr(outStr, para.Range.Style) = 0 Then
            outStr = outStr & vbCrLf & para.Range.Style
        End If
    Next para
    
    getStylesUsed = outStr
    
End Function
Function isTable(ByVal paraRang As Range) As Boolean
    For tableCount = 1 To ActiveDocument.Tables.Count
        tableStart = ActiveDocument.Tables(tableCount).Range.Start
        tableStop = ActiveDocument.Tables(tableCount).Range.End
        If paraRang.Start >= tableStart And paraRang.End <= tableStop Then
            isTable = True
            Exit Function
        End If
    Next tableCount
    isTable = False
End Function

Sub switchtonormal()
Attribute switchtonormal.VB_Description = "Macro recorded 3/31/2004 by Than Garson"
Attribute switchtonormal.VB_ProcData.VB_Invoke_Func = "Project.NewMacros.switchtonormal"
'
' switchtonormal Macro
' Macro recorded 3/31/2004 by Than Garson
'
    If ActiveWindow.View.SplitSpecial = wdPaneNone Then
        ActiveWindow.ActivePane.View.Type = wdNormalView
    Else
        ActiveWindow.View.Type = wdNormalView
    End If
End Sub

Sub convertItalics()
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
    
    Unload ItalicOptions
  End Sub
  Sub convertFootnoteItalics()
    Dim loopCt As Integer
    
    ActiveDocument.Endnotes.convert
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

Sub unItalicizeCommas()
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

Function isCharStyle(ByVal rng As Range) As Boolean
    StyleName = rng.Style
    isCharStyle = False

    If StyleName = "Emphasis Weak,ew" Or _
        StyleName = "Annotations,an" Or _
        StyleName = "Hyperlink,hl" Or _
        StyleName = "Lang Tibetan,tib" Or _
        StyleName = "Lang Sanskrit,san" Or _
        StyleName = "Lang Chinese,chi" Or _
        StyleName = "Lang Japanese,jap" Or _
        StyleName = "Name Personal Human,nph" Or _
        StyleName = "Name Personal other,npo" Or _
        StyleName = "Name Place,np" Or _
        StyleName = "Name organization,nor" Or _
        StyleName = "Date Range,dr" Or _
        StyleName = "Page Number,pgn" Or _
        StyleName = "Speaker generic,sg" Or _
        StyleName = "SpeakerBuddhistDeity,sb" Or _
        StyleName = "SpeakerHuman,sh" Or _
        StyleName = "SpeakerOther,so" Or _
        StyleName = "Text Title,tt" Or _
        StyleName = "Text Title Sanksrit,tts" Or _
        StyleName = "Text Title Tibetan,ttt" Or _
        StyleName = "TextGroup,tg" Then
            isCharStyle = True
    End If
    
End Function
