// Module definitions version 16
//
// In principle, Blue Hell says :
//
// Copyright 1995 .. 2003, Blue Hell
//
//   I was generated from a TFormStore instance named FormStore, so :
//   in principle, blue hell says, "don't edit me", please.
//   Customize me instead by inheriting from my classes.

//   This file describes what details each module contains.
//   Along with this file comes a file with the same base name
//   but extension .RES instead of .PAS, that file contains all
//   the relavant resources for the visual details.

//   TCustomModule has a constructor able to construct the module
//   from a resource file.

Unit Modules;

Interface

Uses

  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  StdCtrls, Buttons, ExtCtrls, ComCtrls, Spin, CustomIf;

Type

  T_WaveWrap = Class( TNMModule)
    BoxWaveWrapGraph : TBox;
    EditLabelWaveWrap : TEditLabel;
    TextLabelWaveWrapLevel : TTextLabel;
    ImageWaveWrapIO : TGraphicImage;
    OutputWaveWrapOut : TOutput;
    InputWaveWrapIn : TInput;
    ImageWaveWrapLineMod : TGraphicImage;
    InputWaveWrapLevelMod : TInput;
    OscGraphWaveWrap : TOscGraph;
    KnobWaveWrapLevel : TKnob;
    SmallKnobWaveWrapLevelMod : TSmallKnob;
  End;

  T_NoteSeqB = Class( TNMModule)
    EditLabelNoteSeqB : TEditLabel;
    OutputNoteSeqBLink : TOutput;
    InputNoteSeqBRst : TInput;
    TextLabelNoteSeqBRst : TTextLabel;
    InputNoteSeqBClk : TInput;
    TextLabelNoteSeqBClk : TTextLabel;
    GraphicImageNoteSeqBRst : TGraphicImage;
    GraphicImageNoteSeqBClk : TGraphicImage;
    OutputNoteSeqBOut : TOutput;
    TextLabelNoteSeqBOut : TTextLabel;
    TextLabelNoteSeqBLink : TTextLabel;
    TextLabelNoteSeqBStep : TTextLabel;
    TextLabelNoteSeqBSnc : TTextLabel;
    OutputNoteSeqBSnc : TOutput;
    TextLabelNoteSeqBHeader : TTextLabel;
    BoxNoteSeqBGraph : TBox;
    BarGraphNoteSeqB : TBarGraph;
    OutputNoteSeqBGclk : TOutput;
    TextLabelNoteSeqBGclk : TTextLabel;
    ClickerNoteSeqBPresetClr : TClicker;
    ClickerNoteSeqBPresetRnd : TClicker;
    IndicatorNoteSeqB01 : TIndicator;
    IndicatorNoteSeqB02 : TIndicator;
    IndicatorNoteSeqB03 : TIndicator;
    IndicatorNoteSeqB04 : TIndicator;
    IndicatorNoteSeqB05 : TIndicator;
    IndicatorNoteSeqB06 : TIndicator;
    IndicatorNoteSeqB07 : TIndicator;
    IndicatorNoteSeqB08 : TIndicator;
    IndicatorNoteSeqB09 : TIndicator;
    IndicatorNoteSeqB10 : TIndicator;
    IndicatorNoteSeqB11 : TIndicator;
    IndicatorNoteSeqB12 : TIndicator;
    IndicatorNoteSeqB13 : TIndicator;
    IndicatorNoteSeqB14 : TIndicator;
    IndicatorNoteSeqB15 : TIndicator;
    IndicatorNoteSeqB16 : TIndicator;
    TextLabelNoteSeqBZoom : TTextLabel;
    TextLabelNoteSeqBPan : TTextLabel;
    SpinnerNoteSeqBStep : TSpinner;
    DisplayNoteSeqBStep : TDisplay;
    SpinnerNoteSeqB01 : TSpinner;
    SpinnerNoteSeqB02 : TSpinner;
    SpinnerNoteSeqB03 : TSpinner;
    SpinnerNoteSeqB04 : TSpinner;
    SpinnerNoteSeqB05 : TSpinner;
    SpinnerNoteSeqB06 : TSpinner;
    SpinnerNoteSeqB07 : TSpinner;
    SpinnerNoteSeqB08 : TSpinner;
    SpinnerNoteSeqB09 : TSpinner;
    SpinnerNoteSeqB10 : TSpinner;
    SpinnerNoteSeqB11 : TSpinner;
    SpinnerNoteSeqB12 : TSpinner;
    SpinnerNoteSeqB13 : TSpinner;
    SpinnerNoteSeqB14 : TSpinner;
    SpinnerNoteSeqB15 : TSpinner;
    SpinnerNoteSeqB16 : TSpinner;
    SpinnerNoteSeqBPosition : TSpinner;
    SpinnerNoteSeqBPan : TSpinner;
    SpinnerNoteSeqBZoom : TSpinner;
    ButtonSetNoteSeqBLoop : TButtonSet;
    ButtonSetNoteSeqBStop : TButtonSet;
    ButtonSetNoteSeqBRec : TButtonSet;
  End;

  T_Clip = Class( TNMModule)
    BoxClipGraph : TBox;
    EditLabelClip : TEditLabel;
    TextLabelClipLevel : TTextLabel;
    ImageClipIO : TGraphicImage;
    OutputClipOut : TOutput;
    InputClipIn : TInput;
    ImageClipLineMod : TGraphicImage;
    InputClipMod : TInput;
    OscGraphClip : TOscGraph;
    KnobClipLevel : TKnob;
    SmallKnobClipLevelMod : TSmallKnob;
    ButtonSetClipSym : TButtonSet;
  End;

  T_Overdrive = Class( TNMModule)
    BoxOverdriveGraph : TBox;
    EditLabelOverdrive : TEditLabel;
    TextLabelOverdriveLevel : TTextLabel;
    ImageOverdriveIO : TGraphicImage;
    OutputOverdriveOut : TOutput;
    InputOverdriveIn : TInput;
    ImageOverdriveLineMod : TGraphicImage;
    InputOverdriveLevelMod : TInput;
    OscGraphOverdrive : TOscGraph;
    KnobOverdriveLevel : TKnob;
    SmallKnobOverdriveLevelMod : TSmallKnob;
  End;

  T_Chorus = Class( TNMModule)
    EditLabelChorus : TEditLabel;
    TextLabelChorusAmount : TTextLabel;
    TextLabelChorusDetune : TTextLabel;
    ImageChorusIO : TGraphicImage;
    TextLabelChorusL : TTextLabel;
    TextLabelChorusR : TTextLabel;
    OutputChorusLeft : TOutput;
    OutputChorusRight : TOutput;
    InputChorusIn : TInput;
    SmallKnobChorusDetune : TSmallKnob;
    SmallKnobChorusAmount : TSmallKnob;
    ButtonSetChorusBypass : TButtonSet;
  End;

  T_Phaser = Class( TNMModule)
    BoxPhaserSpread : TBox;
    ImagePhaserLineLevel : TGraphicImage;
    BoxPhaserGraph : TBox;
    EditLabelPhaser : TEditLabel;
    BoxPhaserModulation : TBox;
    TextLabelPhaserRate : TTextLabel;
    TextLabelPhaserDepth : TTextLabel;
    ImagePhaserLineFreqMod : TGraphicImage;
    InputPhaserFreqMod : TInput;
    TextLabelPhaserCenterFreq : TTextLabel;
    SetterPhaserFeedback : TSetter;
    BoxPhaserVerticalBar : TBox;
    TextLabelPhaserFeedBk : TTextLabel;
    TextLabelPhaserPeaks : TTextLabel;
    OscGraphPhaser : TOscGraph;
    ImagePhaserIO : TGraphicImage;
    OutputPhaserOut : TOutput;
    InputPhaserIn : TInput;
    ImagePhaserLineSpreadMod : TGraphicImage;
    InputPhaserMod : TInput;
    TextLabelPhaserSpread : TTextLabel;
    KnobPhaserLFORate : TKnob;
    DisplayPhaserLFO : TDisplay;
    SmallKnobPhaserLFODepth : TSmallKnob;
    KnobPhaserCenterFreq : TKnob;
    DisplayPhaserCenterFreq : TDisplay;
    SmallKnobPhaserFreqMod : TSmallKnob;
    SmallKnobPhaserFeedback : TSmallKnob;
    DisplayPhaserPeaks : TDisplay;
    SpinnerPhaserPeaks : TSpinner;
    SmallKnobPhaserLevel : TSmallKnob;
    SmallKnobPhaserSpreadMod : TSmallKnob;
    SmallKnobPhaserSpread : TSmallKnob;
    ButtonSetPhaserBypass : TButtonSet;
    ButtonSetPhaserLFO : TButtonSet;
  End;

  T_InvLevShift = Class( TNMModule)
    EditLabelInvLevShift : TEditLabel;
    ImageInvLevShiftIO : TGraphicImage;
    OutputInvLevShiftOut : TOutput;
    InputInvLevShiftIn : TInput;
    ButtonSetInvLvlShiftFunc : TButtonSet;
    ButtonSetInvLevShiftInv : TButtonSet;
  End;

  T_Diode = Class( TNMModule)
    EditLabelDiode : TEditLabel;
    ImageDiodeIO : TGraphicImage;
    OutputDiodeOut : TOutput;
    InputDiodeIn : TInput;
    ButtonSetDiodeFunc : TButtonSet;
  End;

  T_Quantizer = Class( TNMModule)
    ImageQuantizerIO : TGraphicImage;
    EditLabelQuantizer : TEditLabel;
    OutputQuantizerOut : TOutput;
    InputQuantizerIn : TInput;
    TextLabelQuantizerBits : TTextLabel;
    DisplayQuantizerBits : TDisplay;
    SpinnerQuantizerBits : TSpinner;
  End;

  T_Delay = Class( TNMModule)
    EditLabelDelay : TEditLabel;
    ImageDelayIO : TGraphicImage;
    OutputDelayVariableOut : TOutput;
    InputDelayIn : TInput;
    ImageDelayLineMod : TGraphicImage;
    InputDelayMod : TInput;
    OutputDelayFixedOut : TOutput;
    TextLabelDelayTime : TTextLabel;
    TextLabelDelay265ms : TTextLabel;
    SmallKnobDelayMod : TSmallKnob;
    DisplayDelayTime : TDisplay;
    SmallKnobDelayTime : TSmallKnob;
  End;

  T_SampleAndHold = Class( TNMModule)
    EditLabelSampleAndHold : TEditLabel;
    ImageSampleAndHoldIO : TGraphicImage;
    OutputSampleAndHoldOut : TOutput;
    InputSampleAndHoldIn : TInput;
    InputSampleAndHoldClock : TInput;
    ImageSampleAndHoldClock : TGraphicImage;
  End;

  T_Amplifier = Class( TNMModule)
    ImageAmplifierIO : TGraphicImage;
    EditLabelAmplifier : TEditLabel;
    OutputAmplifierOut : TOutput;
    InputAmplifierIn : TInput;
    SmallKnobAmplifierGain : TSmallKnob;
    DisplayAmplifierGain : TDisplay;
  End;

  T_XFade = Class( TNMModule)
    ImageXFadeIO : TGraphicImage;
    BoxXFadeControl : TBox;
    ImageXFadeLineMod : TGraphicImage;
    EditLabelXFade : TEditLabel;
    TextLabelXFade1_1 : TTextLabel;
    OutputXFadeOut : TOutput;
    InputXFadeIn1 : TInput;
    TextLabelXFade2_1 : TTextLabel;
    InputXFadeIn2 : TInput;
    TextLabelXFadeXFade : TTextLabel;
    InputXFadeMod : TInput;
    SetterXFadeCrossFade : TSetter;
    TextLabelXFade1_2 : TTextLabel;
    TextLabelXFade2_2 : TTextLabel;
    SmallKnobXFadeMod : TSmallKnob;
    SmallKnobXFadeCrossFade : TSmallKnob;
  End;

  T_Pan = Class( TNMModule)
    ImagePanIO : TGraphicImage;
    BoxPanControl : TBox;
    ImagePanLineMod : TGraphicImage;
    EditLabelPan : TEditLabel;
    TextLabelPanL1 : TTextLabel;
    TextLabelPanR1 : TTextLabel;
    TextLabelPanPan : TTextLabel;
    InputPanMod : TInput;
    SetterPanPan : TSetter;
    TextLabelPanL2 : TTextLabel;
    TextLabelPanR2 : TTextLabel;
    OutputPanOutL : TOutput;
    OutputPanOutR : TOutput;
    InputPanIn : TInput;
    SmallKnobPanMod : TSmallKnob;
    SmallKnobPanPan : TSmallKnob;
  End;

  T_1to2Fade = Class( TNMModule)
    Image1to2fadeIO : TGraphicImage;
    EditLabel1to2fade : TEditLabel;
    Output1to2fadeOut2 : TOutput;
    Input1to2fadeIn : TInput;
    Output1to2fadeOut1 : TOutput;
    TextLabel1to2fade1_2 : TTextLabel;
    TextLabel1to2fade2_2 : TTextLabel;
    TextLabel1to2fade1_1 : TTextLabel;
    TextLabel1to2fade2_1 : TTextLabel;
    SmallKnob1to2fadefade : TSmallKnob;
  End;

  T_Mixer3 = Class( TNMModule)
    EditLabelMixer3 : TEditLabel;
    TextLabelMixer3In1 : TTextLabel;
    OutputMixer3Out : TOutput;
    ImageMixer3LineIn1 : TGraphicImage;
    InputMixer3In1 : TInput;
    TextLabelMixer3In2 : TTextLabel;
    ImageMixer3LineIn2 : TGraphicImage;
    InputMixer3In2 : TInput;
    TextLabelMixer3In3 : TTextLabel;
    ImageMixer3LineIn3 : TGraphicImage;
    InputMixer3In3 : TInput;
    SmallKnobMixer3In1 : TSmallKnob;
    SmallKnobMixer3In2 : TSmallKnob;
    SmallKnobMixer3In3 : TSmallKnob;
  End;

  T_Mixer8 = Class( TNMModule)
    EditLabelMixer8 : TEditLabel;
    TextLabelMixer8In1 : TTextLabel;
    OutputMixer8Out : TOutput;
    InputMixer8In1 : TInput;
    IndicatorMixer8Out : TIndicator;
    TextLabelMixer8In2 : TTextLabel;
    InputMixer8In2 : TInput;
    TextLabelMixer8In3 : TTextLabel;
    InputMixer8In3 : TInput;
    TextLabelMixer8In4 : TTextLabel;
    InputMixer8In4 : TInput;
    TextLabelMixer8In5 : TTextLabel;
    InputMixer8In5 : TInput;
    TextLabelMixer8In6 : TTextLabel;
    InputMixer8In6 : TInput;
    TextLabelMixer8In7 : TTextLabel;
    InputMixer8In7 : TInput;
    TextLabelMixer8In8 : TTextLabel;
    InputMixer8In8 : TInput;
    SmallKnobMixer8In1 : TSmallKnob;
    SmallKnobMixer8In2 : TSmallKnob;
    SmallKnobMixer8In3 : TSmallKnob;
    SmallKnobMixer8In4 : TSmallKnob;
    SmallKnobMixer8In5 : TSmallKnob;
    SmallKnobMixer8In6 : TSmallKnob;
    SmallKnobMixer8In7 : TSmallKnob;
    SmallKnobMixer8In8 : TSmallKnob;
    ButtonSetMixer8Attenuation : TButtonSet;
  End;

  T_GainControl = Class( TNMModule)
    ImageGainControlIO : TGraphicImage;
    EditLabelGainControl : TEditLabel;
    TextLabelGainControlControl : TTextLabel;
    OutputGainControlOut : TOutput;
    InputGainControlControl : TInput;
    InputGainControlIn : TInput;
    ButtonSetGainControlShift : TButtonSet;
  End;

  T_OnOff = Class( TNMModule)
    ImageOnOffIO : TGraphicImage;
    EditLabelOnOff : TEditLabel;
    OutputOnOffOut : TOutput;
    InputOnOffIn : TInput;
    ButtonSetOnOffOnOff : TButtonSet;
  End;

  T_4to1Switch = Class( TNMModule)
    EditLabel4to1Switch : TEditLabel;
    Output4to1SwitchOut : TOutput;
    Image4to1SwitchLineIn1 : TGraphicImage;
    Input4to1SwitchIn1 : TInput;
    Image4to1SwitchLineIn2 : TGraphicImage;
    Input4to1SwitchIn2 : TInput;
    Image4to1SwitchLineIn3 : TGraphicImage;
    Input4to1SwitchIn3 : TInput;
    Image4to1SwitchLineIn4 : TGraphicImage;
    Input4to1SwitchIn4 : TInput;
    TextLabel4to1Switch1 : TTextLabel;
    TextLabel4to1Switch2 : TTextLabel;
    TextLabel4to1Switch3 : TTextLabel;
    TextLabel4to1Switch4 : TTextLabel;
    TextLabel4to1SwitchInput : TTextLabel;
    SmallKnob4to1SwitchInputLevel1 : TSmallKnob;
    SmallKnob4to1SwitchInputLevel2 : TSmallKnob;
    SmallKnob4to1SwitchInputLevel3 : TSmallKnob;
    SmallKnob4to1SwitchInputLevel4 : TSmallKnob;
    ButtonSet4to1SwitchSelect : TButtonSet;
    ButtonSet4to1SwitchMute : TButtonSet;
  End;

  T_1to4Switch = Class( TNMModule)
    EditLabel1to4Switch : TEditLabel;
    Output1to4SwitchOut4 : TOutput;
    Image1to4SwitchLineIn : TGraphicImage;
    Input1to4SwitchIn : TInput;
    TextLabel1to4SwitchOutput : TTextLabel;
    Output1to4SwitchOut3 : TOutput;
    Output1to4SwitchOut2 : TOutput;
    Output1to4Switch1 : TOutput;
    SmallKnob1to4SwitchInputLevel : TSmallKnob;
    ButtonSet1to4SwitchSelect : TButtonSet;
    ButtonSet1to4SwitchMute : TButtonSet;
  End;

  T_2to1fade = Class( TNMModule)
    Image2to1fadeIO : TGraphicImage;
    EditLabel2to1fade : TEditLabel;
    Output2to1fadeOut : TOutput;
    Input2to1fadeIn1 : TInput;
    TextLabel2to1fade1_2 : TTextLabel;
    TextLabel2to1fade2_2 : TTextLabel;
    TextLabel2to1fade1_1 : TTextLabel;
    TextLabel2to1fade2_1 : TTextLabel;
    Input2to1fadeIn2 : TInput;
    SmallKnob2to1FadeFade : TSmallKnob;
  End;

  T_LevMult = Class( TNMModule)
    ImageLevMultIO : TGraphicImage;
    EditLabelLevMult : TEditLabel;
    OutputLevMultOut : TOutput;
    InputLevMultIn : TInput;
    SmallKnobLevMultMultiplier : TSmallKnob;
    DisplayLevMultMultiplier : TDisplay;
    ButtonSetLevMultUni : TButtonSet;
  End;

  T_LevAdd = Class( TNMModule)
    ImageLevAddIO : TGraphicImage;
    EditLabelLevAdd : TEditLabel;
    OutputLevAddOut : TOutput;
    InputLevAddIn : TInput;
    SmallKnobLevAddOffset : TSmallKnob;
    DisplayLevAddOffset : TDisplay;
    ButtonSetLevAddUni : TButtonSet;
  End;

  T_Shaper = Class( TNMModule)
    EditLabelShaper : TEditLabel;
    ImageShaperIO : TGraphicImage;
    OutputShaperOut : TOutput;
    InputShaperIn : TInput;
    ButtonSetShaperFunc : TButtonSet;
  End;

  T_PosEdgeDly = Class( TNMModule)
    EditLabelPosEdgeDly : TEditLabel;
    OutputPosEdgeDlyOut : TOutput;
    InputPosEdgeDlyIn : TInput;
    ImagePosEdgeDlyIO : TGraphicImage;
    SmallKnobPosEdgeDlyTime : TSmallKnob;
    DisplayPosEdgeDlyTime : TDisplay;
  End;

  T_NegEdgeDly = Class( TNMModule)
    EditLabelNegEdgeDly : TEditLabel;
    OutputNegEdgeDlyOut : TOutput;
    InputNegEdgeDlyIn : TInput;
    ImageNegEdgeDlyIO : TGraphicImage;
    SmallKnobNegEdgeDlyTime : TSmallKnob;
    DisplayNegEdgeDlyTime : TDisplay;
  End;

  T_Pulse = Class( TNMModule)
    EditLabelPulse : TEditLabel;
    OutputPulseOut : TOutput;
    InputPulseIn : TInput;
    ImagePulseIO : TGraphicImage;
    SmallKnobPulseTime : TSmallKnob;
    DisplayPulseTime : TDisplay;
  End;

  T_PartialGen = Class( TNMModule)
    EditLabelPartialGen : TEditLabel;
    OutputPartialGenOut : TOutput;
    InputPartialGenIn : TInput;
    ImagePartialGenIO : TGraphicImage;
    SmallKnobPartialGenRange : TSmallKnob;
    DisplayPartialGenRange : TDisplay;
  End;

  T_ControlMixer = Class( TNMModule)
    BoxControlMixerIn1 : TBox;
    BoxControlMixerIn2 : TBox;
    EditLabelControlMixer : TEditLabel;
    OutputControlMixerOut : TOutput;
    InputControlMixerIn2 : TInput;
    InputControlMixerIn1 : TInput;
    SmallKnobControlMixerLevel1 : TSmallKnob;
    SmallKnobControlMixerLevel2 : TSmallKnob;
    ButtonSetControlMixerInv1 : TButtonSet;
    ButtonSetControlMixerLev2 : TButtonSet;
    ButtonSetControlMixwerMode : TButtonSet;
  End;

  T_NoteVelScal = Class( TNMModule)
    BoxlNoteVelScalGraph : TBox;
    EditLabelNoteVelScal : TEditLabel;
    OutputlNoteVelScalOut : TOutput;
    InputlNoteVelScalNote : TInput;
    TextLabellNoteVelScalLGain : TTextLabel;
    InputlNoteVelScalVel : TInput;
    TextLabellNoteVelScalBrkPt : TTextLabel;
    TextLabellNoteVelScalRGain : TTextLabel;
    OscGraphlNoteVelScal : TOscGraph;
    TextLabellNoteVelScalVel : TTextLabel;
    TextLabellNoteVelScalNote : TTextLabel;
    TextLabellNoteVelScalVelSens : TTextLabel;
    SmallKnoblNoteVelScalLGain : TSmallKnob;
    DisplaylNoteVelScalLGain : TDisplay;
    SmallKnoblNoteVelScalBrkPt : TSmallKnob;
    DisplaylNoteVelScalBrkPt : TDisplay;
    SmallKnoblNoteVelScalRGain : TSmallKnob;
    DisplaylNoteVelScalRGain : TDisplay;
    SmallKnoblNoteVelScalVelSens : TSmallKnob;
  End;

  T_LogicDelay = Class( TNMModule)
    EditLabelLogicDelay : TEditLabel;
    OutputLogicDelayOut : TOutput;
    InputLogicDelayIn : TInput;
    ImageLogicDelayIO : TGraphicImage;
    SmallKnobLogicDelayTime : TSmallKnob;
    DisplayLogicDelayTime : TDisplay;
  End;

  T_CompareLev = Class( TNMModule)
    EditLabelCompareLev : TEditLabel;
    OutputCompareLevOut : TOutput;
    InputCompareLevInA : TInput;
    TextLabelCompareLevC : TTextLabel;
    TextLabelCompareLevA : TTextLabel;
    TextLabelCompareLevAgeqC : TTextLabel;
    SmallKnobCompareLevLevel : TSmallKnob;
    DisplayCompareLevLevel : TDisplay;
  End;

  T_CompareAB = Class( TNMModule)
    EditLabelCompareAB : TEditLabel;
    OutputCompareABOut : TOutput;
    InputCompareABB : TInput;
    TextLabelCompareABB : TTextLabel;
    TextLabelCompareABAgeqB : TTextLabel;
    InputCompareABA : TInput;
    TextLabelCompareABA : TTextLabel;
  End;

  T_ClkDiv = Class( TNMModule)
    EditLabelClkDiv : TEditLabel;
    OutputClkDivOut : TOutput;
    InputClkDivInRst : TInput;
    TextLabelClkDivRst : TTextLabel;
    TextLabelClkDivDivider : TTextLabel;
    InputClkDivInClock : TInput;
    TextLabelClkDivClock : TTextLabel;
    ImageClkDivRst : TGraphicImage;
    ImageClkDivClock : TGraphicImage;
    SpinnerClkDivDivider : TSpinner;
    DisplayClkDivDivider : TDisplay;
  End;

  T_LogicInv = Class( TNMModule)
    EditLabelLogicInv : TEditLabel;
    OutputLogicInvOut : TOutput;
    InputLogicInvIn : TInput;
    ImageLogicInvIO : TGraphicImage;
  End;

  T_ClkDivFix = Class( TNMModule)
    EditLabelClkDivFix : TEditLabel;
    OutputClkDivFixOut16 : TOutput;
    InputClkDivFixRst : TInput;
    TextLabelClkDivFixRst : TTextLabel;
    InputClkDivFixMidiCl : TInput;
    TextLabelClkDivFixMidiCl : TTextLabel;
    ImageClkDivFixRst : TGraphicImage;
    ImageClkDivFixMidiCl : TGraphicImage;
    Output1ClkDivFixOutT8 : TOutput;
    OutputClkDivFixOut8 : TOutput;
    TextLabelClkDivFix16 : TTextLabel;
    TextLabelClkDivFixT8 : TTextLabel;
    TextLabelClkDivFix8 : TTextLabel;
  End;

  T_LogicProc = Class( TNMModule)
    ImageLogicProcIO : TGraphicImage;
    EditLabelLogicProc : TEditLabel;
    OutputLogicProcOut : TOutput;
    InputLogicProcIn2 : TInput;
    InputLogicProcIn1 : TInput;
    ButtonSetLogicProcFunction : TButtonSet;
  End;

  T_KeyQuant = Class( TNMModule)
    EditLabelKeyQuant : TEditLabel;
    OutputKeyQuantOut : TOutput;
    InputKeyQuantIn : TInput;
    ImageKeyQuantIO : TGraphicImage;
    TextLabelKeyQuantRange : TTextLabel;
    TextLabelKeyQuantDir : TTextLabel;
    SmallKnobKeyQuantRange : TSmallKnob;
    DisplayKeyQuantRange : TDisplay;
    ButtonSetKeyQuantC : TButtonSet;
    ButtonSetKeyQuantD : TButtonSet;
    ButtonSetKeyQuantE : TButtonSet;
    ButtonSetKeyQuantF : TButtonSet;
    ButtonSetKeyQuantG : TButtonSet;
    ButtonSetKeyQuantA : TButtonSet;
    ButtonSetKeyQuantB : TButtonSet;
    ButtonSetKeyQuantCis : TButtonSet;
    ButtonSetKeyQuantDis : TButtonSet;
    ButtonSetKeyQuantFis : TButtonSet;
    ButtonSetKeyQuantGis : TButtonSet;
    ButtonSetKeyQuantBes : TButtonSet;
    ButtonSetKeyQuantCont : TButtonSet;
  End;

  T_Digitizer = Class( TNMModule)
    BoxDigitizerRate : TBox;
    BoxDigitizerQuant : TBox;
    EditLabelDigitizer : TEditLabel;
    TextLabelDigitizerQuant : TTextLabel;
    TextLabelDigitizerBits : TTextLabel;
    TextLabelDigitizerSample : TTextLabel;
    TextLabelDigitizerRate : TTextLabel;
    ImageDigitizerLineRateMod : TGraphicImage;
    InputDigitizerRateMod : TInput;
    ImageDigitizerIO : TGraphicImage;
    OutputDigitizerOut : TOutput;
    InputDigitizerIn : TInput;
    DisplayDigitizerQuant : TDisplay;
    SpinnerDigitizerQuant : TSpinner;
    SmallKnobDigitizerRateMod : TSmallKnob;
    SmallKnobDigitizerRate : TSmallKnob;
    DisplayDigitizerRate : TDisplay;
    ButtonSetDigitizerQuantOff : TButtonSet;
    ButtonSetDigitizerRateOff : TButtonSet;
  End;

  T_NoteSeqA = Class( TNMModule)
    EditLabelNoteSeqA : TEditLabel;
    OutputNoteSeqALink : TOutput;
    InputNoteSeqARst : TInput;
    TextLabelNoteSeqARst : TTextLabel;
    InputNoteSeqAClk : TInput;
    TextLabelNoteSeqAClk : TTextLabel;
    GraphicImageNoteSeqARst : TGraphicImage;
    GraphicImageNoteSeqAClk : TGraphicImage;
    OutputNoteSeqAOut : TOutput;
    TextLabelNoteSeqAOut : TTextLabel;
    TextLabelNoteSeqALink : TTextLabel;
    TextLabelNoteSeqAStep : TTextLabel;
    TextLabelVSnc : TTextLabel;
    OutputNoteSeqASnc : TOutput;
    TextLabelNoteSeqAHeading : TTextLabel;
    BoxNoteSeqAGraph : TBox;
    BarGraphNoteSeqA : TBarGraph;
    OutputNoteSeqAGclk : TOutput;
    TextLabelNoteSeqAGclk : TTextLabel;
    ClickerNoteSeqAPresetClr : TClicker;
    IndicatorNoteSeqA01 : TIndicator;
    IndicatorNoteSeqA02 : TIndicator;
    IndicatorNoteSeqA03 : TIndicator;
    IndicatorNoteSeqA04 : TIndicator;
    IndicatorNoteSeqA05 : TIndicator;
    IndicatorNoteSeqA06 : TIndicator;
    IndicatorNoteSeqA07 : TIndicator;
    IndicatorNoteSeqA08 : TIndicator;
    IndicatorNoteSeqA09 : TIndicator;
    IndicatorNoteSeqA10 : TIndicator;
    IndicatorNoteSeqA11 : TIndicator;
    IndicatorNoteSeqA12 : TIndicator;
    IndicatorNoteSeqA13 : TIndicator;
    IndicatorNoteSeqA14 : TIndicator;
    IndicatorNoteSeqA15 : TIndicator;
    IndicatorNoteSeqA16 : TIndicator;
    ClickerNoteSeqAPresetRnd : TClicker;
    SpinnerNoteSeqAStep : TSpinner;
    DisplayNoteSeqAStep : TDisplay;
    SpinnerNoteSeqA01 : TSpinner;
    SpinnerNoteSeqA02 : TSpinner;
    SpinnerNoteSeqA03 : TSpinner;
    SpinnerNoteSeqA04 : TSpinner;
    SpinnerNoteSeqA05 : TSpinner;
    SpinnerNoteSeqA06 : TSpinner;
    SpinnerNoteSeqA07 : TSpinner;
    SpinnerNoteSeqA08 : TSpinner;
    SpinnerNoteSeqA09 : TSpinner;
    SpinnerNoteSeqA10 : TSpinner;
    SpinnerNoteSeqA11 : TSpinner;
    SpinnerNoteSeqA12 : TSpinner;
    SpinnerNoteSeqA13 : TSpinner;
    SpinnerNoteSeqA14 : TSpinner;
    SpinnerNoteSeqA15 : TSpinner;
    SpinnerNoteSeqA16 : TSpinner;
    SpinnerNoteSeqAPosition : TSpinner;
    ButtonSetNoteSeqALoop : TButtonSet;
    ButtonSetNoteSeqARec : TButtonSet;
    ButtonSetNoteSeqAStop : TButtonSet;
  End;

  T_CtrlSeq = Class( TNMModule)
    BoxCtrlSeqGraph : TBox;
    BarGraphCtrlSeq : TBarGraph;
    EditLabelCtrlSeq : TEditLabel;
    OutputCtrlSeqLink : TOutput;
    InputCtrlSeqRst : TInput;
    TextLabelCtrlSeqRst : TTextLabel;
    InputCtrlSeqClk : TInput;
    TextLabelCtrlSeqClk : TTextLabel;
    GraphicImageCtrlSeqRst : TGraphicImage;
    GraphicImageCtrlSeqClk : TGraphicImage;
    OutputCtrlSeqOut : TOutput;
    TextLabelCtrlSeqOut : TTextLabel;
    TextLabelCtrlSeqLink : TTextLabel;
    TextLabelCtrlSeqStep : TTextLabel;
    TextLabelCtrlSeqSnc : TTextLabel;
    OutputCtrlSeqSnc : TOutput;
    ClickerCtrlSeqPresetClr : TClicker;
    IndicatorCtrlSeq01 : TIndicator;
    IndicatorCtrlSeq02 : TIndicator;
    IndicatorCtrlSeq03 : TIndicator;
    IndicatorCtrlSeq04 : TIndicator;
    IndicatorCtrlSeq05 : TIndicator;
    IndicatorCtrlSeq06 : TIndicator;
    IndicatorCtrlSeq07 : TIndicator;
    IndicatorCtrlSeq08 : TIndicator;
    IndicatorCtrlSeq09 : TIndicator;
    IndicatorCtrlSeq10 : TIndicator;
    IndicatorCtrlSeq11 : TIndicator;
    IndicatorCtrlSeq12 : TIndicator;
    IndicatorCtrlSeq13 : TIndicator;
    IndicatorCtrlSeq14 : TIndicator;
    IndicatorCtrlSeq15 : TIndicator;
    IndicatorCtrlSeq16 : TIndicator;
    TextLabelCtrlSeqHeading : TTextLabel;
    ClickerCtrlSeqPresetRnd : TClicker;
    SpinnerCtrlSeqStep : TSpinner;
    DisplayCtrlSeqStep : TDisplay;
    SpinnerCtrlSeq01 : TSpinner;
    SpinnerCtrlSeq02 : TSpinner;
    SpinnerCtrlSeq03 : TSpinner;
    SpinnerCtrlSeq04 : TSpinner;
    SpinnerCtrlSeq05 : TSpinner;
    SpinnerCtrlSeq06 : TSpinner;
    SpinnerCtrlSeq07 : TSpinner;
    SpinnerCtrlSeq08 : TSpinner;
    SpinnerCtrlSeq09 : TSpinner;
    SpinnerCtrlSeq10 : TSpinner;
    SpinnerCtrlSeq11 : TSpinner;
    SpinnerCtrlSeq12 : TSpinner;
    SpinnerCtrlSeq13 : TSpinner;
    SpinnerCtrlSeq14 : TSpinner;
    SpinnerCtrlSeq15 : TSpinner;
    SpinnerCtrlSeq16 : TSpinner;
    ButtonSetCtrlSeqUni : TButtonSet;
    ButtonSetCtrlSeqLoop : TButtonSet;
  End;

  T_Compressor = Class( TNMModule)
    BoxCompressorSideChain : TBox;
    BoxCompressorGraph : TBox;
    EditLabelCompressor : TEditLabel;
    InputCompressorLeft : TInput;
    InputCompressorRight : TInput;
    InputCompressorSideChain : TInput;
    TextLabelCompressorL_1 : TTextLabel;
    TextLabelCompressorR_1 : TTextLabel;
    TextLabelCompressorSideChain : TTextLabel;
    OscGraphCompressor : TOscGraph;
    TextLabelCompressorAttack : TTextLabel;
    TextLabelCompressorRelease : TTextLabel;
    TextLabelCompressorTresh : TTextLabel;
    TextLabelCompressorRatio : TTextLabel;
    TextLabelCompressorRefLvl : TTextLabel;
    TextLabelCompressorLimiter : TTextLabel;
    TextLabelCompressorL_2 : TTextLabel;
    OutputCompressorLeft : TOutput;
    OutputCompressorRight : TOutput;
    TextLabelCompressorR_2 : TTextLabel;
    BoxCompressorLedBar : TBox;
    IndicatorCompressorLimiter : TIndicator;
    LedBarCompressorGain : TLedBar;
    TextLabelCompressorGainReduction : TTextLabel;
    TextLabelCompressordBs : TTextLabel;
    TextLabelCompressorLimActive : TTextLabel;
    DisplayCompressorAttack : TDisplay;
    SmallKnobCompressorAttack : TSmallKnob;
    DisplayCompressorRelease : TDisplay;
    SmallKnobCompressorRelease : TSmallKnob;
    DisplayCompressorTresh : TDisplay;
    SmallKnobCompressorTresh : TSmallKnob;
    DisplayCompressorRatio : TDisplay;
    SmallKnobCompressorRatio : TSmallKnob;
    DisplayCompressorRefLvl : TDisplay;
    SmallKnobCompressorRefLvl : TSmallKnob;
    DisplayCompressorLimiter : TDisplay;
    SmallKnobCompressorLimiter : TSmallKnob;
    ButtonSetCompressorBypass : TButtonSet;
    ButtonSetCompressorSideChainAct : TButtonSet;
    ButtonSetCompressorSideChainMon : TButtonSet;
  End;

  T_Expander = Class( TNMModule)
    BoxExpanderSideChain : TBox;
    BoxExpanderGraph : TBox;
    EditLabelExpander : TEditLabel;
    InputExpanderLeft : TInput;
    InputExpanderRight : TInput;
    InputExpanderSideChain : TInput;
    TextLabelExpanderL_1 : TTextLabel;
    TextLabelExpanderR_1 : TTextLabel;
    TextLabelExpanderSideChain : TTextLabel;
    OscGraphExpander : TOscGraph;
    TextLabelExpanderAttack : TTextLabel;
    TextLabelExpanderRelease : TTextLabel;
    TextLabelExpanderTresh : TTextLabel;
    TextLabelExpanderRatio : TTextLabel;
    TextLabelExpanderGate : TTextLabel;
    TextLabelExpanderHold : TTextLabel;
    TextLabelExpanderL_2 : TTextLabel;
    OutputExpanderLeft : TOutput;
    OutputExpanderRight : TOutput;
    TextLabelExpanderR_2 : TTextLabel;
    BoxExpanderLedBar : TBox;
    IndicatorExpanderGateActive : TIndicator;
    LedBarExpanderGain : TLedBar;
    TextLabelExpanderGainReduction : TTextLabel;
    TextLabelExpanderGaindBs : TTextLabel;
    TextLabelExpanderGateActive : TTextLabel;
    DisplayExpanderAttack : TDisplay;
    SmallKnobExpanderAttack : TSmallKnob;
    DisplayExpanderRelease : TDisplay;
    SmallKnobExpanderRelease : TSmallKnob;
    DisplayExpanderTresh : TDisplay;
    SmallKnobExpanderTresh : TSmallKnob;
    DisplayExpanderRatio : TDisplay;
    SmallKnobExpanderRatio : TSmallKnob;
    DisplayExpanderGate : TDisplay;
    SmallKnobExpanderGate : TSmallKnob;
    DisplayExpanderHold : TDisplay;
    SmallKnobExpanderHold : TSmallKnob;
    ButtonSetExpanderBypass : TButtonSet;
    ButtonSetExpanderSideChainMon : TButtonSet;
    ButtonSetExpanderSideChainAct : TButtonSet;
  End;

  T_RingMod = Class( TNMModule)
    ImageRingModIO : TGraphicImage;
    TextLabelRingModAM : TTextLabel;
    TextLabelRingMod0 : TTextLabel;
    TextLabelRingModRM : TTextLabel;
    EditLabelRingMod : TEditLabel;
    SetterRingModModType : TSetter;
    ImageRingModLineModDepth : TGraphicImage;
    InputRingModModDepth : TInput;
    InputRingModMod : TInput;
    InputRingModIn : TInput;
    OutputRingModOut : TOutput;
    TextLabelRingModModDepth : TTextLabel;
    SmallKnobRingModModType : TSmallKnob;
    SmallKnobRingModModDepth : TSmallKnob;
  End;

  T_Constant = Class( TNMModule)
    EditLabelConstant : TEditLabel;
    OutputConstantOut : TOutput;
    SmallKnobConstantValue : TSmallKnob;
    DisplayConstantValue : TDisplay;
    ButtonSetConstantUni : TButtonSet;
  End;

  T_EventSeq = Class( TNMModule)
    GraphicImageEventSeqLineOutB : TGraphicImage;
    GraphicImageEventSeqLineOutA : TGraphicImage;
    EditLabelEventSeq : TEditLabel;
    OutputEventSeqLink : TOutput;
    InputEventSeqRst : TInput;
    TextLabelEventSeqRst : TTextLabel;
    InputEventSeqClk : TInput;
    TextLabelEventSeqClk : TTextLabel;
    ImageEventSeqRst : TGraphicImage;
    ImageEventSeqClk : TGraphicImage;
    OutputEventSeqOutB : TOutput;
    OutputEventSeqOutA : TOutput;
    TextLabelEventSeqOut : TTextLabel;
    TextLabelEventSeqLink : TTextLabel;
    TextLabelEventSeqStep : TTextLabel;
    TextLabelEventSeqSnc : TTextLabel;
    OutputEventSeqSnc : TOutput;
    ClickerEventSeqPresetClr : TClicker;
    IndicatorEventSeq01 : TIndicator;
    IndicatorEventSeq02 : TIndicator;
    IndicatorEventSeq03 : TIndicator;
    IndicatorEventSeq04 : TIndicator;
    IndicatorEventSeq05 : TIndicator;
    IndicatorEventSeq06 : TIndicator;
    IndicatorEventSeq07 : TIndicator;
    IndicatorEventSeq08 : TIndicator;
    IndicatorEventSeq09 : TIndicator;
    IndicatorEventSeq10 : TIndicator;
    IndicatorEventSeq11 : TIndicator;
    IndicatorEventSeq12 : TIndicator;
    IndicatorEventSeq13 : TIndicator;
    IndicatorEventSeq14 : TIndicator;
    IndicatorEventSeq15 : TIndicator;
    IndicatorEventSeq16 : TIndicator;
    TextLabelEventSeqHeading : TTextLabel;
    SpinnerEventSeqStep : TSpinner;
    DisplayEventSeqStep : TDisplay;
    ButtonSetEventSeqA01 : TButtonSet;
    ButtonSetEventSeqA02 : TButtonSet;
    ButtonSetEventSeqA03 : TButtonSet;
    ButtonSetEventSeqA04 : TButtonSet;
    ButtonSetEventSeqA05 : TButtonSet;
    ButtonSetEventSeqA06 : TButtonSet;
    ButtonSetEventSeqA07 : TButtonSet;
    ButtonSetEventSeqA08 : TButtonSet;
    ButtonSetEventSeqA09 : TButtonSet;
    ButtonSetEventSeqA10 : TButtonSet;
    ButtonSetEventSeqA11 : TButtonSet;
    ButtonSetEventSeqA12 : TButtonSet;
    ButtonSetEventSeqA13 : TButtonSet;
    ButtonSetEventSeqA14 : TButtonSet;
    ButtonSetEventSeqA15 : TButtonSet;
    ButtonSetEventSeqA16 : TButtonSet;
    ButtonSetEventSeqAG : TButtonSet;
    ButtonSetEventSeqB01 : TButtonSet;
    ButtonSetEventSeqB02 : TButtonSet;
    ButtonSetEventSeqB03 : TButtonSet;
    ButtonSetEventSeqB04 : TButtonSet;
    ButtonSetEventSeqB05 : TButtonSet;
    ButtonSetEventSeqB06 : TButtonSet;
    ButtonSetEventSeqB07 : TButtonSet;
    ButtonSetEventSeqB08 : TButtonSet;
    ButtonSetEventSeqB09 : TButtonSet;
    ButtonSetEventSeqB10 : TButtonSet;
    ButtonSetEventSeqB11 : TButtonSet;
    ButtonSetEventSeqB12 : TButtonSet;
    ButtonSetEventSeqB13 : TButtonSet;
    ButtonSetEventSeqB14 : TButtonSet;
    ButtonSetEventSeqB15 : TButtonSet;
    ButtonSetEventSeqB16 : TButtonSet;
    ButtonSetEventSeqBG : TButtonSet;
    ButtonSetEventSeqLoop : TButtonSet;
  End;

  T_NoteScaler = Class( TNMModule)
    EditLabelNoteScaler : TEditLabel;
    OutputNoteScalerOut : TOutput;
    InputNoteScalerIn : TInput;
    ImageNoteScalerIO : TGraphicImage;
    SmallKnobNoteScalerClip : TSmallKnob;
    DisplayNoteScalerClip : TDisplay;
  End;

  T_NoteQuant = Class( TNMModule)
    EditLabelNoteQuant : TEditLabel;
    OutputNoteQuantIO : TOutput;
    InputNoteQuantIn : TInput;
    ImageNoteQuantIO : TGraphicImage;
    TextLabelNoteQuantRange : TTextLabel;
    TextLabelNoteQuantNotes : TTextLabel;
    SmallKnobNoteQuantRange : TSmallKnob;
    DisplayNoteQuantRange : TDisplay;
    DisplayNoteQuantNotes : TDisplay;
    SpinnerNoteQuantNotes : TSpinner;
  End;

  T_Smooth = Class( TNMModule)
    ImageSmoothIO : TGraphicImage;
    EditLabelSmooth : TEditLabel;
    OutputSmoothOut : TOutput;
    InputSmoothIn : TInput;
    ImageSmoothFunction : TGraphicImage;
    SmallKnobSmoothValue : TSmallKnob;
    DisplaySmoothValue : TDisplay;
  End;

  T_PortamentoA = Class( TNMModule)
    ImagePortamentoAIO : TGraphicImage;
    EditLabelPortamentoA : TEditLabel;
    OutputPortamentoAOut : TOutput;
    InputPortamentoAIn : TInput;
    InputPortamentoAOn : TInput;
    TextLabelPortamentoAOn : TTextLabel;
    TextLabelPortamentoATime : TTextLabel;
    SmallKnobPortamentoATime : TSmallKnob;
  End;

  T_PortamentoB = Class( TNMModule)
    ImagePortamentoBIO : TGraphicImage;
    EditLabelPortamentoB : TEditLabel;
    OutputPortamentoBOut : TOutput;
    InputPortamentoBIn : TInput;
    InputPortamentoBJmp : TInput;
    TextLabelPortamentoBJmp : TTextLabel;
    TextLabelPortamentoBTime : TTextLabel;
    ImagePortamentoBJmp : TGraphicImage;
    SmallKnobPortamentoBTime : TSmallKnob;
  End;

  T_OscSlvC = Class( TNMModule)
    BoxOscSlvCFMA : TBox;
    ImageOscSlvCLineFMA : TGraphicImage;
    EditLabelOscSlvC : TEditLabel;
    SetterOscSlvCDetune : TSetter;
    TextLabelOscSlvCDetune : TTextLabel;
    TextLabelOscSlvCFine : TTextLabel;
    TextLabelOscSlvCMst : TTextLabel;
    OutputOscSlvCOut : TOutput;
    InputOscSlvCFMA : TInput;
    TextLabelOscSlvCFMA : TTextLabel;
    TextLabelOscSlvCPartials : TTextLabel;
    InputOscSlvCMst : TInput;
    ImageOscSlvCOscType : TGraphicImage;
    KnobOscSlvCDetuneCoarse : TKnob;
    SmallKnobOscSlvCDetuneFine : TSmallKnob;
    DisplayOscSlvCFreq : TDisplay;
    SmallKnobOscSlvCFMA : TSmallKnob;
    SpinnerOscSlvCPartials : TSpinner;
    ButtonSetOscSlvCMute : TButtonSet;
  End;

  T_OscSlvD = Class( TNMModule)
    BoxOscSlvDFMA : TBox;
    ImageOscSlvDLineFMA : TGraphicImage;
    EditLabelOscSlvD : TEditLabel;
    SetterOscSlvDDetune : TSetter;
    TextLabelOscSlvDDetune : TTextLabel;
    TextLabelOscSlvDFine : TTextLabel;
    TextLabelOscSlvDMast : TTextLabel;
    OscSlvDOut : TOutput;
    InputOscSlvDFMA : TInput;
    TextLabelOscSlvDFMA : TTextLabel;
    TextLabelOscSlvDPartials : TTextLabel;
    InputOscSlvDMst : TInput;
    ImageOscSlvDOscType : TGraphicImage;
    KnobOscSlvDDetuneCoarse : TKnob;
    SmallKnobOscSlvDDetuneFine : TSmallKnob;
    DisplayOscSlvDFreq : TDisplay;
    SmallKnobOscSlvDFMA : TSmallKnob;
    SpinnerOscSlvDPartials : TSpinner;
    ButtonSetOscSlvDMute : TButtonSet;
  End;

  T_OscSlvE = Class( TNMModule)
    BoxOscSlvEFMA : TBox;
    ImageOscSlvELineFMA : TGraphicImage;
    EditLabelOscSlvE : TEditLabel;
    SetterOscSlvEDetune : TSetter;
    TextLabelOscSlvEDetune : TTextLabel;
    TextLabelOscSlvEFine : TTextLabel;
    TextLabelOscSlvEMst : TTextLabel;
    OutputOscSlvEOut : TOutput;
    InputOscSlvEFMA : TInput;
    TextLabelOscSlvEFMA : TTextLabel;
    TextLabelOscSlvEPartials : TTextLabel;
    InputOscSlvEMst : TInput;
    ImageOscSslvEOscType : TGraphicImage;
    InputOscSlvEAM : TInput;
    TextLabelOscSlvEAM : TTextLabel;
    KnobOscSlvEDetuneCoarse : TKnob;
    SmallKnobOscSlveDetuneFine : TSmallKnob;
    DisplayOscSlvEFreq : TDisplay;
    SmallKnobOscSlvEFMA : TSmallKnob;
    SpinnerOscSlvEPartials : TSpinner;
    ButtonSetOscSlvEMute : TButtonSet;
  End;

  T_OscSlvB = Class( TNMModule)
    BoxOscSlvBPW : TBox;
    ImageOscSlvBLinePW : TGraphicImage;
    EditLabelOscSlvB : TEditLabel;
    SetterOscSlvBDetune : TSetter;
    TextLabelOscSlvBDetune : TTextLabel;
    TextLabelOscSlvBFine : TTextLabel;
    TextLabelOscSlvBMst : TTextLabel;
    OutputOscSlvBOut : TOutput;
    InputOscSlvBPW : TInput;
    TextLabelOscSlvBPW : TTextLabel;
    TextLabelOscSlvBPartials : TTextLabel;
    InputOscSlvBMst : TInput;
    SetterOscSlvBPW : TSetter;
    ImageOscSlvBOscType : TGraphicImage;
    KnobOscSlvBDetuneCoarse : TKnob;
    SmallKnobOscSlvBDetuneFine : TSmallKnob;
    DisplayOscSlvBFreq : TDisplay;
    SmallKnobOscSlvBPWMod : TSmallKnob;
    SmallKnobOscSlvBPW : TSmallKnob;
    SpinnerOscSlvBPartials : TSpinner;
    ButtonSetOscSlvBMute : TButtonSet;
  End;

  T_SpectralOsc = Class( TNMModule)
    BoxSpectralOscShape : TBox;
    ImageSpectralOscLineShape : TGraphicImage;
    EditLabelSpectralOsc : TEditLabel;
    SetterSpectralOscFreqFine : TSetter;
    OutputSpectralOscSlv : TOutput;
    TextLabelSpectralOscCoarse : TTextLabel;
    TextLabelSpectralOscFine : TTextLabel;
    TextLabelSpectralOscSlv : TTextLabel;
    InputSpectralOscPitch1 : TInput;
    TextLabelSpectralOscPitch1 : TTextLabel;
    InputSpectralOscPitch2 : TInput;
    TextLabelSpectralOscPitch2 : TTextLabel;
    InputSpectralOscFMA : TInput;
    TextLabelSpectralOscFMA : TTextLabel;
    TextLabelSpectralOscSpectral : TTextLabel;
    InputSpectralOscShape : TInput;
    TextLabelSpectralOscShape : TTextLabel;
    TextLabelSpectralOscFreq : TTextLabel;
    ImageSpectralOscType : TGraphicImage;
    OutputSpectralOscOut : TOutput;
    TextLabelSpectralOscPartials : TTextLabel;
    ImageSpectralOscLinePitch1 : TGraphicImage;
    ImageSpectralOscLinePitch2 : TGraphicImage;
    ImageSpectralOscLineFMA : TGraphicImage;
    KnobSpectralOscFreqCoarse : TKnob;
    SmallKnobSpectralOscFreqFine : TSmallKnob;
    DisplaySpectralOscFreq : TDisplay;
    SmallKnobSpectralOscPitch1 : TSmallKnob;
    SmallKnobSpectralOscPitch2 : TSmallKnob;
    SmallKnobSpectralOscFMA : TSmallKnob;
    SmallKnobSpectralOscShape : TSmallKnob;
    SmallKnobSpectralOscShapeMod : TSmallKnob;
    ButtonSetSpectralOscParrtials : TButtonSet;
    ButtonSetSpectralOscMute : TButtonSet;
    ButtonSetSpetralOscKBT : TButtonSet;
  End;

  T_FormantOsc = Class( TNMModule)
    BoxFormantOscTimbre : TBox;
    ImageFormantOscTimbre : TGraphicImage;
    ImageFormantOscLinePitch2 : TGraphicImage;
    ImageFormantOscLinePitch1 : TGraphicImage;
    EditLabelFormantOsc : TEditLabel;
    SetterFormantOscFine : TSetter;
    OutputFormantOscSlv : TOutput;
    TextLabelFormantOscCoarse : TTextLabel;
    TextLabelFormantOscFine : TTextLabel;
    TextLabelFormantOscSlv : TTextLabel;
    InputFormantOscPitch1 : TInput;
    ImageFormantOscType : TGraphicImage;
    OutputFormantOscOut : TOutput;
    TextLabelFormantOscFreq : TTextLabel;
    InputFormantOscPitch2 : TInput;
    InputFormantOscTimbre : TInput;
    TextLabelFormantOscTimbre : TTextLabel;
    KnobFormantOscFreqCoarse : TKnob;
    SmallKnobFormantOscFreqFine : TSmallKnob;
    DisplayFormantOscFreq : TDisplay;
    SmallKnobFormantOscPitch1 : TSmallKnob;
    SmallKnobFormantOscPitch2 : TSmallKnob;
    SmallKnobFormantOscTimbre : TSmallKnob;
    DisplayFormantOscTimbre : TDisplay;
    ButtonSetFormantOscMute : TButtonSet;
    ButtonSetFormantOscKBT : TButtonSet;
  End;

  T_OscSlvA = Class( TNMModule)
    BoxOscSlvAFMA : TBox;
    ImageOscSlvALineFMA : TGraphicImage;
    EditLabelOscSlvA : TEditLabel;
    SetterOscSlvADetune : TSetter;
    TextLabelOscSlvADetune : TTextLabel;
    TextLabelOscSlvAFine : TTextLabel;
    TextLabelOscSlvAMst : TTextLabel;
    OutputOscSlvAOut : TOutput;
    InputOscSlvAFMA : TInput;
    InputOscSlvAAM : TInput;
    TextLabelOscSlvAFMA : TTextLabel;
    TextLabelOscSlvAAM : TTextLabel;
    TextLabelOscSlvAPartials : TTextLabel;
    ImageOscSlvASync : TGraphicImage;
    InputOscSlvASync : TInput;
    TextLabelOscSlvASync : TTextLabel;
    InputOscSlvAMst : TInput;
    KnobOscSlvADetuneCoarse : TKnob;
    SmallKnobOscSlvADetuneFine : TSmallKnob;
    DisplayOscSlvAFreq : TDisplay;
    SmallKnobOscSlvAFMA : TSmallKnob;
    SpinnerOscSlvAPartials : TSpinner;
    ButtonSetOscSlvAWave : TButtonSet;
    ButtonSetOscSlvAMute : TButtonSet;
  End;

  T_DrumSynth = Class( TNMModule)
    BoxDrumSynthClickNoise : TBox;
    BoxDrumSynthBend : TBox;
    BoxDrumSynthNoise : TBox;
    EditLabelDrumSynt : TEditLabel;
    BoxDrumSynthPitch : TBox;
    InputDrumSynthPitch : TInput;
    TextLabelDrumSynthPitch : TTextLabel;
    TextLabelDrumSynthNoiseDcy : TTextLabel;
    OutputDrumSynthOut : TOutput;
    TextLabelDrumSynthNoiseFilter : TTextLabel;
    TextLabelDrumSynthNoise : TTextLabel;
    TextLabelDrumSynthClick : TTextLabel;
    InputDrumSynthVel : TInput;
    TextLabelDrumSynthVel : TTextLabel;
    TextLabelDrumSynthDcy : TTextLabel;
    TextLabelDrumSynthFreq : TTextLabel;
    InputDrumSynthTrig : TInput;
    TextLabelDrumSynthTrig : TTextLabel;
    TextLabelDrumSynthRes : TTextLabel;
    TextLabelDrumSynthSwp : TTextLabel;
    TextLabelDrumSynthOsc : TTextLabel;
    TextLabelDrumSynthMaster : TTextLabel;
    TextLabelDrumSynthBend : TTextLabel;
    TextLabelDrumSynthSlave : TTextLabel;
    TextLabelDrumSynthTune : TTextLabel;
    TextLabelDrumSynthBendAmt : TTextLabel;
    TextLabelDrumSynthLvl : TTextLabel;
    TextLabelDrumSynthBendDcy : TTextLabel;
    IndicatorDrumSynthTrig : TIndicator;
    TextLabelDrumSynthPreset : TTextLabel;
    ImageDrumSynthOscType : TGraphicImage;
    SmallKnobDrumSynthMasterTune : TSmallKnob;
    SmallKnobDrumSynthMasterDecay : TSmallKnob;
    SmallKnobDrumSynthClick : TSmallKnob;
    DisplayDrumSynthMaster : TDisplay;
    SmallKnobDrumSynthNoiseRes : TSmallKnob;
    SmallKnobDrumSynthNoiseSwp : TSmallKnob;
    SmallKnobDrumSynthMasterLevel : TSmallKnob;
    DisplayDrumSynthSlave : TDisplay;
    SmallKnobDrumSynthSlaveTune : TSmallKnob;
    SmallKnobDrumSynthSlaveDecay : TSmallKnob;
    SmallKnobDrumSynthSlaveLevel : TSmallKnob;
    DisplayDrumSynthPreset : TDisplay;
    SmallKnobDrumSynthNoiseFreq : TSmallKnob;
    SmallKnobDrumSynthNoiseDcy : TSmallKnob;
    SmallKnobDrumSynthBendDecay : TSmallKnob;
    SmallKnobDrumSynthBendAmount : TSmallKnob;
    SmallKnobDrumSynthNoise : TSmallKnob;
    SpinnerDrumSynthPreset : TSpinner;
    ButtonSetDrumSynthFilterType : TButtonSet;
    ButtonSetDrumSynthMute : TButtonSet;
  End;

  T_LFOA = Class( TNMModule)
    BoxLFOARate : TBox;
    ImageLFOALineRate : TGraphicImage;
    BoxLFOAGraph : TBox;
    BoxLFOAPhase : TBox;
    EditLabelLFOA : TEditLabel;
    OutputLFOAOut : TOutput;
    IndicatorLFOAOut : TIndicator;
    TextLabelLFOAPhase : TTextLabel;
    InputLFOARate : TInput;
    TextLabelLFOARst : TTextLabel;
    SetterLFOAKBT : TSetter;
    TextLabelLFOARate : TTextLabel;
    TextLabelLFOAKBT : TTextLabel;
    OutputLFOASlv : TOutput;
    ImageLFOARst : TGraphicImage;
    InputLFOARst : TInput;
    TextLabelLFOASlv : TTextLabel;
    OscGraphLFOA : TOscGraph;
    KnobLFOARate : TKnob;
    SmallKnobLFOAKBT : TSmallKnob;
    DisplayLFOARate : TDisplay;
    SmallKnobLFOAPhase : TSmallKnob;
    DisplayLFOAPhase : TDisplay;
    SmallKnobLFOARateMod : TSmallKnob;
    ButtonSetLFOAWave : TButtonSet;
    ButtonSetLFOARange : TButtonSet;
    ButtonSetLFOAMute : TButtonSet;
    ButtonSetLFOAMono : TButtonSet;
  End;

  T_LFOB = Class( TNMModule)
    BoxLFOBPW : TBox;
    BoxLFOBRate : TBox;
    ImageLFOBLineRate : TGraphicImage;
    ImageLFOBLinePW : TGraphicImage;
    BoxLFOBGraph : TBox;
    BoxLFOBPhase : TBox;
    EditLabelLFOB : TEditLabel;
    OutputLFOBOut : TOutput;
    IndicatorLFOBOut : TIndicator;
    TextLabelLFOBPhase : TTextLabel;
    InputLFOBRate : TInput;
    TextLabelLFOBRst : TTextLabel;
    SetterLFOBKBT : TSetter;
    TextLabelLFOBRate : TTextLabel;
    TextLabelLFOBKBT : TTextLabel;
    OutputLFOBSlv : TOutput;
    ImageLFOBRst : TGraphicImage;
    InputLFOBRst : TInput;
    TextLabelLFOBSlv : TTextLabel;
    OscGraphLFOB : TOscGraph;
    SetterLFOBPW : TSetter;
    InputLFOBPW : TInput;
    TextLabelLFOBPW : TTextLabel;
    KnobLFOBRate : TKnob;
    SmallKnobLFOBKBT : TSmallKnob;
    DisplayLFOBRate : TDisplay;
    SmallKnobLFOBPhase : TSmallKnob;
    DisplayLFOBPhase : TDisplay;
    SmallKnobLFOBRateMod : TSmallKnob;
    SmallKnobLFOBPWMod : TSmallKnob;
    SmallKnobLFOBPW : TSmallKnob;
    ButtonSetLFOBRange : TButtonSet;
    ButtonSetLFOBMono : TButtonSet;
  End;

  T_PercOsc = Class( TNMModule)
    BoxPercOscPitch : TBox;
    ImagePercOscLinePitch : TGraphicImage;
    EditLabelPercOsc : TEditLabel;
    SetterPercOscFine : TSetter;
    TextLabelPercOscPitch : TTextLabel;
    TextLabelPercOscFine : TTextLabel;
    TextLabelPercOscClick : TTextLabel;
    OutputPercOscOut : TOutput;
    InputPercOscFreq : TInput;
    InputPercOscAmp : TInput;
    TextLabelPercOscAmp : TTextLabel;
    ImagePercOscTrig : TGraphicImage;
    InputPercOscTrig : TInput;
    TextLabelPercOscTrig : TTextLabel;
    ImagePercOscOscType : TGraphicImage;
    TextLabelPercOsecDecay : TTextLabel;
    KnobPercOscPitchCoarse : TKnob;
    SmallKnobPercOscPitchFine : TSmallKnob;
    DisplayPercOscFreq : TDisplay;
    SmallKnobPercOscClick : TSmallKnob;
    SmallKnobPercOscDecay : TSmallKnob;
    SmallKnobPercOscFreqMod : TSmallKnob;
    ButtonSetPercOscMute : TButtonSet;
    ButtonSetPercOscPunch : TButtonSet;
  End;

  T_OscSineBank = Class( TNMModule)
    ImageOscSineBankLineLevel5 : TGraphicImage;
    ImageOscSineBankLineLevel6 : TGraphicImage;
    ImageOscSineBankLineLevel3 : TGraphicImage;
    ImageOscSineBankLineLevel4 : TGraphicImage;
    ImageOscSineBankLineLevel2 : TGraphicImage;
    ImageOscSineBankLineLevel1 : TGraphicImage;
    EditLabelOscSineBank : TEditLabel;
    BoxOscSineBankBottom : TBox;
    InputOscSineBankMst : TInput;
    TextLabelOscSineBankMst : TTextLabel;
    ImageOscSineBankSync : TGraphicImage;
    InputOscSineBankSync : TInput;
    TextLabelOscSineBankSync : TTextLabel;
    InputOscSineBankMixIn : TInput;
    TextLabelOscSineBankMixIn : TTextLabel;
    OutputSineBankOscOut : TOutput;
    TextLabelOscSineBankSlaveBank : TTextLabel;
    TextLabelOsc1 : TTextLabel;
    TextLabelOscSineBankLevel1 : TTextLabel;
    SetterOscSineBankFine1 : TSetter;
    InputOscSineBank1 : TInput;
    TextLabelOscSineBankTune1 : TTextLabel;
    TextLabelOsc2 : TTextLabel;
    TextLabelOscSineBankLevel2 : TTextLabel;
    SetterOscSineBankFine2 : TSetter;
    InputOscSineBank2 : TInput;
    TextLabelOscSineBankTune2 : TTextLabel;
    TextLabelOsc3 : TTextLabel;
    TextLabelOscSineBankLevel3 : TTextLabel;
    SetterOscSineBankFine3 : TSetter;
    InputOscSineBank3 : TInput;
    TextLabelOscSineBankTune3 : TTextLabel;
    TextLabelOsc4 : TTextLabel;
    TextLabelOscSineBankLevel4 : TTextLabel;
    SetterOscSineBankFine4 : TSetter;
    InputOscSineBank4 : TInput;
    TextLabelOscSineBankTune4 : TTextLabel;
    TextLabelOsc5 : TTextLabel;
    TextLabelOscSineBankLevel5 : TTextLabel;
    SetterOscSineBankFine5 : TSetter;
    InputOscSineBank5 : TInput;
    TextLabelOscSineBankTune5 : TTextLabel;
    TextLabelOsc6 : TTextLabel;
    TextLabelOscSineBankLevel6 : TTextLabel;
    SetterOscSineBankFine6 : TSetter;
    InputOscSineBank6 : TInput;
    TextLabelOscSineBankTune6 : TTextLabel;
    SmallKnobOscSineBankTuneCoarse1 : TSmallKnob;
    SmallKnobOscSineBankTuneFine1 : TSmallKnob;
    SmallKnobOscSineBankLevel1 : TSmallKnob;
    DisplayOscSinBankFreq1 : TDisplay;
    SmallKnobOscSineBankTuneCoarse2 : TSmallKnob;
    SmallKnobOscSineBankTuneFine2 : TSmallKnob;
    SmallKnobOscSineBankLevel2 : TSmallKnob;
    DisplayOscSinBankFreq2 : TDisplay;
    SmallKnobOscSineBankTuneCoarse3 : TSmallKnob;
    SmallKnobOscSineBankTuneFine3 : TSmallKnob;
    SmallKnobOscSineBankLevel3 : TSmallKnob;
    DisplayOscSinBankFreq3 : TDisplay;
    SmallKnobOscSineBankTuneCoarse4 : TSmallKnob;
    SmallKnobOscSineBankTuneFine4 : TSmallKnob;
    SmallKnobOscSineBankLevel4 : TSmallKnob;
    DisplayOscSinBankFreq4 : TDisplay;
    SmallKnobOscSineBankTuneCoarse5 : TSmallKnob;
    SmallKnobOscSineBankTuneFine5 : TSmallKnob;
    SmallKnobOscSineBankLevel5 : TSmallKnob;
    DisplayOscSinBankFreq5 : TDisplay;
    SmallKnobOscSineBankTuneCoarse6 : TSmallKnob;
    SmallKnobOscSineBankTuneFine6 : TSmallKnob;
    SmallKnobOscSineBankLevel6 : TSmallKnob;
    DisplayOscSinBankFreq6 : TDisplay;
    SpinnerOscSineBankPartials1 : TSpinner;
    SpinnerOscSineBankPartials2 : TSpinner;
    SpinnerOscSineBankPartials3 : TSpinner;
    SpinnerOscSineBankPartials4 : TSpinner;
    SpinnerOscSineBankPartials5 : TSpinner;
    SpinnerOscSineBankPartials6 : TSpinner;
    ButtonSetOscSineBankMute1 : TButtonSet;
    ButtonSetOscSineBankMute2 : TButtonSet;
    ButtonSetOscSineBankMute3 : TButtonSet;
    ButtonSetOscSineBankMute4 : TButtonSet;
    ButtonSetOscSineBankMute6 : TButtonSet;
    ButtonSetOscSineBankMute5 : TButtonSet;
  End;

  T_OscSlvFM = Class( TNMModule)
    ImageOscSlvFMLineFMB : TGraphicImage;
    EditLabelOscSlvFM : TEditLabel;
    SetterOscSlvFMFine : TSetter;
    TextLabelOscSlvFMDetune : TTextLabel;
    TextLabelOscSlvFMFine : TTextLabel;
    TextLabelOscSlvFMMst : TTextLabel;
    OutputOscSlvFMOut : TOutput;
    InputOscSlvFMFMB : TInput;
    TextLabelOscSlvFMB : TTextLabel;
    TextLabelOscSlvFMPartials : TTextLabel;
    InputOscSlvFMMst : TInput;
    ImageOscSlvFMOscType : TGraphicImage;
    ImageOscSlvFMSync : TGraphicImage;
    InputOscSlvFMSync : TInput;
    TextLabelOscSlvFMSync : TTextLabel;
    KnobOscSlvFMDetuneCoarse : TKnob;
    SmallKnobOscSlvFMDetuneFine : TSmallKnob;
    DisplayOscSlvFMFreq : TDisplay;
    SmallKnobOscSlvFMFMB : TSmallKnob;
    SpinnerOscSlvFMPartials : TSpinner;
    ButtonSetOscSlvFMMute : TButtonSet;
    ButtonSetOscSlvFMOctDown : TButtonSet;
  End;

  T_Noise = Class( TNMModule)
    EditLabelNoise : TEditLabel;
    TextLabelNoiseWhite : TTextLabel;
    TextLabelNoiseColored : TTextLabel;
    OutputNoiseOut : TOutput;
    ImageNoiceOscType : TGraphicImage;
    SmallKnobNoiseColor : TSmallKnob;
  End;

  T_PolyAreaIn = Class( TNMModule)
    BoxPolyAreaInLeft : TBox;
    BoxPolyAreaInRight : TBox;
    EditLabelPOlyAreaIn : TEditLabel;
    TextLabelPolyAreaInLeft : TTextLabel;
    IndicatorPolyAreaInLeft : TIndicator;
    OutputPolyAreaInLeft : TOutput;
    TextLabelPolyAreaInRight : TTextLabel;
    IndicatorPolyAreaInRight : TIndicator;
    OutputPolyAreaInRight : TOutput;
    LedBarPolyAreaInLeft : TLedBar;
    LedBarPolyAreaInRight : TLedBar;
    ButtonSetPolyAreaInLevel : TButtonSet;
  End;

  T_1Output = Class( TNMModule)
    EditLabel1Output : TEditLabel;
    TextLabel1OutputDest : TTextLabel;
    Input1OutputIn : TInput;
    TextLabel1OutputMix : TTextLabel;
    TextLabel1OutputLevel : TTextLabel;
    TextLabel1OutputCVA : TTextLabel;
    Knob1OutputLevel : TKnob;
    ButtonSet1OutputRouting : TButtonSet;
    ButtonSet1OutputMute : TButtonSet;
  End;

  T_2Outputs = Class( TNMModule)
    EditLabel2Output : TEditLabel;
    TextLabel2OutputsDest : TTextLabel;
    Input2OutputsL : TInput;
    TextLabel2OutputsMixL : TTextLabel;
    TextLabel2OutputsLevel : TTextLabel;
    Input2OutputsR : TInput;
    TextLabel2OutputsMixR : TTextLabel;
    Knob2OutputsLevel : TKnob;
    ButtonSet2OutputsRouting : TButtonSet;
    ButtonSet2OutputsMute : TButtonSet;
  End;

  T_AudioIn = Class( TNMModule)
    BoxAudioInLeft : TBox;
    BoxAudioInRight : TBox;
    EditLabelAudioIn : TEditLabel;
    TextLabelAudioInLeft : TTextLabel;
    IndicatorAudioInLeft : TIndicator;
    OutputAudioInLeft : TOutput;
    TextLabelAudioInRight : TTextLabel;
    IndicatorAudioInRight : TIndicator;
    OutputAudioInRight : TOutput;
    LedBarAudioInLeft : TLedBar;
    LedBarAudioInRight : TLedBar;
  End;

  T_Keyboard = Class( TNMModule)
    EditLabelKeyBoard : TEditLabel;
    TextLabelKeyBoardNote : TTextLabel;
    TextLabelKeyBoardGate : TTextLabel;
    TextLabelKeyBoardVel : TTextLabel;
    TextLabelKeyBoardRel : TTextLabel;
    TextLabelKeyBoardRelVel : TTextLabel;
    OutputKeyBoardNote : TOutput;
    OutputKeyBoardGate : TOutput;
    OutputKeyBoardVel : TOutput;
    OutputKeyBoardRelVel : TOutput;
  End;

  T_KeyboardPatch = Class( TNMModule)
    EditLabelKbdPatch : TEditLabel;
    TextLabelKbdPatchLatestNote : TTextLabel;
    TextLabelKbdPatchLatestGate : TTextLabel;
    TextLabelKbdPatchLatestVelOn : TTextLabel;
    TextLabelKbdPatchRelVel : TTextLabel;
    OutputKbdPatchLatestNote : TOutput;
    OutputKbdPatchLatestGate : TOutput;
    OutputKbdPatchLatestVelOn : TOutput;
    OutputKbdPatchLatestRelVel : TOutput;
    TextLabelKbdPatchVelOn : TTextLabel;
    TextLabelKbdPatchLatestRelVel : TTextLabel;
  End;

  T_MidiGlobal = Class( TNMModule)
    EditLabelMidiGlobal : TEditLabel;
    TextLabelMidiGlobalClock : TTextLabel;
    TextLabelMidiGlobalSync : TTextLabel;
    TextLabelMidiGlobalActive : TTextLabel;
    OutputMidiGlobalClock : TOutput;
    OutputMidiGlobalSync : TOutput;
    OutputMidiGlobalActive : TOutput;
  End;

  T_OscA = Class( TNMModule)
    BoxOscAPWidth : TBox;
    ImageOscALinePWidth : TGraphicImage;
    ImageOscALineFMA : TGraphicImage;
    ImageOscALinePitch2 : TGraphicImage;
    ImageOscALinePitch1 : TGraphicImage;
    EditLabelOscA : TEditLabel;
    InputOscAPitch1 : TInput;
    TextLabelOscAPitch1 : TTextLabel;
    InputOscAPitch2 : TInput;
    TextLabelOscAPitch2 : TTextLabel;
    SetterOscAFreqFine : TSetter;
    OutputOscASlv : TOutput;
    TextLabelOscACoarse : TTextLabel;
    TextLabelOscAFine : TTextLabel;
    TextLabelOscAFreq : TTextLabel;
    SetterOscAKBT : TSetter;
    TextLabelOscAKBT : TTextLabel;
    InputOscAFMA : TInput;
    TextLabelOscAFMA : TTextLabel;
    ImageOscASync : TGraphicImage;
    InputOscASync : TInput;
    TextLabelOscASysnc : TTextLabel;
    SetterOscAPWidth : TSetter;
    TextLabelOscAPWidth : TTextLabel;
    OutputOscAOut : TOutput;
    InputOscAPWidth : TInput;
    TextLabelOscASlv : TTextLabel;
    KnobOscAFreqCoarse : TKnob;
    SmallKnobOscAFreqFine : TSmallKnob;
    DisplayOscAFreq : TDisplay;
    SmallKnobOscAPitch1 : TSmallKnob;
    SmallKnobOscAPitch2 : TSmallKnob;
    SmallKnobOscAKBT : TSmallKnob;
    SmallKnobOscAFMA : TSmallKnob;
    SmallKnobOscAPWidth : TSmallKnob;
    SmallKnobOscAPWidthMod : TSmallKnob;
    ButtonSetOscAWave : TButtonSet;
    ButtonSetOscAMute : TButtonSet;
  End;

  T_OscB = Class( TNMModule)
    ImageOscBLineFMA : TGraphicImage;
    ImageOscBLinePitch2 : TGraphicImage;
    ImageOscBLinePitch1 : TGraphicImage;
    BoxOscBPWidth : TBox;
    ImageOscCLinePWidth : TGraphicImage;
    EditLabelOscB : TEditLabel;
    InputOscBPitch1 : TInput;
    TextLabelOscBPitch1 : TTextLabel;
    InputOscBPitch2 : TInput;
    TextLabelOscBPitch2 : TTextLabel;
    SetterOscBFreqFine : TSetter;
    OutputOscBSlv : TOutput;
    TextLabelOscBCoarse : TTextLabel;
    TextLabelOscBFine : TTextLabel;
    TextLabelOscBFreq : TTextLabel;
    SetterOscBKBT : TSetter;
    TextLabelOscBKBT : TTextLabel;
    InputOscBFMA : TInput;
    TextLabelOscBFMA : TTextLabel;
    TextLabelOscBPWidth : TTextLabel;
    OutputOscBOut : TOutput;
    TextLabelOscBSlv : TTextLabel;
    InputOscBPWidth : TInput;
    KnobOscBFreqCoarse : TKnob;
    SmallKnobOscBFreqFine : TSmallKnob;
    DisplayOscBFreq : TDisplay;
    SmallKnobOscBPitch1 : TSmallKnob;
    SmallKnobOscBPitch2 : TSmallKnob;
    SmallKnobOscBKBT : TSmallKnob;
    SmallKnobOscBFMA : TSmallKnob;
    SmallKnobOscBPWidth : TSmallKnob;
    ButtonSetOscBWave : TButtonSet;
    ButtonSetOscBMute : TButtonSet;
  End;

  T_OscC = Class( TNMModule)
    BoxOSCCFMA : TBox;
    ImageOscCLineFMA : TGraphicImage;
    EditLabelOscC : TEditLabel;
    SetterOscCFreqFine : TSetter;
    OutputOscCSlv : TOutput;
    TextLabelOscCCoarse : TTextLabel;
    TextLabelOscFine : TTextLabel;
    TextLabelOscSlave : TTextLabel;
    InputOscCPitch : TInput;
    ImageOscCType : TGraphicImage;
    InputOscCFMA : TInput;
    TextLabelOscCFMA : TTextLabel;
    OutputOscCOut : TOutput;
    InputOscCAM : TInput;
    TextLabelOscCAM : TTextLabel;
    TextLabelOscFreq : TTextLabel;
    ImageOscCLineFreqMod : TGraphicImage;
    KnobOscCFreqCoarse : TKnob;
    SmallKnobOscCFreqFine : TSmallKnob;
    DisplayOscCFreq : TDisplay;
    SmallKnobOscPitch : TSmallKnob;
    SmallKnobOscCFMA : TSmallKnob;
    ButtonSetOscCMute : TButtonSet;
    ButtonSetOscCKBT : TButtonSet;
  End;

  T_MasterOsc = Class( TNMModule)
    ImageMasterOscLinePitch2 : TGraphicImage;
    ImageMasterOscLinePitch1 : TGraphicImage;
    EditLabelMasterOsc : TEditLabel;
    InputMasterOscPitch1 : TInput;
    TextLabelMasterOscPitch1 : TTextLabel;
    InputMasterOscPitch2 : TInput;
    TextLabelMasterOscPitch2 : TTextLabel;
    SetterMasterOscFine : TSetter;
    OutputMasterOscSlv : TOutput;
    TextLabelMasterOscCoarse : TTextLabel;
    TextLabelMasterOscFine : TTextLabel;
    TextLabelMasterOscSlv : TTextLabel;
    KnobMasterOscFreqCoarse : TKnob;
    SmallKnobMasterOscFreqFine : TSmallKnob;
    DisplayMasterOscFreq : TDisplay;
    SmallKnobMasterOscPitch1 : TSmallKnob;
    SmallKnobMasterOscPitch2 : TSmallKnob;
    ButtonSetMasterOscKBT : TButtonSet;
  End;

  T_4Outputs = Class( TNMModule)
    EditLabel4Outputs : TEditLabel;
    Input4Outputs1 : TInput;
    TextLabel4OutputsMix1 : TTextLabel;
    TextLabel4OutputsLevel : TTextLabel;
    Input4Outputs2 : TInput;
    TextLabel4OutputsMix2 : TTextLabel;
    Input4Outputs3 : TInput;
    TextLabel4OutputsMix3 : TTextLabel;
    Input4Outputs4 : TInput;
    TextLabel4OutputsMix4 : TTextLabel;
    Knob4OutputsLevel : TKnob;
  End;

  T_NoteDetect = Class( TNMModule)
    EditLabelNoteDetect : TEditLabel;
    TextLabelNoteDetectGate : TTextLabel;
    TextLabelNoteDetectVel : TTextLabel;
    TextLabelNoteDetectRel : TTextLabel;
    TextLabelNoteDetectRelVel : TTextLabel;
    OutputNoteDetectGate : TOutput;
    OutputNoteDetectVel : TOutput;
    OutputNoteDetectRelVel : TOutput;
    SmallKnobNoteDetectNote : TSmallKnob;
    DisplayNoteDetectNote : TDisplay;
  End;

  T_KeybSplit = Class( TNMModule)
    EditLabelKeybSplit : TEditLabel;
    TextLabelKeybSplitLower : TTextLabel;
    TextLabelKeybSplitUpper : TTextLabel;
    InputKeybSplitNote : TInput;
    InputKeybSplitGate : TInput;
    InputKeybSplitVel : TInput;
    OutputKeybSplitNote : TOutput;
    OutputKeybSplitGate : TOutput;
    OutputKeybSplitVel : TOutput;
    TextLabelKeybSplitNote : TTextLabel;
    TextLabelKeybSplitGate : TTextLabel;
    TextLabelKeybSplitVel : TTextLabel;
    SmallKnobKeybSplitLower : TSmallKnob;
    DisplayKeybSplitLower : TDisplay;
    SmallKnobKeybSplitUpper : TSmallKnob;
    DisplayKeybSplitUpper : TDisplay;
  End;

  T_LFOC = Class( TNMModule)
    ImageLFOCLineRate : TGraphicImage;
    EditLabelLFOC : TEditLabel;
    OutputLFOCOut : TOutput;
    IndicatorLFOCOut : TIndicator;
    InputLFOCRate : TInput;
    TextLabelLFOCRate : TTextLabel;
    OutputLFOCSlv : TOutput;
    TextLabelLFOCSlv : TTextLabel;
    KnobLFOCRate : TKnob;
    DisplayLFOCRate : TDisplay;
    SmallKnobLFOCRateMod : TSmallKnob;
    ButtonSetLFOCWave : TButtonSet;
    ButtonSetLFOCRange : TButtonSet;
    ButtonSetLFOCMute : TButtonSet;
    ButtonSetLFOCMono : TButtonSet;
  End;

  T_FilterB = Class( TNMModule)
    ImageFilterBLine : TGraphicImage;
    EditLabelFilterB : TEditLabel;
    OutputFilterBOut : TOutput;
    ImageFilterBFilterType : TGraphicImage;
    InputFilterBIn : TInput;
    SmallKnobFilterBFreq : TSmallKnob;
    DisplayFilterBFreq : TDisplay;
  End;

  T_FilterC = Class( TNMModule)
    ImageFilterCLineSplit : TGraphicImage;
    BoxFilterCReso : TBox;
    EditLabelFilterC : TEditLabel;
    OutputFilterCOutHP : TOutput;
    OutputFilterCOutBP : TOutput;
    OutputFilterCOutLP : TOutput;
    InputFilterCIn : TInput;
    BoxFilterCFreq : TBox;
    TextLabelFilterCTitle : TTextLabel;
    TextLabelFilterCReso : TTextLabel;
    TextLabelFilterCFreq : TTextLabel;
    TextLabelFilterCHP : TTextLabel;
    TextLabelFilterCBP : TTextLabel;
    TextLabelFilterCLP : TTextLabel;
    DisplayFilterCReso : TDisplay;
    KnobFilterCFreq : TKnob;
    KnobFilterCReso : TKnob;
    DisplayFilterCFreq : TDisplay;
    ButtonSetFilterDGC : TButtonSet;
  End;

  T_FilterD = Class( TNMModule)
    BoxFilterDFreq : TBox;
    ImageFilterDLineFreq : TGraphicImage;
    ImageFilterDLineSplit : TGraphicImage;
    BoxFilterDReso : TBox;
    EditLabelFilterD : TEditLabel;
    OutputFilterDHPOut : TOutput;
    OutputFilterDBPOut : TOutput;
    OutputFilterDLPOut : TOutput;
    InputFilterDIn : TInput;
    TextLabelFilterDTitle : TTextLabel;
    TextLabelReso : TTextLabel;
    TextLabelFilterDFreq : TTextLabel;
    InputFilterDFreqMod : TInput;
    SetterFilterDKBT : TSetter;
    TextLabelFilterDKBT : TTextLabel;
    TextLabelFilterDLP : TTextLabel;
    TextLabelFilterDBP : TTextLabel;
    TextLabelFilterDHP : TTextLabel;
    DisplayFilterDReso : TDisplay;
    KnobFilterDFreq : TKnob;
    KnobFilterDReso : TKnob;
    DisplayFilterDFreq : TDisplay;
    SmallKnobFilterDFreqMod : TSmallKnob;
    SmallKnobFilterDKBT : TSmallKnob;
  End;

  T_FilterA = Class( TNMModule)
    ImageFilterALine : TGraphicImage;
    EditLabelFilterA : TEditLabel;
    OutputFilterAOut : TOutput;
    ImageFilterAFilterType : TGraphicImage;
    InputFilterAIn : TInput;
    SmallKnobFilterAFreq : TSmallKnob;
    DisplayFilterAFreq : TDisplay;
  End;

  T_AHDEnv = Class( TNMModule)
    BoxAHD_EnvD : TBox;
    BoxAHD_EnvH : TBox;
    BoxAHD_EnvA : TBox;
    ImageAHD_EnvAmp : TGraphicImage;
    BoxAHD_EnvAHDGraph : TBox;
    EditLabelAHD_Env : TEditLabel;
    OutputAHD_EnvEnv : TOutput;
    IndicatorAHD_EnvTrig : TIndicator;
    InputAHD_EnvAmp : TInput;
    TextLabelAHD_EnvTrig : TTextLabel;
    TextLabelAHD_EnvAmp : TTextLabel;
    OutputAHD_EnvOut : TOutput;
    ImageAHD_EnvTrig : TGraphicImage;
    InputAHD_EnvTrig : TInput;
    TextLabelAHD_EnvA : TTextLabel;
    TextLabelAHD_EnvH : TTextLabel;
    TextLabelAHD_EnvD : TTextLabel;
    InputAHD_EnvIn : TInput;
    TextLabelAHD_EnvEnv : TTextLabel;
    InputAHD_EnvA : TInput;
    InputAHD_EnvH : TInput;
    InputAHD_EnvD : TInput;
    AHDGraphAHD_Env : TAHDGraph;
    SmallKnobAHD_EnvA : TSmallKnob;
    DisplayAHD_EnvA : TDisplay;
    SmallKnobAHD_EnvH : TSmallKnob;
    DisplayAHD_EnvH : TDisplay;
    SmallKnobAHD_EnvD : TSmallKnob;
    DisplayAHD_EnvD : TDisplay;
    SmallKnobAHD_EnvAMod : TSmallKnob;
    SmallKnobAHD_EnvHMod : TSmallKnob;
    SmallKnobAHD_EnvDMod : TSmallKnob;
  End;

  T_MultiEnv = Class( TNMModule)
    ImageMulti_EnvAmp : TGraphicImage;
    BoxMulti_EnvMultiEnvGraph : TBox;
    EditLabelMulti_Env : TEditLabel;
    OutputMulti_EnvEnv : TOutput;
    IndicatorMulti_EnvGate : TIndicator;
    TextLabelMulti_EnvL1 : TTextLabel;
    InputMulti_EnvAmp : TInput;
    TextLabelMulti_EnvAmp : TTextLabel;
    OutputMulti_EnvOLut : TOutput;
    TextLabelMulti_EnvGate : TTextLabel;
    InputMulti_EnvGate : TInput;
    TextLabelMulti_EnvL2 : TTextLabel;
    TextLabelMulti_EnvL3 : TTextLabel;
    TextLabelMulti_EnvL4 : TTextLabel;
    InputMulti_EnvIn : TInput;
    TextLabelMulti_EnvEnv : TTextLabel;
    MultiEnvGraphMulti_Env : TMultiEnvGraph;
    TextLabelMulti_EnvT1 : TTextLabel;
    TextLabelMulti_EnvT2 : TTextLabel;
    TextLabelMulti_EnvT3 : TTextLabel;
    TextLabelMulti_EnvT4 : TTextLabel;
    TextLabelMulti_EnvT5 : TTextLabel;
    TextLabelMulti_EnvSustain : TTextLabel;
    SmallKnobMulti_EnvL1 : TSmallKnob;
    DisplayMulti_EnvL1 : TDisplay;
    SmallKnobMulti_EnvL2 : TSmallKnob;
    DisplayMulti_EnvL2 : TDisplay;
    SmallKnobMulti_EnvL3 : TSmallKnob;
    DisplayMulti_EnvL3 : TDisplay;
    SmallKnobMulti_EnvL4 : TSmallKnob;
    DisplayMulti_EnvL4 : TDisplay;
    SmallKnobMulti_EnvT1 : TSmallKnob;
    SmallKnobMulti_EnvT2 : TSmallKnob;
    SmallKnobMulti_EnvT3 : TSmallKnob;
    SmallKnobMulti_EnvT4 : TSmallKnob;
    DisplayMulti_EnvT1 : TDisplay;
    DisplayMulti_EnvT2 : TDisplay;
    DisplayMulti_EnvT3 : TDisplay;
    DisplayMulti_EnvT4 : TDisplay;
    SmallKnobMulti_EnvT5 : TSmallKnob;
    DisplayMulti_EnvT5 : TDisplay;
    SmallKnobMulti_EnvSustain : TSmallKnob;
    DisplayMulti_EnvSustain : TDisplay;
    ButtonSetMulti_EnvShape : TButtonSet;
  End;

  T_EnvFollower = Class( TNMModule)
    ImageEnvFollowerThing : TGraphicImage;
    EditLabelEnvFollower : TEditLabel;
    TextLabelEnvFollowerRelease : TTextLabel;
    TextLabelEnvFollowerAttack : TTextLabel;
    OutputEnvFollowerOut : TOutput;
    InputEnvFollowerIn : TInput;
    SmallKnobEnvFollowerAttack : TSmallKnob;
    DisplayEnvFollowerAttack : TDisplay;
    SmallKnobEnvFollowerRelease : TSmallKnob;
    DisplayEnvFollowerRelease : TDisplay;
  End;

  T_FilterBank = Class( TNMModule)
    BoxFilterBankGraph : TBox;
    EditLabelFilterBank : TEditLabel;
    ClickerFilterBankPresetMin : TClicker;
    ClickerFilterBankPresetMax : TClicker;
    TextLabelFilterBankPresets : TTextLabel;
    ImageFilterBankIO : TGraphicImage;
    OutputFilterBankOut : TOutput;
    InputFilterBankIn : TInput;
    TextLabelFilterBankLabels : TTextLabel;
    BarGraphFilterBank : TBarGraph;
    SpinnerFilterBank01 : TSpinner;
    SpinnerFilterBank02 : TSpinner;
    SpinnerFilterBank03 : TSpinner;
    SpinnerFilterBank04 : TSpinner;
    SpinnerFilterBank05 : TSpinner;
    SpinnerFilterBank06 : TSpinner;
    SpinnerFilterBank07 : TSpinner;
    SpinnerFilterBank08 : TSpinner;
    SpinnerFilterBank09 : TSpinner;
    SpinnerFilterBank10 : TSpinner;
    SpinnerFilterBank11 : TSpinner;
    SpinnerFilterBank12 : TSpinner;
    SpinnerFilterBank13 : TSpinner;
    SpinnerFilterBank14 : TSpinner;
    ButtonSetFilterBankBypass : TButtonSet;
  End;

  T_EqMid = Class( TNMModule)
    BoxEqMidGraph : TBox;
    EditLabelEqMid : TEditLabel;
    BoxEqMidFreq : TBox;
    TextLabelEqMidFreq : TTextLabel;
    ImageEqMidIO : TGraphicImage;
    OutputEqMidOut : TOutput;
    InputEqMidIn : TInput;
    TextLabelEqMidGain : TTextLabel;
    TextLabelEqMidBW : TTextLabel;
    OscGraphEqMid : TOscGraph;
    IndicatorEqMidClip : TIndicator;
    KnobEqMidFreq : TKnob;
    DisplayEqMidFreq : TDisplay;
    SmallKnobEqMidInputGain : TSmallKnob;
    SmallKnobEqMidGain : TSmallKnob;
    DisplayEqMidGain : TDisplay;
    SmallKnobEqMidBW : TSmallKnob;
    DisplayEqMidBW : TDisplay;
    ButtonSetEqMidBypass : TButtonSet;
  End;

  T_EqShelving = Class( TNMModule)
    BoxEqShelvingGraph : TBox;
    EditLabelEqShelving : TEditLabel;
    BoxEqShelvingFreq : TBox;
    TextLabelEqShelvingFreq : TTextLabel;
    ImageEqShelvingIO : TGraphicImage;
    OutputEqShelvingOut : TOutput;
    InputEqShelvingIn : TInput;
    TextLabelEqShelvingGain : TTextLabel;
    OscGraphEqShelving : TOscGraph;
    IndicatorEqShelvingClip : TIndicator;
    KnobEqShelvingFreq : TKnob;
    DisplayEqShelvingFreq : TDisplay;
    SmallKnobEqShelvingInputGain : TSmallKnob;
    SmallKnobEqShelvingGain : TSmallKnob;
    DisplayEqShelvingGain : TDisplay;
    ButtonSetEqShelvingType : TButtonSet;
    ButtonSetEqShelvingBypass : TButtonSet;
  End;

  T_Vocoder = Class( TNMModule)
    BoxVocoderMain : TBox;
    EditLabelVocoder : TEditLabel;
    ImageVocoderIO : TGraphicImage;
    OutputVocoderOut : TOutput;
    InputVocoderIn : TInput;
    TextLabelVocoderAnalysis : TTextLabel;
    SetterVocoderOutGain : TSetter;
    TextLabelVocoderGain : TTextLabel;
    InputVocoderCtrl : TInput;
    TextLabelVocoderCtrl : TTextLabel;
    ClickerVocoderPresetMin2 : TClicker;
    ClickerVocoderPresetMin1 : TClicker;
    ClickerVocoderPreset1 : TClicker;
    ClickerVocoderPreset0 : TClicker;
    ClickerVocoderPrestInv : TClicker;
    ClickerVocoderPreset2 : TClicker;
    TextLabelVocoderOutput : TTextLabel;
    ClickerVocoderPresetRnd : TClicker;
    TextLabelVocoderLabels : TTextLabel;
    VocoderGraph : TVocoderGraph;
    TextLabelVocoderPresets : TTextLabel;
    SmallKnobVocoderOutGain : TSmallKnob;
    SpinnerVocoderBand01 : TSpinner;
    SpinnerVocoderBand02 : TSpinner;
    SpinnerVocoderBand03 : TSpinner;
    SpinnerVocoderBand04 : TSpinner;
    SpinnerVocoderBand05 : TSpinner;
    SpinnerVocoderBand06 : TSpinner;
    SpinnerVocoderBand07 : TSpinner;
    SpinnerVocoderBand08 : TSpinner;
    SpinnerVocoderBand09 : TSpinner;
    SpinnerVocoderBand10 : TSpinner;
    SpinnerVocoderBand11 : TSpinner;
    SpinnerVocoderBand12 : TSpinner;
    SpinnerVocoderBand13 : TSpinner;
    SpinnerVocoderBand14 : TSpinner;
    SpinnerVocoderBand15 : TSpinner;
    SpinnerVocoderBand16 : TSpinner;
    ButtonSetVocoderFilter : TButtonSet;
    ButtonSetVocoderMonitor : TButtonSet;
  End;

  T_FilterE = Class( TNMModule)
    BoxFilterEReso : TBox;
    ImageFilterELineReso : TGraphicImage;
    InputFilterEResoMod : TInput;
    BoxFilterEFreq : TBox;
    ImageFilterELineFreqMod2 : TGraphicImage;
    ImageFilterELineFreqMod1 : TGraphicImage;
    BoxFilterEGraph : TBox;
    ImageFilterELineIO : TGraphicImage;
    EditLabelFilterE : TEditLabel;
    OutputFilterEOut : TOutput;
    InputFilterEIn : TInput;
    TextLabelFilterEReso : TTextLabel;
    TextLabelFilterEFreq : TTextLabel;
    InputFilterEFreqMod1 : TInput;
    SetterFilterEKBT : TSetter;
    TextLabelFilterEKBT : TTextLabel;
    InputFilterEFreqMod2 : TInput;
    OscGraphFilterE : TOscGraph;
    DisplayFilterEReso : TDisplay;
    KnobFilterEFreq : TKnob;
    KnobFilterEReso : TKnob;
    DisplayFilterEFreq : TDisplay;
    SmallKnobFilterEFreqMod1 : TSmallKnob;
    SmallKnobFilterEKBT : TSmallKnob;
    SmallKnobFilterEResoMod : TSmallKnob;
    SmallKnobFilterEFreqMod2 : TSmallKnob;
    ButtonSetFilterEType : TButtonSet;
    ButtonSetFilterESlope : TButtonSet;
    ButtonSetFilterEGC : TButtonSet;
    ButtonSetFilterEBypasss : TButtonSet;
  End;

  T_FilterF = Class( TNMModule)
    BoxFilterFFreq : TBox;
    ImageFilterFLineFreqMod1 : TGraphicImage;
    BoxFilterFGraph : TBox;
    ImageFilterFLineIO : TGraphicImage;
    BoxFilterFReso : TBox;
    EditLabelFilterF : TEditLabel;
    OutputFilterFOut : TOutput;
    InputFilterFIn : TInput;
    TextLabelFilterFTitle : TTextLabel;
    TextLabelFilterFReso : TTextLabel;
    TextLabelFilterFFreq : TTextLabel;
    InputFilterFFreqMod1 : TInput;
    SetterFilterFKBT : TSetter;
    TextLabelFilterFKBT : TTextLabel;
    InputFilterFFreqMod2 : TInput;
    OscGraphFilterF : TOscGraph;
    ImageLineFreqMod2 : TGraphicImage;
    KnobFilterFFreq : TKnob;
    DisplayFilterFFreq : TDisplay;
    SmallKnobFilterFFreqMod1 : TSmallKnob;
    SmallKnobFilterFKBT : TSmallKnob;
    SmallKnobFilterFFreqMod2 : TSmallKnob;
    KnobFilterFReso : TKnob;
    DisplayFilterFReso : TDisplay;
    ButtonSetFilterFSlope : TButtonSet;
    ButtonSetFilterFBypass : TButtonSet;
  End;

  T_VocalFilter = Class( TNMModule)
    BoxVocalFilterNavigator : TBox;
    ImageVocalFilterLineVowelMod : TGraphicImage;
    BoxVocalFilterFreq : TBox;
    ImageVocalFilterLineFreqMod : TGraphicImage;
    EditLabelVocalFilter : TEditLabel;
    ImageVocalFilterIO : TGraphicImage;
    OutputVocalFilterOut : TOutput;
    InputVocalFilterIn : TInput;
    TextLabelVocalFilterTitle : TTextLabel;
    TextLabelVocalFilterFreq : TTextLabel;
    SetterVocalFilterRes : TSetter;
    TextLabelVocalFilterRes : TTextLabel;
    SetterVocalFilterFreq : TSetter;
    InputVocalFilterFreqMod : TInput;
    InputVocalFilterVowelMod : TInput;
    DisplayVocalFilterVowel1 : TDisplay;
    KnobVocalFilterVowel : TKnob;
    SmallKnobVocalFilterOutLevel : TSmallKnob;
    SmallKnobVocalFilterRes : TSmallKnob;
    SmallKnobVocalFilterFreq : TSmallKnob;
    SmallKnobVocalFilterFreqMod : TSmallKnob;
    SmallKnobVocalFilterVowelMod : TSmallKnob;
    DisplayVocalFilterVowel2 : TDisplay;
    DisplayVocalFilterVowel3 : TDisplay;
    SmallKnobVocalFilterVowel1 : TSmallKnob;
    SmallKnobVocalFilterVowel2 : TSmallKnob;
    SmallKnobVocalFilterVowel3 : TSmallKnob;
  End;

  T_LFOSlvE = Class( TNMModule)
    EditLabelLFOSlvE : TEditLabel;
    TextLabelLFOSlvEMst : TTextLabel;
    OutputLFOSlvEOut : TOutput;
    ImageLFOSlvEType : TGraphicImage;
    InputLFOSlvEMst : TInput;
    IndicatorLFOSlvEOut : TIndicator;
    DisplayLFOSlvERate : TDisplay;
    SmallKnobLFOSlvERate : TSmallKnob;
  End;

  T_ClkGen = Class( TNMModule)
    EditLabelClkGen : TEditLabel;
    TextLabelClkGenRate : TTextLabel;
    OutputClkGen24 : TOutput;
    OutputClkGen4 : TOutput;
    OutputClkGenSync : TOutput;
    TextLabelClkGen24Pulses : TTextLabel;
    TextLabelClkGen4Pulses : TTextLabel;
    TextLabelClkGenSync : TTextLabel;
    TextLabelClkGenReset : TTextLabel;
    OutputClkGenSlv : TOutput;
    ImageClkGenReset : TGraphicImage;
    InputClkGenReset : TInput;
    TextLabelClkGenSlv : TTextLabel;
    SmallKnobClkGenRate : TSmallKnob;
    DisplayClkGenRate : TDisplay;
    ButtonSetClkGenOn : TButtonSet;
  End;

  T_ClkRndGen = Class( TNMModule)
    EditLabelRndGen : TEditLabel;
    TextLabelClkRndGenClk : TTextLabel;
    OutputClkRndGenOut : TOutput;
    ImageClkRndGenType : TGraphicImage;
    InputClkRndGenClk : TInput;
    ImageClkRndGenClk : TGraphicImage;
    ButtonSetClkRndGenMono : TButtonSet;
    ButtonSetClkRndGenCol : TButtonSet;
  End;

  T_LFOSlvD = Class( TNMModule)
    EditLabelLFOSlvD : TEditLabel;
    TextLabelLFOSlvDMst : TTextLabel;
    OutputLFOSlvDOut : TOutput;
    ImageLFOSlvDType : TGraphicImage;
    InputLFOSlvDMst : TInput;
    IndicatorLFOSlvDOut : TIndicator;
    DisplayLFOSlvDRate : TDisplay;
    SmallKnobLFOSlvDRate : TSmallKnob;
  End;

  T_LFOSlvA = Class( TNMModule)
    BoxLFOSlvARate : TBox;
    EditLabelLFOSlvA : TEditLabel;
    OutputLFOSlvAOut : TOutput;
    IndicatorLFOSlvAOut : TIndicator;
    TextLabelLfoSlvARate : TTextLabel;
    TextLabelLfoSlvAMst : TTextLabel;
    TextLabelLFOSlvARst : TTextLabel;
    InputLFOSlvARst : TInput;
    ImageLFOSlvARst : TGraphicImage;
    BoxLFOSlvAGraph : TBox;
    BoxLFOSlvAPhase : TBox;
    TextLabelLFOSlvAPhase : TTextLabel;
    OscGraphLFOSlvA : TOscGraph;
    InputLFOSlvAMst : TInput;
    KnobLFOSlvARate : TKnob;
    DisplayLFOSlvARate : TDisplay;
    SmallKnobLFOSlvAPhase : TSmallKnob;
    DisplayLFOSlvAPhase : TDisplay;
    ButtonSetLFOSlvAWave : TButtonSet;
    ButtonSetLFOSlvAMute : TButtonSet;
    ButtonSetLFOSlvAMono : TButtonSet;
  End;

  T_LFOSlvB = Class( TNMModule)
    EditLabelLFOSlvB : TEditLabel;
    TextLabelLFOSlvBMst : TTextLabel;
    OutputLFOSlvBOut : TOutput;
    ImageLFOSlvBType : TGraphicImage;
    InputLFOSlvBMst : TInput;
    IndicatorLFOSlvBOut : TIndicator;
    DisplayLFOSlvBRate : TDisplay;
    SmallKnobLFOSlvBRate : TSmallKnob;
  End;

  T_LFOSlvC = Class( TNMModule)
    EditLabelLFOSlvC : TEditLabel;
    TextLabelLFOSlvCMst : TTextLabel;
    OutputLFOSlvCOut : TOutput;
    ImageLFOSlvCType : TGraphicImage;
    InputLFOSlvCMst : TInput;
    IndicatorLFOSlvCOut : TIndicator;
    DisplayLFOSlvCRate : TDisplay;
    SmallKnobLFOSlvCRate : TSmallKnob;
  End;

  T_ADSREnv = Class( TNMModule)
    ImageADSR_EnvAmp : TGraphicImage;
    BoxADSR_EnvADSRGraph : TBox;
    EditLabelADSR_Env : TEditLabel;
    OutputADSR_EnvEnv : TOutput;
    IndicatorADSR_EnvGate : TIndicator;
    TextLabelADSR_EnvA : TTextLabel;
    InputADSR_EnvAmp : TInput;
    TextLabelADSR_EnvRetrig : TTextLabel;
    TextLabelADSR_EnvAmp : TTextLabel;
    OutputADSR_EnvOut : TOutput;
    ImageADSR_EnvRetrig : TGraphicImage;
    InputADSR_EnvRetrig : TInput;
    ADSRGraphADSR_Env : TADSRGraph;
    TextLabelADSR_EnvGate : TTextLabel;
    InputADSR_EnvGate : TInput;
    TextLabelADSR_EnvD : TTextLabel;
    TextLabelADSR_EnvS : TTextLabel;
    TextLabelADSR_EnvR : TTextLabel;
    InputADSR_EnvIn : TInput;
    TextLabelADSR_EnvEnv : TTextLabel;
    SmallKnobADSR_EnvA : TSmallKnob;
    DisplayADSR_EnvA : TDisplay;
    SmallKnobADSR_EnvD : TSmallKnob;
    DisplayADSR_EnvD : TDisplay;
    SmallKnobADSR_EnvS : TSmallKnob;
    DisplayADSR_EnvS : TDisplay;
    SmallKnobADSR_EnvR : TSmallKnob;
    DisplayADSR_EnvR : TDisplay;
    ButtonSetADSR_EnvShape : TButtonSet;
    ButtonSetADSR_EnvInvert : TButtonSet;
  End;

  T_ADEnv = Class( TNMModule)
    ImageAD_EnvAmp : TGraphicImage;
    EditLabelAD_Env : TEditLabel;
    TextLabelAD_EnvD : TTextLabel;
    TextLabelAD_EnvA : TTextLabel;
    BoxAD_EnvAD_Graph : TBox;
    ADGraphAD_Env : TADGraph;
    OutputAD_EnvEnv : TOutput;
    OutputAD_EnvOut : TOutput;
    InputAD_EnvIn : TInput;
    ImageAD_EnvGate : TGraphicImage;
    InputAD_EnvGate : TInput;
    IndicatorAD_EnvGate : TIndicator;
    TextLabelAD_EnvAmp : TTextLabel;
    InputAD_EnvAmp : TInput;
    SmallKnobAD_EnvA : TSmallKnob;
    DisplayAD_EnvA : TDisplay;
    SmallKnobAD_EnvD : TSmallKnob;
    DisplayAD_EnvD : TDisplay;
    ButtonSetAD_EnvGate : TButtonSet;
  End;

  T_ModEnv = Class( TNMModule)
    BoxMod_EnvA : TBox;
    BoxMod_EnvR : TBox;
    BoxMod_EnvS : TBox;
    BoxMod_EnvD : TBox;
    ImageMod_EnvAmp : TGraphicImage;
    BoxMod_EnvADSR_Graph : TBox;
    EditLabelMod_Env : TEditLabel;
    OutputMod_EnvEnv : TOutput;
    IndicatorMod_EnvGate : TIndicator;
    TextLabelMod_EnvA : TTextLabel;
    InputMod_EnvAmp : TInput;
    TextLabelMod_EnvRetrig : TTextLabel;
    TextLabelMod_EnvAmp : TTextLabel;
    OutputMod_EnvOut : TOutput;
    ImageMod_EnvRetrig : TGraphicImage;
    InputMod_EnvRetrig : TInput;
    ADSRGraphMod_Env : TADSRGraph;
    TextLabelMod_EnvGate : TTextLabel;
    InputMod_EnvGate : TInput;
    TextLabelMod_EnvD : TTextLabel;
    TextLabelMod_EnvS : TTextLabel;
    TextLabelMod_EnvR : TTextLabel;
    InputMod_EnvIn : TInput;
    TextLabelMod_EnvEnv : TTextLabel;
    InputMod_EnvA : TInput;
    InputMod_EnvD : TInput;
    InputMod_EnvS : TInput;
    InputMod_EnvR : TInput;
    SmallKnobMod_EnvA : TSmallKnob;
    DisplayMod_EnvA : TDisplay;
    SmallKnobMod_EnvD : TSmallKnob;
    DisplayMod_EnvD : TDisplay;
    SmallKnobMod_EnvS : TSmallKnob;
    DisplayMod_EnvS : TDisplay;
    SmallKnobMod_EnvR : TSmallKnob;
    DisplayMod_EnvR : TDisplay;
    SmallKnobMod_EnvAMod : TSmallKnob;
    SmallKnobMod_EnvDMod : TSmallKnob;
    SmallKnobMod_EnvSMod : TSmallKnob;
    SmallKnobMod_EnvRMod : TSmallKnob;
    ButtonSetMod_EnvInvert : TButtonSet;
  End;

  T_PatternGen = Class( TNMModule)
    BoxPatternGenStep : TBox;
    BoxPatternGenHoriz : TBox;
    BoxPatternGenVert : TBox;
    EditLabelPatternGen : TEditLabel;
    OutputPatternGenOut : TOutput;
    InputPatternGenPattern : TInput;
    TextLabelPatternGenPattern : TTextLabel;
    TextLabelPatternGenDelta : TTextLabel;
    TextLabelPatternGenBank : TTextLabel;
    TextLabelPatternGenStep : TTextLabel;
    TextLabelPatternGenRst : TTextLabel;
    ImagePatternGenRst : TGraphicImage;
    InputPatternGenRst : TInput;
    TextLabelPatternGenClk : TTextLabel;
    ImagePatternGenClk : TGraphicImage;
    InputPatternGenClk : TInput;
    ImagePatternGenType : TGraphicImage;
    TextLabelPatternGenOut : TTextLabel;
    DisplayPatternGenPattern : TDisplay;
    SmallKnobPatternGenPattern : TSmallKnob;
    DisplayPatternGenBank : TDisplay;
    SmallKnobPatternGenBank : TSmallKnob;
    DisplayPatternGenStep : TDisplay;
    SmallKnobPatternGenStep : TSmallKnob;
    ButtonSetPatternGenMono : TButtonSet;
    ButtonSetPatternGenLow : TButtonSet;
  End;

  T_RndStepGen = Class( TNMModule)
    EditLabelRndStepGen : TEditLabel;
    TextLabelRndStepGenMst : TTextLabel;
    OutputRndStepGenOut : TOutput;
    ImageRndStepGenType : TGraphicImage;
    InputRndStepGenMst : TInput;
    IndicatorRndStepGenOut : TIndicator;
    DisplayRndStepGenRate : TDisplay;
    SmallKnobRndStepGenRate : TSmallKnob;
  End;

  T_RandomGen = Class( TNMModule)
    EditLabelRandomGen : TEditLabel;
    TextLabelRandomGenMst : TTextLabel;
    OutputRandomGenOut : TOutput;
    ImageRandomGenType : TGraphicImage;
    InputRandomGenMst : TInput;
    IndicatorRandomGenOut : TIndicator;
    DisplayRandomGenRate : TDisplay;
    SmallKnobRandomGenRate : TSmallKnob;
  End;

  T_RndPulsGen = Class( TNMModule)
    EditLabelRndPulsGen : TEditLabel;
    TextLabelRndPulsGenDensity : TTextLabel;
    OutputRndPulsGenOut : TOutput;
    ImageRndPulsGenType : TGraphicImage;
    IndicatorRndPulsGenOut : TIndicator;
    SmallKnobRndPulsGenDensity : TSmallKnob;
  End;



  Procedure RegisterModulesWithSelector( aSelector: TModuleSelector);

Implementation

  Procedure RegisterModules;
  Begin
    RegisterClass( T_WaveWrap);
    RegisterClass( T_NoteSeqB);
    RegisterClass( T_Clip);
    RegisterClass( T_Overdrive);
    RegisterClass( T_Chorus);
    RegisterClass( T_Phaser);
    RegisterClass( T_InvLevShift);
    RegisterClass( T_Diode);
    RegisterClass( T_Quantizer);
    RegisterClass( T_Delay);
    RegisterClass( T_SampleAndHold);
    RegisterClass( T_Amplifier);
    RegisterClass( T_XFade);
    RegisterClass( T_Pan);
    RegisterClass( T_1to2Fade);
    RegisterClass( T_Mixer3);
    RegisterClass( T_Mixer8);
    RegisterClass( T_GainControl);
    RegisterClass( T_OnOff);
    RegisterClass( T_4to1Switch);
    RegisterClass( T_1to4Switch);
    RegisterClass( T_2to1fade);
    RegisterClass( T_LevMult);
    RegisterClass( T_LevAdd);
    RegisterClass( T_Shaper);
    RegisterClass( T_PosEdgeDly);
    RegisterClass( T_NegEdgeDly);
    RegisterClass( T_Pulse);
    RegisterClass( T_PartialGen);
    RegisterClass( T_ControlMixer);
    RegisterClass( T_NoteVelScal);
    RegisterClass( T_LogicDelay);
    RegisterClass( T_CompareLev);
    RegisterClass( T_CompareAB);
    RegisterClass( T_ClkDiv);
    RegisterClass( T_LogicInv);
    RegisterClass( T_ClkDivFix);
    RegisterClass( T_LogicProc);
    RegisterClass( T_KeyQuant);
    RegisterClass( T_Digitizer);
    RegisterClass( T_NoteSeqA);
    RegisterClass( T_CtrlSeq);
    RegisterClass( T_Compressor);
    RegisterClass( T_Expander);
    RegisterClass( T_RingMod);
    RegisterClass( T_Constant);
    RegisterClass( T_EventSeq);
    RegisterClass( T_NoteScaler);
    RegisterClass( T_NoteQuant);
    RegisterClass( T_Smooth);
    RegisterClass( T_PortamentoA);
    RegisterClass( T_PortamentoB);
    RegisterClass( T_OscSlvC);
    RegisterClass( T_OscSlvD);
    RegisterClass( T_OscSlvE);
    RegisterClass( T_OscSlvB);
    RegisterClass( T_SpectralOsc);
    RegisterClass( T_FormantOsc);
    RegisterClass( T_OscSlvA);
    RegisterClass( T_DrumSynth);
    RegisterClass( T_LFOA);
    RegisterClass( T_LFOB);
    RegisterClass( T_PercOsc);
    RegisterClass( T_OscSineBank);
    RegisterClass( T_OscSlvFM);
    RegisterClass( T_Noise);
    RegisterClass( T_PolyAreaIn);
    RegisterClass( T_1Output);
    RegisterClass( T_2Outputs);
    RegisterClass( T_AudioIn);
    RegisterClass( T_Keyboard);
    RegisterClass( T_KeyboardPatch);
    RegisterClass( T_MidiGlobal);
    RegisterClass( T_OscA);
    RegisterClass( T_OscB);
    RegisterClass( T_OscC);
    RegisterClass( T_MasterOsc);
    RegisterClass( T_4Outputs);
    RegisterClass( T_NoteDetect);
    RegisterClass( T_KeybSplit);
    RegisterClass( T_LFOC);
    RegisterClass( T_FilterB);
    RegisterClass( T_FilterC);
    RegisterClass( T_FilterD);
    RegisterClass( T_FilterA);
    RegisterClass( T_AHDEnv);
    RegisterClass( T_MultiEnv);
    RegisterClass( T_EnvFollower);
    RegisterClass( T_FilterBank);
    RegisterClass( T_EqMid);
    RegisterClass( T_EqShelving);
    RegisterClass( T_Vocoder);
    RegisterClass( T_FilterE);
    RegisterClass( T_FilterF);
    RegisterClass( T_VocalFilter);
    RegisterClass( T_LFOSlvE);
    RegisterClass( T_ClkGen);
    RegisterClass( T_ClkRndGen);
    RegisterClass( T_LFOSlvD);
    RegisterClass( T_LFOSlvA);
    RegisterClass( T_LFOSlvB);
    RegisterClass( T_LFOSlvC);
    RegisterClass( T_ADSREnv);
    RegisterClass( T_ADEnv);
    RegisterClass( T_ModEnv);
    RegisterClass( T_PatternGen);
    RegisterClass( T_RndStepGen);
    RegisterClass( T_RandomGen);
    RegisterClass( T_RndPulsGen);
  End;

  Procedure UnRegisterModules;
  Begin
    UnRegisterClass( T_WaveWrap);
    UnRegisterClass( T_NoteSeqB);
    UnRegisterClass( T_Clip);
    UnRegisterClass( T_Overdrive);
    UnRegisterClass( T_Chorus);
    UnRegisterClass( T_Phaser);
    UnRegisterClass( T_InvLevShift);
    UnRegisterClass( T_Diode);
    UnRegisterClass( T_Quantizer);
    UnRegisterClass( T_Delay);
    UnRegisterClass( T_SampleAndHold);
    UnRegisterClass( T_Amplifier);
    UnRegisterClass( T_XFade);
    UnRegisterClass( T_Pan);
    UnRegisterClass( T_1to2Fade);
    UnRegisterClass( T_Mixer3);
    UnRegisterClass( T_Mixer8);
    UnRegisterClass( T_GainControl);
    UnRegisterClass( T_OnOff);
    UnRegisterClass( T_4to1Switch);
    UnRegisterClass( T_1to4Switch);
    UnRegisterClass( T_2to1fade);
    UnRegisterClass( T_LevMult);
    UnRegisterClass( T_LevAdd);
    UnRegisterClass( T_Shaper);
    UnRegisterClass( T_PosEdgeDly);
    UnRegisterClass( T_NegEdgeDly);
    UnRegisterClass( T_Pulse);
    UnRegisterClass( T_PartialGen);
    UnRegisterClass( T_ControlMixer);
    UnRegisterClass( T_NoteVelScal);
    UnRegisterClass( T_LogicDelay);
    UnRegisterClass( T_CompareLev);
    UnRegisterClass( T_CompareAB);
    UnRegisterClass( T_ClkDiv);
    UnRegisterClass( T_LogicInv);
    UnRegisterClass( T_ClkDivFix);
    UnRegisterClass( T_LogicProc);
    UnRegisterClass( T_KeyQuant);
    UnRegisterClass( T_Digitizer);
    UnRegisterClass( T_NoteSeqA);
    UnRegisterClass( T_CtrlSeq);
    UnRegisterClass( T_Compressor);
    UnRegisterClass( T_Expander);
    UnRegisterClass( T_RingMod);
    UnRegisterClass( T_Constant);
    UnRegisterClass( T_EventSeq);
    UnRegisterClass( T_NoteScaler);
    UnRegisterClass( T_NoteQuant);
    UnRegisterClass( T_Smooth);
    UnRegisterClass( T_PortamentoA);
    UnRegisterClass( T_PortamentoB);
    UnRegisterClass( T_OscSlvC);
    UnRegisterClass( T_OscSlvD);
    UnRegisterClass( T_OscSlvE);
    UnRegisterClass( T_OscSlvB);
    UnRegisterClass( T_SpectralOsc);
    UnRegisterClass( T_FormantOsc);
    UnRegisterClass( T_OscSlvA);
    UnRegisterClass( T_DrumSynth);
    UnRegisterClass( T_LFOA);
    UnRegisterClass( T_LFOB);
    UnRegisterClass( T_PercOsc);
    UnRegisterClass( T_OscSineBank);
    UnRegisterClass( T_OscSlvFM);
    UnRegisterClass( T_Noise);
    UnRegisterClass( T_PolyAreaIn);
    UnRegisterClass( T_1Output);
    UnRegisterClass( T_2Outputs);
    UnRegisterClass( T_AudioIn);
    UnRegisterClass( T_Keyboard);
    UnRegisterClass( T_KeyboardPatch);
    UnRegisterClass( T_MidiGlobal);
    UnRegisterClass( T_OscA);
    UnRegisterClass( T_OscB);
    UnRegisterClass( T_OscC);
    UnRegisterClass( T_MasterOsc);
    UnRegisterClass( T_4Outputs);
    UnRegisterClass( T_NoteDetect);
    UnRegisterClass( T_KeybSplit);
    UnRegisterClass( T_LFOC);
    UnRegisterClass( T_FilterB);
    UnRegisterClass( T_FilterC);
    UnRegisterClass( T_FilterD);
    UnRegisterClass( T_FilterA);
    UnRegisterClass( T_AHDEnv);
    UnRegisterClass( T_MultiEnv);
    UnRegisterClass( T_EnvFollower);
    UnRegisterClass( T_FilterBank);
    UnRegisterClass( T_EqMid);
    UnRegisterClass( T_EqShelving);
    UnRegisterClass( T_Vocoder);
    UnRegisterClass( T_FilterE);
    UnRegisterClass( T_FilterF);
    UnRegisterClass( T_VocalFilter);
    UnRegisterClass( T_LFOSlvE);
    UnRegisterClass( T_ClkGen);
    UnRegisterClass( T_ClkRndGen);
    UnRegisterClass( T_LFOSlvD);
    UnRegisterClass( T_LFOSlvA);
    UnRegisterClass( T_LFOSlvB);
    UnRegisterClass( T_LFOSlvC);
    UnRegisterClass( T_ADSREnv);
    UnRegisterClass( T_ADEnv);
    UnRegisterClass( T_ModEnv);
    UnRegisterClass( T_PatternGen);
    UnRegisterClass( T_RndStepGen);
    UnRegisterClass( T_RandomGen);
    UnRegisterClass( T_RndPulsGen);
  End;

  Procedure RegisterModulesWithSelector( aSelector: TModuleSelector);
  Begin
    
    // Registers all module types with aSelector, so TCustomModules can be
    // dragged out of a knob into a TWirePanel component.
    //
    // Example :
    //
    //   aSelector.RegisterModuleClass(
    //     'In/Out',            // Tab specifier for TModuleSelector - auto created if not present yet.
    //     T_Keyboard,          // The ClassName of the class to be registgered.
    //     'Keyboard',          // The name for the bitmap, here Keyboard.bmp will be searched for in : 
    //                          //   the application direcory <appdir>
    //                          //   <appdir>\bitmaps\
    //                          //   the currentdirectory <curdir>
    //                          //   <curdir>\bitmaps\
    //                          //   the path as obtained from the environment variable PATH.
    //     'Keyboard - voice'   // The hint as shown when hovering over the buttons in TModuleSelector.
    //   );
    //
    aSelector.RegisterModuleClass(
      'In/Out',
      T_Keyboard,
      'Keyboard',
      'Keyboard - voice'
    );
    aSelector.RegisterModuleClass(
      'In/Out',
      T_KeyboardPatch,
      'KeyboardPatch',
      'Keyboard - Patch'
    );
    aSelector.RegisterModuleClass(
      'In/Out',
      T_MidiGlobal,
      'MidiGlobal',
      'MIDI - Global'
    );
    aSelector.RegisterModuleClass(
      'In/Out',
      T_AudioIn,
      'AudioIn',
      'Audio In'
    );
    aSelector.RegisterModuleClass(
      'In/Out',
      T_PolyAreaIn,
      'PolyAreaIn',
      'Poly Area In'
    );
    aSelector.RegisterModuleClass(
      'In/Out',
      T_1Output,
      '1Output',
      '1 output'
    );
    aSelector.RegisterModuleClass(
      'In/Out',
      T_2Outputs,
      '2Outputs',
      '2 outputs'
    );
    aSelector.RegisterModuleClass(
      'In/Out',
      T_4Outputs,
      '4Outputs',
      '4 outputs'
    );
    aSelector.RegisterModuleClass(
      'In/Out',
      T_NoteDetect,
      'NoteDetect',
      'Note detector'
    );
    aSelector.RegisterModuleClass(
      'In/Out',
      T_KeybSplit,
      'KeybSplit',
      'Keyboard Split'
    );
    aSelector.RegisterModuleClass(
      'Osc',
      T_MasterOsc,
      'MasterOsc',
      'Master Oscillator'
    );
    aSelector.RegisterModuleClass(
      'Osc',
      T_OscA,
      'OscA',
      'OSC A'
    );
    aSelector.RegisterModuleClass(
      'Osc',
      T_OscB,
      'OscB',
      'OSC B'
    );
    aSelector.RegisterModuleClass(
      'Osc',
      T_OscC,
      'OscC',
      'OSC C'
    );
    aSelector.RegisterModuleClass(
      'Osc',
      T_SpectralOsc,
      'SpectralOsc',
      'Spectral Osc'
    );
    aSelector.RegisterModuleClass(
      'Osc',
      T_FormantOsc,
      'FormantOsc',
      'Formant Osc'
    );
    aSelector.RegisterModuleClass(
      'Osc',
      T_OscSlvA,
      'OscSlvA',
      'OSC Slave A'
    );
    aSelector.RegisterModuleClass(
      'Osc',
      T_OscSlvB,
      'OscSlvB',
      'OSC Slave B'
    );
    aSelector.RegisterModuleClass(
      'Osc',
      T_OscSlvC,
      'OscSlvC',
      'OSC Slave C'
    );
    aSelector.RegisterModuleClass(
      'Osc',
      T_OscSlvD,
      'OscSlvD',
      'OSC Slave D'
    );
    aSelector.RegisterModuleClass(
      'Osc',
      T_OscSlvE,
      'OscSlvE',
      'OSC Slave E'
    );
    aSelector.RegisterModuleClass(
      'Osc',
      T_OscSineBank,
      'OscSineBank',
      'Osc Sine Bank'
    );
    aSelector.RegisterModuleClass(
      'Osc',
      T_OscSlvFM,
      'OscSlvFM',
      'Osc Slave FM'
    );
    aSelector.RegisterModuleClass(
      'Osc',
      T_Noise,
      'Noise',
      'Noise generator'
    );
    aSelector.RegisterModuleClass(
      'Osc',
      T_PercOsc,
      'PercOsc',
      'Percussion OSC'
    );
    aSelector.RegisterModuleClass(
      'Osc',
      T_DrumSynth,
      'DrumSynth',
      'Drumsound synthesizer'
    );
    aSelector.RegisterModuleClass(
      'LFO',
      T_LFOA,
      'LFOA',
      'LFO A'
    );
    aSelector.RegisterModuleClass(
      'LFO',
      T_LFOB,
      'LFOB',
      'LFO B'
    );
    aSelector.RegisterModuleClass(
      'LFO',
      T_LFOC,
      'LFOC',
      'LFO C'
    );
    aSelector.RegisterModuleClass(
      'LFO',
      T_LFOSlvA,
      'LFOSlvA',
      'LFO Slave A'
    );
    aSelector.RegisterModuleClass(
      'LFO',
      T_LFOSlvB,
      'LFOSlvB',
      'LFO Slave B'
    );
    aSelector.RegisterModuleClass(
      'LFO',
      T_LFOSlvC,
      'LFOSlvC',
      'LFO Slave C'
    );
    aSelector.RegisterModuleClass(
      'LFO',
      T_LFOSlvD,
      'LFOSlvD',
      'LFO Slave D'
    );
    aSelector.RegisterModuleClass(
      'LFO',
      T_LFOSlvE,
      'LFOSlvE',
      'LFO Slave E'
    );
    aSelector.RegisterModuleClass(
      'LFO',
      T_ClkGen,
      'ClkGen',
      'Clock generator'
    );
    aSelector.RegisterModuleClass(
      'LFO',
      T_ClkRndGen,
      'ClkRndGen',
      'Clocked random step generator'
    );
    aSelector.RegisterModuleClass(
      'LFO',
      T_RndStepGen,
      'RndStepGen',
      'Random step generator'
    );
    aSelector.RegisterModuleClass(
      'LFO',
      T_RandomGen,
      'RandomGen',
      'Random generator'
    );
    aSelector.RegisterModuleClass(
      'LFO',
      T_RndPulsGen,
      'RndPulsGen',
      'Random pulse generator'
    );
    aSelector.RegisterModuleClass(
      'LFO',
      T_PatternGen,
      'PatternGen',
      'Clocked pattern generator'
    );
    aSelector.RegisterModuleClass(
      'Env',
      T_ADSREnv,
      'ADSREnv',
      'ADSR envelope'
    );
    aSelector.RegisterModuleClass(
      'Env',
      T_ADEnv,
      'ADEnv',
      'AD envelope'
    );
    aSelector.RegisterModuleClass(
      'Env',
      T_ModEnv,
      'ModEnv',
      'ADSR env. with modulation'
    );
    aSelector.RegisterModuleClass(
      'Env',
      T_AHDEnv,
      'AHDEnv',
      'AHD env. with modulation'
    );
    aSelector.RegisterModuleClass(
      'Env',
      T_MultiEnv,
      'MultiEnv',
      'Multistage envelope'
    );
    aSelector.RegisterModuleClass(
      'Env',
      T_EnvFollower,
      'EnvFollower',
      'Envelope follower'
    );
    aSelector.RegisterModuleClass(
      'Filter',
      T_FilterA,
      'FilterA',
      'Filter A, 6dB static LP filter'
    );
    aSelector.RegisterModuleClass(
      'Filter',
      T_FilterB,
      'FilterB',
      'Filter B, 6dB static HP filter'
    );
    aSelector.RegisterModuleClass(
      'Filter',
      T_FilterC,
      'FilterC',
      'Filter C, 12dB static multimode filter'
    );
    aSelector.RegisterModuleClass(
      'Filter',
      T_FilterD,
      'FilterD',
      'Filter D, 12 dB multimode filter'
    );
    aSelector.RegisterModuleClass(
      'Filter',
      T_FilterE,
      'FilterE',
      'Filter E, 24 dB filter'
    );
    aSelector.RegisterModuleClass(
      'Filter',
      T_FilterF,
      'FilterF',
      'Filter F, 24 dB classic LP filter'
    );
    aSelector.RegisterModuleClass(
      'Filter',
      T_VocalFilter,
      'VocalFilter',
      'Vocal filter'
    );
    aSelector.RegisterModuleClass(
      'Filter',
      T_Vocoder,
      'Vocoder',
      'Vocoder'
    );
    aSelector.RegisterModuleClass(
      'Filter',
      T_FilterBank,
      'FilterBank',
      'FilterBank'
    );
    aSelector.RegisterModuleClass(
      'Filter',
      T_EqMid,
      'EqMid',
      'Parametric Eq'
    );
    aSelector.RegisterModuleClass(
      'Filter',
      T_EqShelving,
      'EqShelving',
      'Hi and lo shelving Eq'
    );
    aSelector.RegisterModuleClass(
      'Mixer',
      T_Mixer3,
      'Mixer3',
      '3 inputs mixer'
    );
    aSelector.RegisterModuleClass(
      'Mixer',
      T_Mixer8,
      'Mixer8',
      '8 inputs mixer'
    );
    aSelector.RegisterModuleClass(
      'Mixer',
      T_GainControl,
      'GainControl',
      'Gain controller (multiply)'
    );
    aSelector.RegisterModuleClass(
      'Mixer',
      T_XFade,
      'XFade',
      'X-fade with modulator'
    );
    aSelector.RegisterModuleClass(
      'Mixer',
      T_Pan,
      'Pan',
      'Pan'
    );
    aSelector.RegisterModuleClass(
      'Mixer',
      T_1to2Fade,
      '1to2Fade',
      '1 in to 2 out fader'
    );
    aSelector.RegisterModuleClass(
      'Mixer',
      T_2to1fade,
      '2to1fade',
      '2 in to 1 out fader'
    );
    aSelector.RegisterModuleClass(
      'Mixer',
      T_LevMult,
      'LevMult',
      'Adjustable gain control'
    );
    aSelector.RegisterModuleClass(
      'Mixer',
      T_LevAdd,
      'LevAdd',
      'Adjustable offset'
    );
    aSelector.RegisterModuleClass(
      'Mixer',
      T_OnOff,
      'OnOff',
      'On/Off switch'
    );
    aSelector.RegisterModuleClass(
      'Mixer',
      T_4to1Switch,
      '4to1Switch',
      '4-1 Switch'
    );
    aSelector.RegisterModuleClass(
      'Mixer',
      T_1to4Switch,
      '1to4Switch',
      '1-4 Switch'
    );
    aSelector.RegisterModuleClass(
      'Mixer',
      T_Amplifier,
      'Amplifier',
      'Amplifier'
    );
    aSelector.RegisterModuleClass(
      'Audio',
      T_Clip,
      'Clip',
      'Clip'
    );
    aSelector.RegisterModuleClass(
      'Audio',
      T_Overdrive,
      'Overdrive',
      'Overdrive'
    );
    aSelector.RegisterModuleClass(
      'Audio',
      T_WaveWrap,
      'WaveWrap',
      'Wave Wrapper'
    );
    aSelector.RegisterModuleClass(
      'Audio',
      T_Quantizer,
      'Quantizer',
      'Quantizer'
    );
    aSelector.RegisterModuleClass(
      'Audio',
      T_Delay,
      'Delay',
      'Delay line'
    );
    aSelector.RegisterModuleClass(
      'Audio',
      T_SampleAndHold,
      'SampleAndHold',
      'Sample and hold'
    );
    aSelector.RegisterModuleClass(
      'Audio',
      T_Diode,
      'Diode',
      'Diode processing'
    );
    aSelector.RegisterModuleClass(
      'Audio',
      T_Chorus,
      'Chorus',
      'Stereo chorus'
    );
    aSelector.RegisterModuleClass(
      'Audio',
      T_Phaser,
      'Phaser',
      'Phaser'
    );
    aSelector.RegisterModuleClass(
      'Audio',
      T_InvLevShift,
      'InvLevShift',
      'Level shifter / inverter'
    );
    aSelector.RegisterModuleClass(
      'Audio',
      T_Shaper,
      'Shaper',
      'Signal shaper'
    );
    aSelector.RegisterModuleClass(
      'Audio',
      T_Compressor,
      'Compressor',
      'Compressor'
    );
    aSelector.RegisterModuleClass(
      'Audio',
      T_Expander,
      'Expander',
      'Expander'
    );
    aSelector.RegisterModuleClass(
      'Audio',
      T_RingMod,
      'RingMod',
      'Ring and amplitude modulator'
    );
    aSelector.RegisterModuleClass(
      'Audio',
      T_Digitizer,
      'Digitizer',
      'Digitizer'
    );
    aSelector.RegisterModuleClass(
      'Ctrl',
      T_Constant,
      'Constant',
      'Constant'
    );
    aSelector.RegisterModuleClass(
      'Ctrl',
      T_Smooth,
      'Smooth',
      'Smooth'
    );
    aSelector.RegisterModuleClass(
      'Ctrl',
      T_PortamentoA,
      'PortamentoA',
      'PortamentoA'
    );
    aSelector.RegisterModuleClass(
      'Ctrl',
      T_PortamentoB,
      'PortamentoB',
      'PortamentoB'
    );
    aSelector.RegisterModuleClass(
      'Ctrl',
      T_NoteScaler,
      'NoteScaler',
      'Note scaler'
    );
    aSelector.RegisterModuleClass(
      'Ctrl',
      T_NoteQuant,
      'NoteQuant',
      'Note quantizer'
    );
    aSelector.RegisterModuleClass(
      'Ctrl',
      T_KeyQuant,
      'KeyQuant',
      'Key quantizer'
    );
    aSelector.RegisterModuleClass(
      'Ctrl',
      T_PartialGen,
      'PartialGen',
      'Partial generator'
    );
    aSelector.RegisterModuleClass(
      'Ctrl',
      T_ControlMixer,
      'ControlMixer',
      'Control signal mixer'
    );
    aSelector.RegisterModuleClass(
      'Ctrl',
      T_NoteVelScal,
      'NoteVelScal',
      'Note and Vel scaler'
    );
    aSelector.RegisterModuleClass(
      'Logic',
      T_PosEdgeDly,
      'PosEdgeDly',
      'Positive edge delay'
    );
    aSelector.RegisterModuleClass(
      'Logic',
      T_NegEdgeDly,
      'NegEdgeDly',
      'Negative edge delay'
    );
    aSelector.RegisterModuleClass(
      'Logic',
      T_Pulse,
      'Pulse',
      'Pulse'
    );
    aSelector.RegisterModuleClass(
      'Logic',
      T_LogicDelay,
      'LogicDelay',
      'Logic delay'
    );
    aSelector.RegisterModuleClass(
      'Logic',
      T_LogicInv,
      'LogicInv',
      'Logic inverter'
    );
    aSelector.RegisterModuleClass(
      'Logic',
      T_LogicProc,
      'LogicProc',
      'Logic processsor'
    );
    aSelector.RegisterModuleClass(
      'Logic',
      T_CompareLev,
      'CompareLev',
      'Compare to level'
    );
    aSelector.RegisterModuleClass(
      'Logic',
      T_CompareAB,
      'CompareAB',
      'Compare'
    );
    aSelector.RegisterModuleClass(
      'Logic',
      T_ClkDiv,
      'ClkDiv',
      'Clock divider'
    );
    aSelector.RegisterModuleClass(
      'Logic',
      T_ClkDivFix,
      'ClkDivFix',
      'Clock divider, fixed'
    );
    aSelector.RegisterModuleClass(
      'Seq',
      T_EventSeq,
      'EventSeq',
      'Event sequencer'
    );
    aSelector.RegisterModuleClass(
      'Seq',
      T_CtrlSeq,
      'CtrlSeq',
      'Control sequencer'
    );
    aSelector.RegisterModuleClass(
      'Seq',
      T_NoteSeqA,
      'NoteSeqA',
      'Note sequencer A'
    );
    aSelector.RegisterModuleClass(
      'Seq',
      T_NoteSeqB,
      'NoteSeqB',
      'Note sequencer B'
    );
  End;

Initialization

  RegisterModules;

Finalization

  UnregisterModules;

End.

