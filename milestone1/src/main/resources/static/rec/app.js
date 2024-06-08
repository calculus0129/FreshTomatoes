//document.addEventListener('DOMContentLoaded', function() {
//    fetch('/recommend/user?gender=F&age=1&occ=2')
//        .then(response => response.json())
//        .then(data => {
//            const moviesContainer = document.getElementById('movies');
//            data.forEach(movie => {
//                const movieElement = document.createElement('div');
//                movieElement.innerHTML = `
//                    <h3>${movie.title} (${movie.year})</h3>
//                    <p>Genres: ${movie.genres.join(', ')}</p>
//                `;
//                moviesContainer.appendChild(movieElement);
//            });
//        })
//        .catch(error => console.error('Error loading the movies:', error));
//});
document.getElementById('recommendationForm').addEventListener('submit', function (event) {
    event.preventDefault(); // Prevent the traditional form submission

    const form = event.target; // [object HTMLFormElement]
    const formData = new FormData(form); // [object FormData]
    const params = new URLSearchParams(formData).toString(); // gender=F&age=1&occ=1
    //    console.log(`form: ${form}\nformData: ${formData}\nparams: ${params}`);

    fetch(`/recommend/user?${params}`, { method: 'GET' })
        .then(response => response.json())
        .then(data => {
            console.log(data); // Handle the JSON data here
            // For example, update the DOM with the received movie recommendations
            const moviesContainer = document.getElementById('movies');
            moviesContainer.innerHTML = ''; // Clear previous results
            data.forEach(movie => {
                const movieElement = document.createElement('div');
                movieElement.innerHTML = `<h3><a target="_blank" rel="noopener noreferrer" href="https://duckduckgo.com/?t=h_&q=movie+${movie.title}+(${movie.year})">${movie.title} </a></h3><p>year: ${movie.year}<p>Genres: ${movie.genres.join(', ')}</p>`;
                moviesContainer.appendChild(movieElement);
            });
        })
        .catch(error => console.error('Failed to fetch data:', error));
});