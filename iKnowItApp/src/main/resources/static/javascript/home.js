// [1] - Cookie reader

// Separates the cookie into an array.
const cookieArr = document.cookie.split("=");
console.log(cookieArr);
// Assigns our variable with the userId from the cookie.
const userId = cookieArr[1];

// DOM Elements (I guess it is more correct to say DOM as opposed to HTML like the instructions do. Left the other two js files as is.)
const sectionForm = document.getElementById('section-form');
const sectionContainer = document.getElementById('section-container');
const newBody = document.getElementById('section-title');
const whereAmILink = document.getElementById('where-am-i');

// Modal Elements
let sectionTitle = document.getElementById('update-section-title');
let updateSectionBtn = document.getElementById('update-section-button')

// Header
const headers = {
    'Content-Type': 'application/json'
};

// URL
const baseUrl = 'http://localhost:8080/api/v1/sections';

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

// How adding a section starts - grabs section text and starts via button press.
const submitSection = async (e) => {
    // Overrides a form's default actions.
    e.preventDefault();
    // We create a 'body' to send to the database to update it.
    let bodyObj = {

        // This grabs the DOM's textarea body
        sectionTitle: newBody.value
    }

    // Just following the instructions. I suppose we could have the entire function just written below if desired.
    await addSection(bodyObj);

    // This resets the textarea's body to be blank.
    // Also 'lets' the user know that the section was successfully created.
    newBody.value = '';
}

// Function that handles http request to add section.
async function addSection(obj) {
    // Http request
    const response = await fetch(`${baseUrl}/user/${userId}`, {
            method: "POST",
            body: JSON.stringify(obj),
            headers: headers
        })
        .catch(err => console.error(err.message));

    // If http request is ok (code 200), run the if statement.
    if (response.status === 200) {

        // Run another function to 'display' all our sections
        return getSections();
    }

    // At the end, after getting our section to display on the page, this will let 'submitSection' function to complete itself.
}

// [4]-[1] - Retrieve all sections from user when page loads
async function getSections() {
    await fetch(`${baseUrl}`, {
            method: "GET",
            headers: headers
        })
        // If the request is good, we complete the 'promises'.
        .then(response => response.json())
        .then(data => createSectionCards(data))

        // Error handling.
        .catch(err => console.error(err));
}

// [4]-[2] - Create 'cards' for each section.
const createSectionCards = (arr) => {
    // We clear the update section container first so we can add the sections.
    sectionContainer.innerHTML = '';

    console.log(arr);

    arr.forEach(obj => {
        if (obj.userId == userId) {
            let card = document.createElement("div");
            card.classList.add("col");
            card.classList.add("col-sm-10");
            card.innerHTML = `
                <div class="card d-flex card-style">
                    <div class="card-body d-flex flex-column justify-content-between card-size card-img-overlay" style="height: available">
                        <a class="card-text overflow-auto link" onclick="getToSection(${obj.id})">${obj.sectionTitle}</a>
                    </div>
                </div>
            `// href="./${obj.sectionHtmlName}"
            let buttonCard = document.createElement("div");
            buttonCard.classList.add("d-flex");
            buttonCard.classList.add("stify-content-between");
            buttonCard.classList.add("col-sm-2");
            buttonCard.classList.add("padding-zero-override");
            buttonCard.innerHTML = `
                <button onclick="getSectionById(${obj.id})" type="button" class="btn btn-primary col-xxl-6 margin-buttonCard-override" data-bs-toggle="modal" data-bs-target="#section-edit-modal">Edit</button>
                <button class="btn btn-danger col-xxl-6 margin-buttonCard-override" onclick="handleDelete(${obj.id})">Delete</button>
            `
            sectionContainer.append(card);
            sectionContainer.append(buttonCard);
        } else if (obj.userId != userId) {
            let card = document.createElement("div");
            card.classList.add("col");
            card.classList.add("col-sm-12");
            card.innerHTML = `
                <div class="card d-flex card-style">
                    <div class="card-body d-flex flex-column justify-content-between card-size card-img-overlay" style="height: available">
                        <a class="card-text overflow-auto link" onclick="getToSection(${obj.id})">${obj.sectionTitle}</a>
                    </div>
                </div>
            `
            sectionContainer.append(card);
        }

    })
}

// [4]-[3] - Append them to the HTML container
// 'Populates' our modal for us.
const populateModal = (obj) => {
    sectionTitle.innerText = '';
    sectionTitle.innerText = obj.sectionTitle;
    updateSectionBtn.setAttribute('data-section-id', obj.id);
}

// [5] - Update a section (GET request)
async function getSectionById(sectionId) {
    await fetch(`${baseUrl}/${sectionId}`, {
            method: "GET",
            headers: headers
        })
        .then(res => res.json())
        .then(data => populateModal(data))
        .catch(err => console.err(err.message))
}

async function handleSectionEdit(sectionId) {
    let bodyObj = {
        id: sectionId,
        sectionTitle: sectionTitle.value
    }

    await fetch(baseUrl, {
        method: "PUT",
        body: JSON.stringify(bodyObj),
        headers: headers
        })
        .catch(err => console.error(err));

    return getSections();
}
// [6] - Delete a section
async function handleDelete(sectionId) {
    await fetch(`${baseUrl}/${sectionId}`, {
        method: "DELETE",
        headers: headers
        })
        .catch(err => console.error(err));

        return getSections();
}

// Going to the a section
async function getToSection(sectionId) {
    
    const response = await fetch( `${baseUrl}/${sectionId}`, {
        // We identify the type of http request we want with 'method:'
        method: "POST",
        
        // We use the header we set above.
        headers: headers
    })
    .catch(err => console.err(err));
    
    const responseArr = await response.json();

    console.log("This is a response: " + responseArr[0]);
    console.log("This is a second response: " + responseArr[1]);
    console.log("We made it: " + response);

    if (response.status === 200) {
        document.cookie += `&sectionId=${responseArr[1]}`
        window.location.replace(responseArr[0]);
    }
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


// Event listeners
sectionForm.addEventListener("submit", submitSection);
newBody.addEventListener("keypress", noEnter);
updateSectionBtn.addEventListener("click", (e) => {
    let sectionId = e.target.getAttribute('data-section-id');
    handleSectionEdit(sectionId);
})

// Instant runs
getSections();
youAreHere();