# Navigator
City navigator - test project

Dijkstra's algorithm and other algorithms solve a similar problem.
After searching for libraries in java, the jgrapht library was found. But in it, many algorithms solve the necessary problem only for a directed graph. An implementation was found which finds the shortest paths in an undirected graph. 
The algorithm (KShortestSimplePaths) is a variant of the Bellman-Ford algorithm. This algorithm returns K shortest paths between vertices. This algorithm was modified for get all paths between two cities.

Places where the route list is changed were marked with synchronized modifier to avoid problems with simultaneous changing of the list by several threads.

Exceptions were also added in cases when: 
- When the route between two cities is added with value <= 0
- When the route is added from the city to it (for example, from Minsk to Minsk)
- When trying to find out the route between cities that have not been added before
- When the route between cities is not found

All application logic was divided into 3 layers: controller, service, repository.
Also, unit tests were added to check the logic for calculating routes.

API documentation:
1) Save route segment 

   PUT: api/route
   
   Body example: 
      {
          "departure": {
              "name": "A"
          },
          "destination": {
              "name": "B"
          },
          "distance": 10
      }
2) Get route between cities

   GET: /api/route?departure=A&destination=B
