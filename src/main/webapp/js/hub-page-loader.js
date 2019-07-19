// Get ?hubId=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterHubId = urlParams.get('hubId');

// URL must include ?hubId=XYZ parameter. If not, redirect to hub list.
if (!parameterHubId) {
  window.location.replace('hub-list.html');
}

/**
 * Requests then displays the user's data if they entered any.
 * Diplays 'This user has not entered any information yet.' otherwise.
*/
function fetchHubInfo(){
  const url = '/hub?hubId=' + parameterHubId;
  fetch(url).then((response) => {
    return response.text();
  }).then((hub) => {
    document.getElementById('page-title').innerText = hub.name;
    document.title = hub.name;

    const hubInfoContainer = document.getElementById('hub-info-container');
    
    hubInfoContainer.innerHTML = hub;

  });
}

/** Fetches reviews and add them to the page. */
function fetchHubReviews() {
  const url = '/reviews?hubId=' + parameterHubId;
  fetch(url).then((response) => {
    return response.json();
  }).then((reviews) => {
    const reviewsContainer = document.getElementById('review-container');
    if (reviews.length == 0) {
      reviewsContainer.innerHTML = '<p>This hub has no reviews yet.</p>';
    } else {
      reviewsContainer.innerHTML = '';
    }
    reviews.forEach((review) => {
      const reviewDiv = buildReviewsDiv(review);
      reviewsContainer.appendChild(reviewDiv);
    });
  });
}

/** Fetches data and populates the UI of the page. */
function buildHubPageUI() {
  fetchHubInfo();
  fetchHubReviews();
}
