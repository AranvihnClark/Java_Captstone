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
const commentForm = document.getElementById('comment-form')
const commentContainer = document.getElementById('comment-container');
const whereAmILink = document.getElementById('where-am-i');
const addCommentBtn = document.getElementById('add-comment-button');
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
const baseUrl = 'http://localhost:8080/api/v1/posts';
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
        commentBody: updateCommentBody.value,
        user: userId,
        post: postId
    }

    // Just following the instructions. I suppose we could have the entire function just written below if desired.
    await addComment(bodyObj);

    // This resets the textarea's body to be blank.
    // Also 'lets' the user know that the post was successfully created.
    updateCommentBody.value = '';
}

// Function that handles http request to add post.
async function addComment(obj) {
    // Http request
    const response = await fetch(`${baseUrl}/users/${userId}`, {
            method: "POST",
            body: JSON.stringify(obj),
            headers: headers
        })
        .catch(err => console.error(err.message));

    // If http request is ok (code 200), run the if statement.
    if (response.status === 200) {

        // Run another function to 'display' all our posts
        return getAllSectionPosts(sectionId);
    }

    // At the end, after getting our post to display on the page, this will let 'submitPost' function to complete itself.
}

// [4]-[1] - Retrieve all posts from user when page loads
async function getAllSectionPosts(sectionId) {
    await fetch(`${baseUrl}/sections/${sectionId}`, {
            method: "GET",
            headers: headers
        })
        // If the request is good, we complete the 'promises'.
        .then(response => response.json())
        .then(data => {
            createPostAnsweredCards(data);
        })

        // Error handling.
        .catch(err => console.error(err));
}

const createPostAnsweredCards = (arr) => {
    // We clear the update post container first so we can add the posts.
    postAnsweredContainer.innerHTML = '';
    arr.forEach(obj => {
        if (obj.userId == userId) {
            let card = document.createElement("div");
            card.classList.add("col");
            card.classList.add("col-sm-11");
            card.innerHTML = `
                <div class="card d-flex card-style">
                    <div class="card-body d-flex flex-column justify-content-between card-size " style="height: available">
                        <a class="card-text overflow-auto link" href="${obj.postHtmlName}">${obj.postTitle}</a>
                    </div>
                </div>
            `
            let buttonCard = document.createElement("div");
            buttonCard.classList.add("d-flex");
            buttonCard.classList.add("stify-content-between");
            buttonCard.classList.add("col-sm-1");
            buttonCard.classList.add("padding-zero-override");
            buttonCard.innerHTML = `
                <button class="btn btn-danger col-xxl-6 margin-buttonCard-override" onclick="handleDelete(${obj.id})">Delete</button>
            `
            postAnsweredContainer.append(card);
            postAnsweredContainer.append(buttonCard);
        } else if (obj.userId !== userId) {
            let card = document.createElement("div");
            card.classList.add("col");
            card.classList.add("col-sm-12");
            card.innerHTML = `
                <div class="card d-flex card-style">
                    <div class="card-body d-flex flex-column justify-content-between card-size " style="height: available">
                        <a class="card-text overflow-auto link" href="${obj.postHtmlName}">${obj.postTitle}</a>
                    </div>
                </div>
            `
            postAnsweredContainer.append(card);
        }
    })
}

// [4]-[3] - Append them to the HTML container
// 'Populates' our modal for us.
const populateModal = () => {
    postBody.innerText = '';
    postTitle.innerText = '';
}

// [5] - Update a post (GET request)
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

        return getAllSectionPosts(sectionId);
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
async function displayPostInfo(sectionId) {
    await fetch(`${baseUrl}/post-section/${sectionId}`, {
        method: "GET",
        headers: headers
    })
    .then(res => res.json())
    .then(data => {
        console.log(data);
        titleText.innerHTML = `I Know About ${data.sectionTitle}`;
        sectionCreatorName.innerHTML = `${data.userName}`;
        pageTitle.innerHTML = `${data.sectionTitle}`;
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
postForm.addEventListener("submit", submitPost);
postTitle.addEventListener("keypress", noEnter);
addCommentBtn.addEventListener("click", addComment());
whereAmILink.addEventListener("click", revertCookie);

// Instant runs
displayPostInfo(sectionId);
getAllSectionPosts(sectionId);
youAreHere();