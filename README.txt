Overview
TSP is a NP hard problem in computer science, that identifies the minimal cost of visiting
all the cities only once and getting back to the city from where the tour started.

Approach;
This is heuristic based parallel TSP. The program uses the simple heuristic to prune the exponential 
growth of partial TSP tour that are generated in progress. 
The number of threads spawned by the program is the total number of cores present in the machine.
The simple heuristic for the approach is generating the tour with minimal cost first.


Input/Output:
The input for the program is adjacency matrix.
The first line of the input has to be the total number of cities ( say n ) and that should be 
followed by n X n matrix.

Sample input and output



Input:
10
 0 29 82 46 68 52 72 42 51 55 
 29  0 55 46 42 43 43 23 23 31 
 82 55  0 68 46 55 23 43 41 29 
 46 46 68  0 82 15 72 31 62 42 
 68 42 46 82  0 74 23 52 21 46 
 52 43 55 15 74  0 61 23 55 31 
 72 43 23 72 23 61  0 42 23 31 
 42 23 43 31 52 23 42  0 33 15 
 51 23 41 62 21 55 23 33  0 29 
 55 31 29 42 46 31 31 15 29  0

Output:
Tour : 0 ,1 ,8 ,4 ,6 ,2 ,9 ,7 ,5 ,3 ,0(  Cost is : 247 )


Input:
12
 0 29 82 46 68 52 72 42 51 55 29  74 
 29  0 55 46 42 43 43 23 23 31 41 51
 82 55  0 68 46 55 23 43 41 29 79 21 
 46 46 68  0 82 15 72 31 62 42 21 51 
 68 42 46 82  0 74 23 52 21 46 82 58 
 52 43 55 15 74  0 61 23 55 31 33 37 
 72 43 23 72 23 61  0 42 23 31 77 37  
 42 23 43 31 52 23 42  0 33 15 37 33 
 51 23 41 62 21 55 23 33  0 29 62 46  
 55 31 29 42 46 31 31 15 29  0 51 21  
 29 41 79 21 82 33 77 37 62 51  0 65 
 74 51 21 51 58 37 37 33 46 21 65  0
 
 Output:
 Tour : 0 ,10 ,3 ,5 ,7 ,9 ,11 ,2 ,6 ,4 ,8 ,1 ,0(  Cost is : 264 )
 
