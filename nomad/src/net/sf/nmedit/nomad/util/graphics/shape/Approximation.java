package net.sf.nmedit.nomad.util.graphics.shape;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;

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
 * Created on May 4, 2006
 */

public class Approximation 
{

    private final static int GROWTH = 14;
    
    private Shape shape;
    private double dcontour;

    private double boundsL;
    private double boundsT;
    private double boundsB;
    private double boundsR;
    private int grid;
    
    private int capacity;
    private int npoints;
    private int[] xpoints;
    private int[] ypoints;
    private double flatness;
    private double sqThreshold;
    private int gmid;

    public int getNumPoints()
    {
        return npoints;
    }
    
    public int[] getX()
    {
        return xpoints;
    }
    
    public int[] getY()
    {
        return ypoints;
    }
    
    private void put(int x, int y)
    {
        if (npoints>=capacity)
        {
            capacity += GROWTH;
            int[] newx = new int[capacity];
            int[] newy = new int[capacity];
            System.arraycopy(xpoints, 0, newx, 0, npoints);
            System.arraycopy(ypoints, 0, newy, 0, npoints);
            xpoints = newx;
            ypoints = newy;
        }   

        // insertion sort
        for (int i=0;i<npoints;i++)
        {
            switch (Integer.signum(x-xpoints[i]))
            {
                //case -1: break;// x smaller
                case 0: // x equal
                    switch (Integer.signum(y-ypoints[i]))
                    {
                        //case -1: break;// y smaller
                        case  0: // y equal, already inserted
                            return;
                        case +1: // y bigger, insert here
                            insert(i, x, y);
                            return ;
                    }
                    break ;
                case 1: // x bigger, insert here
                    insert(i, x, y);
                    return ; 
            }
        }

        xpoints[npoints] = x;
        ypoints[npoints] = y;
        npoints++;
    }
    
    private void insert(int index, int x, int y)
    {
        for (int i=npoints;i>index;i--)
        {
            xpoints[i] = xpoints[i-1];
            ypoints[i] = ypoints[i-1]; 
        }
        xpoints[index] = x;
        ypoints[index] = y;
        npoints ++;
    }

    public Approximation(Shape shape, double dcontour, int grid)
    {
        // doutline+flatness = approximated outline
        this(shape, dcontour,dcontour-1,grid);   
    }

    /**
     * Creates an approximation for the outline of the specified shape.
     * The approximation is represented as a grid of rectangles that
     * intersect with the shape.
     * 
     * The approximation is only available when {@link #update()} 
     * was called.
     * 
     * @param shape
     * @param dcontour
     * @param flatness optimal value is dcontour/2
     * @param grid
     */
    protected Approximation(Shape shape, double dcontour, double flatness, int grid)
    {
        this.shape = shape;
        this.dcontour = dcontour;
        this.grid = grid;
        this.gmid = grid/2;
        this.flatness = flatness;

        capacity = GROWTH;
        xpoints = new int[capacity];
        ypoints = new int[capacity];
        npoints = 0;

        // square distance threshold from a line to the center point of the nearest grid cell 
        sqThreshold = dcontour+Math.sqrt(2*grid*grid);
        sqThreshold *= sqThreshold;
        
        update();
    }
    
    public void update()
    {
        npoints = 0;

        PathIterator flatPath = shape.getPathIterator(null, flatness);
        double[] segment = new double[6];
        
        if (!flatPath.isDone())
        {
            // move to
            if (flatPath.currentSegment(segment)!=PathIterator.SEG_MOVETO)
            {
                // should not happen
                throw new IllegalStateException("expected SEG_MOVETO");
            }
            flatPath.next();

            double tx = segment[0];
            double ty = segment[1];
            double ttx, tty;
            
            boundsL = tx;
            boundsT = ty;
            boundsR = boundsL;
            boundsB = boundsT;

            while (!flatPath.isDone())
            {
                switch (flatPath.currentSegment(segment))
                {
                    case PathIterator.SEG_MOVETO:
                        tx = segment[0];
                        ty = segment[1];
                        break ;

                    case PathIterator.SEG_LINETO:
                    case PathIterator.SEG_CLOSE:

                        // remember last point
                        ttx = tx;
                        tty = ty;
                        
                        // get next point
                        tx = segment[0];
                        ty = segment[1];

                        // process line (last-point,next-point)
                        recognizedLine(ttx, tty, tx, ty);
                        
                        // enlarge bounds
                        if (boundsL>tx) boundsL = tx;
                        else if (boundsR<tx) boundsR = tx;
                        if (boundsT>ty) boundsT = ty;
                        else if (boundsB<ty) boundsB = ty;
                        
                        break;
                }
                flatPath.next();
            }
        }
        else
        {
            boundsL = 0;
            boundsT = 0;
            boundsB = 0;
            boundsR = 0;
        }
    }
    
    private int big(double xy)
    {
        xy += dcontour;
        //xy += (grid-(((int)xy)%grid))+1;
        return (int) (xy/grid);
    }
    
    private int small(double xy)
    {
        xy-=dcontour;
        return (int) (xy/grid); // floor
    }
    
    private void recognizedLine( double x1, double y1, double x2, double y2 )
    {
        int il, ir, it, ib;
        
        if (x1<x2)
        {
            il = small(x1); ir = big(x2);
        }
        else
        {
            il = small(x2); ir = big(x1);
        }
        
        if (y1<y2)
        {
            it = small(y1); ib = big(y2);
        }
        else
        {
            it = small(y2); ib = big(y1);
        }

        int ipx = (il*grid)+gmid;
        int jpx;
        int jpx_start=(it*grid)+gmid;
        for (int i=il;i<=ir;i++)
        {
            jpx = jpx_start;
            for (int j=it;j<=ib;j++)
            {
                if (Line2D.ptSegDistSq(x1,y1,x2,y2,ipx, jpx)<=sqThreshold)
                    put(i,j);
                jpx+=grid;
            }
            ipx+=grid;
        }
    }

    public Shape getShape()
    {
        return shape;
    }

    public double getCountourDistance()
    {
        return dcontour;
    }
    
    public double getFlatness()
    {
        return flatness;
    }
    
    public int getGridBoundsLeft()
    {
        return small(boundsL-dcontour);
    }
    
    public int getGridBoundsTop()
    {
        return small(boundsT-dcontour);
    }
    
    public int getGridBoundsRight()
    {
        return big(boundsR+dcontour);
    }
    
    public int getGridBoundsBottom()
    {
        return big(boundsB+dcontour);
    }

    public double getBoundsLeft()
    {
        return boundsL;
    }
    
    public double getBoundsTop()
    {
        return boundsT;
    }
    
    public double getBoundsBottom()
    {
        return boundsB;
    }
    
    public double getBoundsRight()
    {
        return boundsR;
    }
    
    public int getGrid()
    {
        return grid;
    }
    
    public PathIterator getPathIterator()
    {
        return new PathIterator()
        {

            public int getWindingRule()
            {
                return PathIterator.WIND_NON_ZERO;
            }

            public boolean isDone()
            {
                // TODO Auto-generated method stub
                return false;
            }

            public void next()
            {
                // TODO Auto-generated method stub
                
            }

            public int currentSegment( float[] coords )
            {
                // TODO Auto-generated method stub
                return 0;
            }

            public int currentSegment( double[] coords )
            {
                // TODO Auto-generated method stub
                return 0;
            }
            
        };
    }
    
}
