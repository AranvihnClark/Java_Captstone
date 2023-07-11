// We don't need to update posts or create posts.
// We just need to display the posts.

// [1] - Cookie reader
// Separates the cookie into an array.
const cookieArr = document.cookie.split(/[&=]/g);

// Assigns our variable with the userId from the cookie.
const userId = cookieArr[1];
const sectionId = cookieArr[3];
const postId = cookieArr[5];
console.log("Cookie User ID: " + userId);
console.log("Cookie Section ID: " + sectionId);
console.log("Cookie Post ID: " + postId);

// DOM Elements (I guess it is more correct to say DOM as opposed to HTML like the instructions do. Left the other two js files as is.)
const commentForm = document.getElementById('add-comment-form')
const commentContainer = document.getElementById('comment-container');
const postTitleContainer = document.getElementById('post-title-container');
const postBodyContainer = document.getElementById('post-body-container');
const whereAmILink = document.getElementById('where-am-i');
const addCommentBody = document.getElementById('add-comment-body');
//const addCommentBtn = document.getElementById('add-comment-button');
const updatePostTitleBtn = document.getElementById('update-post-title-button');
const updatePostBodyBtn = document.getElementById('update-post-body-button');
const updateCommentBody = document.getElementById('update-comment-body');
const updatePostTitle = document.getElementById('update-post-title');
const updatePostBody = document.getElementById('update-post-body');
const postCreatorName = document.getElementById('post-creator-name');
const pageTitle = document.getElementById('page-title');
const titleText = document.getElementById('title-text');

// Header
const headers = {
    'Content-Type': 'application/json'
};

// URL
const baseUrl = 'http://localhost:8080/api/v1/comments';
// [2] - Clear cookie for logging out
function logout() {

    // We create an array of cookies by splitting the cookies.
    let cookies = document.cookie.split(";");

    // Then we iterate through the cookie to clear the cookie (I guess?)
    // Not familiar with cookies and how to manipulate them but I left the date as the instruction indicated.
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
        commentBody: addCommentBody.value,
        user: userId,
        post: postId
    }

    // Just following the instructions. I suppose we could have the entire function just written below if desired.
    console.log(bodyObj);
    await addComment(bodyObj);

    // This resets the textarea's body to be blank.
    // Also 'lets' the user know that the post was successfully created.
    addCommentBody.value = '';
}

// Function that handles http request to add post.
async function addComment(obj) {
    // Http request
    const response = await fetch(`${baseUrl}/post/${postId}/user/${userId}`, {
            method: "POST",
            body: JSON.stringify(obj),
            headers: headers
        })
        .catch(err => console.error(err.message));

    // If http request is ok (code 200), run the if statement.
    if (response.status === 200) {

        // Run another function to 'display' all our posts
        return getAllPostComments(postId);
    }

    // At the end, after getting our post to display on the page, this will let 'submitPost' function to complete itself.
}

// [4]-[1] - Retrieve all posts from user when page loads
async function getAllPostComments(postId) {
    await fetch(`${baseUrl}/posts/${postId}`, {
            method: "GET",
            headers: headers
        })
        // If the request is good, we complete the 'promises'.
        .then(response => response.json())
        .then(data => {
            createCommentCards(data);
        })

        // Error handling.
        .catch(err => console.error(err));
}

const createCommentCards = (arr) => {
    // We clear the update post container first so we can add the posts.
    commentContainer.innerHTML = '';
    console.log(arr);
    arr.forEach(obj => {
        if (obj.userDto.id == userId) {
            // The overarching 'div'
            let card = document.createElement("div");
            card.classList.add("row");
            card.classList.add("mb-3");

            // The user's card div
            let userCard = document.createElement("div");
            userCard.classList.add("col");
            userCard.classList.add("col-sm-2");
            userCard.classList.add("user-card");
            userCard.classList.add("d-flex");
            userCard.classList.add("flex-column");
            userCard.innerHTML = `
                <div class="d-flex flex-column user-name-position">
                    <p>${obj.userDto.username}</p>
                </div>
            `

            // The comment's body div
            let bodyCard = document.createElement("div");
            bodyCard.classList.add("col");
            bodyCard.classList.add("card");
            bodyCard.classList.add("d-flex");
            bodyCard.innerHTML = `
                <div class="card-body d-flex flex-column">
                    <p class="card-text">${obj.commentBody}</p>
                </div>
            `

            // The delete/update comment div.
            let buttonCard = document.createElement("div");
            buttonCard.classList.add("d-flex");
            buttonCard.classList.add("stify-content-between");
            buttonCard.classList.add("col-auto");
            buttonCard.classList.add("padding-zero-override");
            buttonCard.innerHTML = `
                <button class="btn btn-primary margin-buttonCard-override" onclick="handleDelete(${obj.id})">Edit</button>
                <button class="btn btn-danger margin-buttonCard-override" onclick="handleDelete(${obj.id})">Delete</button>
            `
            card.append(userCard)
            card.append(bodyCard)
            card.append(buttonCard)
            commentContainer.append(card);
        } else if (obj.userDto.id !== userId) {
            // The overarching 'div'
            let card = document.createElement("div");
            card.classList.add("row");
            card.classList.add("mb-3");

            // The user's card div
            let userCard = document.createElement("div");
            userCard.classList.add("col");
            userCard.classList.add("col-sm-2");
            userCard.classList.add("user-card");
            userCard.classList.add("d-flex");
            userCard.classList.add("flex-column");
            userCard.innerHTML = `
                <div class="d-flex flex-column user-name-position">
                    <p>${obj.userDto.username}</p>
                </div>
            `

            // The comment's body div
            let bodyCard = document.createElement("div");
            bodyCard.classList.add("col");
            bodyCard.classList.add("card");
            bodyCard.classList.add("d-flex");
            bodyCard.innerHTML = `
                <div class="card-body d-flex flex-column">
                    <p class="card-text">${obj.commentBody}</p>
                </div>
            `

            card.append(userCard)
            card.append(bodyCard)
            commentContainer.append(card);
        }
    })
}

// [4]-[3] - Append them to the HTML container
// 'Populates' our modal for us.
const populateModal = () => {
    updatePostBody.innerText = '';
    updatePostTitle.innerText = '';
    updateCommentBody.innerText = '';
}

// [5] - Update a comment (GET request)
async function getPostById(postId) {
    await fetch(`${baseUrl}/${postId}`, {
            method: "GET",
            headers: headers
        })
        .then(res => res.json())
        .then(data => populateModal(data))
        .catch(err => console.err(err.message))
}

// [6] - Delete a post
async function handleDelete(postId) {
    await fetch(`${baseUrl}/${postId}`, {
        method: "DELETE",
        headers: headers
        })
        .catch(err => console.error(err));

        return getAllPostComments(sectionId);
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
//    displayPostInfo(sectionId);
}

// HTML changes
async function displayPostInfo(postId) {
    await fetch(`${baseUrl}/comment-post/${postId}`, {
        method: "GET",
        headers: headers
    })
    .then(res => res.json())
    .then(data => {
        titleText.innerHTML = `I Know About ${data.sectionDto.sectionTitle}`;
        postCreatorName.innerHTML = `${data.userDto.username}`;
        pageTitle.innerHTML = `${data.sectionDto.sectionTitle}`;
        postTitleContainer.innerHTML = `${data.postTitle}`;
        postBodyContainer.innerHTML = `${data.postBody}`;
    })

    // Error handling.
    .catch(err => console.error(err));

}

// Needed to remove section from cookie
const revertCookie = async () => {
    let newCookie = document.cookie;
    newCookie = newCookie.split(/[&]/g);
    document.cookie = newCookie[0];
}

// Event listeners
commentForm.addEventListener("submit", submitPost);
updatePostTitle.addEventListener("keypress", noEnter);
//addCommentBtn.addEventListener("submit", addComment());
whereAmILink.addEventListener("click", revertCookie);

// Instant runs
displayPostInfo(postId);
getAllPostComments(postId);
youAreHere();