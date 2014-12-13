import java.awt.datatransfer.MimeTypeParseException;
import java.io.Console;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.BackgroundSubtractorMOG;

public class MainClass {
	static{ System.loadLibrary("opencv_java249"); }

Mat image;
Mat procImg;
Mat Backup;
int [][] arr; // matrix with 0 ,1,2
int [][] fgraph; // n pixel matrix
int [] intensity; // black  white image
int [] usermark;
int objcount=0;
int backcount=0;
int col=0;
int row =0;
 public MainClass(String s)
 {
	 image = Highgui.imread(s);
	 Backup=image;
	Mat mat1 = new Mat(image.height(),image.width(),CvType.CV_8UC1);
	Imgproc.cvtColor(image, mat1, Imgproc.COLOR_RGB2GRAY);
	 image=mat1;
	 col=image.cols();
	 row=image.rows();
	 arr=new int[col][row];
	// intensity =new int[col*row];
	 //fgraph=new int[(col)*(row)+2][(col)*(row)+2];
	 //usermark=new int[col*row];
	 procImg=new Mat(col, row, Highgui.IMREAD_ANYCOLOR);
	 
 }
 public void setPoint(int x,int y,int val)
 {
	 arr[x][y]=val;
	 
 }
 public int getCol()
 {
	 return col;
	 
 }
 public int getRow()
 {
	 return row;
	 
 }
 public void bgcalc()
 {
	 image=Backup;
	 Imgproc.Canny(image, image, 300, 600, 5, true); 
	 Highgui.imwrite("test.jpg", image);
 }
 public void bgsub()
 {
	 
	 
	 
	 BackgroundSubtractorMOG sub = new BackgroundSubtractorMOG(3, 4, 0.8);
	 Mat mGray = new Mat();
	 Mat mRgb = new Mat();
	 Mat mFGMask = new Mat();

	 
	     //I chose the gray frame because it should require less resources to process
	   //  Imgproc.cvtColor(image, mRgb, Imgproc.COLOR_GRAY2RGB); //the apply function will throw the above error if you don't feed it an RGB image
	     sub.apply(mRgb, mFGMask); //apply() exports a gray image by definition
	     
		//BackgroundSubtractorMOG bs = new BackgroundSubtractorMOG();
		//Mat mat1 = new Mat(image.height(),image.width(),CvType.CV_8UC1);
		//Imgproc.cvtColor(image, mat1, Imgproc.COLOR_RGB2GRAY);
		//image=mat1;
		//bs.apply(image, procImg);

		Highgui.imwrite("test.jpg", mFGMask);
		
		//bs.apply(image, procImg);
//		Highgui.imwrite("test.jpg", procImg);
 }
public void printImage()
{
	
	image=Backup;
	
	 
	/*for(int i=0;i<col;i++)
	{
		for(int j=0;j<row;j++)
		{
			System.out.print("row: "+i+""+j+"  ");
			double [] ab=image.get(j, i);
			for(double ac : ab)
			{
				
				System.out.print(ac);
			}
			System.out.print("  ");
		}
		System.out.println();
	}
	for(int i=0;i<col;i++)
	{
		for(int j=0;j<row;j++)
		{
			int sum=0;
			double [] ab=image.get(j, i);
			for(double ac : ab)
			{
				
				sum=sum+(int)ac;
			}
			intensity[i][j]=sum;
		}
	}*/
	for(int i=0;i<col;i++)
	{
		for(int j=0;j<row;j++)
		{
			int sum=0;
			double [] ab=image.get(j, i);
				
				sum=sum+(int)ab[0];
				sum=sum+(int)ab[1]*1000;
				sum=sum+(int)ab[2]*1000000;
			
			intensity[i*row+j]=sum;
			//intensity[i*row+j]=(int) image.get(j, i)[0];
		}
		
	}
	for(int i=0;i<col;i++)
	{
		for(int j=0;j<row;j++)
		{
			usermark[i*row+j]=arr[i][j];
		}
		
	}
	objcount=0;
	backcount=0;
	for(int i=0;i<col;i++)
	{
		for(int j=0;j<row;j++)
		{
			if(arr[i][j]==1)
			{//System.out.print("col: "+i+"row: "+j+""+arr[i][j]+"\n");
				objcount++;
			}
			if(arr[i][j]==2)
			{//System.out.print("col: "+i+"row: "+j+""+arr[i][j]+"\n");
				backcount++;
			}
			
		}
	}
	int n=col*row;
	int k=10,lam=7;
	int[][] r=new int [n][2];
	int [][] b=new int[n][n];
	int max=0,x=0;
	int cost=1;
	int sigma=2;
	
	for(int i=0;i<n;i++)
	{ 
	r[i][0]=(int) (Math.log(intensity[i]/objcount));
	r[i][1]=(int) (Math.log(intensity[i]/backcount));
	}
	for(int i=0;i<n;i++)
	{   
		x=0;
		for(int j=0;j<n;j++)
		{
			if((j==(i-1)) || (j==(i+1)) || j==(i-row) || j==(i+row) )
			{
				b[i][j]=(int) ( Math.pow(Math.E, -1*Math.pow((intensity[i]-intensity[j])/(2*sigma*sigma), 2)));
				x+=b[i][j];
			}
			if( j==(i+row-1) || j==(i+row+1) || j==(i-row-1) || j==(i-row+1))
			{
				b[i][j]=(int) (Math.pow(Math.E, -1*Math.pow((intensity[i]-intensity[j])/(2*sigma*sigma), 2))*1.44);
				x+=b[i][j];
			}
		}
		if(x>max)
		{
			max=x;
		}
		
	}
	k=max+1;
/*	Random rand=new Random();
	for(int i=0;i<n;i=i+10)
	{
		int o=rand.nextInt(2);
		usermark[i]=o+1;
	}*/
	for(int i=0;i<n;i++)
	{
		if(usermark[i]==2)
		{
			fgraph[i][n+1]=k;
			fgraph[n+1][i]=k;

		}
		else if(usermark[i]==1)
		{
			fgraph[i][n+1]=0;
			fgraph[n+1][i]=0;

		}
		else
		{
			fgraph[i][n+1]=lam*r[i][0];
			fgraph[n+1][i]=lam*r[i][0];


		}
	}
	for(int i=0;i<n;i++)
	{
		if(usermark[i]==2)
		{
			fgraph[i][n]=0;
			fgraph[n][i]=0;
		}
		else if(usermark[i]==1)
		{
			fgraph[i][n]=k;
			fgraph[n][i]=k;
		}
		else
		{
			fgraph[i][n]=lam*r[i][1];
			fgraph[n][i]=lam*r[i][1];

		}
	}
	for(int i=0;i<n;i++)
	{
		for(int j=0;j<n;j++)
		{
			if((j==(i-1)) || (j==(i+1)) || j==(i-row) || j==(i+row) || j==(i+row-1) || j==(i+row+1) || j==(i-row-1) || j==(i-row+1))
			{
			fgraph[i][j]=b[i][j];
			}
		}
	}
	//Mat fimage=
	
	
	//ff.ford_fulkerson(fgraph, n, n+1);
		Main.maxflow(fgraph,n, n+1);
	 System.out.println("The main flow is " + Main.answer); 
	 double[] data1=new double[3];
	 data1[0]=0;
	 data1[0]=0;
	 data1[0]=0;
	 
	 double[] data2=new double[3];
	 data2[0]=255;
	 data2[0]=255;
	 data2[0]=255;
	 image=Backup;
	 
	 for(int i=0;i<col;i++)
		{
			for(int j=0;j<row;j++)
			{
				
				//if(Main.Tree[i*row+j]==1)
				//{
				//procImg.put(j, i, data1);
				//}
				
				 if(Main.Tree[i*row+j]==2)
				{
				image.put(j, i, data1);
				}
				 if(Main.Tree[i*row+j]==1)
					{
					image.put(j, i, data2);
					}
				
			}
			
		}
	 image=Backup;
	 Imgproc.Canny(image, image, 300, 600, 5, true); 
	 Highgui.imwrite("test.jpg", image);
	// System.out.println("The ford flow is " + ff.ford_fulkerson(fgraph,n, n+1));
	/* System.out.println("the first set is");
	 	for(int i=0;i<ff.j;i++)
	 	{
	 		System.out.print(ff.set1[i]+ "  ");
	 	}
	 	System.out.println();
	 	System.out.println("The second set is");
	 	for(int i=0;i<ff.k;i++)
	 	{
	 		System.out.print(ff.set2[i]+ "  ");
	 	}
*/
}
 


}




class ff {
	
	 
	private static final int INT_MAX = 100000;

	// Number of vertices in given graph
	static int n=10000000,i,j=0,k=0;

	static boolean[] visited = new boolean[n];
	static int[] set1 = new int[n];
	static int[] set2 = new int[n];
	/* Returns true if there is a path from source 's' to sink 't' in
	  residual graph. Also fills parent[] to store the path */
	static boolean bfs(int[][] rgraph, int s, int t, int[] parent)
	{
	    
	    
	    for(i=0;i<n;i++)
	    {
	    	visited[i]=false;
	    }
	 
	    
	    Queue<Integer> q = new LinkedList<Integer>();
	    q.add(s);
	    visited[s] = true;
	    parent[s] = -1;
	 
	    
	    while (!q.isEmpty())
	    {
	        int u =  (Integer) q.peek();
	        q.remove();
	 
	        for (int v=0; v<n; v++)
	        {
	            if (visited[v]==false && rgraph[u][v] > 0)
	            {
	                q.add(v);
	                parent[v] = u;
	                visited[v] = true;
	            }
	        }
	    }
	 
	    // If we reached sink, return true
	    if(visited[t] == true)
	    {
	    	return true;
	    }
	    else
	    {
	    	return false;
	    }
	}

	public static int ford_fulkerson(int[][] graph, int s, int t)
	{
	    int u, v;
	    n = graph.length;
	    // Create a residual graph and fill the residual graph with
	    
	    int[][] rgraph = new int[n][n]; 
	    for (u = 0; u < n; u++)
	        for (v = 0; v < n; v++)
	             rgraph[u][v] = graph[u][v];
	 
	    int[] parent = new int[n];  
	 
	    int max_flow = 0;  
	    int count =0;
	    // Augment the flow while there is path from source to sink
	    while (bfs(rgraph, s, t, parent))
	    {
	        
	        //find the maximum flow
	     //  System.out.println(count++);
	        int path_flow = INT_MAX;
	        for (v=t; v!=s; v=parent[v])
	        {
	            u = parent[v];
	            if(rgraph[u][v] < path_flow)
	            {
	            	path_flow = rgraph[u][v];
	            }
	        }
	 
	        // update residual capacities of the edges and reverse edges
	       
	        for (v=t; v != s; v=parent[v])
	        {
	            u = parent[v];
	            rgraph[u][v] -= path_flow;
	            rgraph[v][u] += path_flow;
	        }
	 
	        // Add path flow to overall flow
	        max_flow += path_flow;
	    }

		
		bfs(rgraph, s, t, parent);
		for(i=0;i<n;i++)
		{
			if(visited[i]==true)
			{
				set1[j] = i;
				j++;
			}
			else
			{
				set2[k] = i;
				k++;
			}
		}
		
	return max_flow;
	}
	 
	/*public static void main(String[] args)
	{
	    
	    int[][] graph = { 	{0, 16, 13, 0, 0, 0},
	                        {0, 0, 10, 12, 0, 0},
	                        {0, 4, 0, 0, 14, 0},
	                        {0, 0, 9, 0, 0, 20},
	                        {0, 0, 0, 7, 0, 4},
	                        {0, 0, 0, 0, 0, 0}
	                      };
	 
	    System.out.println("The maximum flow is " + ford_fulkerson(graph, 0, 5));
	 
	 	System.out.println("the first set is");
	 	for(i=0;i<j;i++)
	 	{
	 		System.out.print(set1[i]+ "  ");
	 	}
	 	System.out.println();
	 	System.out.println("The second set is");
	 	for(i=0;i<k;i++)
	 	{
	 		System.out.print(set2[i]+ "  ");
	 	}
	 	
	}*/
	
	
	
}



class Main {

	static int n=100000,i,j=0,k=0,p,q,x,ll,temp,y,answer;
	static int[] S = new int[n];
	static int[] T = new int[n];
	static int[] A = new int[n];
	static int[] O = new int[n];
	static int[] Tree = new int[n];
	static int[] Parent = new int[n];
	static ArrayList<Integer> P = new ArrayList<Integer>();
	
	public static int grow(int[][] graph, int[][] rgraph, int s, int t)
	{
		//grow S or T to find an augmenting path P from s to t
		n= graph.length;
		P.clear();
		for(p=0;p<n;p++)
		{
			if(A[p]==1)
			{
				//for each neighbour
				//System.out.println(p + "hey");
				for(q=0;q<n;q++)
				{
						if(((Tree[p]==1 && rgraph[p][q]>0) || (Tree[p]==2 && rgraph[q][p]>0)))
						{
							if(Tree[q]==0)
							{
								A[q]=1;
								Tree[q] = Tree[p];
								if(q==3)
								{
								//System.out.println(q + " hghjgj " + Tree[q] +" p is "+ p);
								}
								Parent[q] = p;
															
							}
							else if(Tree[q]!=0 && Tree[q]!=Tree[p])
							{
								
								//P is path from s to t
								//if(p==1041)
								//System.out.println(p+"     "+q);
								if(Tree[p]==1)
								{
									
									x = p;
									P.add(x);
									while(x!=s)
									{
										if(p==967)
										{
											//System.out.println(x);
										}
										x = Parent[x];
										P.add(x);
										
									}
									
									ll = P.size();
									for(i=0;i<ll/2;i++)
									{
										temp = (int) P.get(i);
										P.set(i, P.get(ll-i-1));
										P.set(ll-i-1,temp);
										
									}
									x = q;
									P.add(x);
									while(x!=t)
									{
										x = Parent[x];
										P.add(x);
										
									}
									
								}
								else
								{
									x = q;
									P.add(x);
									while(x!=s)
									{
										x = Parent[x];
										P.add(x);
										
									}
									ll = P.size();
									for(i=0;i<ll/2;i++)
									{
										temp = (int) P.get(i);
										P.set(i, P.get(ll-i-1));
										P.set(ll-i-1,temp);
										
									}
									
									x = p;
									P.add(x);
									while(x!=t)
									{
										x = Parent[x];
										P.add(x);
										
									}
									
									
								}
								/*for(i=0;i<P.size();i++)
								{
									System.out.print(P.get(i) + " ");
									
								}*/
								P.add(-1);
								//System.out.println();
								return 0;
								
							}
							
						}
					
				}
				
				
			}
			A[p]=0;
		}
		
		return 0;
	}
	
	public static int valid_parent(int[][] graph, int[][] rgraph, int p,int s,int t)
	{
		int ans = -1;
		for(q=0;q<n;q++)
		{
				if(Tree[q]==Tree[p] && ((Tree[q]==1 && rgraph[q][p]>0) || (Tree[q]==2 && rgraph[p][q]>0)))
				{
					//is origin of q source or sink
					x=q;
					while(Parent[x]!=-1)
					{
						x = Parent[x];
					}
					if(x==s || x==t)
					{
						return q;
					}
				
				}
		
		}		
		return ans;
		
		
		
		
		
		
	}
	public static int maxflow(int[][] graph, int s, int t)
	{
		n= graph.length;
		answer=0;
		boolean[] visited = new boolean[n];
		int[][] rgraph = new int[n][n];
		
		for(i=0;i<n;i++)
		{
			for(j=0;j<n;j++)
			{
				rgraph[i][j] = graph[i][j];
			}	
		}
		//for(i=0;i<20;i++)
		{
			//System.out.println(rgraph[i][1041]);
		}
		for(i=0;i<n;i++)
		{
			S[i] = T[i] = A[i] = O[i] = Tree[i] = 0;
			Parent[i] = -1;
		}
		A[s] = S[s] = 1;
		A[t] = T[t]=1;
		//in Tree: 1 is set S, 2 is set T and 0 is neither
		Tree[s] = 1;
		Tree[t] = 2;
	
		while(true)
		{
			//growth stage
			grow(graph,rgraph,s,t);
			
			//	if P =  terminate
			if(P.isEmpty())
			{
				break;
				
			}
			
			
			//***************augment on P*******************//
			
			//find bottleneck
			x = s;
			i=1;
			y = (int)P.get(i);
			int min_flow = rgraph[x][y];
			while(x!=t)
			{
				if(rgraph[x][y] < min_flow)
				{
					min_flow = rgraph[x][y];
				}
				i++;
				x=y;
				y = (int)P.get(i);
			}
			
			
			//update residual graph
			x = s;
			i=1;
			y = (int)P.get(i);
			answer+= min_flow;
			
			while(x!=t)
			{
				
				rgraph[x][y] -= min_flow;
				rgraph[y][x] += min_flow;
				
				//if edge is saturated
				if(rgraph[x][y]==0)
				{
					if(Tree[x]==1 && Tree[y]==1)
					{
						Parent[y] = -1;
						O[y] = 1;
					}
					if(Tree[x]==2 && Tree[y]==2)
					{
						Parent[x] = -1;
						O[x] = 1;
					}
					
				}
				i++;
				x=y;
				y = (int)P.get(i);
			}
			
			//System.out.println("the min flow is " + min_flow);
			/*for(i=0;i<n;i++)
			{
				for(j=0;j<n;j++)
				{
					System.out.print(rgraph[i][j]+ "  ");
				}	
				System.out.println();
			}
			System.out.println();*/
			//*********adopt orphans***************//
			int check=1;
			while(check>0)
			{
				check=0;
			
			for(p=0;p<n;p++)
			{
				//for each orphan node
				if(O[p]==1)
				{
					//remove from list of orphans
					O[p]=0;
					
					//process
					x = valid_parent(graph,rgraph,p,s,t);
					
					//x is a valid parent
					if(x>=0)
					{
						Parent[p] = x;
					}
					//no valid parent found
					else
					{
						for(q=0;q<n;q++)
						{
							if(Tree[q]==Tree[p])
							{
								if((Tree[q]==1 && rgraph[q][p]>0) || (Tree[q]==2 && rgraph[p][q]>0))
								{
									A[q] = 1;
								}
								
								if(Parent[q]==p)
								{
									O[q]=1;
									Parent[q]=-1;
								}
								
								
							}
						}
						
						Tree[p]=0;
						A[p]=0;
						
					}
					
					
				}
				
			}
			for(p=0;p<n;p++)
			{
				if(O[p]==1)
				{
					check=1;
				}
			}
			
			}
			
		
		}
		
		
		
		return 0;
	}
}