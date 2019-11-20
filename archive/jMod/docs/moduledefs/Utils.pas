unit utils;

interface

Uses

  SysUtils, Classes, Windows, Controls, Forms, Graphics, TypInfo,
  Math, MathStuff;

Type

  TDisplayFormat = (
    dfUnsigned,
    dfSigned,
    dfOffset1,
    dfNote,
    dfOscHz,
    dfFilterHz1,
    dfFilterHz2,
    dfLfoHz,
    dfEqHz,
    dfSemitones,
    dfPartials,
    dfBPM,
    dfADSRTime,
    dfVowel,
    dfPhase,
    dfDrumPreset,
    dfEnvAttack,
    dfEnvRelease,
    dfDrumHz,
    dfDrumPartials,
    dfFmtTimbre,
    dfAmpGain,
    dfQuantizerBits,
    dfDelayTime,
    dfPhaserHz,
    dfDigitizerHz,
    dfCompanderRelease,
    dfCompressorTresh,
    dfExpanderTresh,
    dfCompanderRatio,
    dfCompressorRefLvl,
    dfCompressorLimiter,
    dfExpanderGate,
    dfExpanderHold,
    dfSmoothTime,          // Diliberatly has thes Same 'bug' as the Clavia editor has
    dfNoteScale,
    dfNoteRange,
    dfPartialRange,
    dfNotesRange,
    dfVelScalGain,
    dfLogicDelay,
    dfChSelectOut1,
    dfChSelectOut2,
    dfOscWave,
    dfFilterType,
    dfLFOWave,
    dfLFORange,
    dfADSREnvShape,
    dfMultiEnvShape,
    dfPartialsGroup,
    dfFilter2Type,
    dfFilter1Slope,
    dfFilter2Slope,
    dfEqShelvingType,
    dfOnOff,
    dfLogicFunc,
    dfDiodeFunc,
    dfLevelFunc,
    dfShaperFunc,
    dfCtrlMixerMode
  );

  // Some trickery needed as a published set property must be represenatable
  // within 32 bits. Set Of TDisplayFormat requieres more, hence a class
  // wrapper TDisplayFormats was created for it that can read and write a
  // set from / to a stream in a way as if it were a Set type, and yet
  // it is still editable in the Object Inspector without writing a
  // specialized editor for it - which was troublesome to achieve.

  TInternalDisplayFormats = Set Of TDisplayFormat;

  TDisplayFormats = Class( TPersistent)
  Private
    FFormats : TInternalDisplayFormats;
  Private
    Procedure   WriteFormats( aWriter: TWriter);
    Procedure   ReadFormats ( aReader: TReader);
    Function    GetFormat( anIndex: TDisplayFormat): Boolean;
    Procedure   SetFormat( anIndex: TDisplayFormat; aValue: Boolean);
  Protected
    Procedure   DefineProperties( aFiler: TFiler);                     Override;
  Public
    Property    Formats: TInternalDisplayFormats Read FFormats Write FFormats;
  Published
    // Do not store the following prop's, but do make 'm published.
    // Storage is arranged for in a set compatible way, see :
    // WriteFormats, ReadFormats and DefineProperties for this.
    // The properties below are used in the Object Inspector so there is
    // no need to write a custom set property editor.
    Property    Unsigned          : Boolean Index dfUnsigned          Read GetFormat Write SetFormat Stored False;
    Property    Signed            : Boolean Index dfSigned            Read GetFormat Write SetFormat Stored False;
    Property    Offset1           : Boolean Index dfOffset1           Read GetFormat Write SetFormat Stored False;
    Property    Note              : Boolean Index dfNote              Read GetFormat Write SetFormat Stored False;
    Property    OscHz             : Boolean Index dfOscHz             Read GetFormat Write SetFormat Stored False;
    Property    FilterHz1         : Boolean Index dfFilterHz1         Read GetFormat Write SetFormat Stored False;
    Property    FilterHz2         : Boolean Index dfFilterHz2         Read GetFormat Write SetFormat Stored False;
    Property    LfoHz             : Boolean Index dfLfoHz             Read GetFormat Write SetFormat Stored False;
    Property    EqHz              : Boolean Index dfEqHz              Read GetFormat Write SetFormat Stored False;
    Property    Semitones         : Boolean Index dfSemitones         Read GetFormat Write SetFormat Stored False;
    Property    Partials          : Boolean Index dfPartials          Read GetFormat Write SetFormat Stored False;
    Property    BPM               : Boolean Index dfBPM               Read GetFormat Write SetFormat Stored False;
    Property    ADSRTime          : Boolean Index dfADSRTime          Read GetFormat Write SetFormat Stored False;
    Property    Vowel             : Boolean Index dfVowel             Read GetFormat Write SetFormat Stored False;
    Property    Phase             : Boolean Index dfPhase             Read GetFormat Write SetFormat Stored False;
    Property    DrumPreset        : Boolean Index dfDrumPreset        Read GetFormat Write SetFormat Stored False;
    Property    EnvAttack         : Boolean Index dfEnvAttack         Read GetFormat Write SetFormat Stored False;
    Property    EnvRelease        : Boolean Index dfEnvRelease        Read GetFormat Write SetFormat Stored False;
    Property    DrumHz            : Boolean Index dfDrumHz            Read GetFormat Write SetFormat Stored False;
    Property    DrumPartials      : Boolean Index dfDrumPartials      Read GetFormat Write SetFormat Stored False;
    Property    FmtTimbre         : Boolean Index dfFmtTimbre         Read GetFormat Write SetFormat Stored False;
    Property    AmpGain           : Boolean Index dfAmpGain           Read GetFormat Write SetFormat Stored False;
    Property    QuantizerBits     : Boolean Index dfQuantizerBits     Read GetFormat Write SetFormat Stored False;
    Property    DelayTime         : Boolean Index dfDelayTime         Read GetFormat Write SetFormat Stored False;
    Property    PhaserHz          : Boolean Index dfPhaserHz          Read GetFormat Write SetFormat Stored False;
    Property    DigitizerHz       : Boolean Index dfDigitizerHz       Read GetFormat Write SetFormat Stored False;
    Property    CompanderRelease  : Boolean Index dfCompanderRelease  Read GetFormat Write SetFormat Stored False;
    Property    CompressorTresh   : Boolean Index dfCompressorTresh   Read GetFormat Write SetFormat Stored False;
    Property    ExpanderTresh     : Boolean Index dfExpanderTresh     Read GetFormat Write SetFormat Stored False;
    Property    CompanderRatio    : Boolean Index dfCompanderRatio    Read GetFormat Write SetFormat Stored False;
    Property    CompressorRefLvl  : Boolean Index dfCompressorRefLvl  Read GetFormat Write SetFormat Stored False;
    Property    CompressorLimiter : Boolean Index dfCompressorLimiter Read GetFormat Write SetFormat Stored False;
    Property    ExpanderGate      : Boolean Index dfExpanderGate      Read GetFormat Write SetFormat Stored False;
    Property    EpanderHold       : Boolean Index dfExpanderHold      Read GetFormat Write SetFormat Stored False;
    Property    SmoothTime        : Boolean Index dfSmoothTime        Read GetFormat Write SetFormat Stored False;
    Property    NoteScale         : Boolean Index dfNoteScale         Read GetFormat Write SetFormat Stored False;
    Property    NoteRange         : Boolean Index dfNoteRange         Read GetFormat Write SetFormat Stored False;
    Property    PartialRange      : Boolean Index dfPartialRange      Read GetFormat Write SetFormat Stored False;
    Property    NotesRange        : Boolean Index dfNotesRange        Read GetFormat Write SetFormat Stored False;
    Property    VelScalGain       : Boolean Index dfVelScalGain       Read GetFormat Write SetFormat Stored False;
    Property    LogicDelay        : Boolean Index dfLogicDelay        Read GetFormat Write SetFormat Stored False;
    Property    ChSelectOut1      : Boolean Index dfChSelectOut1      Read GetFormat Write SetFormat Stored False;
    Property    ChSelectOut2      : Boolean Index dfChSelectOut2      Read GetFormat Write SetFormat Stored False;
    Property    OscWave           : Boolean Index dfOscWave           Read GetFormat Write SetFormat Stored False;
    Property    FilterType        : Boolean Index dfFilterType        Read GetFormat Write SetFormat Stored False;
    Property    LFOWave           : Boolean Index dfLFOWave           Read GetFormat Write SetFormat Stored False;
    Property    LFORange          : Boolean Index dfLFORange          Read GetFormat Write SetFormat Stored False;
    Property    ADSREnvShape      : Boolean Index dfADSREnvShape      Read GetFormat Write SetFormat Stored False;
    Property    MultiEnvShape     : Boolean Index dfMultiEnvShape     Read GetFormat Write SetFormat Stored False;
    Property    PartialsGroup     : Boolean Index dfPartialsGroup     Read GetFormat Write SetFormat Stored False;
    Property    Filter2Type       : Boolean Index dfFilter2Type       Read GetFormat Write SetFormat Stored False;
    Property    Filter1Slope      : Boolean Index dfFilter1Slope      Read GetFormat Write SetFormat Stored False;
    Property    Filter2Slope      : Boolean Index dfFilter2Slope      Read GetFormat Write SetFormat Stored False;
    Property    EqShelvingType    : Boolean Index dfEqShelvingType    Read GetFormat Write SetFormat Stored False;
    Property    OnOff             : Boolean Index dfOnOff             Read GetFormat Write SetFormat Stored False;
    Property    LogicFunc         : Boolean Index dfLogicFunc         Read GetFormat Write SetFormat Stored False;
    Property    DiodeFunc         : Boolean Index dfDiodeFunc         Read GetFormat Write SetFormat Stored False;
    Property    LevelFunc         : Boolean Index dfLevelFunc         Read GetFormat Write SetFormat Stored False;
    Property    ShaperFunc        : Boolean Index dfShaperFunc        Read GetFormat Write SetFormat Stored False;
    Property    CtrlMixerMode     : Boolean Index dfCtrlMixerMode     Read GetFormat Write SetFormat Stored False;
  End;

  Function  ValueToDisplayStr( aValue: TValue; aFormat: TDisplayFormat): String;



implementation


{ ========
  TDisplayFormats = Class( TPersistent)
  Private
    FFormats : TInternalDisplayFormats;
  Public
    Property    Formats: TInternalDisplayFormats Read FFormats Write FFormats;
  Published
    Property    Unsigned          : Boolean Index dfUnsigned          Read GetFormat Write SetFormat Stored False;
    Property    Signed            : Boolean Index dfSigned            Read GetFormat Write SetFormat Stored False;
    Property    Offset1           : Boolean Index dfOffset1           Read GetFormat Write SetFormat Stored False;
    Property    Note              : Boolean Index dfNote              Read GetFormat Write SetFormat Stored False;
    Property    OscHz             : Boolean Index dfOscHz             Read GetFormat Write SetFormat Stored False;
    Property    FilterHz1         : Boolean Index dfFilterHz1         Read GetFormat Write SetFormat Stored False;
    Property    FilterHz2         : Boolean Index dfFilterHz2         Read GetFormat Write SetFormat Stored False;
    Property    LfoHz             : Boolean Index dfLfoHz             Read GetFormat Write SetFormat Stored False;
    Property    EqHz              : Boolean Index dfEqHz              Read GetFormat Write SetFormat Stored False;
    Property    Semitones         : Boolean Index dfSemitones         Read GetFormat Write SetFormat Stored False;
    Property    Partials          : Boolean Index dfPartials          Read GetFormat Write SetFormat Stored False;
    Property    BPM               : Boolean Index dfBPM               Read GetFormat Write SetFormat Stored False;
    Property    ADSRTime          : Boolean Index dfADSRTime          Read GetFormat Write SetFormat Stored False;
    Property    Vowel             : Boolean Index dfVowel             Read GetFormat Write SetFormat Stored False;
    Property    Phase             : Boolean Index dfPhase             Read GetFormat Write SetFormat Stored False;
    Property    DrumPreset        : Boolean Index dfDrumPreset        Read GetFormat Write SetFormat Stored False;
    Property    EnvAttack         : Boolean Index dfEnvAttack         Read GetFormat Write SetFormat Stored False;
    Property    EnvRelease        : Boolean Index dfEnvRelease        Read GetFormat Write SetFormat Stored False;
    Property    DrumHz            : Boolean Index dfDrumHz            Read GetFormat Write SetFormat Stored False;
    Property    DrumPartials      : Boolean Index dfDrumPartials      Read GetFormat Write SetFormat Stored False;
    Property    FmtTimbre         : Boolean Index dfFmtTimbre         Read GetFormat Write SetFormat Stored False;
    Property    AmpGain           : Boolean Index dfAmpGain           Read GetFormat Write SetFormat Stored False;
    Property    QuantizerBits     : Boolean Index dfQuantizerBits     Read GetFormat Write SetFormat Stored False;
    Property    DelayTime         : Boolean Index dfDelayTime         Read GetFormat Write SetFormat Stored False;
    Property    PhaserHz          : Boolean Index dfPhaserHz          Read GetFormat Write SetFormat Stored False;
    Property    DigitizerHz       : Boolean Index dfDigitizerHz       Read GetFormat Write SetFormat Stored False;
    Property    CompanderRelease  : Boolean Index dfCompanderRelease  Read GetFormat Write SetFormat Stored False;
    Property    CompressorTresh   : Boolean Index dfCompressorTresh   Read GetFormat Write SetFormat Stored False;
    Property    ExpanderTresh     : Boolean Index dfExpanderTresh     Read GetFormat Write SetFormat Stored False;
    Property    CompanderRatio    : Boolean Index dfCompanderRatio    Read GetFormat Write SetFormat Stored False;
    Property    CompressorRefLvl  : Boolean Index dfCompressorRefLvl  Read GetFormat Write SetFormat Stored False;
    Property    CompressorLimiter : Boolean Index dfCompressorLimiter Read GetFormat Write SetFormat Stored False;
    Property    ExpanderGate      : Boolean Index dfExpanderGate      Read GetFormat Write SetFormat Stored False;
    Property    EpanderHold       : Boolean Index dfExpanderHold      Read GetFormat Write SetFormat Stored False;
  Private
}

    Procedure   TDisplayFormats.WriteFormats( aWriter: TWriter);

      Procedure WriteValue( aValue: TValueType);
      Begin
        aWriter.Write( aValue, SizeOf( aValue));
      End;

    var
      i  : TDisplayFormat;
    begin
      WriteValue( vaSet);
      For i := Low( TDisplayFormat) To High ( TDisplayFormat) Do
        If i In FFormats
        Then aWriter.WriteStr( GetEnumName( TypeInfo( TDisplayFormat), Integer( i)));
      aWriter.WriteStr( '');
    End;

    Procedure   TDisplayFormats.ReadFormats( aReader: TReader);
    var
      anEltName : String;
      anInt     : Integer;
    begin
      try
        If aReader.ReadValue <> vaSet
        Then
          Raise
            EReadError.
              Create(
                'Error reading resource TDisplayFormats.Formats (set type expected)'
              );
        FFormats := [];
        While True do
        Begin
          anEltName := aReader.ReadStr;
          If anEltName = ''
          Then Break;
          anInt := GetEnumValue( TypeInfo( TDisplayFormat), anEltName);
          If anInt = -1
          Then
            Raise
              EReadError.
                CreateFmt(
                  'Undefined TDisplayFormat (%s) encounterd on reading TDisplayFormats.Formats',
                  [ anEltName]
                );
          FFormats := FFormats + [ TDisplayFormat( anInt)];
        End;
      Except
        While aReader.ReadStr <> '' Do // Skip unread items
          ;
        Raise;
      End;
    End;

    Function    TDisplayFormats.GetFormat( anIndex: TDisplayFormat) : Boolean;
    Begin
      Result := anIndex In FFormats;
    End;

    Procedure   TDisplayFormats.SetFormat( anIndex: TDisplayFormat; aValue: Boolean);
    Begin
      If aValue
      Then FFormats := FFormats + [ anIndex]
      Else FFormats := FFormats - [ anIndex];
    End;

//   Protected

    Procedure   TDisplayFormats.DefineProperties( aFiler: TFiler); // Override;

      Function DoWrite : Boolean;
      Begin
        Result := FFormats <> [];
      End;

    Begin
      Inherited DefineProperties( aFiler);
      aFiler.DefineProperty( 'Formats', ReadFormats, WriteFormats, DoWrite);
    End;




  // //////////////////////////////////////////////////////////////////////////

  Function  ValueToNoteStr( aValue: TValue): String;
  Const
    Notes  : Array[ 0 .. 11] Of Char = 'CCDDEFFGGAAB';
    Sharps : Array[ 0 .. 11] Of Char = ' # #  # # # ';
  Var
    i : Integer;
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      i := aValue Mod 12;
      Result := Format( '%s%d%s', [ Notes[ i], ( aValue Div 12) - 1, Sharps[ i]]);
    End;
  End;

  Function  ValueToOscHzStr( aValue: TValue): String;
  Var
    aFloat : Extended;
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      aFloat := 440 * Power( 2, ( aValue - 69) / 12);
      If aFloat < 10
      Then Result := Format( '%.2f Hz', [ aFloat])
      Else If aFloat < 100
      Then Result := Format( '%.1f Hz', [ aFloat])
      Else If aFloat < 1000
      Then Result := Format( '%.0f Hz', [ aFloat])
      Else If aFloat < 10000
      Then Result := Format( '%.2f kHz', [ aFloat / 1000])
      Else Result := Format( '%.2f kHz', [ aFloat / 1000]);
    End;
  End;

  Function  ValueToLFOHzStr( aValue: TValue): String;
  Var
    aFloat : Extended;
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      aFloat := 440 * Power( 2, ( aValue - 177) / 12);
      If aFloat < 0.1
      Then Result := Format( '%.1f s', [ 1 / aFloat])
      Else If aFloat < 10
      Then Result := Format( '%.2f Hz', [ aFloat])
      Else If aFloat < 100
      Then Result := Format( '%.1f Hz', [ aFloat])
      Else Result := Format( '%.0f Hz', [ aFloat]);
    End;
  End;

  Function  ValueToFilterHz1Str( aValue: TValue): String;
  // @@@@ This is not quite correct yet
  Var
    aFloat : Extended;
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      aFloat := 504 * Power( 2, ( aValue - 64) / 12);
      If aFloat < 1000
      Then Result := Format( '%.0f Hz', [ aFloat])
      Else If aFloat < 10000
      Then Result := Format( '%.2f kHz', [ aFloat / 1000])
      Else Result := Format( '%.1f kHz', [ aFloat / 1000]);
    End;
  End;

  Function  ValueToFilterHz2Str( aValue: TValue): String;
  Var
    aFloat : Extended;
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      aFloat := 330 * Power( 2, ( aValue - 60) / 12);
      If aFloat < 1000
      Then Result := Format( '%.0f Hz', [ aFloat])
      Else If aFloat < 10000
      Then Result := Format( '%.2f kHz', [ aFloat / 1000])
      Else Result := Format( '%.1f kHz', [ aFloat / 1000]);
    End;
  End;


  Function  ValueToEqHzStr( aValue: TValue): String;
  // @@@@ This is nfot quite correct yet
  Var
    aFloat : Extended;
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      aFloat := 471 * Power( 2, ( aValue - 60) / 12);
      If aFloat < 1000
      Then Result := Format( '%.0f Hz', [ aFloat])
      Else If aFloat < 10000
      Then Result := Format( '%.2f kHz', [ aFloat / 1000])
      Else Result := Format( '%.1f kHz', [ aFloat / 1000]);
    End;
  End;

  Function  ValueToSemiTonesStr( aValue: TValue): String;
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      aValue := aValue - 64;
      Result := IntToStr( aValue);
      Case ( aValue + 72) Mod 12 Of
         0 : Result := Result + '(Oct)';
         7 : Result := Result + '(5th)';
        10 : Result := Result + '(7th)';
      End;
    End;
  End;

  Function  ValueToPartialStr( aValue: TValue): String;
  Const
    Fractions : Array[ 0 .. 10] Of String = (
      '1:32',
      '1:16',
      '1:8',
      '1:4',
      '1:2',
      '1:1',
      '2:1',
      '4:1',
      '8:1',
      '16:1',
      '32:1'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      If ( aValue + 8) Mod 12 = 0
      Then Result := Fractions[ aValue Div 12]
      Else Result := Format( 'x%.3f', [ Power( 2, ( aValue - 64) / 12)]);
    End;
  End;

  Function  ValueToBPMStr( aValue: TValue): String;
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      If aValue <= 32
      Then aValue := 2 * aValue + 24
      Else If aValue <= 96
      Then aValue :=     aValue + 56
      Else aValue := 2 * aValue - 40;
      Result := IntToStr( aValue);
    End;
  End;

  Function  ValueToADSRTimeStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 127] Of String = (
      '0.5m' , '0.7m' , '1.0m' , '1.3m' , '1.5m' , '1.8m' , '2.1m' , '2.3m' ,
      '2.6m' , '2.9m' , '3.2m' , '3.5m' , '3.9m' , '4.2m' , '4.6m' , '4.9m' ,
      '5.3m' , '5.7m' , '6.1m' , '6.6m' , '7.0m' , '7.5m' , '8.0m' , '8.5m' ,
      '9.1m' , '9.7m' , '10m'  , '11m'  , '12m'  , '13m'  , '13m'  , '14m'  ,
      '15m'  , '16m'  , '17m'  , '19m'  , '20m'  , '21m'  , '23m'  , '24m'  ,
      '26m'  , '28m'  , '30m'  , '32m'  , '35m'  , '37m'  , '40m'  , '43m'  ,
      '47m'  , '51m'  , '55m'  , '59m'  , '64m'  , '69m'  , '75m'  , '81m'  ,
      '88m'  , '95m'  , '103m' , '112m' , '122m' , '132m' , '143m' , '156m' ,
      '170m' , '185m' , '201m' , '219m' , '238m' , '260m' , '283m' , '308m' ,
      '336m' , '367m' , '400m' , '436m' , '476m' , '520m' , '567m' , '619m' ,
      '676m' , '738m' , '806m' , '881m' , '962m' , '1.1s' , '1.1s' , '1.3s' ,
      '1.4s' , '1.5s' , '1.6s' , '1.8s' , '2.0s' , '2.1s' , '2.3s' , '2.6s' ,
      '2.8s' , '3.1s' , '3.3s' , '3.7s' , '4.0s' , '4.4s' , '4.8s' , '5.2s' ,
      '5.7s' , '6.3s' , '6.8s' , '7.5s' , '8.2s' , '9.0s' , '9.8s' , '10.7s',
      '11.7s', '12.8s', '14.0s', '15.3s', '16.8s', '18.3s', '20.1s', '21.9s',
      '24.0s', '26.3s', '28.7s', '31.4s', '34.4s', '37.6s', '41.1s', '45.0s'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function  ValueToVowelStr( aValue: TValue): String;
  Begin
    Case aValue Of
      0  : Result := 'A';
      1  : Result := 'E';
      2  : Result := 'I';
      3  : Result := 'O';
      4  : Result := 'U';
      5  : Result := 'Y';
      6  : Result := 'AA';
      7  : Result := 'AE';
      8  : Result := 'OE';
      Else Result := 'inv';
    End;
  End;

  Function  ValueToPhaseStr( aValue: TValue): String;
  Begin
    Result := IntToStr( Round( aValue * 2.8125 - 180.0));
  End;

  Function  ValueToDrumPresetStr( aValue: TValue): String;
  Begin
    Case aValue Of
       0 : Result := 'none';
       1 : Result := 'Kick 1';
       2 : Result := 'Kick 2';
       3 : Result := 'Kick 3';
       4 : Result := 'Kick 4';
       5 : Result := 'Kick 5';
       6 : Result := 'Snare 1';
       7 : Result := 'Snare 2';
       8 : Result := 'Snare 3';
       9 : Result := 'Snare 4';
      10 : Result := 'Snare 5';
      11 : Result := 'Tom1 1';
      12 : Result := 'Tom1 2';
      13 : Result := 'Tom1 3';
      14 : Result := 'Tom2 1';
      15 : Result := 'Tom2 2';
      16 : Result := 'Tom2 3';
      17 : Result := 'Tom3 1';
      18 : Result := 'Tom3 2';
      19 : Result := 'Tom3 3';
      20 : Result := 'Cymb 1';
      21 : Result := 'Cymb 2';
      22 : Result := 'Cymb 3';
      23 : Result := 'Cymb 4';
      24 : Result := 'Cymb 5';
      25 : Result := 'Perc 1';
      26 : Result := 'Perc 2';
      27 : Result := 'Perc 3';
      28 : Result := 'Perc 4';
      29 : Result := 'Perc 5';
      30 : Result := 'Perc 6';
      Else Result := 'inv';
    End;
  End;

  Function  ValueToEnvAttackStr ( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 127] Of String = (
      'Fast' , '0.53m', '0.56m', '0.59m', '0.63m', '0.67m', '0.71m', '0.75m',
      '0.79m', '0.84m', '0.89m', '0.94m', '1.00m', '1.06m', '1.12m', '1.19m',
      '1.26m', '1.33m', '1.41m', '1.50m', '1.59m', '1.68m', '1.78m', '1.89m',
      '2.00m', '2.12m', '2.24m', '2.38m', '2.52m', '2.67m', '2.83m', '3.00m',
      '3.17m', '3.36m', '3.56m', '3.78m', '4.00m', '4.24m', '4.49m', '4.76m',
      '5.04m', '5.34m', '5.66m', '5.99m', '6.35m', '6.73m', '7.13m', '7.55m',
      '8.00m', '8.48m', '8.98m', '9.51m', '10.1m', '10.7m', '11.3m', '12.0m',
      '12.7m', '13.5m', '14.3m', '15.1m', '16.0m', '17.0m', '18.0m', '19.0m',
      '20.2m', '21.4m', '22.6m', '24.0m', '25.4m', '26.9m', '28.5m', '30.2m',
      '32.0m', '33.9m', '35.9m', '38.1m', '40.3m', '42.7m', '45.3m', '47.9m',
      '50.8m', '53.8m', '57.0m', '60.4m', '64.0m', '67.8m', '71.8m', '76.1m',
      '80.6m', '85.4m', '90.5m', '95.9m', '102m' , '108m' , '114m' , '121m' ,
      '128m' , '136m' , '144m' , '152m' , '161m' , '171m' , '181m' , '192m' ,
      '203m' , '215m' , '228m' , '242m' , '256m' , '271m' , '287m' , '304m' ,
      '323m' , '342m' , '362m' , '384m' , '406m' , '431m' , '456m' , '483m' ,
      '512m' , '542m' , '575m' , '609m' , '645m' , '683m' , '724m' , '767m'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function  ValueToEnvReleaseStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 127] Of String = (
      'Fast' , '41.4m', '42.9m', '44.4m', '45.9m', '47.6m', '49.2m', '51.0m',
      '52.8m', '54.6m', '56.6m', '58.6m', '60.6m', '62.8m', '65.0m', '67.3m',
      '69.6m', '72.1m', '74.6m', '77.3m', '80.0m', '82.8m', '85.7m', '88.8m',
      '91.9m', '95.1m', '98.5m', '102m' , '106m' , '109m' , '113m' , '117m' ,
      '121m' , '126m' , '130m' , '135m' , '139m' , '144m' , '149m' , '155m' ,
      '160m' , '166m' , '171m' , '178m' , '184m' , '190m' , '197m' , '204m' ,
      '211m' , '219m' , '226m' , '234m' , '243m' , '251m' , '260m' , '269m' ,
      '279m' , '288m' , '299m' , '309m' , '320m' , '331m' , '343m' , '355m' ,
      '368m' , '381m' , '394m' , '408m' , '422m' , '437m' , '453m' , '469m' ,
      '485m' , '502m' , '520m' , '538m' , '557m' , '577m' , '597m' , '618m' ,
      '640m' , '663m' , '686m' , '710m' , '735m' , '761m' , '788m' , '816m' ,
      '844m' , '874m' , '905m' , '937m' , '970m' , '1.00s', '1.04s', '1.08s',
      '1.11s', '1.15s', '1.19s', '1.24s', '1.28s', '1.33s', '1.37s', '1.42s',
      '1.47s', '1.52s', '1.58s', '1.63s', '1.69s', '1.75s', '1.81s', '1.87s',
      '1.94s', '2.01s', '2.08s', '2.15s', '2.23s', '2.31s', '2.39s', '2.47s',
      '2.56s', '2.65s', '2.74s', '2.84s', '2.94s', '3.04s', '3.15s', '3.26s'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function  ValueToDrumHzStr( aValue: TValue): String;
  Var
    aFloat : Extended;
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      aFloat := 20 * Power( 2, aValue / 24);
      If aFloat < 100
      Then Result := Format( '%.1f Hz', [ aFloat])
      Else Result := Format( '%.0f Hz', [ aFloat]);
    End;
  End;

  Function  ValueToDrumPartialsStr( aValue: TValue): String;
  Const
    Fractions : Array[ 0 .. 2] Of String = (
      '1:1',
      '2:1',
      '4:1'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      If aValue Mod 48 = 0
      Then Result := Fractions[ aValue Div 48]
      Else Result := Format( 'x%.2f', [ Power( 2, aValue / 48)]);
    End;
  End;

  Function  ValueToFmtTimbreStr( aValue: TValue): String;
  Begin
    Case aValue Of
      0 .. 126 : Result := IntToStr( aValue + 1);
      127      : Result := 'Rnd';
      Else       Result := 'inv';
    End;
  End;

  Function  ValueToAmpGainStr( aValue: TValue): String;
  // @@@@ This is not quite correct yet
  Var
    aFloat : Extended;
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      If aValue = 63
      Then aValue := 64;
      aFloat := 0.25 * Power( 2, aValue / 32);
      Result := Format( 'x%.2f', [ aFloat]);
    End;
  End;

  Function  ValueToQuantizerBitsStr( aValue: TValue): String;
  Begin
    If ( aValue < 0) Or ( aValue > 12)
    Then Result := 'inv'
    Else
      If aValue = 12
      Then Result := 'Off'
      Else Result := Format( '%d', [ aValue + 1]);
  End;

  Function  ValueToDelayTimeStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 127] Of String = (
      '0.00ms', '0.02ms', '0.04ms', '0.06ms', '0.08ms', '0.10ms', '0.13ms', '0.15ms',
      '0.17ms', '0.19ms', '0.21ms', '0.23ms', '0.25ms', '0.27ms', '0.29ms', '0.31ms',
      '0.33ms', '0.35ms', '0.38ms', '0.40ms', '0.42ms', '0.44ms', '0.46ms', '0.48ms',
      '0.50ms', '0.52ms', '0.54ms', '0.56ms', '0.58ms', '0.60ms', '0.63ms', '0.65ms',
      '0.67ms', '0.69ms', '0.71ms', '0.73ms', '0.75ms', '0.77ms', '0.79ms', '0.81ms',
      '0.83ms', '0.85ms', '0.88ms', '0.90ms', '0.92ms', '0.94ms', '0.96ms', '0.98ms',
      '1.00ms', '1.02ms', '1.04ms', '1.06ms', '1.08ms', '1.10ms', '1.13ms', '1.15ms',
      '1.17ms', '1.19ms', '1.21ms', '1.23ms', '1.25ms', '1.27ms', '1.29ms', '1.31ms',
      '1.33ms', '1.35ms', '1.38ms', '1.40ms', '1.42ms', '1.44ms', '1.46ms', '1.48ms',
      '1.50ms', '1.52ms', '1.54ms', '1.56ms', '1.58ms', '1.60ms', '1.63ms', '1.65ms',
      '1.67ms', '1.69ms', '1.71ms', '1.73ms', '1.75ms', '1.77ms', '1.79ms', '1.81ms',
      '1.83ms', '1.85ms', '1.88ms', '1.90ms', '1.92ms', '1.94ms', '1.96ms', '1.98ms',
      '2.00ms', '2.02ms', '2.04ms', '2.06ms', '2.08ms', '2.10ms', '2.13ms', '2.15ms',
      '2.17ms', '2.19ms', '2.21ms', '2.23ms', '2.25ms', '2.27ms', '2.29ms', '2.31ms',
      '2.33ms', '2.35ms', '2.38ms', '2.40ms', '2.42ms', '2.44ms', '2.46ms', '2.48ms',
      '2.50ms', '2.52ms', '2.54ms', '2.56ms', '2.58ms', '2.60ms', '2.63ms', '2.65ms'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function  ValueToPhaserHzStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 127] Of String = (
      '100Hz'  , '104Hz'  , '108Hz'  , '113Hz'  , '117Hz'  , '122Hz'  , '127Hz'  , '132Hz'  ,
      '138Hz'  , '143Hz'  , '149Hz'  , '155Hz'  , '162Hz'  , '168Hz'  , '175Hz'  , '182Hz'  ,
      '190Hz'  , '197Hz'  , '205Hz'  , '214Hz'  , '222Hz'  , '231Hz'  , '241Hz'  , '251Hz'  ,
      '261Hz'  , '272Hz'  , '283Hz'  , '294Hz'  , '306Hz'  , '319Hz'  , '332Hz'  , '345Hz'  ,
      '359Hz'  , '374Hz'  , '389Hz'  , '405Hz'  , '421Hz'  , '439Hz'  , '457Hz'  , '475Hz'  ,
      '495Hz'  , '515Hz'  , '536Hz'  , '558Hz'  , '580Hz'  , '604Hz'  , '629Hz'  , '654Hz'  ,
      '681Hz'  , '709Hz'  , '738Hz'  , '768Hz'  , '799Hz'  , '831Hz'  , '865Hz'  , '901Hz'  ,
      '937Hz'  , '976Hz'  , '1.02kHz', '1.06kHz', '1.10kHz', '1.14kHz', '1.19kHz', '1.24kHz',
      '1.29kHz', '1.34kHz', '1.40kHz', '1.45kHz', '1.51kHz', '1.58khz', '1.64kHz', '1.71kHz',
      '1.78kHz', '1.85kHz', '1.92kHz', '2.00kHz', '2.08kHz', '2.17kHz', '2.26kHz', '2.35kHz',
      '2.45kHz', '2.55kHz', '2.65kHz', '2.76kHz', '2.87kHz', '2.99kHz', '3.11kHz', '3.24kHz',
      '3.37kHz', '3.50kHz', '3.65kHz', '3.80kHz', '3.95kHz', '4.11kHz', '4.28kHz', '4.45kHz',
      '4.64kHz', '4.82kHz', '5.02kHz', '5.23kHz', '5.44kHz', '5.66kHz', '5.89kHz', '6.13kHz',
      '6.38kHz', '6.64kHz', '6.91kHz', '7.19kHz', '7.49kHz', '7.79kHz', '8.11kHz', '8.44kHz',
      '8.79kHz', '9.14kHz', '9.52kHz', '9.91kHz', '10.3kHz', '10.7kHz', '11.2kHz', '11.6kHz',
      '12.1kHz', '12.6kHz', '13.1kHz', '13.6kHz', '14.2kHz', '14.8kHz', '15.4kHz', '16.0kHz'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function  ValueToDigitizerHzStr( aValue: TValue): String;
  Var
    aFloat : Extended;
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      aFloat := 32.70 * Power( 2, aValue / 12);
      If aFloat < 100
      Then Result := Format( '%.2fHz', [ aFloat])
      Else If aFloat < 1000
      Then Result := Format( '%.1fHz', [ aFloat])
      Else If aFloat < 10000
      Then Result := Format( '%.3fkHz', [ aFloat / 1000])
      Else Result := Format( '%.2fkHz', [ aFloat / 1000]);
    End;
  End;

  Function  ValueToCompanderReleaseStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 127] Of String = (
      '125m' , '129m' , '124m' , '139m' , '144m' , '149m' , '154m' , '159m' ,
      '165m' , '171m' , '177m' , '183m' , '189m' , '196m' , '203m' , '210m' ,
      '218m' , '225m' , '233m' , '241m' , '250m' , '259m' , '268m' , '277m' ,
      '287m' , '297m' , '208m' , '319m' , '330m' , '342m' , '354m' , '366m' ,
      '379m' , '392m' , '406m' , '420m' , '435m' , '451m' , '467m' , '483m' ,
      '500m' , '518m' , '536m' , '555m' , '574m' , '595m' , '616m' , '637m' ,
      '660m' , '683m' , '707m' , '732m' , '758m' , '785m' , '812m' , '841m' ,
      '871m' , '901m' , '933m' , '966m' , '1.00s', '1.04s', '1.07s', '1.11s',
      '1.15s', '1.19s', '1.23s', '1.27s', '1.32s', '1.37s', '1.41s', '1.46s',
      '1.52s', '1.57s', '1.62s', '1.68s', '1.74s', '1.80s', '1.87s', '1.93s',
      '2.00s', '2.07s', '2.14s', '2.22s', '2.30s', '2.38s', '2.46s', '2.55s',
      '2.64s', '2.73s', '2.83s', '2.93s', '3.03s', '3.14s', '3.25s', '3.36s',
      '3.48s', '3.61s', '3.73s', '3.86s', '4.00s', '4.14s', '4.29s', '4.44s',
      '4.59s', '4.76s', '4.92s', '5.10s', '5.28s', '5.46s', '5.66s', '5.86s',
      '6.06s', '6.28s', '6.50s', '6.73s', '6.96s', '7.21s', '7.46s', '7.73s',
      '8.00s', '8.28s', '8.57s', '8.88s', '9.19s', '9.51s', '9.85s', '10.2s'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function  ValueToCompressorTreshStr( aValue: TValue): String;
  Begin
    If ( aValue < 0) Or ( aValue > 42)
    Then Result := 'inv'
    Else
      If aValue = 42
      Then Result := 'Off'
      Else Result := Format( '%ddB', [ aValue - 30]);
  End;

  Function  ValueToExpanderTreshStr( aValue: TValue): String;
  Begin
    If ( aValue < 0) Or ( aValue > 84)
    Then Result := 'inv'
    Else
      If aValue = 0
      Then Result := 'Off'
      Else Result := Format( '%ddB', [ aValue - 84]);
  End;

  Function  ValueToCompanderRatioStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 66] Of String = (
      '1.0:1', '1.1:1', '1.2:1', '1.3:1', '1.4:1', '1.5:1', '1.6:1', '1.7:1',
      '1.8:1', '1.9:1', '2.0:1', '2.2:1', '2.4:1', '2.6:1', '2.8:1', '3.0:1',
      '3.2:1', '3.4:1', '3.6:1', '3.8:1', '4.0:1', '4.2:1', '4.4:1', '4.6:1',
      '4.8:1', '5.0:1', '5.5:1', '6.0:1', '6.5:1', '7.0:1', '7.5:1', '8.0:1',
      '8.5:1', '9.0:1', '9.5:1', '10:1' , '11:1' , '12:1' , '13:1' , '14:1' ,
      '15:1' , '16:1' , '17:1' , '18:1' , '19:1' , '20:1' , '22:1' , '24:1' ,
      '26:1' , '28:1' , '30:1' , '32:1' , '34:1' , '36:1' , '38:1' , '40:1' ,
      '42:1' , '44:1' , '46:1' , '48:1' , '50:1' , '55:1' , '60:1' , '65:1' ,
      '70:1' , '75:!' , '80:1'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function  ValueToCompressorRefLvlStr( aValue: TValue): String;
  Begin
    If ( aValue < 0) Or ( aValue > 42)
    Then Result := 'inv'
    Else Result := Format( '%ddB', [ aValue - 30]);
  End;

  Function  ValueToCompressorLimiterStr( aValue: TValue): String;
  Begin
    If ( aValue < 0) Or ( aValue > 24)
    Then Result := 'inv'
    Else
      If aValue = 24
      Then Result := 'Off'
      Else Result := Format( '%ddB', [ aValue - 12]);
  End;

  Function  ValueToExpanderGateStr( aValue: TValue): String;
  Begin
    If ( aValue < 0) Or ( aValue > 72)
    Then Result := 'inv'
    Else
      If aValue = 0
      Then Result := 'Off'
      Else Result := Format( '%ddB', [ aValue - 84]);
  End;

  Function  ValueToExpanderHoldStr( aValue: TValue): String;
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else
      If aValue = 0
      Then Result := 'Off'
      Else Result := Format( '%dms', [ 4 * aValue]);
  End;

  Function  ValueToSmoothTimeStr( aValue: TValue): String;
  // This conversion deliberately has the same bug as the Clavia Code
  // - the times are way too long.
  Var
    aFloat : Extended;
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      aFloat := Power( 2, aValue / 9);
      If aFloat < 1000
      Then Result := Format( '%.0fms', [ aFloat])
      Else If aFloat < 10000
      Then Result := Format( '%.1fs', [ aFloat / 1000])
      Else Result := Format( '%.0fs', [ aFloat / 1000]);
    End;
  End;

  Function  ValueToNoteScaleStr( aValue: TValue): String;
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      Case aValue Of
          0 : Result := '0 ';
        127 : Result := '±64 '
        Else  Result := Format( '±%.1f ', [ aValue / 2])
      End;
      Case ( aValue Div 2) Mod 12 + aValue Mod 2 Of
         0 : Result := Result + '(Oct)';
         7 : Result := Result + '(5th)';
        10 : Result := Result + '(7th)';
      End;
    End;
  End;

  Function  ValueToNoteRangeStr( aValue: TValue): String;
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      Case aValue Of
          0 : Result := '0';
        127 : Result := '±64'
        Else  Result := Format( '±%.1f', [ aValue / 2])
      End;
    End;
  End;

  Function  ValueToPartialRangeStr( aValue: TValue): String;
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      Case aValue Of
          0 : Result := '0';
        127 : Result := '±64'
        Else  Result := Format( '±%.1f', [ aValue / 2])
      End;
      If aValue > 64
      Then Result := Result + ' *'
    End;
  End;

  Function  ValueToNotesRangeStr( aValue: TValue): String;
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      Case aValue Of
          0 : Result := 'Off';
        Else  Result := Format( '%d', [ aValue])
      End;
    End;
  End;

  Function  ValueToVelScalGainStr( aValue: TValue): String;
  Begin
    If ( aValue < 0) Or ( aValue > 48)
    Then Result := 'inv'
    Else Result := Format( '%ddB', [ aValue - 24]);
  End;

  Function  ValueToLogicDelayStr( aValue: TValue): String;
  Var
    aFloat : Extended;
  Begin
    If ( aValue < 0) Or ( aValue > 127)
    Then Result := 'inv'
    Else Begin
      aFloat := Power( 2, aValue / 9);
      If aFloat < 1000
      Then Result := Format( '%.0fms', [ aFloat])
      Else If aFloat < 10000
      Then Result := Format( '%.1fs', [ aFloat / 1000])
      Else Result := Format( '%.0fs', [ aFloat / 1000]);
    End;
  End;

  Function ValueToChSelectOut1Str( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 5] Of String = (
      '1', '2', '3', '4', 'L', 'R'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 5)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function ValueToChSelectOut2Str( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 2] Of String = (
      '1/2', '3/4', 'CVA'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 2)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function ValueToOscWaveStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 3] Of String = (
      'Sine', 'Tri', 'Saw', 'Square'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 3)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function ValueToFilterTypeStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 2] Of String = (
      'LP', 'BP', 'HP'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 2)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function ValueToLFOWaveStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 4] Of String = (
      'Sine', 'Tri', 'Saw', 'Inverse saw', 'Square'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 4)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function ValueToLFORangeStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 2] Of String = (
      'Sub', 'Lo', 'Hi'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 2)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function ValueToADSREnvShapeStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 2] Of String = (
      'Log', 'Lin', 'Exp'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 2)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function ValueToMultiEnvShapeStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 2] Of String = (
      'Bipolar', 'Uni/Exp', 'Uni/Lin'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 2)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function ValueToPartialsGroupStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 1] Of String = (
      'Odd', 'All'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 1)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function ValueToFilter2TypeStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 3] Of String = (
      'LP', 'BP', 'HP', 'BR'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 3)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function ValueFilter1SlopeToStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 1] Of String = (
      '12 dB/Oct', '24 dB/Oct'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 1)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function ValueToFilter2SlopeStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 2] Of String = (
      '12 dB/Oct', '18 dB/Oct', '24 dB/Oct'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 2)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function ValueToEqShelvingTypeStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 1] Of String = (
      'Low', 'High'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 1)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function ValueToOnOffStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 1] Of String = (
      'Off', 'On'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 1)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function ValueToLogicFuncStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 2] Of String = (
      'And', 'Or', 'Xor'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 2)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function ValueToDiodeFuncStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 2] Of String = (
      'Pass through', 'Half wave rectifiy', 'Full wave rectify'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 2)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function ValueToLevelFuncStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 2] Of String = (
      'Pass through', 'Shift down', 'Shift up'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 2)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function ValueToShaperFuncStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 4] Of String = (
      'log 2', 'log 1', 'lin', 'exp 1', 'exp 2'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 4)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function ValueToCtrlMixerModeStr( aValue: TValue): String;
  Const
    Table : Array[ 0 .. 1] Of String = (
      'lin', 'exp'
    );
  Begin
    If ( aValue < 0) Or ( aValue > 2)
    Then Result := 'inv'
    Else Result := Table[ aValue];
  End;

  Function  ValueToDisplayStr( aValue: TValue; aFormat: TDisplayFormat): String;
  Begin
    Case aFormat Of
      dfUnsigned          : Result := Format( '%d', [ aValue     ]);
      dfSigned            : Result := Format( '%d', [ aValue - 64]);
      dfOffset1           : Result := Format( '%d', [ aValue +  1]);
      dfNote              : Result := ValueToNoteStr             ( aValue);
      dfOscHz             : Result := ValueToOscHzStr            ( aValue);
      dfLfoHz             : Result := ValueToLFOHzStr            ( aValue);
      dfEqHz              : Result := ValueToEqHzStr             ( aValue);
      dfFilterHz1         : Result := ValueToFilterHz1Str        ( aValue);
      dfFilterHz2         : Result := ValueToFilterHz2Str        ( aValue);
      dfSemiTones         : Result := ValueToSemiTonesStr        ( aValue);
      dfPartials          : Result := ValueToPartialStr          ( aValue);
      dfBPM               : Result := ValueToBPMStr              ( aValue);
      dfADSRTime          : Result := ValueToADSRTimeStr         ( aValue);
      dfVowel             : Result := ValueToVowelStr            ( aValue);
      dfPhase             : Result := ValueToPhaseStr            ( aValue);
      dfDrumPreset        : Result := ValueToDrumPresetStr       ( aValue);
      dfEnvAttack         : Result := ValueToEnvAttackStr        ( aValue);
      dfEnvRelease        : Result := ValueToEnvReleaseStr       ( aValue);
      dfDrumHz            : Result := ValueToDrumHzStr           ( aValue);
      dfDrumPartials      : Result := ValueToDrumPartialsStr     ( aValue);
      dfFmtTimbre         : Result := ValueToFmtTimbreStr        ( aValue);
      dfAmpGain           : Result := ValueToAmpGainStr          ( aValue);
      dfQuantizerBits     : Result := ValueToQuantizerBitsStr    ( aValue);
      dfDelayTime         : Result := ValueToDelayTimeStr        ( aValue);
      dfPhaserHz          : Result := ValueToPhaserHzStr         ( aValue);
      dfDigitizerHz       : Result := ValueToDigitizerHzStr      ( aValue);
      dfCompanderRelease  : Result := ValueToCompanderReleaseStr ( aValue);
      dfCompressorTresh   : Result := ValueToCompressorTreshStr  ( aValue);
      dfExpanderTresh     : Result := ValueToExpanderTreshStr    ( aValue);
      dfCompanderRatio    : Result := ValueToCompanderRatioStr   ( aValue);
      dfCompressorRefLvl  : Result := ValueToCompressorRefLvlStr ( aValue);
      dfCompressorLimiter : Result := ValueToCompressorLimiterStr( aValue);
      dfExpanderGate      : Result := ValueToExpanderGateStr     ( aValue);
      dfExpanderHold      : Result := ValueToExpanderHoldStr     ( aValue);
      dfSmoothTime        : Result := ValueToSmoothTimeStr       ( aValue);
      dfNoteScale         : Result := ValueToNoteScaleStr        ( aValue);
      dfNoteRange         : Result := ValueToNoteRangeStr        ( aValue);
      dfPartialRange      : Result := ValueToPartialRangeStr     ( aValue);
      dfNotesRange        : Result := ValueToNotesRangeStr       ( aValue);
      dfVelScalGain       : Result := ValueToVelScalGainStr      ( aValue);
      dfLogicDelay        : Result := ValueToLogicDelayStr       ( aValue);
      dfChSelectOut1      : Result := ValueToChSelectOut1Str     ( aValue);
      dfChSelectOut2      : Result := ValueToChSelectOut2Str     ( aValue);
      dfOscWave           : Result := ValueToOscWaveStr          ( aValue);
      dfFilterType        : Result := ValueToFilterTypeStr       ( aValue);
      dfLFOWave           : Result := ValueToOscWaveStr          ( aValue);
      dfLFORange          : Result := ValueToLFORangeStr         ( aValue);
      dfADSREnvShape      : Result := ValueToADSREnvShapeStr     ( aValue);
      dfMultiEnvShape     : Result := ValueToMultiEnvShapeStr    ( aValue);
      dfPartialsGroup     : Result := ValueToPartialsGroupStr    ( aValue);
      dfFilter2Type       : Result := ValueToFilter2TypeStr      ( aValue);
      dfFilter1Slope      : Result := ValueFilter1SlopeToStr     ( aValue);
      dfFilter2Slope      : Result := ValueToFilter2SlopeStr     ( aValue);
      dfEqShelvingType    : Result := ValueToEqShelvingTypeStr   ( aValue);
      dfOnOff             : Result := ValueToOnOffStr            ( aValue);
      dfLogicFunc         : Result := ValueToLogicFuncStr        ( aValue);
      dfDiodeFunc         : Result := ValueToDiodeFuncStr        ( aValue);
      dfLevelFunc         : Result := ValueToLevelFuncStr        ( aValue);
      dfShaperFunc        : Result := ValueToShaperFuncStr       ( aValue);
      dfCtrlMixerMode     : Result := ValueToCtrlMixerModeStr    ( aValue);
      Else                  Result := 'inv';
    End;
  End;



end.

