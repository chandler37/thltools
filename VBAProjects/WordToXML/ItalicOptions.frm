VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ItalicOptions 
   Caption         =   "Options for Italics"
   ClientHeight    =   1650
   ClientLeft      =   30
   ClientTop       =   480
   ClientWidth     =   7125
   OleObjectBlob   =   "ItalicOptions033104.frx":0000
   StartUpPosition =   1  'CenterOwner
End
Attribute VB_Name = "ItalicOptions"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim lastChoice As Integer
Dim cancelled As Boolean


Private Sub Cancel_Click()
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
    StyleChoices.AddItem "Tibetan Word"             'ListIndex = 2
    StyleChoices.AddItem "Sanskrit Word"            'ListIndex = 3
    StyleChoices.AddItem "Chinese Word"             'ListIndex = 4
    StyleChoices.AddItem "Japanese Word"            'ListIndex = 5
    StyleChoices.AddItem "Personal Name Human"      'ListIndex = 6
    StyleChoices.AddItem "Personal Name Other"      'ListIndex = 7
    StyleChoices.AddItem "Place Name"               'ListIndex = 8
    StyleChoices.AddItem "Organization Name"        'ListIndex = 9
    StyleChoices.AddItem "Reference"                'ListIndex = 10
    StyleChoices.AddItem "Strong Emphasis"          'ListIndex = 11
    StyleChoices.AddItem "Remove Italics"           'ListIndex = 12
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
            
        Case 2  ' Tibetan Word
            Set getSelectedStyle = ActiveDocument.Styles("Lang Tibetan,tib")
            Exit Function
            
        Case 3  ' Sanskrit Word
            Set getSelectedStyle = ActiveDocument.Styles("Lang Sanskrit,san")
            Exit Function
            
        Case 4  ' Chinese Word
            Set getSelectedStyle = ActiveDocument.Styles("Lang Chinese,chi")
            Exit Function
            
        Case 5  ' Japanese Word
            Set getSelectedStyle = ActiveDocument.Styles("Lang Japanese,jap")
            Exit Function
            
        Case 6  ' Personal Name Human
            Set getSelectedStyle = ActiveDocument.Styles("Name Personal Human,nph")
            Exit Function
            
        Case 7  ' Personal Name Other
            Set getSelectedStyle = ActiveDocument.Styles("Name Personal other,npo")
            Exit Function
            
        Case 8  ' Place Name
            Set getSelectedStyle = ActiveDocument.Styles("Name Place,np")
            Exit Function
        
        Case 9  ' Organizational Name
            Set getSelectedStyle = ActiveDocument.Styles("Name organization,nor")
            Exit Function
            
        Case 10  ' Reference
            Set getSelectedStyle = ActiveDocument.Styles("Reference,rf")
            Exit Function
                
        Case 11  ' Strong Emphasis
            Set getSelectedStyle = ActiveDocument.Styles("Emphasis Strong,es")
            Exit Function
            
        Case 12  ' Remove Italics
            Set getSelectedStyle = ActiveDocument.Styles("Normal,no")
            Exit Function
                                
        Case Default
            Set getSelectedStyle = ActiveDocument.Styles("Emphasis Weak,ew")
        
    End Select
        
End Function
