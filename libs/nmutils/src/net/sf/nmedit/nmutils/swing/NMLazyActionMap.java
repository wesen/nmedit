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
package net.sf.nmedit.nmutils.swing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.plaf.ActionMapUIResource;

public class NMLazyActionMap extends ActionMapUIResource
{
    /**
     * 
     */
    private static final long serialVersionUID = 7173055551302608417L;
    /**
     * Object to invoke <code>loadActionMap</code> on. This may be a Class
     * object.
     */
    private transient Object _loader;

    /**
     * Installs an ActionMap that will be populated by invoking the
     * <code>loadActionMap</code> method on the specified Class when
     * necessary.
     * <p>
     * This should be used if the ActionMap can be shared.
     * 
     * @param c
     *            JComponent to install the ActionMap on.
     * @param loaderClass
     *            Class object that gets loadActionMap invoked on.
     * @param defaultsKey
     *            Key to use to defaults table to check for existing map and
     *            what resulting Map will be registered on.
     */
    public static void installLazyActionMap(UIDefaults defaultMap,
            JComponent c, Class loaderClass, String defaultsKey)
    {
        ActionMap map = (ActionMap) defaultMap.get(defaultsKey);
        if (map == null)
        {
            map = new NMLazyActionMap(loaderClass);
            defaultMap.put(defaultsKey, map);
        }
        SwingUtilities.replaceUIActionMap(c, map);
    }

    /**
     * Returns an ActionMap that will be populated by invoking the
     * <code>loadActionMap</code> method on the specified Class when
     * necessary.
     * <p>
     * This should be used if the ActionMap can be shared.
     * 
     * @param c
     *            JComponent to install the ActionMap on.
     * @param loaderClass
     *            Class object that gets loadActionMap invoked on.
     * @param defaultsKey
     *            Key to use to defaults table to check for existing map and
     *            what resulting Map will be registered on.
     */
    public static ActionMap getActionMap(UIDefaults defaultMap,
            Class loaderClass, String defaultsKey)
    {
        ActionMap map = (ActionMap) defaultMap.get(defaultsKey);
        if (map == null)
        {
            map = new NMLazyActionMap(loaderClass);
            defaultMap.put(defaultsKey, map);
        }
        return map;
    }

    private NMLazyActionMap(Class loader)
    {
        _loader = loader;
    }

    public void put(Action action)
    {
        put(action.getValue(Action.NAME), action);
    }

    public void put(Object key, Action action)
    {
        loadIfNecessary();
        super.put(key, action);
    }

    public Action get(Object key)
    {
        loadIfNecessary();
        return super.get(key);
    }

    public void remove(Object key)
    {
        loadIfNecessary();
        super.remove(key);
    }

    public void clear()
    {
        loadIfNecessary();
        super.clear();
    }

    public Object[] keys()
    {
        loadIfNecessary();
        return super.keys();
    }

    public int size()
    {
        loadIfNecessary();
        return super.size();
    }

    public Object[] allKeys()
    {
        loadIfNecessary();
        return super.allKeys();
    }

    public void setParent(ActionMap map)
    {
        loadIfNecessary();
        super.setParent(map);
    }

    private void loadIfNecessary()
    {
        if (_loader != null)
        {
            Object loader = _loader;
            
            _loader = null;
            Class<?> klass = (Class) loader;
            try
            {
                Method method = klass.getDeclaredMethod("loadActionMap",
                        new Class[] { NMLazyActionMap.class });

                method.invoke(klass, new Object[] { this });
            }
            catch (NoSuchMethodException nsme)
            {
                assert false : "LazyActionMap unable to load actions " + klass;
            }
            catch (IllegalAccessException iae)
            {
                assert false : "LazyActionMap unable to load actions " + iae;
            }
            catch (InvocationTargetException ite)
            {
                assert false : "LazyActionMap unable to load actions " + ite;
            }
            catch (IllegalArgumentException iae)
            {
                assert false : "LazyActionMap unable to load actions " + iae;
            }
        }
    }
}

