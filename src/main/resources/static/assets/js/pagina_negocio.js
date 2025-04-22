function openTab(evt, tabName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    tablinks = document.getElementsByTagName("button");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].classList.remove("text-[#27AE60]", "border-[#27AE60]", "py-2", "transition-all", "duration-300", "ease-in-out", "border-b-2", "border-transparent", "hover:border-[#27AE60]", "hover:text-[#27AE60]", "focus:outline-none", "focus:border-[#27AE60]");
    }

    document.getElementById(tabName).style.display = "block";

    evt.currentTarget.classList.add("py-2", "transition-all", "duration-300", "ease-in-out", "border-b-2", "border-[#27AE60]", "text-[#27AE60]", "hover:border-[#27AE60]", "hover:text-[#27AE60]", "focus:outline-none", "focus:border-[#27AE60]");
}

document.getElementsByClassName("tabcontent")[0].style.display = "block";
document.querySelector("button").classList.add("py-2", "transition-all", "duration-300", "ease-in-out", "border-b-2", "border-[#27AE60]", "text-[#27AE60]", "hover:border-[#27AE60]", "hover:text-[#27AE60]", "focus:outline-none", "focus:border-[#27AE60]");