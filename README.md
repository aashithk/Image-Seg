Image-Seg
=========

Image segmentation using a graph min cut approach
In this project we implement multi-image segmentation which is interactive in nature.
Image segmentation is said to be interactive if it involves feedback from the user. 
In this project, we study one particular approach proposed by Boykov and Jolly to solve the
interactive image segmentation problem. Boykov- Jolly introduced the technique of graph cuts
to achieve the goal of partitioning images into two segments, that they call "object" and
"background". In particular, the problem of interactive segmentation is reduced to the
problem of finding graph min-cuts, which is then solved using the max  flow algorithm 
of Boykov and Kolmogorov. We implement the Boykov-Jolly method and analyze its performance 
against various test cases. An optimization that improves the efficiency of the algorithm 
is proposed and incorporated in our implementation. We give two approaches to extend the 
method to achieve image segmentation where the number of partitions can be more than two.
The implementation, along with the results, of the one of these approaches is also provided.
