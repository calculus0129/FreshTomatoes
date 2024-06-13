
$("#submit-btn").click(function (event) {
    // variables for each parameters
    var plot = $("#plot").val();
    var genres = $("#genres").val();
    var stars = $("#stars").val();
    var directors = $("#directors").val();

    $.ajax({
    // GET REST API 
      dataType: "JSON",
      url: `http://localhost:8080/chat?plot=${plot}&genres=${genres}&stars=${stars}&directors=${directors}`,
      contentType: "application/json; charset=utf-8",
      method: "GET",

      success: function (response) {
        const moviesContainer = document.getElementById('moviecandidiate');
            moviesContainer.innerHTML = ''; // Clear previous results

            // Visualizing format
            const movieCard = `
                <div class="card mb-3">
                    <div class="card-header">
                        <h2>${response.title}</h2>
                        <a href="${response.imdb_link}" target="_blank">IMDB Link</a>
                    </div>
                    <div class="card-body">
                        <p><strong>Year:</strong> ${response.year}</p>
                        <p><strong>Parental Guide:</strong> ${response.parental_guide}</p>
                        <p><strong>Runtime:</strong> ${response.runtime}</p>
                        <p><strong>IMDB Rating:</strong> ${response.imdb_rating}</p>
                        <p><strong>Genres:</strong> ${response.genres.join(', ')}</p>
                        <p><strong>Plot:</strong> ${response.plot}</p>
                        <p><strong>Director(s):</strong> ${response.director.join(', ')}</p>
                        <p><strong>Writer(s):</strong> ${response.writers.join(', ')}</p>
                        <p><strong>Star(s):</strong> ${response.stars.join(', ')}</p>
                    </div>
                </div>
            `;

            moviesContainer.innerHTML = movieCard;
            $('#candidate').show();
      },

      error: function () {
        console.log("Error occurred during AJAX request.");
      }
    });
});