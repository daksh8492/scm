console.log("Script Loaded")

// let currentTheme = getTheme();

// document.addEventListener("DOMContentLoaded",() => {
//     changeTheme();
// })

// function changeTheme(){
//     document.querySelector("html").classList.add(currentTheme);


//     const changeThemeButton = document.querySelector("#theme_change_button");

//     changeThemeButton.addEventListener("click",(event) => {
//         changeThemeButton.querySelector("span").textContent = currentTheme == "light" ? " Dark" : " Light";
//         const oldTheme = currentTheme ;
//         console.log("change theme button clicked");
//         if(currentTheme == "dark")
//             currentTheme = "light";
//         else
//             currentTheme = "dark";

//         document.querySelector("html").classList.remove(oldTheme);

//         document.querySelector("html").classList.add(currentTheme);
//     });



// }


// function setTheme(theme){
//     localStorage.setItem("theme",theme);
// }

// function getTheme(){
//     let theme = localStorage.getItem("theme");
//     return theme ? theme : "light";
// }




let currentTheme = getTheme();

document.addEventListener("DOMContentLoaded", () => {
    // Set the initial theme on page load
    changeTheme();
    updateButtonText();
});

function changeTheme() {
    document.querySelector("html").classList.add(currentTheme);

    const changeThemeButton = document.querySelector("#theme_change_button");

    changeThemeButton.addEventListener("click", (event) => {
        // Toggle between light and dark theme
        const oldTheme = currentTheme;
        currentTheme = currentTheme === "dark" ? "light" : "dark";

        document.querySelector("html").classList.remove(oldTheme);
        document.querySelector("html").classList.add(currentTheme);

        // Save the updated theme in localStorage
        setTheme(currentTheme);

        // Update button text after changing the theme
        updateButtonText();
    });
}

function updateButtonText() {
    const changeThemeButton = document.querySelector("#theme_change_button");
    changeThemeButton.querySelector("span").textContent = currentTheme === "light" ? " Dark" : " Light";
}

function setTheme(theme) {
    localStorage.setItem("theme", theme);
}

function getTheme() {
    let theme = localStorage.getItem("theme");
    return theme ? theme : "light";
}
