// HTML Elements
const loginForm = document.getElementById('login-form');
const loginUsername = document.getElementById('login-username');
const loginPassword = document.getElementById('login-password');

// Header
const headers = {
    'Content-Type': 'application/json'
};

// URL
const baseUrl = 'http://localhost:8080/api/v1/users';

// User login button
const login = async (e) => {
    // We override a form's default actions with the line below.
    e.preventDefault();

    // Like how we did with Axios, we need a 'body' object to send with a POST request to update our database with.
    let bodyObj = {

        // 'username' is the name of the column in our database
        username: loginUsername.value,

        // 'password' is the name of the column in our database
        password: loginPassword.value
    }

    // fetch/catch is the equivalent to Axios' then/catch.
    const response = await fetch( `${baseUrl}/login`, {
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

        // This stores our user's id in a cookie, I think.
        document.cookie = `userId=${responseArr[1]}`;

        // This replaces our current html page to a new html page that was set in our interface implementation.
        window.location.replace(responseArr[0]);
    }
}

// Event Listeners
loginForm.addEventListener("submit", login);