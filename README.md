# FreshTomatoes
## SetUp
From the directory where our submitted Dockerfile and run.sh file are in, <br/>
Build docker image and container
```
docker build -t ms1:cn .
docker run -it --name cn_container ms1:cn
```
and inside the docker container, run `sh run.sh` 

After all tests are finished(each test includes starting spring application and loading data), <br/>
and after the spring application starts and all data are uploaded(last log message: User data all uploaded!), <br/>
open new terminal of the same docker container <br/>
and see the implementation result by using below examples: <br/>

## Curl Examples
### Feature1 : Finding movie titles via hints(plot, genre, actor, bgm); uses ChatGPT API
   #### GET RestAPI /chat
   It has request parameters plot, genre, actor, and bgm. And the user requests for each of sections are processed as a prompt query that is appropriate for the ChatGPT to give out matching movie title results. And the API process the ChatGPT's answer in the form "These are the candidates: movie1 name: ~".
   - Example:
   - `curl "http://localhost:8080/chat?plot=they%20save%20the%20earth&genre=action&actor=Chris%20Evans&bgm=I%20don%27t%20remember"`
### Feature2 : Temporal Visualization of movie genres and movies
   #### Get RestAPI /movieInfo/genres
   Through this API, user can get auto completed genre name from the partial name. This API is required because user can know which genres can be accessed through our application.
   - Examples:
   - `curl http://localhost:8080/movieInfo/genres?s=F`
   - Output: list of genre name start with "F"
   - `curl http://localhost:8080/movieInfo/titles?s=Toygenres?s=Documentarya`
   - Output: empty list
#### GET RestAPI /movieInfo/titles
   This API has similar intention with the above RestAPI, through this, user can get auto completed movie title from the partial title.
   - Examples:
   - `curl http://localhost:8080/movieInfo/titles?s=Toy%20Sto`
   - Output: list of movie titles starting with "Toy Sto"
Below API's are for exporting csv that is matched with the user's request.
#### GET RestAPI /movieInfo/csv/timeline
   This API export csv that has number of movies along time for each genre.
   - `curl http://localhost:8080/movieInfo/csv/timeline`
#### GET RestAPI /movieInfo/csv/timeline/one
   This API export csv that has number of movies along time for one requested genre.
   - `curl http://localhost:8080/movieInfo/csv/timeline/one?g=Sci-Fi`
   - `curl http://localhost:8080/movieInfo/csv/timeline/one?g=Action`
#### GET RestAPI /movieInfo/csv/year
   This API export csv that has movies that are released in the same year, sorted by number of ratings done for each of them
   -Examples:
   - export csv for movie list sorted with increasing rating numbers released in the requested year (e.g. 1970)
   - `curl -X GET http://localhost:8080/movieInfo/csv/year?y=1970`
   - export csv for movie list sorted with increasing rating numbers released in the same year with the requested movie (e.g. Toy Story (1995))
   - `curl -X GET http://localhost:8080/movieInfo/csv/year?t="Toy%20Story%20(1995)"`

### Feature3 : Recommendation via User Information
   - List the movies rated by the users with the given user information(gender, age, occupation), sorted in averageRating-decreasing, reviewNumber-decreasing, and movieTitle lexicographic order.
   - Examples:
     - `curl "http://localhost:8080/recommend/user?gender=F&age=18&occ=20"`
       - Output: 1483 of movie data sorted.
     - `curl "http://localhost:8080/recommend/user?gender=M&age=18&occ=13"`
       - Output: []. That's because there are no such user rating information.





