window.onload = function () {
  let registerButton = document.getElementById("registerButton");
  registerButton.onclick = tryRegister;

  let inputPasswordBox = document.getElementById("passwordInput1");
  let inputRepeatPasswordBox = document.getElementById("passwordInput2");

  inputPasswordBox.oninput = ComparePasswords;
  inputRepeatPasswordBox.oninput = ComparePasswords;
  inputPasswordBox.addEventListener("focus", passDropdown);
  inputRepeatPasswordBox.addEventListener("focus", passDropdown);
  inputPasswordBox.addEventListener("focusout", erasePassDropdown);
  inputRepeatPasswordBox.addEventListener("focusout", erasePassDropdown);

  let usernameBox = document.getElementById("username");
  usernameBox.addEventListener("input", checkUsername);
  usernameBox.addEventListener("focus", userDropdown);
  usernameBox.addEventListener("focusout", eraseUserDropdown);

  function tryRegister() {
    let inputUsername = document.getElementById("username").value;
    let inputPassword = document.getElementById("passwordInput1").value;
    let inputRepeatPassword = document.getElementById("passwordInput2").value;

    if (inputPassword != inputRepeatPassword) {
      console.log("fout bij aanmaken");
    } else {
      let newUserData = {
        username: inputUsername,
        password: inputPassword,
      };

      let request = new XMLHttpRequest();
      request.open("POST", "/user");
      request.setRequestHeader("Content-Type", "application/json");
      request.onreadystatechange = function () {
        if (request.readyState == request.DONE) {
          window.location.href = "/login";
        }
      };
      request.send(JSON.stringify(newUserData));
    }
  }

  visibleIcon = _id("visible");

  invisibleIcon = _id("invisible");

  pass1 = _id("passwordInput1");
  pass2 = _id("passwordInput2");

  passdd = _id("password-dropdown");
  userdd = _id("username-dropdown");
};

let validUsername = false;

function ComparePasswords() {
  let pass1Value = document.getElementById("passwordInput1").value;
  let pass2Value = document.getElementById("passwordInput2").value;

  if (checkPassword()) {
    if (pass1Value == pass2Value && validUsername) {
      registerButton.disabled = false;
      return true;
    } else {
      registerButton.disabled = true;
      return false;
    }
  } else {
    registerButton.disabled = true;
    return false;
  }
}

function _id(name) {
  return document.getElementById(name);
}

function _class(name) {
  return document.getElementsByClassName(name)[0];
}

function makeInvisible() {
  visibleIcon.style.display = "none";
  pass1.type = "text";
  if (pass2) {
    pass2.type = "text";
  }
  invisibleIcon.style.display = "block";
}

function makevisible() {
  visibleIcon.style.display = "block";
  pass1.type = "password";
  if (pass2) {
    pass2.type = "password";
  }
  invisibleIcon.style.display = "none";
}

function passDropdown() {
  passdd.style.height = "5.5em";
}

function erasePassDropdown() {
  passdd.style.height = "0em";
}

function userDropdown() {
  userdd.style.height = "2.5em";
}

function eraseUserDropdown() {
  userdd.style.height = "0em";
}

function checkPassword() {
  function _id(name) {
    return document.getElementById(name);
  }

  function _class(name) {
    return document.getElementsByClassName(name)[0];
  }

  let validPass = true;

  pw = pass1.value;
  caps = _id("capital");
  num = _id("number");
  special = _id("special");
  eight = _id("eight");

  if (/[A-Z]/.test(pw)) {
    caps.style.fontWeight = "bold";
    caps.style.color = "rgb(40,120,40)";
  } else {
    validPass = false;
    caps.style.fontWeight = "normal";
    caps.style.color = "rgb(120,40,40)";
  }

  if (/[0-9]/.test(pw)) {
    num.style.fontWeight = "bold";
    num.style.color = "rgb(40,120,40)";
  } else {
    validPass = false;
    num.style.fontWeight = "normal";
    num.style.color = "rgb(120,40,40)";
  }

  if (/[!@#$&*]/.test(pw)) {
    special.style.fontWeight = "bold";
    special.style.color = "rgb(40,120,40)";
  } else {
    validPass = false;
    special.style.fontWeight = "normal";
    special.style.color = "rgb(120,40,40)";
  }

  if (pw.length > 7) {
    eight.style.fontWeight = "bold";
    eight.style.color = "rgb(40,120,40)";
  } else {
    validPass = false;
    eight.style.fontWeight = "normal";
    eight.style.color = "rgb(120,40,40)";
  }
  return validPass;
}

function checkUsername() {
  let username = document.getElementById("username").value;
  let userLengthError = document.getElementById("username-error-length");
  let userTakenError = document.getElementById("username-error-taken");

  if (username.length < 3 || username.length > 13) {
    validUsername = false;
    userLengthError.style.display = "flex";
  } else {
    userLengthError.style.display = "none";
    let url = "/user/exists/" + encodeURI(username);

    fetch(url).then((inp) =>
      inp.json().then(function checkuser(exists) {
        if (exists) {
          userTakenError.style.display = "flex";
          validUsername = false;
        } else {
          userTakenError.style.display = "none";
          validUsername = true;
        }
        // ComparePasswords();
      })
    );
  }
}
