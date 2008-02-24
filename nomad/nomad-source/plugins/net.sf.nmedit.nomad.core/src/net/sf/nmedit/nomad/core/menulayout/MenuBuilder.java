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
 * Created on Nov 19, 2006
 */
package net.sf.nmedit.nomad.core.menulayout;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import net.sf.nmedit.nmutils.Platform;

/**
 * The menu builder class. Uses a {@link net.sf.nmedit.nomad.core.menulayout.MenuLayout} and 
 * a {@link java.util.ResourceBundle} to setup the {@link javax.swing.Action}s. When the ResourceBundle
 * is changed at runtime each action (menu item) is updated automatically.
 * 
 * <h1>Linking MenuLayout and ResourceBundle</h1>
 * 
 * <p>Each MenuLayout entry has a key (the global entry-point).
 * The key is used up to lookup the properties of the entry in the ResourceBundle.</p>
 * 
 * <h1>ResourceBundle format</h1>
 * 
 * <ul>
 * <li><strong>name:</strong> looked up via the global entry-point key</li>
 * <li><strong>mnemonic:</strong> 
 * The mnemonic character is encoded in the name-property. The mnemonic character
 * is the character after the first ampersand (&amp;) character. The &amp;-character
 * will not show up in the name property. <br />
 * Example Values:<br/>
 * 
 * <ul>
 *  <li>menu.file.save=&Save <br />
 *  name = Save, mnemonic = S  
 *  </li>
 *  <li>menu.file=Save &As .. <br />
 *  name = Save As..., mnemonic = A  
 *  </li>
 * </ul>
 * 
 * </li>
 * <li><strong>keybinding (accelerator):</strong>
 * the keybinding associated with an entry has the key [global entry-point]+"$keybinding".
 * For example:<code>menu.file$keybinding</code>. <br />
 *
 * The keybinding syntax is
 *   <code> ((ALT|SHIFT|CTRL|META)\\+)*(\\w|\\d|F\\d\\d?)</code>. 
 * 
 * Valid values are for example:<br />
 * <code>CTRL+C</code> <br />
 * <code>SHIFT+CTRL+C</code> <br />
 * <code>SHIFT+F1</code> <br />
 * <code>ALT+SHIFT+F12</code> 
 * </li>
 * <li><strong>short (long) description:</strong>
 * the short (long) description associated with an entry has the key [global entry-point]+"$description"
 * ([global entry-point]+"$long-description").
 * For example: <code>menu.file$short-description (menu.file$long-description)</code>.</li>
 * 
 * </ul>
 * 
 * @author Christian Schneider
 */

public class MenuBuilder
{

    private MenuLayout layout;
    private ResourceBundle bundle;

    public MenuBuilder(MenuLayout layout, ResourceBundle bundle)
    {
        this.layout = layout;
        this.bundle = null;
        setResourceBundle(bundle);
    }
    
    public MenuBuilder getClonedTree(String entryPoint)
    {
        MLEntry e = layout.getEntry(entryPoint);
        
        if (e == null)
        {
            MLEntry empty = new MLEntry(entryPoint);
            return new MenuBuilder(new MenuLayout(empty), bundle);
        }
        
        return new MenuBuilder(new MenuLayout(e.cloneTree()), bundle);
    }

    public void setResourceBundle(ResourceBundle bundle)
    {
        if (this.bundle != bundle)
        {
            this.bundle = bundle;
            setActionProperties();
        }
    }
    
    public MenuBuilder(MenuLayout layout)
    {
        this(layout, null);
    }

    public ResourceBundle getResourceBundle()
    {
        return bundle;
    }
    
    public MenuLayout getLayout()
    {
        return layout;
    }

    public MLEntry getEntry(String entryPoint)
    {
        return layout.getEntry(entryPoint);
    }

    public JPopupMenu createPopup(String entryPoint)
    {
        JPopupMenu mnPopup = new JPopupMenu();
        createMenuContents(mnPopup, entryPoint);
        return mnPopup;
    }

    public JMenuBar createMenuBar(String entryPoint)
    {
        JMenuBar mnBar = new JMenuBar();
        createMenuContents(mnBar, entryPoint);
        return mnBar;
    }
    
    private void createMenuContents(JComponent m, String entryPoint)
    {
        MLEntry e = getEntry(entryPoint);
        
        if (e!=null)
        {
            createMenuContents(m , e);
        }
    }
    
    private void createMenuContents(JComponent m, MLEntry parent)
    {
        int separatorIndex = 0;

        for (int i=0;i<parent.size();i++)
        {
            MLEntry childEntry = parent.getEntryAt(i);

            if (childEntry.size()>0)
            {
                if (childEntry.isFlat())
                {
                    if (i>separatorIndex)
                        addSeparator(m);
                    createMenuContents(m, childEntry);
                    if (i<parent.size()-1)
                    {
                        separatorIndex = i+1;
                        addSeparator(m);
                    }
                }
                else
                {
                    JMenu subMenu = new JMenu(childEntry);
                    createMenuContents(subMenu, childEntry);
                    m.add(subMenu);
                }
            }
            else
            {
            	JMenuItem item = new JMenuItem(childEntry);
                m.add(item);
            }
        }
    }
    
    private void addSeparator(JComponent c)
    {
        if (c instanceof JMenu)
        {
            ((JMenu) c).addSeparator();
        }
        else if (c instanceof JPopupMenu )
        {
            ((JPopupMenu) c).addSeparator();
        }
    }

    public boolean addActionListener( String entryPoint, ActionListener listener )
    {
        MLEntry e = getEntry(entryPoint);
        if (e!=null)
        {
            e.addActionListener(listener);
            return true;
        }
        return false;
    }
    
    public void removeActionListener( String entryPoint, ActionListener listener )
    {
        MLEntry e = getEntry(entryPoint);
        if (e!=null)
        {
            e.removeActionListener(listener);
        }
    }

    // utils

    public final static String SUFFIX_KEYBINDING = "$keybinding";
    public final static String SUFFIX_KEYBINDING_OSX = "$keybinding.osx";
    public final static String SUFFIX_SHORT_DESC = "$description";
    public final static String SUFFIX_LONG_DESC = "$long-description";

    private static Map<String, Integer> modifierMap = new HashMap<String, Integer>();
    
    static 
    {
        modifierMap.put("SHIFT", KeyEvent.SHIFT_MASK);
        modifierMap.put("ALT", KeyEvent.ALT_MASK);
        modifierMap.put("ALTGR", KeyEvent.ALT_GRAPH_MASK);
        modifierMap.put("META", KeyEvent.META_MASK);
        modifierMap.put("CTRL", KeyEvent.CTRL_MASK);
    }

    private static Pattern kbPattern = 
        Pattern.compile("(((ALT)|(ALTGR)|(SHIFT)|(CTRL)|(META))\\+)*(\\w|\\d|F\\d\\d?)", Pattern.CASE_INSENSITIVE);
    
    private String getStringOrNull(String key)
    {
        try
        {
            return bundle.getString(key);
        }
        catch (MissingResourceException e)
        {
            return null;
        }
    }

    private void setActionProperties()
    {
        if (layout.getRoot() != null)
        {
            Iterator<MLEntry> i = layout.getRoot().bfsIterator();
            while (i.hasNext())
            {
                setupAction(i.next());
            }
        }
    }
    
    private void setupAction(MLEntry entry)
    {
        
        String eName = null;
        KeyStroke eAcceleratorKey = null;
        Integer eMnemonicKey = 0;
        String eShortDesc = null;
        String eLongDesc = null;

        final String bundleID = entry.getGlobalEntryPoint();
        if (bundleID != null)
        {
            // get the name
            eName = getStringOrNull(bundleID);

            if (eName != null)
            {
              // get the mnemonic key,
              // get index of the ampersand  (&) identifying the mnemonic key
                int amp = eName.indexOf('&');
                
                // check if index is valid
                if (amp>=0 && amp<eName.length()-1)
                {
                    eMnemonicKey = new Integer(eName.codePointAt(amp+1));

                    // remove ampersand
                    eName = eName.substring(0, amp) + eName.substring(amp+1, eName.length());
                }
            }
            
            // get descriptions
            eShortDesc = getStringOrNull(bundleID+SUFFIX_SHORT_DESC);
            eLongDesc = getStringOrNull(bundleID+SUFFIX_LONG_DESC);
            String keybinding = null;
            if (Platform.isFlavor(Platform.OS.MacOSFlavor)) {
                keybinding = getStringOrNull(bundleID+SUFFIX_KEYBINDING_OSX);
            } 
            
            if (keybinding == null) {
                keybinding = getStringOrNull(bundleID+SUFFIX_KEYBINDING);
            }
            
            if (keybinding != null)
                eAcceleratorKey = extractKeyStroke(keybinding);
        }
        
        if (eName != null)
            entry.putValue(Action.NAME, eName);
        if (eMnemonicKey != null)
            entry.putValue(Action.MNEMONIC_KEY, eMnemonicKey);
        if (eAcceleratorKey != null)
            entry.putValue(Action.ACCELERATOR_KEY, eAcceleratorKey);
        if (eShortDesc != null)
            entry.putValue(Action.SHORT_DESCRIPTION, eShortDesc);
        if (eLongDesc != null)
            entry.putValue(Action.LONG_DESCRIPTION, eLongDesc);
    }
    
    public static KeyStroke extractKeyStroke(String keybinding)
    {
        Matcher m = kbPattern.matcher(keybinding);

        if (m.matches())
        {
            int modifiers = 0;
            
            for (int i=1;i<m.groupCount();i++)
            {
                String s = m.group(i);

                if (s!=null)
                {
                    Integer mod = modifierMap.get(s.toUpperCase());
                    if (mod!=null)
                        modifiers|=mod.intValue();
                }
            }

            String key = m.group(m.groupCount());

            if (key.length()>0)
            {
                int cp = key.codePointAt(0);
                
                if (Character.charCount(cp) == key.length())
                {
                    return KeyStroke.getKeyStroke(cp, modifiers); 
                }
            
                if (key.length()>=2 && key.toUpperCase().startsWith("F"))
                {
                    try
                    {
                        int FX = Integer.parseInt(key.substring(1));
                        
                        if (1<=FX && FX<=12)
                        {
                            cp = KeyEvent.VK_F1+FX-1;
                            
                            return KeyStroke.getKeyStroke(cp, modifiers);
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        
                    }
                }    
            }
        }
        
        return null;
    }

    
}
