package nomad.patch;

import java.io.BufferedReader;

import javax.swing.JSplitPane;

class Header {

    // .pch info
    private String version;
	private int kbrangemin, kbrangemax, velrangemin, velrangemax, bendrange;
	private int portatime, portamento, requestedvoices;
	private int octaveshift, seperator;
	private int voiceretriggerpoly, voiceretriggercommon;
	private int v13, v14, v15, v16;
	private int redvisible, bluevisible, yellowvisible, grayvisible;
	private int greenvisible, purplevisible, whitevisible;
    // .pch info
    
	Header() {
			version = "Nord Modular patch 3.0";
			kbrangemin = 0;
			kbrangemax = 127;
			velrangemin = 0;
			velrangemax = 127;
			bendrange = 2;
			portatime = 0;
			portamento = 0;
			requestedvoices = 1;
			seperator = 600;
			octaveshift = 2;
			voiceretriggerpoly = 1;
			voiceretriggercommon = 1;
			v13 = 1;
			v14 = 1;
			v15 = 1;
			v16 = 1;
			redvisible = 1;
			bluevisible = 1;
			yellowvisible = 1;
			grayvisible = 1;
			greenvisible = 1;
			purplevisible = 1;
			whitevisible = 1;
	}
	
	public String getVersion() {
		return version;
	}
	
    public int getSeperator() {
        return seperator;
    }

    public void setSeperator(int newSeperator) {
        seperator = newSeperator;
    }
	
	public void readHeader(BufferedReader pchFile) {
		String[] sa = new String[22];
		try {
			version = pchFile.readLine().substring(8);
			sa = pchFile.readLine().split(" ");
			kbrangemin = Integer.parseInt(sa[0]);
			kbrangemax = Integer.parseInt(sa[1]);
			velrangemin = Integer.parseInt(sa[2]);
			velrangemax = Integer.parseInt(sa[3]);
			bendrange = Integer.parseInt(sa[4]);
			portatime = Integer.parseInt(sa[5]);
			portamento = Integer.parseInt(sa[6]);
			requestedvoices = Integer.parseInt(sa[7]);
			seperator = Integer.parseInt(sa[8]);
			octaveshift = Integer.parseInt(sa[9]);
			voiceretriggerpoly = Integer.parseInt(sa[10]);
			voiceretriggercommon =Integer.parseInt(sa[11]);
			v13 = Integer.parseInt(sa[12]);
			v14 = Integer.parseInt(sa[13]);
			v15 = Integer.parseInt(sa[14]);
			v16 = Integer.parseInt(sa[15]);
			redvisible = Integer.parseInt(sa[16]);
			bluevisible = Integer.parseInt(sa[17]);
			yellowvisible = Integer.parseInt(sa[18]);
			grayvisible = Integer.parseInt(sa[19]);
			greenvisible = Integer.parseInt(sa[20]);
			purplevisible = Integer.parseInt(sa[21]);
			whitevisible = Integer.parseInt(sa[22]);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public StringBuffer createHeader(StringBuffer result, JSplitPane splitPane) {
		result.append("[Header]\r\n");
		result.append("Version=" + version + "\r\n");

		result.append("" + kbrangemin + ' ' + 
						kbrangemax + ' ' + 
						velrangemin + ' ' + 
						velrangemax + ' ' + 
						bendrange + ' ' + 
						portatime + ' ' + 
						portamento + ' ' + 
						requestedvoices + ' ' + 
						(splitPane.getDividerLocation()-1)+ ' ' + 
						octaveshift + ' ' + 
						voiceretriggerpoly + ' ' + 
						voiceretriggercommon + ' ' + 
						v13 + ' ' + 
						v14 + ' ' + 
						v15 + ' ' + 
						v16 + ' ' + 
						redvisible + ' ' + 
						bluevisible + ' ' + 
						yellowvisible + ' ' + 
						grayvisible + ' ' + 
						greenvisible + ' ' + 
						purplevisible + ' ' + 
						whitevisible + "\r\n");
		result.append("[/Header]\r\n\r\n");
		return result;
	}
}
