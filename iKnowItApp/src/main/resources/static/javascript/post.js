// We don't need to update posts or create posts.
// We just need to display the posts.

// [1] - Cookie reader
// Separates the cookie into an array.
const cookieArr = document.cookie.split(/[&=]/g);

// Assigns our variable with the userId from the cookie.
const userId = cookieArr[1];
const sectionId = cookieArr[3];
const postId = cookieArr[5];

// DOM Elements
const postTitleContainer = document.getElementById('post-title-container');
const postBodyContainer = document.getElementById('post-body-container');
const iAmHome = document.getElementById('i-am-home');
const iAmSection = document.getElementById('i-am-section');
const iAmPost = document.getElementById('i-am-post');
const postCreatorName = document.getElementById('post-creator-name');
const pageTitle = document.getElementById('page-title');
const titleText = document.getElementById('title-text');

// Comment DOM Elements
const addCommentHeader = document.getElementById('add-comment-header');
const commentForm = document.getElementById('add-comment-form')
const commentContainer = document.getElementById('comment-container');
const addCommentBody = document.getElementById('add-comment-body');
const updateCommentBtn = document.getElementById('update-comment-button');
const updateCommentBody = document.getElementById('update-comment-body');


// Post DOM Elements
const postDiv = document.getElementById('post-div');
const updatePostTitle = document.getElementById('update-post-title');
const updatePostBody = document.getElementById('update-post-body');
const updatePostBtn = document.getElementById('update-post-button');
const answeredPost = document.getElementById('answered-post');

// Header
const headers = {
    'Content-Type': 'application/json'
};

// URL
const baseUrl = 'http://localhost:8080/api/v1/comments';

// Global Variable
var answerExists = false;

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
            for (let i = 0; i < data.length; i++) {
                if (data[i].knewIt) {
                    answeredPost.innerHTML = 'Someone already knows this...';
                    answerExists = true;
                }
            }
            createCommentCards(data);
        })

        // Error handling.
        .catch(err => console.error(err));

}

const createCommentCards = (arr, numOfLikes) => {
    // We clear the update post container first so we can add the posts.
    commentContainer.innerHTML = '';
    console.log(arr);
    console.log(userId);
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
                    <p class="mb-2 question-text user-text-weight">${obj.userDto.username}</p>
                </div>
            `

            // The comment's body div
            let bodyCard = document.createElement("div");
            bodyCard.classList.add("col");
            bodyCard.classList.add("card");
            bodyCard.classList.add("d-flex");
            bodyCard.classList.add("card-bg");
            bodyCard.innerHTML = `
                <div class="card-body d-flex flex-column">
                    <p class="card-text question-color">${obj.commentBody}</p>
                </div>
            `

        // The delete/update comment div with like button.
            let buttonCard = document.createElement("div");
            buttonCard.classList.add("d-flex");
            buttonCard.classList.add("stify-content-between");
            buttonCard.classList.add("col-auto");
            buttonCard.classList.add("card-no-border");
            buttonCard.classList.add("text-center");
            buttonCard.classList.add("box");

            if (obj.knewIt === true) {
                buttonCard.innerHTML = `
                        <img src="../images/know_it_button.png" alt="profile-pic" class="knew-it-border">
                `;
            } else {
                buttonCard.innerHTML = `
                        <img src="../images/know_it_button.png" alt="profile-pic" class="know-it-border">
                `
                if (!obj.postDto.isAnswered) {
                buttonCard.innerHTML += `
                    <div>
                        <button id="update-comment-modal-button" class="col-auto btn btn-primary align-self-start" onclick="getCommentById(${obj.id})" type="button" data-bs-toggle="modal" data-bs-target="#comment-edit-modal">Edit</button>
                        <button id="delete-comment-modal-button" class="col-auto btn btn-danger align-self-start" onclick="handleCommentDelete(${obj.id})" type="button" data-bs-toggle="modal" data-bs-target="#delete-edit-modal">Delete</button>
                    </div>
                    `;
                }
            }

            /* [EXTRA]
            buttonCard.innerHTML += `
                <div class="text-center">
                    <p class="like-count">${obj.likes}</p>
                    <img src="../images/like_button.png" alt="profile-pic">
                    <p>Likes</p>
                </div>
            `;
            */

            card.append(userCard);
            card.append(bodyCard);
            card.append(buttonCard);
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
                    <p class="question-color user-text-weight">${obj.userDto.username}</p>
                </div>
            `
//                    <img src="../profileImages/template_profile_image.png" alt="profile-pic" class="mb-3">

            // The comment's body div
            let bodyCard = document.createElement("div");
            bodyCard.classList.add("col");
            bodyCard.classList.add("card");
            bodyCard.classList.add("d-flex");
            bodyCard.classList.add("card-bg");
            bodyCard.innerHTML = `
                <div class="card-body d-flex flex-column">
                    <p class="card-text question-color">${obj.commentBody}</p>
                </div>
            `

            // The like/unlike button
            let likeCard = document.createElement("div");
            likeCard.classList.add("col");
            likeCard.classList.add("col-auto");
            likeCard.classList.add("card-no-border");
            likeCard.classList.add("d-flex");
            likeCard.classList.add("stify-content-between");

            if (obj.knewIt === false && obj.postDto.userDto.id == userId) {
                likeCard.innerHTML = `
                    <div class="text-center">
                        <button type="button" onclick="theyKnewIt(${obj.id})" class="btn">
                            <img src="../images/know_it_button.png" alt="profile-pic" class="know-it-border">
                        </button>
                        <p>Best Advice?</p>
                    </div>
                `;
            } else if (obj.knewIt === true) {
                likeCard.innerHTML = `
                    <img src="../images/know_it_button.png" alt="profile-pic" class="knew-it-border">
                `;
            } else {
                likeCard.innerHTML = `
                    <img src="../images/know_it_button.png" alt="profile-pic" class="know-it-border">
                `;
            }
            /* [EXTRA]
            likeCard.innerHTML += `
                    <hr>
                    <div class="text-center">
                        <p class="like-count">${obj.likes}</p>
                        <img src="../images/like_button.png" alt="profile-pic">
                        <p>Likes</p>
                    </div>
            `;
            */
            card.append(userCard);
            card.append(bodyCard);
            card.append(likeCard);
            commentContainer.append(card);
        }
    })
}

// 'Populates' our modal for us.
const populateCommentModal = (obj) => {
    updateCommentBody.innerText = '';
    updateCommentBody.innerText = obj.commentBody;
    updateCommentBtn.setAttribute('data-comment-id', obj.id);
}

async function getCommentById(commentId) {
    await fetch(`${baseUrl}/${commentId}`, {
            method: "GET",
            headers: headers
        })
        .then(res => res.json())
        .then(data => populateCommentModal(data))
        .catch(err => console.err(err.message))
}

async function handleCommentEdit(commentId) {
console.log("Here I am!");
    let bodyObj = {
        id: commentId,
        commentBody: updateCommentBody.value
    }

    await fetch(baseUrl, {
        method: "PUT",
        body: JSON.stringify(bodyObj),
        headers: headers
        })
        .catch(err => console.error(err));

    return getAllPostComments(postId);
}

// Delete a comment
async function handleCommentDelete(commentId) {
    await fetch(`${baseUrl}/${commentId}`, {
        method: "DELETE",
        headers: headers
        })
        .catch(err => console.error(err));

        return getAllPostComments(postId);
}

const confirmCommentUpdate = (e) => {
    let id = e.target.getAttribute('data-comment-id');
    handleCommentEdit(id);
}

//===================
// 'Populates' our modal for us for the post's body.
const populatePostModal = (obj) => {
    updatePostBody.innerText = '';
    updatePostTitle.innerText = '';
    updatePostBody.innerText = obj.postBody;
    updatePostTitle.innerText = obj.postTitle;

    updatePostBtn.setAttribute('data-post-id', obj.id);
}

async function getPostById(postId) {
    await fetch(`${baseUrl}/comment-post/${postId}`, {
            method: "GET",
            headers: headers
        })
        .then(res => res.json())
        .then(data => populatePostModal(data))
        .catch(err => console.err(err.message))
}

async function handlePostEdit(postId) {
    let bodyObj = {
        id: postId,
        postTitle: updatePostTitle.value,
        postBody: updatePostBody.value
    }

    await fetch(`${baseUrl}/post`, {
        method: "PUT",
        body: JSON.stringify(bodyObj),
        headers: headers
        })
        .catch(err => console.error(err));

    return displayPostInfo(postId);
}

const confirmPostUpdate = (e) => {
    let id = e.target.getAttribute('data-post-id');
    handlePostEdit(id);
}

//====================
// Prevents line breaks in our textarea
const noEnter = (e) => {
    if (e.keyCode === 13 && e.shiftKey) {
        e.preventDefault();
    } else if (e.keyCode === 13) {
        e.preventDefault();
    }
    console.log(e);
}

const youAreHere = (obj) => {
    // Just basic shit until I have time to figure it out.
    iAmSection.href = obj.sectionDto.sectionHtmlName;
    iAmSection.innerHTML = `${obj.sectionDto.sectionTitle}`;

    iAmPost.href = obj.postHtmlName;
    iAmPost.innerHTML = `${obj.postTitle}`;
}

// HTML changes
async function displayPostInfo(postId) {
    await fetch(`${baseUrl}/comment-post/${postId}`, {
        method: "GET",
        headers: headers
    })
    .then(res => res.json())
    .then(data => {
        youAreHere(data);
        titleText.innerHTML = `I Know About ${data.sectionDto.sectionTitle}`;
        postCreatorName.innerHTML = `${data.userDto.nickname}`;
        pageTitle.innerHTML = `${data.sectionDto.sectionTitle}`;
        postTitleContainer.innerHTML = `${data.postTitle}`;

        if (data.isAnswered) {
            addCommentHeader.classList.add("d-none");
            commentForm.classList.add("d-none");
        }

        // In case if no body was entered
        if (data.postBody === "") {
            postBodyContainer.innerHTML = `${data.userDto.username} was too lazy to talk about their situation.`;
        } else {
            postBodyContainer.innerHTML = `${data.postBody}`;
        }
    })

    // Error handling.
    .catch(err => console.error(err));
}

async function displayExtraItems() {
    await fetch(`${baseUrl}/comment-post/${postId}`, {
            method: "GET",
            headers: headers
        })
        .then(res => res.json())
        .then(data => {
            if (data.userDto.id == userId) {
                postDiv.innerHTML += `
                    <button id="update-title-modal-button" class="col-auto btn btn-primary align-self-start" onclick="getPostById(${postId})" type="button" data-bs-toggle="modal" data-bs-target="#post-edit-modal">Update?</button>
                `;

                addCommentHeader.classList.add("d-none");
                commentForm.classList.add("d-none");
            }
        })

        // Error handling.
        .catch(err => console.error(err));
}

async function theyKnewIt(commentId) {
    let bodyObj = {
        id: commentId,
        knewIt: true,
    }

    await fetch(`${baseUrl}/comment/status`, {
        method: "PUT",
        body: JSON.stringify(bodyObj),
        headers: headers
        })
        .catch(err => console.error(err));

    return getAllPostComments(postId);
}

// Needed to remove section from cookie
const revertToHomeCookie = async () => {
    let newCookie = document.cookie;
    newCookie = newCookie.split(/[&]/g);
    document.cookie = newCookie[0];
}

// Needed to remove post from cookie
const revertToSectionCookie = async () => {
    let newCookie = document.cookie;
    newCookie = newCookie.split(/[&]/g);
    document.cookie = newCookie[0] + "&" + newCookie[1];
}

// Event listeners
commentForm.addEventListener("submit", submitPost);
//updatePostTitle.addEventListener("keypress", noEnter);

updateCommentBtn.addEventListener("click", confirmCommentUpdate);
updatePostBtn.addEventListener("click", confirmPostUpdate);

iAmHome.addEventListener("click", revertToHomeCookie);
iAmSection.addEventListener("click", revertToSectionCookie);

// Instant runs
displayPostInfo(postId);
getAllPostComments(postId);
displayExtraItems();