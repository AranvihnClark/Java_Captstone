// We don't need to update posts or create posts.
// We just need to display the posts.

// [1] - Cookie reader
// Separates the cookie into an array.
const cookieArr = document.cookie.split(/[&=]/g);

// Assigns our variable with the userId from the cookie.
const userId = cookieArr[1];
const sectionId = cookieArr[3];

// DOM Elements (I guess it is more correct to say DOM as opposed to HTML like the instructions do. Left the other two js files as is.)
const iAmSection = document.getElementById('i-am-section');
const iAmHome = document.getElementById('i-am-home');

// Post DOM Elements
const postQuestionContainer = document.getElementById('post-question-container');
const postForm = document.getElementById('post-form')
const postAnsweredContainer = document.getElementById('post-answer-container');
const addPostBtn = document.getElementById('add-post-button')
const postTitle = document.getElementById('post-title');
const postBody = document.getElementById('post-body');

// Section DOM Elements
const sectionCreatorName = document.getElementById('section-creator-name');
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
        postTitle: postTitle.value,
        postBody: postBody.value,
        isAnswered: "false",
        user: userId,
        section: sectionId
    }

    // Just following the instructions. I suppose we could have the entire function just written below if desired.
    await addPost(bodyObj);

    // This resets the textarea's body to be blank.
    // Also 'lets' the user know that the post was successfully created.
    postTitle.value = '';
    postBody.value = '';
}

// Function that handles http request to add post.
async function addPost(obj) {
    // Http request
    const response = await fetch(`${baseUrl}/sections/${sectionId}/users/${userId}`, {
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
        let answeredObjects = [];
            for (let i = 0; i < data.length; i++) {
                if (data[i].isAnswered) {
                    answeredObjects.push(data.splice(i, 1).pop());
                }
            }
            createPostQuestionCards(data);
            createPostAnsweredCards(answeredObjects);
        })

        // Error handling.
        .catch(err => console.error(err));
}

// [4]-[2] - Create 'cards' for each post.
const createPostQuestionCards = (arr) => {
    // We clear the update post container first so we can add the posts.
    postQuestionContainer.innerHTML = '';

    arr.forEach(obj => {
        if (obj.userDto.id == userId) {
            let card = document.createElement("div");
            card.classList.add("col");
            card.classList.add("col-sm-11");
//            card.classList.add("d-flex");
            card.innerHTML = `
                <div class="card d-flex card-style">
                    <div class="card-body d-flex flex-column justify-content-between card-size card-style" style="height: available">
                        <a class="card-text overflow-auto link" onclick="getToPost(${obj.id})">${obj.postTitle}</a>
                    </div>
                </div>
            `
            let buttonCard = document.createElement("div");
//            buttonCard.classList.add("d-flex");
            buttonCard.classList.add("stify-content-between");
            buttonCard.classList.add("col");
            buttonCard.classList.add("col-sm-1");
            buttonCard.classList.add("padding-zero-override");
            buttonCard.innerHTML = `
                <button class="btn btn-danger margin-buttonCard-override" onclick="handleDelete(${obj.id})">Delete</button>
            `
            postQuestionContainer.append(card);
            postQuestionContainer.append(buttonCard);
        } else if (obj.userDto.id !== userId) {
            let card = document.createElement("div");
            card.classList.add("col");
            card.classList.add("col-sm-12");
//            card.classList.add("d-flex");
            card.innerHTML = `
                <div class="card d-flex card-style">
                    <div class="card-body d-flex flex-column justify-content-between card-size " style="height: available">
                        <a class="card-text overflow-auto link" onclick="getToPost(${obj.id})">${obj.postTitle}</a>
                    </div>
                </div>
            `
            postQuestionContainer.append(card);
        }
    })
}

const createPostAnsweredCards = (arr) => {
    // We clear the update post container first so we can add the posts.
    postAnsweredContainer.innerHTML = '';
    arr.forEach(obj => {
        if (obj.userDto.id == userId) {
            let card = document.createElement("div");
            card.classList.add("col");
            card.classList.add("col-sm-11");
            card.innerHTML = `
                <div class="card d-flex card-style">
                    <div class="card-body d-flex flex-column justify-content-between card-size " style="height: available">
                        <a class="card-text overflow-auto link" onclick="getToPost(${obj.id})">${obj.postTitle}</a>
                    </div>
                </div>
            `
            let buttonCard = document.createElement("div");
            buttonCard.classList.add("d-flex");
            buttonCard.classList.add("stify-content-between");
            buttonCard.classList.add("col-auto");
            buttonCard.classList.add("padding-zero-override");
            buttonCard.innerHTML = `
                <button class="btn btn-danger margin-buttonCard-override" onclick="handleDelete(${obj.id})">Delete</button>
            `
            postAnsweredContainer.append(card);
            postAnsweredContainer.append(buttonCard);
        } else if (obj.userDto.id !== userId) {
            let card = document.createElement("div");
            card.classList.add("col");
            card.classList.add("col-sm-12");
            card.innerHTML = `
                <div class="card d-flex card-style">
                    <div class="card-body d-flex flex-column justify-content-between card-size " style="height: available">
                        <a class="card-text overflow-auto link" onclick="getToPost(${obj.id})">${obj.postTitle}</a>
                    </div>
                </div>
            `
            postAnsweredContainer.append(card);
        }
    })
}

// Going to a Post
async function getToPost(postId) {

    const response = await fetch( `${baseUrl}/${postId}`, {
        // We identify the type of http request we want with 'method:'
        method: "POST",

        // We use the header we set above.
        headers: headers
    })
    .catch(err => console.err(err));

    const responseArr = await response.json();

    if (response.status === 200) {
        document.cookie += `&postId=${responseArr[1]}`
        window.location.replace(responseArr[0]);
    }
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

const youAreHere = (obj) => {
    // Just basic shit until I have time to figure it out.
    iAmSection.href = obj.sectionHtmlName;
    iAmSection.innerHTML = `${obj.sectionTitle}`;
}

// HTML changes
async function displaySectionInfo(sectionId) {
    await fetch(`${baseUrl}/post-section/${sectionId}`, {
        method: "GET",
        headers: headers
    })
    .then(res => res.json())
    .then(data => {
        youAreHere(data);
        titleText.innerHTML = `I Know About ${data.sectionTitle}`;
        sectionCreatorName.innerHTML = `${data.userDto.nickname}`;
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
addPostBtn.addEventListener("click", addPost());
iAmHome.addEventListener("click", revertCookie);

// Instant runs
displaySectionInfo(sectionId);
getAllSectionPosts(sectionId);