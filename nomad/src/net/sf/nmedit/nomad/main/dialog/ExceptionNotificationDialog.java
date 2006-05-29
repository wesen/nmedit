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

/*
 * Created on Feb 15, 2006
 */
package net.sf.nmedit.nomad.main.dialog;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.JButton;
import javax.swing.JEditorPane;

public class ExceptionNotificationDialog extends NomadDialog {

	private Throwable throwable = null;
	private JButton showStackTrace = null;
	private Thread inThread = null;
	private JEditorPane textArea ;

	public ExceptionNotificationDialog(Throwable exception) {
		this(null, exception);
	}

	public ExceptionNotificationDialog(Thread inThread, Throwable exception) {
		setTitle("Exception");
		setInfo("Info",
			"An exception\n"
			+"has occured."
		);
		setScrollbarEnabled(true);
		
		this.inThread  = inThread;
		this.throwable = exception;

		setLayout(new BorderLayout());
		
		textArea = new JPrettyEditorPane();
		textArea.setEditable(false);
		textArea.setContentType("text/html");
		add(textArea, BorderLayout.CENTER);
		
		showText(false);
		showStackTrace = new JButton("Stack Trace");
		add(showStackTrace, BorderLayout.SOUTH);
		
		showStackTrace.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				showStackTrace();
			}});
	}

	protected void showText(boolean printStackTrace) {
		String txt = "<html><body bgcolor=\"#FFFFFF\"><p style=\"font-size:9px;font-family:sans-serif\" >";
		
		txt+="An exception has occured.<br>";
		
		if (inThread!=null) {
			txt+="<b>Thread: </b>"+inThread.getName()+"<br>";
		}
		
		txt+="<b>Message: </b>"+throwable.getMessage()+"</p>";
		if (printStackTrace) {
			txt+="<pre style=\"font-family:'Lucida Console',monospaced;font-size:8px;\"><b>Stack Trace: </b>";
			String st = getStackTrace();
			st = st.replaceAll("Caused by:", "<b>Caused by:</b>");
			st = st.replaceAll("\n","<br>");
			
			int br1 = 0;
			int br2 = 0;
			
			final String pre = "<font color=\"#0000FF\">";
			final String post= "</font>";
			
			while ((br1=st.indexOf('(', br2))>=0) {
				br2 = st.indexOf(')', br1);
				if (br2<0) return;
				st = st.substring(0,br1+1)
				   +pre+st.substring(br1+1,br2)+post
				   +st.substring(br2);
				br1+=pre.length()+post.length();
			}
			
			txt+=st+"</pre>";
		}
		
		txt += "</body></html>";
		textArea.setText(txt);
		validate();
	}

	public void invoke() {
		super.invoke(new String[]{":Close"});
	}

	public Throwable getThrowable() {
		return throwable;
	}
	
	protected String getStackTrace() {
		StringWriter swriter = new StringWriter();
		PrintWriter print = new PrintWriter(swriter);
		throwable.printStackTrace(print);
		return swriter.getBuffer().toString();
	}
	
	protected void showStackTrace() {
		if (showStackTrace==null)
			return;
		
		remove(showStackTrace);
		showText(true);
	}
	
	public static class NoamdExceptionHandler implements UncaughtExceptionHandler {

		public static void setAsDefaultHandler() {
			Thread.setDefaultUncaughtExceptionHandler(new NoamdExceptionHandler());
		}
		
		public void uncaughtException(Thread t, Throwable e) {
			// TODO Auto-generated method stub
			e.printStackTrace();
			
			ExceptionNotificationDialog dialog = 
				new ExceptionNotificationDialog(t, e);
			
			dialog.invoke();
		}
	}
	
	private class JPrettyEditorPane extends JEditorPane {
		protected void paintComponent(Graphics g) {
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			super.paintComponent(g);
		}
	}
	
}
