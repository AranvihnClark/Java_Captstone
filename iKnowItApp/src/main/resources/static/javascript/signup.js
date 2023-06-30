// HTML Elements
const registrationForm = document.getElementById('register-form');
const registerUsername = document.getElementById('register-username');
const registerPassword = document.getElementById('register-password');

// Header
const headers = {
    'Content-Type': 'application/json'
};

// URL
const baseUrl = 'http://localhost:8080/api/v1/users';

// User submit button
const submitForm = async (e) => {

    // We override a form's default actions with the line below.
    e.preventDefault();

    // Like how we did with Axios, we need a 'body' object to send with a POST request to update our database with.
    let bodyObj = {

        // 'username' is the name of the column in our database
        username: registerUsername.value,

        // 'password' is the name of the column in our database
        password: registerPassword.value
    }

    // Not familiar with this but I assume this is JavaScript's default http request syntax.
    // fetch/catch is the equivalent to Axios' then/catch.
    const response = await fetch( `${baseUrl}/register`, {

        // We identify the type of http request we want with 'method:'
        method: "POST",

        // We turn our bodyObj variable into a JSON file using JSON.stringify
        body: JSON.stringify(bodyObj),

        // We use the header we set above.
        headers: headers
        })

        // Used to report errors if there are any/ensures the app doesn't crash if an error does occur by giving it something to do, I assume.
        .catch(err => console.error(err.message));

    // JS will wait until we get a response from the DB after sending it.
    const responseArr = await response.json();

    // If the response was ok (code 200), we will run the below code.
    if (response.status === 200) {

        // This replaces our current html page to a new html page that was set in our interface implementation.
        window.location.replace(responseArr[0]);
    }
}

// Event Listeners
registrationForm.addEventListener("submit", submitForm);