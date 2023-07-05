// [1] - Cookie reader

// Separates the cookie into an array.
const cookieArr = document.cookie.split("=");

// Assigns our variable with the userId from the cookie.
const userId = cookieArr[1];

// DOM Elements (I guess it is more correct to say DOM as opposed to HTML like the instructions do. Left the other two js files as is.)
const postForm = document.getElementById('post-form');
const postContainer = document.getElementById('post-container');
const newTitle = document.getElementById('post-title');
const newBody = document.getElementById('post-body');
const whereAmILink = document.getElementById('where-am-i');

// Modal Elements
let postBody = document.getElementById('update-post-body');
let updatePostBtn = document.getElementById('update-post-button')

// Header
const headers = {
    'Content-Type': 'application/json'
};

// URL
const baseUrl = 'http://localhost:8080/api/v1/posts';

// [2] - Clear cookie for logging out
function logout() {

    // We create an array of cookies by splitting the cookies.
    let cookies = document.cookie.split(";");

    // Then we iterate through the cookie to clear the cookie (I guess?)
    // Not familiar with cookies and how to manipulate them but I left the date as the instuctions.
    // I assume the old date is used to 'expire' the cookie because it has long since been passed.
    for (let i in cookies) {
        document.cookie = /^[^=]+/.exec(cookies[i])[0]+"=; expires = Thu, 01 Jan 1970 00:00:00 GMT"
    }
}

// [3] - Form routes

// How adding a post starts - grabs post text and starts via button press.
const submitPost = async (e) => {
    // Overrides a form's default actions.
    e.preventDefault();
    // We create a 'body' to send to the database to update it.
    let bodyObj = {

        // This grabs the DOM's textarea body
        postTitle: newBody.value
    }

    // Just following the instructions. I suppose we could have the entire function just written below if desired.
    await addPost(bodyObj);

    // This resets the textarea's body to be blank.
    // Also 'lets' the user know that the post was successfully created.
    newBody.value = '';
}

// Function that handles http request to add post.
async function addPost(obj) {
console.log(obj);
console.log(JSON.stringify(obj));
    // Http request
    const response = await fetch(`${baseUrl}/user/${userId}`, {
            method: "POST",
            body: JSON.stringify(obj),
            headers: headers
        })
        .catch(err => console.error(err.message));

    // If http request is ok (code 200), run the if statement.
    if (response.status === 200) {

        // Run another function to 'display' all our posts
        return getAllPosts();
    }

    // At the end, after getting our post to display on the page, this will let 'submitPost' function to complete itself.
}

// [4]-[1] - Retrieve all posts from user when page loads
async function getAllPosts() {
    await fetch(`${baseUrl}`, {
            method: "GET",
            headers: headers
        })
        // If the request is good, we complete the 'promises'.
        .then(response => response.json())
        .then(data => createPostCards(data))

        // Error handling.
        .catch(err => console.error(err));
}

// [4]-[2] - Create 'cards' for each post.
const createPostCards = (arr) => {
    // We clear the update post container first so we can add the posts.
    postContainer.innerHTML = '';
    arr.forEach(obj => {
        let card = document.createElement("div");
        card.classList.add("col");
        card.classList.add("col-sm-10");
        card.innerHTML = `
            <div class="card d-flex card-style">
                <div class="card-body d-flex flex-column justify-content-between card-size card-img-overlay" style="height: available">
                    <a class="card-text overflow-auto" href="${obj.postHtmlPath}">${obj.postTitle}</a>
                </div>
            </div>
        `
        let buttonCard = document.createElement("div");
        buttonCard.classList.add("d-flex");
        buttonCard.classList.add("stify-content-between");
        buttonCard.classList.add("col-sm-2");
        buttonCard.classList.add("padding-zero-override");
        buttonCard.innerHTML = `
            <button onclick="getPostById(${obj.id})" type="button" class="btn btn-primary col-xxl-6 margin-auto-override" data-bs-toggle="modal" data-bs-target="#post-edit-modal">Edit</button>
            <button class="btn btn-danger col-xxl-6 margin-buttonCard-override" onclick="handleDelete(${obj.id})">Delete</button>
        `
        postContainer.append(card);
        postContainer.append(buttonCard);
    })
}

// [4]-[3] - Append them to the HTML container
// 'Populates' our modal for us.
const populateModal = (obj) => {
    postBody.innerText = '';
    postBody.innerText = obj.body;
    updatePostBtn.setAttribute('data-post-id', obj.id);
}

// [5] - Update a post (GET request)
async function getPostById() {
    await fetch(`${baseUrl}/${postId}`, {
            method: "GET",
            headers: headers
        })
        .then(res => res.json())
        .then(data => populateModal(data))
        .catch(err => console.err(err.message))
}

async function handlePostEdit(postId) {
    let bodyObj = {
        id: postId,
        body: postBody.value
    }

    await fetch(baseUrl, {
        method: "PUT",
        body: JSON.stringify(bodyObj),
        headers: headers
        })
        .catch(err => console.error(err));

    return getAllPosts(userId);
}
// [6] - Delete a post
async function handleDelete(postId) {
    await fetch(`${baseUrl}/${postId}`, {
        method: "DELETE",
        headers: headers
        })
        .catch(err => console.error(err));

        return getAllPosts(userId);
}

// Prevents line breaks in our textarea
const noEnter = (e) => {
    if (e.keyCode === 13 && e.shiftKey) {
        e.preventDefault();
    } else if (e.keyCode === 13) {
        e.preventDefault();
    }
    console.log(e);
}

const youAreHere = () => {
    // Just basic shit until I have time to figure it out.
    whereAmILink.innerHTML = "/Home";
}

// DOM elements
const titleText = document.getElementById('title-text');

// HTML changes
async function displaySectionInfo() {
    await fetch(`http://localhost:8080/api/v1/sections`, {
        method: "GET",
        headers: headers
    })
    // If the request is good, we complete the 'promises'.
    .then(response => response.json())
    .then(data => {
        console.log(data);
    })

    // Error handling.
    .catch(err => console.error(err));
}

// Event listeners
postForm.addEventListener("submit", submitPost);
newBody.addEventListener("keypress", noEnter);
updatePostBtn.addEventListener("click", (e) => {
    let postId = e.target.getAttribute('data-post-id');
    handlePostEdit(postId);
})

// Instant runs
// displaySectionInfo();  --> something like this.
getAllPosts(userId);
youAreHere();