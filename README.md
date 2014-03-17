LGA
===

This library helps to compute semi-supervised leasing methods[1] and 
personal pagerank method for large graphs. The implementation could work with any kind
of graph which could be implemented from fr.inria.maestro.lga.graph.model.IGraph interface.
We supposed that graph without loops and multiedges.

The library provides as well:
1) the convenient way to work with graphs in BVGraph[2], Pajek Net format and simple readable format: "nodeFrom nodeTo weight",
   let us name it as matlab format, the numeration of the nodes starts from 1
2) the namers to make the nodes with names
3) the semi-supervised learning classification, including computation of the presonal pagerank method 
4) the expert nodes/seed nodes format to load/save them  
5) the estimations to get the precision, recall, modularity and confusion matrix
6) the various seed choosers

[1] Generalized Optimization Framework for Graph-based Semi-supervised Learning. SDM 2012: 966-974
[2] The WebGraph Framework I: Compression Techniques,
 by Paolo Boldi and Sebastiano Vigna, in Proc. of the Thirteenth World Wide Web Conference, pages 595-601, 2004, ACM Press.


1)The formats
The library provides the convenient way to work with graphs in BVGraph[2], Pajek NET format and simple readable format: "nodeFrom nodeTo weight"
for weighted graph and "nodeFrom nodeTo" for unweighted.

BVGraph format
To load file in BVGraph format: 
String file = "/yourpath/yourBVGraph";// without suffix
IGraph graph = new BVGGraphLoader().load(file); 
To save file in BVGraph format:
BVGGraphSaver().save(graph, file);
If files "/yourpath/yourBVGraphlabels" exists the weghted graph will be loaded.

Pajek Net format
One of the common used formats. The first line represents number of vertices, the next lines represents node labels.
The edges defined as a pair of two nodes. After line "*Edges" exactly as in matlab format.
ILoader loader = new PajekNetLoader().load(file);
new PajekNetSaver().save(graph, file);
*Vertices 9
1 "Name1"
9 "Name"
*Edges
1 2 0.5
1 9 0.1

Matlab format
We call it matlab format, because  it could be easily loaded in matlab as sparse matrix.
This is a simple edge list with or without weights. The numeration of the nodes starts form 1.
File file = new File("/yourpath/yourmatlab.txt");
graph = MatlabLoader().load(file);
MatLabSaver().save(graph, file);

Any type of graphs could be used, if they implements fr.inria.maestro.lga.graph.model.IGraph interface.
See section about how to implement your own graph realization.

2)The namers
The library provides the simple realization of namer.
The "yourGraphNamer.txt" contains a column of names like:
Name1
Name2
...
NameN
The number of line corresponds to node id.
Note:To make it compatible with BVGraph library the names should be sorted in alphabetical order,
not necessary for our library.

String pathToNamer = "yourGraphNamer.txt";
INodeNamer namer = NodeNamerImpl.load(new File(pathToNamer));
or you can use IdenticalIdNodeNamer as your nodeId.
To setup the namer to graph:
graph.getProperties().setValue(IGraph.NODE_NAMER, namer);

3) The algorithms
The library provides the implementation of semi-supervised methods in general case with parameter sigma.
Also the library provides the implementations of particular algorithms: PageRank , Standatd Laplacian,
Normalized Laplacian methods, which could be significantly faster for large graphs in BVG format.
BVG format compress graph and any operation to access the data could take time.

Mainly use the general implementation to compute methods,
if it is slow than try the particular implementations of the methods.

The personal pagerank calculation (the implementation of particular method):
List<Integer> listOfNodes;//the list of nodes ids
IPersonalVector pV = new PersonalVector(listOfNodes,graph.numNodes());
CalculatorInputData iD = new CalculatorInputData(alpha); //has different setups
ICalculator pc = CalculatorFactory.getPageRankCalculator(graph, iD);
double[] ppr = pc.compute(pV.getPersonalVector()); //personalVector is a stohastic vector
The CalculatorInputData has defaults values as marked in Constants:
epsilon 0.000001, max number of iterations 100, alpha 0.5.

The general personal pagerank calculation (based on sigma parameter):
CalculatorInputDataSigma iD = new CalculatorInputDataSigma(sigma, alpha); //has different setups
//iD.setWeighted(true); to compute pagerank as for weighted graph, by default is false
IPagerankCalculator pc = CalculatorFactory.getGeneralCalculator(graph, iD);
double[] gppr = pc.compute(personalVector); //personalVector is a stohastic vector

The semi-supervised learning classification takes as input seeds
SemiSupervisedLearningMethod sslm = new SemiSupervisedLearningMethod();
int[] nodeToClassId = sslm.run(seedsInput, pc); // node I classified as nodeToClassId[I] class, if class equals to -1  means unclassified 

4) The expert/seeds files
Provides a convinient way to work with seeds/expert files.
The format for seeds/expert files:
"Short explanation about file. Could be empty line.

Class Name A	
NodeName1
NodeName2	

Class Name B	
NodeName3
"

IClustering seeds = ClusteringUtils.loadFromFile(new File(seedsName)); //load file
ISeedsInput seedsInput = ClusteringConverter.clustering2SeedsInput(seeds, namer, graph.numNodes()); //convert to seedInput object
IClustering resultClassification = ClusteringConverter.array2Custering(nodeToClassId, namer, seeds); //convert to clustering object from array        
ClusteringUtils.saveToFile(resultClassification, new File("someName.txt"));//save to file


5) The estimations 
Provides methods to get precision, recall and confusion matrix.
\\IClustering expert; IClustering alg;
ClusteringPreprocessor algClustering = new ClusteringPreprocessor(alg);
PrecisionQuantity prec = new PrecisionQuantity(new ClusteringPreprocessor(expert));
Pair<Double, Double> res =  prec.getPrecisionRecall(algClustering);
//res.getFirst() -- precision, res.getSecond() -- recall
prec.printConfusionMatrix(algClustering, new PrintWriter(System.out)) //to print Confusion Matrix

6) How to write your own graph implementation 
The library could work with any kind
of graph which could be implemented from fr.inria.maestro.lga.graph.model.IGraph interface.
IGraph is a simple interface:  
int numNodes(); //number of nodes
long numArcs(); //number of arcs (not nessecary for our library)
int outdegree(int node); //the outdegree of node 
int[] successorArray(int node); //the neighbours of node, for the compatibility with webgraph library 
//the returned array may contain more entries than the outdegree of node,
// the successors should be sorted in increasing order.
IProperties getProperties(); //some graph properties NODE_NAMER and UNDERLYING_GRAPH. 
Also see AbstractGraph implementation.



for weighted graphs IArcWeightedGraph has one additional method
double[] arcWeights(int node);// the appropriate weight with successorArray;
 
Note:
In BVGNameFilelabels.properties could be written the absolute path to the graph and 
the name of class which implement Labels. If you have some problem just modify this file 
manualy.

graphclass = it.unimi.dsi.webgraph.labelling.BitStreamArcLabelledImmutableGraph
underlyinggraph = absolute_path//BVGNameFile
labelspec = fr.inria.maestro.lga.graph.io.utils.WebGraphLabelsFactory$MyLabel()

class is written in 
fr.inria.maestro.lga.graph.io.utils.WebGraphLabelsFactory.MyLabel 