object T_NoteSeqB
  Hint = 'Version = 16'
  TitleLabel = EditLabelNoteSeqB
  Title = 'NoteSeqB'
  Description = 'Note sequencer B'
  ModuleAttributes = []
  ModuleType = 90
  Cycles = 1.437500000000000000
  ProgMem = 1.156250000000000000
  XMem = 0.562500000000000000
  YMem = 1.437500000000000000
  ZeroPage = 3.500000000000000000
  DynMem = 1.281250000000000000
  UnitHeight = 9
  object EditLabelNoteSeqB: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputNoteSeqBLink: TOutput
    Left = 240
    Top = 71
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 2
  end
  object InputNoteSeqBRst: TInput
    Left = 8
    Top = 95
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object TextLabelNoteSeqBRst: TTextLabel
    Left = 5
    Top = 84
    Width = 15
    Height = 11
    Caption = 'Rst'
  end
  object InputNoteSeqBClk: TInput
    Left = 8
    Top = 71
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object TextLabelNoteSeqBClk: TTextLabel
    Left = 6
    Top = 60
    Width = 14
    Height = 11
    Caption = 'Clk'
  end
  object GraphicImageNoteSeqBRst: TGraphicImage
    Left = 4
    Top = 95
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object GraphicImageNoteSeqBClk: TGraphicImage
    Left = 4
    Top = 71
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object OutputNoteSeqBOut: TOutput
    Left = 240
    Top = 119
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object TextLabelNoteSeqBOut: TTextLabel
    Left = 237
    Top = 108
    Width = 15
    Height = 11
    Caption = 'Out'
  end
  object TextLabelNoteSeqBLink: TTextLabel
    Left = 235
    Top = 60
    Width = 17
    Height = 11
    Caption = 'Link'
  end
  object TextLabelNoteSeqBStep: TTextLabel
    Left = 163
    Top = 5
    Width = 19
    Height = 11
    Caption = 'Step'
  end
  object TextLabelNoteSeqBSnc: TTextLabel
    Left = 5
    Top = 108
    Width = 16
    Height = 11
    Caption = 'Snc'
  end
  object OutputNoteSeqBSnc: TOutput
    Left = 8
    Top = 119
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object TextLabelNoteSeqBHeader: TTextLabel
    Left = 30
    Top = 25
    Width = 185
    Height = 11
    Caption = 
      '1    2    3   4    5   6   7    8    9  10   11  12   13 14  15 ' +
      ' 16'
  end
  object BoxNoteSeqBGraph: TBox
    Left = 26
    Top = 36
    Width = 194
    Height = 97
  end
  object BarGraphNoteSeqB: TBarGraph
    Left = 26
    Top = 37
    Width = 192
    Height = 73
    Hint = '!! CUSTOM 0 - ZOOMFACTOR !!'
    DataSize = 16
    DefaultValue = 60
  end
  object OutputNoteSeqBGclk: TOutput
    Left = 240
    Top = 96
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 3
  end
  object TextLabelNoteSeqBGclk: TTextLabel
    Left = 233
    Top = 85
    Width = 19
    Height = 11
    Caption = 'Gclk'
  end
  object ClickerNoteSeqBPresetClr: TClicker
    Left = 2
    Top = 23
    Width = 23
    Height = 14
    AllowAllUp = True
    GroupIndex = 1
    Caption = 'Clr'
    Margin = 3
    CtrlIndex = -1
  end
  object ClickerNoteSeqBPresetRnd: TClicker
    Left = 2
    Top = 39
    Width = 23
    Height = 14
    AllowAllUp = True
    GroupIndex = 3
    Caption = 'Rnd'
    Margin = 0
    CtrlIndex = -1
  end
  object IndicatorNoteSeqB01: TIndicator
    Left = 27
    Top = 72
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object IndicatorNoteSeqB02: TIndicator
    Left = 39
    Top = 72
    Width = 9
    Height = 5
    CtrlIndex = 1
    Active = False
  end
  object IndicatorNoteSeqB03: TIndicator
    Left = 51
    Top = 72
    Width = 9
    Height = 5
    CtrlIndex = 2
    Active = False
  end
  object IndicatorNoteSeqB04: TIndicator
    Left = 63
    Top = 72
    Width = 9
    Height = 5
    CtrlIndex = 3
    Active = False
  end
  object IndicatorNoteSeqB05: TIndicator
    Left = 75
    Top = 72
    Width = 9
    Height = 5
    CtrlIndex = 4
    Active = False
  end
  object IndicatorNoteSeqB06: TIndicator
    Left = 87
    Top = 72
    Width = 9
    Height = 5
    CtrlIndex = 5
    Active = False
  end
  object IndicatorNoteSeqB07: TIndicator
    Left = 99
    Top = 72
    Width = 9
    Height = 5
    CtrlIndex = 6
    Active = False
  end
  object IndicatorNoteSeqB08: TIndicator
    Left = 111
    Top = 72
    Width = 9
    Height = 5
    CtrlIndex = 7
    Active = False
  end
  object IndicatorNoteSeqB09: TIndicator
    Left = 123
    Top = 72
    Width = 9
    Height = 5
    CtrlIndex = 8
    Active = False
  end
  object IndicatorNoteSeqB10: TIndicator
    Left = 135
    Top = 72
    Width = 9
    Height = 5
    CtrlIndex = 9
    Active = False
  end
  object IndicatorNoteSeqB11: TIndicator
    Left = 147
    Top = 72
    Width = 9
    Height = 5
    CtrlIndex = 10
    Active = False
  end
  object IndicatorNoteSeqB12: TIndicator
    Left = 159
    Top = 72
    Width = 9
    Height = 5
    CtrlIndex = 11
    Active = False
  end
  object IndicatorNoteSeqB13: TIndicator
    Left = 171
    Top = 72
    Width = 9
    Height = 5
    CtrlIndex = 12
    Active = False
  end
  object IndicatorNoteSeqB14: TIndicator
    Left = 183
    Top = 72
    Width = 9
    Height = 5
    CtrlIndex = 13
    Active = False
  end
  object IndicatorNoteSeqB15: TIndicator
    Left = 195
    Top = 72
    Width = 9
    Height = 5
    CtrlIndex = 14
    Active = False
  end
  object IndicatorNoteSeqB16: TIndicator
    Left = 207
    Top = 72
    Width = 9
    Height = 5
    CtrlIndex = 15
    Active = False
  end
  object TextLabelNoteSeqBZoom: TTextLabel
    Left = 30
    Top = 14
    Width = 26
    Height = 11
    Caption = 'Zoom'
  end
  object TextLabelNoteSeqBPan: TTextLabel
    Left = 82
    Top = 14
    Width = 17
    Height = 11
    Caption = 'Pan'
  end
  object SpinnerNoteSeqBStep: TSpinner
    Left = 212
    Top = 1
    Width = 12
    Height = 20
    Value = 15
    Display = DisplayNoteSeqBStep
    DisplayFormat = dfUnsigned
    CtrlIndex = 16
    DefaultValue = 15
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayNoteSeqBStep: TDisplay
    Left = 185
    Top = 4
    Width = 26
    Alignment = taCenter
    Caption = '16'
    TabOrder = 1
    DisplayFormats.Formats = [dfOffset1]
    DisplayFormat = dfOffset1
    Clickable = False
    CtrlIndex = -1
  end
  object SpinnerNoteSeqB01: TSpinner
    Left = 27
    Top = 111
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqB02: TSpinner
    Left = 39
    Top = 111
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqB03: TSpinner
    Left = 51
    Top = 111
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 2
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqB04: TSpinner
    Left = 63
    Top = 111
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 3
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqB05: TSpinner
    Left = 75
    Top = 111
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 4
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqB06: TSpinner
    Left = 87
    Top = 111
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 5
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqB07: TSpinner
    Left = 99
    Top = 111
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 6
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqB08: TSpinner
    Left = 111
    Top = 111
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 7
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqB09: TSpinner
    Left = 123
    Top = 111
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 8
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqB10: TSpinner
    Left = 135
    Top = 111
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 9
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqB11: TSpinner
    Left = 147
    Top = 111
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 10
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqB12: TSpinner
    Left = 159
    Top = 111
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 11
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqB13: TSpinner
    Left = 171
    Top = 111
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 12
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqB14: TSpinner
    Left = 183
    Top = 111
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 13
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqB15: TSpinner
    Left = 195
    Top = 111
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 14
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqB16: TSpinner
    Left = 207
    Top = 111
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 15
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqBPosition: TSpinner
    Left = 228
    Top = 34
    Width = 24
    Height = 14
    Value = 0
    DisplayFormat = dfOffset1
    CtrlIndex = 17
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Orientation = soHorizontal
    MinValue = 0
    MaxValue = 16
    StepSize = 1
  end
  object SpinnerNoteSeqBPan: TSpinner
    Left = 101
    Top = 13
    Width = 20
    Height = 12
    Value = 6
    DisplayFormat = dfUnsigned
    CtrlIndex = -1
    DefaultValue = 6
    Morphable = False
    Knobbable = True
    Controllable = True
    Orientation = soHorizontal
    MinValue = 6
    MaxValue = 117
    StepSize = 1
  end
  object SpinnerNoteSeqBZoom: TSpinner
    Left = 58
    Top = 13
    Width = 20
    Height = 12
    Value = 6
    DisplayFormat = dfUnsigned
    CtrlIndex = -1
    DefaultValue = 6
    Morphable = False
    Knobbable = True
    Controllable = True
    Orientation = soHorizontal
    MinValue = 6
    MaxValue = 117
    StepSize = 1
  end
  object ButtonSetNoteSeqBLoop: TButtonSet
    Left = 130
    Top = 4
    Width = 30
    Height = 14
    Value = 1
    DisplayFormat = dfOnOff
    CtrlIndex = 20
    DefaultValue = 1
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Loop'
      end>
  end
  object ButtonSetNoteSeqBStop: TButtonSet
    Left = 228
    Top = 18
    Width = 24
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 19
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        FileName = '_pause.bmp'
      end>
  end
  object ButtonSetNoteSeqBRec: TButtonSet
    Left = 228
    Top = 2
    Width = 24
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 18
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        FileName = '_rec.bmp'
      end>
  end
end
object T_NoteSeqA
  Hint = 'Version = 16'
  TitleLabel = EditLabelNoteSeqA
  Title = 'NoteSeqA'
  Description = 'Note sequencer A'
  ModuleAttributes = []
  ModuleType = 15
  Cycles = 1.437500000000000000
  ProgMem = 1.156250000000000000
  XMem = 0.562500000000000000
  YMem = 1.437500000000000000
  ZeroPage = 3.500000000000000000
  DynMem = 1.250000000000000000
  UnitHeight = 7
  object EditLabelNoteSeqA: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputNoteSeqALink: TOutput
    Left = 240
    Top = 56
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 2
  end
  object InputNoteSeqARst: TInput
    Left = 8
    Top = 66
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object TextLabelNoteSeqARst: TTextLabel
    Left = 5
    Top = 55
    Width = 15
    Height = 11
    Caption = 'Rst'
  end
  object InputNoteSeqAClk: TInput
    Left = 8
    Top = 42
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object TextLabelNoteSeqAClk: TTextLabel
    Left = 6
    Top = 31
    Width = 14
    Height = 11
    Caption = 'Clk'
  end
  object GraphicImageNoteSeqARst: TGraphicImage
    Left = 4
    Top = 66
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object GraphicImageNoteSeqAClk: TGraphicImage
    Left = 4
    Top = 42
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object OutputNoteSeqAOut: TOutput
    Left = 240
    Top = 89
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object TextLabelNoteSeqAOut: TTextLabel
    Left = 219
    Top = 88
    Width = 15
    Height = 11
    Caption = 'Out'
  end
  object TextLabelNoteSeqALink: TTextLabel
    Left = 219
    Top = 55
    Width = 17
    Height = 11
    Caption = 'Link'
  end
  object TextLabelNoteSeqAStep: TTextLabel
    Left = 158
    Top = 5
    Width = 19
    Height = 11
    Caption = 'Step'
  end
  object TextLabelVSnc: TTextLabel
    Left = 5
    Top = 78
    Width = 16
    Height = 11
    Caption = 'Snc'
  end
  object OutputNoteSeqASnc: TOutput
    Left = 8
    Top = 89
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object TextLabelNoteSeqAHeading: TTextLabel
    Left = 28
    Top = 21
    Width = 185
    Height = 11
    Caption = 
      '1    2    3   4    5   6   7    8    9  10   11  12   13 14  15 ' +
      ' 16'
  end
  object BoxNoteSeqAGraph: TBox
    Left = 24
    Top = 32
    Width = 194
    Height = 70
  end
  object BarGraphNoteSeqA: TBarGraph
    Left = 24
    Top = 33
    Width = 192
    Height = 48
    DataSize = 16
    DefaultValue = 64
  end
  object OutputNoteSeqAGclk: TOutput
    Left = 240
    Top = 73
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 3
  end
  object TextLabelNoteSeqAGclk: TTextLabel
    Left = 219
    Top = 72
    Width = 19
    Height = 11
    Caption = 'Gclk'
  end
  object ClickerNoteSeqAPresetClr: TClicker
    Left = 87
    Top = 4
    Width = 31
    Height = 14
    AllowAllUp = True
    GroupIndex = 1
    Caption = 'Clr'
    CtrlIndex = -1
  end
  object IndicatorNoteSeqA01: TIndicator
    Left = 25
    Top = 55
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object IndicatorNoteSeqA02: TIndicator
    Left = 37
    Top = 55
    Width = 9
    Height = 5
    CtrlIndex = 1
    Active = False
  end
  object IndicatorNoteSeqA03: TIndicator
    Left = 49
    Top = 55
    Width = 9
    Height = 5
    CtrlIndex = 2
    Active = False
  end
  object IndicatorNoteSeqA04: TIndicator
    Left = 61
    Top = 55
    Width = 9
    Height = 5
    CtrlIndex = 3
    Active = False
  end
  object IndicatorNoteSeqA05: TIndicator
    Left = 73
    Top = 55
    Width = 9
    Height = 5
    CtrlIndex = 4
    Active = False
  end
  object IndicatorNoteSeqA06: TIndicator
    Left = 85
    Top = 55
    Width = 9
    Height = 5
    CtrlIndex = 5
    Active = False
  end
  object IndicatorNoteSeqA07: TIndicator
    Left = 97
    Top = 55
    Width = 9
    Height = 5
    CtrlIndex = 6
    Active = False
  end
  object IndicatorNoteSeqA08: TIndicator
    Left = 109
    Top = 55
    Width = 9
    Height = 5
    CtrlIndex = 7
    Active = False
  end
  object IndicatorNoteSeqA09: TIndicator
    Left = 121
    Top = 55
    Width = 9
    Height = 5
    CtrlIndex = 8
    Active = False
  end
  object IndicatorNoteSeqA10: TIndicator
    Left = 133
    Top = 55
    Width = 9
    Height = 5
    CtrlIndex = 9
    Active = False
  end
  object IndicatorNoteSeqA11: TIndicator
    Left = 145
    Top = 55
    Width = 9
    Height = 5
    CtrlIndex = 10
    Active = False
  end
  object IndicatorNoteSeqA12: TIndicator
    Left = 157
    Top = 55
    Width = 9
    Height = 5
    CtrlIndex = 11
    Active = False
  end
  object IndicatorNoteSeqA13: TIndicator
    Left = 169
    Top = 55
    Width = 9
    Height = 5
    CtrlIndex = 12
    Active = False
  end
  object IndicatorNoteSeqA14: TIndicator
    Left = 181
    Top = 55
    Width = 9
    Height = 5
    CtrlIndex = 13
    Active = False
  end
  object IndicatorNoteSeqA15: TIndicator
    Left = 193
    Top = 55
    Width = 9
    Height = 5
    CtrlIndex = 14
    Active = False
  end
  object IndicatorNoteSeqA16: TIndicator
    Left = 205
    Top = 55
    Width = 9
    Height = 5
    CtrlIndex = 15
    Active = False
  end
  object ClickerNoteSeqAPresetRnd: TClicker
    Left = 2
    Top = 18
    Width = 24
    Height = 14
    AllowAllUp = True
    GroupIndex = 4
    Caption = 'Rnd'
    Margin = 1
    CtrlIndex = -1
  end
  object SpinnerNoteSeqAStep: TSpinner
    Left = 207
    Top = 1
    Width = 12
    Height = 20
    Value = 15
    Display = DisplayNoteSeqAStep
    DisplayFormat = dfUnsigned
    CtrlIndex = 16
    DefaultValue = 15
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayNoteSeqAStep: TDisplay
    Left = 180
    Top = 4
    Width = 26
    Alignment = taCenter
    Caption = '16'
    TabOrder = 1
    DisplayFormats.Formats = [dfOffset1]
    DisplayFormat = dfOffset1
    Clickable = False
    CtrlIndex = -1
  end
  object SpinnerNoteSeqA01: TSpinner
    Left = 25
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqA02: TSpinner
    Left = 37
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqA03: TSpinner
    Left = 49
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 2
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqA04: TSpinner
    Left = 61
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 3
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqA05: TSpinner
    Left = 73
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 4
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqA06: TSpinner
    Left = 85
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 5
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqA07: TSpinner
    Left = 97
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 6
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqA08: TSpinner
    Left = 109
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 7
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqA09: TSpinner
    Left = 121
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 8
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqA10: TSpinner
    Left = 133
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 9
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqA11: TSpinner
    Left = 145
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 10
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqA12: TSpinner
    Left = 157
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 11
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqA13: TSpinner
    Left = 169
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 12
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqA14: TSpinner
    Left = 181
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 13
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqA15: TSpinner
    Left = 193
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 14
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqA16: TSpinner
    Left = 205
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfNote
    CtrlIndex = 15
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerNoteSeqAPosition: TSpinner
    Left = 228
    Top = 34
    Width = 24
    Height = 14
    Value = 0
    DisplayFormat = dfOffset1
    CtrlIndex = 17
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Orientation = soHorizontal
    MinValue = 0
    MaxValue = 16
    StepSize = 1
  end
  object ButtonSetNoteSeqALoop: TButtonSet
    Left = 122
    Top = 4
    Width = 30
    Height = 14
    Value = 1
    DisplayFormat = dfOnOff
    CtrlIndex = 20
    DefaultValue = 1
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Loop'
      end>
  end
  object ButtonSetNoteSeqARec: TButtonSet
    Left = 228
    Top = 2
    Width = 24
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 18
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        FileName = '_rec.bmp'
      end>
  end
  object ButtonSetNoteSeqAStop: TButtonSet
    Left = 228
    Top = 18
    Width = 24
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 19
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        FileName = '_pause.bmp'
      end>
  end
end
object T_CtrlSeq
  Hint = 'Version = 16'
  TitleLabel = EditLabelCtrlSeq
  Title = 'CtrlSeq'
  Description = 'Control sequencer'
  ModuleAttributes = []
  ModuleType = 91
  Cycles = 1.437500000000000000
  ProgMem = 1.156250000000000000
  XMem = 0.562500000000000000
  YMem = 1.500000000000000000
  ZeroPage = 2.625000000000000000
  DynMem = 1.156250000000000000
  UnitHeight = 7
  object BoxCtrlSeqGraph: TBox
    Left = 26
    Top = 32
    Width = 194
    Height = 69
  end
  object BarGraphCtrlSeq: TBarGraph
    Left = 26
    Top = 33
    Width = 192
    Height = 47
    DataSize = 16
    DefaultValue = 64
  end
  object EditLabelCtrlSeq: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputCtrlSeqLink: TOutput
    Left = 240
    Top = 5
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 2
  end
  object InputCtrlSeqRst: TInput
    Left = 8
    Top = 68
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object TextLabelCtrlSeqRst: TTextLabel
    Left = 5
    Top = 57
    Width = 15
    Height = 11
    Caption = 'Rst'
  end
  object InputCtrlSeqClk: TInput
    Left = 8
    Top = 46
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object TextLabelCtrlSeqClk: TTextLabel
    Left = 6
    Top = 35
    Width = 14
    Height = 11
    Caption = 'Clk'
  end
  object GraphicImageCtrlSeqRst: TGraphicImage
    Left = 4
    Top = 68
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object GraphicImageCtrlSeqClk: TGraphicImage
    Left = 4
    Top = 46
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object OutputCtrlSeqOut: TOutput
    Left = 240
    Top = 89
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object TextLabelCtrlSeqOut: TTextLabel
    Left = 223
    Top = 88
    Width = 15
    Height = 11
    Caption = 'Out'
  end
  object TextLabelCtrlSeqLink: TTextLabel
    Left = 221
    Top = 4
    Width = 17
    Height = 11
    Caption = 'Link'
  end
  object TextLabelCtrlSeqStep: TTextLabel
    Left = 158
    Top = 5
    Width = 19
    Height = 11
    Caption = 'Step'
  end
  object TextLabelCtrlSeqSnc: TTextLabel
    Left = 5
    Top = 78
    Width = 16
    Height = 11
    Caption = 'Snc'
  end
  object OutputCtrlSeqSnc: TOutput
    Left = 8
    Top = 89
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object ClickerCtrlSeqPresetClr: TClicker
    Left = 87
    Top = 4
    Width = 31
    Height = 14
    AllowAllUp = True
    GroupIndex = 1
    Caption = 'Clr'
    CtrlIndex = -1
  end
  object IndicatorCtrlSeq01: TIndicator
    Left = 27
    Top = 56
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object IndicatorCtrlSeq02: TIndicator
    Left = 39
    Top = 56
    Width = 9
    Height = 5
    CtrlIndex = 1
    Active = False
  end
  object IndicatorCtrlSeq03: TIndicator
    Left = 51
    Top = 56
    Width = 9
    Height = 5
    CtrlIndex = 2
    Active = False
  end
  object IndicatorCtrlSeq04: TIndicator
    Left = 63
    Top = 56
    Width = 9
    Height = 5
    CtrlIndex = 3
    Active = False
  end
  object IndicatorCtrlSeq05: TIndicator
    Left = 75
    Top = 56
    Width = 9
    Height = 5
    CtrlIndex = 4
    Active = False
  end
  object IndicatorCtrlSeq06: TIndicator
    Left = 87
    Top = 56
    Width = 9
    Height = 5
    CtrlIndex = 5
    Active = False
  end
  object IndicatorCtrlSeq07: TIndicator
    Left = 99
    Top = 56
    Width = 9
    Height = 5
    CtrlIndex = 6
    Active = False
  end
  object IndicatorCtrlSeq08: TIndicator
    Left = 111
    Top = 56
    Width = 9
    Height = 5
    CtrlIndex = 7
    Active = False
  end
  object IndicatorCtrlSeq09: TIndicator
    Left = 123
    Top = 56
    Width = 9
    Height = 5
    CtrlIndex = 8
    Active = False
  end
  object IndicatorCtrlSeq10: TIndicator
    Left = 135
    Top = 56
    Width = 9
    Height = 5
    CtrlIndex = 9
    Active = False
  end
  object IndicatorCtrlSeq11: TIndicator
    Left = 147
    Top = 56
    Width = 9
    Height = 5
    CtrlIndex = 10
    Active = False
  end
  object IndicatorCtrlSeq12: TIndicator
    Left = 159
    Top = 56
    Width = 9
    Height = 5
    CtrlIndex = 11
    Active = False
  end
  object IndicatorCtrlSeq13: TIndicator
    Left = 171
    Top = 56
    Width = 9
    Height = 5
    CtrlIndex = 12
    Active = False
  end
  object IndicatorCtrlSeq14: TIndicator
    Left = 183
    Top = 56
    Width = 9
    Height = 5
    CtrlIndex = 13
    Active = False
  end
  object IndicatorCtrlSeq15: TIndicator
    Left = 195
    Top = 56
    Width = 9
    Height = 5
    CtrlIndex = 14
    Active = False
  end
  object IndicatorCtrlSeq16: TIndicator
    Left = 207
    Top = 56
    Width = 9
    Height = 5
    CtrlIndex = 15
    Active = False
  end
  object TextLabelCtrlSeqHeading: TTextLabel
    Left = 30
    Top = 21
    Width = 185
    Height = 11
    Caption = 
      '1    2    3   4    5   6   7    8    9  10   11  12   13 14  15 ' +
      ' 16'
  end
  object ClickerCtrlSeqPresetRnd: TClicker
    Left = 2
    Top = 18
    Width = 24
    Height = 14
    AllowAllUp = True
    GroupIndex = 4
    Caption = 'Rnd'
    Margin = 1
    CtrlIndex = -1
  end
  object SpinnerCtrlSeqStep: TSpinner
    Left = 207
    Top = 1
    Width = 12
    Height = 20
    Value = 15
    Display = DisplayCtrlSeqStep
    DisplayFormat = dfUnsigned
    CtrlIndex = 16
    DefaultValue = 15
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayCtrlSeqStep: TDisplay
    Left = 180
    Top = 4
    Width = 26
    Alignment = taCenter
    Caption = '16'
    TabOrder = 1
    DisplayFormats.Formats = [dfOffset1]
    DisplayFormat = dfOffset1
    Clickable = False
    CtrlIndex = -1
  end
  object SpinnerCtrlSeq01: TSpinner
    Left = 27
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerCtrlSeq02: TSpinner
    Left = 39
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerCtrlSeq03: TSpinner
    Left = 51
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerCtrlSeq04: TSpinner
    Left = 63
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerCtrlSeq05: TSpinner
    Left = 75
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerCtrlSeq06: TSpinner
    Left = 87
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerCtrlSeq07: TSpinner
    Left = 99
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 6
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerCtrlSeq08: TSpinner
    Left = 111
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 7
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerCtrlSeq09: TSpinner
    Left = 123
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 8
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerCtrlSeq10: TSpinner
    Left = 135
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 9
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerCtrlSeq11: TSpinner
    Left = 147
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 10
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerCtrlSeq12: TSpinner
    Left = 159
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 11
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerCtrlSeq13: TSpinner
    Left = 171
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 12
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerCtrlSeq14: TSpinner
    Left = 183
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 13
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerCtrlSeq15: TSpinner
    Left = 195
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 14
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerCtrlSeq16: TSpinner
    Left = 207
    Top = 81
    Width = 12
    Height = 20
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 15
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetCtrlSeqUni: TButtonSet
    Left = 225
    Top = 51
    Width = 23
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 17
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Uni'
      end>
  end
  object ButtonSetCtrlSeqLoop: TButtonSet
    Left = 122
    Top = 4
    Width = 30
    Height = 14
    Value = 1
    DisplayFormat = dfOnOff
    CtrlIndex = 18
    DefaultValue = 1
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Loop'
      end>
  end
end
object T_EventSeq
  Hint = 'Version = 16'
  TitleLabel = EditLabelEventSeq
  Title = 'EventSeq'
  Description = 'Event sequencer'
  ModuleAttributes = []
  ModuleType = 17
  Cycles = 1.656250000000000000
  ProgMem = 1.343750000000000000
  XMem = 0.625000000000000000
  YMem = 1.437500000000000000
  ZeroPage = 3.500000000000000000
  DynMem = 2.000000000000000000
  UnitHeight = 5
  object GraphicImageEventSeqLineOutB: TGraphicImage
    Left = 222
    Top = 60
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object GraphicImageEventSeqLineOutA: TGraphicImage
    Left = 222
    Top = 45
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelEventSeq: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputEventSeqLink: TOutput
    Left = 240
    Top = 5
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 3
  end
  object InputEventSeqRst: TInput
    Left = 8
    Top = 37
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object TextLabelEventSeqRst: TTextLabel
    Left = 5
    Top = 26
    Width = 15
    Height = 11
    Caption = 'Rst'
  end
  object InputEventSeqClk: TInput
    Left = 8
    Top = 15
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object TextLabelEventSeqClk: TTextLabel
    Left = 19
    Top = 13
    Width = 14
    Height = 11
    Caption = 'Clk'
  end
  object ImageEventSeqRst: TGraphicImage
    Left = 4
    Top = 37
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object ImageEventSeqClk: TGraphicImage
    Left = 4
    Top = 15
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object OutputEventSeqOutB: TOutput
    Left = 240
    Top = 60
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object OutputEventSeqOutA: TOutput
    Left = 240
    Top = 44
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object TextLabelEventSeqOut: TTextLabel
    Left = 237
    Top = 27
    Width = 15
    Height = 11
    Caption = 'Out'
  end
  object TextLabelEventSeqLink: TTextLabel
    Left = 221
    Top = 4
    Width = 17
    Height = 11
    Caption = 'Link'
  end
  object TextLabelEventSeqStep: TTextLabel
    Left = 158
    Top = 5
    Width = 19
    Height = 11
    Caption = 'Step'
  end
  object TextLabelEventSeqSnc: TTextLabel
    Left = 5
    Top = 48
    Width = 16
    Height = 11
    Caption = 'Snc'
  end
  object OutputEventSeqSnc: TOutput
    Left = 8
    Top = 59
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 2
  end
  object ClickerEventSeqPresetClr: TClicker
    Left = 87
    Top = 4
    Width = 31
    Height = 14
    AllowAllUp = True
    GroupIndex = 1
    Caption = 'Clr'
    CtrlIndex = -1
  end
  object IndicatorEventSeq01: TIndicator
    Left = 26
    Top = 35
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object IndicatorEventSeq02: TIndicator
    Left = 38
    Top = 35
    Width = 9
    Height = 5
    CtrlIndex = 1
    Active = False
  end
  object IndicatorEventSeq03: TIndicator
    Left = 50
    Top = 35
    Width = 9
    Height = 5
    CtrlIndex = 2
    Active = False
  end
  object IndicatorEventSeq04: TIndicator
    Left = 62
    Top = 35
    Width = 9
    Height = 5
    CtrlIndex = 3
    Active = False
  end
  object IndicatorEventSeq05: TIndicator
    Left = 74
    Top = 35
    Width = 9
    Height = 5
    CtrlIndex = 4
    Active = False
  end
  object IndicatorEventSeq06: TIndicator
    Left = 86
    Top = 35
    Width = 9
    Height = 5
    CtrlIndex = 5
    Active = False
  end
  object IndicatorEventSeq07: TIndicator
    Left = 98
    Top = 35
    Width = 9
    Height = 5
    CtrlIndex = 6
    Active = False
  end
  object IndicatorEventSeq08: TIndicator
    Left = 110
    Top = 35
    Width = 9
    Height = 5
    CtrlIndex = 7
    Active = False
  end
  object IndicatorEventSeq09: TIndicator
    Left = 122
    Top = 35
    Width = 9
    Height = 5
    CtrlIndex = 8
    Active = False
  end
  object IndicatorEventSeq10: TIndicator
    Left = 134
    Top = 35
    Width = 9
    Height = 5
    CtrlIndex = 9
    Active = False
  end
  object IndicatorEventSeq11: TIndicator
    Left = 146
    Top = 35
    Width = 9
    Height = 5
    CtrlIndex = 10
    Active = False
  end
  object IndicatorEventSeq12: TIndicator
    Left = 158
    Top = 35
    Width = 9
    Height = 5
    CtrlIndex = 11
    Active = False
  end
  object IndicatorEventSeq13: TIndicator
    Left = 170
    Top = 35
    Width = 9
    Height = 5
    CtrlIndex = 12
    Active = False
  end
  object IndicatorEventSeq14: TIndicator
    Left = 182
    Top = 35
    Width = 9
    Height = 5
    CtrlIndex = 13
    Active = False
  end
  object IndicatorEventSeq15: TIndicator
    Left = 194
    Top = 35
    Width = 9
    Height = 5
    CtrlIndex = 14
    Active = False
  end
  object IndicatorEventSeq16: TIndicator
    Left = 206
    Top = 35
    Width = 9
    Height = 5
    CtrlIndex = 15
    Active = False
  end
  object TextLabelEventSeqHeading: TTextLabel
    Left = 28
    Top = 24
    Width = 185
    Height = 11
    Caption = 
      '1    2    3   4    5   6   7    8    9  10   11  12   13 14  15 ' +
      ' 16'
  end
  object SpinnerEventSeqStep: TSpinner
    Left = 207
    Top = 1
    Width = 12
    Height = 20
    Value = 15
    Display = DisplayEventSeqStep
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 15
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayEventSeqStep: TDisplay
    Left = 180
    Top = 4
    Width = 26
    Alignment = taCenter
    Caption = '16'
    TabOrder = 1
    DisplayFormats.Formats = [dfOffset1]
    DisplayFormat = dfOffset1
    Clickable = False
    CtrlIndex = -1
  end
  object ButtonSetEventSeqA01: TButtonSet
    Left = 26
    Top = 42
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqA02: TButtonSet
    Left = 38
    Top = 42
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 5
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqA03: TButtonSet
    Left = 50
    Top = 42
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 6
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqA04: TButtonSet
    Left = 62
    Top = 42
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 7
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqA05: TButtonSet
    Left = 74
    Top = 42
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 8
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqA06: TButtonSet
    Left = 86
    Top = 42
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 9
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqA07: TButtonSet
    Left = 98
    Top = 42
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 10
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqA08: TButtonSet
    Left = 110
    Top = 42
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 11
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqA09: TButtonSet
    Left = 122
    Top = 42
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 12
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqA10: TButtonSet
    Left = 134
    Top = 42
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 13
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqA11: TButtonSet
    Left = 146
    Top = 42
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 14
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqA12: TButtonSet
    Left = 158
    Top = 42
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 15
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqA13: TButtonSet
    Left = 170
    Top = 42
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 16
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqA14: TButtonSet
    Left = 182
    Top = 42
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 17
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqA15: TButtonSet
    Left = 194
    Top = 42
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 18
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqA16: TButtonSet
    Left = 206
    Top = 42
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 19
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqAG: TButtonSet
    Left = 218
    Top = 42
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'G'
      end>
  end
  object ButtonSetEventSeqB01: TButtonSet
    Left = 26
    Top = 57
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 20
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqB02: TButtonSet
    Left = 38
    Top = 57
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 21
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqB03: TButtonSet
    Left = 50
    Top = 57
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 22
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqB04: TButtonSet
    Left = 62
    Top = 57
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 23
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqB05: TButtonSet
    Left = 74
    Top = 57
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 24
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqB06: TButtonSet
    Left = 86
    Top = 57
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 25
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqB07: TButtonSet
    Left = 98
    Top = 57
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 26
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqB08: TButtonSet
    Left = 110
    Top = 57
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 27
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqB09: TButtonSet
    Left = 122
    Top = 57
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 28
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqB10: TButtonSet
    Left = 134
    Top = 57
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 29
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqB11: TButtonSet
    Left = 146
    Top = 57
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 30
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqB12: TButtonSet
    Left = 158
    Top = 57
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 31
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqB13: TButtonSet
    Left = 170
    Top = 57
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 32
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqB14: TButtonSet
    Left = 182
    Top = 57
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 33
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqB15: TButtonSet
    Left = 194
    Top = 57
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 34
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqB16: TButtonSet
    Left = 206
    Top = 57
    Width = 9
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 35
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
      end>
  end
  object ButtonSetEventSeqBG: TButtonSet
    Left = 218
    Top = 57
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'G'
      end>
  end
  object ButtonSetEventSeqLoop: TButtonSet
    Left = 122
    Top = 4
    Width = 30
    Height = 14
    Value = 1
    DisplayFormat = dfOnOff
    CtrlIndex = 1
    DefaultValue = 1
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Loop'
      end>
  end
end
object T_ClkDivFix
  Hint = 'Version = 16'
  TitleLabel = EditLabelClkDivFix
  Title = 'ClkDivFix'
  Description = 'Clock divider, fixed'
  ModuleAttributes = []
  ModuleType = 77
  Cycles = 0.843750000000000000
  ProgMem = 0.750000000000000000
  XMem = 0.437500000000000000
  YMem = 0.093750000000000000
  ZeroPage = 2.625000000000000000
  DynMem = 0.250000000000000000
  UnitHeight = 2
  object EditLabelClkDivFix: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputClkDivFixOut16: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object InputClkDivFixRst: TInput
    Left = 157
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object TextLabelClkDivFixRst: TTextLabel
    Left = 134
    Top = 10
    Width = 15
    Height = 11
    Caption = 'Rst'
  end
  object InputClkDivFixMidiCl: TInput
    Left = 121
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object TextLabelClkDivFixMidiCl: TTextLabel
    Left = 86
    Top = 10
    Width = 27
    Height = 11
    Caption = 'Midi cl'
  end
  object ImageClkDivFixRst: TGraphicImage
    Left = 152
    Top = 11
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object ImageClkDivFixMidiCl: TGraphicImage
    Left = 117
    Top = 11
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object Output1ClkDivFixOutT8: TOutput
    Left = 212
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object OutputClkDivFixOut8: TOutput
    Left = 184
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 2
  end
  object TextLabelClkDivFix16: TTextLabel
    Left = 229
    Top = 10
    Width = 8
    Height = 11
    Caption = '16'
  end
  object TextLabelClkDivFixT8: TTextLabel
    Left = 198
    Top = 10
    Width = 11
    Height = 11
    Caption = 'T8'
  end
  object TextLabelClkDivFix8: TTextLabel
    Left = 176
    Top = 10
    Width = 5
    Height = 11
    Caption = '8'
  end
end
object T_ClkDiv
  Hint = 'Version = 16'
  TitleLabel = EditLabelClkDiv
  Title = 'ClkDiv'
  Description = 'Clock divider'
  ModuleAttributes = []
  ModuleType = 69
  Cycles = 0.625000000000000000
  ProgMem = 0.562500000000000000
  XMem = 0.500000000000000000
  YMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object EditLabelClkDiv: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputClkDivOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object InputClkDivInRst: TInput
    Left = 168
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object TextLabelClkDivRst: TTextLabel
    Left = 145
    Top = 10
    Width = 15
    Height = 11
    Caption = 'Rst'
  end
  object TextLabelClkDivDivider: TTextLabel
    Left = 186
    Top = 1
    Width = 29
    Height = 11
    Caption = 'Divider'
  end
  object InputClkDivInClock: TInput
    Left = 127
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object TextLabelClkDivClock: TTextLabel
    Left = 96
    Top = 10
    Width = 25
    Height = 11
    Caption = 'Clock'
  end
  object ImageClkDivRst: TGraphicImage
    Left = 163
    Top = 11
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object ImageClkDivClock: TGraphicImage
    Left = 123
    Top = 11
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object SpinnerClkDivDivider: TSpinner
    Left = 218
    Top = 6
    Width = 12
    Height = 20
    Value = 0
    Display = DisplayClkDivDivider
    DisplayFormat = dfUnsigned
    CtrlIndex = 6
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayClkDivDivider: TDisplay
    Left = 187
    Top = 12
    Width = 28
    Alignment = taCenter
    Caption = '1'
    TabOrder = 1
    DisplayFormats.Formats = [dfOffset1]
    DisplayFormat = dfOffset1
    Clickable = False
    CtrlIndex = -1
  end
end
object T_CompareAB
  Hint = 'Version = 16'
  TitleLabel = EditLabelCompareAB
  Title = 'CompareAB'
  Description = 'Compare'
  ModuleAttributes = []
  ModuleType = 89
  Cycles = 0.187500000000000000
  ProgMem = 0.187500000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.250000000000000000
  UnitHeight = 2
  object EditLabelCompareAB: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputCompareABOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object InputCompareABB: TInput
    Left = 195
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 1
  end
  object TextLabelCompareABB: TTextLabel
    Left = 185
    Top = 10
    Width = 7
    Height = 11
    Caption = 'B'
  end
  object TextLabelCompareABAgeqB: TTextLabel
    Left = 212
    Top = 10
    Width = 24
    Height = 11
    Caption = 'A>=B'
  end
  object InputCompareABA: TInput
    Left = 163
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object TextLabelCompareABA: TTextLabel
    Left = 153
    Top = 10
    Width = 7
    Height = 11
    Caption = 'A'
  end
end
object T_CompareLev
  Hint = 'Version = 16'
  TitleLabel = EditLabelCompareLev
  Title = 'CompareLev'
  Description = 'Compare to level'
  ModuleAttributes = []
  ModuleType = 59
  Cycles = 0.187500000000000000
  ProgMem = 0.187500000000000000
  XMem = 0.093750000000000000
  YMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object EditLabelCompareLev: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputCompareLevOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object InputCompareLevInA: TInput
    Left = 195
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object TextLabelCompareLevC: TTextLabel
    Left = 102
    Top = 9
    Width = 7
    Height = 11
    Caption = 'C'
  end
  object TextLabelCompareLevA: TTextLabel
    Left = 185
    Top = 10
    Width = 7
    Height = 11
    Caption = 'A'
  end
  object TextLabelCompareLevAgeqC: TTextLabel
    Left = 212
    Top = 10
    Width = 24
    Height = 11
    Caption = 'A>=C'
  end
  object SmallKnobCompareLevLevel: TSmallKnob
    Left = 154
    Top = 4
    Value = 64
    Display = DisplayCompareLevLevel
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayCompareLevLevel: TDisplay
    Left = 114
    Top = 8
    Width = 35
    Alignment = taCenter
    Caption = '0'
    TabOrder = 1
    DisplayFormats.Formats = [dfSigned]
    DisplayFormat = dfSigned
    Clickable = False
    CtrlIndex = -1
  end
end
object T_LogicProc
  Hint = 'Version = 16'
  TitleLabel = EditLabelLogicProc
  Title = 'LogicProc'
  Description = 'Logic processsor'
  ModuleAttributes = []
  ModuleType = 73
  Cycles = 0.281250000000000000
  ProgMem = 0.250000000000000000
  XMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object ImageLogicProcIO: TGraphicImage
    Left = 190
    Top = 6
    Width = 54
    Height = 20
    FileName = '_logic_proc.bmp'
  end
  object EditLabelLogicProc: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputLogicProcOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object InputLogicProcIn2: TInput
    Left = 200
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object InputLogicProcIn1: TInput
    Left = 186
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object ButtonSetLogicProcFunction: TButtonSet
    Left = 90
    Top = 8
    Width = 91
    Height = 14
    Value = 0
    DisplayFormat = dfLogicFunc
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 2
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smRadio
    Clickers = <
      item
        Caption = 'And'
      end
      item
        Caption = 'Or'
      end
      item
        Caption = 'Xor'
      end>
  end
end
object T_LogicInv
  Hint = 'Version = 16'
  TitleLabel = EditLabelLogicInv
  Title = 'LogicInv'
  Description = 'Logic inverter'
  ModuleAttributes = []
  ModuleType = 70
  Cycles = 0.187500000000000000
  ProgMem = 0.187500000000000000
  XMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.250000000000000000
  UnitHeight = 2
  object EditLabelLogicInv: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputLogicInvOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object InputLogicInvIn: TInput
    Left = 205
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object ImageLogicInvIO: TGraphicImage
    Left = 218
    Top = 6
    Width = 20
    Height = 19
    Transparent = False
    FileName = 'LogicInv.bmp'
  end
end
object T_LogicDelay
  Hint = 'Version = 16'
  TitleLabel = EditLabelLogicDelay
  Title = 'LogicDelay'
  Description = 'Logic delay'
  ModuleAttributes = []
  ModuleType = 37
  Cycles = 0.625000000000000000
  ProgMem = 0.562500000000000000
  XMem = 0.156250000000000000
  YMem = 0.281250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object EditLabelLogicDelay: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputLogicDelayOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object InputLogicDelayIn: TInput
    Left = 205
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object ImageLogicDelayIO: TGraphicImage
    Left = 218
    Top = 6
    Width = 20
    Height = 19
    Transparent = False
    FileName = 'LogicDelay.bmp'
  end
  object SmallKnobLogicDelayTime: TSmallKnob
    Left = 170
    Top = 4
    Value = 64
    Display = DisplayLogicDelayTime
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayLogicDelayTime: TDisplay
    Left = 124
    Top = 8
    Width = 43
    Alignment = taCenter
    Caption = '138ms'
    TabOrder = 1
    DisplayFormats.Formats = [dfLogicDelay]
    DisplayFormat = dfLogicDelay
    Clickable = False
    CtrlIndex = -1
  end
end
object T_Pulse
  Hint = 'Version = 16'
  TitleLabel = EditLabelPulse
  Title = 'Pulse'
  Description = 'Pulse'
  ModuleAttributes = []
  ModuleType = 38
  Cycles = 0.343750000000000000
  ProgMem = 0.312500000000000000
  XMem = 0.218750000000000000
  YMem = 0.156250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object EditLabelPulse: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputPulseOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object InputPulseIn: TInput
    Left = 205
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object ImagePulseIO: TGraphicImage
    Left = 218
    Top = 6
    Width = 20
    Height = 19
    Transparent = False
    FileName = 'Pulse.bmp'
  end
  object SmallKnobPulseTime: TSmallKnob
    Left = 170
    Top = 4
    Value = 64
    Display = DisplayPulseTime
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayPulseTime: TDisplay
    Left = 124
    Top = 8
    Width = 43
    Alignment = taCenter
    Caption = '138ms'
    TabOrder = 1
    DisplayFormats.Formats = [dfLogicDelay]
    DisplayFormat = dfLogicDelay
    Clickable = False
    CtrlIndex = -1
  end
end
object T_NegEdgeDly
  Hint = 'Version = 16'
  TitleLabel = EditLabelNegEdgeDly
  Title = 'NegEdgeDly'
  Description = 'Negative edge delay'
  ModuleAttributes = []
  ModuleType = 0
  UnitHeight = 2
  object EditLabelNegEdgeDly: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputNegEdgeDlyOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object InputNegEdgeDlyIn: TInput
    Left = 205
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object ImageNegEdgeDlyIO: TGraphicImage
    Left = 218
    Top = 6
    Width = 20
    Height = 19
    Transparent = False
    FileName = 'NegEdgeDly.bmp'
  end
  object SmallKnobNegEdgeDlyTime: TSmallKnob
    Left = 170
    Top = 4
    Value = 64
    Display = DisplayNegEdgeDlyTime
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayNegEdgeDlyTime: TDisplay
    Left = 124
    Top = 8
    Width = 43
    Alignment = taCenter
    Caption = '138ms'
    TabOrder = 1
    DisplayFormats.Formats = [dfLogicDelay]
    DisplayFormat = dfLogicDelay
    Clickable = False
    CtrlIndex = -1
  end
end
object T_PosEdgeDly
  Hint = 'Version = 16'
  TitleLabel = EditLabelPosEdgeDly
  Title = 'PosEdgeDly'
  Description = 'Positive edge delay'
  ModuleAttributes = []
  ModuleType = 0
  UnitHeight = 2
  object EditLabelPosEdgeDly: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputPosEdgeDlyOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object InputPosEdgeDlyIn: TInput
    Left = 205
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object ImagePosEdgeDlyIO: TGraphicImage
    Left = 218
    Top = 6
    Width = 20
    Height = 19
    Transparent = False
    FileName = 'PosEdgeDly.bmp'
  end
  object SmallKnobPosEdgeDlyTime: TSmallKnob
    Left = 170
    Top = 4
    Value = 64
    Display = DisplayPosEdgeDlyTime
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayPosEdgeDlyTime: TDisplay
    Left = 124
    Top = 8
    Width = 43
    Alignment = taCenter
    Caption = '138ms'
    TabOrder = 1
    DisplayFormats.Formats = [dfLogicDelay]
    DisplayFormat = dfLogicDelay
    Clickable = False
    CtrlIndex = -1
  end
end
object T_NoteVelScal
  Hint = 'Version = 16'
  TitleLabel = EditLabelNoteVelScal
  Title = 'NoteVelScal'
  Description = 'Note and Vel scaler'
  ModuleAttributes = []
  ModuleType = 115
  Cycles = 0.875000000000000000
  ProgMem = 0.781250000000000000
  XMem = 0.343750000000000000
  YMem = 0.343750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.437500000000000000
  UnitHeight = 4
  object BoxlNoteVelScalGraph: TBox
    Left = 195
    Top = 5
    Width = 56
    Height = 27
  end
  object EditLabelNoteVelScal: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputlNoteVelScalOut: TOutput
    Left = 240
    Top = 43
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object InputlNoteVelScalNote: TInput
    Left = 29
    Top = 43
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 1
  end
  object TextLabellNoteVelScalLGain: TTextLabel
    Left = 88
    Top = 1
    Width = 26
    Height = 11
    Caption = 'L Gain'
  end
  object InputlNoteVelScalVel: TInput
    Left = 8
    Top = 43
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object TextLabellNoteVelScalBrkPt: TTextLabel
    Left = 125
    Top = 1
    Width = 27
    Height = 11
    Caption = 'Brk Pt'
  end
  object TextLabellNoteVelScalRGain: TTextLabel
    Left = 160
    Top = 1
    Width = 28
    Height = 11
    Caption = 'R Gain'
  end
  object OscGraphlNoteVelScal: TOscGraph
    Left = 196
    Top = 6
    Width = 54
    Height = 25
    PenColor = clWhite
    GraphType = otSaw
    Cycles = 1.000000000000000000
  end
  object TextLabellNoteVelScalVel: TTextLabel
    Left = 6
    Top = 30
    Width = 13
    Height = 11
    Caption = 'Vel'
  end
  object TextLabellNoteVelScalNote: TTextLabel
    Left = 23
    Top = 30
    Width = 21
    Height = 11
    Caption = 'Note'
  end
  object TextLabellNoteVelScalVelSens: TTextLabel
    Left = 48
    Top = 20
    Width = 36
    Height = 11
    Caption = 'Vel Sens'
  end
  object SmallKnoblNoteVelScalLGain: TSmallKnob
    Left = 93
    Top = 32
    Value = 24
    Display = DisplaylNoteVelScalLGain
    DisplayFormat = dfSigned
    CtrlIndex = 1
    DefaultValue = 24
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 48
    StepSize = 1
  end
  object DisplaylNoteVelScalLGain: TDisplay
    Left = 88
    Top = 14
    Width = 31
    Alignment = taCenter
    Caption = '0dB'
    TabOrder = 1
    DisplayFormats.Formats = [dfVelScalGain]
    DisplayFormat = dfVelScalGain
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnoblNoteVelScalBrkPt: TSmallKnob
    Left = 129
    Top = 32
    Value = 64
    Display = DisplaylNoteVelScalBrkPt
    DisplayFormat = dfSigned
    CtrlIndex = 2
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplaylNoteVelScalBrkPt: TDisplay
    Left = 124
    Top = 14
    Width = 31
    Alignment = taCenter
    Caption = 'E4 '
    TabOrder = 3
    DisplayFormats.Formats = [dfNote]
    DisplayFormat = dfNote
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnoblNoteVelScalRGain: TSmallKnob
    Left = 165
    Top = 32
    Value = 24
    Display = DisplaylNoteVelScalRGain
    DisplayFormat = dfSigned
    CtrlIndex = 3
    DefaultValue = 24
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 48
    StepSize = 1
  end
  object DisplaylNoteVelScalRGain: TDisplay
    Left = 160
    Top = 14
    Width = 31
    Alignment = taCenter
    Caption = '0dB'
    TabOrder = 5
    DisplayFormats.Formats = [dfVelScalGain]
    DisplayFormat = dfVelScalGain
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnoblNoteVelScalVelSens: TSmallKnob
    Left = 55
    Top = 32
    Value = 0
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_ControlMixer
  Hint = 'Version = 16'
  TitleLabel = EditLabelControlMixer
  Title = 'ControlMixer'
  Description = 'Control signal mixer'
  ModuleAttributes = []
  ModuleType = 66
  Cycles = 0.187500000000000000
  ProgMem = 0.187500000000000000
  XMem = 0.093750000000000000
  YMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.468750000000000000
  UnitHeight = 2
  object BoxControlMixerIn1: TBox
    Left = 115
    Top = 2
    Width = 62
    Height = 26
    Shape = bsFrame
  end
  object BoxControlMixerIn2: TBox
    Left = 178
    Top = 2
    Width = 62
    Height = 26
    Shape = bsFrame
  end
  object EditLabelControlMixer: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputControlMixerOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object InputControlMixerIn2: TInput
    Left = 205
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 1
  end
  object InputControlMixerIn1: TInput
    Left = 142
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object SmallKnobControlMixerLevel1: TSmallKnob
    Left = 154
    Top = 4
    Value = 64
    DisplayFormat = dfSigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobControlMixerLevel2: TSmallKnob
    Left = 217
    Top = 4
    Value = 64
    DisplayFormat = dfSigned
    CtrlIndex = 3
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetControlMixerInv1: TButtonSet
    Left = 117
    Top = 8
    Width = 23
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Inv'
      end>
  end
  object ButtonSetControlMixerLev2: TButtonSet
    Left = 180
    Top = 8
    Width = 23
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Inv'
      end>
  end
  object ButtonSetControlMixwerMode: TButtonSet
    Left = 85
    Top = 8
    Width = 23
    Height = 14
    Value = 0
    DisplayFormat = dfCtrlMixerMode
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Lin'
      end>
  end
end
object T_PartialGen
  Hint = 'Version = 16'
  TitleLabel = EditLabelPartialGen
  Title = 'PartialGen'
  Description = 'Partial generator'
  ModuleAttributes = []
  ModuleType = 22
  Cycles = 0.593750000000000000
  ProgMem = 0.531250000000000000
  XMem = 0.281250000000000000
  YMem = 2.187500000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object EditLabelPartialGen: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputPartialGenOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object InputPartialGenIn: TInput
    Left = 205
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object ImagePartialGenIO: TGraphicImage
    Left = 212
    Top = 9
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object SmallKnobPartialGenRange: TSmallKnob
    Left = 170
    Top = 4
    Value = 127
    Display = DisplayPartialGenRange
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayPartialGenRange: TDisplay
    Left = 136
    Top = 8
    Width = 32
    Alignment = taCenter
    Caption = #177'64 *'
    TabOrder = 1
    DisplayFormats.Formats = [dfPartialRange]
    DisplayFormat = dfPartialRange
    Clickable = False
    CtrlIndex = -1
  end
end
object T_KeyQuant
  Hint = 'Version = 16'
  TitleLabel = EditLabelKeyQuant
  Title = 'KeyQuant'
  Description = 'Key quantizer'
  ModuleAttributes = []
  ModuleType = 98
  Cycles = 1.343750000000000000
  ProgMem = 1.187500000000000000
  XMem = 1.031250000000000000
  YMem = 1.031250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.906250000000000000
  UnitHeight = 4
  object EditLabelKeyQuant: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputKeyQuantOut: TOutput
    Left = 240
    Top = 43
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object InputKeyQuantIn: TInput
    Left = 205
    Top = 43
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object ImageKeyQuantIO: TGraphicImage
    Left = 212
    Top = 41
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object TextLabelKeyQuantRange: TTextLabel
    Left = 175
    Top = 5
    Width = 27
    Height = 11
    Caption = 'Range'
  end
  object TextLabelKeyQuantDir: TTextLabel
    Left = 39
    Top = 45
    Width = 49
    Height = 11
    Caption = '-  __  o  __  +'
  end
  object SmallKnobKeyQuantRange: TSmallKnob
    Left = 178
    Top = 34
    Value = 127
    Display = DisplayKeyQuantRange
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayKeyQuantRange: TDisplay
    Left = 174
    Top = 17
    Width = 31
    Alignment = taCenter
    Caption = #177'64'
    TabOrder = 1
    DisplayFormats.Formats = [dfNoteRange]
    DisplayFormat = dfNoteRange
    Clickable = False
    CtrlIndex = -1
  end
  object ButtonSetKeyQuantC: TButtonSet
    Left = 10
    Top = 33
    Width = 20
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 10
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'C'
      end>
  end
  object ButtonSetKeyQuantD: TButtonSet
    Left = 33
    Top = 33
    Width = 20
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 12
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'D'
      end>
  end
  object ButtonSetKeyQuantE: TButtonSet
    Left = 56
    Top = 33
    Width = 20
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'E'
      end>
  end
  object ButtonSetKeyQuantF: TButtonSet
    Left = 79
    Top = 33
    Width = 20
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'F'
      end>
  end
  object ButtonSetKeyQuantG: TButtonSet
    Left = 102
    Top = 33
    Width = 20
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 5
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'G'
      end>
  end
  object ButtonSetKeyQuantA: TButtonSet
    Left = 125
    Top = 33
    Width = 20
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 7
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'A'
      end>
  end
  object ButtonSetKeyQuantB: TButtonSet
    Left = 148
    Top = 33
    Width = 20
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 9
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'B'
      end>
  end
  object ButtonSetKeyQuantCis: TButtonSet
    Left = 21
    Top = 17
    Width = 20
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 11
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'C#'
      end>
  end
  object ButtonSetKeyQuantDis: TButtonSet
    Left = 44
    Top = 17
    Width = 20
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 13
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'D#'
      end>
  end
  object ButtonSetKeyQuantFis: TButtonSet
    Left = 90
    Top = 17
    Width = 20
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'F#'
      end>
  end
  object ButtonSetKeyQuantGis: TButtonSet
    Left = 113
    Top = 17
    Width = 20
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 6
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'G#'
      end>
  end
  object ButtonSetKeyQuantBes: TButtonSet
    Left = 136
    Top = 17
    Width = 20
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 8
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Bb'
      end>
  end
  object ButtonSetKeyQuantCont: TButtonSet
    Left = 208
    Top = 17
    Width = 29
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 1
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Cont'
      end>
  end
end
object T_NoteQuant
  Hint = 'Version = 16'
  TitleLabel = EditLabelNoteQuant
  Title = 'NoteQuant'
  Description = 'Note quantizer'
  ModuleAttributes = []
  ModuleType = 75
  Cycles = 0.437500000000000000
  ProgMem = 0.406250000000000000
  XMem = 0.156250000000000000
  YMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.343750000000000000
  UnitHeight = 2
  object EditLabelNoteQuant: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputNoteQuantIO: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object InputNoteQuantIn: TInput
    Left = 205
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object ImageNoteQuantIO: TGraphicImage
    Left = 212
    Top = 9
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object TextLabelNoteQuantRange: TTextLabel
    Left = 86
    Top = 0
    Width = 27
    Height = 11
    Caption = 'Range'
  end
  object TextLabelNoteQuantNotes: TTextLabel
    Left = 150
    Top = -1
    Width = 26
    Height = 11
    Caption = 'Notes'
  end
  object SmallKnobNoteQuantRange: TSmallKnob
    Left = 120
    Top = 4
    Value = 127
    Display = DisplayNoteQuantRange
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayNoteQuantRange: TDisplay
    Left = 84
    Top = 12
    Alignment = taCenter
    Caption = #177'64'
    TabOrder = 1
    DisplayFormats.Formats = [dfNoteRange]
    DisplayFormat = dfNoteRange
    Clickable = False
    CtrlIndex = -1
  end
  object DisplayNoteQuantNotes: TDisplay
    Left = 150
    Top = 12
    Width = 29
    Alignment = taCenter
    Caption = 'Off'
    TabOrder = 2
    DisplayFormats.Formats = [dfNotesRange]
    DisplayFormat = dfNotesRange
    Clickable = False
    CtrlIndex = -1
  end
  object SpinnerNoteQuantNotes: TSpinner
    Left = 182
    Top = 5
    Width = 12
    Height = 20
    Value = 0
    Display = DisplayNoteQuantNotes
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_NoteScaler
  Hint = 'Version = 16'
  TitleLabel = EditLabelNoteScaler
  Title = 'NoteScaler'
  Description = 'Note scaler'
  ModuleAttributes = []
  ModuleType = 72
  Cycles = 0.156250000000000000
  ProgMem = 0.156250000000000000
  XMem = 0.093750000000000000
  YMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object EditLabelNoteScaler: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputNoteScalerOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object InputNoteScalerIn: TInput
    Left = 205
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object ImageNoteScalerIO: TGraphicImage
    Left = 212
    Top = 9
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object SmallKnobNoteScalerClip: TSmallKnob
    Left = 170
    Top = 4
    Value = 0
    Display = DisplayNoteScalerClip
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayNoteScalerClip: TDisplay
    Left = 106
    Top = 8
    Width = 59
    Alignment = taCenter
    Caption = '0 (Oct)'
    TabOrder = 1
    DisplayFormats.Formats = [dfNoteScale]
    DisplayFormat = dfNoteScale
    Clickable = False
    CtrlIndex = -1
  end
end
object T_PortamentoB
  Hint = 'Version = 16'
  TitleLabel = EditLabelPortamentoB
  Title = 'PortamentoB'
  Description = 'PortamentoB'
  ModuleAttributes = []
  ModuleType = 16
  Cycles = 0.531250000000000000
  ProgMem = 0.468750000000000000
  XMem = 0.156250000000000000
  YMem = 0.218750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object ImagePortamentoBIO: TGraphicImage
    Left = 212
    Top = 9
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object EditLabelPortamentoB: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputPortamentoBOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object InputPortamentoBIn: TInput
    Left = 205
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object InputPortamentoBJmp: TInput
    Left = 181
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object TextLabelPortamentoBJmp: TTextLabel
    Left = 155
    Top = 10
    Width = 18
    Height = 11
    Caption = 'Jmp'
  end
  object TextLabelPortamentoBTime: TTextLabel
    Left = 99
    Top = 10
    Width = 21
    Height = 11
    Caption = 'Time'
  end
  object ImagePortamentoBJmp: TGraphicImage
    Left = 176
    Top = 11
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object SmallKnobPortamentoBTime: TSmallKnob
    Left = 127
    Top = 4
    Value = 64
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_PortamentoA
  Hint = 'Version = 16'
  TitleLabel = EditLabelPortamentoA
  Title = 'PortamentoA'
  Description = 'PortamentoA'
  ModuleAttributes = []
  ModuleType = 48
  Cycles = 0.468750000000000000
  ProgMem = 0.437500000000000000
  XMem = 0.093750000000000000
  YMem = 0.218750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object ImagePortamentoAIO: TGraphicImage
    Left = 212
    Top = 9
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object EditLabelPortamentoA: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputPortamentoAOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object InputPortamentoAIn: TInput
    Left = 205
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object InputPortamentoAOn: TInput
    Left = 181
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object TextLabelPortamentoAOn: TTextLabel
    Left = 166
    Top = 10
    Width = 12
    Height = 11
    Caption = 'On'
  end
  object TextLabelPortamentoATime: TTextLabel
    Left = 99
    Top = 10
    Width = 21
    Height = 11
    Caption = 'Time'
  end
  object SmallKnobPortamentoATime: TSmallKnob
    Left = 127
    Top = 4
    Value = 64
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_Smooth
  Hint = 'Version = 16'
  TitleLabel = EditLabelSmooth
  Title = 'Smooth'
  Description = 'Smooth'
  ModuleAttributes = []
  ModuleType = 39
  Cycles = 0.250000000000000000
  ProgMem = 0.218750000000000000
  XMem = 0.093750000000000000
  YMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object ImageSmoothIO: TGraphicImage
    Left = 212
    Top = 9
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object EditLabelSmooth: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputSmoothOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object InputSmoothIn: TInput
    Left = 205
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object ImageSmoothFunction: TGraphicImage
    Left = 218
    Top = 6
    Width = 20
    Height = 19
    Transparent = False
    FileName = 'Smooth.bmp'
  end
  object SmallKnobSmoothValue: TSmallKnob
    Left = 170
    Top = 4
    Value = 64
    Display = DisplaySmoothValue
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplaySmoothValue: TDisplay
    Left = 124
    Top = 8
    Width = 43
    Alignment = taCenter
    Caption = '138ms'
    TabOrder = 1
    DisplayFormats.Formats = [dfSmoothTime]
    DisplayFormat = dfSmoothTime
    Clickable = False
    CtrlIndex = -1
  end
end
object T_Constant
  Hint = 'Version = 16'
  TitleLabel = EditLabelConstant
  Title = 'Constant'
  Description = 'Constant'
  ModuleAttributes = []
  ModuleType = 43
  ZeroPage = 0.875000000000000000
  DynMem = 0.343750000000000000
  UnitHeight = 2
  object EditLabelConstant: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputConstantOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object SmallKnobConstantValue: TSmallKnob
    Left = 194
    Top = 4
    Value = 64
    Display = DisplayConstantValue
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayConstantValue: TDisplay
    Left = 156
    Top = 8
    Width = 35
    Alignment = taCenter
    Caption = '0'
    TabOrder = 1
    DisplayFormats.Formats = [dfUnsigned, dfSigned]
    DisplayFormat = dfSigned
    Clickable = False
    CtrlIndex = -1
  end
  object ButtonSetConstantUni: TButtonSet
    Left = 105
    Top = 8
    Width = 23
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 1
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Uni'
      end>
  end
end
object T_Digitizer
  Hint = 'Version = 16'
  TitleLabel = EditLabelDigitizer
  Title = 'Digitizer'
  Description = 'Digitizer'
  ModuleAttributes = []
  ModuleType = 118
  Cycles = 3.437500000000000000
  ProgMem = 0.781250000000000000
  XMem = 0.281250000000000000
  YMem = 0.218750000000000000
  ZeroPage = 0.437500000000000000
  DynMem = 0.468750000000000000
  UnitHeight = 3
  object BoxDigitizerRate: TBox
    Left = 86
    Top = 1
    Width = 110
    Height = 43
    Shape = bsFrame
  end
  object BoxDigitizerQuant: TBox
    Left = 2
    Top = 14
    Width = 84
    Height = 30
    Shape = bsFrame
  end
  object EditLabelDigitizer: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelDigitizerQuant: TTextLabel
    Left = 6
    Top = 14
    Width = 25
    Height = 11
    Caption = 'Quant'
  end
  object TextLabelDigitizerBits: TTextLabel
    Left = 45
    Top = 14
    Width = 17
    Height = 11
    Caption = 'Bits'
  end
  object TextLabelDigitizerSample: TTextLabel
    Left = 92
    Top = 3
    Width = 31
    Height = 11
    Caption = 'Sample'
  end
  object TextLabelDigitizerRate: TTextLabel
    Left = 92
    Top = 13
    Width = 20
    Height = 11
    Caption = 'Rate'
  end
  object ImageDigitizerLineRateMod: TGraphicImage
    Left = 130
    Top = 25
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object InputDigitizerRateMod: TInput
    Left = 126
    Top = 25
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 1
  end
  object ImageDigitizerIO: TGraphicImage
    Left = 210
    Top = 27
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object OutputDigitizerOut: TOutput
    Left = 240
    Top = 29
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputDigitizerIn: TInput
    Left = 202
    Top = 29
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object DisplayDigitizerQuant: TDisplay
    Left = 44
    Top = 26
    Width = 21
    Alignment = taCenter
    Caption = '12'
    TabOrder = 3
    DisplayFormats.Formats = [dfOffset1]
    DisplayFormat = dfOffset1
    Clickable = False
    CtrlIndex = -1
  end
  object SpinnerDigitizerQuant: TSpinner
    Left = 70
    Top = 20
    Width = 12
    Height = 20
    Value = 11
    Display = DisplayDigitizerQuant
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 11
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 11
    StepSize = 1
  end
  object SmallKnobDigitizerRateMod: TSmallKnob
    Left = 143
    Top = 20
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobDigitizerRate: TSmallKnob
    Left = 167
    Top = 20
    Value = 64
    Display = DisplayDigitizerRate
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayDigitizerRate: TDisplay
    Left = 126
    Top = 4
    Width = 62
    Alignment = taCenter
    Caption = '1.318kHz'
    TabOrder = 4
    DisplayFormats.Formats = [dfDigitizerHz]
    DisplayFormat = dfDigitizerHz
    Clickable = False
    CtrlIndex = -1
  end
  object ButtonSetDigitizerQuantOff: TButtonSet
    Left = 5
    Top = 26
    Width = 25
    Height = 14
    Value = 1
    DisplayFormat = dfOnOff
    CtrlIndex = 3
    DefaultValue = 1
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Off'
      end>
  end
  object ButtonSetDigitizerRateOff: TButtonSet
    Left = 94
    Top = 26
    Width = 25
    Height = 14
    Value = 1
    DisplayFormat = dfOnOff
    CtrlIndex = 4
    DefaultValue = 1
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Off'
      end>
  end
end
object T_RingMod
  Hint = 'Version = 16'
  TitleLabel = EditLabelRingMod
  Title = 'RingMod'
  Description = 'Ring and amplitude modulator'
  ModuleAttributes = []
  ModuleType = 117
  Cycles = 1.906250000000000000
  ProgMem = 0.437500000000000000
  XMem = 0.093750000000000000
  YMem = 0.156250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.343750000000000000
  UnitHeight = 3
  object ImageRingModIO: TGraphicImage
    Left = 210
    Top = 4
    Width = 30
    Height = 35
    FileName = '_ringmod.bmp'
  end
  object TextLabelRingModAM: TTextLabel
    Left = 155
    Top = 1
    Width = 16
    Height = 11
    Caption = 'AM'
  end
  object TextLabelRingMod0: TTextLabel
    Left = 145
    Top = 33
    Width = 5
    Height = 11
    Caption = '0'
  end
  object TextLabelRingModRM: TTextLabel
    Left = 175
    Top = 33
    Width = 16
    Height = 11
    Caption = 'RM'
  end
  object EditLabelRingMod: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object SetterRingModModType: TSetter
    Left = 159
    Top = 13
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object ImageRingModLineModDepth: TGraphicImage
    Left = 97
    Top = 26
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object InputRingModModDepth: TInput
    Left = 93
    Top = 25
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object InputRingModMod: TInput
    Left = 201
    Top = 8
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object InputRingModIn: TInput
    Left = 201
    Top = 27
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object OutputRingModOut: TOutput
    Left = 239
    Top = 27
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelRingModModDepth: TTextLabel
    Left = 88
    Top = 8
    Width = 45
    Height = 11
    Caption = 'Mod depth'
  end
  object SmallKnobRingModModType: TSmallKnob
    Left = 153
    Top = 20
    Value = 64
    DisplayFormat = dfSigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterRingModModType
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobRingModModDepth: TSmallKnob
    Left = 109
    Top = 20
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_Expander
  Hint = 'Version = 16'
  TitleLabel = EditLabelExpander
  Title = 'Expander'
  Description = 'Expander'
  ModuleAttributes = []
  ModuleType = 105
  Cycles = 8.281250000000000000
  ProgMem = 1.812500000000000000
  XMem = 0.687500000000000000
  YMem = 0.625000000000000000
  ZeroPage = 1.750000000000000000
  DynMem = 0.687500000000000000
  UnitHeight = 7
  object BoxExpanderSideChain: TBox
    Left = 29
    Top = 15
    Width = 62
    Height = 33
    Shape = bsFrame
  end
  object BoxExpanderGraph: TBox
    Left = 94
    Top = 3
    Width = 45
    Height = 45
  end
  object EditLabelExpander: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object InputExpanderLeft: TInput
    Left = 8
    Top = 18
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputExpanderRight: TInput
    Left = 8
    Top = 34
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object InputExpanderSideChain: TInput
    Left = 33
    Top = 18
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object TextLabelExpanderL_1: TTextLabel
    Left = 20
    Top = 16
    Width = 5
    Height = 11
    Caption = 'L'
  end
  object TextLabelExpanderR_1: TTextLabel
    Left = 19
    Top = 34
    Width = 7
    Height = 11
    Caption = 'R'
  end
  object TextLabelExpanderSideChain: TTextLabel
    Left = 45
    Top = 16
    Width = 42
    Height = 11
    Caption = 'Side chain'
  end
  object OscGraphExpander: TOscGraph
    Left = 95
    Top = 4
    Width = 43
    Height = 43
    PenColor = clWhite
  end
  object TextLabelExpanderAttack: TTextLabel
    Left = 10
    Top = 52
    Width = 28
    Height = 11
    Caption = 'Attack'
  end
  object TextLabelExpanderRelease: TTextLabel
    Left = 44
    Top = 52
    Width = 34
    Height = 11
    Caption = 'Release'
  end
  object TextLabelExpanderTresh: TTextLabel
    Left = 85
    Top = 52
    Width = 26
    Height = 11
    Caption = 'Tresh.'
  end
  object TextLabelExpanderRatio: TTextLabel
    Left = 123
    Top = 52
    Width = 23
    Height = 11
    Caption = 'Ratio'
  end
  object TextLabelExpanderGate: TTextLabel
    Left = 161
    Top = 52
    Width = 20
    Height = 11
    Caption = 'Gate'
  end
  object TextLabelExpanderHold: TTextLabel
    Left = 199
    Top = 52
    Width = 20
    Height = 11
    Caption = 'Hold'
  end
  object TextLabelExpanderL_2: TTextLabel
    Left = 230
    Top = 70
    Width = 5
    Height = 11
    Caption = 'L'
  end
  object OutputExpanderLeft: TOutput
    Left = 239
    Top = 71
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object OutputExpanderRight: TOutput
    Left = 239
    Top = 89
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabelExpanderR_2: TTextLabel
    Left = 229
    Top = 88
    Width = 7
    Height = 11
    Caption = 'R'
  end
  object BoxExpanderLedBar: TBox
    Left = 147
    Top = 15
    Width = 96
    Height = 7
    Shape = bsFrame
  end
  object IndicatorExpanderGateActive: TIndicator
    Left = 215
    Top = 40
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object LedBarExpanderGain: TLedBar
    Left = 150
    Top = 17
    Width = 89
    Height = 2
    Value = 0
    CtrlIndex = 0
  end
  object TextLabelExpanderGainReduction: TTextLabel
    Left = 152
    Top = 2
    Width = 74
    Height = 11
    Caption = 'Gain reduction dB'
  end
  object TextLabelExpanderGaindBs: TTextLabel
    Left = 145
    Top = 22
    Width = 99
    Height = 11
    Caption = '60 48 36 30 24 18 12 9 6 3'
  end
  object TextLabelExpanderGateActive: TTextLabel
    Left = 159
    Top = 36
    Width = 49
    Height = 11
    Caption = 'Gate Active'
  end
  object DisplayExpanderAttack: TDisplay
    Left = 8
    Top = 64
    Width = 34
    Alignment = taCenter
    Caption = 'Fast'
    TabOrder = 1
    DisplayFormats.Formats = [dfEnvAttack]
    DisplayFormat = dfEnvAttack
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobExpanderAttack: TSmallKnob
    Left = 15
    Top = 81
    Value = 0
    Display = DisplayExpanderAttack
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayExpanderRelease: TDisplay
    Left = 45
    Top = 64
    Width = 34
    Alignment = taCenter
    Caption = '250m'
    TabOrder = 3
    DisplayFormats.Formats = [dfCompanderRelease]
    DisplayFormat = dfCompanderRelease
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobExpanderRelease: TSmallKnob
    Left = 52
    Top = 81
    Value = 20
    Display = DisplayExpanderRelease
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 20
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayExpanderTresh: TDisplay
    Left = 82
    Top = 64
    Width = 34
    Alignment = taCenter
    Caption = '-60dB'
    TabOrder = 5
    DisplayFormats.Formats = [dfExpanderTresh]
    DisplayFormat = dfExpanderTresh
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobExpanderTresh: TSmallKnob
    Left = 89
    Top = 81
    Value = 24
    Display = DisplayExpanderTresh
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 24
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 84
    StepSize = 1
  end
  object DisplayExpanderRatio: TDisplay
    Left = 119
    Top = 64
    Width = 34
    Alignment = taCenter
    Caption = '4.0:1'
    TabOrder = 7
    DisplayFormats.Formats = [dfCompanderRatio, dfCompressorLimiter]
    DisplayFormat = dfCompanderRatio
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobExpanderRatio: TSmallKnob
    Left = 126
    Top = 81
    Value = 20
    Display = DisplayExpanderRatio
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 20
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 66
    StepSize = 1
  end
  object DisplayExpanderGate: TDisplay
    Left = 156
    Top = 64
    Width = 34
    Alignment = taCenter
    Caption = 'Off'
    TabOrder = 9
    DisplayFormats.Formats = [dfExpanderGate]
    DisplayFormat = dfExpanderGate
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobExpanderGate: TSmallKnob
    Left = 163
    Top = 81
    Value = 0
    Display = DisplayExpanderGate
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 72
    StepSize = 1
  end
  object DisplayExpanderHold: TDisplay
    Left = 193
    Top = 64
    Width = 34
    Alignment = taCenter
    Caption = 'Off'
    TabOrder = 11
    DisplayFormats.Formats = [dfExpanderHold]
    DisplayFormat = dfExpanderHold
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobExpanderHold: TSmallKnob
    Left = 200
    Top = 81
    Value = 0
    Display = DisplayExpanderHold
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetExpanderBypass: TButtonSet
    Left = 236
    Top = 48
    Width = 15
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 8
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'B'
      end>
  end
  object ButtonSetExpanderSideChainMon: TButtonSet
    Left = 61
    Top = 30
    Width = 26
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 7
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Mon'
      end>
  end
  object ButtonSetExpanderSideChainAct: TButtonSet
    Left = 33
    Top = 30
    Width = 26
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 6
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Act'
      end>
  end
end
object T_Compressor
  Hint = 'Version = 16'
  TitleLabel = EditLabelCompressor
  Title = 'Compressor'
  Description = 'Compressor'
  ModuleAttributes = []
  ModuleType = 21
  Cycles = 8.281250000000000000
  ProgMem = 1.812500000000000000
  XMem = 0.562500000000000000
  YMem = 0.562500000000000000
  ZeroPage = 1.750000000000000000
  DynMem = 0.687500000000000000
  UnitHeight = 7
  object BoxCompressorSideChain: TBox
    Left = 29
    Top = 15
    Width = 62
    Height = 33
    Shape = bsFrame
  end
  object BoxCompressorGraph: TBox
    Left = 94
    Top = 3
    Width = 45
    Height = 45
  end
  object EditLabelCompressor: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object InputCompressorLeft: TInput
    Left = 8
    Top = 18
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputCompressorRight: TInput
    Left = 8
    Top = 34
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object InputCompressorSideChain: TInput
    Left = 33
    Top = 18
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object TextLabelCompressorL_1: TTextLabel
    Left = 20
    Top = 16
    Width = 5
    Height = 11
    Caption = 'L'
  end
  object TextLabelCompressorR_1: TTextLabel
    Left = 19
    Top = 34
    Width = 7
    Height = 11
    Caption = 'R'
  end
  object TextLabelCompressorSideChain: TTextLabel
    Left = 45
    Top = 16
    Width = 42
    Height = 11
    Caption = 'Side chain'
  end
  object OscGraphCompressor: TOscGraph
    Left = 95
    Top = 4
    Width = 43
    Height = 43
    PenColor = clWhite
  end
  object TextLabelCompressorAttack: TTextLabel
    Left = 10
    Top = 52
    Width = 28
    Height = 11
    Caption = 'Attack'
  end
  object TextLabelCompressorRelease: TTextLabel
    Left = 44
    Top = 52
    Width = 34
    Height = 11
    Caption = 'Release'
  end
  object TextLabelCompressorTresh: TTextLabel
    Left = 85
    Top = 52
    Width = 26
    Height = 11
    Caption = 'Tresh.'
  end
  object TextLabelCompressorRatio: TTextLabel
    Left = 123
    Top = 52
    Width = 23
    Height = 11
    Caption = 'Ratio'
  end
  object TextLabelCompressorRefLvl: TTextLabel
    Left = 157
    Top = 52
    Width = 29
    Height = 11
    Caption = 'Ref Lvl'
  end
  object TextLabelCompressorLimiter: TTextLabel
    Left = 195
    Top = 52
    Width = 28
    Height = 11
    Caption = 'Limiter'
  end
  object TextLabelCompressorL_2: TTextLabel
    Left = 230
    Top = 70
    Width = 5
    Height = 11
    Caption = 'L'
  end
  object OutputCompressorLeft: TOutput
    Left = 239
    Top = 71
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object OutputCompressorRight: TOutput
    Left = 239
    Top = 89
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabelCompressorR_2: TTextLabel
    Left = 229
    Top = 88
    Width = 7
    Height = 11
    Caption = 'R'
  end
  object BoxCompressorLedBar: TBox
    Left = 147
    Top = 15
    Width = 96
    Height = 7
    Shape = bsFrame
  end
  object IndicatorCompressorLimiter: TIndicator
    Left = 213
    Top = 40
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object LedBarCompressorGain: TLedBar
    Left = 150
    Top = 17
    Width = 89
    Height = 2
    Value = 0
    CtrlIndex = 0
  end
  object TextLabelCompressorGainReduction: TTextLabel
    Left = 152
    Top = 2
    Width = 74
    Height = 11
    Caption = 'Gain reduction dB'
  end
  object TextLabelCompressordBs: TTextLabel
    Left = 146
    Top = 22
    Width = 95
    Height = 11
    Caption = '30 24 18 15 12  9  6  4  2  1'
  end
  object TextLabelCompressorLimActive: TTextLabel
    Left = 161
    Top = 36
    Width = 44
    Height = 11
    Caption = 'Lim Active'
  end
  object DisplayCompressorAttack: TDisplay
    Left = 8
    Top = 64
    Width = 34
    Alignment = taCenter
    Caption = 'Fast'
    TabOrder = 1
    DisplayFormats.Formats = [dfEnvAttack]
    DisplayFormat = dfEnvAttack
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobCompressorAttack: TSmallKnob
    Left = 15
    Top = 81
    Value = 0
    Display = DisplayCompressorAttack
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayCompressorRelease: TDisplay
    Left = 45
    Top = 64
    Width = 34
    Alignment = taCenter
    Caption = '250m'
    TabOrder = 3
    DisplayFormats.Formats = [dfCompanderRelease]
    DisplayFormat = dfCompanderRelease
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobCompressorRelease: TSmallKnob
    Left = 52
    Top = 81
    Value = 20
    Display = DisplayCompressorRelease
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 20
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayCompressorTresh: TDisplay
    Left = 82
    Top = 64
    Width = 34
    Alignment = taCenter
    Caption = '-12dB'
    TabOrder = 5
    DisplayFormats.Formats = [dfCompressorTresh]
    DisplayFormat = dfCompressorTresh
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobCompressorTresh: TSmallKnob
    Left = 89
    Top = 81
    Value = 18
    Display = DisplayCompressorTresh
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 18
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 42
    StepSize = 1
  end
  object DisplayCompressorRatio: TDisplay
    Left = 119
    Top = 64
    Width = 34
    Alignment = taCenter
    Caption = '4.0:1'
    TabOrder = 7
    DisplayFormats.Formats = [dfCompanderRatio]
    DisplayFormat = dfCompanderRatio
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobCompressorRatio: TSmallKnob
    Left = 126
    Top = 81
    Value = 20
    Display = DisplayCompressorRatio
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 20
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 66
    StepSize = 1
  end
  object DisplayCompressorRefLvl: TDisplay
    Left = 156
    Top = 64
    Width = 34
    Alignment = taCenter
    Caption = '0dB'
    TabOrder = 9
    DisplayFormats.Formats = [dfCompressorRefLvl]
    DisplayFormat = dfCompressorRefLvl
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobCompressorRefLvl: TSmallKnob
    Left = 163
    Top = 81
    Value = 30
    Display = DisplayCompressorRefLvl
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 30
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 42
    StepSize = 1
  end
  object DisplayCompressorLimiter: TDisplay
    Left = 193
    Top = 64
    Width = 34
    Alignment = taCenter
    Caption = '0dB'
    TabOrder = 11
    DisplayFormats.Formats = [dfCompressorLimiter]
    DisplayFormat = dfCompressorLimiter
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobCompressorLimiter: TSmallKnob
    Left = 200
    Top = 81
    Value = 12
    Display = DisplayCompressorLimiter
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 12
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 24
    StepSize = 1
  end
  object ButtonSetCompressorBypass: TButtonSet
    Left = 236
    Top = 48
    Width = 15
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 8
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'B'
      end>
  end
  object ButtonSetCompressorSideChainAct: TButtonSet
    Left = 33
    Top = 30
    Width = 26
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 6
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Act'
      end>
  end
  object ButtonSetCompressorSideChainMon: TButtonSet
    Left = 61
    Top = 30
    Width = 26
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 7
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Mon'
      end>
  end
end
object T_Shaper
  Hint = 'Version = 16'
  TitleLabel = EditLabelShaper
  Title = 'Shaper'
  Description = 'Signal shaper'
  ModuleAttributes = []
  ModuleType = 83
  Cycles = 1.906250000000000000
  ProgMem = 0.468750000000000000
  XMem = 0.218750000000000000
  YMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object EditLabelShaper: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object ImageShaperIO: TGraphicImage
    Left = 210
    Top = 9
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object OutputShaperOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputShaperIn: TInput
    Left = 202
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object ButtonSetShaperFunc: TButtonSet
    Left = 86
    Top = 8
    Width = 110
    Height = 14
    Value = 2
    DisplayFormat = dfShaperFunc
    CtrlIndex = 0
    DefaultValue = 2
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smRadio
    Clickers = <
      item
        FileName = '_log2.bmp'
      end
      item
        FileName = '_log1.bmp'
      end
      item
        FileName = '_lin.bmp'
      end
      item
        FileName = '_exp1.bmp'
      end
      item
        FileName = '_exp2.bmp'
      end>
  end
end
object T_InvLevShift
  Hint = 'Version = 16'
  TitleLabel = EditLabelInvLevShift
  Title = 'InvLevShift'
  Description = 'Level shifter / inverter'
  ModuleAttributes = []
  ModuleType = 57
  Cycles = 0.531250000000000000
  ProgMem = 0.156250000000000000
  XMem = 0.093750000000000000
  YMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.343750000000000000
  UnitHeight = 2
  object EditLabelInvLevShift: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object ImageInvLevShiftIO: TGraphicImage
    Left = 210
    Top = 9
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object OutputInvLevShiftOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputInvLevShiftIn: TInput
    Left = 202
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object ButtonSetInvLvlShiftFunc: TButtonSet
    Left = 130
    Top = 8
    Width = 66
    Height = 14
    Value = 0
    DisplayFormat = dfLevelFunc
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smRadio
    Clickers = <
      item
        FileName = '_sine_line.bmp'
      end
      item
        FileName = '_sine_under_line.bmp'
      end
      item
        FileName = '_sine_above_line.bmp'
      end>
  end
  object ButtonSetInvLevShiftInv: TButtonSet
    Left = 86
    Top = 8
    Width = 23
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 1
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Inv'
      end>
  end
end
object T_Phaser
  Hint = 'Version = 16'
  TitleLabel = EditLabelPhaser
  Title = 'Phaser'
  Description = 'Phaser'
  ModuleAttributes = []
  ModuleType = 102
  Cycles = 12.562500000000000000
  ProgMem = 3.406250000000000000
  XMem = 1.156250000000000000
  YMem = 1.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.781250000000000000
  UnitHeight = 7
  object BoxPhaserSpread: TBox
    Left = 170
    Top = 43
    Width = 80
    Height = 37
    Shape = bsFrame
  end
  object ImagePhaserLineLevel: TGraphicImage
    Left = 181
    Top = 89
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object BoxPhaserGraph: TBox
    Left = 169
    Top = 4
    Width = 82
    Height = 37
  end
  object EditLabelPhaser: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object BoxPhaserModulation: TBox
    Left = 3
    Top = 33
    Width = 75
    Height = 68
    Shape = bsFrame
  end
  object TextLabelPhaserRate: TTextLabel
    Left = 18
    Top = 35
    Width = 20
    Height = 11
    Caption = 'Rate'
  end
  object TextLabelPhaserDepth: TTextLabel
    Left = 49
    Top = 35
    Width = 25
    Height = 11
    Caption = 'Depth'
  end
  object ImagePhaserLineFreqMod: TGraphicImage
    Left = 87
    Top = 84
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object InputPhaserFreqMod: TInput
    Left = 83
    Top = 84
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 1
  end
  object TextLabelPhaserCenterFreq: TTextLabel
    Left = 78
    Top = 16
    Width = 49
    Height = 11
    Caption = 'Center Freq'
  end
  object SetterPhaserFeedback: TSetter
    Left = 143
    Top = 29
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object BoxPhaserVerticalBar: TBox
    Left = 130
    Top = 17
    Width = 11
    Height = 84
    Shape = bsLeftLine
  end
  object TextLabelPhaserFeedBk: TTextLabel
    Left = 132
    Top = 17
    Width = 33
    Height = 11
    Caption = 'FeedBk'
  end
  object TextLabelPhaserPeaks: TTextLabel
    Left = 135
    Top = 66
    Width = 27
    Height = 11
    Caption = 'Peaks'
  end
  object OscGraphPhaser: TOscGraph
    Left = 170
    Top = 5
    Width = 80
    Height = 35
    PenColor = clWhite
  end
  object ImagePhaserIO: TGraphicImage
    Left = 209
    Top = 87
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object OutputPhaserOut: TOutput
    Left = 240
    Top = 89
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputPhaserIn: TInput
    Left = 200
    Top = 89
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object ImagePhaserLineSpreadMod: TGraphicImage
    Left = 209
    Top = 62
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object InputPhaserMod: TInput
    Left = 205
    Top = 61
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 6
  end
  object TextLabelPhaserSpread: TTextLabel
    Left = 171
    Top = 43
    Width = 29
    Height = 11
    Caption = 'Spread'
  end
  object KnobPhaserLFORate: TKnob
    Left = 15
    Top = 69
    Value = 60
    Display = DisplayPhaserLFO
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 60
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayPhaserLFO: TDisplay
    Left = 5
    Top = 49
    Width = 46
    Alignment = taCenter
    Caption = '0.51 Hz'
    TabOrder = 3
    DisplayFormats.Formats = [dfLfoHz]
    DisplayFormat = dfLfoHz
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobPhaserLFODepth: TSmallKnob
    Left = 52
    Top = 48
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 9
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = FormStore.SetterLFOBKBT
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object KnobPhaserCenterFreq: TKnob
    Left = 90
    Top = 47
    Value = 64
    Display = DisplayPhaserCenterFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayPhaserCenterFreq: TDisplay
    Left = 79
    Top = 29
    Width = 49
    Alignment = taCenter
    Caption = '1.29kHz'
    TabOrder = 6
    DisplayFormats.Formats = [dfPhaserHz]
    DisplayFormat = dfPhaserHz
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobPhaserFreqMod: TSmallKnob
    Left = 100
    Top = 79
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobPhaserFeedback: TSmallKnob
    Left = 137
    Top = 36
    Value = 64
    DisplayFormat = dfSigned
    CtrlIndex = 5
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterPhaserFeedback
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayPhaserPeaks: TDisplay
    Left = 133
    Top = 85
    Width = 17
    Alignment = taCenter
    Caption = '3'
    TabOrder = 11
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object SpinnerPhaserPeaks: TSpinner
    Left = 152
    Top = 81
    Width = 12
    Height = 20
    Value = 3
    Display = DisplayPhaserPeaks
    DisplayFormat = dfUnsigned
    CtrlIndex = 6
    DefaultValue = 3
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 1
    MaxValue = 6
    StepSize = 1
  end
  object SmallKnobPhaserLevel: TSmallKnob
    Left = 173
    Top = 82
    Value = 127
    DisplayFormat = dfUnsigned
    CtrlIndex = 10
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobPhaserSpreadMod: TSmallKnob
    Left = 220
    Top = 56
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobPhaserSpread: TSmallKnob
    Left = 178
    Top = 56
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 7
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetPhaserBypass: TButtonSet
    Left = 217
    Top = 87
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 8
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'B'
      end>
  end
  object ButtonSetPhaserLFO: TButtonSet
    Left = 46
    Top = 80
    Width = 25
    Height = 14
    Value = 1
    DisplayFormat = dfOnOff
    CtrlIndex = 1
    DefaultValue = 1
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'LFO'
      end>
  end
end
object T_Chorus
  Hint = 'Version = 16'
  TitleLabel = EditLabelChorus
  Title = 'Chorus'
  Description = 'Stereo chorus'
  ModuleAttributes = []
  ModuleType = 0
  UnitHeight = 3
  object EditLabelChorus: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelChorusAmount: TTextLabel
    Left = 141
    Top = 4
    Width = 34
    Height = 11
    Caption = 'Amount'
  end
  object TextLabelChorusDetune: TTextLabel
    Left = 95
    Top = 4
    Width = 30
    Height = 11
    Caption = 'Detune'
  end
  object ImageChorusIO: TGraphicImage
    Left = 207
    Top = 13
    Width = 32
    Height = 19
    FileName = '_pan.bmp'
  end
  object TextLabelChorusL: TTextLabel
    Left = 228
    Top = 8
    Width = 5
    Height = 11
    Caption = 'L'
  end
  object TextLabelChorusR: TTextLabel
    Left = 227
    Top = 26
    Width = 7
    Height = 11
    Caption = 'R'
  end
  object OutputChorusLeft: TOutput
    Left = 239
    Top = 9
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object OutputChorusRight: TOutput
    Left = 239
    Top = 27
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object InputChorusIn: TInput
    Left = 197
    Top = 18
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object SmallKnobChorusDetune: TSmallKnob
    Left = 100
    Top = 18
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobChorusAmount: TSmallKnob
    Left = 147
    Top = 18
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetChorusBypass: TButtonSet
    Left = 212
    Top = 16
    Width = 15
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'B'
      end>
  end
end
object T_Diode
  Hint = 'Version = 16'
  TitleLabel = EditLabelDiode
  Title = 'Diode'
  Description = 'Diode processing'
  ModuleAttributes = []
  ModuleType = 82
  Cycles = 0.656250000000000000
  ProgMem = 0.187500000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object EditLabelDiode: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object ImageDiodeIO: TGraphicImage
    Left = 210
    Top = 9
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object OutputDiodeOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputDiodeIn: TInput
    Left = 202
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object ButtonSetDiodeFunc: TButtonSet
    Left = 90
    Top = 8
    Width = 91
    Height = 14
    Value = 0
    DisplayFormat = dfDiodeFunc
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 2
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smRadio
    Clickers = <
      item
        FileName = '_sine.bmp'
      end
      item
        FileName = '_sine_rect.bmp'
      end
      item
        FileName = '_sine_drect.bmp'
      end>
  end
end
object T_SampleAndHold
  Hint = 'Version = 16'
  TitleLabel = EditLabelSampleAndHold
  Title = 'Sample&Hold'
  Description = 'Sample and hold'
  ModuleAttributes = []
  ModuleType = 53
  Cycles = 1.031250000000000000
  ProgMem = 0.250000000000000000
  YMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.250000000000000000
  UnitHeight = 2
  object EditLabelSampleAndHold: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object ImageSampleAndHoldIO: TGraphicImage
    Left = 167
    Top = 2
    Width = 73
    Height = 19
    FileName = '_sampleandhold.bmp'
  end
  object OutputSampleAndHoldOut: TOutput
    Left = 240
    Top = 10
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputSampleAndHoldIn: TInput
    Left = 192
    Top = 10
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object InputSampleAndHoldClock: TInput
    Left = 163
    Top = 10
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object ImageSampleAndHoldClock: TGraphicImage
    Left = 158
    Top = 10
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
end
object T_Delay
  Hint = 'Version = 16'
  TitleLabel = EditLabelDelay
  Title = 'Delay'
  Description = 'Delay line'
  ModuleAttributes = []
  ModuleType = 78
  Cycles = 8.000000000000000000
  ProgMem = 1.843750000000000000
  XMem = 9.250000000000000000
  YMem = 9.250000000000000000
  ZeroPage = 1.750000000000000000
  DynMem = 0.343750000000000000
  UnitHeight = 3
  object EditLabelDelay: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object ImageDelayIO: TGraphicImage
    Left = 210
    Top = 27
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object OutputDelayVariableOut: TOutput
    Left = 240
    Top = 29
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object InputDelayIn: TInput
    Left = 202
    Top = 29
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object ImageDelayLineMod: TGraphicImage
    Left = 72
    Top = 24
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object InputDelayMod: TInput
    Left = 68
    Top = 24
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 1
  end
  object OutputDelayFixedOut: TOutput
    Left = 240
    Top = 5
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelDelayTime: TTextLabel
    Left = 117
    Top = 11
    Width = 21
    Height = 11
    Caption = 'Time'
  end
  object TextLabelDelay265ms: TTextLabel
    Left = 204
    Top = 4
    Width = 32
    Height = 11
    Caption = '2.65 ms'
  end
  object SmallKnobDelayMod: TSmallKnob
    Left = 85
    Top = 19
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayDelayTime: TDisplay
    Left = 116
    Top = 23
    Width = 45
    Alignment = taCenter
    Caption = '1.33ms'
    TabOrder = 2
    DisplayFormats.Formats = [dfDelayTime]
    DisplayFormat = dfDelayTime
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobDelayTime: TSmallKnob
    Left = 166
    Top = 19
    Value = 64
    Display = DisplayDelayTime
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_Quantizer
  Hint = 'Version = 16'
  TitleLabel = EditLabelQuantizer
  Title = 'Quantizer'
  Description = 'Quantizer'
  ModuleAttributes = []
  ModuleType = 54
  Cycles = 0.531250000000000000
  ProgMem = 0.156250000000000000
  XMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object ImageQuantizerIO: TGraphicImage
    Left = 210
    Top = 9
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object EditLabelQuantizer: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputQuantizerOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputQuantizerIn: TInput
    Left = 202
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelQuantizerBits: TTextLabel
    Left = 124
    Top = 11
    Width = 17
    Height = 11
    Caption = 'Bits'
  end
  object DisplayQuantizerBits: TDisplay
    Left = 146
    Top = 9
    Width = 26
    Alignment = taCenter
    Caption = 'Off'
    TabOrder = 1
    DisplayFormats.Formats = [dfQuantizerBits]
    DisplayFormat = dfQuantizerBits
    Clickable = False
    CtrlIndex = -1
  end
  object SpinnerQuantizerBits: TSpinner
    Left = 175
    Top = 5
    Width = 12
    Height = 20
    Value = 12
    Display = DisplayQuantizerBits
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 12
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 12
    StepSize = 1
  end
end
object T_WaveWrap
  Hint = 'Version = 16'
  TitleLabel = EditLabelWaveWrap
  Title = 'WaveWarap'
  Description = 'Wave Wrapper'
  ModuleAttributes = []
  ModuleType = 74
  Cycles = 2.687500000000000000
  ProgMem = 0.625000000000000000
  XMem = 0.093750000000000000
  YMem = 0.156250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.342750012874603300
  UnitHeight = 3
  object BoxWaveWrapGraph: TBox
    Left = 160
    Top = 4
    Width = 37
    Height = 37
  end
  object EditLabelWaveWrap: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelWaveWrapLevel: TTextLabel
    Left = 126
    Top = 3
    Width = 22
    Height = 11
    Caption = 'Level'
  end
  object ImageWaveWrapIO: TGraphicImage
    Left = 210
    Top = 27
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object OutputWaveWrapOut: TOutput
    Left = 240
    Top = 29
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputWaveWrapIn: TInput
    Left = 202
    Top = 29
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object ImageWaveWrapLineMod: TGraphicImage
    Left = 80
    Top = 21
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object InputWaveWrapLevelMod: TInput
    Left = 76
    Top = 21
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object OscGraphWaveWrap: TOscGraph
    Left = 161
    Top = 5
    Width = 35
    Height = 35
    PenColor = clWhite
  end
  object KnobWaveWrapLevel: TKnob
    Left = 124
    Top = 14
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobWaveWrapLevelMod: TSmallKnob
    Left = 93
    Top = 16
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_Overdrive
  Hint = 'Version = 16'
  TitleLabel = EditLabelOverdrive
  Title = 'Overdrive'
  Description = 'Overdrive'
  ModuleAttributes = []
  ModuleType = 62
  Cycles = 4.000000000000000000
  ProgMem = 1.656250000000000000
  XMem = 0.281250000000000000
  YMem = 0.343750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.343750000000000000
  UnitHeight = 3
  object BoxOverdriveGraph: TBox
    Left = 160
    Top = 4
    Width = 37
    Height = 37
  end
  object EditLabelOverdrive: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelOverdriveLevel: TTextLabel
    Left = 126
    Top = 3
    Width = 22
    Height = 11
    Caption = 'Level'
  end
  object ImageOverdriveIO: TGraphicImage
    Left = 210
    Top = 27
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object OutputOverdriveOut: TOutput
    Left = 240
    Top = 29
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputOverdriveIn: TInput
    Left = 202
    Top = 29
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object ImageOverdriveLineMod: TGraphicImage
    Left = 80
    Top = 21
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object InputOverdriveLevelMod: TInput
    Left = 76
    Top = 21
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object OscGraphOverdrive: TOscGraph
    Left = 161
    Top = 5
    Width = 35
    Height = 35
    PenColor = clWhite
  end
  object KnobOverdriveLevel: TKnob
    Left = 124
    Top = 14
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOverdriveLevelMod: TSmallKnob
    Left = 93
    Top = 16
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_Clip
  Hint = 'Version = 16'
  TitleLabel = EditLabelClip
  Title = 'Clip'
  Description = 'Clip'
  ModuleAttributes = []
  ModuleType = 61
  Cycles = 1.531250000000000000
  ProgMem = 0.375000000000000000
  XMem = 0.093750000000000000
  YMem = 0.156250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.375000000000000000
  UnitHeight = 3
  object BoxClipGraph: TBox
    Left = 160
    Top = 4
    Width = 37
    Height = 37
  end
  object EditLabelClip: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelClipLevel: TTextLabel
    Left = 126
    Top = 3
    Width = 22
    Height = 11
    Caption = 'Level'
  end
  object ImageClipIO: TGraphicImage
    Left = 210
    Top = 27
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object OutputClipOut: TOutput
    Left = 240
    Top = 29
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputClipIn: TInput
    Left = 202
    Top = 29
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object ImageClipLineMod: TGraphicImage
    Left = 80
    Top = 21
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object InputClipMod: TInput
    Left = 76
    Top = 21
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object OscGraphClip: TOscGraph
    Left = 161
    Top = 5
    Width = 35
    Height = 35
    PenColor = clWhite
  end
  object KnobClipLevel: TKnob
    Left = 124
    Top = 14
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobClipLevelMod: TSmallKnob
    Left = 93
    Top = 16
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetClipSym: TButtonSet
    Left = 39
    Top = 19
    Width = 26
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Sym'
      end>
  end
end
object T_Amplifier
  Hint = 'Version = 16'
  TitleLabel = EditLabelAmplifier
  Title = 'Amplifier'
  Description = 'Amplifier'
  ModuleAttributes = []
  ModuleType = 81
  Cycles = 0.656250000000000000
  ProgMem = 0.187500000000000000
  XMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object ImageAmplifierIO: TGraphicImage
    Left = 169
    Top = 3
    Width = 73
    Height = 19
    FileName = '_levmult.bmp'
  end
  object EditLabelAmplifier: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputAmplifierOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputAmplifierIn: TInput
    Left = 201
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object SmallKnobAmplifierGain: TSmallKnob
    Left = 120
    Top = 4
    Value = 64
    Display = DisplayAmplifierGain
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayAmplifierGain: TDisplay
    Left = 149
    Top = 8
    Width = 41
    Alignment = taCenter
    Caption = 'x1.00'
    TabOrder = 1
    DisplayFormats.Formats = [dfAmpGain]
    DisplayFormat = dfAmpGain
    Clickable = False
    CtrlIndex = -1
  end
end
object T_1to4Switch
  Hint = 'Version = 16'
  TitleLabel = EditLabel1to4Switch
  Title = '1-4Switch'
  Description = '1-4 Switch'
  ModuleAttributes = []
  ModuleType = 88
  Cycles = 0.906250000000000000
  ProgMem = 0.218750000000000000
  XMem = 0.093750000000000000
  ZeroPage = 3.500000000000000000
  DynMem = 0.375000000000000000
  UnitHeight = 3
  object EditLabel1to4Switch: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object Output1to4SwitchOut4: TOutput
    Left = 237
    Top = 30
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 3
  end
  object Image1to4SwitchLineIn: TGraphicImage
    Left = 95
    Top = 25
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object Input1to4SwitchIn: TInput
    Left = 114
    Top = 25
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object TextLabel1to4SwitchOutput: TTextLabel
    Left = 149
    Top = 5
    Width = 28
    Height = 11
    Caption = 'Output'
  end
  object Output1to4SwitchOut3: TOutput
    Left = 220
    Top = 30
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object Output1to4SwitchOut2: TOutput
    Left = 203
    Top = 30
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object Output1to4Switch1: TOutput
    Left = 186
    Top = 30
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object SmallKnob1to4SwitchInputLevel: TSmallKnob
    Left = 87
    Top = 18
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSet1to4SwitchSelect: TButtonSet
    Left = 182
    Top = 3
    Width = 68
    Height = 14
    Value = 0
    DisplayFormat = dfOffset1
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smRadio
    Clickers = <
      item
        Caption = '1'
      end
      item
        Caption = '2'
      end
      item
        Caption = '3'
      end
      item
        Caption = '4'
      end>
  end
  object ButtonSet1to4SwitchMute: TButtonSet
    Left = 163
    Top = 28
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
end
object T_4to1Switch
  Hint = 'Version = 16'
  TitleLabel = EditLabel4to1Switch
  Title = '4-1Switch'
  Description = '4-1 Switch'
  ModuleAttributes = []
  ModuleType = 79
  Cycles = 1.031250000000000000
  ProgMem = 0.218750000000000000
  XMem = 0.281250000000000000
  YMem = 0.156250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.531250000000000000
  UnitHeight = 3
  object EditLabel4to1Switch: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object Output4to1SwitchOut: TOutput
    Left = 240
    Top = 30
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object Image4to1SwitchLineIn1: TGraphicImage
    Left = 15
    Top = 25
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object Input4to1SwitchIn1: TInput
    Left = 34
    Top = 25
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object Image4to1SwitchLineIn2: TGraphicImage
    Left = 67
    Top = 25
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object Input4to1SwitchIn2: TInput
    Left = 86
    Top = 25
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object Image4to1SwitchLineIn3: TGraphicImage
    Left = 119
    Top = 25
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object Input4to1SwitchIn3: TInput
    Left = 138
    Top = 25
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 3
  end
  object Image4to1SwitchLineIn4: TGraphicImage
    Left = 171
    Top = 25
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object Input4to1SwitchIn4: TInput
    Left = 190
    Top = 25
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 4
  end
  object TextLabel4to1Switch1: TTextLabel
    Left = 46
    Top = 24
    Width = 3
    Height = 11
    Caption = '1'
  end
  object TextLabel4to1Switch2: TTextLabel
    Left = 98
    Top = 24
    Width = 5
    Height = 11
    Caption = '2'
  end
  object TextLabel4to1Switch3: TTextLabel
    Left = 150
    Top = 24
    Width = 5
    Height = 11
    Caption = '3'
  end
  object TextLabel4to1Switch4: TTextLabel
    Left = 202
    Top = 24
    Width = 5
    Height = 11
    Caption = '4'
  end
  object TextLabel4to1SwitchInput: TTextLabel
    Left = 155
    Top = 5
    Width = 20
    Height = 11
    Caption = 'Input'
  end
  object SmallKnob4to1SwitchInputLevel1: TSmallKnob
    Left = 7
    Top = 18
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnob4to1SwitchInputLevel2: TSmallKnob
    Left = 59
    Top = 18
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnob4to1SwitchInputLevel3: TSmallKnob
    Left = 111
    Top = 18
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnob4to1SwitchInputLevel4: TSmallKnob
    Left = 163
    Top = 18
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSet4to1SwitchSelect: TButtonSet
    Left = 182
    Top = 3
    Width = 68
    Height = 14
    Value = 0
    DisplayFormat = dfOffset1
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smRadio
    Clickers = <
      item
        Caption = '1'
      end
      item
        Caption = '2'
      end
      item
        Caption = '3'
      end
      item
        Caption = '4'
      end>
  end
  object ButtonSet4to1SwitchMute: TButtonSet
    Left = 219
    Top = 28
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 5
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
end
object T_OnOff
  Hint = 'Version = 16'
  TitleLabel = EditLabelOnOff
  Title = 'OnOff'
  Description = 'On/Off switch'
  ModuleAttributes = []
  ModuleType = 76
  Cycles = 0.406250000000000000
  ProgMem = 0.125000000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object ImageOnOffIO: TGraphicImage
    Left = 169
    Top = 3
    Width = 73
    Height = 19
    FileName = '_onoff.bmp'
  end
  object EditLabelOnOff: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputOnOffOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputOnOffIn: TInput
    Left = 201
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object ButtonSetOnOffOnOff: TButtonSet
    Left = 158
    Top = 8
    Width = 23
    Height = 14
    Value = 1
    DisplayFormat = dfOnOff
    CtrlIndex = 0
    DefaultValue = 1
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'On'
      end>
  end
end
object T_LevAdd
  Hint = 'Version = 16'
  TitleLabel = EditLabelLevAdd
  Title = 'LevAdd'
  Description = 'Adjustable offset'
  ModuleAttributes = []
  ModuleType = 112
  Cycles = 0.531250000000000000
  ProgMem = 0.156250000000000000
  XMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.343750000000000000
  UnitHeight = 2
  object ImageLevAddIO: TGraphicImage
    Left = 169
    Top = 3
    Width = 73
    Height = 19
    FileName = '_levadd.bmp'
  end
  object EditLabelLevAdd: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputLevAddOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputLevAddIn: TInput
    Left = 201
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object SmallKnobLevAddOffset: TSmallKnob
    Left = 130
    Top = 4
    Value = 64
    Display = DisplayLevAddOffset
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayLevAddOffset: TDisplay
    Left = 156
    Top = 8
    Width = 27
    Alignment = taCenter
    Caption = '64'
    TabOrder = 1
    DisplayFormats.Formats = [dfUnsigned, dfSigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object ButtonSetLevAddUni: TButtonSet
    Left = 100
    Top = 8
    Width = 23
    Height = 14
    Value = 1
    DisplayFormat = dfOnOff
    CtrlIndex = 0
    DefaultValue = 1
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Uni'
      end>
  end
end
object T_LevMult
  Hint = 'Version = 16'
  TitleLabel = EditLabelLevMult
  Title = 'LevMult'
  Description = 'Adjustable gain control'
  ModuleAttributes = []
  ModuleType = 111
  Cycles = 0.531250000000000000
  ProgMem = 0.156250000000000000
  XMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.343750000000000000
  UnitHeight = 2
  object ImageLevMultIO: TGraphicImage
    Left = 169
    Top = 3
    Width = 73
    Height = 19
    FileName = '_levmult.bmp'
  end
  object EditLabelLevMult: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputLevMultOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputLevMultIn: TInput
    Left = 201
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object SmallKnobLevMultMultiplier: TSmallKnob
    Left = 130
    Top = 4
    Value = 64
    Display = DisplayLevMultMultiplier
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayLevMultMultiplier: TDisplay
    Left = 156
    Top = 8
    Width = 27
    Alignment = taCenter
    Caption = '64'
    TabOrder = 1
    DisplayFormats.Formats = [dfUnsigned, dfSigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object ButtonSetLevMultUni: TButtonSet
    Left = 100
    Top = 8
    Width = 23
    Height = 14
    Value = 1
    DisplayFormat = dfOnOff
    CtrlIndex = 0
    DefaultValue = 1
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Uni'
      end>
  end
end
object T_2to1fade
  Hint = 'Version = 16'
  TitleLabel = EditLabel2to1fade
  Title = '2to1Fade'
  Description = '2 in to 1 out fader'
  ModuleAttributes = []
  ModuleType = 114
  Cycles = 0.781250000000000000
  ProgMem = 0.218750000000000000
  XMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object Image2to1fadeIO: TGraphicImage
    Left = 162
    Top = 6
    Width = 84
    Height = 19
    FileName = '_2to1.bmp'
  end
  object EditLabel2to1fade: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object Output2to1fadeOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object Input2to1fadeIn1: TInput
    Left = 156
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabel2to1fade1_2: TTextLabel
    Left = 168
    Top = 16
    Width = 3
    Height = 11
    Caption = '1'
  end
  object TextLabel2to1fade2_2: TTextLabel
    Left = 199
    Top = 16
    Width = 5
    Height = 11
    Caption = '2'
  end
  object TextLabel2to1fade1_1: TTextLabel
    Left = 107
    Top = 17
    Width = 3
    Height = 11
    Caption = '1'
  end
  object TextLabel2to1fade2_1: TTextLabel
    Left = 137
    Top = 17
    Width = 5
    Height = 11
    Caption = '2'
  end
  object Input2to1fadeIn2: TInput
    Left = 187
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object SmallKnob2to1FadeFade: TSmallKnob
    Left = 114
    Top = 4
    Value = 64
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_1to2Fade
  Hint = 'Version = 16'
  TitleLabel = EditLabel1to2fade
  Title = '1to2Fade'
  Description = '1 in to 2 out fader'
  ModuleAttributes = []
  ModuleType = 113
  Cycles = 0.781250000000000000
  ProgMem = 0.218750000000000000
  XMem = 0.093750000000000000
  YMem = 0.093750000000000000
  ZeroPage = 1.750000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object Image1to2fadeIO: TGraphicImage
    Left = 159
    Top = 6
    Width = 84
    Height = 19
    FileName = '_1to2.bmp'
  end
  object EditLabel1to2fade: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object Output1to2fadeOut2: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object Input1to2fadeIn: TInput
    Left = 154
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object Output1to2fadeOut1: TOutput
    Left = 206
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabel1to2fade1_2: TTextLabel
    Left = 200
    Top = 16
    Width = 3
    Height = 11
    Caption = '1'
  end
  object TextLabel1to2fade2_2: TTextLabel
    Left = 233
    Top = 16
    Width = 5
    Height = 11
    Caption = '2'
  end
  object TextLabel1to2fade1_1: TTextLabel
    Left = 107
    Top = 17
    Width = 3
    Height = 11
    Caption = '1'
  end
  object TextLabel1to2fade2_1: TTextLabel
    Left = 137
    Top = 17
    Width = 5
    Height = 11
    Caption = '2'
  end
  object SmallKnob1to2fadefade: TSmallKnob
    Left = 114
    Top = 4
    Value = 64
    DisplayFormat = dfSigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_Pan
  Hint = 'Version = 16'
  TitleLabel = EditLabelPan
  Title = 'Pan'
  Description = 'Pan'
  ModuleAttributes = []
  ModuleType = 47
  Cycles = 1.656250000000000000
  ProgMem = 0.406250000000000000
  XMem = 0.093750000000000000
  YMem = 0.093750000000000000
  ZeroPage = 1.750000000000000000
  DynMem = 0.343750000000000000
  UnitHeight = 3
  object ImagePanIO: TGraphicImage
    Left = 207
    Top = 13
    Width = 32
    Height = 19
    FileName = '_pan.bmp'
  end
  object BoxPanControl: TBox
    Left = 90
    Top = 3
    Width = 100
    Height = 38
    Shape = bsFrame
  end
  object ImagePanLineMod: TGraphicImage
    Left = 100
    Top = 23
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelPan: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelPanL1: TTextLabel
    Left = 148
    Top = 29
    Width = 5
    Height = 11
    Caption = 'L'
  end
  object TextLabelPanR1: TTextLabel
    Left = 178
    Top = 29
    Width = 7
    Height = 11
    Caption = 'R'
  end
  object TextLabelPanPan: TTextLabel
    Left = 94
    Top = 5
    Width = 17
    Height = 11
    Caption = 'Pan'
  end
  object InputPanMod: TInput
    Left = 95
    Top = 23
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object SetterPanPan: TSetter
    Left = 161
    Top = 7
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelPanL2: TTextLabel
    Left = 228
    Top = 8
    Width = 5
    Height = 11
    Caption = 'L'
  end
  object TextLabelPanR2: TTextLabel
    Left = 227
    Top = 26
    Width = 7
    Height = 11
    Caption = 'R'
  end
  object OutputPanOutL: TOutput
    Left = 239
    Top = 9
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object OutputPanOutR: TOutput
    Left = 239
    Top = 27
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object InputPanIn: TInput
    Left = 206
    Top = 18
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object SmallKnobPanMod: TSmallKnob
    Left = 116
    Top = 16
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobPanPan: TSmallKnob
    Left = 155
    Top = 16
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterPanPan
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_XFade
  Hint = 'Version = 16'
  TitleLabel = EditLabelXFade
  Title = 'X-Fade'
  Description = 'X-fade with modulator'
  ModuleAttributes = []
  ModuleType = 18
  Cycles = 1.656250000000000000
  ProgMem = 0.406250000000000000
  XMem = 0.093750000000000000
  YMem = 0.156250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.343750000000000000
  UnitHeight = 3
  object ImageXFadeIO: TGraphicImage
    Left = 215
    Top = 13
    Width = 32
    Height = 19
    FileName = '_xfade.bmp'
  end
  object BoxXFadeControl: TBox
    Left = 90
    Top = 3
    Width = 100
    Height = 38
    Shape = bsFrame
  end
  object ImageXFadeLineMod: TGraphicImage
    Left = 100
    Top = 23
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelXFade: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelXFade1_1: TTextLabel
    Left = 148
    Top = 29
    Width = 3
    Height = 11
    Caption = '1'
  end
  object OutputXFadeOut: TOutput
    Left = 240
    Top = 18
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputXFadeIn1: TInput
    Left = 213
    Top = 9
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelXFade2_1: TTextLabel
    Left = 178
    Top = 29
    Width = 5
    Height = 11
    Caption = '2'
  end
  object InputXFadeIn2: TInput
    Left = 213
    Top = 27
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabelXFadeXFade: TTextLabel
    Left = 94
    Top = 5
    Width = 27
    Height = 11
    Caption = 'X-fade'
  end
  object InputXFadeMod: TInput
    Left = 95
    Top = 23
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object SetterXFadeCrossFade: TSetter
    Left = 161
    Top = 7
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelXFade1_2: TTextLabel
    Left = 206
    Top = 8
    Width = 3
    Height = 11
    Caption = '1'
  end
  object TextLabelXFade2_2: TTextLabel
    Left = 205
    Top = 26
    Width = 5
    Height = 11
    Caption = '2'
  end
  object SmallKnobXFadeMod: TSmallKnob
    Left = 116
    Top = 16
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobXFadeCrossFade: TSmallKnob
    Left = 155
    Top = 16
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterXFadeCrossFade
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_GainControl
  Hint = 'Version = 16'
  TitleLabel = EditLabelGainControl
  Title = 'GainControl'
  Description = 'Gain controller (multiply)'
  ModuleAttributes = []
  ModuleType = 44
  Cycles = 0.781250000000000000
  ProgMem = 0.218750000000000000
  XMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object ImageGainControlIO: TGraphicImage
    Left = 146
    Top = 3
    Width = 94
    Height = 19
    FileName = '_gaincontrol.bmp'
  end
  object EditLabelGainControl: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelGainControlControl: TTextLabel
    Left = 100
    Top = 10
    Width = 32
    Height = 11
    Caption = 'Control'
  end
  object OutputGainControlOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputGainControlControl: TInput
    Left = 144
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputGainControlIn: TInput
    Left = 204
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object ButtonSetGainControlShift: TButtonSet
    Left = 160
    Top = 8
    Width = 25
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        FileName = '_sine_above_line.bmp'
      end>
  end
end
object T_Mixer8
  Hint = 'Version = 16'
  TitleLabel = EditLabelMixer8
  Title = 'Mixer'
  Description = '8 inputs mixer'
  ModuleAttributes = []
  ModuleType = 40
  Cycles = 2.187500000000000000
  ProgMem = 0.500000000000000000
  XMem = 0.281250000000000000
  YMem = 0.343750000000000000
  ZeroPage = 0.906250000000000000
  DynMem = 0.687500000000000000
  UnitHeight = 4
  object EditLabelMixer8: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelMixer8In1: TTextLabel
    Left = 7
    Top = 19
    Width = 3
    Height = 11
    Caption = '1'
  end
  object OutputMixer8Out: TOutput
    Left = 240
    Top = 44
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputMixer8In1: TInput
    Left = 13
    Top = 19
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object IndicatorMixer8Out: TIndicator
    Left = 225
    Top = 47
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object TextLabelMixer8In2: TTextLabel
    Left = 33
    Top = 19
    Width = 5
    Height = 11
    Caption = '2'
  end
  object InputMixer8In2: TInput
    Left = 40
    Top = 19
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabelMixer8In3: TTextLabel
    Left = 60
    Top = 19
    Width = 5
    Height = 11
    Caption = '3'
  end
  object InputMixer8In3: TInput
    Left = 67
    Top = 19
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object TextLabelMixer8In4: TTextLabel
    Left = 87
    Top = 19
    Width = 5
    Height = 11
    Caption = '4'
  end
  object InputMixer8In4: TInput
    Left = 94
    Top = 19
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 3
  end
  object TextLabelMixer8In5: TTextLabel
    Left = 113
    Top = 19
    Width = 5
    Height = 11
    Caption = '5'
  end
  object InputMixer8In5: TInput
    Left = 120
    Top = 19
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 4
  end
  object TextLabelMixer8In6: TTextLabel
    Left = 140
    Top = 19
    Width = 5
    Height = 11
    Caption = '6'
  end
  object InputMixer8In6: TInput
    Left = 147
    Top = 19
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 5
  end
  object TextLabelMixer8In7: TTextLabel
    Left = 167
    Top = 19
    Width = 5
    Height = 11
    Caption = '7'
  end
  object InputMixer8In7: TInput
    Left = 174
    Top = 19
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 6
  end
  object TextLabelMixer8In8: TTextLabel
    Left = 194
    Top = 19
    Width = 5
    Height = 11
    Caption = '8'
  end
  object InputMixer8In8: TInput
    Left = 201
    Top = 19
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 7
  end
  object SmallKnobMixer8In1: TSmallKnob
    Left = 6
    Top = 34
    Value = 100
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 100
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobMixer8In2: TSmallKnob
    Left = 33
    Top = 34
    Value = 100
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 100
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobMixer8In3: TSmallKnob
    Left = 60
    Top = 34
    Value = 100
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 100
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobMixer8In4: TSmallKnob
    Left = 87
    Top = 34
    Value = 100
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 100
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobMixer8In5: TSmallKnob
    Left = 113
    Top = 34
    Value = 100
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 100
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobMixer8In6: TSmallKnob
    Left = 140
    Top = 34
    Value = 100
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 100
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobMixer8In7: TSmallKnob
    Left = 167
    Top = 34
    Value = 100
    DisplayFormat = dfUnsigned
    CtrlIndex = 6
    DefaultValue = 100
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobMixer8In8: TSmallKnob
    Left = 194
    Top = 34
    Value = 100
    DisplayFormat = dfUnsigned
    CtrlIndex = 7
    DefaultValue = 100
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetMixer8Attenuation: TButtonSet
    Left = 225
    Top = 18
    Width = 26
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 8
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = '-6dB'
      end>
  end
end
object T_Mixer3
  Hint = 'Version = 16'
  TitleLabel = EditLabelMixer3
  Title = 'Mixer'
  Description = '3 inputs mixer'
  ModuleAttributes = []
  ModuleType = 19
  Cycles = 0.906250000000000000
  ProgMem = 0.218750000000000000
  XMem = 0.156250000000000000
  YMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.375000000000000000
  UnitHeight = 2
  object EditLabelMixer3: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelMixer3In1: TTextLabel
    Left = 86
    Top = 10
    Width = 3
    Height = 11
    Caption = '1'
  end
  object OutputMixer3Out: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object ImageMixer3LineIn1: TGraphicImage
    Left = 97
    Top = 11
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object InputMixer3In1: TInput
    Left = 93
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelMixer3In2: TTextLabel
    Left = 134
    Top = 10
    Width = 5
    Height = 11
    Caption = '2'
  end
  object ImageMixer3LineIn2: TGraphicImage
    Left = 145
    Top = 11
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object InputMixer3In2: TInput
    Left = 141
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabelMixer3In3: TTextLabel
    Left = 182
    Top = 10
    Width = 5
    Height = 11
    Caption = '3'
  end
  object ImageMixer3LineIn3: TGraphicImage
    Left = 193
    Top = 11
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object InputMixer3In3: TInput
    Left = 189
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object SmallKnobMixer3In1: TSmallKnob
    Left = 108
    Top = 4
    Value = 127
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobMixer3In2: TSmallKnob
    Left = 156
    Top = 4
    Value = 127
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobMixer3In3: TSmallKnob
    Left = 204
    Top = 4
    Value = 127
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_EqShelving
  Hint = 'Version = 16'
  TitleLabel = EditLabelEqShelving
  Title = 'EqShelving'
  Description = 'Hi and lo shelving Eq'
  ModuleAttributes = []
  ModuleType = 104
  Cycles = 2.437500000000000000
  ProgMem = 0.562500000000000000
  XMem = 0.218750000000000000
  YMem = 0.156250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.468750000000000000
  UnitHeight = 4
  object BoxEqShelvingGraph: TBox
    Left = 185
    Top = 3
    Width = 67
    Height = 29
  end
  object EditLabelEqShelving: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object BoxEqShelvingFreq: TBox
    Left = 2
    Top = 15
    Width = 85
    Height = 42
    Shape = bsFrame
  end
  object TextLabelEqShelvingFreq: TTextLabel
    Left = 6
    Top = 15
    Width = 43
    Height = 11
    Caption = 'Frequency'
  end
  object ImageEqShelvingIO: TGraphicImage
    Left = 211
    Top = 41
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object OutputEqShelvingOut: TOutput
    Left = 240
    Top = 43
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputEqShelvingIn: TInput
    Left = 203
    Top = 43
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelEqShelvingGain: TTextLabel
    Left = 102
    Top = 5
    Width = 19
    Height = 11
    Caption = 'Gain'
  end
  object OscGraphEqShelving: TOscGraph
    Left = 184
    Top = 4
    Width = 67
    Height = 27
    PenColor = clWhite
  end
  object IndicatorEqShelvingClip: TIndicator
    Left = 240
    Top = 34
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object KnobEqShelvingFreq: TKnob
    Left = 57
    Top = 27
    Value = 60
    Display = DisplayEqShelvingFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 60
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayEqShelvingFreq: TDisplay
    Left = 6
    Top = 33
    Width = 48
    Alignment = taCenter
    Caption = '471 Hz'
    TabOrder = 3
    DisplayFormats.Formats = [dfEqHz]
    DisplayFormat = dfEqHz
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobEqShelvingInputGain: TSmallKnob
    Left = 178
    Top = 35
    Value = 100
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 100
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobEqShelvingGain: TSmallKnob
    Left = 103
    Top = 34
    Value = 64
    Display = DisplayEqShelvingGain
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayEqShelvingGain: TDisplay
    Left = 94
    Top = 16
    Width = 39
    Alignment = taCenter
    Caption = '64'
    TabOrder = 4
    DisplayFormats.Formats = [dfUnsigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object ButtonSetEqShelvingType: TButtonSet
    Left = 147
    Top = 5
    Width = 24
    Height = 26
    Value = 0
    DisplayFormat = dfEqShelvingType
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soVertical
    Direction = diReversed
    SelectMode = smRadio
    Clickers = <
      item
        Caption = 'Lo'
      end
      item
        Caption = 'Hi'
      end>
  end
  object ButtonSetEqShelvingBypass: TButtonSet
    Left = 218
    Top = 40
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'B'
      end>
  end
end
object T_EqMid
  Hint = 'Version = 16'
  TitleLabel = EditLabelEqMid
  Title = 'EqMid'
  Description = 'Parametric Eq'
  ModuleAttributes = []
  ModuleType = 103
  Cycles = 3.343750000000000000
  ProgMem = 1.062500000000000000
  XMem = 0.343750000000000000
  YMem = 0.562500000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.468750000000000000
  UnitHeight = 4
  object BoxEqMidGraph: TBox
    Left = 185
    Top = 3
    Width = 67
    Height = 29
  end
  object EditLabelEqMid: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object BoxEqMidFreq: TBox
    Left = 2
    Top = 15
    Width = 85
    Height = 42
    Shape = bsFrame
  end
  object TextLabelEqMidFreq: TTextLabel
    Left = 6
    Top = 15
    Width = 43
    Height = 11
    Caption = 'Frequency'
  end
  object ImageEqMidIO: TGraphicImage
    Left = 211
    Top = 41
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object OutputEqMidOut: TOutput
    Left = 240
    Top = 43
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputEqMidIn: TInput
    Left = 203
    Top = 43
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelEqMidGain: TTextLabel
    Left = 102
    Top = 5
    Width = 19
    Height = 11
    Caption = 'Gain'
  end
  object TextLabelEqMidBW: TTextLabel
    Left = 148
    Top = 5
    Width = 15
    Height = 11
    Caption = 'BW'
  end
  object OscGraphEqMid: TOscGraph
    Left = 184
    Top = 4
    Width = 67
    Height = 27
    PenColor = clWhite
  end
  object IndicatorEqMidClip: TIndicator
    Left = 240
    Top = 34
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object KnobEqMidFreq: TKnob
    Left = 57
    Top = 27
    Value = 60
    Display = DisplayEqMidFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 60
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayEqMidFreq: TDisplay
    Left = 6
    Top = 33
    Width = 48
    Alignment = taCenter
    Caption = '471 Hz'
    TabOrder = 3
    DisplayFormats.Formats = [dfEqHz]
    DisplayFormat = dfEqHz
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobEqMidInputGain: TSmallKnob
    Left = 178
    Top = 35
    Value = 100
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 100
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobEqMidGain: TSmallKnob
    Left = 103
    Top = 34
    Value = 64
    Display = DisplayEqMidGain
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayEqMidGain: TDisplay
    Left = 94
    Top = 16
    Width = 39
    Alignment = taCenter
    Caption = '64'
    TabOrder = 5
    DisplayFormats.Formats = [dfUnsigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobEqMidBW: TSmallKnob
    Left = 146
    Top = 34
    Value = 64
    Display = DisplayEqMidBW
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayEqMidBW: TDisplay
    Left = 137
    Top = 16
    Width = 39
    Alignment = taCenter
    Caption = '64'
    TabOrder = 6
    DisplayFormats.Formats = [dfUnsigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object ButtonSetEqMidBypass: TButtonSet
    Left = 218
    Top = 41
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'B'
      end>
  end
end
object T_FilterBank
  Hint = 'Version = 16'
  TitleLabel = EditLabelFilterBank
  Title = 'FilterBank'
  Description = 'FilterBank'
  ModuleAttributes = []
  ModuleType = 32
  Cycles = 17.937500000000000000
  ProgMem = 9.000000000000000000
  XMem = 5.593750000000000000
  YMem = 10.218750000000000000
  ZeroPage = 0.906250000000000000
  DynMem = 0.968750000000000000
  UnitHeight = 7
  object BoxFilterBankGraph: TBox
    Left = 15
    Top = 22
    Width = 226
    Height = 43
  end
  object EditLabelFilterBank: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object ClickerFilterBankPresetMin: TClicker
    Left = 98
    Top = 88
    Width = 24
    Height = 14
    AllowAllUp = True
    Caption = 'Min'
    Margin = 1
    CtrlIndex = -1
  end
  object ClickerFilterBankPresetMax: TClicker
    Left = 134
    Top = 88
    Width = 24
    Height = 14
    AllowAllUp = True
    Caption = 'Max'
    Margin = 1
    CtrlIndex = -1
  end
  object TextLabelFilterBankPresets: TTextLabel
    Left = 54
    Top = 89
    Width = 33
    Height = 11
    Caption = 'Presets'
  end
  object ImageFilterBankIO: TGraphicImage
    Left = 212
    Top = 88
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object OutputFilterBankOut: TOutput
    Left = 240
    Top = 91
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 3
  end
  object InputFilterBankIn: TInput
    Left = 204
    Top = 90
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = -1
  end
  object TextLabelFilterBankLabels: TTextLabel
    Left = 15
    Top = 12
    Width = 222
    Height = 11
    Caption = 
      '50    75   110  170 250 380 570 850 1.3   1.9  2.9  4.2  6.4  8.' +
      '3'
  end
  object BarGraphFilterBank: TBarGraph
    Left = 16
    Top = 23
    Width = 224
    Height = 41
    DataSize = 14
    DefaultValue = 127
  end
  object SpinnerFilterBank01: TSpinner
    Left = 18
    Top = 65
    Width = 12
    Height = 20
    Value = 127
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerFilterBank02: TSpinner
    Left = 34
    Top = 65
    Width = 12
    Height = 20
    Value = 127
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerFilterBank03: TSpinner
    Left = 50
    Top = 65
    Width = 12
    Height = 20
    Value = 127
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerFilterBank04: TSpinner
    Left = 66
    Top = 65
    Width = 12
    Height = 20
    Value = 127
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerFilterBank05: TSpinner
    Left = 82
    Top = 65
    Width = 12
    Height = 20
    Value = 127
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerFilterBank06: TSpinner
    Left = 98
    Top = 65
    Width = 12
    Height = 20
    Value = 127
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerFilterBank07: TSpinner
    Left = 114
    Top = 65
    Width = 12
    Height = 20
    Value = 127
    DisplayFormat = dfUnsigned
    CtrlIndex = 6
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerFilterBank08: TSpinner
    Left = 130
    Top = 65
    Width = 12
    Height = 20
    Value = 127
    DisplayFormat = dfUnsigned
    CtrlIndex = 7
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerFilterBank09: TSpinner
    Left = 146
    Top = 65
    Width = 12
    Height = 20
    Value = 127
    DisplayFormat = dfUnsigned
    CtrlIndex = 8
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerFilterBank10: TSpinner
    Left = 162
    Top = 65
    Width = 12
    Height = 20
    Value = 127
    DisplayFormat = dfUnsigned
    CtrlIndex = 9
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerFilterBank11: TSpinner
    Left = 178
    Top = 65
    Width = 12
    Height = 20
    Value = 127
    DisplayFormat = dfUnsigned
    CtrlIndex = 10
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerFilterBank12: TSpinner
    Left = 194
    Top = 65
    Width = 12
    Height = 20
    Value = 127
    DisplayFormat = dfUnsigned
    CtrlIndex = 11
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerFilterBank13: TSpinner
    Left = 210
    Top = 65
    Width = 12
    Height = 20
    Value = 127
    DisplayFormat = dfUnsigned
    CtrlIndex = 12
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerFilterBank14: TSpinner
    Left = 226
    Top = 65
    Width = 12
    Height = 20
    Value = 127
    DisplayFormat = dfUnsigned
    CtrlIndex = 13
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetFilterBankBypass: TButtonSet
    Left = 219
    Top = 88
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 14
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'B'
      end>
  end
end
object T_Vocoder
  Hint = 'Version = 16'
  TitleLabel = EditLabelVocoder
  Title = 'Vocoder'
  Description = 'Vocoder'
  ModuleAttributes = []
  ModuleType = 108
  Cycles = 48.937500000000000000
  ProgMem = 34.218750000000000000
  XMem = 23.781250000000000000
  YMem = 26.718750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 15.000000000000000000
  UnitHeight = 8
  object BoxVocoderMain: TBox
    Left = 31
    Top = 25
    Width = 194
    Height = 73
  end
  object EditLabelVocoder: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object ImageVocoderIO: TGraphicImage
    Left = 214
    Top = 104
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object OutputVocoderOut: TOutput
    Left = 240
    Top = 106
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputVocoderIn: TInput
    Left = 209
    Top = 106
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabelVocoderAnalysis: TTextLabel
    Left = 108
    Top = 1
    Width = 35
    Height = 11
    Caption = 'Analysis'
  end
  object SetterVocoderOutGain: TSetter
    Left = 234
    Top = 68
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelVocoderGain: TTextLabel
    Left = 229
    Top = 56
    Width = 19
    Height = 11
    Caption = 'Gain'
  end
  object InputVocoderCtrl: TInput
    Left = 9
    Top = 25
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelVocoderCtrl: TTextLabel
    Left = 6
    Top = 14
    Width = 15
    Height = 11
    Caption = 'Ctrl'
  end
  object ClickerVocoderPresetMin2: TClicker
    Left = 41
    Top = 103
    Width = 22
    Height = 14
    AllowAllUp = True
    Caption = '-2'
    Margin = 2
    CtrlIndex = -1
  end
  object ClickerVocoderPresetMin1: TClicker
    Left = 63
    Top = 103
    Width = 22
    Height = 14
    AllowAllUp = True
    Caption = '-1'
    Margin = 2
    CtrlIndex = -1
  end
  object ClickerVocoderPreset1: TClicker
    Left = 107
    Top = 103
    Width = 22
    Height = 14
    AllowAllUp = True
    Caption = '+1'
    Margin = 2
    CtrlIndex = -1
  end
  object ClickerVocoderPreset0: TClicker
    Left = 85
    Top = 103
    Width = 22
    Height = 14
    AllowAllUp = True
    Caption = '0'
    Margin = 5
    CtrlIndex = -1
  end
  object ClickerVocoderPrestInv: TClicker
    Left = 151
    Top = 103
    Width = 22
    Height = 14
    AllowAllUp = True
    Caption = 'Inv'
    Margin = 1
    CtrlIndex = -1
  end
  object ClickerVocoderPreset2: TClicker
    Left = 129
    Top = 103
    Width = 22
    Height = 14
    AllowAllUp = True
    Caption = '+2'
    Margin = 2
    CtrlIndex = -1
  end
  object TextLabelVocoderOutput: TTextLabel
    Left = 224
    Top = 45
    Width = 28
    Height = 11
    Caption = 'Output'
  end
  object ClickerVocoderPresetRnd: TClicker
    Left = 173
    Top = 103
    Width = 22
    Height = 14
    AllowAllUp = True
    Caption = 'Rnd'
    Margin = 0
    CtrlIndex = -1
  end
  object TextLabelVocoderLabels: TTextLabel
    Left = 35
    Top = 14
    Width = 185
    Height = 11
    Caption = 
      '1    2    3   4    5    6    7   8    9  10  11  12  13  14  15 ' +
      ' 16'
  end
  object VocoderGraph: TVocoderGraph
    Left = 31
    Top = 26
    Width = 192
    Height = 50
    PenColor = clWhite
  end
  object TextLabelVocoderPresets: TTextLabel
    Left = 3
    Top = 105
    Width = 33
    Height = 11
    Caption = 'Presets'
  end
  object SmallKnobVocoderOutGain: TSmallKnob
    Left = 228
    Top = 76
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 16
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterVocoderOutGain
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerVocoderBand01: TSpinner
    Left = 32
    Top = 77
    Width = 12
    Height = 20
    Value = 1
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 1
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 16
    StepSize = 1
  end
  object SpinnerVocoderBand02: TSpinner
    Left = 44
    Top = 77
    Width = 12
    Height = 20
    Value = 2
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 2
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 16
    StepSize = 1
  end
  object SpinnerVocoderBand03: TSpinner
    Left = 56
    Top = 77
    Width = 12
    Height = 20
    Value = 3
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 3
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 16
    StepSize = 1
  end
  object SpinnerVocoderBand04: TSpinner
    Left = 68
    Top = 77
    Width = 12
    Height = 20
    Value = 4
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 4
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 16
    StepSize = 1
  end
  object SpinnerVocoderBand05: TSpinner
    Left = 80
    Top = 77
    Width = 12
    Height = 20
    Value = 5
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 5
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 16
    StepSize = 1
  end
  object SpinnerVocoderBand06: TSpinner
    Left = 92
    Top = 77
    Width = 12
    Height = 20
    Value = 6
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 6
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 16
    StepSize = 1
  end
  object SpinnerVocoderBand07: TSpinner
    Left = 104
    Top = 77
    Width = 12
    Height = 20
    Value = 7
    DisplayFormat = dfUnsigned
    CtrlIndex = 6
    DefaultValue = 7
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 16
    StepSize = 1
  end
  object SpinnerVocoderBand08: TSpinner
    Left = 116
    Top = 77
    Width = 12
    Height = 20
    Value = 8
    DisplayFormat = dfUnsigned
    CtrlIndex = 7
    DefaultValue = 8
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 16
    StepSize = 1
  end
  object SpinnerVocoderBand09: TSpinner
    Left = 128
    Top = 77
    Width = 12
    Height = 20
    Value = 9
    DisplayFormat = dfUnsigned
    CtrlIndex = 8
    DefaultValue = 9
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 16
    StepSize = 1
  end
  object SpinnerVocoderBand10: TSpinner
    Left = 140
    Top = 77
    Width = 12
    Height = 20
    Value = 10
    DisplayFormat = dfUnsigned
    CtrlIndex = 9
    DefaultValue = 10
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 16
    StepSize = 1
  end
  object SpinnerVocoderBand11: TSpinner
    Left = 152
    Top = 77
    Width = 12
    Height = 20
    Value = 11
    DisplayFormat = dfUnsigned
    CtrlIndex = 10
    DefaultValue = 11
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 16
    StepSize = 1
  end
  object SpinnerVocoderBand12: TSpinner
    Left = 164
    Top = 77
    Width = 12
    Height = 20
    Value = 12
    DisplayFormat = dfUnsigned
    CtrlIndex = 11
    DefaultValue = 12
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 16
    StepSize = 1
  end
  object SpinnerVocoderBand13: TSpinner
    Left = 176
    Top = 77
    Width = 12
    Height = 20
    Value = 13
    DisplayFormat = dfUnsigned
    CtrlIndex = 12
    DefaultValue = 13
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 16
    StepSize = 1
  end
  object SpinnerVocoderBand14: TSpinner
    Left = 188
    Top = 77
    Width = 12
    Height = 20
    Value = 14
    DisplayFormat = dfUnsigned
    CtrlIndex = 13
    DefaultValue = 14
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 16
    StepSize = 1
  end
  object SpinnerVocoderBand15: TSpinner
    Left = 200
    Top = 77
    Width = 12
    Height = 20
    Value = 15
    DisplayFormat = dfUnsigned
    CtrlIndex = 14
    DefaultValue = 15
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 16
    StepSize = 1
  end
  object SpinnerVocoderBand16: TSpinner
    Left = 212
    Top = 77
    Width = 12
    Height = 20
    Value = 16
    DisplayFormat = dfUnsigned
    CtrlIndex = 15
    DefaultValue = 16
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 16
    StepSize = 1
  end
  object ButtonSetVocoderFilter: TButtonSet
    Left = 3
    Top = 40
    Width = 26
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 17
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        FileName = '_exp.bmp'
      end>
  end
  object ButtonSetVocoderMonitor: TButtonSet
    Left = 3
    Top = 54
    Width = 26
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 18
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Mon'
      end>
  end
end
object T_VocalFilter
  Hint = 'Version = 16'
  TitleLabel = EditLabelVocalFilter
  Title = 'VocalFilter'
  Description = 'Vocal filter'
  ModuleAttributes = []
  ModuleType = 45
  Cycles = 7.593750000000000000
  ProgMem = 3.062500000000000000
  XMem = 1.781250000000000000
  YMem = 2.062500000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.687500000000000000
  UnitHeight = 5
  object BoxVocalFilterNavigator: TBox
    Left = 74
    Top = 15
    Width = 132
    Height = 57
    Shape = bsFrame
  end
  object ImageVocalFilterLineVowelMod: TGraphicImage
    Left = 80
    Top = 54
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object BoxVocalFilterFreq: TBox
    Left = 28
    Top = 15
    Width = 46
    Height = 57
    Shape = bsFrame
  end
  object ImageVocalFilterLineFreqMod: TGraphicImage
    Left = 37
    Top = 54
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelVocalFilter: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object ImageVocalFilterIO: TGraphicImage
    Left = 216
    Top = 52
    Width = 32
    Height = 20
    FileName = '_amp.bmp'
  end
  object OutputVocalFilterOut: TOutput
    Left = 242
    Top = 61
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputVocalFilterIn: TInput
    Left = 211
    Top = 61
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelVocalFilterTitle: TTextLabel
    Left = 133
    Top = 15
    Width = 66
    Height = 11
    Caption = 'Vowel navigator'
  end
  object TextLabelVocalFilterFreq: TTextLabel
    Left = 31
    Top = 15
    Width = 21
    Height = 11
    Caption = 'Freq.'
  end
  object SetterVocalFilterRes: TSetter
    Left = 10
    Top = 41
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelVocalFilterRes: TTextLabel
    Left = 6
    Top = 29
    Width = 17
    Height = 11
    Caption = 'Res'
  end
  object SetterVocalFilterFreq: TSetter
    Left = 55
    Top = 19
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object InputVocalFilterFreqMod: TInput
    Left = 33
    Top = 54
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 2
  end
  object InputVocalFilterVowelMod: TInput
    Left = 76
    Top = 54
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 1
  end
  object DisplayVocalFilterVowel1: TDisplay
    Left = 122
    Top = 32
    Width = 25
    Alignment = taCenter
    Caption = 'A'
    TabOrder = 6
    DisplayFormats.Formats = [dfVowel]
    DisplayFormat = dfVowel
    Clickable = False
    CtrlIndex = -1
  end
  object KnobVocalFilterVowel: TKnob
    Left = 83
    Top = 19
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobVocalFilterOutLevel: TSmallKnob
    Left = 220
    Top = 31
    Value = 100
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 100
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobVocalFilterRes: TSmallKnob
    Left = 4
    Top = 49
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 8
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterVocalFilterRes
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobVocalFilterFreq: TSmallKnob
    Left = 49
    Top = 26
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 6
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterVocalFilterFreq
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobVocalFilterFreqMod: TSmallKnob
    Left = 49
    Top = 48
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 7
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobVocalFilterVowelMod: TSmallKnob
    Left = 92
    Top = 48
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayVocalFilterVowel2: TDisplay
    Left = 149
    Top = 32
    Width = 25
    Alignment = taCenter
    Caption = 'E'
    TabOrder = 7
    DisplayFormats.Formats = [dfVowel]
    DisplayFormat = dfVowel
    Clickable = False
    CtrlIndex = -1
  end
  object DisplayVocalFilterVowel3: TDisplay
    Left = 177
    Top = 32
    Width = 25
    Alignment = taCenter
    Caption = 'I'
    TabOrder = 8
    DisplayFormats.Formats = [dfVowel]
    DisplayFormat = dfVowel
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobVocalFilterVowel1: TSmallKnob
    Left = 124
    Top = 48
    Value = 0
    Display = DisplayVocalFilterVowel1
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 8
    StepSize = 1
  end
  object SmallKnobVocalFilterVowel2: TSmallKnob
    Left = 152
    Top = 48
    Value = 1
    Display = DisplayVocalFilterVowel2
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 1
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 8
    StepSize = 1
  end
  object SmallKnobVocalFilterVowel3: TSmallKnob
    Left = 179
    Top = 48
    Value = 2
    Display = DisplayVocalFilterVowel3
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 2
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 8
    StepSize = 1
  end
end
object T_FilterF
  Hint = 'Version = 16'
  TitleLabel = EditLabelFilterF
  Title = 'FilterF'
  Description = 'Filter F, 24 dB classic LP filter'
  ModuleAttributes = []
  ModuleType = 92
  Cycles = 5.531250000000000000
  ProgMem = 2.000000000000000000
  XMem = 0.687500000000000000
  YMem = 0.687500000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.593750000000000000
  UnitHeight = 6
  object BoxFilterFFreq: TBox
    Left = 2
    Top = 15
    Width = 120
    Height = 74
    Shape = bsFrame
  end
  object ImageFilterFLineFreqMod1: TGraphicImage
    Left = 11
    Top = 46
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object BoxFilterFGraph: TBox
    Left = 185
    Top = 2
    Width = 68
    Height = 37
  end
  object ImageFilterFLineIO: TGraphicImage
    Left = 209
    Top = 70
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object BoxFilterFReso: TBox
    Left = 123
    Top = 15
    Width = 62
    Height = 74
    Shape = bsFrame
  end
  object EditLabelFilterF: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputFilterFOut: TOutput
    Left = 240
    Top = 72
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputFilterFIn: TInput
    Left = 200
    Top = 72
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object TextLabelFilterFTitle: TTextLabel
    Left = 91
    Top = 0
    Width = 65
    Height = 11
    Caption = 'Classic LP filter'
  end
  object TextLabelFilterFReso: TTextLabel
    Left = 127
    Top = 17
    Width = 48
    Height = 11
    Caption = 'Resonance'
  end
  object TextLabelFilterFFreq: TTextLabel
    Left = 6
    Top = 19
    Width = 43
    Height = 11
    Caption = 'Frequency'
  end
  object InputFilterFFreqMod1: TInput
    Left = 7
    Top = 46
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object SetterFilterFKBT: TSetter
    Left = 102
    Top = 57
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelFilterFKBT: TTextLabel
    Left = 98
    Top = 45
    Width = 19
    Height = 11
    Caption = 'KBT'
  end
  object InputFilterFFreqMod2: TInput
    Left = 7
    Top = 71
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 1
  end
  object OscGraphFilterF: TOscGraph
    Left = 186
    Top = 3
    Width = 65
    Height = 35
    PenColor = clWhite
  end
  object ImageLineFreqMod2: TGraphicImage
    Left = 12
    Top = 71
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object KnobFilterFFreq: TKnob
    Left = 56
    Top = 59
    Value = 60
    Display = DisplayFilterFFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 60
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayFilterFFreq: TDisplay
    Left = 46
    Top = 41
    Width = 47
    Alignment = taCenter
    Caption = '330 Hz'
    TabOrder = 5
    DisplayFormats.Formats = [dfNote, dfFilterHz2]
    DisplayFormat = dfFilterHz2
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobFilterFFreqMod1: TSmallKnob
    Left = 21
    Top = 40
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobFilterFKBT: TSmallKnob
    Left = 96
    Top = 65
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterFilterFKBT
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobFilterFFreqMod2: TSmallKnob
    Left = 21
    Top = 65
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object KnobFilterFReso: TKnob
    Left = 140
    Top = 55
    Value = 0
    Display = DisplayFilterFReso
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayFilterFReso: TDisplay
    Left = 141
    Top = 36
    Width = 25
    Alignment = taCenter
    Caption = '0'
    TabOrder = 6
    DisplayFormats.Formats = [dfUnsigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object ButtonSetFilterFSlope: TButtonSet
    Left = 193
    Top = 46
    Width = 51
    Height = 14
    Value = 2
    DisplayFormat = dfFilter2Slope
    CtrlIndex = 5
    DefaultValue = 2
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smRadio
    Clickers = <
      item
        Caption = '12'
      end
      item
        Caption = '18'
      end
      item
        Caption = '24'
      end>
  end
  object ButtonSetFilterFBypass: TButtonSet
    Left = 217
    Top = 69
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 6
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'B'
      end>
  end
end
object T_FilterE
  Hint = 'Version = 16'
  TitleLabel = EditLabelFilterE
  Title = 'FilterE'
  Description = 'Filter E, 24 dB filter'
  ModuleAttributes = []
  ModuleType = 51
  Cycles = 6.625000000000000000
  ProgMem = 1.468750000000000000
  XMem = 0.562500000000000000
  YMem = 0.625000000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.750000000000000000
  UnitHeight = 6
  object BoxFilterEReso: TBox
    Left = 122
    Top = 15
    Width = 63
    Height = 74
    Shape = bsFrame
  end
  object ImageFilterELineReso: TGraphicImage
    Left = 141
    Top = 71
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object InputFilterEResoMod: TInput
    Left = 136
    Top = 71
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object BoxFilterEFreq: TBox
    Left = 2
    Top = 15
    Width = 94
    Height = 74
    Shape = bsFrame
  end
  object ImageFilterELineFreqMod2: TGraphicImage
    Left = 11
    Top = 71
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object ImageFilterELineFreqMod1: TGraphicImage
    Left = 11
    Top = 46
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object BoxFilterEGraph: TBox
    Left = 185
    Top = 2
    Width = 68
    Height = 37
  end
  object ImageFilterELineIO: TGraphicImage
    Left = 209
    Top = 70
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object EditLabelFilterE: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputFilterEOut: TOutput
    Left = 240
    Top = 72
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputFilterEIn: TInput
    Left = 200
    Top = 72
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object TextLabelFilterEReso: TTextLabel
    Left = 126
    Top = 17
    Width = 48
    Height = 11
    Caption = 'Resonance'
  end
  object TextLabelFilterEFreq: TTextLabel
    Left = 6
    Top = 19
    Width = 21
    Height = 11
    Caption = 'Freq.'
  end
  object InputFilterEFreqMod1: TInput
    Left = 7
    Top = 46
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object SetterFilterEKBT: TSetter
    Left = 78
    Top = 57
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelFilterEKBT: TTextLabel
    Left = 74
    Top = 45
    Width = 19
    Height = 11
    Caption = 'KBT'
  end
  object InputFilterEFreqMod2: TInput
    Left = 7
    Top = 71
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 3
  end
  object OscGraphFilterE: TOscGraph
    Left = 186
    Top = 3
    Width = 65
    Height = 35
    PenColor = clWhite
  end
  object DisplayFilterEReso: TDisplay
    Left = 126
    Top = 29
    Width = 25
    Alignment = taCenter
    Caption = '0'
    TabOrder = 7
    DisplayFormats.Formats = [dfUnsigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object KnobFilterEFreq: TKnob
    Left = 45
    Top = 36
    Value = 60
    Display = DisplayFilterEFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 60
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object KnobFilterEReso: TKnob
    Left = 155
    Top = 31
    Value = 0
    Display = DisplayFilterEReso
    DisplayFormat = dfUnsigned
    CtrlIndex = 6
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayFilterEFreq: TDisplay
    Left = 36
    Top = 18
    Width = 48
    Alignment = taCenter
    Caption = '330 Hz'
    TabOrder = 0
    DisplayFormats.Formats = [dfNote, dfFilterHz2]
    DisplayFormat = dfFilterHz2
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobFilterEFreqMod1: TSmallKnob
    Left = 21
    Top = 40
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobFilterEKBT: TSmallKnob
    Left = 72
    Top = 65
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterFilterEKBT
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobFilterEResoMod: TSmallKnob
    Left = 150
    Top = 65
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobFilterEFreqMod2: TSmallKnob
    Left = 21
    Top = 65
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 8
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetFilterEType: TButtonSet
    Left = 97
    Top = 31
    Width = 24
    Height = 56
    Value = 0
    DisplayFormat = dfFilter2Type
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soVertical
    Direction = diReversed
    SelectMode = smRadio
    Clickers = <
      item
        Caption = 'LP'
      end
      item
        Caption = 'BP'
      end
      item
        Caption = 'HP'
      end
      item
        Caption = 'BR'
      end>
  end
  object ButtonSetFilterESlope: TButtonSet
    Left = 202
    Top = 46
    Width = 34
    Height = 14
    Value = 1
    DisplayFormat = dfFilter1Slope
    CtrlIndex = 7
    DefaultValue = 1
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smRadio
    Clickers = <
      item
        Caption = '12'
      end
      item
        Caption = '24'
      end>
  end
  object ButtonSetFilterEGC: TButtonSet
    Left = 126
    Top = 44
    Width = 24
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 1
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'GC'
      end>
  end
  object ButtonSetFilterEBypasss: TButtonSet
    Left = 217
    Top = 70
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 9
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'B'
      end>
  end
end
object T_FilterD
  Hint = 'Version = 16'
  TitleLabel = EditLabelFilterD
  Title = 'FilterD'
  Description = 'Filter D, 12 dB multimode filter'
  ModuleAttributes = []
  ModuleType = 49
  Cycles = 4.031250000000000000
  ProgMem = 1.531250000000000000
  XMem = 0.562500000000000000
  YMem = 0.437500000000000000
  ZeroPage = 2.625000000000000000
  DynMem = 0.437500000000000000
  UnitHeight = 5
  object BoxFilterDFreq: TBox
    Left = 2
    Top = 15
    Width = 114
    Height = 57
    Shape = bsFrame
  end
  object ImageFilterDLineFreq: TGraphicImage
    Left = 11
    Top = 54
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object ImageFilterDLineSplit: TGraphicImage
    Left = 207
    Top = 27
    Width = 39
    Height = 35
    FileName = 'filt_fork.bmp'
  end
  object BoxFilterDReso: TBox
    Left = 117
    Top = 15
    Width = 63
    Height = 57
    Shape = bsFrame
  end
  object EditLabelFilterD: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputFilterDHPOut: TOutput
    Left = 240
    Top = 23
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object OutputFilterDBPOut: TOutput
    Left = 240
    Top = 40
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object OutputFilterDLPOut: TOutput
    Left = 240
    Top = 56
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object InputFilterDIn: TInput
    Left = 200
    Top = 40
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabelFilterDTitle: TTextLabel
    Left = 91
    Top = 0
    Width = 88
    Height = 11
    Caption = '12 dB multimode filter'
  end
  object TextLabelReso: TTextLabel
    Left = 121
    Top = 17
    Width = 48
    Height = 11
    Caption = 'Resonance'
  end
  object TextLabelFilterDFreq: TTextLabel
    Left = 7
    Top = 17
    Width = 43
    Height = 11
    Caption = 'Frequency'
  end
  object InputFilterDFreqMod: TInput
    Left = 7
    Top = 54
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object SetterFilterDKBT: TSetter
    Left = 97
    Top = 40
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelFilterDKBT: TTextLabel
    Left = 93
    Top = 28
    Width = 19
    Height = 11
    Caption = 'KBT'
  end
  object TextLabelFilterDLP: TTextLabel
    Left = 221
    Top = 55
    Width = 12
    Height = 11
    Caption = 'LP'
  end
  object TextLabelFilterDBP: TTextLabel
    Left = 221
    Top = 39
    Width = 14
    Height = 11
    Caption = 'BP'
  end
  object TextLabelFilterDHP: TTextLabel
    Left = 221
    Top = 23
    Width = 14
    Height = 11
    Caption = 'HP'
  end
  object DisplayFilterDReso: TDisplay
    Left = 121
    Top = 50
    Width = 25
    Alignment = taCenter
    Caption = '0'
    TabOrder = 5
    DisplayFormats.Formats = [dfUnsigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object KnobFilterDFreq: TKnob
    Left = 53
    Top = 42
    Value = 60
    Display = DisplayFilterDFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 60
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object KnobFilterDReso: TKnob
    Left = 150
    Top = 42
    Value = 0
    Display = DisplayFilterDReso
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayFilterDFreq: TDisplay
    Left = 7
    Top = 30
    Width = 48
    Alignment = taCenter
    Caption = '330 Hz'
    TabOrder = 0
    DisplayFormats.Formats = [dfNote, dfFilterHz2]
    DisplayFormat = dfFilterHz2
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobFilterDFreqMod: TSmallKnob
    Left = 21
    Top = 48
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobFilterDKBT: TSmallKnob
    Left = 91
    Top = 48
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterFilterDKBT
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_FilterC
  Hint = 'Version = 16'
  TitleLabel = EditLabelFilterC
  Title = 'FilterC'
  Description = 'Filter C, 12dB static multimode filter'
  ModuleAttributes = []
  ModuleType = 50
  Cycles = 1.781250000000000000
  ProgMem = 0.437500000000000000
  XMem = 0.218750000000000000
  YMem = 0.156250000000000000
  ZeroPage = 2.625000000000000000
  DynMem = 0.406250000000000000
  UnitHeight = 4
  object ImageFilterCLineSplit: TGraphicImage
    Left = 207
    Top = 12
    Width = 39
    Height = 35
    FileName = 'filt_fork.bmp'
  end
  object BoxFilterCReso: TBox
    Left = 117
    Top = 15
    Width = 63
    Height = 42
    Shape = bsFrame
  end
  object EditLabelFilterC: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputFilterCOutHP: TOutput
    Left = 240
    Top = 8
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object OutputFilterCOutBP: TOutput
    Left = 240
    Top = 25
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object OutputFilterCOutLP: TOutput
    Left = 240
    Top = 41
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputFilterCIn: TInput
    Left = 200
    Top = 25
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object BoxFilterCFreq: TBox
    Left = 31
    Top = 15
    Width = 85
    Height = 42
    Shape = bsFrame
  end
  object TextLabelFilterCTitle: TTextLabel
    Left = 91
    Top = 0
    Width = 113
    Height = 11
    Caption = '12 dB static multimode filter'
  end
  object TextLabelFilterCReso: TTextLabel
    Left = 121
    Top = 15
    Width = 48
    Height = 11
    Caption = 'Resonance'
  end
  object TextLabelFilterCFreq: TTextLabel
    Left = 35
    Top = 15
    Width = 43
    Height = 11
    Caption = 'Frequency'
  end
  object TextLabelFilterCHP: TTextLabel
    Left = 221
    Top = 8
    Width = 14
    Height = 11
    Caption = 'HP'
  end
  object TextLabelFilterCBP: TTextLabel
    Left = 221
    Top = 24
    Width = 14
    Height = 11
    Caption = 'BP'
  end
  object TextLabelFilterCLP: TTextLabel
    Left = 221
    Top = 40
    Width = 12
    Height = 11
    Caption = 'LP'
  end
  object DisplayFilterCReso: TDisplay
    Left = 121
    Top = 25
    Width = 25
    Alignment = taCenter
    Caption = '0'
    TabOrder = 2
    DisplayFormats.Formats = [dfUnsigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object KnobFilterCFreq: TKnob
    Left = 86
    Top = 27
    Value = 60
    Display = DisplayFilterCFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 60
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object KnobFilterCReso: TKnob
    Left = 148
    Top = 27
    Value = 0
    Display = DisplayFilterCReso
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayFilterCFreq: TDisplay
    Left = 35
    Top = 33
    Width = 48
    Alignment = taCenter
    Caption = '330 Hz'
    TabOrder = 3
    DisplayFormats.Formats = [dfNote, dfFilterHz2]
    DisplayFormat = dfFilterHz2
    Clickable = True
    CtrlIndex = 0
  end
  object ButtonSetFilterDGC: TButtonSet
    Left = 121
    Top = 41
    Width = 24
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'GC'
      end>
  end
end
object T_FilterB
  Hint = 'Version = 16'
  TitleLabel = EditLabelFilterB
  Title = 'FilterB'
  Description = 'Filter B, 6dB static HP filter'
  ModuleAttributes = []
  ModuleType = 87
  Cycles = 1.031250000000000000
  ProgMem = 0.250000000000000000
  XMem = 0.093750000000000000
  YMem = 0.156250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object ImageFilterBLine: TGraphicImage
    Left = 210
    Top = 9
    Width = 31
    Height = 13
    Cursor = 1
    FileName = '_thing.bmp'
  end
  object EditLabelFilterB: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputFilterBOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object ImageFilterBFilterType: TGraphicImage
    Left = 218
    Top = 5
    Width = 19
    Height = 19
    Transparent = False
    FileName = 'FilterB.bmp'
  end
  object InputFilterBIn: TInput
    Left = 204
    Top = 11
    Width = 10
    Height = 10
    Cursor = 1
    WireColor = clRed
    CtrlIndex = 0
  end
  object SmallKnobFilterBFreq: TSmallKnob
    Left = 163
    Top = 5
    Value = 64
    Display = DisplayFilterBFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayFilterBFreq: TDisplay
    Left = 106
    Top = 9
    Width = 52
    Alignment = taCenter
    Caption = '504 Hz'
    TabOrder = 1
    DisplayFormats.Formats = [dfFilterHz1]
    DisplayFormat = dfFilterHz1
    Clickable = False
    CtrlIndex = -1
  end
end
object T_FilterA
  Hint = 'Version = 16'
  TitleLabel = EditLabelFilterA
  Title = 'FilterA'
  Description = 'Filter A, 6dB static LP filter'
  ModuleAttributes = []
  ModuleType = 86
  Cycles = 0.656250000000000000
  ProgMem = 0.187500000000000000
  XMem = 0.093750000000000000
  YMem = 0.093750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object ImageFilterALine: TGraphicImage
    Left = 210
    Top = 9
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object EditLabelFilterA: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputFilterAOut: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object ImageFilterAFilterType: TGraphicImage
    Left = 218
    Top = 5
    Width = 20
    Height = 19
    Transparent = False
    FileName = 'FilterA.bmp'
  end
  object InputFilterAIn: TInput
    Left = 204
    Top = 11
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object SmallKnobFilterAFreq: TSmallKnob
    Left = 163
    Top = 5
    Value = 64
    Display = DisplayFilterAFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayFilterAFreq: TDisplay
    Left = 105
    Top = 9
    Width = 52
    Alignment = taCenter
    Caption = '504 Hz'
    TabOrder = 1
    DisplayFormats.Formats = [dfFilterHz1]
    DisplayFormat = dfFilterHz1
    Clickable = False
    CtrlIndex = -1
  end
end
object T_EnvFollower
  Hint = 'Version = 16'
  TitleLabel = EditLabelEnvFollower
  Title = 'EnvFollower'
  Description = 'Envelope follower'
  ModuleAttributes = []
  ModuleType = 71
  Cycles = 2.312500000000000000
  ProgMem = 0.500000000000000000
  XMem = 0.218750000000000000
  YMem = 0.156250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.343750000000000000
  UnitHeight = 3
  object ImageEnvFollowerThing: TGraphicImage
    Left = 210
    Top = 29
    Width = 31
    Height = 13
    FileName = '_thing.bmp'
  end
  object EditLabelEnvFollower: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelEnvFollowerRelease: TTextLabel
    Left = 133
    Top = 29
    Width = 34
    Height = 11
    Caption = 'Release'
  end
  object TextLabelEnvFollowerAttack: TTextLabel
    Left = 68
    Top = 29
    Width = 28
    Height = 11
    Caption = 'Attack'
  end
  object OutputEnvFollowerOut: TOutput
    Left = 240
    Top = 31
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object InputEnvFollowerIn: TInput
    Left = 202
    Top = 31
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object SmallKnobEnvFollowerAttack: TSmallKnob
    Left = 100
    Top = 19
    Value = 0
    Display = DisplayEnvFollowerAttack
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayEnvFollowerAttack: TDisplay
    Left = 93
    Top = 2
    Alignment = taCenter
    Caption = 'Fast'
    TabOrder = 2
    DisplayFormats.Formats = [dfEnvAttack]
    DisplayFormat = dfEnvAttack
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobEnvFollowerRelease: TSmallKnob
    Left = 171
    Top = 19
    Value = 20
    Display = DisplayEnvFollowerRelease
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 20
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayEnvFollowerRelease: TDisplay
    Left = 164
    Top = 2
    Alignment = taCenter
    Caption = '80.0m'
    TabOrder = 3
    DisplayFormats.Formats = [dfEnvRelease]
    DisplayFormat = dfEnvRelease
    Clickable = False
    CtrlIndex = -1
  end
end
object T_MultiEnv
  Hint = 'Version = 16'
  TitleLabel = EditLabelMulti_Env
  Title = 'Multi_Env'
  Description = 'Multistage envelope'
  ModuleAttributes = []
  ModuleType = 52
  Cycles = 1.968750000000000000
  ProgMem = 1.406250000000000000
  XMem = 2.531250000000000000
  YMem = 0.437500000000000000
  ZeroPage = 1.750000000000000000
  DynMem = 0.781250000000000000
  UnitHeight = 7
  object ImageMulti_EnvAmp: TGraphicImage
    Left = 210
    Top = 80
    Width = 32
    Height = 20
    FileName = '_amp.bmp'
  end
  object BoxMulti_EnvMultiEnvGraph: TBox
    Left = 165
    Top = 3
    Width = 86
    Height = 28
  end
  object EditLabelMulti_Env: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputMulti_EnvEnv: TOutput
    Left = 240
    Top = 71
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object IndicatorMulti_EnvGate: TIndicator
    Left = 7
    Top = 55
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object TextLabelMulti_EnvL1: TTextLabel
    Left = 24
    Top = 44
    Width = 8
    Height = 11
    Caption = 'L1'
  end
  object InputMulti_EnvAmp: TInput
    Left = 6
    Top = 89
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 2
  end
  object TextLabelMulti_EnvAmp: TTextLabel
    Left = 1
    Top = 76
    Width = 20
    Height = 11
    Caption = 'Amp'
  end
  object OutputMulti_EnvOLut: TOutput
    Left = 240
    Top = 89
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabelMulti_EnvGate: TTextLabel
    Left = 2
    Top = 44
    Width = 20
    Height = 11
    Caption = 'Gate'
  end
  object InputMulti_EnvGate: TInput
    Left = 6
    Top = 62
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object TextLabelMulti_EnvL2: TTextLabel
    Left = 59
    Top = 44
    Width = 10
    Height = 11
    Caption = 'L2'
  end
  object TextLabelMulti_EnvL3: TTextLabel
    Left = 94
    Top = 44
    Width = 10
    Height = 11
    Caption = 'L3'
  end
  object TextLabelMulti_EnvL4: TTextLabel
    Left = 129
    Top = 44
    Width = 10
    Height = 11
    Caption = 'L4'
  end
  object InputMulti_EnvIn: TInput
    Left = 202
    Top = 89
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabelMulti_EnvEnv: TTextLabel
    Left = 218
    Top = 70
    Width = 16
    Height = 11
    Caption = 'Env'
  end
  object MultiEnvGraphMulti_Env: TMultiEnvGraph
    Left = 166
    Top = 4
    Width = 86
    Height = 26
    PenColor = clWhite
    T1 = 0
    T2 = 0
    T3 = 0
    T4 = 0
    T5 = 0
    L1 = 0
    L2 = 0
    L3 = 0
    L4 = 0
    SustainPoint = msOff
  end
  object TextLabelMulti_EnvT1: TTextLabel
    Left = 24
    Top = 88
    Width = 9
    Height = 11
    Caption = 'T1'
  end
  object TextLabelMulti_EnvT2: TTextLabel
    Left = 59
    Top = 88
    Width = 11
    Height = 11
    Caption = 'T2'
  end
  object TextLabelMulti_EnvT3: TTextLabel
    Left = 94
    Top = 88
    Width = 11
    Height = 11
    Caption = 'T3'
  end
  object TextLabelMulti_EnvT4: TTextLabel
    Left = 129
    Top = 88
    Width = 11
    Height = 11
    Caption = 'T4'
  end
  object TextLabelMulti_EnvT5: TTextLabel
    Left = 163
    Top = 88
    Width = 11
    Height = 11
    Caption = 'T5'
  end
  object TextLabelMulti_EnvSustain: TTextLabel
    Left = 197
    Top = 31
    Width = 31
    Height = 11
    Caption = 'Sustain'
  end
  object SmallKnobMulti_EnvL1: TSmallKnob
    Left = 36
    Top = 33
    Value = 127
    Display = DisplayMulti_EnvL1
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayMulti_EnvL1: TDisplay
    Left = 25
    Top = 16
    Alignment = taCenter
    Caption = '127'
    TabOrder = 2
    DisplayFormats.Formats = [dfUnsigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobMulti_EnvL2: TSmallKnob
    Left = 71
    Top = 33
    Value = 45
    Display = DisplayMulti_EnvL2
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 45
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayMulti_EnvL2: TDisplay
    Left = 60
    Top = 16
    Alignment = taCenter
    Caption = '45'
    TabOrder = 4
    DisplayFormats.Formats = [dfUnsigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobMulti_EnvL3: TSmallKnob
    Left = 106
    Top = 33
    Value = 64
    Display = DisplayMulti_EnvL3
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayMulti_EnvL3: TDisplay
    Left = 95
    Top = 16
    Alignment = taCenter
    Caption = '64'
    TabOrder = 6
    DisplayFormats.Formats = [dfUnsigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobMulti_EnvL4: TSmallKnob
    Left = 141
    Top = 33
    Value = 0
    Display = DisplayMulti_EnvL4
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayMulti_EnvL4: TDisplay
    Left = 130
    Top = 16
    Alignment = taCenter
    Caption = '0'
    TabOrder = 11
    DisplayFormats.Formats = [dfUnsigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobMulti_EnvT1: TSmallKnob
    Left = 36
    Top = 77
    Value = 0
    Display = DisplayMulti_EnvT1
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobMulti_EnvT2: TSmallKnob
    Left = 71
    Top = 77
    Value = 30
    Display = DisplayMulti_EnvT2
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 30
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobMulti_EnvT3: TSmallKnob
    Left = 106
    Top = 77
    Value = 30
    Display = DisplayMulti_EnvT3
    DisplayFormat = dfUnsigned
    CtrlIndex = 6
    DefaultValue = 30
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobMulti_EnvT4: TSmallKnob
    Left = 141
    Top = 77
    Value = 30
    Display = DisplayMulti_EnvT4
    DisplayFormat = dfUnsigned
    CtrlIndex = 7
    DefaultValue = 30
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayMulti_EnvT1: TDisplay
    Left = 25
    Top = 60
    Alignment = taCenter
    Caption = '0.5m'
    TabOrder = 12
    DisplayFormats.Formats = [dfADSRTime]
    DisplayFormat = dfADSRTime
    Clickable = False
    CtrlIndex = -1
  end
  object DisplayMulti_EnvT2: TDisplay
    Left = 60
    Top = 60
    Alignment = taCenter
    Caption = '13m'
    TabOrder = 13
    DisplayFormats.Formats = [dfADSRTime]
    DisplayFormat = dfADSRTime
    Clickable = False
    CtrlIndex = -1
  end
  object DisplayMulti_EnvT3: TDisplay
    Left = 95
    Top = 60
    Alignment = taCenter
    Caption = '13m'
    TabOrder = 14
    DisplayFormats.Formats = [dfADSRTime]
    DisplayFormat = dfADSRTime
    Clickable = False
    CtrlIndex = -1
  end
  object DisplayMulti_EnvT4: TDisplay
    Left = 130
    Top = 60
    Alignment = taCenter
    Caption = '13m'
    TabOrder = 16
    DisplayFormats.Formats = [dfADSRTime]
    DisplayFormat = dfADSRTime
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobMulti_EnvT5: TSmallKnob
    Left = 175
    Top = 77
    Value = 0
    Display = DisplayMulti_EnvT5
    DisplayFormat = dfUnsigned
    CtrlIndex = 8
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayMulti_EnvT5: TDisplay
    Left = 164
    Top = 60
    Alignment = taCenter
    Caption = '0.5m'
    TabOrder = 18
    DisplayFormats.Formats = [dfADSRTime]
    DisplayFormat = dfADSRTime
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobMulti_EnvSustain: TSmallKnob
    Left = 230
    Top = 35
    Value = 3
    Display = DisplayMulti_EnvSustain
    DisplayFormat = dfUnsigned
    CtrlIndex = 9
    DefaultValue = 3
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 4
    StepSize = 1
  end
  object DisplayMulti_EnvSustain: TDisplay
    Left = 199
    Top = 43
    Width = 29
    Alignment = taCenter
    Caption = '3'
    TabOrder = 19
    DisplayFormats.Formats = [dfUnsigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object ButtonSetMulti_EnvShape: TButtonSet
    Left = 95
    Top = 1
    Width = 69
    Height = 14
    Value = 2
    DisplayFormat = dfMultiEnvShape
    CtrlIndex = 10
    DefaultValue = 2
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smRadio
    Clickers = <
      item
        FileName = '_slope_plus_min.bmp'
      end
      item
        FileName = '_slope_plus_exp.bmp'
      end
      item
        FileName = '_slope_plus_lin.bmp'
      end>
  end
end
object T_AHDEnv
  Hint = 'Version = 16'
  TitleLabel = EditLabelAHD_Env
  Title = 'AHD_Env'
  Description = 'AHD env. with modulation'
  ModuleAttributes = []
  ModuleType = 46
  Cycles = 2.468750000000000000
  ProgMem = 1.750000000000000000
  XMem = 1.312500000000000000
  YMem = 0.218750000000000000
  ZeroPage = 1.750000000000000000
  DynMem = 0.531250000000000000
  UnitHeight = 5
  object BoxAHD_EnvD: TBox
    Left = 159
    Top = 2
    Width = 38
    Height = 71
    Shape = bsFrame
  end
  object BoxAHD_EnvH: TBox
    Left = 122
    Top = 2
    Width = 38
    Height = 71
    Shape = bsFrame
  end
  object BoxAHD_EnvA: TBox
    Left = 85
    Top = 2
    Width = 38
    Height = 71
    Shape = bsFrame
  end
  object ImageAHD_EnvAmp: TGraphicImage
    Left = 210
    Top = 50
    Width = 32
    Height = 20
    FileName = '_amp.bmp'
  end
  object BoxAHD_EnvAHDGraph: TBox
    Left = 197
    Top = 3
    Width = 56
    Height = 27
  end
  object EditLabelAHD_Env: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputAHD_EnvEnv: TOutput
    Left = 240
    Top = 41
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object IndicatorAHD_EnvTrig: TIndicator
    Left = 7
    Top = 34
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object InputAHD_EnvAmp: TInput
    Left = 6
    Top = 59
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 5
  end
  object TextLabelAHD_EnvTrig: TTextLabel
    Left = 18
    Top = 40
    Width = 16
    Height = 11
    Caption = 'Trig'
  end
  object TextLabelAHD_EnvAmp: TTextLabel
    Left = 18
    Top = 58
    Width = 20
    Height = 11
    Caption = 'Amp'
  end
  object OutputAHD_EnvOut: TOutput
    Left = 240
    Top = 59
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object ImageAHD_EnvTrig: TGraphicImage
    Left = 2
    Top = 41
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object InputAHD_EnvTrig: TInput
    Left = 6
    Top = 41
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object TextLabelAHD_EnvA: TTextLabel
    Left = 89
    Top = 39
    Width = 7
    Height = 11
    Caption = 'A'
  end
  object TextLabelAHD_EnvH: TTextLabel
    Left = 126
    Top = 39
    Width = 7
    Height = 11
    Caption = 'H'
  end
  object TextLabelAHD_EnvD: TTextLabel
    Left = 163
    Top = 39
    Width = 7
    Height = 11
    Caption = 'D'
  end
  object InputAHD_EnvIn: TInput
    Left = 202
    Top = 59
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 4
  end
  object TextLabelAHD_EnvEnv: TTextLabel
    Left = 218
    Top = 40
    Width = 16
    Height = 11
    Caption = 'Env'
  end
  object InputAHD_EnvA: TInput
    Left = 88
    Top = 60
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 1
  end
  object InputAHD_EnvH: TInput
    Left = 125
    Top = 60
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 2
  end
  object InputAHD_EnvD: TInput
    Left = 162
    Top = 60
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 3
  end
  object AHDGraphAHD_Env: TAHDGraph
    Left = 199
    Top = 4
    Width = 54
    Height = 25
    PenColor = clWhite
    Symetrical = False
    Attack = 0
    Hold = 32
    Decay = 77
  end
  object SmallKnobAHD_EnvA: TSmallKnob
    Left = 99
    Top = 20
    Value = 0
    Display = DisplayAHD_EnvA
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayAHD_EnvA: TDisplay
    Left = 88
    Top = 4
    Alignment = taCenter
    Caption = '0.5m'
    TabOrder = 2
    DisplayFormats.Formats = [dfADSRTime]
    DisplayFormat = dfADSRTime
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobAHD_EnvH: TSmallKnob
    Left = 136
    Top = 20
    Value = 32
    Display = DisplayAHD_EnvH
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 32
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayAHD_EnvH: TDisplay
    Left = 125
    Top = 4
    Alignment = taCenter
    Caption = '15m'
    TabOrder = 4
    DisplayFormats.Formats = [dfADSRTime]
    DisplayFormat = dfADSRTime
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobAHD_EnvD: TSmallKnob
    Left = 173
    Top = 20
    Value = 77
    Display = DisplayAHD_EnvD
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 77
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayAHD_EnvD: TDisplay
    Left = 162
    Top = 4
    Alignment = taCenter
    Caption = '520m'
    TabOrder = 8
    DisplayFormats.Formats = [dfADSRTime]
    DisplayFormat = dfADSRTime
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobAHD_EnvAMod: TSmallKnob
    Left = 99
    Top = 45
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobAHD_EnvHMod: TSmallKnob
    Left = 136
    Top = 45
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobAHD_EnvDMod: TSmallKnob
    Left = 173
    Top = 45
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_ModEnv
  Hint = 'Version = 16'
  TitleLabel = EditLabelMod_Env
  Title = 'Mod_Env'
  Description = 'ADSR env. with modulation'
  ModuleAttributes = []
  ModuleType = 23
  Cycles = 3.343750000000000000
  ProgMem = 2.531250000000000000
  XMem = 2.593750000000000000
  YMem = 0.156250000000000000
  ZeroPage = 1.750000000000000000
  DynMem = 0.687500000000000000
  UnitHeight = 6
  object BoxMod_EnvA: TBox
    Left = 48
    Top = 16
    Width = 38
    Height = 71
    Shape = bsFrame
  end
  object BoxMod_EnvR: TBox
    Left = 159
    Top = 16
    Width = 38
    Height = 71
    Shape = bsFrame
  end
  object BoxMod_EnvS: TBox
    Left = 122
    Top = 16
    Width = 38
    Height = 71
    Shape = bsFrame
  end
  object BoxMod_EnvD: TBox
    Left = 85
    Top = 16
    Width = 38
    Height = 71
    Shape = bsFrame
  end
  object ImageMod_EnvAmp: TGraphicImage
    Left = 210
    Top = 65
    Width = 32
    Height = 20
    FileName = '_amp.bmp'
  end
  object BoxMod_EnvADSR_Graph: TBox
    Left = 197
    Top = 3
    Width = 56
    Height = 30
  end
  object EditLabelMod_Env: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputMod_EnvEnv: TOutput
    Left = 240
    Top = 56
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object IndicatorMod_EnvGate: TIndicator
    Left = 7
    Top = 30
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object TextLabelMod_EnvA: TTextLabel
    Left = 52
    Top = 53
    Width = 7
    Height = 11
    Caption = 'A'
  end
  object InputMod_EnvAmp: TInput
    Left = 6
    Top = 74
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 7
  end
  object TextLabelMod_EnvRetrig: TTextLabel
    Left = 18
    Top = 55
    Width = 25
    Height = 11
    Caption = 'Retrig'
  end
  object TextLabelMod_EnvAmp: TTextLabel
    Left = 18
    Top = 73
    Width = 20
    Height = 11
    Caption = 'Amp'
  end
  object OutputMod_EnvOut: TOutput
    Left = 240
    Top = 74
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object ImageMod_EnvRetrig: TGraphicImage
    Left = 2
    Top = 56
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object InputMod_EnvRetrig: TInput
    Left = 6
    Top = 56
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object ADSRGraphMod_Env: TADSRGraph
    Left = 198
    Top = 4
    Width = 54
    Height = 28
    PenColor = clWhite
    Symetrical = False
    Attack = 127
    Decay = 127
    Sustain = 17
    Release = 127
    AttackType = atLog
    Inverted = False
  end
  object TextLabelMod_EnvGate: TTextLabel
    Left = 18
    Top = 37
    Width = 20
    Height = 11
    Caption = 'Gate'
  end
  object InputMod_EnvGate: TInput
    Left = 6
    Top = 38
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object TextLabelMod_EnvD: TTextLabel
    Left = 89
    Top = 53
    Width = 7
    Height = 11
    Caption = 'D'
  end
  object TextLabelMod_EnvS: TTextLabel
    Left = 126
    Top = 53
    Width = 6
    Height = 11
    Caption = 'S'
  end
  object TextLabelMod_EnvR: TTextLabel
    Left = 163
    Top = 53
    Width = 7
    Height = 11
    Caption = 'R'
  end
  object InputMod_EnvIn: TInput
    Left = 202
    Top = 74
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 6
  end
  object TextLabelMod_EnvEnv: TTextLabel
    Left = 218
    Top = 55
    Width = 16
    Height = 11
    Caption = 'Env'
  end
  object InputMod_EnvA: TInput
    Left = 51
    Top = 74
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 2
  end
  object InputMod_EnvD: TInput
    Left = 88
    Top = 74
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 3
  end
  object InputMod_EnvS: TInput
    Left = 125
    Top = 74
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 4
  end
  object InputMod_EnvR: TInput
    Left = 162
    Top = 74
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 5
  end
  object SmallKnobMod_EnvA: TSmallKnob
    Left = 62
    Top = 35
    Value = 0
    Display = DisplayMod_EnvA
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayMod_EnvA: TDisplay
    Left = 51
    Top = 18
    Alignment = taCenter
    Caption = '0.5m'
    TabOrder = 2
    DisplayFormats.Formats = [dfADSRTime]
    DisplayFormat = dfADSRTime
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobMod_EnvD: TSmallKnob
    Left = 99
    Top = 35
    Value = 77
    Display = DisplayMod_EnvD
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 77
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayMod_EnvD: TDisplay
    Left = 88
    Top = 18
    Alignment = taCenter
    Caption = '520m'
    TabOrder = 4
    DisplayFormats.Formats = [dfADSRTime]
    DisplayFormat = dfADSRTime
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobMod_EnvS: TSmallKnob
    Left = 136
    Top = 35
    Value = 127
    Display = DisplayMod_EnvS
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayMod_EnvS: TDisplay
    Left = 125
    Top = 18
    Alignment = taCenter
    Caption = '127'
    TabOrder = 6
    DisplayFormats.Formats = [dfUnsigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobMod_EnvR: TSmallKnob
    Left = 173
    Top = 35
    Value = 0
    Display = DisplayMod_EnvR
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayMod_EnvR: TDisplay
    Left = 162
    Top = 18
    Alignment = taCenter
    Caption = '0.5m'
    TabOrder = 11
    DisplayFormats.Formats = [dfADSRTime]
    DisplayFormat = dfADSRTime
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobMod_EnvAMod: TSmallKnob
    Left = 62
    Top = 59
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobMod_EnvDMod: TSmallKnob
    Left = 99
    Top = 59
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobMod_EnvSMod: TSmallKnob
    Left = 136
    Top = 59
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 6
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobMod_EnvRMod: TSmallKnob
    Left = 173
    Top = 59
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 7
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetMod_EnvInvert: TButtonSet
    Left = 212
    Top = 38
    Width = 25
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 8
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        FileName = '_adsr_up_dn.bmp'
      end>
  end
end
object T_ADEnv
  Hint = 'Version = 16'
  TitleLabel = EditLabelAD_Env
  Title = 'AD_Env'
  Description = 'AD envelope'
  ModuleAttributes = []
  ModuleType = 84
  Cycles = 1.437500000000000000
  ProgMem = 0.875000000000000000
  XMem = 0.750000000000000000
  YMem = 0.218750000000000000
  ZeroPage = 1.750000000000000000
  DynMem = 0.375000000000000000
  UnitHeight = 3
  object ImageAD_EnvAmp: TGraphicImage
    Left = 210
    Top = 22
    Width = 32
    Height = 20
    FileName = '_amp.bmp'
  end
  object EditLabelAD_Env: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelAD_EnvD: TTextLabel
    Left = 163
    Top = 28
    Width = 7
    Height = 11
    Caption = 'D'
  end
  object TextLabelAD_EnvA: TTextLabel
    Left = 126
    Top = 28
    Width = 7
    Height = 11
    Caption = 'A'
  end
  object BoxAD_EnvAD_Graph: TBox
    Left = 197
    Top = 2
    Width = 38
    Height = 23
  end
  object ADGraphAD_Env: TADGraph
    Left = 198
    Top = 3
    Width = 36
    Height = 21
    PenColor = clWhite
    Symetrical = False
    Attack = 0
    Decay = 77
  end
  object OutputAD_EnvEnv: TOutput
    Left = 240
    Top = 13
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object OutputAD_EnvOut: TOutput
    Left = 240
    Top = 31
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object InputAD_EnvIn: TInput
    Left = 202
    Top = 31
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object ImageAD_EnvGate: TGraphicImage
    Left = 2
    Top = 31
    Width = 5
    Height = 5
    FileName = '_arrow_up.bmp'
  end
  object InputAD_EnvGate: TInput
    Left = 6
    Top = 31
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object IndicatorAD_EnvGate: TIndicator
    Left = 20
    Top = 33
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object TextLabelAD_EnvAmp: TTextLabel
    Left = 41
    Top = 17
    Width = 20
    Height = 11
    Caption = 'Amp'
  end
  object InputAD_EnvAmp: TInput
    Left = 46
    Top = 30
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 2
  end
  object SmallKnobAD_EnvA: TSmallKnob
    Left = 136
    Top = 18
    Value = 0
    Display = DisplayAD_EnvA
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayAD_EnvA: TDisplay
    Left = 125
    Top = 2
    Alignment = taCenter
    Caption = '0.5m'
    TabOrder = 2
    DisplayFormats.Formats = [dfADSRTime]
    DisplayFormat = dfADSRTime
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobAD_EnvD: TSmallKnob
    Left = 173
    Top = 18
    Value = 77
    Display = DisplayAD_EnvD
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 77
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayAD_EnvD: TDisplay
    Left = 162
    Top = 2
    Alignment = taCenter
    Caption = '520m'
    TabOrder = 3
    DisplayFormats.Formats = [dfADSRTime]
    DisplayFormat = dfADSRTime
    Clickable = False
    CtrlIndex = -1
  end
  object ButtonSetAD_EnvGate: TButtonSet
    Left = 4
    Top = 15
    Width = 29
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Gate'
      end>
  end
end
object T_ADSREnv
  Hint = 'Version = 16'
  TitleLabel = EditLabelADSR_Env
  Title = 'ADSR_Env'
  Description = 'ADSR envelope'
  ModuleAttributes = []
  ModuleType = 20
  Cycles = 1.625000000000000000
  ProgMem = 1.062500000000000000
  XMem = 1.031250000000000000
  YMem = 0.218750000000000000
  ZeroPage = 1.750000000000000000
  DynMem = 0.531250000000000000
  UnitHeight = 5
  object ImageADSR_EnvAmp: TGraphicImage
    Left = 210
    Top = 50
    Width = 32
    Height = 20
    FileName = '_amp.bmp'
  end
  object BoxADSR_EnvADSRGraph: TBox
    Left = 197
    Top = 3
    Width = 56
    Height = 27
  end
  object EditLabelADSR_Env: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputADSR_EnvEnv: TOutput
    Left = 240
    Top = 41
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object IndicatorADSR_EnvGate: TIndicator
    Left = 7
    Top = 15
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object TextLabelADSR_EnvA: TTextLabel
    Left = 52
    Top = 60
    Width = 7
    Height = 11
    Caption = 'A'
  end
  object InputADSR_EnvAmp: TInput
    Left = 6
    Top = 59
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 3
  end
  object TextLabelADSR_EnvRetrig: TTextLabel
    Left = 18
    Top = 40
    Width = 25
    Height = 11
    Caption = 'Retrig'
  end
  object TextLabelADSR_EnvAmp: TTextLabel
    Left = 18
    Top = 58
    Width = 20
    Height = 11
    Caption = 'Amp'
  end
  object OutputADSR_EnvOut: TOutput
    Left = 240
    Top = 59
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object ImageADSR_EnvRetrig: TGraphicImage
    Left = 2
    Top = 41
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object InputADSR_EnvRetrig: TInput
    Left = 6
    Top = 41
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 2
  end
  object ADSRGraphADSR_Env: TADSRGraph
    Left = 198
    Top = 4
    Width = 54
    Height = 25
    PenColor = clWhite
    Symetrical = False
    Attack = 0
    Decay = 77
    Sustain = 127
    Release = 0
    AttackType = atLog
    Inverted = False
  end
  object TextLabelADSR_EnvGate: TTextLabel
    Left = 18
    Top = 22
    Width = 20
    Height = 11
    Caption = 'Gate'
  end
  object InputADSR_EnvGate: TInput
    Left = 6
    Top = 23
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object TextLabelADSR_EnvD: TTextLabel
    Left = 89
    Top = 60
    Width = 7
    Height = 11
    Caption = 'D'
  end
  object TextLabelADSR_EnvS: TTextLabel
    Left = 126
    Top = 60
    Width = 6
    Height = 11
    Caption = 'S'
  end
  object TextLabelADSR_EnvR: TTextLabel
    Left = 163
    Top = 60
    Width = 7
    Height = 11
    Caption = 'R'
  end
  object InputADSR_EnvIn: TInput
    Left = 202
    Top = 59
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelADSR_EnvEnv: TTextLabel
    Left = 218
    Top = 40
    Width = 16
    Height = 11
    Caption = 'Env'
  end
  object SmallKnobADSR_EnvA: TSmallKnob
    Left = 62
    Top = 49
    Value = 0
    Display = DisplayADSR_EnvA
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayADSR_EnvA: TDisplay
    Left = 51
    Top = 32
    Alignment = taCenter
    Caption = '0.5m'
    TabOrder = 2
    DisplayFormats.Formats = [dfADSRTime]
    DisplayFormat = dfADSRTime
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobADSR_EnvD: TSmallKnob
    Left = 99
    Top = 49
    Value = 77
    Display = DisplayADSR_EnvD
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 77
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayADSR_EnvD: TDisplay
    Left = 88
    Top = 32
    Alignment = taCenter
    Caption = '520m'
    TabOrder = 4
    DisplayFormats.Formats = [dfADSRTime]
    DisplayFormat = dfADSRTime
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobADSR_EnvS: TSmallKnob
    Left = 136
    Top = 49
    Value = 127
    Display = DisplayADSR_EnvS
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 127
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayADSR_EnvS: TDisplay
    Left = 125
    Top = 32
    Alignment = taCenter
    Caption = '127'
    TabOrder = 6
    DisplayFormats.Formats = [dfUnsigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobADSR_EnvR: TSmallKnob
    Left = 173
    Top = 49
    Value = 0
    Display = DisplayADSR_EnvR
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayADSR_EnvR: TDisplay
    Left = 162
    Top = 32
    Alignment = taCenter
    Caption = '0.5m'
    TabOrder = 7
    DisplayFormats.Formats = [dfADSRTime]
    DisplayFormat = dfADSRTime
    Clickable = False
    CtrlIndex = -1
  end
  object ButtonSetADSR_EnvShape: TButtonSet
    Left = 51
    Top = 14
    Width = 75
    Height = 14
    Value = 0
    DisplayFormat = dfADSREnvShape
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smRadio
    Clickers = <
      item
        FileName = '_log.bmp'
      end
      item
        FileName = '_lin.bmp'
      end
      item
        FileName = '_exp.bmp'
      end>
  end
  object ButtonSetADSR_EnvInvert: TButtonSet
    Left = 169
    Top = 14
    Width = 25
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 5
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        FileName = '_adsr_up_dn.bmp'
      end>
  end
end
object T_PatternGen
  Hint = 'Version = 16'
  TitleLabel = EditLabelPatternGen
  Title = 'PatternGen'
  Description = 'Clocked pattern generator'
  ModuleAttributes = []
  ModuleType = 99
  Cycles = 2.343750000000000000
  ProgMem = 1.531250000000000000
  XMem = 0.500000000000000000
  YMem = 0.343750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.468750000000000000
  UnitHeight = 4
  object BoxPatternGenStep: TBox
    Left = 155
    Top = 3
    Width = 39
    Height = 53
    Shape = bsFrame
  end
  object BoxPatternGenHoriz: TBox
    Left = 71
    Top = 24
    Width = 56
    Height = 9
    Shape = bsTopLine
  end
  object BoxPatternGenVert: TBox
    Left = 70
    Top = 25
    Width = 11
    Height = 20
    Shape = bsLeftLine
  end
  object EditLabelPatternGen: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputPatternGenOut: TOutput
    Left = 240
    Top = 45
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object InputPatternGenPattern: TInput
    Left = 66
    Top = 38
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 2
  end
  object TextLabelPatternGenPattern: TTextLabel
    Left = 84
    Top = 5
    Width = 31
    Height = 11
    Caption = 'Pattern'
  end
  object TextLabelPatternGenDelta: TTextLabel
    Left = 197
    Top = 5
    Width = 22
    Height = 11
    Caption = 'Delta'
  end
  object TextLabelPatternGenBank: TTextLabel
    Left = 125
    Top = 5
    Width = 22
    Height = 11
    Caption = 'Bank'
  end
  object TextLabelPatternGenStep: TTextLabel
    Left = 164
    Top = 5
    Width = 19
    Height = 11
    Caption = 'Step'
  end
  object TextLabelPatternGenRst: TTextLabel
    Left = 27
    Top = 33
    Width = 15
    Height = 11
    Caption = 'Rst'
  end
  object ImagePatternGenRst: TGraphicImage
    Left = 27
    Top = 45
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object InputPatternGenRst: TInput
    Left = 31
    Top = 45
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object TextLabelPatternGenClk: TTextLabel
    Left = 4
    Top = 33
    Width = 14
    Height = 11
    Caption = 'Clk'
  end
  object ImagePatternGenClk: TGraphicImage
    Left = 4
    Top = 45
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object InputPatternGenClk: TInput
    Left = 8
    Top = 45
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object ImagePatternGenType: TGraphicImage
    Left = 231
    Top = 3
    Width = 19
    Height = 18
    Transparent = False
    FileName = 'PatternGen.bmp'
  end
  object TextLabelPatternGenOut: TTextLabel
    Left = 237
    Top = 32
    Width = 15
    Height = 11
    Caption = 'Out'
  end
  object DisplayPatternGenPattern: TDisplay
    Left = 83
    Top = 17
    Alignment = taCenter
    Caption = '0'
    TabOrder = 1
    DisplayFormats.Formats = [dfUnsigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobPatternGenPattern: TSmallKnob
    Left = 90
    Top = 33
    Value = 0
    Display = DisplayPatternGenPattern
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayPatternGenBank: TDisplay
    Left = 120
    Top = 17
    Alignment = taCenter
    Caption = '0'
    TabOrder = 3
    DisplayFormats.Formats = [dfUnsigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobPatternGenBank: TSmallKnob
    Left = 127
    Top = 33
    Value = 0
    Display = DisplayPatternGenBank
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayPatternGenStep: TDisplay
    Left = 158
    Top = 17
    Alignment = taCenter
    Caption = '15'
    TabOrder = 5
    DisplayFormats.Formats = [dfUnsigned]
    DisplayFormat = dfUnsigned
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobPatternGenStep: TSmallKnob
    Left = 165
    Top = 33
    Value = 15
    Display = DisplayPatternGenStep
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 15
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetPatternGenMono: TButtonSet
    Left = 196
    Top = 41
    Width = 33
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Mono'
      end>
  end
  object ButtonSetPatternGenLow: TButtonSet
    Left = 196
    Top = 17
    Width = 33
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Low'
      end>
  end
end
object T_RndPulsGen
  Hint = 'Version = 16'
  TitleLabel = EditLabelRndPulsGen
  Title = 'RndPulsGen'
  Description = 'Random pulse generator'
  ModuleAttributes = []
  ModuleType = 35
  Cycles = 0.343750000000000000
  ProgMem = 0.312500000000000000
  XMem = 0.218750000000000000
  YMem = 0.156250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object EditLabelRndPulsGen: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelRndPulsGenDensity: TTextLabel
    Left = 146
    Top = 8
    Width = 31
    Height = 11
    Caption = 'Density'
  end
  object OutputRndPulsGenOut: TOutput
    Left = 240
    Top = 13
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object ImageRndPulsGenType: TGraphicImage
    Left = 212
    Top = 5
    Width = 18
    Height = 19
    Transparent = False
    FileName = 'RndPulsGen.bmp'
  end
  object IndicatorRndPulsGenOut: TIndicator
    Left = 240
    Top = 6
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object SmallKnobRndPulsGenDensity: TSmallKnob
    Left = 182
    Top = 3
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_RandomGen
  Hint = 'Version = 16'
  TitleLabel = EditLabelRandomGen
  Title = 'RandomGen'
  Description = 'Random generator'
  ModuleAttributes = []
  ModuleType = 110
  Cycles = 1.437500000000000000
  ProgMem = 1.187500000000000000
  XMem = 0.500000000000000000
  YMem = 0.343750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object EditLabelRandomGen: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelRandomGenMst: TTextLabel
    Left = 89
    Top = 8
    Width = 17
    Height = 11
    Caption = 'Mst'
  end
  object OutputRandomGenOut: TOutput
    Left = 240
    Top = 13
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object ImageRandomGenType: TGraphicImage
    Left = 212
    Top = 5
    Width = 19
    Height = 19
    Transparent = False
    FileName = 'RandomGen.bmp'
  end
  object InputRandomGenMst: TInput
    Left = 110
    Top = 9
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 0
  end
  object IndicatorRandomGenOut: TIndicator
    Left = 240
    Top = 6
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object DisplayRandomGenRate: TDisplay
    Left = 128
    Top = 7
    Width = 49
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 1
    DisplayFormats.Formats = [dfLfoHz, dfPartials]
    DisplayFormat = dfPartials
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobRandomGenRate: TSmallKnob
    Left = 182
    Top = 3
    Value = 64
    Display = DisplayRandomGenRate
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_RndStepGen
  Hint = 'Version = 16'
  TitleLabel = EditLabelRndStepGen
  Title = 'RndStepGen'
  Description = 'Random step generator'
  ModuleAttributes = []
  ModuleType = 34
  Cycles = 0.625000000000000000
  ProgMem = 0.656250000000000000
  XMem = 0.218750000000000000
  YMem = 0.343750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object EditLabelRndStepGen: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelRndStepGenMst: TTextLabel
    Left = 89
    Top = 8
    Width = 17
    Height = 11
    Caption = 'Mst'
  end
  object OutputRndStepGenOut: TOutput
    Left = 240
    Top = 13
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object ImageRndStepGenType: TGraphicImage
    Left = 212
    Top = 5
    Width = 18
    Height = 18
    Transparent = False
    FileName = 'RndStepGen.bmp'
  end
  object InputRndStepGenMst: TInput
    Left = 110
    Top = 9
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 0
  end
  object IndicatorRndStepGenOut: TIndicator
    Left = 240
    Top = 6
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object DisplayRndStepGenRate: TDisplay
    Left = 128
    Top = 7
    Width = 49
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 1
    DisplayFormats.Formats = [dfLfoHz, dfPartials]
    DisplayFormat = dfPartials
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobRndStepGenRate: TSmallKnob
    Left = 182
    Top = 3
    Value = 64
    Display = DisplayRndStepGenRate
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_ClkRndGen
  Hint = 'Version = 16'
  TitleLabel = EditLabelRndGen
  Title = 'ClkRndGen'
  Description = 'Clocked random step generator'
  ModuleAttributes = []
  ModuleType = 33
  Cycles = 0.625000000000000000
  ProgMem = 0.625000000000000000
  XMem = 0.218750000000000000
  YMem = 0.218750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.343750000000000000
  UnitHeight = 2
  object EditLabelRndGen: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelClkRndGenClk: TTextLabel
    Left = 169
    Top = 9
    Width = 14
    Height = 11
    Caption = 'Clk'
  end
  object OutputClkRndGenOut: TOutput
    Left = 240
    Top = 10
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object ImageClkRndGenType: TGraphicImage
    Left = 212
    Top = 5
    Width = 18
    Height = 19
    Transparent = False
    FileName = 'ClkRndGen.bmp'
  end
  object InputClkRndGenClk: TInput
    Left = 191
    Top = 10
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object ImageClkRndGenClk: TGraphicImage
    Left = 187
    Top = 10
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object ButtonSetClkRndGenMono: TButtonSet
    Left = 86
    Top = 8
    Width = 33
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Mono'
      end>
  end
  object ButtonSetClkRndGenCol: TButtonSet
    Left = 126
    Top = 8
    Width = 33
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 1
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Col'
      end>
  end
end
object T_ClkGen
  Hint = 'Version = 16'
  TitleLabel = EditLabelClkGen
  Title = 'ClkGen'
  Description = 'Clock generator'
  ModuleAttributes = []
  ModuleType = 68
  Cycles = 1.000000000000000000
  ProgMem = 0.781250000000000000
  XMem = 0.437500000000000000
  YMem = 0.281250000000000000
  ZeroPage = 3.531250000000000000
  DynMem = 0.343750000000000000
  UnitHeight = 3
  object EditLabelClkGen: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelClkGenRate: TTextLabel
    Left = 83
    Top = 11
    Width = 20
    Height = 11
    Caption = 'Rate'
  end
  object OutputClkGen24: TOutput
    Left = 240
    Top = 3
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object OutputClkGen4: TOutput
    Left = 240
    Top = 17
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object OutputClkGenSync: TOutput
    Left = 240
    Top = 31
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 3
  end
  object TextLabelClkGen24Pulses: TTextLabel
    Left = 190
    Top = 2
    Width = 47
    Height = 11
    Caption = '24 pulses/b'
  end
  object TextLabelClkGen4Pulses: TTextLabel
    Left = 195
    Top = 16
    Width = 42
    Height = 11
    Caption = '4 pulses/b'
  end
  object TextLabelClkGenSync: TTextLabel
    Left = 217
    Top = 30
    Width = 20
    Height = 11
    Caption = 'Sync'
  end
  object TextLabelClkGenReset: TTextLabel
    Left = 20
    Top = 17
    Width = 25
    Height = 11
    Caption = 'Reset'
  end
  object OutputClkGenSlv: TOutput
    Left = 6
    Top = 31
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 2
  end
  object ImageClkGenReset: TGraphicImage
    Left = 2
    Top = 17
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object InputClkGenReset: TInput
    Left = 6
    Top = 17
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object TextLabelClkGenSlv: TTextLabel
    Left = 20
    Top = 30
    Width = 13
    Height = 11
    Caption = 'Slv'
  end
  object SmallKnobClkGenRate: TSmallKnob
    Left = 127
    Top = 19
    Value = 64
    Display = DisplayClkGenRate
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayClkGenRate: TDisplay
    Left = 74
    Top = 23
    Width = 40
    Alignment = taCenter
    Caption = '120'
    TabOrder = 1
    DisplayFormats.Formats = [dfBPM]
    DisplayFormat = dfBPM
    Clickable = False
    CtrlIndex = -1
  end
  object ButtonSetClkGenOn: TButtonSet
    Left = 156
    Top = 16
    Width = 29
    Height = 14
    Value = 1
    DisplayFormat = dfOnOff
    CtrlIndex = 1
    DefaultValue = 1
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'On'
      end>
  end
end
object T_LFOSlvE
  Hint = 'Version = 16'
  TitleLabel = EditLabelLFOSlvE
  Title = 'LFOSlvE'
  Description = 'LFO Slave E'
  ModuleAttributes = []
  ModuleType = 30
  Cycles = 0.343750000000000000
  ProgMem = 0.312500000000000000
  XMem = 0.156250000000000000
  YMem = 0.156250000000000000
  ZeroPage = 0.906250000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object EditLabelLFOSlvE: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelLFOSlvEMst: TTextLabel
    Left = 89
    Top = 8
    Width = 17
    Height = 11
    Caption = 'Mst'
  end
  object OutputLFOSlvEOut: TOutput
    Left = 240
    Top = 13
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object ImageLFOSlvEType: TGraphicImage
    Left = 212
    Top = 5
    Width = 19
    Height = 19
    Transparent = False
    FileName = 'LFOSlvE.bmp'
  end
  object InputLFOSlvEMst: TInput
    Left = 110
    Top = 9
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 0
  end
  object IndicatorLFOSlvEOut: TIndicator
    Left = 240
    Top = 6
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object DisplayLFOSlvERate: TDisplay
    Left = 128
    Top = 7
    Width = 49
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 1
    DisplayFormats.Formats = [dfLfoHz, dfPartials]
    DisplayFormat = dfPartials
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobLFOSlvERate: TSmallKnob
    Left = 182
    Top = 3
    Value = 64
    Display = DisplayLFOSlvERate
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_LFOSlvD
  Hint = 'Version = 16'
  TitleLabel = EditLabelLFOSlvD
  Title = 'LFOSlvD'
  Description = 'LFO Slave D'
  ModuleAttributes = []
  ModuleType = 29
  Cycles = 0.375000000000000000
  ProgMem = 0.343750000000000000
  XMem = 0.156250000000000000
  YMem = 0.156250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object EditLabelLFOSlvD: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelLFOSlvDMst: TTextLabel
    Left = 89
    Top = 8
    Width = 17
    Height = 11
    Caption = 'Mst'
  end
  object OutputLFOSlvDOut: TOutput
    Left = 240
    Top = 13
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object ImageLFOSlvDType: TGraphicImage
    Left = 212
    Top = 5
    Width = 19
    Height = 19
    Transparent = False
    FileName = 'LFOSlvD.bmp'
  end
  object InputLFOSlvDMst: TInput
    Left = 110
    Top = 9
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 0
  end
  object IndicatorLFOSlvDOut: TIndicator
    Left = 240
    Top = 6
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object DisplayLFOSlvDRate: TDisplay
    Left = 128
    Top = 7
    Width = 49
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 1
    DisplayFormats.Formats = [dfLfoHz, dfPartials]
    DisplayFormat = dfPartials
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobLFOSlvDRate: TSmallKnob
    Left = 182
    Top = 3
    Value = 64
    Display = DisplayLFOSlvDRate
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_LFOSlvC
  Hint = 'Version = 16'
  TitleLabel = EditLabelLFOSlvC
  Title = 'LFOSlvC'
  Description = 'LFO Slave C'
  ModuleAttributes = []
  ModuleType = 28
  Cycles = 0.781250000000000000
  ProgMem = 0.687500000000000000
  XMem = 0.343750000000000000
  YMem = 0.156250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object EditLabelLFOSlvC: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelLFOSlvCMst: TTextLabel
    Left = 89
    Top = 8
    Width = 17
    Height = 11
    Caption = 'Mst'
  end
  object OutputLFOSlvCOut: TOutput
    Left = 240
    Top = 13
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object ImageLFOSlvCType: TGraphicImage
    Left = 212
    Top = 5
    Width = 19
    Height = 19
    Transparent = False
    FileName = 'LFOSlvC.bmp'
  end
  object InputLFOSlvCMst: TInput
    Left = 110
    Top = 9
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 0
  end
  object IndicatorLFOSlvCOut: TIndicator
    Left = 240
    Top = 6
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object DisplayLFOSlvCRate: TDisplay
    Left = 128
    Top = 7
    Width = 49
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 1
    DisplayFormats.Formats = [dfLfoHz, dfPartials]
    DisplayFormat = dfPartials
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobLFOSlvCRate: TSmallKnob
    Left = 182
    Top = 3
    Value = 64
    Display = DisplayLFOSlvCRate
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_LFOSlvB
  Hint = 'Version = 16'
  TitleLabel = EditLabelLFOSlvB
  Title = 'LFOSlvB'
  Description = 'LFO Slave B'
  ModuleAttributes = []
  ModuleType = 27
  Cycles = 0.375000000000000000
  ProgMem = 0.343750000000000000
  XMem = 0.093750000000000000
  YMem = 0.156250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object EditLabelLFOSlvB: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelLFOSlvBMst: TTextLabel
    Left = 89
    Top = 8
    Width = 17
    Height = 11
    Caption = 'Mst'
  end
  object OutputLFOSlvBOut: TOutput
    Left = 240
    Top = 13
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object ImageLFOSlvBType: TGraphicImage
    Left = 212
    Top = 5
    Width = 19
    Height = 18
    Transparent = False
    FileName = 'LFOSlvB.bmp'
  end
  object InputLFOSlvBMst: TInput
    Left = 110
    Top = 9
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 0
  end
  object IndicatorLFOSlvBOut: TIndicator
    Left = 240
    Top = 6
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object DisplayLFOSlvBRate: TDisplay
    Left = 128
    Top = 7
    Width = 49
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 1
    DisplayFormats.Formats = [dfLfoHz, dfPartials]
    DisplayFormat = dfPartials
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobLFOSlvBRate: TSmallKnob
    Left = 182
    Top = 3
    Value = 64
    Display = DisplayLFOSlvBRate
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_LFOSlvA
  Hint = 'Version = 16'
  TitleLabel = EditLabelLFOSlvA
  Title = 'LFOSlvA'
  Description = 'LFO Slave A'
  ModuleAttributes = []
  ModuleType = 80
  Cycles = 1.218750000000000000
  ProgMem = 1.906250000000000000
  XMem = 0.437500000000000000
  YMem = 0.218750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.500000000000000000
  UnitHeight = 4
  object BoxLFOSlvARate: TBox
    Left = 22
    Top = 13
    Width = 87
    Height = 46
    Shape = bsFrame
  end
  object EditLabelLFOSlvA: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputLFOSlvAOut: TOutput
    Left = 240
    Top = 45
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object IndicatorLFOSlvAOut: TIndicator
    Left = 240
    Top = 39
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object TextLabelLfoSlvARate: TTextLabel
    Left = 80
    Top = 13
    Width = 20
    Height = 11
    Caption = 'Rate'
  end
  object TextLabelLfoSlvAMst: TTextLabel
    Left = 2
    Top = 11
    Width = 17
    Height = 11
    Caption = 'Mst'
  end
  object TextLabelLFOSlvARst: TTextLabel
    Left = 2
    Top = 33
    Width = 15
    Height = 11
    Caption = 'Rst'
  end
  object InputLFOSlvARst: TInput
    Left = 6
    Top = 45
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object ImageLFOSlvARst: TGraphicImage
    Left = 2
    Top = 45
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object BoxLFOSlvAGraph: TBox
    Left = 195
    Top = 5
    Width = 56
    Height = 27
  end
  object BoxLFOSlvAPhase: TBox
    Left = 129
    Top = 3
    Width = 62
    Height = 31
    Shape = bsFrame
  end
  object TextLabelLFOSlvAPhase: TTextLabel
    Left = 132
    Top = 4
    Width = 27
    Height = 11
    Caption = 'Phase'
  end
  object OscGraphLFOSlvA: TOscGraph
    Left = 196
    Top = 6
    Width = 54
    Height = 25
    PenColor = clWhite
    Cycles = 1.000000000000000000
  end
  object InputLFOSlvAMst: TInput
    Left = 6
    Top = 22
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 0
  end
  object KnobLFOSlvARate: TKnob
    Left = 75
    Top = 26
    Value = 64
    Display = DisplayLFOSlvARate
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayLFOSlvARate: TDisplay
    Left = 25
    Top = 17
    Width = 49
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 2
    DisplayFormats.Formats = [dfLfoHz, dfPartials]
    DisplayFormat = dfPartials
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobLFOSlvAPhase: TSmallKnob
    Left = 166
    Top = 8
    Value = 64
    Display = DisplayLFOSlvAPhase
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayLFOSlvAPhase: TDisplay
    Left = 132
    Top = 15
    Width = 32
    Alignment = taCenter
    Caption = '0'
    TabOrder = 3
    DisplayFormats.Formats = [dfPhase]
    DisplayFormat = dfPhase
    Clickable = False
    CtrlIndex = -1
  end
  object ButtonSetLFOSlvAWave: TButtonSet
    Left = 119
    Top = 43
    Width = 95
    Height = 14
    Value = 0
    DisplayFormat = dfLFOWave
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smRadio
    Clickers = <
      item
        FileName = '_sine.bmp'
      end
      item
        FileName = '_tri.bmp'
      end
      item
        FileName = '_saw.bmp'
      end
      item
        FileName = '_saw_dn.bmp'
      end
      item
        FileName = '_square.bmp'
      end>
  end
  object ButtonSetLFOSlvAMute: TButtonSet
    Left = 219
    Top = 43
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
  object ButtonSetLFOSlvAMono: TButtonSet
    Left = 33
    Top = 39
    Width = 33
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Mono'
      end>
  end
end
object T_LFOC
  Hint = 'Version = 16'
  TitleLabel = EditLabelLFOC
  Title = 'LFOC'
  Description = 'LFO C'
  ModuleAttributes = []
  ModuleType = 26
  Cycles = 1.468750000000000000
  ProgMem = 1.062500000000000000
  XMem = 0.500000000000000000
  YMem = 0.156250000000000000
  ZeroPage = 1.750000000000000000
  DynMem = 0.500000000000000000
  UnitHeight = 4
  object ImageLFOCLineRate: TGraphicImage
    Left = 60
    Top = 40
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelLFOC: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputLFOCOut: TOutput
    Left = 240
    Top = 45
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object IndicatorLFOCOut: TIndicator
    Left = 240
    Top = 39
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object InputLFOCRate: TInput
    Left = 55
    Top = 40
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object TextLabelLFOCRate: TTextLabel
    Left = 33
    Top = 39
    Width = 20
    Height = 11
    Caption = 'Rate'
  end
  object OutputLFOCSlv: TOutput
    Left = 6
    Top = 45
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 1
  end
  object TextLabelLFOCSlv: TTextLabel
    Left = 3
    Top = 31
    Width = 13
    Height = 11
    Caption = 'Slv'
  end
  object KnobLFOCRate: TKnob
    Left = 94
    Top = 27
    Value = 64
    Display = DisplayLFOCRate
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayLFOCRate: TDisplay
    Left = 42
    Top = 17
    Width = 49
    Alignment = taCenter
    Caption = '0.64 Hz'
    TabOrder = 2
    DisplayFormats.Formats = [dfLfoHz]
    DisplayFormat = dfLfoHz
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobLFOCRateMod: TSmallKnob
    Left = 70
    Top = 33
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetLFOCWave: TButtonSet
    Left = 157
    Top = 3
    Width = 95
    Height = 14
    Value = 0
    DisplayFormat = dfLFOWave
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smRadio
    Clickers = <
      item
        FileName = '_sine.bmp'
      end
      item
        FileName = '_tri.bmp'
      end
      item
        FileName = '_saw.bmp'
      end
      item
        FileName = '_saw_dn.bmp'
      end
      item
        FileName = '_square.bmp'
      end>
  end
  object ButtonSetLFOCRange: TButtonSet
    Left = 129
    Top = 12
    Width = 23
    Height = 42
    Value = 1
    DisplayFormat = dfLFORange
    CtrlIndex = 1
    DefaultValue = 1
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soVertical
    Direction = diReversed
    SelectMode = smRadio
    Clickers = <
      item
        Caption = 'Sub'
      end
      item
        Caption = 'Lo'
      end
      item
        Caption = 'Hi'
      end>
  end
  object ButtonSetLFOCMute: TButtonSet
    Left = 219
    Top = 42
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 5
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
  object ButtonSetLFOCMono: TButtonSet
    Left = 92
    Top = 4
    Width = 33
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Mono'
      end>
  end
end
object T_LFOB
  Hint = 'Version = 16'
  TitleLabel = EditLabelLFOB
  Title = 'LFOB'
  Description = 'LFO B'
  ModuleAttributes = []
  ModuleType = 25
  Cycles = 1.187500000000000000
  ProgMem = 1.062500000000000000
  XMem = 0.500000000000000000
  YMem = 0.281250000000000000
  ZeroPage = 1.750000000000000000
  DynMem = 0.625000000000000000
  UnitHeight = 5
  object BoxLFOBPW: TBox
    Left = 158
    Top = 35
    Width = 73
    Height = 37
    Shape = bsFrame
  end
  object BoxLFOBRate: TBox
    Left = 21
    Top = 20
    Width = 98
    Height = 52
    Shape = bsFrame
  end
  object ImageLFOBLineRate: TGraphicImage
    Left = 28
    Top = 52
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object ImageLFOBLinePW: TGraphicImage
    Left = 168
    Top = 51
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object BoxLFOBGraph: TBox
    Left = 195
    Top = 5
    Width = 56
    Height = 27
  end
  object BoxLFOBPhase: TBox
    Left = 129
    Top = 3
    Width = 62
    Height = 31
    Shape = bsFrame
  end
  object EditLabelLFOB: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputLFOBOut: TOutput
    Left = 240
    Top = 60
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object IndicatorLFOBOut: TIndicator
    Left = 240
    Top = 54
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object TextLabelLFOBPhase: TTextLabel
    Left = 132
    Top = 4
    Width = 27
    Height = 11
    Caption = 'Phase'
  end
  object InputLFOBRate: TInput
    Left = 23
    Top = 52
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object TextLabelLFOBRst: TTextLabel
    Left = 2
    Top = 20
    Width = 15
    Height = 11
    Caption = 'Rst'
  end
  object SetterLFOBKBT: TSetter
    Left = 135
    Top = 42
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelLFOBRate: TTextLabel
    Left = 23
    Top = 36
    Width = 20
    Height = 11
    Caption = 'Rate'
  end
  object TextLabelLFOBKBT: TTextLabel
    Left = 130
    Top = 32
    Width = 19
    Height = 11
    Caption = 'KBT'
  end
  object OutputLFOBSlv: TOutput
    Left = 6
    Top = 60
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 1
  end
  object ImageLFOBRst: TGraphicImage
    Left = 2
    Top = 32
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object InputLFOBRst: TInput
    Left = 6
    Top = 32
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object TextLabelLFOBSlv: TTextLabel
    Left = 3
    Top = 46
    Width = 13
    Height = 11
    Caption = 'Slv'
  end
  object OscGraphLFOB: TOscGraph
    Left = 196
    Top = 6
    Width = 54
    Height = 25
    PenColor = clWhite
    GraphType = otSquare
    Cycles = 1.000000000000000000
  end
  object SetterLFOBPW: TSetter
    Left = 210
    Top = 38
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object InputLFOBPW: TInput
    Left = 163
    Top = 51
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 2
  end
  object TextLabelLFOBPW: TTextLabel
    Left = 162
    Top = 38
    Width = 15
    Height = 11
    Caption = 'PW'
  end
  object KnobLFOBRate: TKnob
    Left = 61
    Top = 40
    Value = 64
    Display = DisplayLFOBRate
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobLFOBKBT: TSmallKnob
    Left = 129
    Top = 49
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterLFOBKBT
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayLFOBRate: TDisplay
    Left = 41
    Top = 23
    Width = 49
    Alignment = taCenter
    Caption = '0.64 Hz'
    TabOrder = 3
    DisplayFormats.Formats = [dfLfoHz]
    DisplayFormat = dfLfoHz
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobLFOBPhase: TSmallKnob
    Left = 166
    Top = 8
    Value = 64
    Display = DisplayLFOBPhase
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayLFOBPhase: TDisplay
    Left = 132
    Top = 15
    Width = 32
    Alignment = taCenter
    Caption = '0'
    TabOrder = 7
    DisplayFormats.Formats = [dfPhase]
    DisplayFormat = dfPhase
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobLFOBRateMod: TSmallKnob
    Left = 38
    Top = 46
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterLFOBKBT
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobLFOBPWMod: TSmallKnob
    Left = 180
    Top = 45
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 6
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterLFOBKBT
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobLFOBPW: TSmallKnob
    Left = 204
    Top = 45
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 7
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterLFOBPW
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetLFOBRange: TButtonSet
    Left = 92
    Top = 25
    Width = 23
    Height = 42
    Value = 1
    DisplayFormat = dfLFORange
    CtrlIndex = 1
    DefaultValue = 1
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soVertical
    Direction = diReversed
    SelectMode = smRadio
    Clickers = <
      item
        Caption = 'Sub'
      end
      item
        Caption = 'Lo'
      end
      item
        Caption = 'Hi'
      end>
  end
  object ButtonSetLFOBMono: TButtonSet
    Left = 92
    Top = 4
    Width = 33
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Mono'
      end>
  end
end
object T_LFOA
  Hint = 'Version = 16'
  TitleLabel = EditLabelLFOA
  Title = 'LFOA'
  Description = 'LFO A'
  ModuleAttributes = []
  ModuleType = 24
  Cycles = 1.781250000000000000
  ProgMem = 2.375000000000000000
  XMem = 0.562500000000000000
  YMem = 0.218750000000000000
  ZeroPage = 1.750000000000000000
  DynMem = 0.625000000000000000
  UnitHeight = 5
  object BoxLFOARate: TBox
    Left = 21
    Top = 20
    Width = 98
    Height = 52
    Shape = bsFrame
  end
  object ImageLFOALineRate: TGraphicImage
    Left = 27
    Top = 53
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object BoxLFOAGraph: TBox
    Left = 195
    Top = 5
    Width = 56
    Height = 27
  end
  object BoxLFOAPhase: TBox
    Left = 129
    Top = 3
    Width = 62
    Height = 31
    Shape = bsFrame
  end
  object EditLabelLFOA: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object OutputLFOAOut: TOutput
    Left = 240
    Top = 60
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 1
  end
  object IndicatorLFOAOut: TIndicator
    Left = 240
    Top = 54
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object TextLabelLFOAPhase: TTextLabel
    Left = 132
    Top = 4
    Width = 27
    Height = 11
    Caption = 'Phase'
  end
  object InputLFOARate: TInput
    Left = 23
    Top = 52
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object TextLabelLFOARst: TTextLabel
    Left = 2
    Top = 20
    Width = 15
    Height = 11
    Caption = 'Rst'
  end
  object SetterLFOAKBT: TSetter
    Left = 135
    Top = 42
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelLFOARate: TTextLabel
    Left = 23
    Top = 36
    Width = 20
    Height = 11
    Caption = 'Rate'
  end
  object TextLabelLFOAKBT: TTextLabel
    Left = 130
    Top = 32
    Width = 19
    Height = 11
    Caption = 'KBT'
  end
  object OutputLFOASlv: TOutput
    Left = 6
    Top = 60
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 0
  end
  object ImageLFOARst: TGraphicImage
    Left = 2
    Top = 32
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object InputLFOARst: TInput
    Left = 6
    Top = 32
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object TextLabelLFOASlv: TTextLabel
    Left = 3
    Top = 46
    Width = 13
    Height = 11
    Caption = 'Slv'
  end
  object OscGraphLFOA: TOscGraph
    Left = 196
    Top = 6
    Width = 54
    Height = 25
    PenColor = clWhite
    Cycles = 1.000000000000000000
  end
  object KnobLFOARate: TKnob
    Left = 61
    Top = 40
    Value = 64
    Display = DisplayLFOARate
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobLFOAKBT: TSmallKnob
    Left = 129
    Top = 49
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterLFOAKBT
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayLFOARate: TDisplay
    Left = 41
    Top = 23
    Width = 49
    Alignment = taCenter
    Caption = '0.64 Hz'
    TabOrder = 3
    DisplayFormats.Formats = [dfLfoHz]
    DisplayFormat = dfLfoHz
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobLFOAPhase: TSmallKnob
    Left = 166
    Top = 8
    Value = 64
    Display = DisplayLFOAPhase
    DisplayFormat = dfUnsigned
    CtrlIndex = 6
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayLFOAPhase: TDisplay
    Left = 132
    Top = 15
    Width = 32
    Alignment = taCenter
    Caption = '0'
    TabOrder = 5
    DisplayFormats.Formats = [dfPhase]
    DisplayFormat = dfPhase
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobLFOARateMod: TSmallKnob
    Left = 38
    Top = 46
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterLFOAKBT
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetLFOAWave: TButtonSet
    Left = 156
    Top = 38
    Width = 95
    Height = 14
    Value = 0
    DisplayFormat = dfLFOWave
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smRadio
    Clickers = <
      item
        FileName = '_sine.bmp'
      end
      item
        FileName = '_tri.bmp'
      end
      item
        FileName = '_saw.bmp'
      end
      item
        FileName = '_saw_dn.bmp'
      end
      item
        FileName = '_square.bmp'
      end>
  end
  object ButtonSetLFOARange: TButtonSet
    Left = 92
    Top = 25
    Width = 23
    Height = 42
    Value = 1
    DisplayFormat = dfLFORange
    CtrlIndex = 1
    DefaultValue = 1
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soVertical
    Direction = diReversed
    SelectMode = smRadio
    Clickers = <
      item
        Caption = 'Sub'
      end
      item
        Caption = 'Lo'
      end
      item
        Caption = 'Hi'
      end>
  end
  object ButtonSetLFOAMute: TButtonSet
    Left = 219
    Top = 58
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 7
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
  object ButtonSetLFOAMono: TButtonSet
    Left = 92
    Top = 4
    Width = 33
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Mono'
      end>
  end
end
object T_DrumSynth
  Hint = 'Version = 16'
  TitleLabel = EditLabelDrumSynt
  Title = 'DrumSynth'
  Description = 'Drumsound synthesizer'
  ModuleAttributes = []
  ModuleType = 58
  Cycles = 12.218750000000000000
  ProgMem = 4.718750000000000000
  XMem = 2.187500000000000000
  YMem = 1.812500000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 1.031250000000000000
  UnitHeight = 9
  object BoxDrumSynthClickNoise: TBox
    Left = 188
    Top = 55
    Width = 65
    Height = 49
    Shape = bsFrame
  end
  object BoxDrumSynthBend: TBox
    Left = 118
    Top = 55
    Width = 68
    Height = 49
    Shape = bsFrame
  end
  object BoxDrumSynthNoise: TBox
    Left = 118
    Top = 2
    Width = 135
    Height = 52
    Shape = bsFrame
  end
  object EditLabelDrumSynt: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object BoxDrumSynthPitch: TBox
    Left = 25
    Top = 17
    Width = 92
    Height = 116
    Shape = bsFrame
  end
  object InputDrumSynthPitch: TInput
    Left = 8
    Top = 119
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 2
  end
  object TextLabelDrumSynthPitch: TTextLabel
    Left = 2
    Top = 108
    Width = 22
    Height = 11
    Caption = 'Pitch'
  end
  object TextLabelDrumSynthNoiseDcy: TTextLabel
    Left = 199
    Top = 13
    Width = 16
    Height = 11
    Caption = 'Dcy'
  end
  object OutputDrumSynthOut: TOutput
    Left = 240
    Top = 120
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelDrumSynthNoiseFilter: TTextLabel
    Left = 148
    Top = 3
    Width = 48
    Height = 11
    Caption = 'Noise Filter'
  end
  object TextLabelDrumSynthNoise: TTextLabel
    Left = 221
    Top = 65
    Width = 25
    Height = 11
    Caption = 'Noise'
  end
  object TextLabelDrumSynthClick: TTextLabel
    Left = 197
    Top = 65
    Width = 21
    Height = 11
    Caption = 'Click'
  end
  object InputDrumSynthVel: TInput
    Left = 8
    Top = 85
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 1
  end
  object TextLabelDrumSynthVel: TTextLabel
    Left = 6
    Top = 73
    Width = 13
    Height = 11
    Caption = 'Vel'
  end
  object TextLabelDrumSynthDcy: TTextLabel
    Left = 62
    Top = 83
    Width = 16
    Height = 11
    Caption = 'Dcy'
  end
  object TextLabelDrumSynthFreq: TTextLabel
    Left = 127
    Top = 13
    Width = 19
    Height = 11
    Caption = 'Freq'
  end
  object InputDrumSynthTrig: TInput
    Left = 8
    Top = 52
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object TextLabelDrumSynthTrig: TTextLabel
    Left = 5
    Top = 41
    Width = 16
    Height = 11
    Caption = 'Trig'
  end
  object TextLabelDrumSynthRes: TTextLabel
    Left = 152
    Top = 13
    Width = 17
    Height = 11
    Caption = 'Res'
  end
  object TextLabelDrumSynthSwp: TTextLabel
    Left = 175
    Top = 13
    Width = 17
    Height = 11
    Caption = 'Swp'
  end
  object TextLabelDrumSynthOsc: TTextLabel
    Left = 60
    Top = 19
    Width = 20
    Height = 11
    Caption = 'OSC'
  end
  object TextLabelDrumSynthMaster: TTextLabel
    Left = 35
    Top = 27
    Width = 30
    Height = 11
    Caption = 'Master'
  end
  object TextLabelDrumSynthBend: TTextLabel
    Left = 142
    Top = 56
    Width = 22
    Height = 11
    Caption = 'Bend'
  end
  object TextLabelDrumSynthSlave: TTextLabel
    Left = 81
    Top = 27
    Width = 23
    Height = 11
    Caption = 'Slave'
  end
  object TextLabelDrumSynthTune: TTextLabel
    Left = 60
    Top = 59
    Width = 21
    Height = 11
    Caption = 'Tune'
  end
  object TextLabelDrumSynthBendAmt: TTextLabel
    Left = 130
    Top = 65
    Width = 18
    Height = 11
    Caption = 'Amt'
  end
  object TextLabelDrumSynthLvl: TTextLabel
    Left = 63
    Top = 108
    Width = 15
    Height = 11
    Caption = 'Lev'
  end
  object TextLabelDrumSynthBendDcy: TTextLabel
    Left = 158
    Top = 65
    Width = 16
    Height = 11
    Caption = 'Dcy'
  end
  object IndicatorDrumSynthTrig: TIndicator
    Left = 8
    Top = 65
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object TextLabelDrumSynthPreset: TTextLabel
    Left = 122
    Top = 104
    Width = 28
    Height = 11
    Caption = 'Preset'
  end
  object ImageDrumSynthOscType: TGraphicImage
    Left = 4
    Top = 18
    Width = 20
    Height = 19
    Transparent = False
    FileName = 'DrumSynth.bmp'
  end
  object SmallKnobDrumSynthMasterTune: TSmallKnob
    Left = 34
    Top = 52
    Value = 0
    Display = DisplayDrumSynthMaster
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobDrumSynthMasterDecay: TSmallKnob
    Left = 34
    Top = 77
    Value = 25
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 25
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobDrumSynthClick: TSmallKnob
    Left = 197
    Top = 77
    Value = 25
    DisplayFormat = dfUnsigned
    CtrlIndex = 13
    DefaultValue = 25
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayDrumSynthMaster: TDisplay
    Left = 29
    Top = 37
    Width = 41
    Alignment = taCenter
    Caption = '20.0 Hz'
    TabOrder = 6
    DisplayFormats.Formats = [dfDrumHz]
    DisplayFormat = dfDrumHz
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobDrumSynthNoiseRes: TSmallKnob
    Left = 150
    Top = 25
    Value = 25
    DisplayFormat = dfUnsigned
    CtrlIndex = 7
    DefaultValue = 25
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobDrumSynthNoiseSwp: TSmallKnob
    Left = 173
    Top = 25
    Value = 25
    DisplayFormat = dfUnsigned
    CtrlIndex = 8
    DefaultValue = 25
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobDrumSynthMasterLevel: TSmallKnob
    Left = 34
    Top = 102
    Value = 25
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 25
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayDrumSynthSlave: TDisplay
    Left = 72
    Top = 37
    Width = 41
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 10
    DisplayFormats.Formats = [dfDrumPartials]
    DisplayFormat = dfDrumPartials
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobDrumSynthSlaveTune: TSmallKnob
    Left = 87
    Top = 52
    Value = 0
    Display = DisplayDrumSynthSlave
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobDrumSynthSlaveDecay: TSmallKnob
    Left = 87
    Top = 77
    Value = 25
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 25
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobDrumSynthSlaveLevel: TSmallKnob
    Left = 87
    Top = 102
    Value = 25
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 25
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayDrumSynthPreset: TDisplay
    Left = 120
    Top = 116
    Width = 57
    Alignment = taCenter
    Caption = 'Kick 1'
    TabOrder = 16
    DisplayFormats.Formats = [dfDrumPreset]
    DisplayFormat = dfDrumPreset
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobDrumSynthNoiseFreq: TSmallKnob
    Left = 127
    Top = 25
    Value = 25
    DisplayFormat = dfUnsigned
    CtrlIndex = 6
    DefaultValue = 25
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobDrumSynthNoiseDcy: TSmallKnob
    Left = 196
    Top = 25
    Value = 25
    DisplayFormat = dfUnsigned
    CtrlIndex = 9
    DefaultValue = 25
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobDrumSynthBendDecay: TSmallKnob
    Left = 156
    Top = 77
    Value = 25
    DisplayFormat = dfUnsigned
    CtrlIndex = 12
    DefaultValue = 25
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobDrumSynthBendAmount: TSmallKnob
    Left = 129
    Top = 77
    Value = 25
    DisplayFormat = dfUnsigned
    CtrlIndex = 11
    DefaultValue = 25
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobDrumSynthNoise: TSmallKnob
    Left = 223
    Top = 77
    Value = 25
    DisplayFormat = dfUnsigned
    CtrlIndex = 14
    DefaultValue = 25
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerDrumSynthPreset: TSpinner
    Left = 178
    Top = 117
    Width = 20
    Height = 12
    Value = 1
    Display = DisplayDrumSynthPreset
    DisplayFormat = dfUnsigned
    CtrlIndex = -1
    DefaultValue = 1
    Morphable = False
    Knobbable = False
    Controllable = False
    Orientation = soHorizontal
    MinValue = 1
    MaxValue = 30
    StepSize = 1
  end
  object ButtonSetDrumSynthFilterType: TButtonSet
    Left = 222
    Top = 7
    Width = 25
    Height = 42
    Value = 0
    DisplayFormat = dfFilterType
    CtrlIndex = 10
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soVertical
    Direction = diReversed
    SelectMode = smRadio
    Clickers = <
      item
        Caption = 'LP'
      end
      item
        Caption = 'BP'
      end
      item
        Caption = 'HP'
      end>
  end
  object ButtonSetDrumSynthMute: TButtonSet
    Left = 219
    Top = 117
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 15
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
end
object T_PercOsc
  Hint = 'Version = 16'
  TitleLabel = EditLabelPercOsc
  Title = 'PercOsc'
  Description = 'Percussion OSC'
  ModuleAttributes = []
  ModuleType = 95
  Cycles = 4.875000000000000000
  ProgMem = 1.531250000000000000
  XMem = 0.500000000000000000
  YMem = 0.500000000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.593750000000000000
  UnitHeight = 4
  object BoxPercOscPitch: TBox
    Left = 95
    Top = 3
    Width = 121
    Height = 52
    Shape = bsFrame
  end
  object ImagePercOscLinePitch: TGraphicImage
    Left = 110
    Top = 35
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelPercOsc: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object SetterPercOscFine: TSetter
    Left = 196
    Top = 19
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelPercOscPitch: TTextLabel
    Left = 160
    Top = 9
    Width = 22
    Height = 11
    Caption = 'Pitch'
  end
  object TextLabelPercOscFine: TTextLabel
    Left = 192
    Top = 9
    Width = 18
    Height = 11
    Caption = 'Fine'
  end
  object TextLabelPercOscClick: TTextLabel
    Left = 42
    Top = 13
    Width = 21
    Height = 11
    Caption = 'Click'
  end
  object OutputPercOscOut: TOutput
    Left = 240
    Top = 44
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputPercOscFreq: TInput
    Left = 106
    Top = 35
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 2
  end
  object InputPercOscAmp: TInput
    Left = 11
    Top = 26
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabelPercOscAmp: TTextLabel
    Left = 6
    Top = 13
    Width = 20
    Height = 11
    Caption = 'Amp'
  end
  object ImagePercOscTrig: TGraphicImage
    Left = 6
    Top = 43
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object InputPercOscTrig: TInput
    Left = 11
    Top = 43
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelPercOscTrig: TTextLabel
    Left = 24
    Top = 42
    Width = 16
    Height = 11
    Caption = 'Trig'
  end
  object ImagePercOscOscType: TGraphicImage
    Left = 231
    Top = 4
    Width = 20
    Height = 19
    Transparent = False
    FileName = 'PercOsc.bmp'
  end
  object TextLabelPercOsecDecay: TTextLabel
    Left = 67
    Top = 13
    Width = 26
    Height = 11
    Caption = 'Decay'
  end
  object KnobPercOscPitchCoarse: TKnob
    Left = 157
    Top = 21
    Value = 64
    Display = DisplayPercOscFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobPercOscPitchFine: TSmallKnob
    Left = 190
    Top = 27
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterPercOscFine
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayPercOscFreq: TDisplay
    Left = 99
    Top = 13
    Width = 56
    Alignment = taCenter
    Caption = '330 Hz'
    TabOrder = 5
    DisplayFormats.Formats = [dfNote, dfOscHz]
    DisplayFormat = dfOscHz
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobPercOscClick: TSmallKnob
    Left = 43
    Top = 25
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobPercOscDecay: TSmallKnob
    Left = 69
    Top = 25
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobPercOscFreqMod: TSmallKnob
    Left = 124
    Top = 29
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetPercOscMute: TButtonSet
    Left = 219
    Top = 42
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 6
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
  object ButtonSetPercOscPunch: TButtonSet
    Left = 218
    Top = 25
    Width = 34
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'Punch'
      end>
  end
end
object T_Noise
  Hint = 'Version = 16'
  TitleLabel = EditLabelNoise
  Title = 'Noise'
  Description = 'Noise generator'
  ModuleAttributes = []
  ModuleType = 31
  Cycles = 1.656250000000000000
  ProgMem = 0.406250000000000000
  XMem = 0.281250000000000000
  YMem = 0.156250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object EditLabelNoise: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelNoiseWhite: TTextLabel
    Left = 84
    Top = 17
    Width = 23
    Height = 11
    Caption = 'White'
  end
  object TextLabelNoiseColored: TTextLabel
    Left = 135
    Top = 17
    Width = 34
    Height = 11
    Caption = 'Colored'
  end
  object OutputNoiseOut: TOutput
    Left = 240
    Top = 10
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object ImageNoiceOscType: TGraphicImage
    Left = 212
    Top = 5
    Width = 20
    Height = 19
    Transparent = False
    FileName = 'Noise.bmp'
  end
  object SmallKnobNoiseColor: TSmallKnob
    Left = 112
    Top = 4
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_OscSlvFM
  Hint = 'Version = 16'
  TitleLabel = EditLabelOscSlvFM
  Title = 'OscSlvFM'
  Description = 'Osc Slave FM'
  ModuleAttributes = []
  ModuleType = 85
  Cycles = 3.187500000000000000
  ProgMem = 0.718750000000000000
  XMem = 0.437500000000000000
  YMem = 0.281250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.500000000000000000
  UnitHeight = 3
  object ImageOscSlvFMLineFMB: TGraphicImage
    Left = 187
    Top = 13
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelOscSlvFM: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object SetterOscSlvFMFine: TSetter
    Left = 126
    Top = 12
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelOscSlvFMDetune: TTextLabel
    Left = 88
    Top = 1
    Width = 30
    Height = 11
    Caption = 'Detune'
  end
  object TextLabelOscSlvFMFine: TTextLabel
    Left = 122
    Top = 1
    Width = 18
    Height = 11
    Caption = 'Fine'
  end
  object TextLabelOscSlvFMMst: TTextLabel
    Left = 4
    Top = 17
    Width = 17
    Height = 11
    Caption = 'Mst'
  end
  object OutputOscSlvFMOut: TOutput
    Left = 240
    Top = 29
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputOscSlvFMFMB: TInput
    Left = 183
    Top = 13
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabelOscSlvFMB: TTextLabel
    Left = 176
    Top = 2
    Width = 22
    Height = 11
    Caption = 'FMB'
  end
  object TextLabelOscSlvFMPartials: TTextLabel
    Left = 27
    Top = 28
    Width = 32
    Height = 11
    Caption = 'Partials'
  end
  object InputOscSlvFMMst: TInput
    Left = 7
    Top = 29
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 0
  end
  object ImageOscSlvFMOscType: TGraphicImage
    Left = 231
    Top = 4
    Width = 20
    Height = 19
    Transparent = False
    FileName = 'OscSlvFM.bmp'
  end
  object ImageOscSlvFMSync: TGraphicImage
    Left = 150
    Top = 13
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object InputOscSlvFMSync: TInput
    Left = 155
    Top = 13
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object TextLabelOscSlvFMSync: TTextLabel
    Left = 149
    Top = 2
    Width = 20
    Height = 11
    Caption = 'Sync'
  end
  object KnobOscSlvFMDetuneCoarse: TKnob
    Left = 89
    Top = 13
    Value = 64
    Display = DisplayOscSlvFMFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSlvFMDetuneFine: TSmallKnob
    Left = 120
    Top = 19
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscSlvFMFine
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayOscSlvFMFreq: TDisplay
    Left = 29
    Top = 11
    Width = 56
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 3
    DisplayFormats.Formats = [dfOscHz, dfSemitones, dfPartials]
    DisplayFormat = dfPartials
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobOscSlvFMFMB: TSmallKnob
    Left = 198
    Top = 6
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerOscSlvFMPartials: TSpinner
    Left = 63
    Top = 26
    Width = 20
    Height = 12
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = -1
    DefaultValue = 0
    Morphable = False
    Knobbable = False
    Controllable = False
    Orientation = soHorizontal
    MinValue = -32
    MaxValue = 32
    StepSize = 1
  end
  object ButtonSetOscSlvFMMute: TButtonSet
    Left = 219
    Top = 27
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
  object ButtonSetOscSlvFMOctDown: TButtonSet
    Left = 156
    Top = 27
    Width = 36
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = '-3 Oct'
      end>
  end
end
object T_OscSineBank
  Hint = 'Version = 16'
  TitleLabel = EditLabelOscSineBank
  Title = 'OscSineBank'
  Description = 'Osc Sine Bank'
  ModuleAttributes = []
  ModuleType = 106
  Cycles = 16.781250000000000000
  ProgMem = 3.562500000000000000
  XMem = 0.843750000000000000
  YMem = 2.250000000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 1.406250000000000000
  UnitHeight = 10
  object ImageOscSineBankLineLevel5: TGraphicImage
    Left = 177
    Top = 115
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object ImageOscSineBankLineLevel6: TGraphicImage
    Left = 217
    Top = 115
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object ImageOscSineBankLineLevel3: TGraphicImage
    Left = 94
    Top = 115
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object ImageOscSineBankLineLevel4: TGraphicImage
    Left = 136
    Top = 115
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object ImageOscSineBankLineLevel2: TGraphicImage
    Left = 55
    Top = 115
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object ImageOscSineBankLineLevel1: TGraphicImage
    Left = 13
    Top = 115
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelOscSineBank: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object BoxOscSineBankBottom: TBox
    Left = 12
    Top = 134
    Width = 232
    Height = 13
    Shape = bsTopLine
  end
  object InputOscSineBankMst: TInput
    Left = 10
    Top = 137
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 0
  end
  object TextLabelOscSineBankMst: TTextLabel
    Left = 22
    Top = 136
    Width = 17
    Height = 11
    Caption = 'Mst'
  end
  object ImageOscSineBankSync: TGraphicImage
    Left = 45
    Top = 136
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object InputOscSineBankSync: TInput
    Left = 50
    Top = 137
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object TextLabelOscSineBankSync: TTextLabel
    Left = 63
    Top = 136
    Width = 20
    Height = 11
    Caption = 'Sync'
  end
  object InputOscSineBankMixIn: TInput
    Left = 91
    Top = 137
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabelOscSineBankMixIn: TTextLabel
    Left = 104
    Top = 136
    Width = 24
    Height = 11
    Caption = 'Mix in'
  end
  object OutputSineBankOscOut: TOutput
    Left = 240
    Top = 137
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelOscSineBankSlaveBank: TTextLabel
    Left = 114
    Top = 1
    Width = 105
    Height = 11
    Caption = 'Sine slave oscillator bank'
  end
  object TextLabelOsc1: TTextLabel
    Left = 16
    Top = 12
    Width = 20
    Height = 11
    Caption = 'Osc1'
  end
  object TextLabelOscSineBankLevel1: TTextLabel
    Left = 24
    Top = 98
    Width = 19
    Height = 11
    Caption = 'level'
  end
  object SetterOscSineBankFine1: TSetter
    Left = 30
    Top = 69
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object InputOscSineBank1: TInput
    Left = 9
    Top = 115
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 3
  end
  object TextLabelOscSineBankTune1: TTextLabel
    Left = 7
    Top = 35
    Width = 21
    Height = 11
    Caption = 'Tune'
  end
  object TextLabelOsc2: TTextLabel
    Left = 57
    Top = 12
    Width = 22
    Height = 11
    Caption = 'Osc2'
  end
  object TextLabelOscSineBankLevel2: TTextLabel
    Left = 65
    Top = 98
    Width = 19
    Height = 11
    Caption = 'level'
  end
  object SetterOscSineBankFine2: TSetter
    Left = 71
    Top = 69
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object InputOscSineBank2: TInput
    Left = 50
    Top = 115
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 4
  end
  object TextLabelOscSineBankTune2: TTextLabel
    Left = 48
    Top = 35
    Width = 21
    Height = 11
    Caption = 'Tune'
  end
  object TextLabelOsc3: TTextLabel
    Left = 97
    Top = 12
    Width = 22
    Height = 11
    Caption = 'Osc3'
  end
  object TextLabelOscSineBankLevel3: TTextLabel
    Left = 106
    Top = 98
    Width = 19
    Height = 11
    Caption = 'level'
  end
  object SetterOscSineBankFine3: TSetter
    Left = 112
    Top = 69
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object InputOscSineBank3: TInput
    Left = 91
    Top = 115
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 5
  end
  object TextLabelOscSineBankTune3: TTextLabel
    Left = 89
    Top = 35
    Width = 21
    Height = 11
    Caption = 'Tune'
  end
  object TextLabelOsc4: TTextLabel
    Left = 139
    Top = 12
    Width = 22
    Height = 11
    Caption = 'Osc4'
  end
  object TextLabelOscSineBankLevel4: TTextLabel
    Left = 147
    Top = 98
    Width = 19
    Height = 11
    Caption = 'level'
  end
  object SetterOscSineBankFine4: TSetter
    Left = 153
    Top = 69
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object InputOscSineBank4: TInput
    Left = 132
    Top = 115
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 6
  end
  object TextLabelOscSineBankTune4: TTextLabel
    Left = 130
    Top = 35
    Width = 21
    Height = 11
    Caption = 'Tune'
  end
  object TextLabelOsc5: TTextLabel
    Left = 180
    Top = 12
    Width = 22
    Height = 11
    Caption = 'Osc5'
  end
  object TextLabelOscSineBankLevel5: TTextLabel
    Left = 188
    Top = 98
    Width = 19
    Height = 11
    Caption = 'level'
  end
  object SetterOscSineBankFine5: TSetter
    Left = 194
    Top = 69
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object InputOscSineBank5: TInput
    Left = 173
    Top = 115
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 7
  end
  object TextLabelOscSineBankTune5: TTextLabel
    Left = 171
    Top = 35
    Width = 21
    Height = 11
    Caption = 'Tune'
  end
  object TextLabelOsc6: TTextLabel
    Left = 221
    Top = 12
    Width = 22
    Height = 11
    Caption = 'Osc6'
  end
  object TextLabelOscSineBankLevel6: TTextLabel
    Left = 229
    Top = 98
    Width = 19
    Height = 11
    Caption = 'level'
  end
  object SetterOscSineBankFine6: TSetter
    Left = 235
    Top = 69
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object InputOscSineBank6: TInput
    Left = 214
    Top = 115
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 8
  end
  object TextLabelOscSineBankTune6: TTextLabel
    Left = 212
    Top = 35
    Width = 21
    Height = 11
    Caption = 'Tune'
  end
  object SmallKnobOscSineBankTuneCoarse1: TSmallKnob
    Left = 24
    Top = 44
    Value = 64
    Display = DisplayOscSinBankFreq1
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSineBankTuneFine1: TSmallKnob
    Left = 24
    Top = 76
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscSineBankFine1
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSineBankLevel1: TSmallKnob
    Left = 24
    Top = 109
    Value = 100
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 100
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayOscSinBankFreq1: TDisplay
    Left = 7
    Top = 22
    Width = 39
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 6
    DisplayFormats.Formats = [dfPartials]
    DisplayFormat = dfPartials
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobOscSineBankTuneCoarse2: TSmallKnob
    Left = 65
    Top = 44
    Value = 64
    Display = DisplayOscSinBankFreq2
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSineBankTuneFine2: TSmallKnob
    Left = 65
    Top = 76
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscSineBankFine2
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSineBankLevel2: TSmallKnob
    Left = 65
    Top = 109
    Value = 100
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 100
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayOscSinBankFreq2: TDisplay
    Left = 48
    Top = 22
    Width = 39
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 10
    DisplayFormats.Formats = [dfPartials]
    DisplayFormat = dfPartials
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobOscSineBankTuneCoarse3: TSmallKnob
    Left = 106
    Top = 44
    Value = 64
    Display = DisplayOscSinBankFreq3
    DisplayFormat = dfUnsigned
    CtrlIndex = 6
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSineBankTuneFine3: TSmallKnob
    Left = 106
    Top = 76
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 7
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscSineBankFine3
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSineBankLevel3: TSmallKnob
    Left = 106
    Top = 109
    Value = 100
    DisplayFormat = dfUnsigned
    CtrlIndex = 8
    DefaultValue = 100
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayOscSinBankFreq3: TDisplay
    Left = 89
    Top = 22
    Width = 39
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 14
    DisplayFormats.Formats = [dfPartials]
    DisplayFormat = dfPartials
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobOscSineBankTuneCoarse4: TSmallKnob
    Left = 147
    Top = 44
    Value = 64
    Display = DisplayOscSinBankFreq4
    DisplayFormat = dfUnsigned
    CtrlIndex = 9
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSineBankTuneFine4: TSmallKnob
    Left = 147
    Top = 76
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 10
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscSineBankFine4
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSineBankLevel4: TSmallKnob
    Left = 147
    Top = 109
    Value = 100
    DisplayFormat = dfUnsigned
    CtrlIndex = 11
    DefaultValue = 100
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayOscSinBankFreq4: TDisplay
    Left = 130
    Top = 22
    Width = 39
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 18
    DisplayFormats.Formats = [dfPartials]
    DisplayFormat = dfPartials
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobOscSineBankTuneCoarse5: TSmallKnob
    Left = 188
    Top = 44
    Value = 64
    Display = DisplayOscSinBankFreq5
    DisplayFormat = dfUnsigned
    CtrlIndex = 12
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSineBankTuneFine5: TSmallKnob
    Left = 188
    Top = 76
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 13
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscSineBankFine5
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSineBankLevel5: TSmallKnob
    Left = 188
    Top = 109
    Value = 100
    DisplayFormat = dfUnsigned
    CtrlIndex = 14
    DefaultValue = 100
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayOscSinBankFreq5: TDisplay
    Left = 171
    Top = 22
    Width = 39
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 22
    DisplayFormats.Formats = [dfPartials]
    DisplayFormat = dfPartials
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobOscSineBankTuneCoarse6: TSmallKnob
    Left = 229
    Top = 44
    Value = 64
    Display = DisplayOscSinBankFreq6
    DisplayFormat = dfUnsigned
    CtrlIndex = 15
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSineBankTuneFine6: TSmallKnob
    Left = 229
    Top = 76
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 16
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscSineBankFine6
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSineBankLevel6: TSmallKnob
    Left = 229
    Top = 109
    Value = 100
    DisplayFormat = dfUnsigned
    CtrlIndex = 17
    DefaultValue = 100
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayOscSinBankFreq6: TDisplay
    Left = 212
    Top = 22
    Width = 39
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 23
    DisplayFormats.Formats = [dfPartials]
    DisplayFormat = dfPartials
    Clickable = False
    CtrlIndex = -1
  end
  object SpinnerOscSineBankPartials1: TSpinner
    Left = 10
    Top = 46
    Width = 12
    Height = 20
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = -1
    DefaultValue = 0
    Morphable = False
    Knobbable = False
    Controllable = False
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerOscSineBankPartials2: TSpinner
    Left = 51
    Top = 46
    Width = 12
    Height = 20
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = -1
    DefaultValue = 0
    Morphable = False
    Knobbable = False
    Controllable = False
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerOscSineBankPartials3: TSpinner
    Left = 92
    Top = 46
    Width = 12
    Height = 20
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = -1
    DefaultValue = 0
    Morphable = False
    Knobbable = False
    Controllable = False
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerOscSineBankPartials4: TSpinner
    Left = 133
    Top = 46
    Width = 12
    Height = 20
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = -1
    DefaultValue = 0
    Morphable = False
    Knobbable = False
    Controllable = False
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerOscSineBankPartials5: TSpinner
    Left = 174
    Top = 46
    Width = 12
    Height = 20
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = -1
    DefaultValue = 0
    Morphable = False
    Knobbable = False
    Controllable = False
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerOscSineBankPartials6: TSpinner
    Left = 215
    Top = 46
    Width = 12
    Height = 20
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = -1
    DefaultValue = 0
    Morphable = False
    Knobbable = False
    Controllable = False
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetOscSineBankMute1: TButtonSet
    Left = 7
    Top = 80
    Width = 15
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 18
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
  object ButtonSetOscSineBankMute2: TButtonSet
    Left = 48
    Top = 80
    Width = 15
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 19
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
  object ButtonSetOscSineBankMute3: TButtonSet
    Left = 90
    Top = 80
    Width = 15
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 20
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
  object ButtonSetOscSineBankMute4: TButtonSet
    Left = 131
    Top = 80
    Width = 15
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 21
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
  object ButtonSetOscSineBankMute6: TButtonSet
    Left = 212
    Top = 80
    Width = 15
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 23
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
  object ButtonSetOscSineBankMute5: TButtonSet
    Left = 171
    Top = 80
    Width = 15
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 22
    DefaultValue = 0
    Morphable = False
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
end
object T_OscSlvE
  Hint = 'Version = 16'
  TitleLabel = EditLabelOscSlvE
  Title = 'OscSlvE'
  Description = 'OSC Slave E'
  ModuleAttributes = []
  ModuleType = 13
  Cycles = 3.062500000000000000
  ProgMem = 0.687500000000000000
  XMem = 0.343750000000000000
  YMem = 0.156250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.437500000000000000
  UnitHeight = 3
  object BoxOscSlvEFMA: TBox
    Left = 150
    Top = 3
    Width = 46
    Height = 38
    Shape = bsFrame
  end
  object ImageOscSlvELineFMA: TGraphicImage
    Left = 162
    Top = 21
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelOscSlvE: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object SetterOscSlvEDetune: TSetter
    Left = 126
    Top = 12
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelOscSlvEDetune: TTextLabel
    Left = 88
    Top = 1
    Width = 30
    Height = 11
    Caption = 'Detune'
  end
  object TextLabelOscSlvEFine: TTextLabel
    Left = 122
    Top = 1
    Width = 18
    Height = 11
    Caption = 'Fine'
  end
  object TextLabelOscSlvEMst: TTextLabel
    Left = 4
    Top = 17
    Width = 17
    Height = 11
    Caption = 'Mst'
  end
  object OutputOscSlvEOut: TOutput
    Left = 240
    Top = 29
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputOscSlvEFMA: TInput
    Left = 156
    Top = 21
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabelOscSlvEFMA: TTextLabel
    Left = 151
    Top = 6
    Width = 22
    Height = 11
    Caption = 'FMA'
  end
  object TextLabelOscSlvEPartials: TTextLabel
    Left = 29
    Top = 28
    Width = 32
    Height = 11
    Caption = 'Partials'
  end
  object InputOscSlvEMst: TInput
    Left = 7
    Top = 29
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 0
  end
  object ImageOscSslvEOscType: TGraphicImage
    Left = 231
    Top = 4
    Width = 20
    Height = 19
    Transparent = False
    FileName = 'OscSlvE.bmp'
  end
  object InputOscSlvEAM: TInput
    Left = 203
    Top = 29
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object TextLabelOscSlvEAM: TTextLabel
    Left = 200
    Top = 16
    Width = 16
    Height = 11
    Caption = 'AM'
  end
  object KnobOscSlvEDetuneCoarse: TKnob
    Left = 89
    Top = 13
    Value = 64
    Display = DisplayOscSlvEFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSlveDetuneFine: TSmallKnob
    Left = 120
    Top = 19
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscSlvEDetune
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayOscSlvEFreq: TDisplay
    Left = 29
    Top = 11
    Width = 56
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 3
    DisplayFormats.Formats = [dfOscHz, dfSemitones, dfPartials]
    DisplayFormat = dfPartials
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobOscSlvEFMA: TSmallKnob
    Left = 171
    Top = 14
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerOscSlvEPartials: TSpinner
    Left = 63
    Top = 26
    Width = 20
    Height = 12
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = -1
    DefaultValue = 0
    Morphable = False
    Knobbable = False
    Controllable = False
    Orientation = soHorizontal
    MinValue = -32
    MaxValue = 32
    StepSize = 1
  end
  object ButtonSetOscSlvEMute: TButtonSet
    Left = 219
    Top = 28
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
end
object T_OscSlvD
  Hint = 'Version = 16'
  TitleLabel = EditLabelOscSlvD
  Title = 'OscSlvD'
  Description = 'OSC Slave D'
  ModuleAttributes = []
  ModuleType = 12
  Cycles = 5.875000000000000000
  ProgMem = 1.312500000000000000
  XMem = 0.156250000000000000
  YMem = 0.218750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.437500000000000000
  UnitHeight = 3
  object BoxOscSlvDFMA: TBox
    Left = 161
    Top = 3
    Width = 51
    Height = 38
    Shape = bsFrame
  end
  object ImageOscSlvDLineFMA: TGraphicImage
    Left = 172
    Top = 22
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelOscSlvD: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object SetterOscSlvDDetune: TSetter
    Left = 126
    Top = 12
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelOscSlvDDetune: TTextLabel
    Left = 88
    Top = 1
    Width = 30
    Height = 11
    Caption = 'Detune'
  end
  object TextLabelOscSlvDFine: TTextLabel
    Left = 122
    Top = 1
    Width = 18
    Height = 11
    Caption = 'Fine'
  end
  object TextLabelOscSlvDMast: TTextLabel
    Left = 4
    Top = 17
    Width = 17
    Height = 11
    Caption = 'Mst'
  end
  object OscSlvDOut: TOutput
    Left = 240
    Top = 29
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputOscSlvDFMA: TInput
    Left = 168
    Top = 21
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabelOscSlvDFMA: TTextLabel
    Left = 163
    Top = 6
    Width = 22
    Height = 11
    Caption = 'FMA'
  end
  object TextLabelOscSlvDPartials: TTextLabel
    Left = 29
    Top = 28
    Width = 32
    Height = 11
    Caption = 'Partials'
  end
  object InputOscSlvDMst: TInput
    Left = 7
    Top = 29
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 0
  end
  object ImageOscSlvDOscType: TGraphicImage
    Left = 231
    Top = 4
    Width = 19
    Height = 19
    Transparent = False
    FileName = 'OscSlvD.bmp'
  end
  object KnobOscSlvDDetuneCoarse: TKnob
    Left = 89
    Top = 13
    Value = 64
    Display = DisplayOscSlvDFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSlvDDetuneFine: TSmallKnob
    Left = 120
    Top = 19
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscSlvDDetune
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayOscSlvDFreq: TDisplay
    Left = 29
    Top = 11
    Width = 56
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 3
    DisplayFormats.Formats = [dfOscHz, dfSemitones, dfPartials]
    DisplayFormat = dfPartials
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobOscSlvDFMA: TSmallKnob
    Left = 183
    Top = 14
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerOscSlvDPartials: TSpinner
    Left = 63
    Top = 26
    Width = 20
    Height = 12
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = -1
    DefaultValue = 0
    Morphable = False
    Knobbable = False
    Controllable = False
    Orientation = soHorizontal
    MinValue = -32
    MaxValue = 32
    StepSize = 1
  end
  object ButtonSetOscSlvDMute: TButtonSet
    Left = 219
    Top = 28
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
end
object T_OscSlvC
  Hint = 'Version = 16'
  TitleLabel = EditLabelOscSlvC
  Title = 'OscSlvC'
  Description = 'OSC Slave C'
  ModuleAttributes = []
  ModuleType = 11
  Cycles = 6.000000000000000000
  ProgMem = 1.750000000000000000
  XMem = 0.156250000000000000
  YMem = 0.218750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.500000000000000000
  UnitHeight = 3
  object BoxOscSlvCFMA: TBox
    Left = 161
    Top = 3
    Width = 51
    Height = 38
    Shape = bsFrame
  end
  object ImageOscSlvCLineFMA: TGraphicImage
    Left = 172
    Top = 22
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelOscSlvC: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object SetterOscSlvCDetune: TSetter
    Left = 126
    Top = 12
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelOscSlvCDetune: TTextLabel
    Left = 88
    Top = 1
    Width = 30
    Height = 11
    Caption = 'Detune'
  end
  object TextLabelOscSlvCFine: TTextLabel
    Left = 122
    Top = 1
    Width = 18
    Height = 11
    Caption = 'Fine'
  end
  object TextLabelOscSlvCMst: TTextLabel
    Left = 4
    Top = 17
    Width = 17
    Height = 11
    Caption = 'Mst'
  end
  object OutputOscSlvCOut: TOutput
    Left = 240
    Top = 29
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputOscSlvCFMA: TInput
    Left = 168
    Top = 21
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabelOscSlvCFMA: TTextLabel
    Left = 163
    Top = 6
    Width = 22
    Height = 11
    Caption = 'FMA'
  end
  object TextLabelOscSlvCPartials: TTextLabel
    Left = 29
    Top = 28
    Width = 32
    Height = 11
    Caption = 'Partials'
  end
  object InputOscSlvCMst: TInput
    Left = 7
    Top = 29
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 0
  end
  object ImageOscSlvCOscType: TGraphicImage
    Left = 231
    Top = 4
    Width = 20
    Height = 19
    Transparent = False
    FileName = 'OscSlvC.bmp'
  end
  object KnobOscSlvCDetuneCoarse: TKnob
    Left = 89
    Top = 13
    Value = 64
    Display = DisplayOscSlvCFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSlvCDetuneFine: TSmallKnob
    Left = 120
    Top = 19
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscSlvCDetune
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayOscSlvCFreq: TDisplay
    Left = 29
    Top = 11
    Width = 56
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 3
    DisplayFormats.Formats = [dfOscHz, dfSemitones, dfPartials]
    DisplayFormat = dfPartials
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobOscSlvCFMA: TSmallKnob
    Left = 183
    Top = 14
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerOscSlvCPartials: TSpinner
    Left = 63
    Top = 26
    Width = 20
    Height = 12
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = -1
    DefaultValue = 0
    Morphable = False
    Knobbable = False
    Controllable = False
    Orientation = soHorizontal
    MinValue = -32
    MaxValue = 32
    StepSize = 1
  end
  object ButtonSetOscSlvCMute: TButtonSet
    Left = 219
    Top = 28
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
end
object T_OscSlvB
  Hint = 'Version = 16'
  TitleLabel = EditLabelOscSlvB
  Title = 'OscSlvB'
  Description = 'OSC Slave B'
  ModuleAttributes = []
  ModuleType = 10
  Cycles = 7.125000000000000000
  ProgMem = 2.156250000000000000
  XMem = 0.218750000000000000
  YMem = 0.343750000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.500000000000000000
  UnitHeight = 3
  object BoxOscSlvBPW: TBox
    Left = 148
    Top = 3
    Width = 66
    Height = 38
    Shape = bsFrame
  end
  object ImageOscSlvBLinePW: TGraphicImage
    Left = 157
    Top = 21
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelOscSlvB: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object SetterOscSlvBDetune: TSetter
    Left = 126
    Top = 12
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelOscSlvBDetune: TTextLabel
    Left = 88
    Top = 1
    Width = 30
    Height = 11
    Caption = 'Detune'
  end
  object TextLabelOscSlvBFine: TTextLabel
    Left = 122
    Top = 1
    Width = 18
    Height = 11
    Caption = 'Fine'
  end
  object TextLabelOscSlvBMst: TTextLabel
    Left = 4
    Top = 17
    Width = 17
    Height = 11
    Caption = 'Mst'
  end
  object OutputOscSlvBOut: TOutput
    Left = 240
    Top = 29
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputOscSlvBPW: TInput
    Left = 152
    Top = 21
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabelOscSlvBPW: TTextLabel
    Left = 151
    Top = 6
    Width = 15
    Height = 11
    Caption = 'PW'
  end
  object TextLabelOscSlvBPartials: TTextLabel
    Left = 29
    Top = 28
    Width = 32
    Height = 11
    Caption = 'Partials'
  end
  object InputOscSlvBMst: TInput
    Left = 7
    Top = 29
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 0
  end
  object SetterOscSlvBPW: TSetter
    Left = 195
    Top = 7
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object ImageOscSlvBOscType: TGraphicImage
    Left = 231
    Top = 4
    Width = 20
    Height = 20
    Transparent = False
    FileName = 'OscSlvB.bmp'
  end
  object KnobOscSlvBDetuneCoarse: TKnob
    Left = 89
    Top = 13
    Value = 64
    Display = DisplayOscSlvBFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSlvBDetuneFine: TSmallKnob
    Left = 120
    Top = 19
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscSlvBDetune
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayOscSlvBFreq: TDisplay
    Left = 29
    Top = 11
    Width = 56
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 4
    DisplayFormats.Formats = [dfOscHz, dfSemitones, dfPartials]
    DisplayFormat = dfPartials
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobOscSlvBPWMod: TSmallKnob
    Left = 167
    Top = 14
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSlvBPW: TSmallKnob
    Left = 189
    Top = 14
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscSlvBPW
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerOscSlvBPartials: TSpinner
    Left = 63
    Top = 26
    Width = 20
    Height = 12
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = -1
    DefaultValue = 0
    Morphable = False
    Knobbable = False
    Controllable = False
    Orientation = soHorizontal
    MinValue = -32
    MaxValue = 32
    StepSize = 1
  end
  object ButtonSetOscSlvBMute: TButtonSet
    Left = 219
    Top = 28
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
end
object T_OscSlvA
  Hint = 'Version = 16'
  TitleLabel = EditLabelOscSlvA
  Title = 'OscSlvA'
  Description = 'OSC Slave A'
  ModuleAttributes = []
  ModuleType = 14
  Cycles = 7.625000000000000000
  ProgMem = 5.875000000000000000
  XMem = 0.437500000000000000
  YMem = 0.281250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.500000000000000000
  UnitHeight = 4
  object BoxOscSlvAFMA: TBox
    Left = 144
    Top = 22
    Width = 50
    Height = 36
    Shape = bsFrame
  end
  object ImageOscSlvALineFMA: TGraphicImage
    Left = 157
    Top = 37
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelOscSlvA: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object SetterOscSlvADetune: TSetter
    Left = 126
    Top = 20
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelOscSlvADetune: TTextLabel
    Left = 88
    Top = 8
    Width = 30
    Height = 11
    Caption = 'Detune'
  end
  object TextLabelOscSlvAFine: TTextLabel
    Left = 122
    Top = 8
    Width = 18
    Height = 11
    Caption = 'Fine'
  end
  object TextLabelOscSlvAMst: TTextLabel
    Left = 19
    Top = 43
    Width = 17
    Height = 11
    Caption = 'Mst'
  end
  object OutputOscSlvAOut: TOutput
    Left = 240
    Top = 44
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputOscSlvAFMA: TInput
    Left = 153
    Top = 37
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object InputOscSlvAAM: TInput
    Left = 202
    Top = 44
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 3
  end
  object TextLabelOscSlvAFMA: TTextLabel
    Left = 146
    Top = 23
    Width = 22
    Height = 11
    Caption = 'FMA'
  end
  object TextLabelOscSlvAAM: TTextLabel
    Left = 199
    Top = 31
    Width = 16
    Height = 11
    Caption = 'AM'
  end
  object TextLabelOscSlvAPartials: TTextLabel
    Left = 29
    Top = 34
    Width = 32
    Height = 11
    Caption = 'Partials'
  end
  object ImageOscSlvASync: TGraphicImage
    Left = 3
    Top = 27
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object InputOscSlvASync: TInput
    Left = 7
    Top = 27
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object TextLabelOscSlvASync: TTextLabel
    Left = 2
    Top = 16
    Width = 20
    Height = 11
    Caption = 'Sync'
  end
  object InputOscSlvAMst: TInput
    Left = 7
    Top = 44
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 0
  end
  object KnobOscSlvADetuneCoarse: TKnob
    Left = 89
    Top = 20
    Value = 64
    Display = DisplayOscSlvAFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscSlvADetuneFine: TSmallKnob
    Left = 120
    Top = 26
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscSlvADetune
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayOscSlvAFreq: TDisplay
    Left = 29
    Top = 18
    Width = 56
    Alignment = taCenter
    Caption = '1:1'
    TabOrder = 3
    DisplayFormats.Formats = [dfOscHz, dfSemitones, dfPartials]
    DisplayFormat = dfPartials
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobOscSlvAFMA: TSmallKnob
    Left = 168
    Top = 31
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SpinnerOscSlvAPartials: TSpinner
    Left = 63
    Top = 33
    Width = 20
    Height = 12
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = -1
    DefaultValue = 0
    Morphable = False
    Knobbable = False
    Controllable = False
    Orientation = soHorizontal
    MinValue = -32
    MaxValue = 32
    StepSize = 1
  end
  object ButtonSetOscSlvAWave: TButtonSet
    Left = 165
    Top = 3
    Width = 88
    Height = 14
    Value = 0
    DisplayFormat = dfOscWave
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smRadio
    Clickers = <
      item
        FileName = '_sine.bmp'
      end
      item
        FileName = '_tri.bmp'
      end
      item
        FileName = '_saw.bmp'
      end
      item
        FileName = '_square.bmp'
      end>
  end
  object ButtonSetOscSlvAMute: TButtonSet
    Left = 219
    Top = 43
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
end
object T_FormantOsc
  Hint = 'Version = 16'
  TitleLabel = EditLabelFormantOsc
  Title = 'FormantOsc'
  Description = 'Formant Osc'
  ModuleAttributes = []
  ModuleType = 96
  Cycles = 9.062500000000000000
  ProgMem = 2.531250000000000000
  XMem = 0.562500000000000000
  YMem = 0.500000000000000000
  ZeroPage = 1.750000000000000000
  DynMem = 0.593750000000000000
  UnitHeight = 4
  object BoxFormantOscTimbre: TBox
    Left = 151
    Top = 4
    Width = 76
    Height = 35
    Shape = bsFrame
  end
  object ImageFormantOscTimbre: TGraphicImage
    Left = 160
    Top = 19
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object ImageFormantOscLinePitch2: TGraphicImage
    Left = 51
    Top = 40
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object ImageFormantOscLinePitch1: TGraphicImage
    Left = 11
    Top = 40
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelFormantOsc: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object SetterFormantOscFine: TSetter
    Left = 131
    Top = 26
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object OutputFormantOscSlv: TOutput
    Left = 154
    Top = 44
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 1
  end
  object TextLabelFormantOscCoarse: TTextLabel
    Left = 91
    Top = 16
    Width = 31
    Height = 11
    Caption = 'Coarse'
  end
  object TextLabelFormantOscFine: TTextLabel
    Left = 127
    Top = 16
    Width = 18
    Height = 11
    Caption = 'Fine'
  end
  object TextLabelFormantOscSlv: TTextLabel
    Left = 166
    Top = 43
    Width = 13
    Height = 11
    Caption = 'Slv'
  end
  object InputFormantOscPitch1: TInput
    Left = 6
    Top = 40
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object ImageFormantOscType: TGraphicImage
    Left = 231
    Top = 4
    Width = 20
    Height = 19
    FileName = 'FormantOsc.bmp'
  end
  object OutputFormantOscOut: TOutput
    Left = 240
    Top = 44
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelFormantOscFreq: TTextLabel
    Left = 4
    Top = 18
    Width = 19
    Height = 11
    Caption = 'Freq'
  end
  object InputFormantOscPitch2: TInput
    Left = 46
    Top = 40
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 1
  end
  object InputFormantOscTimbre: TInput
    Left = 156
    Top = 19
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 2
  end
  object TextLabelFormantOscTimbre: TTextLabel
    Left = 193
    Top = 4
    Width = 29
    Height = 11
    Caption = 'Timbre'
  end
  object KnobFormantOscFreqCoarse: TKnob
    Left = 92
    Top = 28
    Value = 64
    Display = DisplayFormantOscFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobFormantOscFreqFine: TSmallKnob
    Left = 125
    Top = 34
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterFormantOscFine
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayFormantOscFreq: TDisplay
    Left = 30
    Top = 16
    Width = 57
    Alignment = taCenter
    Caption = '330 Hz'
    TabOrder = 5
    DisplayFormats.Formats = [dfNote, dfOscHz]
    DisplayFormat = dfOscHz
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobFormantOscPitch1: TSmallKnob
    Left = 21
    Top = 34
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobFormantOscPitch2: TSmallKnob
    Left = 61
    Top = 34
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 6
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobFormantOscTimbre: TSmallKnob
    Left = 170
    Top = 13
    Value = 0
    Display = DisplayFormantOscTimbre
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayFormantOscTimbre: TDisplay
    Left = 195
    Top = 17
    Width = 27
    Alignment = taCenter
    Caption = '1'
    TabOrder = 6
    DisplayFormats.Formats = [dfFmtTimbre]
    DisplayFormat = dfFmtTimbre
    Clickable = False
    CtrlIndex = -1
  end
  object ButtonSetFormantOscMute: TButtonSet
    Left = 219
    Top = 43
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
  object ButtonSetFormantOscKBT: TButtonSet
    Left = 187
    Top = 43
    Width = 25
    Height = 14
    Value = 1
    DisplayFormat = dfOnOff
    CtrlIndex = 2
    DefaultValue = 1
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'KBT'
      end>
  end
end
object T_SpectralOsc
  Hint = 'Version = 16'
  TitleLabel = EditLabelSpectralOsc
  Title = 'SpectralOsc'
  Description = 'Spectral Osc'
  ModuleAttributes = []
  ModuleType = 107
  Cycles = 7.906250000000000000
  ProgMem = 2.406250000000000000
  XMem = 0.500000000000000000
  YMem = 0.750000000000000000
  ZeroPage = 1.750000000000000000
  DynMem = 0.750000000000000000
  UnitHeight = 5
  object BoxSpectralOscShape: TBox
    Left = 154
    Top = 1
    Width = 50
    Height = 72
    Shape = bsFrame
  end
  object ImageSpectralOscLineShape: TGraphicImage
    Left = 164
    Top = 55
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelSpectralOsc: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object SetterSpectralOscFreqFine: TSetter
    Left = 131
    Top = 10
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object OutputSpectralOscSlv: TOutput
    Left = 6
    Top = 25
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 1
  end
  object TextLabelSpectralOscCoarse: TTextLabel
    Left = 91
    Top = 0
    Width = 31
    Height = 11
    Caption = 'Coarse'
  end
  object TextLabelSpectralOscFine: TTextLabel
    Left = 127
    Top = 0
    Width = 18
    Height = 11
    Caption = 'Fine'
  end
  object TextLabelSpectralOscSlv: TTextLabel
    Left = 4
    Top = 13
    Width = 13
    Height = 11
    Caption = 'Slv'
  end
  object InputSpectralOscPitch1: TInput
    Left = 6
    Top = 57
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 1
  end
  object TextLabelSpectralOscPitch1: TTextLabel
    Left = 3
    Top = 40
    Width = 22
    Height = 11
    Caption = 'Pitch'
  end
  object InputSpectralOscPitch2: TInput
    Left = 48
    Top = 57
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 2
  end
  object TextLabelSpectralOscPitch2: TTextLabel
    Left = 45
    Top = 40
    Width = 22
    Height = 11
    Caption = 'Pitch'
  end
  object InputSpectralOscFMA: TInput
    Left = 90
    Top = 57
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelSpectralOscFMA: TTextLabel
    Left = 87
    Top = 40
    Width = 22
    Height = 11
    Caption = 'FMA'
  end
  object TextLabelSpectralOscSpectral: TTextLabel
    Left = 162
    Top = 3
    Width = 34
    Height = 11
    Caption = 'Spectral'
  end
  object InputSpectralOscShape: TInput
    Left = 159
    Top = 55
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 3
  end
  object TextLabelSpectralOscShape: TTextLabel
    Left = 166
    Top = 12
    Width = 25
    Height = 11
    Caption = 'shape'
  end
  object TextLabelSpectralOscFreq: TTextLabel
    Left = 33
    Top = 11
    Width = 19
    Height = 11
    Caption = 'Freq'
  end
  object ImageSpectralOscType: TGraphicImage
    Left = 231
    Top = 3
    Width = 20
    Height = 19
    Transparent = False
    FileName = 'SpectralOsc.bmp'
  end
  object OutputSpectralOscOut: TOutput
    Left = 240
    Top = 59
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelSpectralOscPartials: TTextLabel
    Left = 212
    Top = 24
    Width = 32
    Height = 11
    Caption = 'Partials'
  end
  object ImageSpectralOscLinePitch1: TGraphicImage
    Left = 10
    Top = 58
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object ImageSpectralOscLinePitch2: TGraphicImage
    Left = 54
    Top = 58
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object ImageSpectralOscLineFMA: TGraphicImage
    Left = 99
    Top = 58
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object KnobSpectralOscFreqCoarse: TKnob
    Left = 92
    Top = 11
    Value = 64
    Display = DisplaySpectralOscFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobSpectralOscFreqFine: TSmallKnob
    Left = 125
    Top = 17
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterSpectralOscFreqFine
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplaySpectralOscFreq: TDisplay
    Left = 30
    Top = 23
    Width = 57
    Alignment = taCenter
    Caption = '330 Hz'
    TabOrder = 7
    DisplayFormats.Formats = [dfNote, dfOscHz]
    DisplayFormat = dfOscHz
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobSpectralOscPitch1: TSmallKnob
    Left = 19
    Top = 50
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobSpectralOscPitch2: TSmallKnob
    Left = 61
    Top = 50
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobSpectralOscFMA: TSmallKnob
    Left = 103
    Top = 50
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 6
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobSpectralOscShape: TSmallKnob
    Left = 168
    Top = 22
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobSpectralOscShapeMod: TSmallKnob
    Left = 177
    Top = 48
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 7
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetSpectralOscParrtials: TButtonSet
    Left = 205
    Top = 37
    Width = 48
    Height = 14
    Value = 1
    DisplayFormat = dfPartialsGroup
    CtrlIndex = 3
    DefaultValue = 1
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smRadio
    Clickers = <
      item
        Caption = 'Odd'
      end
      item
        Caption = 'All'
      end>
  end
  object ButtonSetSpectralOscMute: TButtonSet
    Left = 219
    Top = 57
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 9
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
  object ButtonSetSpetralOscKBT: TButtonSet
    Left = 127
    Top = 54
    Width = 24
    Height = 14
    Value = 1
    DisplayFormat = dfOnOff
    CtrlIndex = 8
    DefaultValue = 1
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'KBT'
      end>
  end
end
object T_OscC
  Hint = 'Version = 16'
  TitleLabel = EditLabelOscC
  Title = 'OscC'
  Description = 'OSC C'
  ModuleAttributes = []
  ModuleType = 9
  Cycles = 4.968750000000000000
  ProgMem = 1.093750000000000000
  XMem = 0.562500000000000000
  YMem = 0.093750000000000000
  ZeroPage = 1.750000000000000000
  DynMem = 0.531250000000000000
  UnitHeight = 4
  object BoxOSCCFMA: TBox
    Left = 154
    Top = 19
    Width = 49
    Height = 38
    Shape = bsFrame
  end
  object ImageOscCLineFMA: TGraphicImage
    Left = 164
    Top = 37
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelOscC: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object SetterOscCFreqFine: TSetter
    Left = 131
    Top = 26
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object OutputOscCSlv: TOutput
    Left = 6
    Top = 44
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 1
  end
  object TextLabelOscCCoarse: TTextLabel
    Left = 91
    Top = 16
    Width = 31
    Height = 11
    Caption = 'Coarse'
  end
  object TextLabelOscFine: TTextLabel
    Left = 127
    Top = 16
    Width = 18
    Height = 11
    Caption = 'Fine'
  end
  object TextLabelOscSlave: TTextLabel
    Left = 18
    Top = 43
    Width = 13
    Height = 11
    Caption = 'Slv'
  end
  object InputOscCPitch: TInput
    Left = 46
    Top = 40
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object ImageOscCType: TGraphicImage
    Left = 231
    Top = 4
    Width = 20
    Height = 19
    Transparent = False
    FileName = 'OscC.bmp'
  end
  object InputOscCFMA: TInput
    Left = 158
    Top = 37
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelOscCFMA: TTextLabel
    Left = 157
    Top = 19
    Width = 22
    Height = 11
    Caption = 'FMA'
  end
  object OutputOscCOut: TOutput
    Left = 240
    Top = 44
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputOscCAM: TInput
    Left = 176
    Top = 6
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object TextLabelOscCAM: TTextLabel
    Left = 155
    Top = 5
    Width = 16
    Height = 11
    Caption = 'AM'
  end
  object TextLabelOscFreq: TTextLabel
    Left = 4
    Top = 18
    Width = 19
    Height = 11
    Caption = 'Freq'
  end
  object ImageOscCLineFreqMod: TGraphicImage
    Left = 51
    Top = 40
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object KnobOscCFreqCoarse: TKnob
    Left = 92
    Top = 28
    Value = 64
    Display = DisplayOscCFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscCFreqFine: TSmallKnob
    Left = 125
    Top = 34
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscCFreqFine
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayOscCFreq: TDisplay
    Left = 30
    Top = 16
    Width = 57
    Alignment = taCenter
    Caption = '330 Hz'
    TabOrder = 4
    DisplayFormats.Formats = [dfNote, dfOscHz]
    DisplayFormat = dfOscHz
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobOscPitch: TSmallKnob
    Left = 61
    Top = 34
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscCFMA: TSmallKnob
    Left = 174
    Top = 31
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetOscCMute: TButtonSet
    Left = 219
    Top = 42
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 5
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
  object ButtonSetOscCKBT: TButtonSet
    Left = 202
    Top = 4
    Width = 25
    Height = 14
    Value = 1
    DisplayFormat = dfOnOff
    CtrlIndex = 2
    DefaultValue = 1
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'KBT'
      end>
  end
end
object T_OscB
  Hint = 'Version = 16'
  TitleLabel = EditLabelOscB
  Title = 'OscB'
  Description = 'OSC B'
  ModuleAttributes = []
  ModuleType = 8
  Cycles = 8.281250000000000000
  ProgMem = 6.250000000000000000
  XMem = 0.687500000000000000
  YMem = 0.500000000000000000
  ZeroPage = 1.750000000000000000
  DynMem = 0.687500000000000000
  UnitHeight = 6
  object ImageOscBLineFMA: TGraphicImage
    Left = 99
    Top = 71
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object ImageOscBLinePitch2: TGraphicImage
    Left = 54
    Top = 71
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object ImageOscBLinePitch1: TGraphicImage
    Left = 10
    Top = 71
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object BoxOscBPWidth: TBox
    Left = 165
    Top = 4
    Width = 50
    Height = 48
    Shape = bsFrame
  end
  object ImageOscCLinePWidth: TGraphicImage
    Left = 177
    Top = 30
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelOscB: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object InputOscBPitch1: TInput
    Left = 6
    Top = 70
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 1
  end
  object TextLabelOscBPitch1: TTextLabel
    Left = 3
    Top = 54
    Width = 22
    Height = 11
    Caption = 'Pitch'
  end
  object InputOscBPitch2: TInput
    Left = 50
    Top = 70
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 2
  end
  object TextLabelOscBPitch2: TTextLabel
    Left = 47
    Top = 54
    Width = 22
    Height = 11
    Caption = 'Pitch'
  end
  object SetterOscBFreqFine: TSetter
    Left = 105
    Top = 23
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object OutputOscBSlv: TOutput
    Left = 6
    Top = 42
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 1
  end
  object TextLabelOscBCoarse: TTextLabel
    Left = 65
    Top = 11
    Width = 31
    Height = 11
    Caption = 'Coarse'
  end
  object TextLabelOscBFine: TTextLabel
    Left = 101
    Top = 11
    Width = 18
    Height = 11
    Caption = 'Fine'
  end
  object TextLabelOscBFreq: TTextLabel
    Left = 4
    Top = 10
    Width = 19
    Height = 11
    Caption = 'Freq'
  end
  object SetterOscBKBT: TSetter
    Left = 136
    Top = 23
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelOscBKBT: TTextLabel
    Left = 132
    Top = 11
    Width = 19
    Height = 11
    Caption = 'KBT'
  end
  object InputOscBFMA: TInput
    Left = 94
    Top = 70
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelOscBFMA: TTextLabel
    Left = 91
    Top = 54
    Width = 22
    Height = 11
    Caption = 'FMA'
  end
  object TextLabelOscBPWidth: TTextLabel
    Left = 174
    Top = 6
    Width = 30
    Height = 11
    Caption = 'PWidth'
  end
  object OutputOscBOut: TOutput
    Left = 240
    Top = 74
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelOscBSlv: TTextLabel
    Left = 18
    Top = 41
    Width = 13
    Height = 11
    Caption = 'Slv'
  end
  object InputOscBPWidth: TInput
    Left = 171
    Top = 30
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 3
  end
  object KnobOscBFreqCoarse: TKnob
    Left = 66
    Top = 25
    Value = 64
    Display = DisplayOscBFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscBFreqFine: TSmallKnob
    Left = 99
    Top = 31
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscBFreqFine
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayOscBFreq: TDisplay
    Left = 4
    Top = 22
    Width = 57
    Alignment = taCenter
    Caption = '330 Hz'
    TabOrder = 7
    DisplayFormats.Formats = [dfNote, dfOscHz]
    DisplayFormat = dfOscHz
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobOscBPitch1: TSmallKnob
    Left = 19
    Top = 64
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscBPitch2: TSmallKnob
    Left = 63
    Top = 64
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscBKBT: TSmallKnob
    Left = 130
    Top = 31
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscBKBT
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscBFMA: TSmallKnob
    Left = 107
    Top = 64
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 6
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscBPWidth: TSmallKnob
    Left = 186
    Top = 24
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 7
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetOscBWave: TButtonSet
    Left = 226
    Top = 3
    Width = 25
    Height = 56
    Value = 0
    DisplayFormat = dfOscWave
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soVertical
    Direction = diReversed
    SelectMode = smRadio
    Clickers = <
      item
        FileName = '_sine.bmp'
      end
      item
        FileName = '_tri.bmp'
      end
      item
        FileName = '_saw.bmp'
      end
      item
        FileName = '_square.bmp'
      end>
  end
  object ButtonSetOscBMute: TButtonSet
    Left = 219
    Top = 72
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 8
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
end
object T_OscA
  Hint = 'Version = 16'
  TitleLabel = EditLabelOscA
  Title = 'OscA'
  Description = 'OSC A'
  ModuleAttributes = []
  ModuleType = 7
  Cycles = 10.812500000000000000
  ProgMem = 6.562500000000000000
  XMem = 0.843750000000000000
  YMem = 0.343750000000000000
  ZeroPage = 1.750000000000000000
  DynMem = 0.375000000000000000
  UnitHeight = 6
  object BoxOscAPWidth: TBox
    Left = 165
    Top = 4
    Width = 50
    Height = 74
    Shape = bsFrame
  end
  object ImageOscALinePWidth: TGraphicImage
    Left = 177
    Top = 58
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object ImageOscALineFMA: TGraphicImage
    Left = 99
    Top = 71
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object ImageOscALinePitch2: TGraphicImage
    Left = 54
    Top = 71
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object ImageOscALinePitch1: TGraphicImage
    Left = 10
    Top = 71
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelOscA: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object InputOscAPitch1: TInput
    Left = 6
    Top = 70
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object TextLabelOscAPitch1: TTextLabel
    Left = 3
    Top = 54
    Width = 22
    Height = 11
    Caption = 'Pitch'
  end
  object InputOscAPitch2: TInput
    Left = 50
    Top = 70
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 3
  end
  object TextLabelOscAPitch2: TTextLabel
    Left = 47
    Top = 54
    Width = 22
    Height = 11
    Caption = 'Pitch'
  end
  object SetterOscAFreqFine: TSetter
    Left = 105
    Top = 23
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object OutputOscASlv: TOutput
    Left = 6
    Top = 42
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 1
  end
  object TextLabelOscACoarse: TTextLabel
    Left = 65
    Top = 11
    Width = 31
    Height = 11
    Caption = 'Coarse'
  end
  object TextLabelOscAFine: TTextLabel
    Left = 101
    Top = 11
    Width = 18
    Height = 11
    Caption = 'Fine'
  end
  object TextLabelOscAFreq: TTextLabel
    Left = 4
    Top = 10
    Width = 19
    Height = 11
    Caption = 'Freq'
  end
  object SetterOscAKBT: TSetter
    Left = 136
    Top = 22
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelOscAKBT: TTextLabel
    Left = 132
    Top = 10
    Width = 19
    Height = 11
    Caption = 'KBT'
  end
  object InputOscAFMA: TInput
    Left = 94
    Top = 70
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabelOscAFMA: TTextLabel
    Left = 91
    Top = 54
    Width = 22
    Height = 11
    Caption = 'FMA'
  end
  object ImageOscASync: TGraphicImage
    Left = 133
    Top = 69
    Width = 3
    Height = 10
    FileName = '_sync.bmp'
  end
  object InputOscASync: TInput
    Left = 138
    Top = 70
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelOscASysnc: TTextLabel
    Left = 131
    Top = 54
    Width = 20
    Height = 11
    Caption = 'Sync'
  end
  object SetterOscAPWidth: TSetter
    Left = 185
    Top = 16
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object TextLabelOscAPWidth: TTextLabel
    Left = 175
    Top = 4
    Width = 30
    Height = 11
    Caption = 'PWidth'
  end
  object OutputOscAOut: TOutput
    Left = 240
    Top = 74
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object InputOscAPWidth: TInput
    Left = 171
    Top = 58
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 4
  end
  object TextLabelOscASlv: TTextLabel
    Left = 18
    Top = 41
    Width = 13
    Height = 11
    Caption = 'Slv'
  end
  object KnobOscAFreqCoarse: TKnob
    Left = 66
    Top = 25
    Value = 64
    Display = DisplayOscAFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscAFreqFine: TSmallKnob
    Left = 99
    Top = 31
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscAFreqFine
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayOscAFreq: TDisplay
    Left = 4
    Top = 22
    Width = 57
    Alignment = taCenter
    Caption = '330 Hz'
    TabOrder = 8
    DisplayFormats.Formats = [dfNote, dfOscHz]
    DisplayFormat = dfOscHz
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobOscAPitch1: TSmallKnob
    Left = 19
    Top = 64
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 5
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscAPitch2: TSmallKnob
    Left = 63
    Top = 64
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 6
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscAKBT: TSmallKnob
    Left = 130
    Top = 30
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 2
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscAKBT
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscAFMA: TSmallKnob
    Left = 107
    Top = 64
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 7
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscAPWidth: TSmallKnob
    Left = 179
    Top = 24
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterOscAPWidth
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobOscAPWidthMod: TSmallKnob
    Left = 190
    Top = 51
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 8
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetOscAWave: TButtonSet
    Left = 226
    Top = 3
    Width = 25
    Height = 56
    Value = 0
    DisplayFormat = dfOscWave
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soVertical
    Direction = diReversed
    SelectMode = smRadio
    Clickers = <
      item
        FileName = '_sine.bmp'
      end
      item
        FileName = '_tri.bmp'
      end
      item
        FileName = '_saw.bmp'
      end
      item
        FileName = '_square.bmp'
      end>
  end
  object ButtonSetOscAMute: TButtonSet
    Left = 219
    Top = 72
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 9
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
end
object T_MasterOsc
  Hint = 'Version = 16'
  TitleLabel = EditLabelMasterOsc
  Title = 'MasterOsc'
  Description = 'Master Oscillator'
  ModuleAttributes = []
  ModuleType = 97
  Cycles = 0.781250000000000000
  ProgMem = 0.687500000000000000
  XMem = 0.218750000000000000
  YMem = 0.156250000000000000
  ZeroPage = 0.875000000000000000
  DynMem = 0.500000000000000000
  UnitHeight = 3
  object ImageMasterOscLinePitch2: TGraphicImage
    Left = 200
    Top = 26
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object ImageMasterOscLinePitch1: TGraphicImage
    Left = 159
    Top = 25
    Width = 22
    Height = 9
    FileName = 'hline.bmp'
  end
  object EditLabelMasterOsc: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object InputMasterOscPitch1: TInput
    Left = 155
    Top = 25
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object TextLabelMasterOscPitch1: TTextLabel
    Left = 155
    Top = 9
    Width = 22
    Height = 11
    Caption = 'Pitch'
  end
  object InputMasterOscPitch2: TInput
    Left = 195
    Top = 25
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 1
  end
  object TextLabelMasterOscPitch2: TTextLabel
    Left = 195
    Top = 9
    Width = 22
    Height = 11
    Caption = 'Pitch'
  end
  object SetterMasterOscFine: TSetter
    Left = 133
    Top = 11
    Width = 9
    Height = 6
    DefaultValue = 64
  end
  object OutputMasterOscSlv: TOutput
    Left = 6
    Top = 32
    Width = 10
    Height = 10
    WireColor = clGray
    CtrlIndex = 0
  end
  object TextLabelMasterOscCoarse: TTextLabel
    Left = 93
    Top = 1
    Width = 31
    Height = 11
    Caption = 'Coarse'
  end
  object TextLabelMasterOscFine: TTextLabel
    Left = 129
    Top = 1
    Width = 18
    Height = 11
    Caption = 'Fine'
  end
  object TextLabelMasterOscSlv: TTextLabel
    Left = 4
    Top = 19
    Width = 13
    Height = 11
    Caption = 'Slv'
  end
  object KnobMasterOscFreqCoarse: TKnob
    Left = 94
    Top = 13
    Value = 64
    Display = DisplayMasterOscFreq
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobMasterOscFreqFine: TSmallKnob
    Left = 127
    Top = 19
    Value = 64
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    Setter = SetterMasterOscFine
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayMasterOscFreq: TDisplay
    Left = 32
    Top = 24
    Width = 57
    Alignment = taCenter
    Caption = '330 Hz'
    TabOrder = 4
    DisplayFormats.Formats = [dfNote, dfOscHz]
    DisplayFormat = dfOscHz
    Clickable = True
    CtrlIndex = 0
  end
  object SmallKnobMasterOscPitch1: TSmallKnob
    Left = 168
    Top = 19
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 3
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object SmallKnobMasterOscPitch2: TSmallKnob
    Left = 208
    Top = 19
    Value = 0
    DisplayFormat = dfUnsigned
    CtrlIndex = 4
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSetMasterOscKBT: TButtonSet
    Left = 227
    Top = 4
    Width = 25
    Height = 14
    Value = 1
    DisplayFormat = dfOnOff
    CtrlIndex = 2
    DefaultValue = 1
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'KBT'
      end>
  end
end
object T_KeybSplit
  Hint = 'Version = 16'
  TitleLabel = EditLabelKeybSplit
  Title = 'KeybSplit'
  Description = 'Keyboard Split'
  ModuleAttributes = []
  ModuleType = 100
  Cycles = 0.790000021457672100
  ProgMem = 1.000000000000000000
  XMem = 1.000000000000000000
  YMem = 1.000000000000000000
  ZeroPage = 3.000000000000000000
  DynMem = 1.000000000000000000
  UnitHeight = 3
  object EditLabelKeybSplit: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelKeybSplitLower: TTextLabel
    Left = 86
    Top = 7
    Width = 25
    Height = 11
    Caption = 'Lower'
  end
  object TextLabelKeybSplitUpper: TTextLabel
    Left = 151
    Top = 7
    Width = 24
    Height = 11
    Caption = 'Upper'
  end
  object InputKeybSplitNote: TInput
    Left = 202
    Top = 3
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object InputKeybSplitGate: TInput
    Left = 202
    Top = 17
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object InputKeybSplitVel: TInput
    Left = 202
    Top = 31
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 2
  end
  object OutputKeybSplitNote: TOutput
    Left = 240
    Top = 3
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object OutputKeybSplitGate: TOutput
    Left = 240
    Top = 17
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object OutputKeybSplitVel: TOutput
    Left = 240
    Top = 31
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 2
  end
  object TextLabelKeybSplitNote: TTextLabel
    Left = 215
    Top = 2
    Width = 21
    Height = 11
    Caption = 'Note'
  end
  object TextLabelKeybSplitGate: TTextLabel
    Left = 215
    Top = 16
    Width = 20
    Height = 11
    Caption = 'Gate'
  end
  object TextLabelKeybSplitVel: TTextLabel
    Left = 217
    Top = 30
    Width = 13
    Height = 11
    Caption = 'Vel'
  end
  object SmallKnobKeybSplitLower: TSmallKnob
    Left = 88
    Top = 19
    Value = 64
    Display = DisplayKeybSplitLower
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayKeybSplitLower: TDisplay
    Left = 52
    Top = 23
    Width = 29
    Alignment = taCenter
    Caption = 'E4 '
    TabOrder = 2
    DisplayFormats.Formats = [dfNote]
    DisplayFormat = dfNote
    Clickable = False
    CtrlIndex = -1
  end
  object SmallKnobKeybSplitUpper: TSmallKnob
    Left = 152
    Top = 19
    Value = 64
    Display = DisplayKeybSplitUpper
    DisplayFormat = dfUnsigned
    CtrlIndex = 1
    DefaultValue = 64
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayKeybSplitUpper: TDisplay
    Left = 116
    Top = 23
    Width = 29
    Alignment = taCenter
    Caption = 'E4 '
    TabOrder = 3
    DisplayFormats.Formats = [dfNote]
    DisplayFormat = dfNote
    Clickable = False
    CtrlIndex = -1
  end
end
object T_NoteDetect
  Hint = 'Version = 16'
  TitleLabel = EditLabelNoteDetect
  Title = 'NoteDetect'
  Description = 'Note detector'
  ModuleAttributes = []
  ModuleType = 67
  ZeroPage = 2.656250000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 2
  object EditLabelNoteDetect: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelNoteDetectGate: TTextLabel
    Left = 142
    Top = 10
    Width = 20
    Height = 11
    Caption = 'Gate'
  end
  object TextLabelNoteDetectVel: TTextLabel
    Left = 184
    Top = 10
    Width = 13
    Height = 11
    Caption = 'Vel'
  end
  object TextLabelNoteDetectRel: TTextLabel
    Left = 222
    Top = 5
    Width = 14
    Height = 11
    Caption = 'Rel'
  end
  object TextLabelNoteDetectRelVel: TTextLabel
    Left = 222
    Top = 17
    Width = 13
    Height = 11
    Caption = 'Vel'
  end
  object OutputNoteDetectGate: TOutput
    Left = 165
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object OutputNoteDetectVel: TOutput
    Left = 202
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 1
  end
  object OutputNoteDetectRelVel: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 2
  end
  object SmallKnobNoteDetectNote: TSmallKnob
    Left = 112
    Top = 4
    Value = 60
    Display = DisplayNoteDetectNote
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 60
    Morphable = False
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object DisplayNoteDetectNote: TDisplay
    Left = 76
    Top = 8
    Width = 29
    Alignment = taCenter
    Caption = 'C4 '
    TabOrder = 1
    DisplayFormats.Formats = [dfNote]
    DisplayFormat = dfNote
    Clickable = False
    CtrlIndex = -1
  end
end
object T_4Outputs
  Hint = 'Version = 16'
  TitleLabel = EditLabel4Outputs
  Title = '4Outputs'
  Description = '4 outputs'
  ModuleAttributes = []
  ModuleType = 3
  Cycles = 2.062500000000000000
  ProgMem = 0.468750000000000000
  XMem = 0.093750000000000000
  DynMem = 0.281250000000000000
  UnitHeight = 3
  object EditLabel4Outputs: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object Input4Outputs1: TInput
    Left = 102
    Top = 23
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabel4OutputsMix1: TTextLabel
    Left = 96
    Top = 9
    Width = 20
    Height = 11
    Caption = 'Mix 1'
  end
  object TextLabel4OutputsLevel: TTextLabel
    Left = 221
    Top = 1
    Width = 22
    Height = 11
    Caption = 'Level'
  end
  object Input4Outputs2: TInput
    Left = 132
    Top = 23
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabel4OutputsMix2: TTextLabel
    Left = 125
    Top = 9
    Width = 22
    Height = 11
    Caption = 'Mix 2'
  end
  object Input4Outputs3: TInput
    Left = 166
    Top = 23
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 2
  end
  object TextLabel4OutputsMix3: TTextLabel
    Left = 160
    Top = 9
    Width = 22
    Height = 11
    Caption = 'Mix 3'
  end
  object Input4Outputs4: TInput
    Left = 196
    Top = 23
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 3
  end
  object TextLabel4OutputsMix4: TTextLabel
    Left = 189
    Top = 9
    Width = 22
    Height = 11
    Caption = 'Mix 4'
  end
  object Knob4OutputsLevel: TKnob
    Left = 220
    Top = 12
    Value = 115
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 115
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
end
object T_2Outputs
  Hint = 'Version = 16'
  TitleLabel = EditLabel2Output
  Title = '2Outputs'
  Description = '2 outputs'
  ModuleAttributes = []
  ModuleType = 4
  Cycles = 1.406250000000000000
  ProgMem = 0.343750000000000000
  XMem = 0.093750000000000000
  YMem = 0.093750000000000000
  DynMem = 0.375000000000000000
  UnitHeight = 3
  object EditLabel2Output: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabel2OutputsDest: TTextLabel
    Left = 0
    Top = 21
    Width = 20
    Height = 11
    Caption = 'Dest'
  end
  object Input2OutputsL: TInput
    Left = 166
    Top = 23
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabel2OutputsMixL: TTextLabel
    Left = 160
    Top = 9
    Width = 22
    Height = 11
    Caption = 'Mix L'
  end
  object TextLabel2OutputsLevel: TTextLabel
    Left = 221
    Top = 1
    Width = 22
    Height = 11
    Caption = 'Level'
  end
  object Input2OutputsR: TInput
    Left = 196
    Top = 23
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object TextLabel2OutputsMixR: TTextLabel
    Left = 189
    Top = 9
    Width = 24
    Height = 11
    Caption = 'Mix R'
  end
  object Knob2OutputsLevel: TKnob
    Left = 220
    Top = 12
    Value = 115
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 115
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSet2OutputsRouting: TButtonSet
    Left = 26
    Top = 21
    Width = 86
    Height = 14
    Value = 0
    DisplayFormat = dfChSelectOut2
    CtrlIndex = 1
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 1
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smRadio
    Clickers = <
      item
        Caption = '1/2'
      end
      item
        Caption = '3/4'
      end
      item
        Caption = 'CVA'
      end>
  end
  object ButtonSet2OutputsMute: TButtonSet
    Left = 132
    Top = 21
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
end
object T_1Output
  Hint = 'Version = 16'
  TitleLabel = EditLabel1Output
  Title = '1Output'
  Description = '1 output'
  ModuleAttributes = []
  ModuleType = 5
  Cycles = 1.281250000000000000
  ProgMem = 0.312500000000000000
  XMem = 0.093750000000000000
  YMem = 0.093750000000000000
  DynMem = 0.375000000000000000
  UnitHeight = 3
  object EditLabel1Output: TEditLabel
    Left = 1
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabel1OutputDest: TTextLabel
    Left = 0
    Top = 21
    Width = 20
    Height = 11
    Caption = 'Dest'
  end
  object Input1OutputIn: TInput
    Left = 196
    Top = 23
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabel1OutputMix: TTextLabel
    Left = 193
    Top = 9
    Width = 15
    Height = 11
    Caption = 'Mix'
  end
  object TextLabel1OutputLevel: TTextLabel
    Left = 221
    Top = 1
    Width = 22
    Height = 11
    Caption = 'Level'
  end
  object TextLabel1OutputCVA: TTextLabel
    Left = 110
    Top = 8
    Width = 42
    Height = 11
    Alignment = taCenter
    AutoSize = False
    Caption = 'CVA'
    Color = 11053224
    ParentColor = False
    Transparent = False
  end
  object Knob1OutputLevel: TKnob
    Left = 220
    Top = 12
    Value = 115
    DisplayFormat = dfUnsigned
    CtrlIndex = 0
    DefaultValue = 115
    Morphable = True
    Knobbable = True
    Controllable = True
    MinValue = 0
    MaxValue = 127
    StepSize = 1
  end
  object ButtonSet1OutputRouting: TButtonSet
    Left = 26
    Top = 21
    Width = 125
    Height = 14
    Value = 0
    DisplayFormat = dfChSelectOut1
    CtrlIndex = 1
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 1
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smRadio
    Clickers = <
      item
        Caption = '1'
      end
      item
        Caption = '2'
      end
      item
        Caption = '3'
      end
      item
        Caption = '4'
      end
      item
        Caption = 'L'
      end
      item
        Caption = 'R'
      end>
  end
  object ButtonSet1OutputMute: TButtonSet
    Left = 164
    Top = 21
    Width = 17
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 2
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = 'M'
      end>
  end
end
object T_PolyAreaIn
  Hint = 'Version = 16'
  TitleLabel = EditLabelPOlyAreaIn
  Title = 'PolyAreaIn'
  Description = 'Poly Area In'
  ModuleAttributes = [maSingleton, maCVAOnly]
  ModuleType = 127
  Cycles = 2.200000047683716000
  ProgMem = 1.000000000000000000
  XMem = 1.000000000000000000
  YMem = 1.000000000000000000
  ZeroPage = 2.000000000000000000
  DynMem = 1.000000000000000000
  UnitHeight = 2
  object BoxPolyAreaInLeft: TBox
    Left = 123
    Top = 6
    Width = 96
    Height = 7
    Shape = bsFrame
  end
  object BoxPolyAreaInRight: TBox
    Left = 123
    Top = 18
    Width = 96
    Height = 7
    Shape = bsFrame
  end
  object EditLabelPOlyAreaIn: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelPolyAreaInLeft: TTextLabel
    Left = 95
    Top = 3
    Width = 16
    Height = 11
    Caption = 'Left'
  end
  object IndicatorPolyAreaInLeft: TIndicator
    Left = 222
    Top = 7
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object OutputPolyAreaInLeft: TOutput
    Left = 240
    Top = 4
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelPolyAreaInRight: TTextLabel
    Left = 95
    Top = 15
    Width = 22
    Height = 11
    Caption = 'Right'
  end
  object IndicatorPolyAreaInRight: TIndicator
    Left = 222
    Top = 19
    Width = 9
    Height = 5
    CtrlIndex = 1
    Active = False
  end
  object OutputPolyAreaInRight: TOutput
    Left = 240
    Top = 16
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object LedBarPolyAreaInLeft: TLedBar
    Left = 126
    Top = 8
    Width = 89
    Height = 2
    Value = 0
    CtrlIndex = 0
  end
  object LedBarPolyAreaInRight: TLedBar
    Left = 126
    Top = 20
    Width = 89
    Height = 2
    Value = 0
    CtrlIndex = 1
  end
  object ButtonSetPolyAreaInLevel: TButtonSet
    Left = 4
    Top = 14
    Width = 33
    Height = 14
    Value = 0
    DisplayFormat = dfOnOff
    CtrlIndex = 0
    DefaultValue = 0
    Morphable = True
    Knobbable = True
    Controllable = True
    Margin = 0
    Separation = 0
    Orientation = soHorizontal
    Direction = diNormal
    SelectMode = smCheck
    Clickers = <
      item
        Caption = '+ 6dB'
      end>
  end
end
object T_AudioIn
  Hint = 'Version = 16'
  TitleLabel = EditLabelAudioIn
  Title = 'AudioIn'
  Description = 'Audio In'
  ModuleAttributes = []
  ModuleType = 2
  Cycles = 2.200000047683716000
  ProgMem = 1.000000000000000000
  XMem = 1.000000000000000000
  YMem = 1.000000000000000000
  ZeroPage = 2.000000000000000000
  DynMem = 1.000000000000000000
  UnitHeight = 2
  object BoxAudioInLeft: TBox
    Left = 123
    Top = 6
    Width = 96
    Height = 7
    Shape = bsFrame
  end
  object BoxAudioInRight: TBox
    Left = 123
    Top = 18
    Width = 96
    Height = 7
    Shape = bsFrame
  end
  object EditLabelAudioIn: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelAudioInLeft: TTextLabel
    Left = 95
    Top = 3
    Width = 16
    Height = 11
    Caption = 'Left'
  end
  object IndicatorAudioInLeft: TIndicator
    Left = 222
    Top = 7
    Width = 9
    Height = 5
    CtrlIndex = 0
    Active = False
  end
  object OutputAudioInLeft: TOutput
    Left = 240
    Top = 4
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 0
  end
  object TextLabelAudioInRight: TTextLabel
    Left = 95
    Top = 15
    Width = 22
    Height = 11
    Caption = 'Right'
  end
  object IndicatorAudioInRight: TIndicator
    Left = 222
    Top = 19
    Width = 9
    Height = 5
    CtrlIndex = 1
    Active = False
  end
  object OutputAudioInRight: TOutput
    Left = 240
    Top = 16
    Width = 10
    Height = 10
    WireColor = clRed
    CtrlIndex = 1
  end
  object LedBarAudioInLeft: TLedBar
    Left = 126
    Top = 8
    Width = 89
    Height = 2
    Value = 0
    CtrlIndex = 0
  end
  object LedBarAudioInRight: TLedBar
    Left = 126
    Top = 20
    Width = 89
    Height = 2
    Value = 0
    CtrlIndex = 1
  end
end
object T_MidiGlobal
  Hint = 'Version = 16'
  TitleLabel = EditLabelMidiGlobal
  Title = 'MidiGlobal'
  Description = 'MIDI - Global'
  ModuleAttributes = [maSingleton]
  ModuleType = 65
  Cycles = 0.129999995231628400
  ProgMem = 1.000000000000000000
  XMem = 1.000000000000000000
  ZeroPage = 1.000000000000000000
  DynMem = 1.000000000000000000
  UnitHeight = 2
  object EditLabelMidiGlobal: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelMidiGlobalClock: TTextLabel
    Left = 99
    Top = 10
    Width = 25
    Height = 11
    Caption = 'Clock'
  end
  object TextLabelMidiGlobalSync: TTextLabel
    Left = 158
    Top = 10
    Width = 20
    Height = 11
    Caption = 'Sync'
  end
  object TextLabelMidiGlobalActive: TTextLabel
    Left = 206
    Top = 10
    Width = 27
    Height = 11
    Caption = 'Active'
  end
  object OutputMidiGlobalClock: TOutput
    Left = 128
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 0
  end
  object OutputMidiGlobalSync: TOutput
    Left = 184
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object OutputMidiGlobalActive: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 2
  end
end
object T_KeyboardPatch
  Hint = 'Version = 16'
  TitleLabel = EditLabelKbdPatch
  Title = 'KbdPatch'
  Description = 'Keyboard - Patch'
  ModuleAttributes = [maSingleton]
  ModuleType = 63
  ZeroPage = 4.000000000000000000
  DynMem = 1.000000000000000000
  UnitHeight = 3
  object EditLabelKbdPatch: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelKbdPatchLatestNote: TTextLabel
    Left = 21
    Top = 14
    Width = 47
    Height = 11
    Caption = 'Latest note'
  end
  object TextLabelKbdPatchLatestGate: TTextLabel
    Left = 85
    Top = 14
    Width = 46
    Height = 11
    Caption = 'Latest gate'
  end
  object TextLabelKbdPatchLatestVelOn: TTextLabel
    Left = 159
    Top = 4
    Width = 26
    Height = 11
    Caption = 'Latest'
  end
  object TextLabelKbdPatchRelVel: TTextLabel
    Left = 222
    Top = 15
    Width = 28
    Height = 11
    Caption = 'Rel vel'
  end
  object OutputKbdPatchLatestNote: TOutput
    Left = 40
    Top = 28
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object OutputKbdPatchLatestGate: TOutput
    Left = 103
    Top = 28
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object OutputKbdPatchLatestVelOn: TOutput
    Left = 167
    Top = 28
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 2
  end
  object OutputKbdPatchLatestRelVel: TOutput
    Left = 231
    Top = 28
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 3
  end
  object TextLabelKbdPatchVelOn: TTextLabel
    Left = 159
    Top = 15
    Width = 25
    Height = 11
    Caption = 'vel on'
  end
  object TextLabelKbdPatchLatestRelVel: TTextLabel
    Left = 222
    Top = 4
    Width = 26
    Height = 11
    Caption = 'Latest'
  end
end
object T_Keyboard
  Hint = 'Version = 16'
  TitleLabel = EditLabelKeyBoard
  Title = 'Keyboard'
  Description = 'Keyboard - voice'
  ModuleAttributes = [maSingleton]
  ModuleType = 1
  ZeroPage = 3.000000000000000000
  DynMem = 1.000000000000000000
  UnitHeight = 2
  object EditLabelKeyBoard: TEditLabel
    Left = 0
    Top = 0
    Width = 85
    Height = 13
  end
  object TextLabelKeyBoardNote: TTextLabel
    Left = 104
    Top = 10
    Width = 21
    Height = 11
    Caption = 'Note'
  end
  object TextLabelKeyBoardGate: TTextLabel
    Left = 142
    Top = 10
    Width = 20
    Height = 11
    Caption = 'Gate'
  end
  object TextLabelKeyBoardVel: TTextLabel
    Left = 184
    Top = 10
    Width = 13
    Height = 11
    Caption = 'Vel'
  end
  object TextLabelKeyBoardRel: TTextLabel
    Left = 222
    Top = 5
    Width = 14
    Height = 11
    Caption = 'Rel'
  end
  object TextLabelKeyBoardRelVel: TTextLabel
    Left = 222
    Top = 15
    Width = 13
    Height = 11
    Caption = 'Vel'
  end
  object OutputKeyBoardNote: TOutput
    Left = 128
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 0
  end
  object OutputKeyBoardGate: TOutput
    Left = 165
    Top = 11
    Width = 10
    Height = 10
    WireColor = clYellow
    CtrlIndex = 1
  end
  object OutputKeyBoardVel: TOutput
    Left = 202
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 2
  end
  object OutputKeyBoardRelVel: TOutput
    Left = 240
    Top = 11
    Width = 10
    Height = 10
    WireColor = clBlue
    CtrlIndex = 3
  end
end
