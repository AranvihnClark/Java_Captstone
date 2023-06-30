// [1] - Cookie reader

// Separates the cookie into an array.
const cookieArr = document.cookie.split("=");

// Assigns our variable with the userId from the cookie.
const userId = cookieArr[1];

// DOM Elements (I guess it is more correct to say DOM as opposed to HTML like the instructions do. Left the other two js files as is.)
const noteForm = document.getElementById('note-form');
const noteContainer = document.getElementById('note-container');
const newBody = document.getElementById('note-body');

// Modal Elements
let noteBody = document.getElementById('update-note-body');
let updateNoteBtn = document.getElementById('update-note-button')

// Header
const headers = {
    'Content-Type': 'application/json'
};

// URL
const baseUrl = 'http://localhost:8080/api/v1/notes';

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

// How adding a note starts - grabs note text and starts via button press.
const submitNote = async (e) => {
    // Overrides a form's default actions.
    e.preventDefault();

    // We create a 'body' to send to the database to update it.
    let bodyObj = {

        // This grabs the DOM's textarea body
        body: newBody.value
    }

    // Just following the instructions. I suppose we could have the entire function just written below if desired.
    await addNote(bodyObj);

    // This resets the textarea's body to be blank.
    // Also 'lets' the user know that the note was successfully created.
    newBody.value = '';
}

// Function that handles http request to add note.
async function addNote(obj) {

    // Http request
    const response = await fetch(`${baseUrl}/user/${userId}`, {
            method: "POST",
            body: JSON.stringify(obj),
            headers: headers
        })
        .catch(err => console.error(err.message));

    // If http request is ok (code 200), run the if statement.
    if (response.status === 200) {

        // Run another function to 'display' all our notes
        return getNotes(userId);
    }

    // At the end, after getting our note to display on the page, this will let 'submitNote' function to complete itself.
}

// [4]-[1] - Retrieve all notes from user when page loads
async function getNotes(userId) {
    await fetch(`${baseUrl}/user/${userId}`, {
            method: "GET",
            headers: headers
        })
        // If the request is good, we complete the 'promises'.
        .then(response => response.json())
        .then(data => createNoteCards(data))

        // Error handling.
        .catch(err => console.error(err));
}

// [4]-[2] - Create 'cards' for each note.
const createNoteCards = (arr) => {
    // We clear the update note container first so we can add the notes.
    noteContainer.innerHTML = '';
    arr.forEach(obj => {
        let card = document.createElement("div");

        card.classList.add("m-2");
        card.classList.add("col");
        card.innerHTML = `
            <div class="card d-flex card-style">
                <img src="https://cdn.pixabay.com/photo/2017/02/01/19/53/leaves-2031234_1280.png" class="card-img" alt="card-frame">
                <div class="card-body d-flex flex-column justify-content-between card-size card-img-overlay" style="height: available">
                    <p class="card-text overflow-auto">${obj.body}</p>
                    <div class="d-flex justify-content-between">
                        <button onclick="getNoteById(${obj.id})" type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#note-edit-modal">Edit</button>
                        <button class="btn btn-danger" onclick="handleDelete(${obj.id})">Delete</button>
                    </div>
                </div>
            </div>
        `
        noteContainer.append(card);
    })
}

// [4]-[3] - Append them to the HTML container
// 'Populates' our modal for us.
const populateModal = (obj) => {
    noteBody.innerText = '';
    noteBody.innerText = obj.body;
    updateNoteBtn.setAttribute('data-note-id', obj.id);
}

// [5] - Update a note (GET request)
async function getNoteById(noteId) {
    await fetch(`${baseUrl}/${noteId}`, {
            method: "GET",
            headers: headers
        })
        .then(res => res.json())
        .then(data => populateModal(data))
        .catch(err => console.err(err.message))
}

async function handleNoteEdit(noteId) {
    let bodyObj = {
        id: noteId,
        body: noteBody.value
    }

    await fetch(baseUrl, {
        method: "PUT",
        body: JSON.stringify(bodyObj),
        headers: headers
        })
        .catch(err => console.error(err));

    return getNotes(userId);
}
// [6] - Delete a note
async function handleDelete(noteId) {
    await fetch(`${baseUrl}/${noteId}`, {
        method: "DELETE",
        headers: headers
        })
        .catch(err => console.error(err));

        return getNotes(userId);
}

// Event listeners
noteForm.addEventListener("submit", submitNote);
updateNoteBtn.addEventListener("click", (e) => {
    let noteId = e.target.getAttribute('data-note-id');
    handleNoteEdit(noteId);
})

// Instant runs
getNotes(userId);