VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ItalicOptions 
   Caption         =   "Options for Italics"
   ClientHeight    =   1650
   ClientLeft      =   30
   ClientTop       =   480
   ClientWidth     =   7125
   OleObjectBlob   =   "ItalicOptions080204.frx":0000
   StartUpPosition =   1  'CenterOwner
End
Attribute VB_Name = "ItalicOptions"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False

Dim lastChoice As Integer
Dim cancelled As Boolean


Public Sub Cancel_Click()
    cancelled = True
    ItalicOptions.Hide
End Sub

Private Sub Submit_Click()
    lastChoice = ItalicOptions.StyleChoices.ListIndex
    ItalicOptions.Hide
End Sub

Private Sub UserForm_Activate()
    ItalicOptions.StyleChoices.SetFocus
    ItalicOptions.StyleChoices.ListIndex = lastChoice
    cancelled = False
End Sub

Private Sub UserForm_Initialize()
    StyleChoices.AddItem "Weak Emphasis"            'ListIndex = 0
    StyleChoices.AddItem "Title"                    'ListIndex = 1
    StyleChoices.AddItem "Chinese Word"             'ListIndex = 2
    StyleChoices.AddItem "French Word"              'ListIndex = 3
    StyleChoices.AddItem "German Word"              'ListIndex = 4
    StyleChoices.AddItem "Japanese Word"            'ListIndex = 5
    StyleChoices.AddItem "Korean Word"              'ListIndex = 6
    StyleChoices.AddItem "Nepali Word"              'ListIndex = 7
    StyleChoices.AddItem "Pali Word"                'ListIndex = 8
    StyleChoices.AddItem "Sanskrit Word"            'ListIndex = 9
    StyleChoices.AddItem "Spanish Word"             'ListIndex = 10
    StyleChoices.AddItem "Tibetan Word"             'ListIndex = 11
    StyleChoices.AddItem "Page Number"              'ListIndex = 12
    StyleChoices.AddItem "Page Reference"           'ListIndex = 13
    StyleChoices.AddItem "Personal Name Human"      'ListIndex = 14
    StyleChoices.AddItem "Personal Name Other"      'ListIndex = 15
    StyleChoices.AddItem "Place Name"               'ListIndex = 16
    StyleChoices.AddItem "Organization Name"        'ListIndex = 17
    StyleChoices.AddItem "Reference"                'ListIndex = 18
    StyleChoices.AddItem "Speaker Generic"          'ListIndex = 19
    StyleChoices.AddItem "Speaker Buddhist Deity"   'ListIndex = 20
    StyleChoices.AddItem "Speaker Human"            'ListIndex = 21
    StyleChoices.AddItem "Speaker Other"            'ListIndex = 22
    StyleChoices.AddItem "Strong Emphasis"          'ListIndex = 23
    StyleChoices.AddItem "Remove Italics"           'ListIndex = 24
End Sub
Public Function wasCancelled() As Boolean
    wasCancelled = cancelled
End Function
Public Function getSelectedStyle() As Style
    
    Select Case lastChoice
        Case 0  ' Weak Emphasis
            Set getSelectedStyle = ActiveDocument.Styles("Emphasis Weak,ew")
            Exit Function
            
        Case 1  ' Title English
            Set getSelectedStyle = ActiveDocument.Styles("Text Title,tt")
            Exit Function
            
        Case 2  ' Chinese Word
            Set getSelectedStyle = ActiveDocument.Styles("Lang Chinese,chi")
            Exit Function
            
        Case 3  ' French Word
            Set getSelectedStyle = ActiveDocument.Styles("Lang French,fre")
            Exit Function
            
        Case 4  ' German Word
            Set getSelectedStyle = ActiveDocument.Styles("Lang German,ger")
            Exit Function
            
        Case 5  ' Japanese Word
            Set getSelectedStyle = ActiveDocument.Styles("Lang Japanese,jap")
            Exit Function
            
        Case 6  ' Korean Word
            Set getSelectedStyle = ActiveDocument.Styles("Lang Korean,kor")
            Exit Function
            
        Case 7  ' Nepali Word
            Set getSelectedStyle = ActiveDocument.Styles("Lang Nepali,nep")
            Exit Function
            
        Case 8  ' Pali Word
            Set getSelectedStyle = ActiveDocument.Styles("Lang Pali,pal")
            Exit Function
            
        Case 9  ' Sanskrit Word
            Set getSelectedStyle = ActiveDocument.Styles("Lang Sanskrit,san")
            Exit Function
            
        Case 10  ' Spanish Word
            Set getSelectedStyle = ActiveDocument.Styles("Lang Spanish,Spa")
            Exit Function
            
        Case 11  ' Tibetan Word
            Set getSelectedStyle = ActiveDocument.Styles("Lang Tibetan,tib")
            Exit Function
            
        Case 12 ' Page Number
            Set getSelectedStyle = ActiveDocument.Styles("Page Number,pgn")
            Exit Function
        
        Case 13 ' Page Refence
            Set getSelectedStyle = ActiveDocument.Styles("Pages,pg")
            Exit Function
            
        Case 14  ' Personal Name Human
            Set getSelectedStyle = ActiveDocument.Styles("Name Personal Human,nph")
            Exit Function
            
        Case 15  ' Personal Name Other
            Set getSelectedStyle = ActiveDocument.Styles("Name Personal other,npo")
            Exit Function
            
        Case 16  ' Place Name
            Set getSelectedStyle = ActiveDocument.Styles("Name Place,np")
            Exit Function
        
        Case 17  ' Organizational Name
            Set getSelectedStyle = ActiveDocument.Styles("Name organization,nor")
            Exit Function
            
        Case 18  ' Reference
            Set getSelectedStyle = ActiveDocument.Styles("Reference,rf")
            Exit Function
                
        Case 19  ' Speaker Generic
            Set getSelectedStyle = ActiveDocument.Styles("Speaker generic,sg")
            Exit Function
            
        Case 20  ' Speaker Buddhist Deity
            Set getSelectedStyle = ActiveDocument.Styles("SpeakerBuddhistDeity,sb")
            Exit Function
            
        Case 21  ' Speaker Human
            Set getSelectedStyle = ActiveDocument.Styles("SpeakerHuman,sh")
            Exit Function
            
        Case 22  ' Speaker Other
            Set getSelectedStyle = ActiveDocument.Styles("SpeakerOther,so")
            Exit Function
            
        Case 23  ' Strong Emphasis
            Set getSelectedStyle = ActiveDocument.Styles("Emphasis Strong,es")
            Exit Function
            
        Case 24  ' Remove Italics
            Set getSelectedStyle = ActiveDocument.Styles("Normal,no")
            Exit Function
                                
        Case Default
            Set getSelectedStyle = ActiveDocument.Styles("Emphasis Weak,ew")
        
    End Select
        
End Function
