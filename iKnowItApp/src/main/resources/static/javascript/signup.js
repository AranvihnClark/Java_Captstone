// HTML Elements
const signupForm = document.getElementById('signup-form');
const signupUsername = document.getElementById('signup-username');
const signupPassword = document.getElementById('signup-password');
const signupNickname = document.getElementById('signup-nickname');
const signupEmailAddress = document.getElementById('signup-email-address');
const errorLog = document.getElementById('error-log');

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

    // We will encompass the rest of this in an if-statement to check for password length.
    if (signupPassword.value.length < 8) {
        errorLog.innerHTML = "Password must be greater than 8 characters in length.";
    } else {

        // Like how we did with Axios, we need a 'body' object to send with a POST request to update our database with.
        let bodyObj = {

            // 'username' is the name of the column in our database
            username: signupUsername.value,

            // 'password' is the name of the column in our database
            password: signupPassword.value,

            // The rest below is as above but for their respective table columns
            nickname: signupNickname.value,
            emailAddress: signupEmailAddress.value
        }

        // Not familiar with this but I assume this is JavaScript's default http request syntax.
        // fetch/catch is the equivalent to Axios' then/catch.
        const response = await fetch( `${baseUrl}/signup`, {

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

            // In case if there is a duplicate somewhere.
            if (responseArr.length > 1) {
                errorLog.innerHTML = responseArr[0];
            } else {
                // This replaces our current html page to a new html page that was set in our interface implementation.
                window.location.replace(responseArr[0]);
            }
        }
    }
}

// Event Listeners
signupForm.addEventListener("submit", submitForm);