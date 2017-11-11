Best read with the "raw" option.

Beachte, dass hier nicht Java Arrays gemeint sind!!
!!Pseudocode Array[1] Startknoten <-> Java Array[0] Startknoten !!

Input: Matrix A (n x n) wobei Knoten 1 der Startknoten und Knoten n der Endknoten  
ist. Ein Eintrag [1][n]=1000000 ist stellt unendlich dar. [i][j]= -1 genau dann,  
wenn Knoten j vor Knoten i kommen muss.  
Output: Eine gültige Tour, welche alle Präzedenzen erfüllt, als Liste B.  

int CurNumDependencies =-1;  
int[n] dependenciesPerVertex;  
initialisiere mit null, und 1,n=-1  
int VertexWithHighestDependencyCount=-1;  
for every vertex != 1,n do  
	for i=2 to n-1 do  
			if A[i][v]==-1  
				then dependenciesPerVertex[v]++  
			fi  
	od  
od  
while(length of B < length of A-2) do  
	for every vertex v != 0,n do  
			if(dependenciesPerVertex[v] > CurNumDependencies)   
				curNumDependencies=dependenciesPerVertex[v];  
				VertexWithHighestDependencyCount = v;  
			else   
				if(dependenciesPerVertex[v] = CurNumDependencies && CurNumDependencies != -1)  
					if(A[VertexWithHighestDependencyCount][v] == -1)  
							VertexWithHighestDependencyCount = v;  
					fi  
				fi  
	od  
	Hänge VertexWithHighestDependencyCount an B an;  
	dependencyPerVertex[VertexWithHighestDependencyCount]=-1;
	curNumDependencies = -1;  
	VertexWithHighestDependencyCount=-1;  
od  
Gebe B zurück.  
