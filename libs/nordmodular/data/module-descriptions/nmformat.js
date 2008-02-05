/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
 
// ****************************************************************************
// helpers
// ****************************************************************************
 
/**
 * Rounds d until digit '-to'. For example d=123, to=2 returns 120.
 * If d=123.456 and to=-2 then 123.45 is returned.
 * 
 * double d Value to round
 * int to decimal position
 * @return rounded value of d
 */
function roundTo(d, to) 
{
    if (to == 0) return Math.round(d);
    var norm = Math.pow(10.0, -to);
    return Math.round(d*norm) / norm;
}

// ****************************************************************************
// Formatting functions for Clavia Nord Modular parameters.
// ****************************************************************************

function fmtAmpGain(value)
{
    if (value==63) value++;
    var aFloat = 0.25 * Math.pow(2.0, value/32.0);
	return 'x'+roundTo(aFloat/32.0,-2);
}

//bpm
function fmtBPM(value)
{
	if (value<=32)
        value = 2 * value +24;
    else if (value<=96)
        value = value +56;    
    else
        value = 2*value -40;    
    return value+" bpm";
}

// adsr env ref level

function fmtEnvRefLevel(value)
{
    return (value-30)+"dB";
}

// CompressorLimiter
function fmtCompressorLimiter(value)
{
    return value==24 ? "Off" : ((value-12)+"dB");
}

// CompressorThreshold
function fmtCompressorThreshold(value)
{
    return value==42 ? "Off" : ((value-30)+"dB");
}

// DigitizerHz 
function fmtDigitizerHz(value)
{
    var aFloat = (32.70 * Math.pow(2.0, value/12.0));

    if (aFloat<100)
        return ""+roundTo(aFloat, -2)+" Hz";       
    else if (aFloat<1000)
        return ""+roundTo(aFloat, -1)+" Hz";
    else if (aFloat<10000)
        return ""+roundTo(aFloat/1000.0, -3)+" kHz";     
    else
        return ""+roundTo(aFloat/1000.0, -2)+" kHz";
}
    
// DrumHz 
function fmtDrumHz(value)
{
    var aFloat = 20.0 * Math.pow(2.0, value/24.0);

    if (aFloat<100)
        return ""+roundTo(aFloat, -1)+" Hz";
    
    else
        return ""+roundTo(aFloat, 0)+" Hz";
}
    
// DrumPartials 
    
var DRUM_PARTIAL_FRACTIONS = ["1:1", "2:1", "4:1"];
function fmtDrumPartials(value)
{
    if (value%48==0)
        return DRUM_PARTIAL_FRACTIONS[value / 48];
    else
        return "x"+roundTo(Math.pow(2.0, value/48.0), -2);
}
// EnvelopeLevelDivider
{

function fmtEnvelopeLevelDivider(value)
{
    return value == 127 ? "64" : ""+(value*0.5);
}
// EqHz 
function fmtEqHz(value)
{
    var aFloat = 471.0 * Math.pow(2.0, (value-60)/12);

    if (aFloat<1000)
        return ""+roundTo(aFloat, 0)+" Hz";

    else if (aFloat<10000)
        return ""+roundTo(aFloat/1000, -2)+" kHz";

    else
        return ""+roundTo(aFloat/1000, -1)+" kHz";
}

}

// noteVelScaleGain
function fmtNoteVelScaleGain(value)
{
    return (value-24)+'dB';
}

// ExpanderGate
function fmtExpanderGate(value)
{   
    return value == 0 ? "Off" : ((value-84)+"dB");
}
// ExpanderHold
function fmtExpanderHold(value)
{
    return value == 0 ? "Off" : ((value*4)+"ms");
}
// ExpanderThreshold
function fmtExpanderThreshold(value)
{
    return value == 84 ? "Off" : ((value-84)+"dB");
}
// FilterHz1 
function fmtFilterHz1(value)
{
    var aFloat = 504.0 * Math.pow(2.0, (value-64)/12.0);

    if (aFloat<1000)
        return ""+roundTo(aFloat, 0)+" Hz";

    else if (aFloat<10000)
        return ""+roundTo(aFloat, -2)+" kHz";

    else
        return ""+roundTo(aFloat, -1)+" kHz";
}
// FilterHz2 
function fmtFilterHz2(value)
{
    var aFloat = 330.0 * Math.pow(2.0, (value-60)/12.0);

    if (aFloat<1000)
        return ""+roundTo(aFloat, 0)+" Hz";

    else if (aFloat<10000)
        return ""+roundTo(aFloat/1000, -2)+" kHz";

    else
        return ""+roundTo(aFloat/1000, -1)+" kHz";
}
// Freqkbt
function fmtFreqkbt(value)
{
    return value == 0 ? "Off" : ("x"+(value*0.015748031496062992));
}
// LFOHz 
function fmtLFOHz(value)
{
    var aFloat = 440.0 * Math.pow(2.0, (value-177)/12.0);

    if (aFloat<0.1)
        return ""+roundTo(1/aFloat, -1)+" s";

    else if (aFloat<10)
        return ""+roundTo(aFloat, -2)+" Hz";
        
    else if (aFloat<100)
        return ""+roundTo(aFloat, -1)+" Hz";

    else
        return ""+roundTo(aFloat, 0)+" Hz";
}
// LogicDelay 
function fmtLogicDelay(value)
{
    var aFloat = Math.pow(2.0, value/9.0);

    if (aFloat<1000)
        return ""+roundTo(aFloat, 0)+" ms";
    
    else if (aFloat<10000)
        return ""+roundTo(aFloat/1000.0, -1)+" s";
    
    else
        return ""+roundTo(aFloat/1000.0, 0)+" s";
}
// Note 
var NOTE_NOTES = ["C","C","D","D","E","F","F","G","G","A","A","B"];
var NOTE_SHARPS = [" ","#"," ","#"," "," ","#"," ","#"," ","#"," "];

function fmtNote(value)
{
    var v12 = value % 12;
    return NOTE_NOTES[v12]+(Math.round(value/12) -1)+NOTE_SHARPS[v12];
}

// NoteScale 
function fmtNoteScale(value)
{
    switch(value) {
        case 0:   return "0 (Oct)";
        case 127: return "?64";
    };

    var suffix = "";
    switch ((value/2)%12 + value%2) {
        case 0: suffix = "(Oct)"; break;
        case 7: suffix = "(5th)"; break;
        case 10:suffix = "(7th)"; break;
    };
    return "?"+roundTo(value/2.0, -1)+suffix;
}

//Offset64_2
function fmtOffset64_2(value)
{
    return value == 127 ? "64" : (""+(value-64));
}
// OscHz 
function fmtOscHz(value)
{
    var aFloat = 440.0 * Math.pow(2.0, (value-69)/12.0);

    if (aFloat<10)
        return ""+roundTo(aFloat, -2)+" Hz";
    
    else if (aFloat<100)
        return ""+roundTo(aFloat, -1)+" Hz";
        
    else if (aFloat<1000)
        return ""+roundTo(aFloat,  0)+" Hz";
            
    else if (aFloat<1000)
        return ""+roundTo(aFloat/1000.0, -2)+" kHz";
    
    else
        return ""+roundTo(aFloat/1000.0, -2)+" kHz";
}
// class PartialRange 
function fmtPartialRange(value)
{
    switch(value) {
        case 0:   return "0";
        case 127: return "?64 *";
    };
    var suffix = (value>64)? " *":"";
    return "?"+roundTo(value/2.0, -1)+suffix;
}
// Partials 
var PARTIALS_FRACTIONS = ["1:32", "1:16", "1:8", 
			"1:4", "1:2", "1:1", "2:1", 
			"4:1", "8:1", "16:1", "32:1"];
function fmtPartials(value)
{
    if ((value+8)%12==0)
        return PARTIALS_FRACTIONS[value / 12];
    else
        return "x"+roundTo(Math.pow(2.0, (value-64)/12.0), -3);
}
// PatternStep
function fmtPatternStep(value)
{
    return value == 0 ? "Off" : ""+(value+1);
}
// Phase 
function fmtPhase(value)
{
    return "" + roundTo(value*2.8125-180.0, 0);
}
// Semitones 
function fmtSemitones(value)
{
    value -= 64;
    var suffix = "";
    switch ((value+72 )%12) {
        case 0: suffix = "(Oct)"; break;
        case 7: suffix = "(5th)"; break;
        case 10:suffix = "(7th)"; break;
    };
    return ""+(value)+suffix;
}
// SmoothTime 
function fmtSmoothTime(value)
{
    var aFloat = Math.pow(2.0, value/9.0);

    if (aFloat<1000)
        return ""+roundTo(aFloat, 0)+" ms";
    
    else if (aFloat<10000)
        return ""+roundTo(aFloat/1000.0, -1)+" s";
    
    else
        return ""+roundTo(aFloat/1000.0, 0)+" s";
}
// Timbre
function fmtTimbre(value)
{
    return value == 127 ? "Rnd" : ""+(value+1);
}   

// pulse width

function fmtPulseWidth(value)
{
    return ((value+1)*0.77165354330708658)+'%';
}

// array types

function map(array, value)
{
    return (value>=0 && value<array.length) ? array[value] : (""+value);
}

var DATA_OFF_ON = ["Off" , "On"];

function fmtOffOn(value)
{
    return map(DATA_OFF_ON, value);
}

var DATA_COMPANDER_RELEASE = ["125m","129m", "124m" , "139m", "144m", "149m", "154m", "159m"
, "165m", "171m", "177m", "183m", "189m", "196m", "203m", "210m", "218m", "225m", "233m", "241m"
, "250m", "259m", "268m", "277m", "287m", "297m", "208m", "319m", "330m", "342m", "354m", "366m"
, "379m", "392m", "406m", "420m", "435m", "451m", "467m", "483m", "500m", "518m", "536m", "555m"
, "574m", "595m", "616m", "637m", "660m", "683m", "707m", "732m", "758m", "785m", "812m"
, "841m", "871m", "901m", "933m", "966m", "1.00s", "1.04s", "1.07s", "1.11s", "1.15s"
, "1.19s", "1.23s", "1.27s", "1.32s", "1.37s", "1.41s", "1.46s", "1.52s", "1.57s", "1.62s"
, "1.68s", "1.74s", "1.80s", "1.87s", "1.93s", "2.00s", "2.07s", "2.14s", "2.22s", "2.30s"
, "2.38s", "2.46s", "2.55s", "2.64s", "2.73s", "2.83s", "2.93s", "3.03s", "3.14s", "3.25s"
, "3.36s", "3.48s", "3.61s", "3.73s", "3.86s", "4.00s", "4.14s", "4.29s", "4.44s", "4.59s", "4.76s", "4.92s"
, "5.10s", "5.28s", "5.46s", "5.66s", "5.86s", "6.06s", "6.28s", "6.50s", "6.73s", "6.96s", "7.21s"
, "7.46s", "7.73s", "8.00s", "8.28s", "8.57s", "8.88s", "9.19s", "9.51s", "9.85s", "10.2s"];

function fmtCompanderRelease(value)
{
    return map(DATA_COMPANDER_RELEASE, value);
}

var DATA_FILTER_SLOPE_2 = ["12 dB/Oct", "24 dB/Oct"];

function fmtFilterSlope2(value)
{
    return map(DATA_FILTER_SLOPE_2, value);
}

var DATA_CHANNEL_SELECT_OUT_1 = ["1", "2", "3", "4", "L", "R"];

function fmtChannelSelectOut1(value)
{
    return map(DATA_CHANNEL_SELECT_OUT_1, value);
}

var DATA_DIODE_FUNCTION = ["Pass through", "Half wave rectifiy", "Full wave rectify"];

function fmtDiodeFunction(value)
{
    return map(DATA_DIODE_FUNCTION, value);
}

var DATA_ENVELOPE_RELEASE =["Fast", "41.4m", "42.9m", "44.4m", "45.9m", "47.6m", "49.2m", "51.0m", "52.8m", "54.6m", "56.6m"
, "58.6m", "60.6m", "62.8m", "65.0m", "67.3m", "69.6m", "72.1m", "74.6m", "77.3m", "80.0m", "82.8m"
, "85.7m", "88.8m", "91.9m", "95.1m", "98.5m", "102m", "106m", "109m", "113m", "117m", "121m"
, "126m", "130m", "135m", "139m", "144m", "149m", "155m", "160m", "166m", "171m", "178m", "184m"
, "190m", "197m", "204m", "211m", "219m", "226m", "234m", "243m", "251m", "260m", "269m", "279m"
, "288m", "299m", "309m", "320m", "331m", "343m", "355m", "368m", "381m", "394m", "408m", "422m"
, "437m", "453m", "469m", "485m", "502m", "520m", "538m", "557m", "577m", "597m", "618m", "640m"
, "663m", "686m", "710m", "735m", "761m", "788m", "816m", "844m", "874m", "905m", "937m", "970m"
, "1.00s", "1.04s", "1.08s", "1.11s", "1.15s", "1.19s", "1.24s", "1.28s", "1.33s", "1.37s", "1.42s"
, "1.47s", "1.52s", "1.58s", "1.63s", "1.69s", "1.75s", "1.81s", "1.87s", "1.94s", "2.01s", "2.08s"
, "2.15s", "2.23s", "2.31s", "2.39s", "2.47s", "2.56s", "2.65s", "2.74s", "2.84s", "2.94s", "3.04s"
, "3.15s", "3.26s"];

function fmtEnvelopeRelease(value)
{
    return map(DATA_ENVELOPE_RELEASE, value);
}

var DATA_DELAY_TIME = ["0.00ms", "0.02ms", "0.04ms", "0.06ms", "0.08ms", "0.10ms", "0.13ms", "0.15ms", "0.17ms", "0.19ms"
, "0.21ms", "0.23ms", "0.25ms", "0.27ms", "0.29ms", "0.31ms", "0.33ms", "0.35ms", "0.38ms", "0.40ms"
, "0.42ms", "0.44ms", "0.46ms", "0.48ms", "0.50ms", "0.52ms", "0.54ms", "0.56ms", "0.58ms", "0.60ms"
, "0.63ms", "0.65ms", "0.67ms", "0.69ms", "0.71ms", "0.73ms", "0.75ms", "0.77ms", "0.79ms", "0.81ms"
, "0.83ms", "0.85ms", "0.88ms", "0.90ms", "0.92ms", "0.94ms", "0.96ms", "0.98ms", "1.00ms", "1.02ms"
, "1.04ms", "1.06ms", "1.08ms", "1.10ms", "1.13ms", "1.15ms", "1.17ms", "1.19ms", "1.21ms", "1.23ms"
, "1.25ms", "1.27ms", "1.29ms", "1.31ms", "1.33ms", "1.35ms", "1.38ms", "1.40ms", "1.42ms", "1.44ms"
, "1.46ms", "1.48ms", "1.50ms", "1.52ms", "1.54ms", "1.56ms", "1.58ms", "1.60ms", "1.63ms", "1.65ms"
, "1.67ms", "1.69ms", "1.71ms", "1.73ms", "1.75ms", "1.77ms", "1.79ms", "1.81ms", "1.83ms", "1.85ms"
, "1.88ms", "1.90ms", "1.92ms", "1.94ms", "1.96ms", "1.98ms", "2.00ms", "2.02ms", "2.04ms", "2.06ms"
, "2.08ms", "2.10ms", "2.13ms", "2.15ms", "2.17ms", "2.19ms", "2.21ms", "2.23ms", "2.25ms", "2.27ms"
, "2.29ms", "2.31ms", "2.33ms", "2.35ms", "2.38ms", "2.40ms", "2.42ms", "2.44ms", "2.46ms", "2.48ms"
, "2.50ms", "2.52ms", "2.54ms", "2.56ms", "2.58ms", "2.60ms", "2.63ms", "2.65ms"];

function fmtDelayTime(value)
{
    return map(DATA_DELAY_TIME, value);
}

var DATA_OSCILLATOR_WAVE =["Sine", "Tri", "Saw", "Square"];

function fmtOscillatorWave(value)
{
    return map(DATA_OSCILLATOR_WAVE, value);
}

var DATA_WHITE_COLORED = ["White", "Colored"];

function fmtWhiteColored(value)
{
    return map(DATA_WHITE_COLORED, value);
}

var DATA_PHASER_CENTER_FREQUENCY = 
["100Hz", "104Hz", "108Hz", "113Hz", "117Hz", "122Hz", "127Hz", "132Hz", "138Hz", "143Hz", "149Hz"
, "155Hz", "162Hz", "168Hz", "175Hz", "182Hz", "190Hz", "197Hz", "205Hz", "214Hz", "222Hz", "231Hz"
, "241Hz", "251Hz", "261Hz", "272Hz", "283Hz", "294Hz", "306Hz", "319Hz", "332Hz", "345Hz", "359Hz"
, "374Hz", "389Hz", "405Hz", "421Hz", "439Hz", "457Hz", "475Hz", "495Hz", "515Hz", "536Hz", "558Hz"
, "580Hz", "604Hz", "629Hz", "654Hz", "681Hz", "709Hz", "738Hz", "768Hz", "799Hz", "831Hz", "865Hz"
, "901Hz", "937Hz", "976Hz", "1.02kHz", "1.06kHz", "1.10kHz", "1.14kHz", "1.19kHz", "1.24kHz", "1.29kHz"
, "1.34kHz", "1.40kHz", "1.45kHz", "1.51kHz", "1.58khz", "1.64kHz", "1.71kHz", "1.78kHz", "1.85kHz"
, "1.92kHz", "2.00kHz", "2.08kHz", "2.17kHz", "2.26kHz", "2.35kHz", "2.45kHz", "2.55kHz", "2.65kHz"
, "2.76kHz", "2.87kHz", "2.99kHz", "3.11kHz", "3.24kHz", "3.37kHz", "3.50kHz", "3.65kHz", "3.80kHz"
, "3.95kHz", "4.11kHz", "4.28kHz", "4.45kHz", "4.64kHz", "4.82kHz", "5.02kHz", "5.23kHz", "5.44kHz"
, "5.66kHz", "5.89kHz", "6.13kHz", "6.38kHz", "6.64kHz", "6.91kHz", "7.19kHz", "7.49kHz", "7.79kHz"
, "8.11kHz", "8.44kHz", "8.79kHz", "9.14kHz", "9.52kHz", "9.91kHz", "10.3kHz", "10.7kHz", "11.2kHz"
, "11.6kHz", "12.1kHz", "12.6kHz", "13.1kHz", "13.6kHz", "14.2kHz", "14.8kHz", "15.4kHz", "16.0kHz"];

function fmtPhaserCenterFrequency(value)
{
    return map(DATA_PHASER_CENTER_FREQUENCY, value);
}

var DATA_VOWELS =["A", "E", "I", "O", "U", "Y", "AA", "AE", "OE"];

function fmtVowels(value)
{
    return map(DATA_VOWELS, value);
}

var DATA_FILTER_SLOPE_3 = ["12 dB/Oct", "18 dB/Oct", "24 dB/Oct"];

function fmtFilterSlope3(value)
{
    return map(DATA_FILTER_SLOPE_3, value);
}

var DATA_ENVELOPE_ADSR_SHAPE = ["Log", "Lin", "Exp"];

function fmtEnvelopeAdsrShape(value)
{
    return map(DATA_ENVELOPE_ADSR_SHAPE, value);
}

var DATA_ENVELOPE_ATTACK = ["Fast", "0.53m", "0.56m", "0.59m", "0.63m", "0.67m", "0.71m", "0.75m", "0.79m", "0.84m", "0.89m"
, "0.94m", "1.00m", "1.06m", "1.12m", "1.19m", "1.26m", "1.33m", "1.41m", "1.50m", "1.59m", "1.68m"
, "1.78m", "1.89m", "2.00m", "2.12m", "2.24m", "2.38m", "2.52m", "2.67m", "2.83m", "3.00m", "3.17m"
, "3.36m", "3.56m", "3.78m", "4.00m", "4.24m", "4.49m", "4.76m", "5.04m", "5.34m", "5.66m", "5.99m"
, "6.35m", "6.73m", "7.13m", "7.55m", "8.00m", "8.48m", "8.98m", "9.51m", "10.1m", "10.7m", "11.3m"
, "12.0m", "12.7m", "13.5m", "14.3m", "15.1m", "16.0m", "17.0m", "18.0m", "19.0m", "20.2m", "21.4m"
, "22.6m", "24.0m", "25.4m", "26.9m", "28.5m", "30.2m", "32.0m", "33.9m", "35.9m", "38.1m", "40.3m"
, "42.7m", "45.3m", "47.9m", "50.8m", "53.8m", "57.0m", "60.4m", "64.0m", "67.8m", "71.8m", "76.1m"
, "80.6m", "85.4m", "90.5m", "95.9m", "102m", "108m", "114m", "121m", "128m", "136m", "144m", "152m"
, "161m", "171m", "181m", "192m", "203m", "215m", "228m", "242m", "256m", "271m", "287m", "304m"
, "323m", "342m", "362m", "384m", "406m", "431m", "456m", "483m", "512m", "542m", "575m", "609m"
, "645m", "683m", "724m", "767m"];

function fmtEnvelopeAttack(value)
{
    return map(DATA_ENVELOPE_ATTACK, value);
}

var DATA_ENVELOPE_MULTI_SHAPE = ["Bipolar", "Uni/Exp", "Uni/Lin"];

function fmtEnvelopeMultiShape(value)
{
    return map(DATA_ENVELOPE_MULTI_SHAPE, value);
}

var DATA_FILTER_TYPE_4 = ["LP", "BP", "HP", "BR"];

function fmtFilterType4(value)
{
    return map(DATA_FILTER_TYPE_4, value);
}

var DATA_LOW_HIGH = ["Low", "High"];

function fmtLowHigh(value)
{
    return map(DATA_LOW_HIGH, value);
}

var DATA_CHANNEL_SELECT_OUT_2 = ["1/2", "3/4", "CVA"];

function fmtChannelSelectOut2(value)
{
    return map(DATA_CHANNEL_SELECT_OUT_2, value);
}

var DATA_FILTER_TYPE_3 = ["LP", "BP", "HP"];

function fmtFilterType3(value)
{
    return map(DATA_FILTER_TYPE_3, value);
}

var DATA_COMPANDER_RATIO = ["1.0:1", "1.1:1", "1.2:1", "1.3:1", "1.4:1", "1.5:1", "1.6:1", "1.7:1", "1.8:1", "1.9:1", "2.0:1"
, "2.2:1", "2.4:1", "2.6:1", "2.8:1", "3.0:1", "3.2:1", "3.4:1", "3.6:1", "3.8:1", "4.0:1", "4.2:1"
, "4.4:1", "4.6:1", "4.8:1", "5.0:1", "5.5:1", "6.0:1", "6.5:1", "7.0:1", "7.5:1", "8.0:1", "8.5:1"
, "9.0:1", "9.5:1", "10:1", "11:1", "12:1", "13:1", "14:1", "15:1", "16:1", "17:1", "18:1", "19:1"
, "20:1", "22:1", "24:1", "26:1", "28:1", "30:1", "32:1", "34:1", "36:1", "38:1", "40:1", "42:1"
, "44:1", "46:1", "48:1", "50:1", "55:1", "60:1", "65:1", "70:1", "75:1", "80:1"];

function fmtCompanderRatio(value)
{
    return map(DATA_COMPANDER_RATIO, value);
}

var DATA_OFF_ON = ["Off", "On"];

function fmtOffOn(value)
{
    return map(DATA_OFF_ON, value);
}

var DATA_DRUM_PRESETS = ["none", "Kick 1", "Kick 2", "Kick 3", "Kick 4", "Kick 5", "Snare 1", "Snare 2", "Snare 3"
, "Snare 4", "Snare 5", "Tom1 1", "Tom1 2", "Tom1 3", "Tom2 1", "Tom2 2", "Tom2 3", "Tom3 1"
, "Tom3 2", "Tom3 3", "Cymb 1", "Cymb 2", "Cymb 3", "Cymb 4", "Cymb 5", "Perc 1", "Perc 2"
, "Perc 3", "Perc 4", "Perc 5", "Perc 6"];

function fmtDrumPresets(value)
{
    return map(DATA_DRUM_PRESETS, value);
}

var DATA_ADSR_TIME = ["0.5m", "0.7m", "1.0m", "1.3m", "1.5m", "1.8m", "2.1m", "2.3m", "2.6m", "2.9m", "3.2m", "3.5m"
, "3.9m", "4.2m", "4.6m", "4.9m", "5.3m", "5.7m", "6.1m", "6.6m", "7.0m", "7.5m", "8.0m", "8.5m"
, "9.1m", "9.7m", "10m", "11m", "12m", "13m", "13m", "14m", "15m", "16m", "17m", "19m", "20m"
, "21m", "23m", "24m", "26m", "28m", "30m", "32m", "35m", "37m", "40m", "43m", "47m", "51m"
, "55m", "59m", "64m", "69m", "75m", "81m", "88m", "95m", "103m", "112m", "122m", "132m", "143m"
, "156m", "170m", "185m", "201m", "219m", "238m", "260m", "283m", "308m", "336m", "367m", "400m"
, "436m", "476m", "520m", "567m", "619m", "676m", "738m", "806m", "881m", "962m", "1.1s", "1.1s"
, "1.3s", "1.4s", "1.5s", "1.6s", "1.8s", "2.0s", "2.1s", "2.3s", "2.6s", "2.8s", "3.1s", "3.3s"
, "3.7s", "4.0s", "4.4s", "4.8s", "5.2s", "5.7s", "6.3s", "6.8s", "7.5s", "8.2s", "9.0s", "9.8s"
, "10.7s", "11.7s", "12.8s", "14.0s", "15.3s", "16.8s", "18.3s", "20.1s", "21.9s", "24.0s", "26.3s"
, "28.7s", "31.4s", "34.4s", "37.6s", "41.1s", "45.0s"];

function fmtAdsrTime(value)
{
    return map(DATA_ADSR_TIME, value);
}

var DATA_ODD_ALL = ["Odd", "All"];

function fmtOddAll(value)
{
    return map(DATA_ODD_ALL, value);
}

var DATA_CONTROL_MIXER_MODE = ["lin", "exp"];

function fmtControlMixerMode(value)
{
    return map(DATA_CONTROL_MIXER_MODE, value);
}

var DATA_MULTI_ENV_SUSTAIN = ["--", "L1", "L2", "L3", "L4"];

function fmtMultiEnvSustain(value)
{
    return map(DATA_MULTI_ENV_SUSTAIN, value);
}

var DATA_SHAPE_FUNCTION =  ["log 2", "log 1", "lin", "exp 1", "exp 2"];

function fmtShapeFunction(value)
{
    return map(DATA_SHAPE_FUNCTION, value);
}

var DATA_LFO_WAVE = ["Sine", "Tri", "Saw", "Inverse saw", "Square"];

function fmtWave(value)
{
    return map(DATA_LFO_WAVE, value);
}

var DATA_LFO_RANGE = ["Sub", "Lo", "Hi"];

function fmtLFORange(value)
{
    return map(DATA_LFO_RANGE, value);
}

var DATA_LEVEL_FUNCTION = ["Pass through", "Shift down", "Shift up"];

function fmtLevelFunction(value)
{
    return map(DATA_LEVEL_FUNCTION, value);
}

var DATA_LOGIC_FUNCTION = ["And", "Or", "Xor"];

function fmtLogicFunction(value)
{
    return map(DATA_LOGIC_FUNCTION, value);
}

var DATA_MORPH_KB_ASSIGNMENT = ["None", "Velocity", "Note"];

function fmtMorphKbAssignment(value)
{
    return map(DATA_MORPH_KB_ASSIGNMENT, value);
}
