/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');

// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
  window.location.replace('/');
}

/** Sets the page title based on the URL parameter username. */
function setPageTitle() {
  document.getElementById('page-title').innerText = parameterUsername;
  document.title = parameterUsername + ' - User Page';
}

/**
 * Shows the review form if the user is logged in and viewing their own page.
 */
function showReviewFormIfViewingSelf() {
  fetch('/login-status')
      .then((response) => {
        return response.json();
      })
      .then((loginStatus) => {
        if (loginStatus.isLoggedIn &&
            loginStatus.username == parameterUsername) {
          const reviewForm = document.getElementById('review-form');
          reviewForm.classList.remove('hidden');
          document.getElementById('about-me-form').classList.remove('hidden');
        }
      });
}

/** Fetches reviews and add them to the page. */
function fetchReviews() {
  const url = '/reviews?user=' + parameterUsername;
  fetch(url)
      .then((response) => {
        return response.json();
      })
      .then((reviews) => {
        const reviewsContainer = document.getElementById('review-container');
        if (reviews.length == 0) {
          reviewsContainer.innerHTML = '<p>This user has no posts yet.</p>';
        } else {
          reviewsContainer.innerHTML = '';
        }
        reviews.forEach((review) => {
          const reviewDiv = buildReviewsDiv(review);
          reviewsContainer.appendChild(reviewDiv);
        });
      });
}

/**
 * Builds an element that displays the review.
 * @param {review} review
 * @return {Element}
 */
function buildReviewsDiv(review) {
  const headerDiv = document.createElement('div');
  headerDiv.classList.add('card-footer');
  headerDiv.appendChild(document.createTextNode(new Date(review.timestamp)));

  const bodyDiv = document.createElement('div');
  bodyDiv.classList.add('card-body');
  bodyDiv.innerHTML = review.text;

  const hubDiv = document.createElement('div');
  hubDiv.classList.add('card-header');
  hubNameNode = document.createElement('a');
  hubNameNode.setAttribute("href", "/hub.html?hubId=" + review.hubId);
  hubNameNode.appendChild(document.createTextNode(review.hub));
  hubDiv.appendChild(hubNameNode);
  hubDiv.appendChild(document.createTextNode(' - ' + review.rating + ' stars'));

  const reviewDiv = document.createElement('div');
  reviewDiv.classList.add('card');
  reviewDiv.appendChild(hubDiv);
  reviewDiv.appendChild(bodyDiv);
  reviewDiv.appendChild(headerDiv);

  return reviewDiv;
}

/** Creates an element for the dropdown of hubs. **/
function makeHubDropdown() {
  fetch('/hub-list').then((response) => {
    return response.json();
  }).then((hubs) => {
    const hubList = document.getElementById('hubId');
    hubs.forEach((hub) => {
      var child = document.createElement('option');
      child.textContent = hub.name;
      child.value = hub.id;
      hubList.appendChild(child)
    });
  });
}

/** Fetches data and populates the UI of the page. */
function buildUI() {
  setPageTitle();
  showReviewFormIfViewingSelf();
  fetchReviews();
  fetchAboutMe();
  makeHubDropdown();
  const config = {removePlugins: [ 'ImageUpload' , 'blockQuote'],
                  toolbar: [ 'heading', '|', 'bold', 'italic', 'link', 'bulletedList', 'numberedList' ]};
  ClassicEditor.create(document.getElementById('review-input'), config);
}

/**
 * Requests then displays the user's data if they entered any.
 * Diplays 'This user has not entered any information yet.' otherwise.
*/
function fetchAboutMe(){
  const url = '/about?user=' + parameterUsername;
  fetch(url).then((response) => {
    return response.text();
  }).then((aboutMe) => {
    const aboutMeContainer = document.getElementById('about-me-container');
    if(aboutMe == ''){
      aboutMe = 'This user has not entered any information yet.';
    }
    
    aboutMeContainer.innerHTML = aboutMe;

  });
}
