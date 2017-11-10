Input: Matrix A (n x n) wobei Knoten 1 der Startknoten und Knoten n der Endknoten 
ist. Ein Eintrag [1][n]=1000000 ist stellt unendlich dar. [i][j]= -1 genau dann,
wenn Knoten j vor Knoten i kommen muss.
Output: Eine gültige Tour, welche alle Präzedenzen erfüllt, als Liste B.

int CurNumDependencies =-1;
int[n] dependenciesPerVertex;
initialisiere mit null, und 0,n=-1
int VertexWithHighestDependencyCount=-1;
for every vertex != 0,n do
	for j=1 to n-1 do
		for i=1 to n-1 do
			if A[i][j]==-1
				then dependenciesPerVertex[j]++
			fi
		od
	od
od
while(length of B < length of A-2) do
	for every vertex v != 0,n do
			if(dependenciesPerVertex > CurNumDependencies) 
				curNumDependencies=dependenciesPerVertex;
				VertexWithHighestDependencyCount = v;
			else 
				if(dependenciesPerVertex = CurNumDependencies && CurNumDependencies != -1)
					if(A[VertexWithHighestDependencyCount][v] == -1)
							VertexWithHighestDependencyCount = v;
					fi
				fi
	od
	Hänge VertexWithHighestDependencyCount an B an;
	curNumDependencies = -1;
	VertexWithHighestDependencyCount=-1;
od
Gebe B zurück.