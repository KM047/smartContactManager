let currentTheme = getThemeFromLocalStorage();

document.addEventListener("DOMContentLoaded", () => {
    changeTheme();
});

function changeTheme() {
    document.querySelector("html").classList.add(currentTheme);

    const themeChangerBtn = document.getElementById("theme_changer");
    themeChangerBtn.addEventListener("click", () => {
        document.querySelector("html").classList.remove(currentTheme);

        currentTheme = currentTheme === "light" ? "dark" : "light";

        setThemeToLocalStorage(currentTheme);
        document.querySelector("html").classList.add(currentTheme);
        const span1 = document.getElementById("span1");
        const span2 = document.getElementById("span2");

        if (currentTheme === "dark") {
            span1.classList.add("hidden");
            span2.classList.remove("hidden");
        } else {
            span1.classList.remove("hidden");
            span2.classList.add("hidden");
        }
    });
}

// set theme to localStorage

function setThemeToLocalStorage(theme) {
    localStorage.setItem("theme", theme);
}

// get theme from the localStorage

function getThemeFromLocalStorage() {
    return localStorage.getItem("theme")
        ? localStorage.getItem("theme")
        : "light";
}
